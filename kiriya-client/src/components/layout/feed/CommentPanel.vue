<template>
  <div :class="['comment-panel', layout]">
    <div class="panel-header d-flex justify-space-between">
      <h3>댓글 ({{ comments?.length || 0 }})</h3>
      <button class="close-btn" @click="$emit('close-comments')">×</button>
    </div>
    <form @submit.prevent="addComment" class="comment-form">
      <input v-model="commentInput" placeholder="댓글을 입력하세요..." />
      <button type="submit">등록</button>
    </form>
    <div class="comment-list">
      <div v-for="comment in comments" :key="comment.cno" class="comment-item">
        <img :src="comment.authorProfileUrl || defaultProfile" class="comment-profile"/>
        <div>
          <span class="comment-author">{{ comment.author }}</span>
          <span class="comment-date">{{ timeAgo(comment.createdAt) }}</span>
          <div class="comment-content">{{ comment.content }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue'
import defaultProfile from '@/assets/default-profile.png'
import axios from 'axios'

import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
dayjs.extend(relativeTime)
function timeAgo(date) { return dayjs(date).fromNow() }

const props = defineProps(['feed', 'layout'])
const emit = defineEmits(['add-comment', 'close-comments'])

const comments = ref([])
const commentInput = ref('')
async function addComment() {
  // 실제로는 axios POST 후, emit 또는 refetch 필요
  if (!commentInput.value.trim()) return
  try {
    // 1. 댓글 등록 API 호출
    await axios.post(
      `http://localhost:8080/api/boards/${props.feed.bno}/comments`,
      { content: commentInput.value }
      // (JWT 인증 필요시 withCredentials 등 옵션 추가)
    )
    // 2. 등록 성공 → 댓글 목록 갱신
    await fetchComments() // 아래 함수로 새로 불러오기
    commentInput.value = ''
    emit('add-comment')
  } catch (e) {
    alert('댓글 등록 실패')
  }
}

// 댓글 목록 불러오기 함수 예시
async function fetchComments() {
  const { data } = await axios.get(
    `http://localhost:8080/api/boards/${props.feed.bno}/comments`
  )
  comments.value = data.data || data
  console.log(comments.value)
}

watch(() => props.feed.bno, fetchComments, {immediate: true})
onMounted(() => {
    fetchComments()
})
</script>

<style scoped>
.comment-panel {
  background: #fff;
  border-radius: 14px;
  box-shadow: 0 2px 18px rgba(120,120,160,.09);
  padding: 18px 20px 12px 20px;
  overflow-y: auto;
  font-family: system-ui, "Apple SD Gothic Neo", "맑은 고딕", Arial, sans-serif !important;
  z-index: 1112;
  align-self: auto;
  height: auto;
  min-height: 480px;
}

.panel-header {
  display: flex; justify-content: space-between; align-items: center;
  /* font-weight: bold; */
 font-size: 16px; margin-bottom: 10px;
}
.close-btn {
  background: none; border: none; font-size: 32px; cursor: pointer;
  color: #b6a3e9;
  transition: color 0.18s;
}
.close-btn:hover { color: #8569c6; }
.comment-list {
  flex: 1; overflow-y: auto; margin-bottom: 10px;
}
.comment-item { display: flex; align-items: flex-start; gap: 9px; margin-bottom: 8px;}
.comment-profile { width: 33px; height: 33px; border-radius: 50%; object-fit: cover; }
.comment-author { font-weight: 500; font-size: 14px; color: #533a95;}
.comment-date { font-size: 11px; color: #b5b5cc; margin-left: 7px;}
.comment-content { font-size: 15px; margin-top: 2px; color: #353350;}
.comment-form { display: flex; gap: 8px; padding-bottom: 12px; }
.comment-form input { flex:1; border-radius: 8px; border: 1px solid #ece7ff; padding: 6px 9px;}
.comment-form button { border-radius: 8px; background: #bca7fa; border:none; color:#fff; padding: 6px 14px;}
</style>
