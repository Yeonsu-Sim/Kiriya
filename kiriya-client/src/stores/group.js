// src/stores/group.js
import { defineStore } from 'pinia'
import axios from 'axios'
import qs from 'qs'

import { useUserStore } from './user'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  paramsSerializer: params => qs.stringify(params, { arrayFormat: 'repeat' }),  // 중요
  // withCredentials: true,
})


export const useGroupStore = defineStore('group', {
  state: () => ({
    _groups: [],
    _members: [],
  }),
  getters: {
    groups: (state) => state._groups,
    members: (state) => state._members,
  },
  actions: {

    /** 내 그룹 불러오기 */
    async fetchGroups() {
      try {
        const res = await api.get(`/groups`, )
        const data = res.data.data || res.data
        console.log('그룹 목록 조회:', res.data)
        // API 리턴 형태에 따라 gno, emails 등 키 조정
        this._groups = data.map(g => ({ ...g, manage: true }))

      } catch (e) {
        console.error('그룹 목록 조회 실패:', e)
      }
    },

    /** 그룹 생성하기 */
    async createGroup({ name, description, membersEmails }) {
      try {
        const payload = {
          name,
          description,
        }
        await api.post(
          `/groups`,
          payload,
          {
            params: { 
              emails: membersEmails.value
            },
          }
        )
        // 저장 후 목록 새로고침
        await this.fetchGroups()
        this._members.value = [] // 초기화
      } catch (err) {
        console.error('그룹 생성 실패:', err.response?.data || err.message)
      }
    },

    /** 그룹 번호로 그룹 조회 */
    async getGroup(gno) {
      try {
        const res = await api.get(`/groups/${gno}`)
        const { group: g, members: m } = res.data.data
        this._groups = g
        _members.value = m
      } catch (e) {
        console.error('그룹 상세 조회 실패:', e)
      }
    },

    /** 그룹 수정하기 */
    async editGroup({ gno, name, description, membersEmails }) {
      try {
        const payload = {
          name,
          description,
        }
        await api.put(
          `/groups/${gno}`,
          payload,
          { params: { 
              emails: membersEmails.value
            },
          }
        )
        await this.fetchGroups()
      } catch (err) {
        console.error('그룹 수정 실패', err)
      } finally {
        loading.value = false
      }
    },

    /** 그룹 삭제하기 */
    async deleteGroup(gno) {
      if (!confirm('정말 삭제하시겠습니까?')) return
      try {
        await api.delete(`/groups/${gno}`)
        // 삭제 후 다시 조회
        await this.fetchGroups()
      } catch (e) {
        console.error('그룹 삭제 실패:', e)
      }
    }

  }
})
