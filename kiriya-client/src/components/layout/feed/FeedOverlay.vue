<template>
  <div ref="overlayRef" class="overlay-bg" @click.self="$emit('close')"
    :class="{
      'column-layout': !isRow,
      'row-layout': isRow,
    }"
  >
      <!-- 본문 -->
      <FeedOverlayContent
        :feed="feed"
        class="overlay-child"
        @toggleComments="showComments = !showComments"
        @toggleLike="$emit('toggleLike',feed)"
        @close="$emit('close')"
      />
      <!-- 댓글 -->
      <CommentPanel
        v-if="showComments"
        :feed="feed"
        class="overlay-child"
        @add-comment="handleAddComment"
        @close-comments="showComments=false"
      />
  </div>
</template>

<script setup>
import { ref, watch, onMounted, nextTick } from 'vue'
import FeedOverlayContent from '@/components/layout/feed/FeedOverlayContent.vue'
import CommentPanel from '@/components/layout/feed/CommentPanel.vue'

const overlayRef = ref(null)
const isRow = ref(false)

const contentRef = ref(null)
const commentRef = ref(null)

const updateLayout = () => {
  const el = overlayRef.value
  if (el) {
    const width = el.offsetWidth
    const height = el.offsetHeight
    const viewportHeight = window.innerHeight
    const isHeightFull = height / viewportHeight >= 0.85
    isRow.value = width <= height || isHeightFull

    // if (isRow.value && contentRef.value && commentRef.value) {
    //   const contentHeight = contentRef.value.offsetHeight
    //   commentRef.value.style.height = contentHeight + 'px'
    // } else if (commentRef.value) {
    //   commentRef.value.style.height = 'auto' 
    // }
  }
}

function handleAddComment() {
  props.feed.commentCount++
}

onMounted(async () => {
  await nextTick()
  updateLayout()
})


watch(() => isRow.value, () => {
  nextTick(() => updateLayout())
})

const props = defineProps(["feed"])


const showComments = ref(false)

</script>

<style scoped>
.overlay-bg {
  position: fixed; inset: 0; z-index: 2000;
  background: rgba(60,53,90,0.18);
  display: flex; justify-content: center;
}

.column-layout {
  flex-direction: column;
  align-items: center;
  width: auto;
}

.row-layout {
  flex-direction: row;
  align-items: center;
  height: auto;
}

.overlay-child {
  flex-shrink: 0;
}

.column-layout .overlay-child {
  width: 100%;
}

.row-layout .overlay-child {
  /* height: 100%; */
}
</style>
