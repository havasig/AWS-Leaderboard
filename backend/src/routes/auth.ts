import { Router, Request, Response } from 'express'
import { register, login } from '../services/authService'

const router = Router()

router.post('/register', async (req: Request, res: Response) => {
    try {
        const { username, password } = req.body
        if (!username || !password) {
            res.status(400).json({ error: 'Username and password are required' })
            return
        }
        const user = await register(username, password)
        res.status(201).json({ message: 'User created', username: user.username })
    } catch (error) {
        res.status(500).json({ error: 'Something went wrong' })
    }
})

router.post('/login', async (req: Request, res: Response) => {
    try {
        const { username, password } = req.body
        if (!username || !password) {
            res.status(400).json({ error: 'Username and password are required' })
            return
        }
        const result = await login(username, password)
        res.status(200).json({ token: result.token })
    } catch (error) {
        res.status(401).json({ error: 'Invalid credentials' })
    }
})

export default router