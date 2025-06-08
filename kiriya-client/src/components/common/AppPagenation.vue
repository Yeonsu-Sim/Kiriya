<!-- 페이지네이션 -->
<template>
  <div class="d-flex justify-center">
    <v-pagination
    v-model="currentPage"
    :length="pageCount"
    :total-visible="5"
    rounded
    color="primary"
    />
  </div>
</template>

<script setup>
  import { computed, ref, watch } from 'vue'

  const props = defineProps({
    items: Array
  })
  const emit = defineEmits(['update:paginated'])

  const currentPage = ref(1)
  const itemsPerPage = 5

  const pageCount = computed(() => Math.ceil(props.items.length / itemsPerPage))
  const startIndex = computed(() => (currentPage.value - 1) * itemsPerPage)

  const paginatedItems = computed(() => {
    return props.items.slice(startIndex.value, startIndex.value + itemsPerPage)
  })

  // 부모에게 emit
  watch([paginatedItems, currentPage], () => {
    emit('update:paginated', paginatedItems.value)
  }, { immediate: true })
</script>
