<template>
  <div>
    <el-form ref="loginForm" 
    v-loading="loading"
    element-loading-text="拼命加载中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"
    :model="loginForm" 
    class="loginContainer"
     :rules="rules">
      <h3 class="loginTitle">系统登录</h3>
      <el-form-item prop="username">
        <el-input type="text" v-model="loginForm.username" placeholder="请输入用户名"></el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input type="password" v-model="loginForm.password" placeholder="请输入密码"></el-input>
      </el-form-item>
      <el-form-item prop="code">
        <el-input
          type="text"
          v-model="loginForm.code"
          placeholder="点击图片更换验证码"
          style="width: 250px;margin-right: 5px"
        ></el-input>
        <img :src="captchaUrl" @click="updateCaptcha()" />
      </el-form-item>
      <el-checkbox class="loginRemember" v-model="checked">记住我</el-checkbox>
      <el-button type="primary" style="width:100%" @click="submitLogin">登录</el-button>
    </el-form>
  </div>
</template>

<script>

// import { Checkbox } from "element-ui";
import { Form } from 'element-ui';
export default {
  name: "Login",
  data() {
    return {
      captchaUrl:'/captcha?time='+new Date(),
      loginForm: {
        username: "admin",
        password: "zx990416",
        code: ""
      },
      loading:false,
      checked: true,
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }],
        code: [{ required: true, message: "请输入验证码", trigger: "blur" }]
      }
    };
  },
  methods: {
    updateCaptcha() {
      this.captchaUrl = '/captcha?time='+new Date();
    },
    submitLogin() {
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          // this.loading=true,
          //存储用户token
          this.postRequest('/login',this.loginForm).then(resp=>{
            if(resp){
            this.loading=false;
            const tokenStr = resp.result.tokenHead+resp.result.token;
            window.sessionStorage.setItem('tokenStr',tokenStr);
            // 拿到用户要跳转的路径
              let path = this.$route.query.redirect;
              // 用户可能输入首页地址或错误地址，让他跳到首页，否则跳转到他输入的地址
              this.$router.replace((path === '/' || path === undefined) ? '/home' : path)
            
            }
          })
        } else {
          this.$message.error("请输入所有字段！");
        }
      });
      // alert("11111")
    }
  }
};
</script>

<style>
.loginContainer {
  border-radius: 15px;
  margin: 180px auto;
  width: 350px;
  padding: 15px 35px;
  background: #ffb0b0e7;
  border: 1px solid #eaeaea;
  box-shadow: 0 0 25px #e95d5d;
}
.loginTitle {
  text-align: center;
  margin: 0 auto 40px auto;
}
.loginRemember {
  text-align: left;
  margin: 0 0 15px 0;
}
/*验证码*/
.el-form-item__content {
  display: flex;
  align-items: center;
}
</style>