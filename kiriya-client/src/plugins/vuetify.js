/* eslint-disable no-undef */
// Styles
import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

// Composables
import { createVuetify } from 'vuetify'

import { aliases, mdi } from 'vuetify/iconsets/mdi'

const vuetify = createVuetify({
  icons: {
    defaultSet: 'mdi',
    aliases,
    sets: { mdi },
  },
  theme: {
    // defaultTheme: 'light',
    defaultTheme: 'africaTheme',
    themes: {
      africaTheme: {
        dark: false,
        colors: {
          primary: '#7F80BF',
          secondary: '#EEEEEE',
          accent: '#FFD966',
          background: '#FAFAFA',
          surface: '#FFFFFF',
          error: '#EF5350',
          info: '#64B5F6',
          success: '#7CB342',
          warning: '#FBC02D',

          // 🏞 Custom marker theme colors (pastel & semantic)
          attraction: '#A5D6A7',      // 관광지
          culture: '#FFD180',         // 문화시설
          festival: '#FFAB91',        // 축제/공연/행사
          course: '#D09FDF',          // 여행코스
          leisure: '#86b5fd',         // 레포츠
          lodging: '#B0BEC5',         // 숙박
          shopping: '#F48FB1',        // 쇼핑
          food: '#81D4FA',            // 음식점

        },
      },
    },
  },
})

export default vuetify
