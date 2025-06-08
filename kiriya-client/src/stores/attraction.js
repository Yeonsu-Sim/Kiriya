import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import axios from 'axios'
import qs from 'qs'

import { useUserStore } from './user'
const userStore = useUserStore()

export const useAttractionStore = defineStore('attraction', () => {
  // 내부 상태
  const _attractions = ref([])
  const _detail = ref(null)

  const loginMno = userStore.loginMno

  // 외부 노출
  const attractions = computed(() => _attractions.value)
  const detail = computed(() => _detail.value)

  // axios 인스턴스 (기본 설정 포함 가능)
  const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' }),  // 중요
    withCredentials: true,
    // timeout: 5000,
  })

  // 관광지 목록 조회
  const fetchAttractions = async ({ sidoCode, gunguCode, contentTypeList, query }) => {

    // 임시 필터링 -> 나중에 페이징 처리하면 철회
    if (!sidoCode) {
      console.error('시/도를 선택하세요...')
      return
    } else if (!gunguCode) {
      console.error('시/군/구를 선택하세요...')
      return
    } else if (contentTypeList.length == 0) {
      clearAttractions()
      return
    }

    const queryParams = {
      sidoCode: sidoCode || '',
      gunguCode: gunguCode || '',
      contentTypeList: contentTypeList || '',
      keyword: query || '',
    }
    try {
      const response = await api.get(`/attractions/search/${loginMno}`, { params: queryParams })
      const data = response.data

      _attractions.value = (data.data.attractionList || []).map(el => ({
        ...el,
        utmk: { lat: el.latitude, lng: el.longitude },
        address: el.addr1,
        label: el.title,
      }))
    } catch (e) {
      console.error('관광지 조회 실패:', e)
      clearAttractions()
    }
  }

  // 관광지 상세 조회
  const fetchAttractionDetail = async (contentId) => {
    try {
      const response = await api.get(`/attractions/${contentId}/${loginMno}`)
      const data = response.data
      _detail.value = data.data.attractionDetail
    } catch (e) {
      console.error('관광지 상세 조회 실패:', e)
      clearDetail()
    }
  }

  const clearAttractions = () => { _attractions.value = []; }

  const clearDetail = () => { _detail.value = null }

  const like = (contentId) => {
    const target = _attractions.value.find(a => a.contentId === contentId)
    if (target) {
      target.liked = true
      target.likes++
    }

    const d = _detail.value
    if (d && d.contentId === contentId) {
      d.liked = true
      d.likes++
    }
  }
  
  const unlike = (contentId) => {
    const target = _attractions.value.find(a => a.contentId === contentId)
    if (target) {
      target.liked = false
      target.likes--
    }

    const d = _detail.value
    if (d && d.contentId === contentId) {
      d.liked = false
      d.likes--
    }
  }

  return {
    attractions,
    detail,
    fetchAttractions,
    fetchAttractionDetail,
    clearAttractions, clearDetail,
    like, unlike,
  }
}, )
