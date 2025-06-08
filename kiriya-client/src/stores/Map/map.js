// ✅ map.js (Pinia Store)
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { isKakaoMapApiLoaded } from 'vue3-kakao-maps/@utils'

export const useMapStore = defineStore('map', () => {
  const map = ref(null)
  const coord = ref({ lat: 33.450701, lng: 126.570667 })
  const label = ref('')
  const hasLocation = ref(false)

  function setMap(mapRef) {
    map.value = mapRef
  }

  function setCoord(lat, lng, newLabel = '') {
    coord.value = { lat, lng }
    if (newLabel) label.value = newLabel
  }

  function setHasLocation(value) {
    hasLocation.value = value
  }

  function fitbounds(items) {
    console.debug("items::::",items)
    if (
      !map.value ||
      !isKakaoMapApiLoaded.value ||
      typeof window.kakao === 'undefined' ||
      !items ||
      items.length === 0
    ) return

    const bounds = new window.kakao.maps.LatLngBounds()
    items.forEach(item => {
      if (item.lat && item.lng) {
        const latLng = new window.kakao.maps.LatLng(item.lat, item.lng)
        bounds.extend(latLng)
      }
    })

    map.value.setBounds(bounds)
  }

  function panToCenter(item) {
    if (
      !map.value ||
      !isKakaoMapApiLoaded.value ||
      typeof window.kakao === 'undefined' ||
      !item?.lat || !item?.lng
    ) return

    const latLng = new window.kakao.maps.LatLng(item.lat, item.lng)
    map.value.panTo(latLng)
  }

  return {
    map,
    coord,
    label,
    hasLocation,
    setMap,
    setCoord,
    setHasLocation,
    fitbounds, panToCenter,
  }
})
