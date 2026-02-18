import bcrypt from 'bcrypt'
import jwt from 'jsonwebtoken'

const JWT_SECRET = 'your-secret-key' // we'll move this to an env variable later

export const register = async (username: string, password: string) => {
    const passwordHash = await bcrypt.hash(password, 10)
    // TODO: save user to Firestore
    return { username, passwordHash }
}

export const login = async (username: string, password: string) => {
    // TODO: get user from Firestore
    const user = { username, passwordHash: '' } // placeholder

    const isValid = await bcrypt.compare(password, user.passwordHash)
    if (!isValid) throw new Error('Invalid credentials')

    const token = jwt.sign({ username }, JWT_SECRET, { expiresIn: '7d' })
    return { token }
}
