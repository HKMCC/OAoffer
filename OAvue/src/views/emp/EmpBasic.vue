<template>
  <div>
    <div>
      <div style="display: flex;justify-content: space-between">
        <div>
          <el-input style="width: 300px;margin-right: 10px"
                    prefix-icon="el-icon-search"
                    v-model="empName"
                    @keydown.enter.native="innitemps"
                    clearable
                    :disabled="showAdvanceSearchVisible"
                    @clear="innitemps"
                    placeholder="请输入员工名进行搜索。。。。。"
          ></el-input>
          <el-button type="primary" icon="el-icon-search" @click="innitemps" :disabled="showAdvanceSearchVisible">搜索</el-button>
          <el-button type="primary" @click="showAdvanceSearchVisible=!showAdvanceSearchVisible">
            <i :class="showAdvanceSearchVisible?'fa fa-angle-double-up':'fa fa-angle-double-down'" aria-hidden="true"></i>
            高级搜索
          </el-button>
        </div>
<!--导入导出数据-->
        <div >
          <el-upload action="/employee/basid/import"
                     :headers="headers"
                     style="display: inline-flex;margin-right: 8px"
                     :before-upload="beforeUpload"
                     :on-success="onSuccess"
                     :on-error:="onError"
                     :disabled="disabled"
                     :show-file-list=false>
            <el-button type="success" :icon="expoicon">
              {{ expotext }}
            </el-button>
          </el-upload>

          <el-button type="success" @click="expodata" icon="el-icon-download">
            导出数据
          </el-button>
          <el-button type="primary" icon="el-icon-plus" @click="addEmp">添加员工</el-button>
        </div>
      </div>
<!--   高级搜索弹出信息   -->
      <transition name="slide-fade">
      <div v-show="showAdvanceSearchVisible" style="border: 1px solid #379ff5;
             border-radius: 5px;box-sizing: border-box;
             padding: 5px;margin: 10px 0;">
<!--        第一行-->
        <el-row>
          <el-col :span="5">
            政治面貌：
            <el-select v-model="searchValue.politicId" placeholder="请选择政治面貌" size="mini" style="width: 130px;">
              <el-option
                  v-for="item in politicsstatus"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            民族：
            <el-select v-model="searchValue.nationId" placeholder="民族" size="mini" style="width: 130px;">
              <el-option
                  v-for="item in nations"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            职位：
            <el-select v-model="searchValue.posId" placeholder="职位" size="mini" style="width: 130px;">
              <el-option
                  v-for="item in positions"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="4">
            职称：
            <el-select v-model="searchValue.jobLevelId" placeholder="职称" size="mini" style="width: 130px;">
              <el-option
                  v-for="item in joblevels"
                  :key="item.id"
                  :label="item.name"
                  :value="item.id">
              </el-option>
            </el-select>
          </el-col>
          <el-col :span="6">
            聘用形式：
            <el-radio-group v-model="searchValue.engageForm">
              <el-radio label="劳动合同">劳动合同</el-radio>
              <el-radio label="劳务合同">劳务合同</el-radio>
            </el-radio-group>
          </el-col>
        </el-row>
        <el-row style="margin-top: 10px;">
          <!-- 30-4 处理部门 v-model="visible2" -->
          <el-col :span="5">
            所属部门：
            <el-popover
                placement="bottom"
                title="请选择部门"
                width="220"
                trigger="manual"
                v-model="visible2">
              <!-- 23-20 添加树形控件 default-expand-all	是否默认展开所有节点 ，节点点击事件 @node-click="handleNodeClick" -->
              <el-tree :data="allDeps"
                       :props="defaultProps"
                       default-expand-all
                       @node-click="searchHandleNodeClick"></el-tree>
              <!-- 30-6 @node-click="searchHandleNodeClick" -->
              <!-- node-click	节点被点击时的回调 共三个参数，依次为：传递给 data 属性的数组中该节点所对应的对象、节点对应的 Node、节点组件本身。 -->
              <!-- 自定义点击事件 -->
              <!-- 30-7 @click="showDepView2" -->
              <div slot="reference"
                   style="width:130px;display: inline-flex;
                 border-radius: 5px;border: 1px solid #dedede;height: 28px;cursor: pointer;align-items: center;
                 font-size: 12px;padding-left: 8px;box-sizing: border-box;"
                   @click="showdep2">{{ inputDepName }}
              </div><!-- 23-25 回显数据 {{inputDepName}} -->
            </el-popover>
          </el-col>
          <!-- 30-3 处理日期：v-model="searchValue.beginDateScope" value-format="yyyy-MM-dd" ;
               两个面板各自独立切换当前年份 使用unlink-panels -->
          <el-col :span="10" >
            入职日期：
            <el-date-picker
                unlink-panels
                size="mini"
                v-model="searchValue.beginDateScope"
                type="daterange"
                value-format="yyyy-MM-dd"
                range-separator="至"
                start-placeholder="开始日期"
                end-placeholder="结束日期">
            </el-date-picker>
          </el-col>
          <el-col :span="5" :offset="4">
            <el-button  @click="quxiao" size="mini">取消</el-button>
            <!-- 30-10 @click="initEmps('advanced')" -->
            <el-button @click="innitemps('advanced')" type="primary" icon="el-icon-search" size="mini" >搜索</el-button>
          </el-col>
        </el-row>
      </div>
      </transition>
    </div>

