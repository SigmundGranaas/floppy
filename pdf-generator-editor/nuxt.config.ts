// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  modules: ['@nuxtjs/tailwindcss',     'nuxt-icon'],
  build: {
    transpile: ['vue-codemirror', '@codemirror/lang-json', '@codemirror/lang-xml', '@codemirror/theme-one-dark']
  },
  devtools: { enabled: true },
  compatibilityDate: '2025-02-16',
  ssr: false,
  tailwindcss: {
    config: {
      content: [],
      theme: {
        extend: {},
      },
      plugins: [],
    }
  }
})