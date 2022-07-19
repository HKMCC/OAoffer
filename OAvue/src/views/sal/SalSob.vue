<template>
  <div>
    <div style="display: flex;justify-content: space-between;margin-top: 10px">
      <el-button type="primary" icon="el-icon-plus" @click="addSalSob">添加工资账套</el-button>
      <el-button type="success" icon="el-icon-refresh" @click="iniSalData"></el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table
          :data="salaries"
          border
          stripe
      >
        <el-table-column
            type="selection"
            width="40px"
        >
        </el-table-column>
        <el-table-column
            prop="basicSalary"
            label="基本工资"
            width="120"
        >
        </el-table-column>
        <el-table-column
            prop="name"
            label="工资账套名称"
            width="120"
        >

        </el-table-column>
        <el-table-column
            prop="trafficSalary"
            label="交通补助"
            width="120"
        >

        </el-table-column>
        <el-table-column
            prop="lunchSalary"
            label="午餐补助"
            width="120"
        >
        </el-table-column>
        <el-table-column
            prop="bonus"
            label="奖金"
            width="70"
        >
        </el-table-column>
        <el-table-column
            prop="createDate"
            label="启用时间"
            width="120"
        >
        </el-table-column>
        <el-table-column
            label="养老金"
            align="center"
        >
          <el-table-column
              prop="pensionPer"
              label="比率"
              width="70"
          >
          </el-table-column>
          <el-table-column
              prop="pensionBase"
              label="基数"
              width="70"
          >
          </el-table-column>
        </el-table-column>
        <el-table-column
            label="医疗保险"
            align="center"
        >

          <el-table-column
              prop="medicalPer"
              label="比率"
              width="70"
          >
          </el-table-column>
          <el-table-column
              prop="medicalBase"
              label="基数"
              width="70"
          >
          </el-table-column>
        </el-table-column>
        <el-table-column
            label="公积金"
            align="center"
        >
          <el-table-column
              prop="accumulationFundPer"
              label="比率"
              width="70"
          >
          </el-table-column>
          <el-table-column
              prop="accumulationFundBase"
              label="基数"
              width="70"
          >
          </el-table-column>
        </el-table-column>
        <el-table-column
            label="操作"
            width="250"
        >
          <template slot-scope="scope">
            <el-button type="primary" @click="showEdit(scope.row)">编辑</el-button>
            <el-button type="danger" @click="deleteSalary(scope.row)">删除</el-button>
          </template>

        </el-table-column>
      </el-table>
    </div>
    <el-dialog
        :title="dialogTotle"
        :visible.sync="dialogVisible"
        width="50%"
    >
      <div style="display: flex;justify-content: space-around;align-items: center;">
        <el-steps direction="vertical" :active="onactive" >
          <el-step :title="itemName" v-for="(itemName,index) in salarItemName" :key="index">
          </el-step>
        </el-steps>
          <el-input v-for="(value,title,index) in salary"
                    :key="index"
                    v-model="salary[title]"
                    v-show="onactive == index"
                    :placeholder="'请输入'+salarItemName[index]+'....'"
                    style="width: 200px"
          >

          </el-input>

      </div>
      <span slot="footer" class="dialog-footer">

    <el-button @click="preStep">{{onactive==10?'取消':'上一步'}}</el-button>
    <el-button type="primary" @click="nextSetp">{{onactive==10?'完成':'下一步'}}</el-button>
  </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "SalSob",
  data() {
    return {
      dialogTotle:'',
      onactive:0,
      dialogVisible: false,
      salaries: [],
      salarItemName: [
        '工资账套名称',
        '基本工资',
        '交通补助',
        '午餐补助',
        '奖金',
        '养老金比率',
        '养老金基数',
        '医疗保险比率',
        '医疗保险基数',
        '公积金比率',
        '公积金基数',

      ],
      salary:{
        name:'',
        basicSalary:0,
        trafficSalary:0,
        lunchSalary:0,
        bonus:0,
        pensionPer:0,
        pensionBase:0,
        medicalPer:0,
        medicalBase:0,
        accumulationFundPer:0,
        accumulationFundBase:0
      }
    }
  },
  mounted() {
    this.iniSalData();
  },
  methods: {
    //编辑
    showEdit(data){
      this.dialogTotle = '编辑工资账套'
      this.onactive = 0;
      this.salary.id = data.id;
      this.salary.name = data.name;
      this.salary.basicSalary = data.basicSalary;
      this.salary.trafficSalary = data.trafficSalary;
      this.salary.lunchSalary = data.lunchSalary;
      this.salary.bonus = data.bonus;
      this.salary.pensionPer = data.pensionPer;
      this.salary.pensionBase = data.pensionBase;
      this.salary.medicalPer = data.medicalPer;
      this.salary.medicalBase = data.medicalBase;
      this.salary.accumulationFundPer = data.accumulationFundPer;
      this.salary.accumulationFundBase = data.accumulationFundBase;
      this.dialogVisible = true;
    },
    //删除工资账套
    deleteSalary(data){
      this.$confirm(
          "此操作将永久删除该[" + data.name + "]工资账套, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }
      )
          .then(() => {
            this.deleteRequest("/salary/sob/" + data.id).then(resp => {
              if (resp) {
                this.iniSalData();
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
    preStep(){
      if (this.onactive == 0){
        return;
      } else if(this.onactive == 10){
        this.dialogVisible = false;
        return;
      }
      this.onactive--;
    },
    nextSetp(){
      if (this.onactive ==10){
        if (this.salary.id){
            this.putRequest('/salary/sob/',this.salary).then(resp=>{
              if (resp){
                this.iniSalData();
                this.dialogVisible = false;
              }
            })
        }else {
          this.postRequest('/salary/sob/',this.salary).then(resp=>{
            if(resp){
              this.iniSalData()
              this.dialogVisible = false;

            }
          })
        }

        return;
      }
      this.onactive++;
    },
    addSalSob() {
      this.dialogTotle  = '添加工资账套'
      this.salary={
        name:'',
        basicSalary:0,
        trafficSalary:0,
        lunchSalary:0,
        bonus:0,
        pensionPer:0,
        pensionBase:0,
        medicalPer:0,
        medicalBase:0,
        accumulationFundPer:0,
        accumulationFundBase:0
      }
      this.onactive=0;
      this.dialogVisible = true;
    },
    iniSalData() {
      this.getRequest('/salary/sob/').then(resp => {
        if (resp) {
          this.salaries = resp
        }

      })
    },

  }
}
</script>

<style>

</style>