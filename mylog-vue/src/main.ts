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

declare global {
  interface Window {
    _security_cache?: ArrayBuffer
  }
}

const app = createApp(App)
const pinia = createPinia()
const triggerDefense = () => {
  // 原有显示警告逻辑

  // 新增主动防御措施
  history.pushState(null, '', location.href) // 禁用后退
  window.addEventListener('beforeunload', e => {
    e.preventDefault()
    e.returnValue = ''
  })

  // 持续干扰（原有逻辑加强）
  const createDebugLoop = () => {
    const debug = () => {
      try {
        debugger
        setInterval(debug, 100)
        new Function('debugger')()
      } catch {}
    }
    debug()
  }
  createDebugLoop()
}
// ================= 精准检测防误触系统 =================
const SecurityGuard = {
  install() {
    // 防御配置（新增快捷键配置）
    const config = {
      checkInterval: 500,
      memoryFillSize: 2,
      crashMessage: '安全警告：非法调试行为已被阻止',
      enableMemoryAttack: false,
      requiredTriggers: 2,
      blockShortcuts: ['F12', 'Control+Shift+I', 'Control+Shift+C', 'Control+U'] // 新增快捷键拦截配置
    }

    // 生产环境检测（保持不变）
    const isProductionEnv = (): boolean => {
      return import.meta.env.MODE === 'production' // Vite 专用环境变量
    }
    // 增强防御系统初始化
    const initSecuritySystem = () => {
      if (!isProductionEnv()) return

      // 快捷键拦截系统（新增）
      const bindShortcutBlocker = () => {
        // 通用事件处理器
        const handler = (e: KeyboardEvent) => {
          const keyCombination = [
            e.ctrlKey ? 'Control+' : '',
            e.shiftKey ? 'Shift+' : '',
            e.altKey ? 'Alt+' : '',
            e.key
          ].join('').replace(/\+$/,'')

          if (config.blockShortcuts.includes(keyCombination)) {
            e.preventDefault()
            e.stopPropagation()
            if (!triggered) triggerDefense()
          }
        }

        // 多维度事件监听
        document.addEventListener('keydown', handler) // 普通按键监听
        window.addEventListener('keydown', handler)   // 窗口级监听
      }

      // 右键菜单拦截（新增）
      const disableContextMenu = () => {
        document.addEventListener('contextmenu', e => {
          e.preventDefault()
          if (!triggered) triggerDefense()
        })
      }

      // 防止新窗口打开开发者工具（新增）
      const protectNewWindow = () => {
        const originalWindow = window.open
        window.open = function(...args) {
          const newWindow = originalWindow.apply(this, args)
          if (newWindow) {
            newWindow.close()
            triggerDefense()
          }
          return null
        }
      }

      let triggered = false
      const check = () => { /* 原有检测逻辑 */ }

      bindShortcutBlocker()
      disableContextMenu()
      protectNewWindow()
      check()
    }

    initSecuritySystem()
  }
}


// ================= 挂载实例（保持不变） =================
pinia.use(createPersistedState())
app.use(pinia)
app.use(router)
app.use(Antd)
app.use(MdEditor)
app.use(ElementPlus)
// app.use(SecurityGuard)
app.mount('#app')
