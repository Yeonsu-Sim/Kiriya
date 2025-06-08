<template>
  <v-app-bar app color="primary">
    <v-row align="center" justify="center">
      <div class="d-flex justify-space-between w-100 px-10" style="max-width:1000px;">
        <!-- 로고 -->
        <div class="d-flex flex-row align-center">
          <router-link class="router-link" to="/">
            <v-app-bar-title class="logo-title text-md-center">Kiriya</v-app-bar-title>
          </router-link>
        </div>

        <!-- 데스크탑 메뉴 -->
        <div align="center" class="d-none d-md-flex ga-4" justify="end">
          <RouterLink
            v-for="item in visibleMenu"
            :key="item.to"
            class="router-link"
            :to="item.to"
          >
            {{ item.label }}
          </RouterLink>
        </div>
      </div>
    </v-row>

    <!-- 모바일 메뉴 버튼 -->
    <v-app-bar-nav-icon
      class="d-md-none position-absolute right-0"
      @click="drawer = !drawer"
    />
  </v-app-bar>

  <!-- 모바일 드로어 메뉴 -->
  <v-navigation-drawer
    v-model="drawer"
    class="d-md-none h-auto"
    color="primary"
    location="top"
    temporary
  >
    <div class="pa-4 ga-4 d-flex flex-column" v-if="drawer">
      <RouterLink
        v-for="item in visibleMenu"
        :key="'mobile-' + item.to"
        class="router-link"
        :to="item.to"
      >
        {{ item.label }}
      </RouterLink>
    </div>
  </v-navigation-drawer>
</template>

<script setup>
  import { computed, ref } from 'vue'
  import { useUserStore } from '@/stores/user'

  const drawer = ref(false)
  const userStore = useUserStore()
  const isLoggedIn = computed(() => userStore.isLoggedIn)

  // 공통 메뉴 정의
  const baseMenu = [
    { label: '관광지도', to: '/map' },
    { label: '피드', to: '/feed' },
    { label: '고객센터', to: '/support' },
  ]

  // 로그인 상태별 추가 메뉴
  const visibleMenu = computed(() => {
    return [
      ...baseMenu,
      ...(isLoggedIn.value
        ? [
          { label: '그룹', to: '/groups'},
          { label: '여행', to: '/trips'},
          { label: '로그아웃', to: '/logout' },
          { label: '마이페이지', to: '/myPage' },
          
        ]
        : [
          { label: '로그인', to: '/login' },
          { label: '회원가입', to: '/signup' },
        ]),
    ]
  })
</script>

<style lang="scss" scoped>
  .logo-title {
    font-size: 24px;
    font-weight: bold;
    color: white;
    text-decoration: none;
  }
  .router-link:hover {
    color: #BDBDBD;
  }
</style>
