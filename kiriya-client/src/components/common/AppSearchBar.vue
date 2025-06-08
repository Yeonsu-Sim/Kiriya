<!-- 검색창 -->
 <template>
  <div class="d-flex flex-column">
    <div class="d-flex align-center">
      <v-text-field
        v-model="searchQuery"
        prepend-inner-icon="mdi-magnify"
        density="compact"
        label="검색어를 입력하세요..."
        variant="solo"
        hide-details
        single-line
        clearable
        @click:append-inner="search"
        @keyup.enter.prevent="search"
      ></v-text-field>
    </div>
  </div>
</template>

<script setup>
  import { ref } from 'vue'
  import { useRoute } from 'vue-router'
  
  const route = useRoute()
  
  const searchQuery = ref(route.query.text || route.query.query || '')

  watch(
    () => route.query,
    (newQuery) => {
      searchQuery.value = newQuery.query
    }
  )

  const emit = defineEmits(['search'])

  onMounted(() => {
    if (searchQuery.value) {
      emit('search', searchQuery.value)
    }
  })

  const search = async () => {
    await emit('search', searchQuery.value)
  }
  
</script>