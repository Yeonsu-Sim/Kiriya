// src/stores/filterStore.js
import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import qs from 'qs'

import { useMarkerStore } from './marker'

export const useFilterStore = defineStore('filter', () => {
  const markerStore = useMarkerStore()

  const sidoList = ref([])
  const gunguList = ref([])
  const contentTypeList = ref([])

  const selectedSido = ref('')
  const selectedGungu = ref('')
  const selectedContentTypes = ref([])
  const contentTypesAllSelected = computed(() => selectedContentTypes.value.length == contentTypeList.value.length)

  // axios 인스턴스 (기본 설정 포함 가능)
  const api = axios.create({
    baseURL: 'http://localhost:8080/api',
    paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' }),  // 중요
    withCredentials: true,
    // timeout: 5000,
  })

  const fetchSidoList = async () => {
    try {
      const res = await api.get('/util/sidos')
      sidoList.value = (res.data.data.sidoList || []).map(item => ({
        value: item.code,
        label: item.name,
      }))
    } catch (e) {
      alert('시/도 필터 불러오기 실패')
      sidoList.value = []
    }
  }

  const fetchGunguList = async (sidoCode) => {
    try {
      const res = await api.get('/util/gungus', {
        params: { sidoCode },
      })
      gunguList.value = (res.data.data.gunguList || []).map(item => ({
        value: item.code,
        label: item.name,
      }))
    } catch (e) {
      alert('시/군/구 필터 불러오기 실패')
      gunguList.value = []
    }
}

const fetchContentTypeList = async () => {
    try {
        const res = await api.get('/util/content-types', )
        contentTypeList.value = (res.data.data.contentTypeList || []).map(item => ({
            value: item.code,
            label: item.name,
            icon: markerStore.icons[item.code+''],
            color: markerStore.colors[item.code+''],
        }))
    } catch (e) {
        alert('컨텐츠 타입 필터 불러오기 실패')
        contentTypeList.value = []
    }
    fillSelectedContentTypes()
  }

  const clearGungu = () => {
    selectedGungu.value = ''
    gunguList.value = []
  }

  const clearSelectedContentTypes = () => {
    selectedContentTypes.value = []
  }

  const fillSelectedContentTypes = () => {
    clearSelectedContentTypes()
    contentTypeList.value.map(item => selectedContentTypes.value.push(item.value))
  }

  const resetFilter = () => {
    selectedSido.value = ''
    selectedGungu.value = ''
    fillSelectedContentTypes()
  }

  return {
    sidoList,
    gunguList,
    contentTypeList,
    selectedSido,
    selectedGungu,
    selectedContentTypes,
    fetchSidoList,
    fetchGunguList,
    fetchContentTypeList,
    clearGungu,
    contentTypesAllSelected,
    clearSelectedContentTypes,
    fillSelectedContentTypes,
    resetFilter,
  }
})
