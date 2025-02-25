<template>
  <div class="h-screen flex flex-col bg-gray-50 text-gray-900">
    <!-- App Bar -->
    <AppBar
        :title="'PDF Generator'"
        :isPanelCollapsed="isPanelCollapsed"
        @toggle-panel="togglePanel"
    />

    <!-- Main content -->
    <div class="flex-1 flex overflow-hidden">
      <!-- Left pane: Editors -->
      <EditorPanel
          v-if="!isPanelCollapsed"
          :selectedTemplate="selectedTemplate"
          :templates="templates"
          :activeEditor="activeEditor"
          :jsonContent="jsonContent"
          :xslContent="xslContent"
          @update:jsonContent="jsonContent = $event"
          @update:xslContent="xslContent = $event"
          @update:activeEditor="activeEditor = $event"
          @update:selectedTemplate="selectedTemplate = $event"
          @select-template="loadSelectedTemplate"
          @save-template="saveTemplate"
          @create-template="showNewTemplateModal = true"
          @apply-xslt="applyXsltTemplate"
          @save-xslt="handleSaveXslt"
      />

      <!-- Right pane: PDF Preview -->
      <PreviewPanel
          :error="error"
          :pdfUrl="pdfUrl"
          :fullWidth="isPanelCollapsed"
      />
    </div>

    <!-- Bottom action bar -->
    <ActionBar
        :isGenerating="isGenerating"
        @generate-pdf="generatePdf"
    />

    <!-- New Template Modal -->
    <TemplateModal
        v-if="showNewTemplateModal"
        :templates="templates"
        :currentContent="{ json: jsonContent, xsl: xslContent }"
        :defaultTemplateType="newTemplateType"
        :close="closeTemplateModal"
        @create="createNewTemplate"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AppBar from '~/components/AppBar.vue'
import EditorPanel from '~/components/EditorPanel.vue'
import PreviewPanel from '~/components/PreviewPanel.vue'
import ActionBar from '~/components/ActionBar.vue'
import TemplateModal from '~/components/TemplateModal.vue'
import {watchDebounced} from "@vueuse/core";

// State variables
const activeEditor = ref('json')
const isPanelCollapsed = ref(false)
const pdfUrl = ref('')
const error = ref('')
const isGenerating = ref(false)

// Template management
const templates = ref([])
const selectedTemplate = ref('')
const showNewTemplateModal = ref(false)
const newTemplateType = ref('full') // Default template type for modal

// Editor content
const jsonContent = ref('')
const xslContent = ref('')

// Is the current content unsaved?
const hasUnsavedChanges = ref(false)
const originalContent = ref({ json: '', xsl: '' })

// Toggle panel collapse state
const togglePanel = () => {
  isPanelCollapsed.value = !isPanelCollapsed.value
}

// Close template modal and reset its state
const closeTemplateModal = () => {
  showNewTemplateModal.value = false
  newTemplateType.value = 'full' // Reset to default
}

// Empty template defaults
const emptyJsonContent = JSON.stringify({
  "cv": {
    "personalInfo": {
      "name": "John Smith",
      "title": "Senior Software Engineer",
      "contact": {
        "email": "john.smith@example.com",
        "phone": "+1 (555) 123-4567",
        "location": "San Francisco, CA"
      }
    },
    "profile": "Experienced software engineer with over 10 years of expertise in building scalable web applications and leading development teams. Passionate about clean code, efficient algorithms, and mentoring junior developers.",
    "experience": [
      {
        "position": "Senior Software Engineer",
        "company": "Tech Solutions Inc.",
        "period": "2018 - Present",
        "description": "Lead a team of 5 developers working on cloud-based enterprise applications. Improved system performance by 40% through architecture redesign.",
        "achievements": [
          "Redesigned authentication system reducing login times by 60%",
          "Implemented CI/CD pipeline reducing deployment time from days to hours",
          "Led migration from monolith to microservices architecture"
        ]
      },
      {
        "position": "Software Engineer",
        "company": "Web Dynamics Ltd.",
        "period": "2014 - 2018",
        "description": "Developed and maintained customer-facing web applications using JavaScript, React, and Node.js.",
        "achievements": [
          "Built responsive UI components used across 15+ projects",
          "Reduced API response time by 35% through caching optimization",
          "Mentored 3 junior developers who were later promoted"
        ]
      },
      {
        "position": "Junior Developer",
        "company": "StartUp Innovations",
        "period": "2012 - 2014",
        "description": "Worked on front-end development for early-stage startup.",
        "achievements": [
          "Implemented key features for company's flagship product",
          "Assisted in database design and optimization"
        ]
      }
    ],
    "education": [
      {
        "degree": "Master of Science in Computer Science",
        "institution": "Stanford University",
        "year": "2012"
      },
      {
        "degree": "Bachelor of Science in Computer Engineering",
        "institution": "University of California, Berkeley",
        "year": "2010"
      }
    ],
    "skills": [
      {
        "category": "Programming Languages",
        "items": ["JavaScript", "TypeScript", "Python", "Java", "C++"]
      },
      {
        "category": "Frameworks & Libraries",
        "items": ["React", "Node.js", "Express", "Django", "Spring Boot"]
      },
      {
        "category": "Tools & Platforms",
        "items": ["Git", "Docker", "Kubernetes", "AWS", "CI/CD", "Agile Methodologies"]
      }
    ],
    "languages": [
      {
        "name": "English",
        "proficiency": "Native"
      },
      {
        "name": "Spanish",
        "proficiency": "Fluent"
      },
      {
        "name": "French",
        "proficiency": "Intermediate"
      }
    ],
    "certifications": [
      {
        "name": "AWS Certified Solutions Architect",
        "issuer": "Amazon Web Services",
        "year": "2020"
      },
      {
        "name": "Certified Scrum Master",
        "issuer": "Scrum Alliance",
        "year": "2019"
      }
    ]
  }
}, null, 2)

