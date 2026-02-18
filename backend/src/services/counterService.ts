import admin from 'firebase-admin'

const db = admin.firestore()
const usersCollection = db.collection('users')

export const getCounter = async (username: string) => {
    const doc = await usersCollection.doc(username).get()
    if (!doc.exists) throw new Error('User not found')
    return { counter: doc.data()!.counter }
}

export const incrementCounter = async (username: string) => {
    const ref = usersCollection.doc(username)
    await ref.update({ counter: admin.firestore.FieldValue.increment(1) })
    const updated = await ref.get()
    return { counter: updated.data()!.counter }
}

export const getLeaderboard = async () => {
    const snapshot = await usersCollection.orderBy('counter', 'desc').limit(10).get()
    return snapshot.docs.map(doc => ({
        username: doc.data().username,
        counter: doc.data().counter
    }))
}