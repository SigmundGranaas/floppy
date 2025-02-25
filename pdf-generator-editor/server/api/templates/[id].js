import fs from 'fs'
import path from 'path'
import { promises as fsPromises } from 'fs'

const TEMPLATES_DIR = path.resolve(process.cwd(), 'templates')

// Get a specific template
export default defineEventHandler(async (event) => {
    const id = event.context.params.id
    const filePath = path.join(TEMPLATES_DIR, `${id}.json`)

    try {
        const content = await fsPromises.readFile(filePath, 'utf8')
        return JSON.parse(content)
    } catch (error) {
        console.error(`Error retrieving template ${id}:`, error)
        throw createError({
            statusCode: 404,
            statusMessage: 'Template not found'
        })
    }
})

// Update a template
export const put = defineEventHandler(async (event) => {
    const id = event.context.params.id
    const filePath = path.join(TEMPLATES_DIR, `${id}.json`)

    try {
        // Check if template exists
        await fsPromises.access(filePath)

        // Get current template data to preserve the name
        const currentContent = await fsPromises.readFile(filePath, 'utf8')
        const currentTemplate = JSON.parse(currentContent)

        // Update template with new data
        const body = await readBody(event)
        const templateData = {
            name: currentTemplate.name, // Preserve the name
            jsonData: body.jsonData || currentTemplate.jsonData,
            xslTemplate: body.xslTemplate || currentTemplate.xslTemplate
        }

        // Save updated template
        await fsPromises.writeFile(filePath, JSON.stringify(templateData, null, 2))

        return {
            id,
            name: templateData.name
        }
    } catch (error) {
        console.error(`Error updating template ${id}:`, error)
        if (error.code === 'ENOENT') {
            throw createError({
                statusCode: 404,
                statusMessage: 'Template not found'
            })
        }
        throw createError({
            statusCode: 500,
            statusMessage: 'Failed to update template'
        })
    }
})

// Delete a template
export const del = defineEventHandler(async (event) => {
    const id = event.context.params.id
    const filePath = path.join(TEMPLATES_DIR, `${id}.json`)

    try {
        // Check if template exists
        await fsPromises.access(filePath)

        // Delete the template file
        await fsPromises.unlink(filePath)

        return { success: true }
    } catch (error) {
        console.error(`Error deleting template ${id}:`, error)
        if (error.code === 'ENOENT') {
            throw createError({
                statusCode: 404,
                statusMessage: 'Template not found'
            })
        }
        throw createError({
            statusCode: 500,
            statusMessage: 'Failed to delete template'
        })
    }
})