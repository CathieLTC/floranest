import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './assets/styles/global.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './components/layout/Navbar.vue'
import router from './router/index.js'

const pinia = createPinia();
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(pinia);
app.use(router)
app.use(ElementPlus)
app.mount('#app')
