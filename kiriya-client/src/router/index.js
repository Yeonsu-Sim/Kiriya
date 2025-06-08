// Composables
import { createRouter, createWebHistory } from 'vue-router'
import { setupLayouts } from 'virtual:generated-layouts'
import { useUserStore } from '@/stores/user'

const routes = [
  // // 회원
  { path: '/login', name: 'Login', component: () => import('@/pages/Member/Auth.vue'), meta: { authMode: 'login'} },
  { path: '/logout', component: () => import('@/pages/Member/Logout.vue') },
  { path: '/signup', component: () => import('@/pages/Member/Auth.vue'), meta: { authMode: 'signup'} },
  { path: '/myPage', component: () => import('@/pages/Member/MemberDetail.vue')},
  { path: '/member/:mno/edit', component: () => import('@/pages/Member/MemberEdit.vue'), meta: { requiresAuth: true } },
  // { path: '/member/:mno/trips', component: () => import('@/pages/Member/MemberTrips.vue'), meta: { requiresAuth: true } },
  { path: '/member/find-password', component: () => import('@/pages/Member/FindPassword.vue') },

  // // 그룹
  { path: '/groups', name: 'GroupList', component: () => import('@/pages/Group/GroupList.vue'), meta: { requiresAuth: true } },
  { path: '/groups/:gno', name: 'GroupDetail', component: () => import('@/pages/Group/GroupDetail.vue'), meta: { requiresAuth: true } },
  { path: '/groups/:gno/edit', name: 'GroupEdit', component: () => import('@/pages/Group/GroupEdit.vue'), meta: { requiresAuth: true } },
  { path: '/groups/create', name: 'GroupCreate', component: () => import('@/pages/Group/GroupCreate.vue'), meta: { requiresAuth: true } },
  // { path: '/groups/:gno/members', component: () => import('@/pages/Group/GroupMembers.vue'), meta: { requiresAuth: true } },
  // { path: '/groups/:gno/trips', component: () => import('@/pages/Group/GroupTrips.vue'), meta: { requiresAuth: true } },

  // // 게시글
  { path: '/feed', component: () => import('@/pages/Feed/Feed.vue') },
  // { path: '/boards`/search', component: () => import('@/pages/Feed/Board/BoardSearch.vue') },
  // { path: '/boards`/:bno', component: () => import('@/pages/Feed/Board/BoardDetail.vue') },
  { path: '/boards/write', name: 'BoardWrite', component: () => import('@/pages/Feed/Board/BoardWrite.vue'), meta: { requiresAuth: true } },
  { path: '/boards/edit/:bno', name: 'BoardEdit', component: () => import('@/pages/Feed/Board/BoardEdit.vue'), meta: { requiresAuth: true } },

  // 공지사항
  { path: '/support/notices', name: 'NoticeList', component: () => import('@/pages/Support/Notice/NoticeList.vue') },
  { path: '/support/notices/detail/:id', name: 'NoticeDetail', component: () => import('@/pages/Support/Notice/NoticeDetail.vue') },
  { path: '/support/notices/write', name: 'NoticeWrite', component: () => import('@/pages/Support/Notice/NoticeWrite.vue'), meta: { requiresAuth: true } },
  { path: '/support/notices/edit/:id', name: 'NoticeEdit', component: () => import('@/pages/Support/Notice/NoticeEdit.vue'), meta: { requiresAuth: true } },

  // QnA
  { path: '/support/qnas', name: 'QnaList', component: () => import('@/pages/Support/Qna/QnaList.vue') },
  { path: '/support/qnas/detail/:id', name: 'QnaDetail', component: () => import('@/pages/Support/Qna/QnaDetail.vue') },
  { path: '/support/qnas/write', name: 'QnaWrite', component: () => import('@/pages/Support/Qna/QnaWrite.vue'), meta: { requiresAuth: true } },
  { path: '/support/qnas/edit/:id', name: 'QnaEdit', component: () => import('@/pages/Support/Qna/QnaEdit.vue'), meta: { requiresAuth: true } },

  // // 여행
  { path: '/trips', name: 'TripList', component: () => import('@/pages/Trip/TripList.vue'), meta: { requiresAuth: true } },
  { path: '/trips/:tno', name: 'TripDetail', component: () => import('@/pages/Trip/TripDetail.vue'), meta: { requiresAuth: true } },
  { path: '/trips/write', name: 'TripWrite', component: () => import('@/pages/Trip/TripWrite.vue'), meta: { requiresAuth: true } },
  { path: '/trips/edit/:tno', name: 'TripEdit', component: () => import('@/pages/Trip/TripEdit.vue'), meta: { requiresAuth: true } },
  // { path: '/trips/:tno/plans', component: () => import('@/pages/Trip/TripPlans.vue'), meta: { requiresAuth: true } },
  // { path: '/groups/:gno/trips/:tno', component: () => import('@/pages/Trip/TripCopy.vue'), meta: { requiresAuth: true } },

  // // 일정
  // { path: '/plans', component: () => import('@/pages/Plan/PlanList.vue') },
  // { path: '/plans/:pno', component: () => import('@/pages/Plan/PlanDetail.vue') },
  // { path: '/plans/write', component: () => import('@/pages/Plan/PlanWrite.vue'), meta: { requiresAuth: true } },
  // { path: '/plans/edit/:pno', component: () => import('@/pages/Plan/PlanEdit.vue'), meta: { requiresAuth: true } },

  // 고객센터
  { path: '/support', name: 'Support', component: () => import('@/pages/Support/Support.vue') },

  // 지도
  { path: '/map', component: () => import('@/pages/Map/Map.vue') },

  // 홈
  { path: '/', component: () => import('@/pages/Home.vue') },
]


const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: setupLayouts(routes),
})

// ✅ 네비게이션 가드 적용
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  const isLoggedIn = userStore.isLoggedIn

  if (to.meta.requiresAuth && !isLoggedIn) {
    return next('/login')
  }
  next()
})

// Workaround for https://github.com/vitejs/vite/issues/11804
router.onError((err, to) => {
  if (err?.message?.includes?.('Failed to fetch dynamically imported module')) {
    if (!localStorage.getItem('vuetify:dynamic-reload')) {
      console.log('Reloading page to fix dynamic import error')
      localStorage.setItem('vuetify:dynamic-reload', 'true')
      location.assign(to.fullPath)
    } else {
      console.error('Dynamic import error, reloading page did not fix it', err)
    }
  } else {
    console.error(err)
  }
})

router.isReady().then(() => {
  localStorage.removeItem('vuetify:dynamic-reload')
})

export default router