<!--    中间员工资料表格-->
    <div style="margin-top: 10px;">
      <el-table
          :data="emps"
          v-loading="loading"
          element-loading-text="拼命加载中"
          element-loading-spinner="el-icon-loading"
          element-loading-background="rgba(0, 0, 0, 0.8)"
          style="width: 100%" stripe border>
        <el-table-column
            type="selection"
            width="55">
        </el-table-column>
        <el-table-column
            prop="name"
            label="姓名"
            align="left"
            fixed
            width="90">
        </el-table-column>
        <el-table-column
            prop="gender"
            label="性别"
            align="left"
            width="60">
        </el-table-column>
        <el-table-column
            prop="workID"
            label="工号"
            align="left"
            width="85">
        </el-table-column>
        <el-table-column
            prop="birthday"
            label="出生日期"
            align="left"
            width="95">
        </el-table-column>
        <el-table-column
            prop="idCard"
            label="身份证号"
            width="150">
        </el-table-column>
        <el-table-column
            prop="wedlock"
            label="婚姻状态"
            align="center"
            width="90">
        </el-table-column>
        <el-table-column
            prop="nation.name"
            label="民族"
            align="left"
            width="70">
        </el-table-column>
        <el-table-column
            prop="nativePlace"
            label="籍贯"
            align="center"
            width="80">
        </el-table-column>
        <el-table-column
            prop="politicsStatus.name"
            label="政治面貌"
            width="120">
        </el-table-column>
        <el-table-column
            prop="email"
            label="电子邮件"
            align="left"
            width="170">
        </el-table-column>
        <el-table-column
            prop="phone"
            label="电话号码"
            align="left"
            width="100">
        </el-table-column>
        <el-table-column
            prop="address"
            label="联系地址"
            align="center"
            width="300">
        </el-table-column>
        <el-table-column
            prop="department.name"
            label="所属部门"
            align="left"
            width="100">
        </el-table-column>
        <el-table-column
            prop="position.name"
            label="职位"
            width="100">
        </el-table-column>
        <el-table-column
            prop="joblevel.name"
            label="级别"
            width="120">
        </el-table-column>
        <el-table-column
            prop="engageForm"
            label="聘用形式"
            align="left"
            width="100">
        </el-table-column>
        <el-table-column
            prop="tiptopDegree"
            label="最高学历"
            align="center"
            width="100">
        </el-table-column>
        <el-table-column
            prop="school"
            label="毕业学校"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="specialty"
            label="所属专业"
            align="left"
            width="150">
        </el-table-column>
        <el-table-column
            prop="workState"
            label="在职状态"
            align="center"
            width="90">
        </el-table-column>
        <el-table-column
            prop="beginDate"
            label="入职日期"
            align="left"
            width="95">
        </el-table-column>
        <el-table-column
            prop="conversionTime"
            label="转正日期"
            align="left"
            width="95">
        </el-table-column>
        <el-table-column
            prop="beginContract"
            label="合同起始日期"
            align="left"
            width="120">
        </el-table-column>
        <el-table-column
            prop="endContract"
            label="合同截止日期"
            align="left"
            width="120">
        </el-table-column>
        <el-table-column
            label="合同期限"
            align="left"
            width="120">
          <template slot-scope="scope">
            <el-tag>{{ scope.row.contractTerm }}</el-tag>
            年
          </template>
        </el-table-column>
        <el-table-column
            label="操作"
            fixed="right"
            width="300">
          <template slot-scope="scope">
            <!-- 25-4 给编辑按钮绑定点击事件 @click="showEmpView(scope.row)" -->
            <el-button style="padding: 3px;" size="mini" @click="showEmpView(scope.row)">编辑</el-button>
            <el-button style="padding: 3px;" size="mini" type="primary" plain>查看高级资料</el-button>
            <!-- 24-1 删除员工 @click="deleteEmp(scope.row)" -->
            <el-button style="padding: 3px;" size="mini" type="danger" @click="deleteEmp(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          @current-change="currentchange"
          @size-change="sizechange"
          :total="total"
          style="display: flex;justify-content: flex-end">
      </el-pagination>
    </div>
