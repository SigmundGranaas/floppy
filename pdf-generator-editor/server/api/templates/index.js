import path from 'path'
import { promises as fsPromises } from 'fs'

const TEMPLATES_DIR = path.resolve(process.cwd(), 'templates')

// Ensure templates directory exists
const ensureTemplatesDir = async () => {
    try {
        await fsPromises.mkdir(TEMPLATES_DIR, { recursive: true })
    } catch (error) {
        console.error('Error creating templates directory:', error)
    }
}

// Get list of all templates
export default defineEventHandler(async (event) => {
    await ensureTemplatesDir()

    try {
        const files = await fsPromises.readdir(TEMPLATES_DIR)
        const templateFiles = files.filter(file => file.endsWith('.json'))

        const templates = []
        for (const file of templateFiles) {
            const filePath = path.join(TEMPLATES_DIR, file)
            const content = await fsPromises.readFile(filePath, 'utf8')
            const template = JSON.parse(content)
            templates.push({
                id: path.basename(file, '.json'),
                name: template.name
            })
        }

        return templates
    } catch (error) {
        console.error('Error retrieving templates:', error)
        throw createError({
            statusCode: 500,
            statusMessage: 'Failed to retrieve templates'
        })
    }
})