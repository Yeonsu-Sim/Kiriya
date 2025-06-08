import { defineStore } from 'pinia'
import { ref, computed, shallowRef  } from 'vue'
import axios from 'axios'

import { useAttractionStore } from '@/stores/attraction'
import { useUserStore } from '@/stores/user'

const attractionStore = useAttractionStore()
const userStore = useUserStore()

// axios 인스턴스 (기본 설정 포함 가능)
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
  // timeout: 5000,
})

export const useBucketlistStore = defineStore('bucketlist', () => {
  // 내부 상태
  const _bucketlist = ref([])
  
  const loginMno = userStore.loginMno

  // 외부 노출
  const bucketlist = computed(() => _bucketlist.value )


  // 버킷리스트 조회
  const fetchBucketlist = async () => {
    console.log('loginMno:',loginMno)
    if (loginMno <= 0) {
      return
    }

    try {
      const response = await api.get(`/members/${loginMno}/attractions`)
      const data = response.data
      _bucketlist.value = data.data 
      console.log(bucketlist)
    } catch (e) {
      console.log(e)
      alert('버킷리스트 조회 실패:', e)
      clearBucketlist()
    }
  }
  
  const clearBucketlist = ( () => _bucketlist.value = [])
  
  // 버킷리스트 담기
  const like = async (contentId) => {
    console.log("좋아요를 눌렀습니다.", contentId)
    
    try {
      const response = await api.post(`/attractions/${contentId}/likes/${loginMno}`)
      const data = response.data
      await fetchBucketlist()
      attractionStore.like(contentId)
    } catch (e) {
      console.log(e)
      alert('버킷리스트 담기 실패:', e)
    }
  }
  
  const unlike = async (contentId) => {
    console.log("좋아요를 취소했습니다.", contentId)
    
    try {
      const response = await api.delete(`/attractions/${contentId}/likes/${loginMno}`)
      const data = response.data
      await fetchBucketlist()
      attractionStore.unlike(contentId)
    } catch (e) {
      console.log(e)
      alert('버킷리스트 제외 실패:', e)
    }
  }

  return {
    bucketlist,
    fetchBucketlist,
    like, unlike,
    loginMno,
  }
}, )
