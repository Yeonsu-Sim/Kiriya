<template>
    <v-card class="rounded-2xl" elevation="3">
      <v-img
        :src="trip.thumbnailUrl || defaultTripImage"
        height="180px"
        cover
        class="rounded-t-2xl"
      />
      <v-card-title class="text-h6 font-weight-bold">{{ trip.title }}</v-card-title>
      <v-card-subtitle class="text-caption text-grey-darken-1">
        {{ formatDate(trip.startDate) }} ~ {{ formatDate(trip.endDate) }}
      </v-card-subtitle>
      <v-card-text class="text-truncate">{{ trip.description }}</v-card-text>
      <v-card-actions>
        <v-btn variant="text" color="primary" @click="$emit('detail', trip.tno)">
          <v-icon start>mdi-eye</v-icon>상세보기
        </v-btn>
        <v-btn variant="text" color="error" @click="$emit('delete', trip.tno)">
          <v-icon start>mdi-delete</v-icon>삭제
        </v-btn>
      </v-card-actions>
    </v-card>
  </template>
  
  <script setup>
  import defaultTripImage from '@/assets/default-trip.jpg'
  const props = defineProps({
    trip: {
      type: Object,
      required: true
    }
  })
  const formatDate = (dateString) => {
    if (!dateString) return ''
    return dateString.split(' ')[0]
  }
  </script>
  