<template>
  <!-- <div class="d-flex flex-column align-center"> -->
    <v-row align="center" justify="space-between" class="mb-2">
      <!-- 글쓰기 버튼 -->
      <v-col cols="6">
        <slot name="write"></slot>
      </v-col>
      <v-col cols="6"> 
        <!-- 검색창 -->
        <AppSearchBar
          :loading="loading"
          id="search-input"
          class="align-self-end"
          @search="onSearch($event)"
        />
      </v-col>
    </v-row>

    <!-- 목록 테이블 -->
    <v-table class="w-100 text-center">
      <thead class="bg-grey-lighten-3">
        <tr>
            <th v-for="(header, i) in headers" :key="i" class="text-center">
              {{ header.label }}
            </th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in paginatedItems" :key="item.id" @click="showDetail(item.id)">
          <td v-for="(header, i) in headers" :key="i">
            <slot :name="header.key" :item="item">
            <template v-if="header.key === 'id'">
              {{ startIndex + index + 1 }}
            </template>
            <template v-else-if="header.key === 'title'">
                <v-tooltip 
                  location="top" 
                  open-delay="500" 
                  transition="fade-transition"
                  :content-class="'custom-tooltip'"
                >
                  <template #activator="{ props }">
                    <span v-bind="props" class="ellipsis-cell">
                      {{ item[header.key] }}
                    </span>
                  </template>
                  <span>{{ item[header.key] }}</span>
                </v-tooltip>
            </template>
            <template v-else-if="header.key === 'createdAt'">
              {{ new Date(item[header.key]).toLocaleDateString() }}
            </template>
            <template v-else-if="header.key === 'updatedAt'">
              {{ new Date(item[header.key]).toLocaleDateString() }}
            </template>
            <template v-else-if="header.key === 'manage'">
                <template v-if="item[header.key]">
                    <slot name="manage" :item="item"></slot>
                </template>
            </template>
            <template v-else>
              {{ item[header.key] }}
            </template>
            </slot>
          </td>
        </tr>
        <tr v-if="filteredItems.length === 0">
          <td :colspan="headers.length">게시글이 없습니다.</td>
        </tr>
      </tbody>
    </v-table>
    
    <!-- 페이지네이션 -->
    <div class="d-flex justify-center">
      <v-pagination
      v-model="currentPage"
      :length="pageCount"
      :total-visible="5"
      rounded
      color="primary"
      />
    </div>

  <!-- </div> -->
</template>

<script setup>
  import AppSearchBar from '@/components/common/AppSearchBar.vue'

  import { ref, computed } from 'vue'
  import { useRouter } from 'vue-router'
  const router = useRouter()

  const props = defineProps({
    items: Array,
    headers: Array, // [{ label: '제목', key: 'title' }, ...]
    domain: String, // e.g., 'notice', 'qna', ...
  })

  const currentPage = ref(1)
  const itemsPerPage = 5

  const filteredItems = computed(() => {
    return props.items
  })

  const pageCount = computed(() => {
    return Math.ceil(filteredItems.value.length / itemsPerPage)
  })

  const startIndex = computed(() => (currentPage.value - 1) * itemsPerPage)

  const paginatedItems = computed(() => {
    return filteredItems.value.slice(startIndex.value, startIndex.value + itemsPerPage)
  })

  // 검색어 입력 시 동작 
  const emit = defineEmits(['search'])
  const loaded = ref(false)
  const loading = ref(false)

  const onSearch = async (query) => {
    currentPage.value = 1 // 검색 시 첫 페이지로 이동
    loaded.value = false
    loading.value = true
    await emit('search', query)
    loaded.value = true
    loading.value = false
  }
  
  // 상세보기 클릭 시 동작
  const showDetail = (id) => {
    if (!props.domain) return
    if (!id) return
    console.log('상세보기 클릭:', id)
    router.push({ path: `${props.domain}/detail/${id}`})
  }

</script>

<style scoped>

  tr:hover {
    background-color: #f5f5f5;
  }

  th {
    background-color: #f5f5f5;
    font-weight: bold;
  }

  th, td {
    text-align: center;
    max-width: 200px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  #search-input {
    max-width: 300px;
    align-self: flex-end;
    margin-left: auto;
    flex-shrink: 1;
  }

  .ellipsis-cell {
  max-width: 200px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
</style>
