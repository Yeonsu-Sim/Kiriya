import blueMarker from '@/assets/marker/blue_marker.png'
import redMarker from '@/assets/marker/red_marker.png'
import purplrMarker from '@/assets/marker/purple_marker.png'
import primaryMarker from '@/assets/marker/primary_marker.png'

import { defineStore } from 'pinia'
import { ref, shallowRef } from 'vue'

import { useBucketlistStore } from '../bucketlist'
import { useRouteStore } from './route'
import { useRouteViewStore } from './routeView'

const bucketlistStore = useBucketlistStore()
const routeStore = useRouteStore()
const routeViewStore = useRouteViewStore()

export const useMarkerStore = defineStore('marker', () => {
  const icons = {
    '12': 'mdi-wallet-travel',         // 관광지
    '14': 'mdi-drama-masks',             // 문화시설
    '15': 'mdi-party-popper',       // 축제공연행사
    '25': 'mdi-directions',         // 여행코스
    '28': 'mdi-tennis-ball',                // 레포츠
    '32': 'mdi-home',              // 숙박
    '38': 'mdi-shopping-outline',           // 쇼핑
    '39': 'mdi-silverware-fork-knife', // 음식점
  }

  const colors = {
    '12': 'attraction',         // 관광지
    '14': 'culture',             // 문화시설
    '15': 'festival',       // 축제공연행사
    '25': 'course',         // 여행코스
    '28': 'leisure',                // 레포츠
    '32': 'lodging',              // 숙박
    '38': 'shopping',           // 쇼핑
    '39': 'food', // 음식점
  }

  const _attractions = shallowRef([])  // 관광지 마커
  const _bucketlists = shallowRef([])  // 버킷리스트 마커
  const _route = ref([])  // 경로만들기 마커
  const _routeView = ref([])  // 경로보기 마커

  // computed
  const attractions = computed(() => _attractions.value )
  const bucketlists = computed(() => _bucketlists.value )
  const route = computed(() => _route.value )
  const routeView = computed(() => _routeView.value )

  const bucketlistMarkerShow = ref(false)
  const routeMarkerShow = ref(true)
  const routeViewMarkerShow = ref(false)
  
  // const replaceAttractions = (items) => { _attractions.value = items }
  const addAttraction = (item) => { _attractions.value.push(item) }
  const clearAttractions = () => { _attractions.value = [] }
  
  // const replaceBucketlist = (items) => { _bucketlist.value = items }
  const addBucketList = (item) => { _bucketlists.value.push(item) }
  const clearBucketList = () => { _bucketlists.value = [] }
  
  // const replaceRoute = (items) => { _attractions.value = items }
  const addRoute = (item) => { _route.value.push(item) }
  const clearRoute = () => { _route.value = [] }
  
  // const replaceRouteView = (items) => { _attractions.value = items }
  const addRouteView = (item) => { _routeView.value.push(item) }
  const clearRouteView = () => { _routeView.value = [] }

  const clearAllMarkers = () => {
    clearAttractions()
    clearBucketList()
    clearRoute()
    clearRouteView()
  }

  const showBucketlistMarker = () => { bucketlistMarkerShow.value = true }
  const hideBucketlistMarker = () => { bucketlistMarkerShow.value = false }
  const showRouteMarker = () => { routeMarkerShow.value = true }
  const hideRouteMarker = () => { routeMarkerShow.value = false }
  const showRouteViewMarker = () => { routeViewMarkerShow.value = true }
  const hideRouteViewMarker = () => { routeViewMarkerShow.value = false }

  watch(
    () => bucketlistStore.bucketlist,
    (value) => {
      clearBucketList()  // 먼저 초기화
      value.forEach(item => {
        const marker = {
          lat: item.latitude,
          lng: item.longitude,
          label: item.title,
          contentId: item.contentId,
          imageSrc: purplrMarker,
          // order: '💗',
        }
        addBucketList(marker)
      })
    },
    { immediate: true, deep: true }
  )

  watch(
    () => routeStore.route,
    (value) => {
      clearRoute()  // 먼저 초기화

      const offset = new kakao.maps.Point(30, 29)

      for (let i=0; i<value.length; i++) {
        let image = {
          imageSrc: primaryMarker,
          imageWidth: 60,
          imageHeight: 60,
          imageOption: {offset},
        }
        let order = i

        if (i === 0) {
          image.imageSrc = redMarker
          order = '출발'
        } else if (i == value.length-1) {
          image.imageSrc = blueMarker
          order = '도착'
        }

        const marker = {
          lat: value[i].latitude,
          lng: value[i].longitude,
          label: value[i].title,
          contentId: value[i].contentId,
          image,
          order,
          zIndex:99999,
        }
        addRoute(marker)
      }
    },
    { immediate: true, deep: true }
  ),

  watch(
    () => routeViewStore.routeView,
    (value) => {
      clearRouteView()  // 먼저 초기화

      const offset = new kakao.maps.Point(30, 29)

      for (let i=0; i<value.length; i++) {
        let image = {
          imageSrc: primaryMarker,
          imageWidth: 60,
          imageHeight: 60,
          imageOption: {offset},
        }
        let order = i

        if (i === 0) {
          image.imageSrc = redMarker
          order = '출발'
        } else if (i == value.length-1) {
          image.imageSrc = blueMarker
          order = '도착'
        }

        const marker = {
          lat: value[i].latitude,
          lng: value[i].longitude,
          label: value[i].title,
          contentId: value[i].contentId,
          image,
          order,
          zIndex:99999,
        }
        addRouteView(marker)
      }
    },
    { immediate: true, deep: true }
  )
  

  return {
    icons, colors, 
    attractions, bucketlists, route, routeView,
    addAttraction, addBucketList, addRoute, addRouteView,
    clearAttractions, clearBucketList, clearRoute, clearRouteView,
    clearAllMarkers,

    routeMarkerShow, routeViewMarkerShow, bucketlistMarkerShow,
    showBucketlistMarker, hideBucketlistMarker, 
    showRouteMarker, hideRouteMarker,
    showRouteViewMarker, hideRouteViewMarker,
  }
}, )