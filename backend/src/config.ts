import dotenv from 'dotenv'
dotenv.config()

if (!process.env.JWT_SECRET) {
    throw new Error('JWT_SECRET environment variable is not set')
}

if (!process.env.FIREBASE_SERVICE_ACCOUNT) {
    throw new Error('FIREBASE_SERVICE_ACCOUNT environment variable is not set')
}

export const config = {
    jwtSecret: process.env.JWT_SECRET,
    port: process.env.PORT || 3000
}