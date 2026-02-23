import { Router, Response } from 'express'
import { authMiddleware, AuthRequest } from '../middleware/authMiddleware'
import { getCerts, getUserProgress, completeMaterial, getLeaderboard } from '../services/certService'

const router = Router()

router.use(authMiddleware)

router.get('/certs', async (req: AuthRequest, res: Response) => {
    try {
        const result = getCerts()
        res.status(200).json(result)
    } catch (error) {
        res.status(500).json({ error: 'Something went wrong' })
    }
})

router.get('/certs/progress', async (req: AuthRequest, res: Response) => {
    try {
        const result = await getUserProgress(req.username!)
        res.status(200).json(result)
    } catch (error) {
        res.status(500).json({ error: 'Something went wrong' })
    }
})

router.get('/users/:username/progress', async (req: AuthRequest, res: Response) => {
    try {
        const username = req.params.username as string
        const result = await getUserProgress(username)
        res.status(200).json(result)
    } catch (error) {
        res.status(500).json({ error: 'Something went wrong' })
    }
})

router.post('/certs/:certId/materials/:materialId/complete', async (req: AuthRequest, res: Response) => {
    try {
        const result = await completeMaterial(req.username!, req.params.certId as string, req.params.materialId as string)
        res.status(200).json(result)
    } catch (error) {
        res.status(400).json({ error: 'Invalid cert or material' })
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