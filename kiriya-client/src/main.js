// Plugins
import '@/plugins/axios'
import { registerPlugins } from '@/plugins'

// Components
import App from './App.vue'

// Composables
import { createApp } from 'vue'
import { createPinia } from 'pinia'

// Styles
import 'unfonts.css'
import '@/styles/global.scss'
import vuetify from '@/plugins/vuetify'
import { useKakao } from 'vue3-kakao-maps/@utils';

const kakaoKey = import.meta.env.VITE_KAKAO_JS_KEY
useKakao(kakaoKey, ['clusterer', 'services', 'drawing']);

const app = createApp(App)
registerPlugins(app)
app.use(createPinia())
app.use(vuetify)
app.mount('#app')

