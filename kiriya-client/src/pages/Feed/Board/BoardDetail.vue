<template>
    <v-container class="py-10" style="max-width: 650px;">
      <v-card class="pa-8" elevation="1">
        <!-- 제목 -->
        <v-card-title class="text-2xl font-bold">{{ board.title }}</v-card-title>
        <!-- 작성자/작성일/태그 -->
        <div class="d-flex align-center justify-between mt-2 mb-4 text-gray-500" style="font-size: 15px;">
          <span>by {{ board.author }}</span>
          <span>{{ board.createdAt }}</span>
        </div>
        <!-- 태그 -->
        <div class="mb-3" v-if="board.tags && board.tags.length">
          <v-chip
            v-for="tag in board.tags"
            :key="tag"
            color="deep-purple lighten-4"
            class="mr-2"
            text-color="purple"
            size="small"
            label
          >#{{ tag }}</v-chip>
        </div>
        <!-- 이미지 슬라이드 -->
        <div v-if="board.imageUrls && board.imageUrls.length" class="mb-5">
          <v-carousel v-if="board.imageUrls.length > 1" hide-delimiter-background show-arrows>
            <v-carousel-item
              v-for="(img, idx) in board.imageUrls"
              :key="idx"
              :src="img"
            />
          </v-carousel>
          <img
            v-else
            :src="board.imageUrls[0]"
            alt="게시글 이미지"
            style="width:100%;border-radius:16px;"
          />
        </div>
        <!-- 내용 -->
        <v-card-text class="text-lg mb-5" style="white-space:pre-line;">{{ board.content }}</v-card-text>
  
        <!-- 좋아요/댓글 -->
        <div class="d-flex align-center">
          <v-btn
            icon
            @click="onLike"
            :color="board.liked ? 'deep-purple accent-3' : 'grey'"
          >
            <v-icon>mdi-thumb-up</v-icon>
          </v-btn>
          <span class="ml-2 mr-6">{{ board.likes || 0 }}</span>
          <!-- (댓글은 추후 추가) -->
          <!-- <v-icon class="mr-2">mdi-comment</v-icon> {{ board.commentCount || 0 }} -->
        </div>
      </v-card>
    </v-container>
  </template>
  
  <script setup>
  import { ref, onMounted } from 'vue'
  import { useRoute } from 'vue-router'
  import axios from 'axios'
  
  const route = useRoute()
  const bno = route.params.bno
  
  const board = ref({
    title: '',
    content: '',
    imageUrls: [],
    tags: [],
    author: '',
    createdAt: '',
    likes: 0,
    liked: false
  })
  
  // 게시글 불러오기
  async function fetchBoard() {
    const { data } = await axios.get(`http://localhost:8080/api/boards/${bno}`)
    const res = data.data.board
    // 백엔드 반환에 맞게 프로퍼티 매핑 필요
    board.value = {
      title: res.title,
      content: res.content,
      imageUrls: res.imageUrls || [],
      tags: res.tags || [],
      author: res.author,
      createdAt: res.createdAt?.substring(0, 10) || '', // 날짜만 추출
      likes: res.likes,
      liked: res.liked // (로그인 시에만)
    }
  }
  
  onMounted(fetchBoard)
  
  // 좋아요 토글
  async function onLike() {
    if (!board.value.liked) {
      const { data } = await axios.post(`http://localhost:8080/api/boards/${bno}/likes`)
      board.value.liked = true
      board.value.likes = data.data.likesCnt
    } else {
      const { data } = await axios.delete(`http://localhost:8080/api/boards/${bno}/likes`)
      board.value.liked = false
      board.value.likes = data.data.likesCnt
    }
  }
  </script>
  