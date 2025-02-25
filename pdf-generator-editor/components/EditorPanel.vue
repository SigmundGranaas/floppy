<template>
  <div class="w-1/2 flex flex-col bg-white border-r border-gray-200">
    <!-- Template selector toolbar -->
    <div class="flex items-center justify-between px-4 py-2 bg-gray-50 border-b border-gray-200">
      <div class="flex items-center gap-2">
        <label for="template-select" class="text-sm text-gray-600">Template:</label>
        <div class="relative">
          <select
              id="template-select"
              v-model="selectedTemplateLocal"
              @change="$emit('select-template')"
              class="appearance-none bg-white border border-gray-300 rounded-md py-2 pl-3 pr-8 text-sm text-gray-700 focus:outline-none focus:ring-1 focus:ring-blue-500 focus:border-blue-500"
              :class="{ 'opacity-50': templates.length === 0 }"
              :disabled="templates.length === 0"
          >
            <option v-if="templates.length === 0" value="">No templates available</option>
            <option v-else-if="!selectedTemplateLocal" value="">Select a template</option>
            <option v-for="template in templates" :key="template.id" :value="template.id">
              {{ template.name }}
              <template v-if="template.isXsltOnly">(XSLT Only)</template>
              <template v-if="template.isJsonOnly">(JSON Only)</template>
            </option>
          </select>
          <div class="absolute inset-y-0 right-0 flex items-center px-2 pointer-events-none">
            <NuxtIcon name="mdi:chevron-down" class="w-5 h-5 text-gray-500" />
          </div>
        </div>
      </div>

      <div class="flex gap-2">
        <button
            @click="handleSaveTemplate"
            class="p-2 rounded-full hover:bg-gray-200 transition-colors"
            title="Save template"
        >
          <NuxtIcon
              :name="selectedTemplateLocal ? 'mdi:content-save' : 'mdi:content-save-plus'"
              class="w-5 h-5 text-gray-600"
          />
        </button>
        <button
            @click="$emit('create-template')"
            class="p-2 rounded-full hover:bg-gray-200 transition-colors"
            title="Create new template"
        >
          <NuxtIcon name="mdi:plus-circle" class="w-5 h-5 text-gray-600" />
        </button>
      </div>
    </div>

    <!-- Editor tabs -->
    <div class="flex bg-gray-50 border-b border-gray-200">
      <button
          @click="setActiveEditor('json')"
          class="flex items-center gap-2 px-4 py-3 text-sm border-b-2 transition-colors focus:outline-none"
          :class="activeEditorLocal === 'json'
          ? 'text-blue-600 border-blue-600'
          : 'text-gray-600 border-transparent hover:bg-gray-100'"
      >
        <NuxtIcon name="mdi:code-json" class="w-5 h-5" />
        JSON Editor
      </button>
      <button
          @click="setActiveEditor('xsl')"
          class="flex items-center gap-2 px-4 py-3 text-sm border-b-2 transition-colors focus:outline-none"
          :class="activeEditorLocal === 'xsl'
          ? 'text-blue-600 border-blue-600'
          : 'text-gray-600 border-transparent hover:bg-gray-100'"
      >
        <NuxtIcon name="mdi:xml" class="w-5 h-5" />
        XSL Editor
      </button>

      <!-- XSLT Templates Dropdown (visible only in XSL editor tab) -->
      <div v-if="activeEditorLocal === 'xsl'" class="ml-auto mr-2 flex items-center">
        <XsltTemplateDropdown
            :templates="templates"
            :selectedTemplate="selectedTemplate"
            @select="$emit('apply-xslt', $event)"
            @save-current="saveCurrentXslt"
        />
      </div>
    </div>

    <!-- Editor content -->
    <div class="flex-1 relative overflow-hidden">
      <JsonEditor
          v-if="activeEditorLocal === 'json'"
          v-model="jsonContentLocal"
          class="absolute inset-0"
      />
      <XslEditor
          v-else
          v-model="xslContentLocal"
          class="absolute inset-0"
      />
    </div>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import XsltTemplateDropdown from '~/components/XsltTemplateDropdown.vue'

const props = defineProps({
  templates: {
    type: Array,
    default: () => []
  },
  selectedTemplate: {
    type: String,
    default: ''
  },
  activeEditor: {
    type: String,
    default: 'json'
  },
  jsonContent: {
    type: String,
    default: ''
  },
  xslContent: {
    type: String,
    default: ''
  }
})

const emit = defineEmits([
  'update:jsonContent',
  'update:xslContent',
  'update:activeEditor',
  'update:selectedTemplate',
  'select-template',
  'save-template',
  'create-template',
  'apply-xslt',
  'save-xslt'
])

// Local state with two-way binding
const selectedTemplateLocal = computed({
  get: () => props.selectedTemplate,
  set: (value) => {
    emit('update:selectedTemplate', value)
  }
})

const activeEditorLocal = computed({
  get: () => props.activeEditor,
  set: (value) => emit('update:activeEditor', value)
})

const jsonContentLocal = computed({
  get: () => props.jsonContent,
  set: (value) => emit('update:jsonContent', value)
})

const xslContentLocal = computed({
  get: () => props.xslContent,
  set: (value) => emit('update:xslContent', value)
})

// Methods
const setActiveEditor = (editor) => {
  activeEditorLocal.value = editor
}

const handleSaveTemplate = () => {
  // If no template is currently selected, create a new one
  if (!selectedTemplateLocal.value) {
    emit('create-template')
  } else {
    // Otherwise save the current one
    emit('save-template')
  }
}

const saveCurrentXslt = () => {
  // Save current XSLT as a template
  emit('save-xslt')
}
</script>