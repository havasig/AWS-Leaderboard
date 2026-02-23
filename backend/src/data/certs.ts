export interface Material {
    id: string
    title: string
    type: 'video' | 'article' | 'practice_exam'
    url: string
}

export interface Cert {
    id: string
    title: string
    shortTitle: string
    materials: Material[]
}

export const certs: Cert[] = [
    {
        id: 'sa-associate',
        title: 'AWS Solutions Architect Associate',
        shortTitle: 'SAA',
        materials: [
            { id: 'saa-1', title: 'SAA Course', type: 'video', url: 'https://example.com' },
            { id: 'saa-2', title: 'SAA Practice Exam', type: 'practice_exam', url: 'https://example.com' },
            { id: 'saa-3', title: 'SAA Study Guide', type: 'article', url: 'https://example.com' },
        ]
    },
    {
        id: 'sa-pro',
        title: 'AWS Solutions Architect Professional',
        shortTitle: 'SAP',
        materials: [
            { id: 'sap-1', title: 'SAP Course', type: 'video', url: 'https://example.com' },
            { id: 'sap-2', title: 'SAP Practice Exam', type: 'practice_exam', url: 'https://example.com' },
            { id: 'sap-3', title: 'SAP Study Guide', type: 'article', url: 'https://example.com' },
        ]
    },
    {
        id: 'ml-specialty',
        title: 'AWS Machine Learning Specialty',
        shortTitle: 'MLS',
        materials: [
            { id: 'mls-1', title: 'MLS Course', type: 'video', url: 'https://example.com' },
            { id: 'mls-2', title: 'MLS Practice Exam', type: 'practice_exam', url: 'https://example.com' },
            { id: 'mls-3', title: 'MLS Study Guide', type: 'article', url: 'https://example.com' },
        ]
    },
    {
        id: 'genai-pro',
        title: 'AWS Generative AI Practitioner',
        shortTitle: 'GenAI',
        materials: [
            { id: 'genai-1', title: 'GenAI Course', type: 'video', url: 'https://example.com' },
            { id: 'genai-2', title: 'GenAI Practice Exam', type: 'practice_exam', url: 'https://example.com' },
            { id: 'genai-3', title: 'GenAI Study Guide', type: 'article', url: 'https://example.com' },
        ]
    }
]