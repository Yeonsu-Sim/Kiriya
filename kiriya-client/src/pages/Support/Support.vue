<template>
  <v-row justify="center" align="center" class="pa-4 ma-10" style="max-height: 1000px;">
    <v-card color="surface" class="w-100 pa-4" style="max-width: 1000px; max-height: 1000px;">
      <h2 class="text-primary text-center ma-4">고객센터</h2>
      <v-tabs
        v-model="tab"
        bg-color="transparent"
        color="primary"
        align-tabs="center"
      >
        <v-tab class="tab-text"
          v-for="item in items"
          :key="item.label"
          :value="item.value"
          @click="changeTab(item.value)"
        >
          {{ item.label }}
        </v-tab>
      </v-tabs>
  
      <v-tabs-window v-model="tab" class="pa-4">
        <v-tabs-window-item
          v-for="item in items"
          :key="item.label"
          :value="item.value"
          class="pa-4"
        >
          <v-card color="surface" flat>
            <component :is="item.component"></component>
          </v-card>
        </v-tabs-window-item>
      </v-tabs-window>
    </v-card>
  </v-row>
</template>

<script setup>
	import QnaList from './Qna/QnaList.vue'
	import NoticeList from './Notice/NoticeList.vue'

  import { ref } from 'vue'
  import { useRoute, useRouter } from 'vue-router'
  const route = useRoute()
  const router = useRouter()

  // const tab = ref('QnA')
  const tab = ref(route.query.tab || 'notice')

  const items = [
    { label: '공지사항', value: 'notice', component: NoticeList },
    { label: 'QnA', value: 'QnA', component: QnaList }
  ]

  const changeTab = (tab) => {
    router.push({ tab: route.query.tab, query: { tab } })
  }
</script>

<style lang="scss" scoped>
  .tab-text {
    color: gray;
  }
</style>
