import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus, {ElMessage} from 'element-plus'
import 'element-plus/dist/index.css'
import * as VueRouter from 'vue-router'
import routes from './plugins/routes.ts'
import './assets/css/global.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'


const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
const router = VueRouter.createRouter({
    history: VueRouter.createWebHistory(),
    routes,
})
router.beforeEach((to,from,next)=>{
    if (to.path==='/login') return next()
    let token = window.sessionStorage.getItem('token');
    if (!token){
        ElMessage.error('登录状态失效,请重新登录')
        return next('/login')
    }
    next()
})
app.use(router)
app.use(ElementPlus)

app.mount('#app')
