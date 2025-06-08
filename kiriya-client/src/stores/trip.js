// src/stores/trip.js
import { defineStore } from 'pinia'
import axios from 'axios'
import { useUserStore } from './user'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
})


export const useTripStore = defineStore('trip', {
  state: () => ({
    _trips: [],
  }),
  getters: {
    trips: (state) => state._trips,
  },
  actions: {
    /** 내 여행 불러오기 */
    async fetchTrips() {
      const userStore = useUserStore()
      const loginMno = userStore.profile?.mno

      console.log("mno:",loginMno)
      try {
        const response = await api.get(`/members/${loginMno}/trips`)
        console.log("response:",response)
        this._trips = response.data.data
        console.log('trips:',this.trips)
      } catch (e) {
        console.error('여행 조회 실패:', e)
        this._trips = []
      }
    },

    /** 그룹 여행 불러오기 */
    async fetchTripsByGno(gno) {
      console.log("gno:",gno)
      try {
        const response = await api.get(`/groups/${gno}/trips`)
        console.log("response:",response)
        this._trips = response.data.data
        console.log('trips:',this.trips)
      } catch (e) {
        console.error('여행 조회 실패:', e)
        this._trips = []
      }
    },

    /** 여행 생성하기 */
    async createTrip({ title, description, startDate, endDate }) {
      const userStore = useUserStore()
      const loginMno = userStore.loginMno
      console.log(title, description, startDate, endDate, loginMno)
      if (!loginMno) {
        console.warn('로그인 이후 이용해 주세요.')
        return
      }

      await api.post(`/trips`, {
        title,
        description,
        startDate,
        endDate,
        mno: loginMno,
      })
      // 저장 후 목록 새로고침
      await this.fetchTrips()
    },

    /** 여행 번호로 여행 조회 */
    async getTrip(tno) {
      console.log('tno:',tno)
      try {
        const response = await api.get(`/trips/${tno}`)
        console.log("response:",response)
        return response.data.data.trip
      } catch (e) {
        console.error('여행 조회 실패:', e)
        return null
      }
    },

    /** 여행 수정하기 */
    async editTrip({ tno, title, description, startDate, endDate }) {
      console.log('tno:',tno)
      try {
        const response = await api.put(`/trips/${tno}`, {
          tno,
          title,
          description,
          startDate,
          endDate,
          // mno: loginMno,
        })
        console.log("response:",response)
      } catch (e) {
        console.error('여행 수정 실패:', e)
      }
      await this.fetchTrips()
    },

    /** 여행 삭제하기 */
    async deleteTrip(tno) {
      console.log('tno:',tno)
      try {
        const response = await api.delete(`/trips/${tno}`)
        console.log("response:",response)
      } catch (e) {
        console.error('여행 삭제 실패:', e)
      }
      await this.fetchTrips()
    }

  }
})
