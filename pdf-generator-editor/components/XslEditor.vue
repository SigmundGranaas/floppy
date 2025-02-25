<template>
  <div class="h-full w-full">
    <Codemirror
        v-model="code"
        :style="{ height: '100%' }"
        :autofocus="true"
        :indent-with-tab="true"
        :tab-size="2"
        :extensions="extensions"
    />
  </div>
</template>

<script setup>
import { Codemirror } from 'vue-codemirror'
import { xml } from '@codemirror/lang-xml'
import { oneDark } from '@codemirror/theme-one-dark'
import { computed } from 'vue'

const props = defineProps({
  modelValue: {
    type: String,
    default: ''
  }
})

const emit = defineEmits(['update:modelValue'])

// Editor extensions
const extensions = [xml(), oneDark]

// Two-way binding
const code = computed({
  get: () => props.modelValue,
  set: (value) => emit('update:modelValue', value)
})
</script>