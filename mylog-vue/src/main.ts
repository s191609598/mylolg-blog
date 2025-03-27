import { createApp } from 'vue'

import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue'
import { createPinia } from 'pinia'
import { createPersistedState } from 'pinia-plugin-persistedstate'
import 'ant-design-vue/dist/reset.css'
import '@/access/access.ts'
import { MdEditor } from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

const app = createApp(App)

// app.config.globalProperties.$baseUrl = 'http://192.168.124.173:7333'
// app.config.globalProperties.$baseUrl = 'http://localhost:7333'

const pinia = createPinia()
// 使用 pinia-plugin-persistedstate 插件
pinia.use(createPersistedState())
app.use(pinia)
app.use(router)
app.use(Antd)
app.use(MdEditor)
app.use(ElementPlus)
app.mount('#app')
