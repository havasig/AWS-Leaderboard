import { Router, Response } from 'express'
import { authMiddleware, AuthRequest } from '../middleware/authMiddleware'
import { getCounter, incrementCounter, getLeaderboard } from '../services/counterService'

const router = Router()

router.use(authMiddleware)

router.get('/counter', async (req: AuthRequest, res: Response) => {
    try {
        const result = await getCounter(req.username!)
        res.status(200).json(result)
    } catch (error) {
        res.status(404).json({ error: 'User not found' })
    }
})

router.post('/counter/increment', async (req: AuthRequest, res: Response) => {
    try {
        const result = await incrementCounter(req.username!)
        res.status(200).json(result)
    } catch (error) {
        res.status(500).json({ error: 'Something went wrong' })
    }
})

router.get('/leaderboard', async (req: AuthRequest, res: Response) => {
    try {
        const result = await getLeaderboard()
        res.status(200).json(result)
    } catch (error) {
        res.status(500).json({ error: 'Something went wrong' })
    }
})

export default router