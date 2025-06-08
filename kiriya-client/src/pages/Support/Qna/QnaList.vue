<template>
  <AppTable :headers="headers" :items="items" domain="/support/qnas"
		@search="onSearch($event)"
	>
		<template #write>
			<v-btn color="primary" @click="$router.push({ name: 'QnaWrite' })">
				글쓰기
			</v-btn>
		</template>

		<template #manage="{ item }">
			<v-btn
				v-if="userStore.profile?.mno === item.mno" 
				size="small"
				color="error"
				variant="tonal"
				@click.stop.prevent="() => onManageClick(item.id)"
			>
				삭제
			</v-btn>
		</template>
	</AppTable>
</template>

<script setup>
	import AppTable from '@/components/common/AppTable.vue'
	import { ref, onMounted } from 'vue'
	import axios from 'axios'
	import { useRouter, useRoute } from 'vue-router'
	import { useUserStore } from '@/stores/user'

	const userStore = useUserStore()

	const headers = [
    { label: '번호', key: 'id' },
    { label: '제목', key: 'title' },
    { label: '작성자', key: 'author' },
    { label: '작성일', key: 'createdAt' },
    { label: '답변상태', key: 'status' },
    { label: '관리', key: 'manage' },
	]

	const items = ref([])
	const router = useRouter()
	const route = useRoute()



	onMounted(async () => {
		try {
			await onSearch(route.query.text || '')
		} catch (e) {
			console.error('QnA 불러오기 실패:', e)
		}
	})

	const onSearch = async (query) => {
		console.log('검색어:', query)
		try {
			const res = await axios.get(`http://localhost:8080/api/qnas/questions`)
			console.log('검색 결과:', res.data.data)
      /** 임시로 만든 검색 기능 */
      if (query) {
        res.data.data = res.data.data.filter(item => item.title.includes(query))
      }
			items.value = res.data.data.map((item) => ({
				...item,
				id: item.questionNo,
				manage: true,
			}))
			router.push({ query: { tab: route.query.tab, text: query } })
		} catch (e) {
			console.error('검색 실패:', e)
		}
	}

	const onManageClick = async (id) => {
		if (!id) return
		console.log('삭제 클릭:', id)
		try {
			await axios.delete(`http://localhost:8080/api/qnas/questions/${id}`)
			items.value = items.value.filter(item => item.id !== id)
		} catch (e) {
			console.error('삭제 실패:', e)
		}
	}
</script>

<style scoped>
</style>