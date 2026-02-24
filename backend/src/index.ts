import express from 'express'
import authRouter from './routes/authRouter'
import { config } from './config'
import certRouter from './routes/certRouter'
import morgan from 'morgan'

const app = express()

app.use(morgan(process.env.NODE_ENV === 'production' ? 'combined' : 'dev'))

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
