<template>
  <div style="width: 500px">
    <el-input 
    placeholder="输入关键字进行过滤" v-model="filterText" 
    prefix-icon="el-icon-search"></el-input>

    <el-tree
      class="filter-tree"
      :data="deps"
      :props="defaultProps"
      :filter-node-method="filterNode"
      :expand-on-click-node="false"
      ref="tree"
    >
      <span
        class="custom-tree-node"
        slot-scope="{ node, data }"
        style="display: flex;
      justify-content: space-between; width: 100%"
      >
        <span>{{ data.name }}</span>
        <span>
          <el-button class="depBtn" type="primary" size="mini" @click="() => showAddDep(data)">添加部门</el-button>
          <el-button class="depBtn" type="danger" size="mini" @click="() => deleteDep(data)">删除部门</el-button>
        </span>
      </span>
    </el-tree>
    <el-dialog 
      title="添加部门"
     :visible.sync="dialogVisible" 
     width="30%" 
 >
      <div>
          <table>
            <tr>
              <td>
                  <el-tag>上级部门</el-tag>
                  
              </td>
              <td>{{paname}}</td>
            </tr>
            <tr>
              <td>
                  <el-tag>部门名称</el-tag>
              </td>
              <td>
                  <el-input v-model="dep.name" placeholder="请输入部门名称。。。。"></el-input>
              </td>
            </tr>
          </table>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAddDep">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "DepMana",
  data() {
    return {
      filterText: "",
      deps: [],
      defaultProps: {
        children: "children",
        label: "name"
      },
      dialogVisible:false,
      dep:{
        name:'',
        parentId: -1
      },
      paname:''
    };
  },
  mounted() {
    this.innitDeps();
  },
  methods: {
    
    initDep(){
      this.dep = {
        name:'',
        parseInt: -1
      }
      this.paname = '';
    },
    addDep2Dep2(deps,dep){
      //循环
      for(let i=0; i< deps.length;i++){
        let d=deps[i];
        if(d.id == dep.parentId){
          d.children = d.children.concat(dep);
          if(d.children.length>0){
            d.parentId=true;
          }
          return;
        }else{
          this.addDep2Dep2(d.children,dep);
        }
        
      }
    },
    doAddDep(){
      this.postRequest('/system/basic/department/',this.dep).then(resp=>{
        if(resp){
          this.addDep2Dep2(this.deps , resp.result);
          this.dialogVisible = false;
          this.initDep(); 
        }
      })
    },
    //添加部门
    showAddDep(data) {
      this.dep.parentId = data.id;
      this.paname = data.name;
      this.dialogVisible=true;
    },
    //删除部门
    deleteDep(data) {
      if(data.isParent){
        this.$message.error("父部门删除失败！")
      }else{
         this.$confirm('此操作将永久删除该[' + data.name + ']部门, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.deleteRequest('/system/basic/department/'+data.id).then(resp=>{
          if(resp){
            this.removeDepFromDeps(null,this.deps,data.id)
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
      }
      

    },
    //删除部门
    removeDepFromDeps(p,deps,id){
        for(let i =0; i<deps.length;i++){
          let d=deps[i];
          if(d.id == id){
            deps.splice(i,1);
            if(deps.length ==0){
              p.isParent = false;
            }
            return;
          }else{
            this.removeDepFromDeps(d,d.children,id)
          }
        }
    },
    // 初始化部门
    innitDeps() {
      this.getRequest("/system/basic/department/").then(resp => {
        if (resp) {
          this.deps = resp;
        }
      });
    },
    filterNode(value, data) {
      if (!value) return true;
      return data.name.indexOf(value) !== -1;
    }
  },
  watch: {
    filterText(val) {
      this.$refs.tree.filter(val);
    }
  }
};
</script>

<style>
.depBtn {
  padding: 2px;
}
</style>