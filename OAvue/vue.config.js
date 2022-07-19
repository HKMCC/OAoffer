// const { defineConfig } = require('@vue/cli-service')
// module.exports= defineConfig({
//   transpileDependencies: true,
  
// });
// defineConfig;

let proxy={}
proxy['/']={
  //websocker
  ws:false,
  //目标地址
  target:'http://localhost:8081',
  //发生请求头host会被设置成target
  changeDrigin: true,
  //不重写请求地址
  pathReWrite:{
    '^/':'/'
  }
};
//websoc代理跨域
// proxy['/ws']={
//   ws:true,
//   target: 'ws://localhost:8081'
// };

module.exports = {
  devServer:{
    host:'localhost',
    port:8080,
    proxy:proxy
  },
  lintOnSave:false
}
