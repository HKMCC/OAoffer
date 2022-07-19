import Vue from 'vue'
import App from './App.vue'
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import router from './router'
import store from './store/index'
import "font-awesome/css/font-awesome.css"


import {postRequest} from "./utils/api";
import {putRequest} from "./utils/api";
import {getRequest} from "./utils/api";
import {deleteRequest} from "./utils/api";
import { initMenu } from './utils/menus';
import {downloadRequest} from "./utils/download";


Vue.config.productionTip = false
Vue.use(ElementUI);
Vue.prototype.postRequest = postRequest;
Vue.prototype.putRequest = putRequest;
Vue.prototype.getRequest = getRequest;
Vue.prototype.deleteRequest = deleteRequest;
Vue.prototype.downloadRequest = downloadRequest;

//全局前置守卫
router.beforeEach((to,from,next)=>{
    if(window.sessionStorage.getItem('tokenStr')){
        initMenu(router,store);
        //如果用户不存在
        if(!window.sessionStorage.getItem('user')){
            //发送请求获取用户信息
            return postRequest('/admin/info').then(resp=>{
                
                if(resp){
                    window.sessionStorage.setItem('user',JSON.stringify(resp))
                    store.commit('INIT_ADMIN',resp)
                    next();
                }
            })
        }
        next();
    }else{
        if (to.path === '/') {
            next()
        }else{
            next('/?redirect='+to.path);
        }
    }
})


new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