const emptyXslContent = `<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
  xmlns:fo="http://www.w3.org/1999/XSL/Format">

  <!-- Main template -->
  <xsl:template match="/">
    <fo:root>
      <!-- Define the page layout -->
      <fo:layout-master-set>
        <fo:simple-page-master master-name="A4"
          page-width="210mm"
          page-height="297mm"
          margin-top="1cm"
          margin-bottom="1cm"
          margin-left="2cm"
          margin-right="2cm">
          <fo:region-body margin-top="1cm"/>
        </fo:simple-page-master>
      </fo:layout-master-set>

      <!-- Page content -->
      <fo:page-sequence master-reference="A4">
        <fo:flow flow-name="xsl-region-body">

          <!-- Header with name and title -->
          <fo:block font-size="24pt"
            font-weight="bold"
            color="#2c3e50"
            space-after="0.5cm">
            <xsl:value-of select="//personalInfo/name"/>
          </fo:block>

          <fo:block font-size="16pt"
            font-style="italic"
            color="#34495e"
            space-after="0.3cm">
            <xsl:value-of select="//personalInfo/title"/>
          </fo:block>

          <!-- Contact information -->
          <fo:block font-size="10pt"
            space-after="0.5cm">
            <xsl:value-of select="//personalInfo/contact/email"/> |
            <xsl:value-of select="//personalInfo/contact/phone"/> |
            <xsl:value-of select="//personalInfo/contact/location"/>
          </fo:block>

          <!-- Profile section -->
          <fo:block font-size="12pt"
            text-align="justify"
            space-after="0.8cm"
            line-height="1.4">
            <fo:block font-size="14pt"
              font-weight="bold"
              color="#2c3e50"
              border-bottom="1pt solid #bdc3c7"
              space-after="0.3cm">
              Profile
            </fo:block>
            <xsl:value-of select="//profile"/>
          </fo:block>

          <!-- Experience section -->
          <fo:block space-after="0.8cm">
            <fo:block font-size="14pt"
              font-weight="bold"
              color="#2c3e50"
              border-bottom="1pt solid #bdc3c7"
              space-after="0.3cm">
              Professional Experience
            </fo:block>

            <xsl:for-each select="//experience">
              <fo:block font-size="12pt"
                font-weight="bold"
                space-after="0.1cm">
                <xsl:value-of select="position"/> | <xsl:value-of select="company"/>
              </fo:block>

              <fo:block font-size="10pt"
                font-style="italic"
                space-after="0.2cm">
                <xsl:value-of select="period"/>
              </fo:block>

              <fo:block font-size="10pt"
                text-align="justify"
                space-after="0.2cm"
                line-height="1.3">
                <xsl:value-of select="description"/>
              </fo:block>

              <fo:list-block provisional-distance-between-starts="5mm"
                space-after="0.4cm">
                <xsl:for-each select="achievements/node()">
                  <fo:list-item>
                    <fo:list-item-label end-indent="label-end()">
                      <fo:block>•</fo:block>
                    </fo:list-item-label>
                    <fo:list-item-body start-indent="body-start()">
                      <fo:block font-size="10pt"><xsl:value-of select="."/></fo:block>
                    </fo:list-item-body>
                  </fo:list-item>
                </xsl:for-each>
              </fo:list-block>
            </xsl:for-each>
          </fo:block>

          <!-- Education section -->
          <fo:block space-after="0.8cm">
            <fo:block font-size="14pt"
              font-weight="bold"
              color="#2c3e50"
              border-bottom="1pt solid #bdc3c7"
              space-after="0.3cm">
              Education
            </fo:block>

            <xsl:for-each select="//education">
              <fo:block font-size="12pt"
                font-weight="bold"
                space-after="0.1cm">
                <xsl:value-of select="degree"/>
              </fo:block>

              <fo:block font-size="10pt"
                space-after="0.3cm">
                <xsl:value-of select="institution"/> | <xsl:value-of select="year"/>
              </fo:block>
            </xsl:for-each>
          </fo:block>

          <!-- Skills section -->
          <fo:block space-after="0.8cm">
            <fo:block font-size="14pt"
              font-weight="bold"
              color="#2c3e50"
              border-bottom="1pt solid #bdc3c7"
              space-after="0.3cm">
              Skills
            </fo:block>

            <xsl:for-each select="//skills">
              <fo:block font-size="11pt"
                font-weight="bold"
                space-after="0.1cm">
                <xsl:value-of select="category"/>:
              </fo:block>

              <fo:block font-size="10pt"
                space-after="0.3cm">
                <xsl:for-each select="items/node()">
                  <xsl:value-of select="."/>
                  <xsl:if test="position() != last()">, </xsl:if>
                </xsl:for-each>
              </fo:block>
            </xsl:for-each>
          </fo:block>

          <!-- Languages section -->
          <fo:block space-after="0.8cm">
            <fo:block font-size="14pt"
              font-weight="bold"
              color="#2c3e50"
              border-bottom="1pt solid #bdc3c7"
              space-after="0.3cm">
              Languages
            </fo:block>

            <fo:list-block provisional-distance-between-starts="5mm">
              <xsl:for-each select="//languages">
                <fo:list-item>
                  <fo:list-item-label end-indent="label-end()">
                    <fo:block>•</fo:block>
                  </fo:list-item-label>
                  <fo:list-item-body start-indent="body-start()">
                    <fo:block font-size="10pt">
                      <xsl:value-of select="name"/> - <xsl:value-of select="proficiency"/>
                    </fo:block>
                  </fo:list-item-body>
                </fo:list-item>
              </xsl:for-each>
            </fo:list-block>
          </fo:block>

          <!-- Certifications section -->
          <fo:block>
            <fo:block font-size="14pt"
              font-weight="bold"
              color="#2c3e50"
              border-bottom="1pt solid #bdc3c7"
              space-after="0.3cm">
              Certifications
            </fo:block>

            <xsl:for-each select="//certifications">
              <fo:block font-size="11pt"
                font-weight="bold"
                space-after="0.1cm">
                <xsl:value-of select="name"/>
              </fo:block>

              <fo:block font-size="10pt"
                space-after="0.3cm">
                <xsl:value-of select="issuer"/> | <xsl:value-of select="year"/>
              </fo:block>
            </xsl:for-each>
          </fo:block>

        </fo:flow>
      </fo:page-sequence>
    </fo:root>
  </xsl:template>
</xsl:stylesheet>`

