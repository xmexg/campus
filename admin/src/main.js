import { createApp } from 'vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
import App from './App.vue'
import router from './router'
import './style.css'
import './styles/ruoyi-page.css'
import * as echarts from 'echarts';

const app = createApp(App)
app.config.globalProperties.$echarts = echarts;
app.use(router)
app.use(ElementPlus, { locale: zhCn })
app.mount('#app')
