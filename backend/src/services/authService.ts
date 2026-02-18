import bcrypt from 'bcrypt'
import jwt from 'jsonwebtoken'
import admin from 'firebase-admin'
import serviceAccount from '../../serviceAccount.json'
import { config } from '../config'

if (!admin.apps.length) {
    admin.initializeApp({
        credential: admin.credential.cert(serviceAccount as admin.ServiceAccount)
    })
}

const db = admin.firestore()
const usersCollection = db.collection('users')

export const register = async (username: string, password: string) => {
    const existing = await usersCollection.doc(username).get()
    if (existing.exists) throw new Error('User already exists')

    const passwordHash = await bcrypt.hash(password, 10)
    await usersCollection.doc(username).set({ username, passwordHash, counter: 0 })
    return { username }
}

export const login = async (username: string, password: string) => {
    const doc = await usersCollection.doc(username).get()
    if (!doc.exists) throw new Error('Invalid credentials')

    const user = doc.data()!
    const isValid = await bcrypt.compare(password, user.passwordHash)
    if (!isValid) throw new Error('Invalid credentials')

    const token = jwt.sign({ username }, config.jwtSecret, { expiresIn: '7d' })
    return { token }
}
