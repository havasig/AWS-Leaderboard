import express from 'express'
import authRouter from './routes/authRouter'
import { config } from './config'
import certRouter from './routes/certRouter'

const app = express()

// Trigger deploy

app.use(express.json())

app.use('/auth', authRouter)

app.get('/health', (req, res) => {
    res.json({ status: 'ok' })
})

app.listen(config.port, () => {
    console.log(`Server is running on port ${config.port}`)
})

app.use('/', certRouter)