// Load templates from the server
const fetchTemplates = async () => {
  try {
    const response = await fetch('/api/templates')
    if (!response.ok) {
      throw new Error('Failed to fetch templates')
    }

    templates.value = await response.json()

    // Select first template by default if available and none is selected
    if (templates.value.length > 0 && !selectedTemplate.value) {
      selectedTemplate.value = templates.value[0].id
      await loadSelectedTemplate()
    } else if (templates.value.length === 0 || !selectedTemplate.value) {
      // If no templates exist or none selected, use empty template
      jsonContent.value = emptyJsonContent
      xslContent.value = emptyXslContent
      originalContent.value = {
        json: emptyJsonContent,
        xsl: emptyXslContent
      }
    }
  } catch (e) {
    console.error('Error loading templates:', e)
    error.value = `Error loading templates: ${e.message}`

    // Use default content if templates can't be loaded
    jsonContent.value = emptyJsonContent
    xslContent.value = emptyXslContent
    originalContent.value = {
      json: emptyJsonContent,
      xsl: emptyXslContent
    }
  }
}

// Load a specific template by ID
const loadSelectedTemplate = async () => {
  if (!selectedTemplate.value) {
    jsonContent.value = emptyJsonContent
    xslContent.value = emptyXslContent
    originalContent.value = {
      json: emptyJsonContent,
      xsl: emptyXslContent
    }
    return
  }

  try {
    const response = await fetch(`/api/templates/${selectedTemplate.value}`)
    if (!response.ok) {
      throw new Error('Failed to load template')
    }

    const template = await response.json()

    // Handle different template types
    if (template.isXsltOnly) {
      // For XSLT-only templates, load the XSLT but keep the current JSON
      // or use empty JSON if there's none
      if (!jsonContent.value || jsonContent.value === '') {
        jsonContent.value = emptyJsonContent
      }
      xslContent.value = template.xslTemplate
    } else if (template.isJsonOnly) {
      // For JSON-only templates, load the JSON but keep the current XSLT
      // or use empty XSLT if there's none
      jsonContent.value = JSON.stringify(template.jsonData, null, 2)
      if (!xslContent.value || xslContent.value === '') {
        xslContent.value = emptyXslContent
      }
    } else {
      // For full templates, load both JSON and XSLT
      jsonContent.value = JSON.stringify(template.jsonData, null, 2)
      xslContent.value = template.xslTemplate
    }

    // Store original content for change detection
    originalContent.value = {
      json: jsonContent.value,
      xsl: xslContent.value
    }
    hasUnsavedChanges.value = false

    // Show a success toast when loading template
    showToast(`Template "${template.name}" loaded`)
  } catch (e) {
    console.error('Error loading template:', e)
    error.value = `Error loading template: ${e.message}`
  }
}

