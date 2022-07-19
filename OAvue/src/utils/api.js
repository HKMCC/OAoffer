import axios from 'axios';
import { Message } from 'element-ui';
import router from '../router';



//请求拦截器处理token存放
axios.interceptors.request.use(config => {
    if (window.sessionStorage.getItem("tokenStr")) {
        // console.log(window.sessionStorage.getItem('tokenStr'))
        config.headers['Authorization'] = window.sessionStorage.getItem('tokenStr');
        // console.log(config.headers)
    }
    return config;
}, error => {
    console.log(error);
})




//拦截器
axios.interceptors.response.use(success => {
    //200已经发到后端
    if (success.status && success.status == 200) {
        if (success.data.code == 500 || success.data.code == 401 || success.data.code == 403) {
            Message.error({ message: success.data.message });
            return;
        }
        if (success.data.message) {
            Message.success({ message: success.data.message });
        }
    }
    return success.data;
},
    error => {
        //接口都访问失败了
        if (error.response.code == 504 || error.response.code == 404) {
            Message.error({ message: '服务器被吃了( ╯□╰ )' })
        } else if (success.data.code == 403) {
            Message.error({ message: '权限不足( ╯□╰ )' })
        } else if (success.data.code == 401) {
            Message.error({ message: '尚未登录你想干嘛？？？？' })
            router.replace('/');
        }
        else {
            if (error.response.data.message) {
                Message.error({ message: error.response.data.message })
            }
            else {
                Message.error({ message: '未知错误' });
            }

        }
        return;
    });

let base = '';


export const postRequest = (url, params) => {
    return axios({
        method: 'post',
        //是真的牛逼''  ``
        url: `${base}${url}`,
        data: params
    })

}

export const putRequest = (url, params) => {
    return axios({
        method: 'put',
        //是真的牛逼''  ``
        url: `${base}${url}`,
        data: params
    })

}
export const getRequest = (url, params) => {
    return axios({
        method: 'get',
        //是真的牛逼''  ``
        url: `${base}${url}`,
        data: params
    })

}
export const deleteRequest = (url, params) => {
    return axios({
        method: 'delete',
        //是真的牛逼''  ``
        url: `${base}${url}`,
        data: params
    })

}

