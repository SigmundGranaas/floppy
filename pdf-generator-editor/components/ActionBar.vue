<template>
  <footer class="flex justify-center p-4 bg-white border-t border-gray-200 shadow-sm z-5">
    <button
        @click="$emit('generate-pdf')"
        :disabled="isGenerating"
        class="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 active:bg-blue-800 disabled:bg-gray-300 text-white disabled:text-gray-500 text-sm font-medium px-6 py-2 rounded-full transition-colors shadow-sm disabled:shadow-none disabled:cursor-not-allowed"
    >
      <Icon name="mdi:baseline-feed" class="w-5 h-5" />
      {{ isGenerating ? 'Generating...' : 'Generate PDF' }}
    </button>
  </footer>
</template>

<script setup>
defineProps({
  isGenerating: {
    type: Boolean,
    default: false
  }
})

defineEmits(['generate-pdf'])
</script>