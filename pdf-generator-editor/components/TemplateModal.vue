<template>
  <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50" @click="close">
    <div class="bg-white rounded-lg shadow-xl w-full max-w-md max-h-90vh overflow-auto" @click.stop>
      <div class="flex justify-between items-center p-4 border-b border-gray-200">
        <h2 class="text-lg font-medium text-gray-900">Create New Template</h2>
        <button @click="close" class="p-2 rounded-full text-gray-500 hover:bg-gray-100 transition-colors">
          <NuxtIcon name="mdi:close" class="w-5 h-5" />
        </button>
      </div>

      <div class="p-6">
        <div class="mb-4">
          <label for="template-name" class="block text-sm font-medium text-gray-700 mb-1">Template Name</label>
          <input
              v-model="templateName"
              id="template-name"
              type="text"
              class="w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 text-sm"
              placeholder="Enter template name"
              @keyup.enter="createTemplate"
          />
        </div>

        <div class="mb-4">
          <label for="template-type" class="block text-sm font-medium text-gray-700 mb-1">Template Type</label>
          <div class="relative">
            <select
                v-model="templateType"
                id="template-type"
                class="w-full appearance-none bg-white border border-gray-300 rounded-md py-2 pl-3 pr-10 text-sm text-gray-700 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="full">Full Template (JSON + XSLT)</option>
              <option value="xslt">XSLT Only</option>
              <option value="json">JSON Only</option>
            </select>
            <div class="absolute inset-y-0 right-0 flex items-center px-2 pointer-events-none">
              <NuxtIcon name="mdi:chevron-down" class="w-5 h-5 text-gray-500" />
            </div>
          </div>
        </div>

        <div v-if="templateType !== 'xslt'" class="mb-4">
          <label for="base-template" class="block text-sm font-medium text-gray-700 mb-1">Base On</label>
          <div class="relative">
            <select
                v-model="baseType"
                id="base-template"
                class="w-full appearance-none bg-white border border-gray-300 rounded-md py-2 pl-3 pr-10 text-sm text-gray-700 focus:outline-none focus:ring-blue-500 focus:border-blue-500"
            >
              <option value="empty">Empty Template</option>
              <option value="current">Current Editor Content</option>
              <option v-for="template in templates" :key="template.id" :value="template.id">
                {{ template.name }}
              </option>
            </select>
            <div class="absolute inset-y-0 right-0 flex items-center px-2 pointer-events-none">
              <NuxtIcon name="mdi:chevron-down" class="w-5 h-5 text-gray-500" />
            </div>
          </div>
        </div>

        <div v-if="templateType === 'xslt'" class="p-3 bg-blue-50 border border-blue-200 rounded text-sm text-blue-800 mb-4">
          <NuxtIcon name="mdi:information-outline" class="inline-block align-text-bottom mr-1 w-5 h-5" />
          This will save your current XSLT as a reusable template that can be applied to any JSON data.
        </div>

        <div v-else-if="baseType === 'current'" class="p-3 bg-blue-50 border border-blue-200 rounded text-sm text-blue-800">
          <NuxtIcon name="mdi:information-outline" class="inline-block align-text-bottom mr-1 w-5 h-5" />
          This will create a new template using your current editor content.
        </div>
      </div>

      <div class="flex justify-end gap-2 p-4 border-t border-gray-200">
        <button
            @click="close"
            class="px-4 py-2 text-sm font-medium text-blue-600 hover:bg-blue-50 rounded-md transition-colors"
        >
          Cancel
        </button>
        <button
            @click="createTemplate"
            class="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-md transition-colors disabled:bg-gray-300 disabled:text-gray-500 disabled:cursor-not-allowed"
            :disabled="!templateName.trim()"
        >
          Create
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const props = defineProps({
  templates: {
    type: Array,
    default: () => []
  },
  currentContent: {
    type: Object,
    default: () => ({ json: '', xsl: '' })
  },
  defaultTemplateType: {
    type: String,
    default: 'full'
  },
  close: {
    type: Function,
    required: true
  }
})

const emit = defineEmits(['create'])

const templateName = ref('')
const baseType = ref('current') // Default to current content
const templateType = ref(props.defaultTemplateType) // Use the provided default type

// Initialize with props when component mounts
onMounted(() => {
  templateType.value = props.defaultTemplateType
})

const createTemplate = () => {
  if (!templateName.value.trim()) return

  emit('create', {
    name: templateName.value,
    baseType: baseType.value,
    templateType: templateType.value
  })
}
</script>