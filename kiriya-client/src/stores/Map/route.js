import { defineStore } from 'pinia'
import { ref, computed, watch } from 'vue'
import axios from 'axios'

import { useAttractionStore } from '../attraction'
import { usePlanStore } from '../plan'

// const markerStore = useMarkerStore()
const attractionStore = useAttractionStore()
const planStore = usePlanStore()

// axios 인스턴스
const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
})

export const useRouteStore = defineStore('route', () => {
  const _route = ref([])
  const _routeDuration = ref([])
  const _routePath = ref([])

  const route = computed(() => _route.value)
  const routeDuration = computed(() => _routeDuration.value)
  const routePath = computed(() => _routePath.value)

  const routeSaveDialogShow = ref(false)
  const routeImportDialogShow = ref(false)
  const newRouteShow = ref(true)
  const importRouteShow = ref(false)
  
  const routeTrip = ref(null)
  const routePlan = ref(null)
  
  const startContentId = ref(null)
  const endContentId = ref(null)

  const isIn = (contentId) => {
    return _route.value.some(item => item.contentId === contentId)
  }

  const addRoute = (item) => {
    if (_route.length > 30) {
      alert('관광지는 30개 까지 추가 가능합니다.')
      return
    }
    if (isIn(item.contentId)) {
      alert('이미 포함된 관광지입니다.')
      return
    }
    _route.value.push({
      no: item.no, title: item.title, addr1: item.addr1,
      contentId: item.contentId, contentTypeId: item.contentTypeId,
      longitude: item.longitude, latitude: item.latitude,
      firstImage1: item.firstImage1, firstImage2: item.firstImage2,
    })
  }

  const removeRoute = (item) => {
    const idx = _route.value.findIndex(el => el.contentId === item.contentId)
    if (idx !== -1) {
      _route.value.splice(idx, 1)
    }
  }

  const setRoute = (newRoute) => {
    _route.value = newRoute
  }

  const clearRoute = () => { 
    _route.value = []
    routePlan.value = null
    routeTrip.value = null
    importRouteShow.value = false
  }

  const importRoute = (plan) => {
    routePlan.value = plan
  }

  /** 경로 추천 받기 **/
  const getShortestRoute = async () => {
    if (_route.value.length < 4) {
      alert("관광지를 4개 이상 추가해 주세요.")
      return
    } else if (!startContentId.value) {
      alert("출발지를 선택해 주세요.")
      return
    } else if (!endContentId.value) { 
      alert("도착지를 선택해 주세요.")
      return
    }

    const params = {
      route: _route.value,
      startId: startContentId.value,
      endId: endContentId.value,
    }
    
    try {
      const response = await api.post("/map/route", params)
      const shortestRoute = response.data.data.route

      // 중요!! 같은 구성은 같은 객체로 봄 -> 변화 감지 못함 -> ref 동작 안함
      _route.value = JSON.parse(JSON.stringify(shortestRoute))  // 다른 참조로 넣기
    } catch (e) {
      alert("최적 경로 요청 실패", e)
      return
    }
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
      + `&menu=route`
  }

  /* 일정에서 route import */
  const fetchRoute = async (pno) => {
    clearRoute()
    const plans = await planStore.fetchRoute(pno)
    console.log('route:',plans.route)
    _route.value = plans.route
  }

  // route 변화 감지하여 routeDuration, routePath 자동 생성
  watch(_route, async (newRoute) => {
    const paths = []
    const durations = []

    for (let i = 0; i < newRoute.length - 1; i++) {
      // 지도 링크
      const link = getNaverMapLink(newRoute[i], newRoute[i + 1])
      paths.push(link)

      // 카카오 API 통해 시간 계산
      const time = await getDuration(newRoute[i], newRoute[i + 1])
      durations.push(time)
    }

    _routePath.value = paths
    _routeDuration.value = durations
  }, { deep: true })


  return {
    route, routeDuration, routePath, routePlan, routeTrip,
    startContentId, endContentId,
    routeSaveDialogShow, routeImportDialogShow, newRouteShow, importRouteShow,
    addRoute, removeRoute, getShortestRoute, clearRoute, setRoute, isIn, importRoute, fetchRoute,
  }
})