// Save current template
const saveTemplate = async () => {
  if (!selectedTemplate.value) {
    // If no template is selected, prompt to create a new one
    showNewTemplateModal.value = true
    return
  }

  try {
    const response = await fetch(`/api/templates/${selectedTemplate.value}`)
    if (!response.ok) {
      throw new Error('Template not found')
    }

    const template = await response.json()

    // Prepare the data to save based on template type
    let saveData = {}

    if (template.isXsltOnly) {
      // For XSLT-only templates, only update the XSLT
      saveData = {
        ...template,
        xslTemplate: xslContent.value
      }
    } else if (template.isJsonOnly) {
      // For JSON-only templates, only update the JSON
      saveData = {
        ...template,
        jsonData: JSON.parse(jsonContent.value)
      }
    } else {
      // For full templates, update both
      saveData = {
        ...template,
        jsonData: JSON.parse(jsonContent.value),
        xslTemplate: xslContent.value
      }
    }

    const saveResponse = await fetch(`/api/templates/${selectedTemplate.value}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(saveData)
    })

    if (!saveResponse.ok) {
      throw new Error('Failed to save template')
    }

    // Update original content after successful save
    originalContent.value = {
      json: jsonContent.value,
      xsl: xslContent.value
    }
    hasUnsavedChanges.value = false

    // Show success message
    showToast('Template saved successfully')

    // Refresh templates list
    await fetchTemplates()
  } catch (e) {
    console.error('Error saving template:', e)
    error.value = `Error saving template: ${e.message}`
  }
}

// Create a new template
const createNewTemplate = async (templateData) => {
  if (!templateData.name.trim()) {
    error.value = 'Template name is required'
    return
  }

  try {
    let newTemplateData = {
      name: templateData.name,
      jsonData: {},
      xslTemplate: ''
    }

    // Handle XSLT-only template creation
    if (templateData.templateType === 'xslt') {
      // For XSLT-only templates, we'll create a special template that just stores the XSLT
      // with minimal default JSON data
      newTemplateData.isXsltOnly = true
      newTemplateData.jsonData = {
        "__templateInfo": {
          "type": "xslt-only",
          "description": "This is an XSLT-only template"
        }
      }
      newTemplateData.xslTemplate = xslContent.value
    }
    // Handle JSON-only template creation
    else if (templateData.templateType === 'json') {
      try {
        newTemplateData.isJsonOnly = true

        // Determine JSON content based on base type
        if (templateData.baseType === 'empty') {
          newTemplateData.jsonData = JSON.parse(emptyJsonContent)
          newTemplateData.xslTemplate = '' // Store empty XSL
        } else if (templateData.baseType === 'current') {
          newTemplateData.jsonData = JSON.parse(jsonContent.value)
          newTemplateData.xslTemplate = '' // Store empty XSL
        } else {
          // Fetch the selected template to base on
          const response = await fetch(`/api/templates/${templateData.baseType}`)
          if (!response.ok) {
            throw new Error('Failed to load base template')
          }
          const baseTemplate = await response.json()
          newTemplateData.jsonData = baseTemplate.jsonData
          newTemplateData.xslTemplate = '' // Store empty XSL
        }
      } catch (parseError) {
        throw new Error(`Invalid JSON content: ${parseError.message}`)
      }
    }
    // Handle full template creation (both JSON and XSLT)
    else {
      // Determine content based on selected base
      if (templateData.baseType === 'empty') {
        newTemplateData.jsonData = JSON.parse(emptyJsonContent)
        newTemplateData.xslTemplate = emptyXslContent
      } else if (templateData.baseType === 'current') {
        try {
          newTemplateData.jsonData = JSON.parse(jsonContent.value)
          newTemplateData.xslTemplate = xslContent.value
        } catch (parseError) {
          throw new Error(`Invalid JSON content: ${parseError.message}`)
        }
      } else {
        // Fetch the selected template to base on
        const response = await fetch(`/api/templates/${templateData.baseType}`)
        if (!response.ok) {
          throw new Error('Failed to load base template')
        }
        const baseTemplate = await response.json()
        newTemplateData.jsonData = baseTemplate.jsonData
        newTemplateData.xslTemplate = baseTemplate.xslTemplate
      }
    }

    // Create the new template
    const createResponse = await fetch('/api/templates', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(newTemplateData)
    })

    if (!createResponse.ok) {
      throw new Error('Failed to create template')
    }

    // Show success message
    showToast(`Template "${templateData.name}" created successfully`)

    // Refresh templates and select the new one
    const newTemplate = await createResponse.json()
    await fetchTemplates()
    selectedTemplate.value = newTemplate.id
    await loadSelectedTemplate()

    // Close modal
    closeTemplateModal()
  } catch (e) {
    console.error('Error creating template:', e)
    error.value = `Error creating template: ${e.message}`
  }
}

// Apply an XSLT template to the current JSON data
const applyXsltTemplate = async (templateId) => {
  if (!templateId) return

  try {
    // Validate current JSON first
    try {
      JSON.parse(jsonContent.value)
    } catch (parseError) {
      throw new Error(`Cannot apply XSLT: Invalid JSON content: ${parseError.message}`)
    }

    const response = await fetch(`/api/templates/${templateId}`)
    if (!response.ok) {
      throw new Error('Failed to load XSLT template')
    }

    const template = await response.json()

    // Apply the template's XSLT to the current JSON
    xslContent.value = template.xslTemplate

    // Show success message
    showToast(`Applied XSLT template "${template.name}" to current JSON data`)

    // Update original content
    originalContent.value.xsl = xslContent.value

    // Generate preview with new XSLT
    await generatePdf()
  } catch (e) {
    console.error('Error applying XSLT template:', e)
    error.value = `Error applying XSLT template: ${e.message}`
  }
}

// Function to save current XSLT as a template
const saveCurrentXsltAsTemplate = () => {
  // Check if there's any XSLT content to save
  if (!xslContent.value.trim()) {
    error.value = 'No XSLT content to save'
    return
  }

  // Show modal with 'xslt' as the default template type
  showNewTemplateWithXslt()
}

// Function to show new template modal with XSLT pre-selected
const showNewTemplateWithXslt = () => {
  newTemplateType.value = 'xslt'
  showNewTemplateModal.value = true
}

// Handler for EditorPanel's save-xslt event
const handleSaveXslt = () => {
  saveCurrentXsltAsTemplate()
}

watchDebounced(
    jsonContent,
    () => generatePdf(),
    { debounce: 500, maxWait: 1000 },
)
// Generate PDF from current content
const generatePdf = async () => {
  error.value = ''
  isGenerating.value = true

  try {
    // Validate JSON before sending
    try {
      JSON.parse(jsonContent.value)
    } catch (parseError) {
      throw new Error(`Invalid JSON: ${parseError.message}`)
    }

    const response = await fetch('/api/generate-pdf', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        data: JSON.parse(jsonContent.value),
        xslTemplate: xslContent.value
      })
    })

    if (!response.ok) {
      const errorData = await response.text()
      throw new Error(`Failed to generate PDF: ${errorData}`)
    }

    // Create a blob from the PDF data
    const blob = await response.blob()
    // Create a URL for the blob
    if (pdfUrl.value) {
      URL.revokeObjectURL(pdfUrl.value)
    }
    pdfUrl.value = URL.createObjectURL(blob)
  } catch (e) {
    console.error('Error generating PDF:', e)
    error.value = e.message
  } finally {
    isGenerating.value = false
  }
}

// Simple toast notification
const showToast = (message) => {
  // This is a placeholder for a toast notification system
  // You could implement this with a third-party library or custom component
  console.log('Toast:', message)
  // For now, we'll just clear the error message to provide feedback
  error.value = ''
}

// Initialize
onMounted(async () => {
  await fetchTemplates()
})
</script>