<template>
  <div class="app-list">
    <div class="create-btn" @click="$emit('create')">
      <v-icon size="small">mdi-plus</v-icon>
      <span>새 {{ domain }} 만들기</span>
    </div>

    <v-divider class="my-2" />

    <div
      v-for="(item, index) in items"
      :key="item.id"
      class="list-item d-flex justify-space-between align-center"
      @click="$emit('select', item.id)"
      >
      <div class="d-flex align-center ga-3 text-truncate">
        <div class="d-flex flex-column">
          <div class="d-flex align-center ga-1">
            <span class="font-weight-medium text-primary">{{ item.title }}</span>
            <v-icon v-if="item.isPrivate" size="14" color="grey">mdi-lock</v-icon>
          </div>
          <span class="text-subtitle-2 font-weight-bold" style="color:gray;"
          >{{ item.description }}</span>
          <span class="text-body-2" style="color:gray;"
          >{{ item.date }}</span>
          <div class="d-flex ga-2 mt-1 text-grey text-caption">
            <div v-if="item.places !== undefined">
              <v-icon size="14">mdi-map-marker</v-icon> {{ item.places }}
            </div>
            <div v-if="item.members !== undefined">
              <v-icon size="14">mdi-account</v-icon> {{ item.members }}
            </div>
          </div>
        </div>
      </div>

      <v-menu
        location="top start"
        offset="5"
        class="text-body-2"
      >
        <template #activator="{ props: menuProps }">
          <v-btn icon variant="plain" size="small" v-bind="menuProps">
            <v-icon>mdi-dots-vertical</v-icon>
          </v-btn>
        </template>

        <v-list density="compact" style="max-width: 100px;">
          <v-list-item @click.stop="$emit('edit', item.id)">
            <v-list-item-title>✏️ 수정</v-list-item-title>
          </v-list-item>
          <v-list-item @click.stop="$emit('delete', item.id)">
            <v-list-item-title class="text-error">🗑️ 삭제</v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  items: {
    type: Array,
    default: () => []
  },
  domain: String
})

defineEmits(['create', 'select', 'edit', 'delete'])

</script>

<style scoped>
.app-list {
  padding: 12px;
}

.create-btn {
  display: flex;
  align-items: center;
  color: gray;
  font-weight: 500;
  cursor: pointer;
  padding: 8px;
  border-radius: 8px;
  transition: background 0.3s;
}
.create-btn:hover {
  background: #f5f5f5;
}

.list-item {
  padding: 6px 4px;
  border-bottom: 1px solid #eee;
  transition: background-color 0.3s ease, box-shadow 0.3s ease, transform 0.15s ease;
  cursor: pointer;
  border-radius: 6px;
}
.create-btn {
  transition: background-color 0.3s ease, box-shadow 0.3s ease, transform 0.15s ease;
}
.list-item:hover,
.create-btn:hover {
  background-color: #f8f8f8;
}
.list-item:active,
.create-btn:active {
  transform: scale(0.98);
}
</style>
