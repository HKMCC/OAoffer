<template>
  <div>
    <el-card class="box-card" style="width: 400px">
      <div slot="header" class="clearfix">
        <span>{{ adminInfo.name }}</span>
      </div>
      <div>
        <div style="display: flex;justify-content: center">
          <el-upload action="/admin/userface"
                     :headers="header"
                     :data="adminInfo"
                     :show-file-list="false"
                     :on-success="onSuccess">

            <img title="点击更换头像" :src="adminInfo.userFace" style="height: 100px;width: 100px;border-radius: 50px" alt="">
          </el-upload>

        </div>
        <div>电话号码
          <el-tag>{{ adminInfo.telephone }}}</el-tag>
        </div>
        <div>手机号码
          <el-tag>{{ adminInfo.phone }}}</el-tag>
        </div>
        <div>居住地址
          <el-tag>{{ adminInfo.address }}}</el-tag>
        </div>
        <div>用户标签
          <el-tag v-for="(r,index) in adminInfo.roles" :key="index">{{ r.nameZh }}}</el-tag>
        </div>
        <div style="display: flex;justify-content: space-around;margin-top: 10px">
          <el-button type="success" @click="showinfoEdit">修改信息</el-button>
          <el-button type="danger" @click="showpasswordEdit">修改密码</el-button>
        </div>

      </div>
    </el-card>
    <el-dialog
        title="修改用户信息"
        :visible.sync="dialogVisible"
        width="30%">
      <div>
        <table>
          <tr>
            <td>用户昵称:</td>
            <td>
              <el-input v-model="adminInfo2.name"/>
            </td>
          </tr>
          <tr>
            <td>电话号码:</td>
            <td>
              <el-input v-model="adminInfo2.telephone"/>
            </td>
          </tr>
          <tr>
            <td>手机号码:</td>
            <td>
              <el-input v-model="adminInfo2.phone"></el-input>
            </td>
          </tr>
          <tr>
            <td>地址:</td>
            <td>
              <el-input v-model="adminInfo2.address"/>
            </td>
          </tr>
        </table>
      </div>
      <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="updateAdmininfo">确 定</el-button>
  </span>
    </el-dialog>
    <el-dialog
        title="修改密码"
        :visible.sync="passworddialogVisible"
        width="30%">
      <div>
        <el-form :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="100px" class="demo-ruleForm">
          <el-form-item label="旧密码" prop="oldPass">
            <el-input type="password" v-model="ruleForm.oldPass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="新密码" prop="pass">
            <el-input type="password" v-model="ruleForm.pass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" prop="checkPass">
            <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="submitForm('ruleForm')">提交</el-button>
            <el-button @click="resetForm('ruleForm')">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

    </el-dialog>
  </div>
</template>

<script>
export default {
  name: 'AdminInfo',
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.ruleForm.checkPass !== '') {
          this.$refs.ruleForm.validateField('checkPass');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'));
      } else if (value !== this.ruleForm.pass) {
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      header:{
        Authorization:window.sessionStorage.getItem("tokenStr")
      },
      adminInfo: null,
      adminInfo2: null,
      dialogVisible: false,
      passworddialogVisible: false,
      ruleForm: {
        oldPass: '',
        pass: '',
        checkPass: '',
      },
      rules: {
        pass: [
          {validator: validatePass, trigger: 'blur'}
        ],
        checkPass: [
          {validator: validatePass2, trigger: 'blur'}
        ],
        oldPass: [
          {validator: validatePass, trigger: 'blur'}
        ]
      }
    };

  },
  mounted() {
    this.initadmin();
  },
  methods: {
    onSuccess(){
      this.initadmin();
    },
    showpasswordEdit() {
      this.passworddialogVisible = true;
    },
    updateAdmininfo() {
      this.putRequest('/admin/info', this.adminInfo2).then(resp => {
        if (resp) {
          this.dialogVisible = false;
          this.initadmin();
        }
      })
    },
    showinfoEdit() {
      this.dialogVisible = true
    },
    initadmin() {
      this.postRequest('/admin/info').then(resp => {
        if (resp) {
          this.adminInfo = resp;
          this.adminInfo2 = Object.assign({}, this.adminInfo);
          window.sessionStorage.setItem('user', JSON.stringify(resp))
          this.$store.commit('INIT_ADMIN', resp)
        }
      })
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.ruleForm.adminId = this.adminInfo.id;
          console.log(this.ruleForm)
          this.putRequest('/admin/pass', this.ruleForm).then(resp => {
            if (resp) {
              console.log(this.ruleForm)
              this.postRequest('/logout')
              window.sessionStorage.removeItem('user')
              window.sessionStorage.removeItem('tokenStr');
              window.sessionStorage.removeItem('tokenStr');
              this.$store.commit('initRoutes', []);
              this.$router.replace('/')
            }
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }

  }
}
</script>

<style scoped>

</style>