<!--    添加员工的弹出-->
    <div>
      <el-dialog
          title="添加员工"
          :visible.sync="dialogVisible"
          width="80%">
        <div>
          <el-form ref="empForm" :model="emp" :rules="rules">
            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item label="姓名：" prop="name">
                  <el-input size="mini" style="width: 150px" v-model="emp.name" placeholder="请输入员工姓名"
                            prefix-icon="el-icon-edit"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="性别：" prop="gender">
                  <el-radio-group v-model="emp.gender" style="margin-top: 8px;">
                    <el-radio label="男">男</el-radio>
                    <el-radio label="女">女</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="出生日期：" prop="birthday">
                  <el-date-picker
                      v-model="emp.birthday"
                      type="date"
                      value-format="yyyy-MM-dd"
                      size="mini"
                      style="width: 150px;"
                      placeholder="出生日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="政治面貌" prop="politicId">
                  <el-select v-model="emp.politicId" placeholder="请选择政治面貌" size="mini" style="width: 200px">
                    <el-option
                        v-for="item in politicsstatus"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item label="民族：" prop="nationId">
                  <el-select v-model="emp.nationId" placeholder="民族" size="mini" style="width: 150px;">
                    <el-option
                        v-for="item in nations"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="籍贯：：" prop="nativePlace">
                  <el-input v-model="emp.nativePlace" placeholder="籍贯" prefix-icon="el-icon-edit" size="small"
                            style="width: 120px;"></el-input>

                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="电子邮箱：" prop="email">
                  <el-input v-model="emp.email" placeholder="请输入电子邮箱" prefix-icon="el-icon-message" size="mini"
                            style="width: 150px;"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="联系地址：" prop="address">
                  <el-input v-model="emp.address" placeholder="请输入联系地址" prefix-icon="el-icon-edit" size="mini"
                            style="width: 200px;"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="6">
                <el-form-item label="职位：" prop="posId">
                  <el-select v-model="emp.posId" placeholder="职位" size="mini" style="width: 150px;">
                    <el-option
                        v-for="item in positions"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="职称：" prop="jobLevelId">
                  <el-select v-model="emp.jobLevelId" placeholder="职称" size="mini" style="width: 150px;">
                    <el-option
                        v-for="item in joblevels"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="所属部门：" prop="departmentId">
                  <el-popover
                      placement="bottom"
                      title="请选择部门"
                      width="200"
                      trigger="manual"
                      content="这是一段内容,这是一段内容,这是一段内容,这是一段内容。"
                      v-model="visible">
                    <el-tree :data="allDeps"
                             :props="defaultProps"
                             default-expand-all
                             @node-click="handleNodeClick">

                    </el-tree>
                    <div slot="reference" @click="showdep" style="width: 150px;display: inline-flex;
                  border: 1px solid #dedede;height: 24px;border-radius: 5px;
                  cursor: pointer;margin-top: 5px ;align-items: center;font-size: 13px;
                   padding-left: 8px;box-sizing: border-box;">
                      {{ inputDepName }}
                    </div>
                    <!--                    <el-button  @click="visible = !visible">手动激活</el-button>-->
                  </el-popover>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="电话号码：" prop="phone">
                  <el-input v-model="emp.phone" placeholder="请输入电话号码" size="mini" style="width: 200px;"
                            prefix-icon="el-icon-phone"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="20">
              <!--              工号-->
              <el-col :span="6">
                <el-form-item label="工号：" prop="workId">
                  <el-input v-model="emp.workID" placeholder="请输入工号" size="mini" style="width: 150px;"
                            prefix-icon="el-icon-edit" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="学历：" prop="tiptopDegree">
                  <el-select v-model="emp.tiptopDegree" placeholder="学历" size="mini" style="width: 150px;">
                    <el-option
                        v-for="item in tiptopDegrees"
                        :key="item"
                        :label="item"
                        :value="item">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="毕业院校：" prop="school">
                  <el-input v-model="emp.school" placeholder="请输入学校" size="mini" style="width: 150px;"
                            prefix-icon="el-icon-edit"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="专业名称：" prop="specialty">
                  <el-input v-model="emp.specialty" placeholder="请输入专业名称" size="mini" style="width: 200px;"
                            prefix-icon="el-icon-edit"></el-input>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <!--              工号-->
              <el-col :span="6">
                <el-form-item label="入职日期：" prop="beginDate">
                  <el-date-picker
                      v-model="emp.beginDate"
                      type="date"
                      value-format="yyyy-MM-dd"
                      size="mini"
                      style="width: 120px;"
                      placeholder="入职日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="5">
                <el-form-item label="转正日期：" prop="conversionTime">
                  <el-date-picker
                      v-model="emp.conversionTime"
                      type="date"
                      value-format="yyyy-MM-dd"
                      size="mini"
                      style="width: 120px;"
                      placeholder="转正日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="合同起始日期：" prop="beginContract">
                  <el-date-picker
                      v-model="emp.beginContract"
                      type="date"
                      value-format="yyyy-MM-dd"
                      size="mini"
                      style="width: 135px;"
                      placeholder="合同起始日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="7">
                <el-form-item label="合同截止日期：" prop="endContract">
                  <el-date-picker
                      v-model="emp.endContract"
                      type="date"
                      value-format="yyyy-MM-dd"
                      size="mini"
                      style="width: 170px;"
                      placeholder="合同截止日期">
                  </el-date-picker>
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <!--              身份证-->
              <el-col :span="8">
                <el-form-item label="身份证号码：" prop="idCard">
                  <el-input v-model="emp.idCard" placeholder="请输入身份证号码"
                            size="mini" prefix-icon="el-icon-edit" style="width: 180px;"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="聘用形式：" prop="engageForm">
                  <el-radio-group v-model="emp.engageForm" style="margin-top: 8px;">
                    <el-radio label="劳动合同">劳动合同</el-radio>
                    <el-radio label="劳务合同">劳务合同</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="婚姻状况：" prop="wedlock">
                  <el-radio-group v-model="emp.wedlock" style="margin-top: 8px;">
                    <el-radio label="未婚">未婚</el-radio>
                    <el-radio label="已婚">已婚</el-radio>
                    <el-radio label="离异">离异</el-radio>
                  </el-radio-group>
                </el-form-item>
              </el-col>
            </el-row>
          </el-form>
        </div>
        <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisible = false">取 消</el-button>
    <el-button type="primary" @click="doAddEmp">确 定</el-button>
  </span>
      </el-dialog>
    </div>
  </div>
