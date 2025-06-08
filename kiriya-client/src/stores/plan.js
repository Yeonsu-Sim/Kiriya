// src/stores/plan.js
import { defineStore } from 'pinia'
import axios from 'axios'
import { useUserStore } from './user'
import { useRouteStore } from '@/stores/Map/route'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  withCredentials: true,
})

export const usePlanStore = defineStore('plan', {
  state: () => ({
    // _plans: [],
  }),
  getters: {
    // plans: (state) => state._plans,
  },
  actions: {
    /** 내 일정 불러오기 */
    async fetchPlans(tno) {
      // const userStore = useUserStore()
      // const loginMno = userStore.loginMno

      // console.log("mno:",loginMno)
      
      try {
        const response = await api.get(`/trips/${tno}/plans`)
        console.log("response:",response)
        // this._plans = response.data.data
        // console.log('plans:',this.plans)
        return response.data.data.planList
      } catch (e) {
        console.error('일정 조회 실패:', e)
        // this._plans = []
      }
    },

    /** 일정 생성하기 */
    async createPlan(payload) {
      const userStore = useUserStore()
      const routeStore = useRouteStore()

      const loginMno = userStore.loginMno

      console.log("mno:",loginMno)
      try {
        const response = await api.post(`/plans`, {
          mno: loginMno,
          ...payload,
          route: routeStore.route,
        })
        if (response.data.error) throw new Exception(response.data.error.message)
        console.log("response:",response)
        alert('저장되었습니다.')
      } catch (e) {
        console.error('일정 생성 실패:', e)
      }
    },

    /** 경로 불러오기 */
    async fetchRoute(pno) {
      try {
        const response = await api.get(`/plans/${pno}`)
        if (response.data.error) throw new Exception(response.data.error.message)
        console.log("response:",response)
        return response.data.data.plan
      } catch (e) {
        console.error('경로 불러오기 실패:', e)
        return []
      }
    },

    /** 일정 수정하기 */
    async editPlan({ pno, title, description, startDate, endDate, route}) {
      console.log('pno:',pno)
      console.log('route:',route)
      if (route) {
        route = JSON.parse(JSON.stringify(route))
      }
      try {
        const response = await api.put(`/plans/${pno}`, {
          pno,
          title,
          description,
          startDate,
          endDate,
          route,
          // mno: loginMno,
        })
        console.log("response:",response)
      } catch (e) {
        console.error('일정 수정 실패:', e)
      }
    },

    /** 일정 경로 수정하기 */
    async updatePlan() {
      if (!confirm('현재 경로로 바꾸시겠습니까?')) return
      const routeStore = useRouteStore()
      const plan = routeStore.routePlan
      try {
        await this.editPlan({
          ...plan,
          route: routeStore.route,
        })
      } catch (e) {
        console.error('경로 수정 실패:', e)
      }
    },

    /** 일정 삭제하기 */
    async deletePlan(pno) {
      console.log('pno:',pno)
      try {
        const response = await api.delete(`/plans/${pno}`)
        console.log("response:",response)
      } catch (e) {
        console.error('일정 삭제 실패:', e)
      }
    }
  }
})
