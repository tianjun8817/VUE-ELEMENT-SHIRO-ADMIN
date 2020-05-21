<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.username"
        placeholder="填写用户名"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-button class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">搜索</el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >添加</el-button>
    </div>
    <!-- 表格信息 -->
    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      fit
      highlight-current-row
      style="width: 100%;"
      align="center"
    >
      <el-table-column label="用户编号" prop="id" align="center" min-width="18%">
        <template slot-scope="{ row }">
          <el-button type="text" icon="el-icon-view" @click="viewUser(row.id)">{{ row.id }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="用户名" align="center" min-width="20%">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.username }}</span>
        </template>
      </el-table-column>
      <el-table-column label="邮箱" align="center" min-width="15%">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.email }}</span>
        </template>
      </el-table-column>
      <el-table-column label="手机号码" align="center" min-width="15%">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.mobile }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" min-width="10%">
        <template slot-scope="{ row }">
          <el-tag :type="row.state | genericStateCSS">
            {{
              row.state | genericStateStr
            }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="时间" align="center" min-width="20%">
        <template slot-scope="{ row }">
          <div>
            创建于
            <span style="font-size:5px">
              {{
                row.ctime | parseTime("{y}-{m}-{d} {h}:{i}")
              }}
            </span>
          </div>
          <div v-if="row.utime">
            修改于
            <span style="font-size:5px">
              {{
                row.utime | parseTime("{y}-{m}-{d} {h}:{i}")
              }}
            </span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        min-width="30%"
        label="操作"
        align="left"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{ row }">
          <el-row :gutter="5">
            <el-col :span="7" :xs="24">
              <el-button
                v-if="row.state != 0"
                type="primary"
                size="mini"
                @click="handleSetRole(row)"
              >角色变更</el-button>
            </el-col>
            <el-col :span="5" :xs="24">
              <el-button
                v-if="row.state != 0"
                type="warning"
                size="mini"
                @click="handleUpdate(row)"
              >编辑</el-button>
            </el-col>
            <el-col :span="5" :xs="24">
              <el-button
                v-if="row.roleCode !== 'SUPER_ADMIN'"
                size="mini"
                :type="row.state === 0 ? 'success' : 'info'"
                @click="handleDelete(row, row.state === 0 ? 1 : 0)"
              >{{ row.state === 0 ? "启用" : "禁用" }}</el-button>
            </el-col>
            <el-col :span="5" :xs="24">
              <el-button
                v-if="row.roleCode !== 'SUPER_ADMIN'"
                size="mini"
                type="danger"
                @click="handleChangePwd(row, row.state === 0 ? 1 : 0)"
              >密码重置</el-button>
            </el-col>
          </el-row>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页信息 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.curPage"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 用户信息新增修改-->
    <el-dialog
      width="30%"
      :title="textMap[dialogStatus]"
      :show-close="false"
      :visible.sync="dialogFormVisible"
    >
      <div slot="title" style="border-bottom: 1px solid #dcdfe6;width:100%;padding-bottom: 5px;">
        <span>{{ textMap[dialogStatus] }}</span>
      </div>
      <el-form ref="dataForm" label-position="left" :rules="rules" :model="temp" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="10" style="width: 100%;">
            <el-form-item label="登录名" prop="username">
              <el-input v-model="temp.username" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10" style="width: 100%;">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model="temp.email" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="10" style="width: 100%;">
            <el-form-item label="手机号码" prop="mobile">
              <el-input v-model="temp.mobile" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="10" style="width: 100%;">
            <el-form-item v-if="dialogStatus === 'create'" label="设置密码" prop="password">
              <el-input v-model="temp.password" placeholder="默认为6个0" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="dialogStatus === 'create' ? createData() : updateData()"
        >确认</el-button>
      </div>
    </el-dialog>

    <!-- 角色列表-->
    <el-dialog
      width="50%"
      :title="textMap[dialogStatus]"
      :show-close="false"
      :visible.sync="dialogRolePageVisible"
    >
      <div slot="title" style="border-bottom: 1px solid #dcdfe6;width:100%;padding-bottom: 5px;">
        <span>角色信息设置</span>
      </div>
      <!-- 查询条件 -->
      <div class="filter-container">
        <el-input
          v-model="rolesQuery.roleName"
          placeholder="角色名"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleRoleFilter"
        />
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          @click="handleRoleFilter"
        >搜索</el-button>
      </div>
      <!-- 表格信息 -->
      <el-table
        ref="rolesTable"
        v-loading="roleListLoading"
        :data="roleList"
        fit
        highlight-current-row
        style="width: 100%;"
        align="center"
        @selection-change="handleRolesChange"
      >
        <el-table-column align="center" type="selection" min-width="100px" />
        <el-table-column label="角色名" align="center" min-width="100px">
          <template slot-scope="{ row }">
            <span class="link-type">{{ row.roleName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="角色Code" min-width="60px" align="center">
          <template slot-scope="{ row }">
            <span class="link-type">{{ row.roleCode }}</span>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页信息 -->
      <pagination
        v-show="roleTotal > 0"
        :total="roleTotal"
        :page.sync="rolesQuery.curPage"
        :limit.sync="rolesQuery.limit"
        @pagination="getRolesList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogRolePageVisible = false">取消</el-button>
        <el-button type="primary" @click="saveRolesData">确认</el-button>
      </div>
    </el-dialog>

    <!-- 用户信息新增修改-->
    <el-dialog width="30%" title="用户密码重置" :show-close="false" :visible.sync="dialogRestPwdVisible">
      <div slot="title" style="border-bottom: 1px solid #dcdfe6;width:100%;padding-bottom: 5px;">
        <span>用户密码重置</span>
      </div>
      <el-form ref="dataForm" label-position="left" :rules="rules" :model="temp" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="10" style="width: 100%;">
            <el-form-item label="新密码">
              <el-input v-model="newPassWord" placeholder="默认为6个0" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="resetPassword()">确认</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, fetchUserRoleList, saveRoles, save, resetPassword } from '@/api/sys/user'
import Pagination from '@/components/Pagination'
import { Message } from 'element-ui'
export default {
  name: 'UserInfos',
  components: { Pagination },
  filters: {},
  data() {
    return {
      tableKey: 0,
      list: null,
      roleList: null,
      total: 0,
      listLoading: true,
      roleListLoading: true,
      listQuery: {
        curPage: 1,
        limit: 10
      },
      roleTotal: 0,
      rolesQuery: {
        curPage: 1,
        limit: 10,
        full: true
      },
      temp: {
        id: undefined,
        email: '',
        username: '',
        mobile: ''
      },
      dialogFormVisible: false,
      dialogRolePageVisible: false,
      dialogRestPwdVisible: false,
      dialogStatus: '',
      textMap: {
        update: '修改用户信息',
        create: '添加后台用户'
      },
      rules: {
        username: [
          { required: true, message: '请填写用户名', trigger: 'blur' }
        ],
        mobile: [
          {
            required: true,
            message: '请填写手机号码',
            trigger: 'blur'
          }
        ],
        email: [{ required: true, message: '请填写手机邮箱', trigger: 'blur' }]
      },
      rolesSelection: [],
      newPassWord: '',
      uid: ''
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取列表数据
    getList() {
      this.listLoading = true
      fetchList(this.listQuery).then(data => {
        this.list = data.list
        this.total = data.totalCount
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1.5 * 1000)
      })
    },
    // 条件查询
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    // 信息重置
    resetTemp() {
      this.temp = {
        id: undefined,
        email: '',
        username: '',
        mobile: ''
      }
    },
    // 用户信息创建
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    handleChangePwd(row) {
      this.newPassWord = ''
      this.dialogRestPwdVisible = true
      this.uid = row.id
    },
    resetPassword() {
      this.$confirm(
        '确认重置用户密码吗?',
        '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'success'
        }
      ).then(() => {
        resetPassword({
          uid: this.uid,
          password: this.newPassWord
        }).then(data => {
          this.$notify({
            title: '成功',
            message: '用户密码重置成功',
            type: 'success',
            duration: 2000
          })
          this.newPassWord = ''
          this.uid = ''
          this.dialogRestPwdVisible = false
        }).catch(error => {
          Message({
            message: error.msg,
            type: 'error',
            duration: 5 * 1000
          })
        })
      }).catch(() => {})
    },
    createData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          save(this.temp)
            .then(data => {
              this.$notify({
                title: '成功',
                message: '用户' + this.textMap[this.dialogStatus] + '成功',
                type: 'success',
                duration: 2000
              })
              this.dialogFormVisible = false
              this.listQuery.page = 1
              this.listQuery.username = ''
              this.getList()
            })
            .catch(error => {
              Message({
                message: error.msg,
                type: 'error',
                duration: 5 * 1000
              })
            })
        }
      })
    },
    // 查看详情
    viewUser(id) {
      this.$router.push({ path: '/sys/user/view', query: { id: id }})
    },
    // 设置角色相关信息
    handleSetRole(row) {
      this.dialogRolePageVisible = true
      this.getRolesList(row)
    },
    getRolesList(row) {
      this.roleListLoading = true
      this.rolesQuery.userId = row === undefined ? this.rolesQuery.userId : row.id
      fetchUserRoleList(this.rolesQuery).then(data => {
        this.roleList = data.list
        this.roleTotal = data.totalCount
        // Just to simulate the time of the request
        setTimeout(() => {
          if (this.roleList) {
            this.roleList.forEach((item, index) => {
              if (item.checked) {
                this.$refs.rolesTable.toggleRowSelection(item, true)
              }
            })
          }
          this.roleListLoading = false
        }, 1.5 * 1000)
      })
    },
    // 条件查询
    handleRoleFilter() {
      this.rolesQuery.page = 1
      this.getRolesList()
    },
    // 权限信息设置
    handleRolesChange(val) {
      this.rolesSelection = []
      val.forEach((item, index) => {
        this.rolesSelection.push(item.id)
      })
    },
    saveRolesData() {
      this.$confirm(
        '确认角色' + (this.rolesSelection.length > 0 ? '授予' : '撤销') + '吗?',
        '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'success'
        }
      ).then(() => {
        saveRoles({
          userId: this.rolesQuery.userId,
          roleIds: this.rolesSelection
        }).then(data => {
          this.$notify({
            title: '成功',
            message: '角色' + (this.rolesSelection.length > 0 ? '授予' : '撤销') + '成功',
            type: 'success',
            duration: 2000
          })
        }).catch(error => {
          Message({
            message: error.msg,
            type: 'error',
            duration: 5 * 1000
          })
        })
      }).catch(() => {})
    }
  }
}
</script>
