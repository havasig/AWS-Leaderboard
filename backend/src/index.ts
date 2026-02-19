import express from 'express'
import authRouter from './routes/auth'
import counterRouter from './routes/counterRouter'
import { config } from './config'

const app = express()

app.use(express.json())

app.use('/auth', authRouter)

app.get('/health', (req, res) => {
    res.json({ status: 'ok' })
})

app.listen(config.port, () => {
    console.log(`Server is running on port ${config.port}`)
})

app.use('/', counterRouter)
