<template>
  <div class="flex flex-col h-full w-full bg-white rounded-md shadow-sm overflow-hidden">
    <!-- PDF Toolbar -->
    <div class="flex justify-between items-center px-4 py-2 bg-gray-50 border-b border-gray-200">
      <div class="flex items-center gap-2">
        <button
            @click="scrollToPage(currentPage - 1)"
            class="p-1.5 rounded-full hover:bg-gray-200 transition-colors"
            title="Previous page"
            :disabled="currentPage <= 1"
        >
          <NuxtIcon name="mdi:chevron-left" class="w-5 h-5 text-gray-600" />
        </button>
        <div class="flex items-center gap-1">
          <input
              v-model="pageInput"
              @blur="scrollToInputPage"
              @keyup.enter="scrollToInputPage"
              type="text"
              class="w-12 px-2 py-1 text-center bg-white border border-gray-300 rounded-md text-sm"
          />
          <span class="text-sm text-gray-600"> / {{ pageCount || '?' }}</span>
        </div>
        <button
            @click="scrollToPage(currentPage + 1)"
            class="p-1.5 rounded-full hover:bg-gray-200 transition-colors"
            title="Next page"
            :disabled="currentPage >= pageCount"
        >
          <NuxtIcon name="mdi:chevron-right" class="w-5 h-5 text-gray-600" />
        </button>
      </div>

      <div>
        <a
            :href="url"
            download="document.pdf"
            class="flex items-center gap-2 px-3 py-1.5 text-sm font-medium text-blue-600 hover:bg-blue-50 rounded-md transition-colors"
            title="Download PDF"
        >
          <NuxtIcon name="mdi:download" class="w-5 h-5" />
          Download
        </a>
      </div>
    </div>

    <!-- PDF Viewer with all pages -->
    <div
        ref="pdfContainer"
        class="flex-1 overflow-auto bg-gray-100 flex flex-col items-center p-4 gap-4"
        @scroll="updateCurrentPageFromScroll"
    >
      <div v-if="isLoading" class="flex items-center justify-center p-8 w-full h-32">
        <div class="text-gray-500">Loading PDF...</div>
      </div>

      <template v-if="!isLoading && pageCount > 0">
        <div
            v-for="page in pageCount"
            :key="page"
            :id="`page-${page}`"
            class="w-full max-w-4xl bg-white shadow-md relative"
        >
          <!-- Page number indicator -->
          <div class="absolute top-2 right-2 bg-gray-800 bg-opacity-50 text-white px-2 py-1 rounded text-xs">
            {{ page }} / {{ pageCount }}
          </div>

          <VuePdfEmbed
              :source="url"
              :page="page"
              @loaded="handlePageLoaded"
              @error="handlePageError(page)"
              class="w-full"
          />
        </div>
      </template>

      <div v-if="!isLoading && pageCount === 0" class="flex items-center justify-center p-8 w-full h-32">
        <div class="text-gray-500">No pages to display. The PDF may be invalid or empty.</div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import VuePdfEmbed from 'vue-pdf-embed'

const props = defineProps({
  url: {
    type: String,
    required: true
  }
})

// PDF state
const isLoading = ref(true)
const pageCount = ref(0)
const currentPage = ref(1)
const pageInput = ref('1')
const pdfContainer = ref(null)
const loadedPages = ref(0)

// Watch for URL changes to reset state
watch(() => props.url, () => {
  currentPage.value = 1
  pageInput.value = '1'
  pageCount.value = 0
  isLoading.value = true
  loadedPages.value = 0

  // Defer getting the PDF info to allow the component to reset
  setTimeout(() => {
    loadPdfInfo()
  }, 100)
})

// Watch for currentPage changes to update input
watch(currentPage, (newPage) => {
  pageInput.value = newPage.toString()
})

// Handle page loaded event
const handlePageLoaded = (pdf) => {
  // Only set the page count once from the first page that loads
  if (pageCount.value === 0 && pdf && pdf.numPages) {
    pageCount.value = pdf.numPages
  }

  loadedPages.value++
  if (loadedPages.value === 1) {
    isLoading.value = false
  }
}

// Load the PDF information
const loadPdfInfo = async () => {
  try {
    isLoading.value = true

    // We'll use the first page to get the total page count
    // The actual pages will be rendered by the v-for loop with VuePdfEmbed
    const pdfjsLib = window.pdfjsLib

    if (pdfjsLib) {
      const loadingTask = pdfjsLib.getDocument(props.url)
      const pdf = await loadingTask.promise

      pageCount.value = pdf.numPages
      isLoading.value = false
    } else {
      // If pdfjsLib is not available, we'll rely on the VuePdfEmbed component
      // to provide the page count through its 'loaded' event
      setTimeout(() => {
        // If after 5 seconds we still don't have a page count,
        // set isLoading to false to show any error messages
        if (pageCount.value === 0) {
          isLoading.value = false
        }
      }, 5000)
    }
  } catch (error) {
    console.error('Error loading PDF info:', error)
    isLoading.value = false
  }
}

// Determine which page is most visible in the viewport
const updateCurrentPageFromScroll = () => {
  if (!pdfContainer.value || pageCount.value === 0) return

  const containerRect = pdfContainer.value.getBoundingClientRect()
  let bestVisiblePage = currentPage.value
  let maxVisibleArea = 0

  for (let page = 1; page <= pageCount.value; page++) {
    const pageElement = document.getElementById(`page-${page}`)
    if (!pageElement) continue

    const pageRect = pageElement.getBoundingClientRect()

    // Check if the page is visible at all
    if (pageRect.bottom < containerRect.top || pageRect.top > containerRect.bottom) continue

    // Calculate visibility area
    const visibleHeight = Math.min(pageRect.bottom, containerRect.bottom) - Math.max(pageRect.top, containerRect.top)
    const visibleArea = visibleHeight * pageRect.width

    if (visibleArea > maxVisibleArea) {
      maxVisibleArea = visibleArea
      bestVisiblePage = page
    }
  }

  if (bestVisiblePage !== currentPage.value) {
    currentPage.value = bestVisiblePage
  }
}

// Scroll to a specific page
const scrollToPage = (page) => {
  if (page < 1 || page > pageCount.value) return

  const pageElement = document.getElementById(`page-${page}`)
  if (pageElement && pdfContainer.value) {
    pageElement.scrollIntoView({ behavior: 'smooth', block: 'start' })
    currentPage.value = page
  }
}

// Handle page input
const scrollToInputPage = () => {
  const pageNum = parseInt(pageInput.value, 10)
  if (!isNaN(pageNum) && pageNum >= 1 && pageNum <= pageCount.value) {
    scrollToPage(pageNum)
  } else {
    pageInput.value = currentPage.value.toString()
  }
}

// Handle page error
const handlePageError = (page) => {
  console.error(`Error loading page ${page}`)
  loadedPages.value++

  // If the first page fails to load, we still need to exit the loading state
  if (loadedPages.value === 1) {
    isLoading.value = false
  }
}

// Load PDF info on component mount
onMounted(() => {
  if (props.url) {
    loadPdfInfo()
  }
})
</script>

<style scoped>
/* Ensure pages have spacing between them */
.pdf-container > div {
  margin-bottom: 1rem;
}
</style>