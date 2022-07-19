import Vue from 'vue'
import VueRouter from 'vue-router'
import Login from '@/views/Login'
import Home from "@/views/Home";
import FriendChat from "@/views/chat/FriendChat";
import AdminInfo from "@/views/AdminInfo";



Vue.use(VueRouter)


const routes = [
    {
        path: '/',
        name: 'Login',
        component: Login,
        hidden:true
    },
    {
        path: '/home',
        name: 'Home',
        component: Home,
        children:[
            {
                path: '/chat',
                name: '在线聊天',
                component: FriendChat,
            },
            {
                path: '/userInfo',
                name: '个人中心',
                component: AdminInfo,
            }
        ]
    }

]

const router = new VueRouter({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router
