<template>
  <v-container>
    <v-card class="pa-10" max-width="1000" style="margin: auto;">
      <h2 class="text-center text-primary">그룹 상세정보</h2>
      <v-card-text v-if="group">
        <div class="mb-2">
          <strong>그룹명:</strong> {{ group.name }}
        </div>
        <div class="mb-4">
          <strong>설명:</strong> {{ group.description }}
        </div>

        <v-divider class="my-4" />

        <div>
          <h3 class="mt-4">그룹 멤버</h3>
          
          <!-- 프로필 아바타만 나란히 보여주기 -->
          <div class="member-avatars d-flex">
            <v-tooltip
              v-for="member in members"
              :key="member.mno"
              :content-class="'custom-tooltip'"
              bottom
            >
              <!-- activator slot: 마우스 호버 지점 -->
              <template #activator="{ props, on }">
                <v-avatar
                  v-bind="props"
                  v-on="on"
                  size="56"
                  :class="{
                    'owner-border': member.mno === group.mno,
                    'member-border': member.mno !== group.mno
                  }"
                >
                  <v-img
                    :src="member.profileUrl || defaultProfileImage"
                    cover
                  />
                </v-avatar>
              </template>
              <!-- 툴팁 내용 -->
              <div class="tooltip-content">
                <div><strong>{{ member.memberName }}</strong></div>
                <div>{{ member.memberEmail }}</div>
              </div>
            </v-tooltip>
            <!-- 멤버가 하나도 없을 때 -->
            <div v-if="!members.length" class="no-members">
              멤버가 없습니다.
            </div>
          </div>
        </div>

        <v-divider class="my-4" />

        <div class="d-flex align-center justify-space-between mb-2">
          
          <h3 class="mt-4">그룹 여행</h3>
          <v-btn
            color="primary"
            class="rounded-pill font-bold mt-4"
            variant="tonal"
            size="small"
            @click="showAddTripModal = true"
          >
            <v-icon start size="18">mdi-plus</v-icon>
            내 여행 등록하기
          </v-btn>
        </div>

        <AddMyTripModal
          v-model="showAddTripModal"
          :group-gno="gno"
          @success="fetchGroupTrips"
        />
        <div>
          <v-list>
            <v-list-item
        v-for="trip in trips"
        :key="trip.tno"
        class="mb-2"
        @click="goTripDetail(trip.tno)"
        style="cursor:pointer;"
      >
        <v-list-item-content>
          <v-list-item-title>{{ trip.title }}</v-list-item-title>
          <v-list-item-subtitle>
            {{ trip.startDateOnly }} ~ {{ trip.endDateOnly }}
          </v-list-item-subtitle>
        </v-list-item-content>
      </v-list-item>

      <v-list-item v-if="!trips.length">
        <v-list-item-content>
          <v-list-item-title>등록된 여행이 없습니다.</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
          </v-list>
        </div>
      </v-card-text>

      <v-card-actions>
        <v-btn color="primary" variant="tonal" v-if="isOwner" @click="goEdit">수정</v-btn>
        <v-btn variant="tonal" @click="goList">목록으로</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import axios from 'axios'
import defaultProfileImage from '@/assets/default-profile.png'
import AddMyTripModal from '@/components/layout/trip/AddMyTripModal.vue'

const route = useRoute()
const router = useRouter()
const gno = route.params.gno
const group = ref(null)
const members = ref([])
const trips = ref([])

const showAddTripModal = ref(false)
const userStore = useUserStore()
console.log(userStore.profile.mno)
const isOwner = computed(() => userStore.profile?.mno === group.value?.mno)

async function fetchGroupDetail() {
  try {
    const res = await axios.get(`http://localhost:8080/api/groups/${gno}`)
    const { group: g, members: m } = res.data.data
    group.value = g
    members.value = m
  } catch (e) {
    console.error('그룹 상세 조회 실패:', e)
  }
}

async function fetchGroupTrips() {
  try {
    const res = await axios.get(`http://localhost:8080/api/groups/${gno}/trips`)
    trips.value = res.data.data.trips
  } catch (err) {
    console.error('그룹 여행 목록 로드 실패', err)
  }
}

function goTripDetail(tno) {
  router.push(`/trips/${tno}`)
}

function goEdit() {
  router.push({ name: 'GroupEdit', params: { gno: route.params.gno } })
}

function goList() {
  router.push({ name: 'GroupList' })
}

onMounted(async() => {
  await Promise.all([fetchGroupDetail(), fetchGroupTrips()])
})
</script>

<style scoped>
.member-avatars {
  gap: 12px;            /* 아바타 사이 간격 */
  overflow-x: auto;     /* 가로 스크롤 허용 */
  padding: 8px 0;
}
.tooltip-content {
  text-align: center;
  font-size: 14px;
  line-height: 1.2;
}

.owner-border {    
  border: 3px solid #FFD966;
  box-sizing: border-box;
  border-radius: 50% !important;
  overflow: hidden;
}

.member-border {
  border: 3px solid #f5f5f5;
  box-sizing: border-box;
  border-radius: 50% !important;
  overflow: hidden;
}

.no-members {
  color: #777;
  padding: 8px;
}
</style>