import { defineEventHandler, readBody, setHeader, createError } from 'h3'

export default defineEventHandler(async (event) => {
    try {
        const body = await readBody(event)

        const response = await fetch('http://localhost:8080/api/v1/general/pdf/custom-template', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body)
        })

        if (!response.ok) {
            const errorText = await response.text()
            let errorMessage = 'Failed to generate PDF'

            try {
                // Try to parse as JSON if possible
                const errorData = JSON.parse(errorText)
                errorMessage = errorData.message || errorMessage
            } catch (e) {
                // If it's not valid JSON, use the raw text (truncated if too long)
                errorMessage = errorText.substring(0, 200)
            }

            throw createError({
                statusCode: response.status,
                message: errorMessage
            })
        }

        // Get the PDF data
        const pdfArrayBuffer = await response.arrayBuffer()

        // Convert to Uint8Array for proper handling
        const pdfBytes = new Uint8Array(pdfArrayBuffer)

        // Set appropriate headers for PDF response
        setHeader(event, 'Content-Type', 'application/pdf')
        setHeader(event, 'Content-Disposition', 'inline; filename="document.pdf"')
        setHeader(event, 'Content-Length', pdfBytes.length.toString())

        // Return the PDF bytes
        return pdfBytes
    } catch (error) {
        console.error('PDF generation error:', error)

        // If it's already a h3 error, rethrow it
        if (error.statusCode) {
            throw error
        }

        // Otherwise create a new error
        throw createError({
            statusCode: 500,
            message: error.message || 'Internal server error'
        })
    }
})