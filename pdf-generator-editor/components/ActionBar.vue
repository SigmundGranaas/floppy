<template>
  <footer class="flex justify-center p-4 bg-white border-t border-gray-100 z-10">
    <button
        @click="$emit('generate-pdf')"
        :disabled="isGenerating"
        class="flex items-center gap-3 bg-blue-600 hover:bg-blue-700 active:bg-blue-800 disabled:bg-gray-200 text-white disabled:text-gray-400 text-sm font-medium px-6 py-2.5 rounded-full transition-colors shadow-md disabled:shadow-none disabled:cursor-not-allowed"
    >
      <Icon name="material-symbols:description-rounded" class="w-5 h-5" />
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