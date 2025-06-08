<template>
  <v-sheet
    :elevation="4"
    :height="height"
    :width="width"
    :color="color"
    rounded
  >
    <!-- 탭 헤더 -->
    <v-tabs
      v-model="curTab"
      bg-color="transparent"
      color="primary"
      align-tabs="center"
      grow
      class="rounded-lg"
    >
      <v-tab
        v-for="item in items"
        :key="item.label"
        :value="item.value"
        class="tab-text"
        @click="$emit('tabChange', item.value)"
      >
        {{ item.label }}
      </v-tab>
    </v-tabs>

    <!-- 탭 콘텐츠 -->
    <v-window v-model="curTab">
      <v-window-item
        v-for="item in items"
        :key="item.label"
        :value="item.value"
        rounded
        class="pa-0"
      >
        <!-- 이 안에 슬롯이나 정적인 콘텐츠를 넣을 수 있음 -->
        <component 
          :is="item.content" 
          @select="$emit('select', $event)"
          @like="$emit('like', $event)"
          @unlike="$emit('unlike', $event)"
        />
      </v-window-item>
    </v-window>
  </v-sheet>
</template>

<script setup>
import { defineEmits } from 'vue';
const emit = defineEmits(['select', 'like', 'unlike', 'tabChange'])
const props = defineProps({
    height: Number,
    width: Number,
    color: String,
    items: Array,
    tab: {
      type: String,
      required: true,
    },
  })
const curTab = ref(props.tab);

</script>

<style scoped>
.tab-text {
  color: gray;
}
</style>
