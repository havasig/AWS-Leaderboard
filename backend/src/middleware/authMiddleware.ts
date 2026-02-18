import { Request, Response, NextFunction } from 'express'
import jwt from 'jsonwebtoken'
import { config } from '../config'

export interface AuthRequest extends Request {
    username?: string
}

export const authMiddleware = (req: AuthRequest, res: Response, next: NextFunction) => {
    const authHeader = req.headers.authorization
    if (!authHeader || !authHeader.startsWith('Bearer ')) {
        res.status(401).json({ error: 'No token provided' })
        return
    }

    const token = authHeader.split(' ')[1]!
    try {
        const decoded = jwt.verify(token, config.jwtSecret) as unknown as { username: string }
        req.username = decoded.username
        next()
    } catch {
        res.status(401).json({ error: 'Invalid token' })
    }
}