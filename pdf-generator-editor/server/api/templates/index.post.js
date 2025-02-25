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

// Create a new template
export default defineEventHandler(async (event) => {
    await ensureTemplatesDir()

    try {
        const body = await readBody(event)

        if (!body.name) {
            throw createError({
                statusCode: 400,
                statusMessage: 'Template name is required'
            })
        }

        // Generate a unique ID
        const templateId = `template_${Date.now()}`
        const templateData = {
            name: body.name,
            jsonData: body.jsonData || {},
            xslTemplate: body.xslTemplate || ''
        }

        // Save template
        const filePath = path.join(TEMPLATES_DIR, `${templateId}.json`)
        await fsPromises.writeFile(filePath, JSON.stringify(templateData, null, 2))

        return {
            id: templateId,
            name: templateData.name
        }
    } catch (error) {
        console.error('Error creating template:', error)
        throw createError({
            statusCode: error.statusCode || 500,
            statusMessage: error.statusMessage || 'Failed to create template'
        })
    }
})