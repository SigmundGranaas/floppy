<template>
  <div class="relative">
    <button
        @click="isOpen = !isOpen"
        class="flex items-center gap-1 text-sm text-gray-700 hover:bg-gray-100 rounded px-3 py-1.5 transition-colors"
        :class="{ 'bg-gray-100': isOpen }"
    >
      <NuxtIcon name="mdi:file-code-outline" class="w-5 h-5 text-gray-600" />
      Apply XSLT
      <NuxtIcon name="mdi:chevron-down" class="w-4 h-4 text-gray-500" />
    </button>

    <div
        v-if="isOpen"
        class="absolute left-0 top-full mt-1 w-64 bg-white shadow-lg rounded-md border border-gray-200 z-10 py-1"
        v-click-outside="() => isOpen = false"
    >
      <div v-if="xsltTemplates.length === 0" class="px-4 py-3 text-sm text-gray-500">
        No XSLT templates available
      </div>
      <template v-else>
        <div class="text-xs text-gray-500 px-4 py-1 uppercase tracking-wider">
          XSLT Templates
        </div>
        <div v-for="template in xsltTemplates" :key="template.id" class="px-1">
          <button
              @click="selectTemplate(template.id)"
              class="w-full text-left px-3 py-2 text-sm hover:bg-blue-50 hover:text-blue-700 rounded flex items-center justify-between"
          >
            <span>{{ template.name }}</span>
            <NuxtIcon name="mdi:check" v-if="isSelected(template.id)" class="w-4 h-4 text-blue-600" />
          </button>
        </div>
      </template>

      <div class="border-t border-gray-200 my-1 pt-1">
        <button
            @click="$emit('save-current')"
            class="w-full text-left px-3 py-2 text-sm hover:bg-blue-50 hover:text-blue-700 rounded flex items-center"
        >
          <NuxtIcon name="mdi:content-save" class="w-4 h-4 mr-2 text-gray-600" />
          Save current XSLT as template
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { onClickOutside } from '@vueuse/core'

const props = defineProps({
  templates: {
    type: Array,
    default: () => []
  },
  selectedTemplate: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['select', 'save-current'])

const isOpen = ref(false)

// Filter templates to show only XSLT-only templates
const xsltTemplates = computed(() => {
  return props.templates.filter(template => template.isXsltOnly)
})

const selectTemplate = (templateId) => {
  emit('select', templateId)
  isOpen.value = false
}

const isSelected = (templateId) => {
  return props.selectedTemplate === templateId
}

// Handle outside clicks
const vClickOutside = {
  mounted(el, binding) {
    onClickOutside(el, binding.value)
  }
}
</script>