</template>

<script>
export default {
  name: "EmpBasic",
  data() {
    return {
      showAdvanceSearchVisible:false,
      headers:{
        Authorization: window.sessionStorage.getItem("tokenStr")
      },
      disabled:false,
      expoicon: 'el-icon-upload2',
      expotext: '导入数据',
      inputDepName: '',
      emps: [],
      loading: false,
      total: 0,
      currentPage: 1,
      size: 10,
      empName: '',
      dialogVisible: false,
      politicsstatus: [],
      nations: [],
      joblevels: [],
      positions: [],
      visible2:false,
      visible: false,
      tiptopDegrees: ['博士', '硕士', '本科', '大专', '高中', '初中', '小学', '其它'],
      //搜索员工
      searchValue:{
        politicId:null,
        nationId:null,
        posId:null,
        jobLevelId:null,
        engageForm:'',
        departmentId:null,
        beginDateScope:null
      },
      //添加员工
      emp: {
        id: null,
        name: '',
        gender: '',
        birthday: '',
        idCard: '',
        wedlock: '',
        nationId: null,
        nativePlace: '',
        politicId: null,
        email: '',
        phone: '',
        address: '',
        departmentId: null,
        jobLevelId: null,
        posId: null,
        engageForm: '',
        tiptopDegree: '',
        specialty: '',
        school: '',
        beginDate: '',
        workState: '在职',
        workID: '',
        contractTerm: null,
        conversionTime: '',
        notworkDate: null,
        beginContract: '',
        endContract: '',
        workAge: null,
        salaryId: null
      },
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      allDeps: [],
      //校验表单
      rules: {
        name: [{required: true, message: '请输入员工名', trigger: 'blur'}],
        gender: [{required: true, message: '请输入员工性别', trigger: 'blur'}],
        birthday: [{required: true, message: '请输入出生日期', trigger: 'blur'}],
        idCard: [{required: true, message: '请输入身份证号码', trigger: 'blur'},
          {
            pattern: /(^[1-9]\d{5}(18|19|([23]\d))\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$)|(^[1-9]\d{5}\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\d{2}$)/,
            message: '身份证号码不正确', trigger: 'blur'
          }],
        wedlock: [{required: true, message: '请输入婚姻状况', trigger: 'blur'}],
        nationId: [{required: true, message: '请输入民族', trigger: 'blur'}],
        nativePlace: [{required: true, message: '请输入籍贯', trigger: 'blur'}],
        politicId: [{required: true, message: '请输入政治面貌', trigger: 'blur'}],
        email: [{required: true, message: '请输入邮箱地址', trigger: 'blur'},
          {type: 'email', message: '邮箱地址格式不正确', trigger: 'blur'}],
        phone: [{required: true, message: '请输入电话号码', trigger: 'blur'},
          {
            pattern: /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/,
            message: '请输入合法手机号码', trigger: 'blur'
          }],
        address: [{required: true, message: '请输入地址', trigger: 'blur'}],
        departmentId: [{required: true, message: '请输入部门名称', trigger: 'blur'}],
        jobLevelId: [{required: true, message: '请输入职称', trigger: 'blur'}],
        posId: [{required: true, message: '请输入职位', trigger: 'blur'}],
        engageForm: [{required: true, message: '请输入聘用形式', trigger: 'blur'}],
        tiptopDegree: [{required: true, message: '请输入学历', trigger: 'blur'}],
        specialty: [{required: true, message: '请输入专业', trigger: 'blur'}],
        school: [{required: true, message: '请输入毕业院校', trigger: 'blur'}],
        beginDate: [{required: true, message: '请输入入职日期', trigger: 'blur'}],
        workState: [{required: true, message: '请输入工作状态', trigger: 'blur'}],
        workID: [{required: true, message: '请输入工号', trigger: 'blur'}],
        contractTerm: [{required: true, message: '请输入合同期限', trigger: 'blur'}],
        conversionTime: [{required: true, message: '请输入转正日期', trigger: 'blur'}],
        notworkDate: [{required: true, message: '请输入离职日期', trigger: 'blur'}],
        beginContract: [{required: true, message: '请输入合同起始日期', trigger: 'blur'}],
        endContract: [{required: true, message: '请输入合同结束日期', trigger: 'blur'}],
        workAge: [{required: true, message: '请输入工龄', trigger: 'blur'}]
      }
    }
  },
  mounted() {
    this.initPositions();
    this.innitemps();
    this.initData();

  },
  methods: {
    quxiao(){
      this.searchValue={
        politicId:null,
        nationId:null,
        posId:null,
        jobLevelId:null,
        engageForm:'',
        departmentId:null,
        beginDateScope:null
      }
    },
    //上传成功的钩子
    onSuccess() {
      this.expoicon = "el-icon-upload2"
      this.expotext = "导入数据"
      this.disabled = false
    },
    //失败钩子
    onError() {
      this.expoicon = "el-icon-upload2"
      this.expotext = "导入数据"
      this.disabled = false
    },
    beforeUpload() {
      this.expoicon = "el-icon-loading"
      this.expotext = "正在导入"
      this.disabled = true
    },
    //导出员工
    expodata() {
      this.downloadRequest('/employee/basid/export')
    },
    //添加员工确定
    doAddEmp() {
      this.$refs['empForm'].validate(valid => {
        if (valid) {
          this.postRequest('/employee/basid/', this.emp).then(resp => {
            if (resp) {
              this.dialogVisible = false;
              this.innitemps();
            }
          })
        }
      });
    },
    //节点点击事件
    handleNodeClick(data) {
      this.inputDepName = data.name;
      this.emp.departmentId = data.id;
      this.visible = !this.visible;
    },
    searchHandleNodeClick(data){
      this.inputDepName = data.name;
      this.searchValue.departmentId = data.id;
      this.visible2 = !this.visible2;
    },
    showdep2() {
      this.visible2 = !this.visible2;
    },
    showdep() {
      this.visible = !this.visible;
    },
    getWorkerId() {
      this.getRequest('/employee/basid/maxWorkID').then(resp => {
        if (resp) {
          this.emp.workID = resp.result
        }
      })
    },
    initPositions() {
      this.getRequest('/employee/basid/position').then(resp => {
        if (resp) {
          this.positions = resp;
        }
      })
    },
    //初始化不经常变动的数据sessionStorage
    initData() {
      if (!window.sessionStorage.getItem("nations")) {
        this.getRequest('/employee/basid/nations').then(resp => {
          if (resp) {
            this.nations = resp;
            window.sessionStorage.setItem("nations", JSON.stringify(resp))
          }
        })
      } else {
        this.nations = JSON.parse(sessionStorage.getItem("nations"));
      }
      //职称
      if (!window.sessionStorage.getItem("joblevels")) {
        this.getRequest('/employee/basid/joblevels').then(resp => {
          if (resp) {
            this.joblevels = resp;
            window.sessionStorage.setItem("joblevels", JSON.stringify(resp))
          }
        })
      } else {
        this.joblevels = JSON.parse(sessionStorage.getItem("joblevels"));
      }
      if (!window.sessionStorage.getItem("politicsstatus")) {
        this.getRequest('/employee/basid/politicsstatus').then(resp => {
          if (resp) {
            this.politicsstatus = resp;
            window.sessionStorage.setItem("politicsstatus", JSON.stringify(resp))
          }
        })
      } else {
        this.politicsstatus = JSON.parse(sessionStorage.getItem("politicsstatus"));
      }
      if (!window.sessionStorage.getItem("deps")) {
        this.getRequest('/employee/basid/department').then(resp => {
          if (resp) {
            this.allDeps = resp;
            window.sessionStorage.setItem("allDeps", JSON.stringify(resp))
          }
        })
      } else {
        this.allDeps = JSON.parse(sessionStorage.getItem("allDeps"));
      }
    },
    //添加员工按钮
    addEmp() {
      this.getWorkerId();

      this.dialogVisible = true;
    },
    sizechange(size) {
      this.size = size
      this.innitemps();
    },
    currentchange(currentPage) {
      this.currentPage = currentPage;
      this.innitemps();
    },
    innitemps(type) {
      this.loading = true
      // 30-11 定义高级搜索 url
      let url = '/employee/basid/?currentPage=' + this.currentPage + '&size=' + this.size
      // eslint-disable-next-line no-undef
      if (type && type === 'advanced') { // 说明是高级搜索
        if (this.searchValue.politicId) {
          url += '&politicId=' + this.searchValue.politicId
        }
        if (this.searchValue.nationId) {
          url += '&nationId=' + this.searchValue.nationId
        }
        if (this.searchValue.posId) {
          url += '&posId=' + this.searchValue.posId
        }
        if (this.searchValue.jobLevelId) {
          url += '&jobLevelId=' + this.searchValue.jobLevelId
        }
        if (this.searchValue.engageForm) {
          url += '&engageForm=' + this.searchValue.engageForm
        }
        if (this.searchValue.departmentId) {
          url += '&departmentId=' + this.searchValue.departmentId
        }
        if (this.searchValue.beginDateScope) {
          url += '&beginDateScope=' + this.searchValue.beginDateScope
        }
      } else {
        // eslint-disable-next-line no-unused-vars
        url += '&name=' + this.empName
      }
      // let url = '/employee/basid/?currentPage=' + this.currentPage + '&size=' + this.size + '&name=' + this.empName
      console.log(url)
      this.getRequest(url).then(resp => {
        if (resp) {
          this.loading = false
          this.emps = resp.data;
          this.total = resp.total
        }

      })
    }
  }
}
</script>

<style>
.slide-fade-enter-active {
  transition: all .8s ease;
}

.slide-fade-leave-active {
  transition: all .8s cubic-bezier(1.0, 0.5, 0.8, 1.0);
}

.slide-fade-enter, .slide-fade-leave-to
  /* .slide-fade-leave-active for below version 2.1.8 */
{
  transform: translateX(10px);
  opacity: 0;
}
</style>  