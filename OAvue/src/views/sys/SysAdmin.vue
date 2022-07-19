<template>
  <div>
    <div style="display: flex; justify-content: center; margin-top: 10px">
      <el-input
        placeholder="搜索操作员。。。。"
        v-model="keywords"
        prefix-icon="el-icon-search"
        style="width:400px;margin-right: 10px"
      ></el-input>
      <el-button type="primary" icon="el-icon-search" @click="doSearch">搜索</el-button>
    </div>
    <div class="admin-container">
      <el-card class="admin-card" v-for="(admin,index) in admins" :key="index">
        <div slot="header" class="clearfix">
          <span>{{admin.name}}</span>
          <el-button
            style="float: right; padding: 3px 0;color: red"
            type="text"
            icon="el-icon-delete"
            @click="deleteAdmin(admin)"
          ></el-button>
        </div>
        <div>
          <div class="img-container">
            <img :src="admin.userFace" :alt="admin.name" :title="admin.name" class="userface-img" />
          </div>
        </div>
        <div class="userinfo-container">
          <div>用户名：{{admin.name}}</div>
          <div>手机号码：{{admin.phone}}</div>
          <div>电话号码：{{admin.telephone}}</div>
          <div>地址：{{admin.address}}</div>
          <div>
            用户状态：
            <el-switch
              @change="enabledChange(admin)"
              
              v-model="admin.enabled"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text="启用"
              inactive-text="禁用"
            ></el-switch>
          </div>
          <div>
            用户角色：
            <el-tag
              style="margin-right: 4px"
              type="success"
              v-for="(role,index) in admin.roles"
              :key="index"
            >{{role.nameZh}}</el-tag>
            <el-popover placement="right" title="角色列表" width="200" trigger="click"  @show="showroles(admin)" @hide="upPop(admin)">
              <el-select v-model="selectRoles" placeholder="请选择" multiple >
                <el-option
                  v-for="(r,index) in allroles"
                  :key="index"
                  :label="r.nameZh"
                  :value="r.id"
                ></el-option>
              </el-select>
              <el-button type="text" slot="reference" icon="el-icon-more"></el-button>
            </el-popover>
          </div>
          <div>备注：{{admin.remark}}</div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
export default {
  name: "SysAdmin",
  data() {
    return {
      admins: [],
      keywords: "",
      allroles: [],
      selectRoles:[],
    };
  },
  mounted() {
     this.initAdmins();
  },
  methods: {
    
    upPop(admin){
      let flg = false
      let roles = [];
      Object.assign(roles,admin.roles)

      if (roles.length !== this.selectRoles.length) {
        flg =true;
      }
      else{
        for(let i=0;i<roles.length;i++){
          let role = roles[i]
          for(let j=0;j<this.selectRoles.length;j++){
              let sr  =this.selectRoles[j]
              if (role.id == sr) {
                roles.splice(i,1)
                i--;
                break;
              }
          }
        }
        if (roles.length !=0) {
          flg = true;
        }
      }


      if(flg){
         let url = "/system/admin/roles?adminId="+admin.id;
      
      this.selectRoles.forEach(sr=>{
        url+='&rids='+sr
      })
      this.putRequest(url).then(resp=>{
        if(resp){
          this.initAdmins()
        }
      })

      }
     
    },
    showroles(admin){
      this.initAllRoles();
      let roles = admin.roles;
      // console.log(roles)
      this.selectRoles = [];
      roles.forEach(r => {
        this.selectRoles.push(r.id)
      });
     
    },
    initAllRoles(){
      this.getRequest('/system/basic/permiss/').then(resp=>{
        this.allroles = resp;
      })
    },
    enabledChange(admin) {
      this.putRequest("/system/admin/", admin).then(resp => {
        if (resp) {
          this.initAdmins();
        }
      });
    },
    deleteAdmin(admin) {
      this.$confirm(
        "此操作将永久删除该[" + admin.name + "]操作员, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          this.deleteRequest("/system/admin/" + admin.id).then(resp => {
            if (resp) {
              this.initAdmins();
            }
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消删除"
          });
        });
    },
    doSearch() {
      this.initAdmins();
    },
    initAdmins() {
      this.getRequest("/system/admin/?keywords=" + this.keywords).then(resp => {
        if (resp) {
          this.admins = resp;
        }
      });
    }
  }
};
</script>

<style>
.admin-card {
  width: 350px;
  margin-bottom: 10px;
}
.admin-container {
  flex-wrap: wrap;
  display: flex;
  margin-top: 10px;
  justify-content: space-around;
}
.userface-img {
  width: 72px;
  height: 72px;
  border-radius: 72px;
}
.img-container {
  width: 100%;
  display: flex;
  justify-content: center;
}
.userinfo-container {
  font-size: 12px;
  color: #409eff;
}
</style>