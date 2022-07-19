<template>
  <div>
    <div>
      <el-input
        size="small"
        class="addPosInput"
        placeholder="输入职位"
        suffix-icon="el-icon-plus"
        @keydown.enter.native="addPositons"
        v-model="pos.name"
      ></el-input>
      <el-button size="small" icon="el-icon-plus" type="primary" @click="addPositons">添加</el-button>
    </div>
    <div class="postManaMain">
      <el-table 
      @selection-change="handleSelectionChange"
      :data="position" 
      stripe style="width: 70%" 
      size="small" 
      border>
        /**多选框 */
        <el-table-column type="selection" width="55"></el-table-column>

        <el-table-column prop="id" label="编号" width="70"></el-table-column>
        <el-table-column prop="name" label="职位" width="180"></el-table-column>
        <el-table-column prop="createDate" label="创建时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" @click="showEdit(scope.$index, scope.row)">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
        <!-- <el-table-column prop="enabled" label="是否启用"></el-table-column> -->
      </el-table>
    </div>
    <el-button size="small" style="margin-top: 8px" type="danger" :disabled="this.multipleSelection.length===0" @click="deleteMany">批量删除</el-button>
    <el-dialog 
        title="编辑职位" 
        :visible.sync="dialogVisible" 
        width="30%">
      <div>
        <el-tag>职位名称</el-tag>
        <el-input v-model="updatePos.name" size="small" class="updatePosInput"></el-input>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="doupdate">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "PosMana",
  data() {
    return {
      pos: {
        name: ""
      },
      position: [],
      dialogVisible: false,
      updatePos: {
        name: ""
      },
       multipleSelection: [] // 批量删除勾选的对象
    };
  },
  //钩子
  mounted() {
    this.initPositions();
  },
  methods: {
    //批量删除
    deleteMany(){
        this.$confirm('此操作将永久删除[' + this.multipleSelection.length + ']条职位, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let ids = '?'
        this.multipleSelection.forEach(item => {
          ids += 'ids=' + item.id + '&'
        },
        )
        this.deleteRequest('/system/basic/pos/' + ids).then(resp => {
          if (resp) {
            
            this.initPositions()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    doupdate() {
      this.putRequest("/system/basic/pos/", this.updatePos).then(resp => {
        if (resp) {
          this.initPositions();
          this.dialogVisible = false;
        }
      });
    },
    addPositons() {
      if (this.pos.name) {
        this.postRequest("/system/basic/pos/", this.pos).then(resp => {
          if (resp) {
            this.initPositions();
            this.pos.name = "";
          }
        });
      } else {
        this.$message.error("职位名称不能为空");
      }
    },
    showEdit(index, data) {
      // let a = data;
      // this.updatePos = a;
      //数据拷t贝
      Object.assign(this.updatePos, data);
      this.updatePos.createDate = "";
      this.dialogVisible = true;
    },
    handleDelete(index, data) {
      this.$confirm(
        "此操作将永久删除[" + data.name + "]职位, 是否继续?",
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }
      )
        .then(() => {
          this.deleteRequest(" /system/basic/pos/" + data.id).then(resp => {
            if (resp) {
              //初始化
              this.initPositions();
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
    //初始化职位列表
    initPositions() {
      this.getRequest("/system/basic/pos/").then(resp => {
        if (resp) {
          this.position = resp;
        }
      });
    },
     // 批量删除(取值）
    handleSelectionChange(val) {
      this.multipleSelection = val
      // console.log(val)
    },
  }
};
</script>

<style >
.addPosInput {
  width: 300px;
  margin-right: 8px;
}
.postManaMain {
  margin-top: 10px;
}
.updatePosInput {
  width: 200px;
  margin-left: 8px;
}
</style>