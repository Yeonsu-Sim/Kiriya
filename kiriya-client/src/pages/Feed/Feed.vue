<template>
  <div class="feed-board-bg">
    <div class="feed-board-wrapper">
      <!-- 왼쪽 사이드바 -->
      <FeedSidebar class="sidebar" 
        :tags="allTags"
        :sortBy="sortBy.value"
        :selectedTags="selectedTags"
        @update:selectedTags="handleSelectTags"
        @showMyPosts="handleShowMyPosts"
        @showLikedPosts="handleShowLikedPosts"
        @showAllPosts="handleShowAllPosts"
        @toggleSort="toggleSort"
      />

      <!-- 메인 피드(Masonry) -->
      <div class="feed-main">
        <div class="masonry-grid">
          <FeedCard
            v-for="board in filteredBoards"
            :key="board.bno"
            :board="board"
            @showDetail="showDetail"
            @edit="editBoard"
            @delete="deleteBoard"
            @toggleLike="toggleLike"
          />
        </div>
        <FeedOverlay
          v-if="showOverlay"
          :feed="selectedFeed"
          @toggleLike="toggleLike"
          @close="showOverlay = false"
        />
      </div>

      <!-- 오버레이: 상세/댓글 보기용 (선택적) -->
      <!-- <FeedOverlay v-if="showOverlay" :feed="selectedFeed" @close="showOverlay = false"/> -->
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import FeedSidebar from '@/components/layout/feed/FeedSidebar.vue'
import FeedCard from '@/components/layout/feed/FeedCard.vue' 
import FeedOverlay from '@/components/layout/feed/FeedOverlay.vue'
import axios from 'axios'

const userStore = useUserStore()
const router = useRouter()
const loggedMno = computed(() => userStore.profile?.mno)
const boards = ref([])
const selectedTags = ref([])
const showOnlyMine = ref(false)
const showOverlay = ref(false)
const selectedFeed = ref(null)
const showOnlyLiked = ref(false)

const sortBy = ref('recent')
function toggleSort() {
  sortBy.value = sortBy.value === 'recent' ? 'popular' : 'recent'
}

onMounted(async () => {
  const { data } = await axios.get('http://localhost:8080/api/boards', {
    params: {mno: userStore.profile?.mno || 0}
  })
  boards.value = data.data.boardList
})

const filteredBoards = computed(() => {
  let filtered = boards.value
  if(selectedTags.value.length > 0){
    filtered = filtered.filter(board => (board.tags || []).some(tag => selectedTags.value.includes(tag)))
  }
  if(showOnlyMine.value && loggedMno.value){
    filtered = filtered.filter(board => board.mno === loggedMno.value)
  }

  if(showOnlyLiked.value && loggedMno.value) {
    filtered = filtered.filter(board => board.liked)
  }

  if(sortBy.value === 'popular') {
    filtered = filtered.slice().sort((a,b) => b.likes - a.likes)
  } else {
    filtered = filtered.slice().sort((a,b) => new Date(b.created_at) - new Date(a.created_at))
  }
  return filtered
})

function handleSelectTags(tags) {
  selectedTags.value = tags
  showOnlyMine.value = false
  showOnlyLiked.value = false
}

function handleShowMyPosts(){
  if (!userStore.profile || !userStore.profile.mno) {
    alert('로그인이 필요합니다!')
    return
  }
  showOnlyMine.value = true
  selectedTags.value = []
  showOnlyLiked.value = false
}

function handleShowAllPosts(){
  selectedTags.value = []
  showOnlyMine.value = false
  showOnlyLiked.value = false
}

function handleShowLikedPosts() {
  showOnlyLiked.value = true
  selectedTags.value = []
  showOnlyMine.value = false
}

const allTags = computed(() => {
  const tagSet = new Set()
  boards.value.forEach(board => (board.tags || []).forEach(t => tagSet.add(t)))
  return Array.from(tagSet)
})

function showDetail(feed) {
  selectedFeed.value = feed
  showOverlay.value = true
}

function editBoard(board){
  router.push(`/boards/edit/${board.bno}`)
}

async function deleteBoard(board) {
  if (!confirm('정말로 삭제하시겠습니까?')) return
  try {
    await axios.delete(`http://localhost:8080/api/boards/${board.bno}`)
    // 삭제 성공: boards에서 해당 글만 제거
    boards.value = boards.value.filter(b => b.bno !== board.bno)
    alert('삭제가 완료되었습니다.')
  } catch (e) {
    alert('삭제 중 오류가 발생했습니다.')
    console.error(e)
  }
}

async function toggleLike(board) {
  if (!userStore.isLoggedIn) {
    alert('로그인이 필요합니다!')
    return
  }
  const api = board.liked
    ? axios.delete(`http://localhost:8080/api/boards/${board.bno}/likes`)
    : axios.post(`http://localhost:8080/api/boards/${board.bno}/likes`)
  const { data } = await api
  board.liked = !board.liked
  board.likes = data.data.likesCnt
}
</script>

<style scoped>
.feed-board-bg {
  background: #f7f7fa;
  min-height: 100vh;
  padding: 0;
}
.feed-board-wrapper {
  display: flex;
  max-width: 1440px;
  margin: 0 auto;
  padding-top: 48px;
}
.sidebar {
  background-color: #fff;
  flex: 0 0 320px;
  margin-right: 20px;
}
.feed-main {
  flex: 1 1 0%;
  min-width: 0;
}
.masonry-grid {
  column-count: 3;           /* 원하는 열 개수(PC 3~4, 모바일 1) */
  column-gap: 10px;
}

.feed-card {
  break-inside: avoid;       /* 컬럼 내에서 카드 쪼개짐 방지 */
  margin-bottom: 24px;
  width: 100%;               /* Masonry에서는 개별 width는 100%로 */
  display: inline-block;     /* Masonry 레이아웃 핵심 */
}

@media (max-width: 1200px) {
  .masonry-grid { column-count: 2; }
}
@media (max-width: 800px) {
  .masonry-grid { column-count: 1; }
}

</style>
