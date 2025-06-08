<template>
  <div class="sidebar-wrap">
    <div class="mt-6">
      <button class="sidebar-btn sidebar-write-btn" @click="goToBoardWrite">
        내 피드 올리기
      </button>
      <div class="row-btns">
        <button
          class="sidebar-btn sidebar-sub-btn"
          @click="$emit('showMyPosts')"
        >
          내 피드만
        </button>
        <button
          class="sidebar-btn sidebar-sub-btn"
          @click="$emit('showLikedPosts')"
        >
          ♥ 피드만
        </button>
      </div>
      <div class="row-btns">
        <button
        class="sidebar-btn sidebar-sub-btn"
        @click="onClickSortToggle">
          <span>인기글 순</span>
        </button>
      <button
          class="sidebar-btn sidebar-sub-btn"
          @click="$emit('showAllPosts')"
        >
          전체 피드
        </button>
      </div>
    </div>
    <div class="sidebar-section">
      <div class="sidebar-title">태그로 보기</div>
      <div class="tag-list">
        <v-chip
          v-for="tag in tags"
          :key="tag"
          :class="{ 'tag-chip': true, selected: selectedTags.includes(tag) }"
          color="primary"
          variant="outlined"
          @click="toggleTag(tag)"
        >
          #{{ tag }}
        </v-chip>
      </div>
    </div>
  </div>
</template>
  <script setup>
import { computed } from 'vue'
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/user";
const router = useRouter()
const userStore = useUserStore()
function goToBoardWrite() {
  if (!userStore.profile || !userStore.profile.mno) {
    alert('로그인이 필요합니다.')
    router.push('/login')
    return
  }
  router.push({ name: "BoardWrite" });
}

const props = defineProps({
  tags: { type: Array, default: () => [] },
  selectedTags: { type: Array, default: () => [] },
  sortBy: String
});

const emit = defineEmits([
  "update:selectedTags",
  "showMyPosts",
  "showAllPosts",
  "showLikedPosts",
  "toggleSort",
]);

function toggleTag(tag) {
  const selected = props.selectedTags ? props.selectedTags.slice() : [];
  const idx = selected.indexOf(tag);
  if (idx >= 0) selected.splice(idx, 1);
  else selected.push(tag);

  emit("update:selectedTags", selected);
}

function onClickSortToggle() {
  emit('toggleSort')
}
</script>
  <style scoped>
.sidebar-wrap {
  background: #fff;
  border-radius: 18px;
  box-shadow: 0 3px 14px rgba(160, 120, 210, 0.09);
  padding: 30px 18px 26px 18px;
  width: 220px; /* 기존 190px → 220px */
  min-width: 200px; /* 기존 170px → 200px */
  display: flex;
  flex-direction: column;
  align-items: stretch;
  gap: 40px;
}
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px 7px;
  margin: 10px;
}
.tag-chip.selected {
  background: #a485e8 !important;
  color: #fff !important;
}
.sidebar-title {
  font-weight: 700;
  font-size: 18px;
  margin-bottom: 14px;
}
.sidebar-tag {
  background: #f4f0fe;
  color: #8569c6;
  border-radius: 10px;
  display: inline-block;
  padding: 3px 14px;
  margin: 3px 0 5px 0;
  font-size: 14px;
}
.sidebar-write-btn {
  width: 100%;
  background: linear-gradient(90deg, #a67fe8, #b9a9fa);
  color: #fff;
  font-size: 1.05rem;
  display: flex;
  justify-content: center;
}

.sidebar-btn {
  border-radius: 15px;
  padding: 10px 28px;
  font-weight: 600;
  margin-top: 15px;
  cursor: pointer;
  transition: filter 0.18s;
}
.sidebar-btn:hover {
  filter: brightness(0.92);
}

.row-btns {
  display: flex;
  gap: 10px;
}

.sidebar-sub-btn {
  border: 2.5px solid transparent;
  flex: 1;
  height: 42px;
  background: linear-gradient(#fff, #fff) padding-box,
    linear-gradient(90deg, #bfa7f2, #a8e7f6) border-box;
  color: #7c5cc6;
  border-radius: 11px;
  font-weight: 600;
  font-size: 1.05rem;
  cursor: pointer;
  transition: color 0.16s, border-color 0.16s;
}
</style>
  