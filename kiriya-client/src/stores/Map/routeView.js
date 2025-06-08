import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'
import axios from 'axios'

import { usePlanStore } from '../plan'
const planStore = usePlanStore()

// axios 인스턴스
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
})

export const useRouteViewStore = defineStore('routeView', () => {
  const _routeView = ref([])
  const _routeViewDuration = ref([])
  const _routeViewPath = ref([])

  const routeView = computed(() => _routeView.value)
  const routeViewDuration = computed(() => _routeViewDuration.value)
  const routeViewPath = computed(() => _routeViewPath.value)

  const routeViewImportDialogShow = ref(false)
  const importRouteViewShow = ref(false)
  
  const routeViewTrip = ref(null)
  const routeViewPlan = ref(null)

  const clearRouteView = () => { 
    _routeView.value = []
    routeViewPlan.value = null
    routeViewTrip.value = null
    importRouteViewShow.value = false
  }

  const importRouteView = (plan) => {
    routeViewPlan.value = plan
  }

  /* 1:1 길찾기 */
  const getDirections = async (src, dst) => {
    if (!src?.longitude || !src?.latitude || !dst?.longitude || !dst?.latitude) {
      console.warn('위치 정보 누락:', { src, dst })
      throw new Error('위치 정보 누락')
    }
    const params = {
      srcTitle: src.title,
      dstTitle: dst.title,
      origin: src.longitude+","+src.latitude,
      destination: dst.longitude+","+dst.latitude,
      priority: "TIME",
    }

    try {
      const result = await api.post("/map/directions", params)
      const response = result.data.data
      if (!response) throw new Exception('응답 오류 발생')
      if (response.result_code != 0) throw new Exception(response.result_msg)
      return result
      
    } catch (e) {
      console.log("길찾기 요청 실패", e)
      throw e
    }
  };

  /* 1:1 소요 시간 구하기 */
  const getDuration = async (src, dst) => {
    try {
      const directions = await getDirections(src, dst);
      const response = directions.data.data;

      const totalMinutes = Math.ceil(response.sections[0].duration / 60);
      const hours = Math.floor(totalMinutes / 60);
      const minutes = totalMinutes % 60;

      if (hours > 0) {
        return `${hours}시간 ${minutes}분`;
      } else {
        return `${minutes}분`;
      }
    } catch (e) {
      console.log("소요 시간 요청 실패", e);
      return "알 수 없음";
    }
  };

  /* 네이버 길찾기 링크 생성 함수 */
  const getNaverMapLink = (src, dst) => {
    return `http://map.naver.com/index.nhn`
      + `?slng=${src.longitude}&slat=${src.latitude}&stext=${encodeURIComponent(src.title)}`
      + `&elng=${dst.longitude}&elat=${dst.latitude}&etext=${encodeURIComponent(dst.title)}`
      + `&pathType=0&showMap=true`
      + `&menu=routeView`
  }

  /* 일정에서 routeView import */
  const fetchRouteView = async (pno) => {
    clearRouteView()
    const plans = await planStore.fetchRoute(pno)
    console.log('routeView:',plans.route)
    _routeView.value = plans.route || []
  }

  // routeView 변화 감지하여 routeViewDuration, routeViewPath 자동 생성
  watch(_routeView, async (newRouteView) => {
    const paths = []
    const durations = []

    for (let i = 0; i < newRouteView.length - 1; i++) {
      // 지도 링크
      const link = getNaverMapLink(newRouteView[i], newRouteView[i + 1])
      paths.push(link)

      // 카카오 API 통해 시간 계산
      const time = await getDuration(newRouteView[i], newRouteView[i + 1])
      durations.push(time)
    }

    _routeViewPath.value = paths
    _routeViewDuration.value = durations
  }, { deep: true })


  return {
    routeView, routeViewDuration, routeViewPath, routeViewPlan, routeViewTrip,
    routeViewImportDialogShow, importRouteViewShow,
    clearRouteView, importRouteView, fetchRouteView,
  }
})
