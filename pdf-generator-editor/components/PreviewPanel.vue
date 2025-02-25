<template>
  <div :class="[
    'flex-1 bg-gray-50 overflow-auto relative flex flex-col',
    fullWidth ? 'w-full' : ''
  ]">
    <div v-if="error" class="flex justify-center p-6">
      <div class="flex items-start bg-red-50 border-l-4 border-red-500 rounded-md p-4 max-w-2xl shadow-sm">
        <NuxtIcon name="mdi:alert-circle" class="w-6 h-6 text-red-500 mr-4 flex-shrink-0" />
        <div>
          <h3 class="text-base font-medium text-red-800 mb-1">Error</h3>
          <p class="text-sm text-red-700">{{ error }}</p>
        </div>
      </div>
    </div>

    <div v-else-if="!pdfUrl" class="flex flex-col items-center justify-center h-full text-gray-500 p-6 text-center">
      <NuxtIcon name="mdi:file-document-outline" class="w-16 h-16 text-gray-300 mb-4" />
      <p class="text-base">Click "Generate PDF" to preview your document</p>
    </div>

    <ClientOnly v-else>
      <div class="flex-1 p-6 flex justify-center">
        <PdfViewer
            :url="pdfUrl"
            class="max-w-4xl w-full h-full shadow-md bg-white"
        />
      </div>
    </ClientOnly>
  </div>
</template>

<script setup>
defineProps({
  error: {
    type: String,
    default: ''
  },
  pdfUrl: {
    type: String,
    default: ''
  },
  fullWidth: {
    type: Boolean,
    default: false
  }
})
</script>