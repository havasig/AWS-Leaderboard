import express from 'express'
import authRouter from './routes/auth'

const app = express()
const PORT = 3000

app.use(express.json())

app.use('/auth', authRouter)

app.get('/health', (req, res) => {
    res.json({ status: 'ok' })
})

app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`)
})