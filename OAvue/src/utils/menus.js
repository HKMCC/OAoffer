import {getRequest} from './api';
// import router from '@/router';
// import store from '@/store';

//初始化菜单
export const initMenu = (router,store)=>{
        if(store.state.routes.length>0){
            return;
        }
    getRequest('/system/cfg/menu').then(data=>{
        if(data){   
            //格式化好的Router
            let fmtRoutes = formatRoutes(data);
            //添加到路由
            router.addRoutes(fmtRoutes);
            //数据存vuex
            store.commit('initRoutes',fmtRoutes);
            //连接websocket
            // store.dispatch('connect')
        }
    })
}


// export const formatRoutes = (data) =>{
//     let fmtRoutes=[];

//     data.forEach(router =>{
//         let{
//             path,
//             component,
//             name,
//             iconCls,
//             children,
//         } = router;C
//         if(children && children instanceof Array){
//             children = formatRoutes(children)C
//         }

//         let fmRouter = {
//             path:path,
//             name:name,
//             iconCls:iconCls,
//             children:children,
//             component(resolve){
//                 if(component.startsWith('Home')){
//                     require(['../views/'+component+'.vue'].resolve)
//                 }else if(component.startsWith('Emp')){
//                     require(['../views/emp/'+component+'.vue'].resolve)
//                 }else if(component.startsWith('Per')){
//                     require(['../views/per/'+component+'.vue'].resolve)
//                 }else if(component.startsWith('Sal')){
//                     require(['../views/sal/'+component+'.vue'].resolve)
//                 }else if(component.startsWith('Star')){
//                     require(['../views/sta/'+component+'.vue'].resolve)
//                 }else if(component.startsWith('Sys')){
//                     require(['../views/sys/'+component+'.vue'].resolve)
//                 }
                
//             }
//         }
//         fmtRoutes.push(fmRouter);
//     })
//     return fmtRoutes;
// }
export const formatRoutes = (routes) => {
    let fmtRoutes = []
    routes.forEach(router => {
        let {
            path,
            component,
            name,
            iconCls,
            children
        } = router;
        // 如果有 children 并且类型是数组
        if (children && children instanceof Array) {
            // 递归
            children = formatRoutes(children)
        }
        // 单独对某一个路由格式化 component
        let fmRouter = {
            path: path,
            name: name,
            iconCls: iconCls,
            children: children,
            component(resolve) {
                // 判断组件以什么开头，到对应的目录去找
                if (component.startsWith('Home')) {
                    require(['@/views/' + component + '.vue'], resolve);
                }else if (component.startsWith('Emp')) {
                    require(['@/views/emp/' + component + '.vue'], resolve);
                }else if (component.startsWith('Per')) {
                    require(['@/views/per/' + component + '.vue'], resolve);
                }else if (component.startsWith('Sal')) {
                    require(['@/views/sal/' + component + '.vue'], resolve);
                }else if (component.startsWith('Sta')) {
                    require(['@/views/sta/' + component + '.vue'], resolve);
                }else if (component.startsWith('Sys')) {
                    require(['@/views/sys/' + component + '.vue'], resolve);
                }
            }
        }
        fmtRoutes.push(fmRouter)
    })
    return fmtRoutes
}

