import admin from 'firebase-admin'
import { certs } from '../data/certs'

const db = admin.firestore()
const progressCollection = db.collection('progress')

export const getCerts = () => {
    return certs
}

export const getUserProgress = async (username: string) => {
    const snapshot = await progressCollection
        .where('username', '==', username)
        .get()

    const completedMaterials = snapshot.docs.map(doc => doc.data().materialId)

    return certs.map(cert => {
        const completedInCert = cert.materials.filter(m => 
            completedMaterials.includes(m.id)
        ).length
        const totalMaterials = cert.materials.length
        const certified = completedInCert === totalMaterials

        return {
            certId: cert.id,
            title: cert.title,
            shortTitle: cert.shortTitle,
            completedMaterials: completedInCert,
            totalMaterials,
            certified,
            materials: cert.materials.map(m => ({
                ...m,
                completed: completedMaterials.includes(m.id)
            }))
        }
    })
}

export const completeMaterial = async (username: string, certId: string, materialId: string) => {
    const cert = certs.find(c => c.id === certId)
    if (!cert) throw new Error('Cert not found')

    const material = cert.materials.find(m => m.id === materialId)
    if (!material) throw new Error('Material not found')

    const docId = `${username}_${materialId}`
    await progressCollection.doc(docId).set({ username, certId, materialId, completed: true })

    return { success: true }
}

export const getLeaderboard = async () => {
    const snapshot = await progressCollection.get()

    const userProgress: Record<string, { completedMaterials: number, certIds: Set<string> }> = {}

    snapshot.docs.forEach(doc => {
        const { username, certId, materialId } = doc.data()
        if (!userProgress[username]) {
            userProgress[username] = { completedMaterials: 0, certIds: new Set() }
        }
        userProgress[username].completedMaterials++

        const cert = certs.find(c => c.id === certId)
        if (cert) {
            const allCompleted = cert.materials.every(m =>
                snapshot.docs.some(d => d.data().username === username && d.data().materialId === m.id)
            )
            if (allCompleted) userProgress[username].certIds.add(certId)
        }
    })

    return Object.entries(userProgress)
        .map(([username, data]) => ({
            username,
            certsEarned: data.certIds.size,
            materialsCompleted: data.completedMaterials
        }))
        .sort((a, b) => b.certsEarned - a.certsEarned || b.materialsCompleted - a.materialsCompleted)
}