<template>
  <header class="flex items-center justify-between px-4 h-16 bg-white text-gray-800 shadow-sm z-10">
    <div class="flex items-center gap-4">
      <button @click="$emit('toggle-panel')" class="p-2 rounded-full hover:bg-gray-100 transition-colors">
        <NuxtIcon
            :name="isPanelCollapsed ? 'mdi:menu' : 'mdi:menu-open'"
            class="w-6 h-6 text-gray-600"
        />
      </button>
      <h1 class="text-xl font-normal">{{ title }}</h1>
    </div>
    <div class="flex items-center gap-4">
      <button class="p-2 rounded-full hover:bg-gray-100 transition-colors" title="Help">
        <NuxtIcon name="mdi:help-circle-outline" class="w-6 h-6 text-gray-600" />
      </button>
      <button class="p-2 rounded-full hover:bg-gray-100 transition-colors" title="Settings">
        <NuxtIcon name="mdi:cog" class="w-6 h-6 text-gray-600" />
      </button>
    </div>
  </header>
</template>

<script setup>
defineProps({
  title: {
    type: String,
    required: true
  },
  isPanelCollapsed: {
    type: Boolean,
    default: false
  }
})

defineEmits(['toggle-panel'])
</script>