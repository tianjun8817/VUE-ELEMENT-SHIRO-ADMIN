<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.roleCode"
        placeholder="填写角色CODE"
        style="width: 200px;"
        class="filter-item"
        @keyup.enter.native="handleFilter"
      />
      <el-button
        class="filter-item"
        type="primary"
        icon="el-icon-search"
        @click="handleFilter"
      >
        搜索
      </el-button>
      <el-button
        class="filter-item"
        style="margin-left: 10px;"
        type="primary"
        icon="el-icon-edit"
        @click="handleCreate"
      >
        添加
      </el-button>
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
      <el-table-column
        label="角色编号"
        prop="id"
        align="center"
        min-width="18%"
      >
        <template slot-scope="{ row }">
          <el-button
            type="text"
            icon="el-icon-view"
            @click="viewRole(row.id)"
          >{{ row.id }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="角色名" align="center" min-width="20%">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.roleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="角色CODE" align="center" min-width="15%">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.roleCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" min-width="10%">
        <template slot-scope="{ row }">
          <el-tag :type="row.state | genericStateCSS">{{
            row.state | genericStateStr
          }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="时间" align="center" min-width="20%">
        <template slot-scope="{ row }">
          <div>
            创建于
            <span style="font-size:5px">{{ row.ctime | parseTime("{y}-{m}-{d} {h}:{i}") }}</span>
          </div>
          <div v-if="row.utime">
            修改于
            <span style="font-size:5px">{{ row.utime | parseTime("{y}-{m}-{d} {h}:{i}") }}</span>
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
            <el-col :span="5" :xs="24">
              <el-button
                v-if="row.state != 0"
                size="mini"
                type="primary"
                @click="getPermsList(row)"
              >
                权限
              </el-button>
            </el-col>
            <el-col :span="5" :xs="24">
              <el-button
                v-if="row.state != 0"
                size="mini"
                type="primary"
                @click="handleMenus(row)"
              >
                菜单
              </el-button>
            </el-col>
            <el-col :span="5" :xs="24">
              <el-button
                v-if="row.state != 0"
                type="warning"
                size="mini"
                @click="handleUpdate(row)"
              >
                编辑
              </el-button>
            </el-col>
            <el-col :span="5" :xs="24">
              <el-button
                v-if="row.roleCode !== 'SUPER_ADMIN'"
                size="mini"
                :type="row.state === 0 ? 'success' : 'danger'"
                @click="handleDelete(row, row.state === 0 ? 1 : 0)"
              >
                {{ row.state === 0 ? "启用" : "禁用" }}
              </el-button>
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

    <!-- 角色信息新增修改-->
    <el-dialog
      width="30%"
      :title="textMap[dialogStatus]"
      :show-close="false"
      :visible.sync="dialogFormVisible"
    >
      <div
        slot="title"
        style="border-bottom: 1px solid #dcdfe6;width:100%;padding-bottom: 5px;"
      >
        <span>{{ textMap[dialogStatus] }}</span>
      </div>
      <el-form ref="dataForm" :rules="rules" :model="temp" label-width="80px">
        <el-row :gutter="20">
          <el-col :span="20">
            <el-form-item label="角色值" prop="roleCode">
              <el-input v-model="temp.roleCode" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="20">
            <el-form-item label="角色名" prop="roleName">
              <el-input v-model="temp.roleName" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="20">
            <el-form-item label="描述" prop="remark">
              <el-input v-model="temp.remark" type="textarea" :rows="4" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="saveData()">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 权限列表-->
    <el-dialog
      width="50%"
      :title="textMap[dialogStatus]"
      :show-close="false"
      :visible.sync="dialogPermPageVisible"
    >
      <div
        slot="title"
        style="border-bottom: 1px solid #dcdfe6;width:100%;padding-bottom: 5px;"
      >
        <span>权限信息设置</span>
      </div>
      <!-- 查询条件 -->
      <div class="filter-container">
        <el-input
          v-model="permsQuery.perm"
          placeholder="权限值"
          style="width: 200px;"
          class="filter-item"
          @keyup.enter.native="handleFilter"
        />
        <el-button
          class="filter-item"
          type="primary"
          icon="el-icon-search"
          @click="handleFilter"
        >
          搜索
        </el-button>
      </div>
      <!-- 表格信息 -->
      <el-table
        ref="permsTable"
        v-loading="permListLoading"
        :data="permList"
        fit
        highlight-current-row
        style="width: 100%;"
        align="center"
        @selection-change="handlePermsChange"
      >
        <el-table-column align="center" type="selection" min-width="100px" />
        <el-table-column label="权限值" align="center" min-width="100px">
          <template slot-scope="{ row }">
            <span class="link-type">{{ row.perm }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" min-width="60px" align="center">
          <template slot-scope="{ row }">
            <el-tag :type="row.state | genericStateCSS">{{
              row.state | genericStateStr
            }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页信息 -->
      <pagination
        v-show="permTotal > 0"
        :total="permTotal"
        :page.sync="permsQuery.curPage"
        :limit.sync="permsQuery.limit"
        @pagination="getPermsList"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogPermPageVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="savePermsData">
          确认
        </el-button>
      </div>
    </el-dialog>

    <!-- 菜单列表-->
    <el-dialog
      width="25%"
      title="菜单信息设置"
      :show-close="false"
      :visible.sync="dialogMenusVisible"
      top="5vh"
      class="menus"
      @open="showLoading"
      @opened="openRoleMenus"
    >
      <menus ref="roleMenus" :role-menus="roleMenus" />
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogMenusVisible = false">
          取消
        </el-button>
        <el-button type="primary" @click="saveRoleMenus()">
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, save, fetchRolePermList, savePerms, saveMenus } from '@/api/sys/role'
import Pagination from '@/components/Pagination'
import { Message } from 'element-ui'
import Menus from './components/Menus'

export default {
  name: 'RoleInfo',
  components: { Pagination, Menus },
  data() {
    return {
      tableKey: 0,
      list: null,
      permList: null,
      permTotal: 0,
      total: 0,
      listLoading: true,
      permListLoading: true,
      listQuery: {
        curPage: 1,
        limit: 10
      },
      permsQuery: {
        curPage: 1,
        limit: 5,
        full: true
      },
      temp: {
        id: '',
        roleCode: '',
        roleName: '',
        remark: ''
      },
      dialogFormVisible: false,
      dialogPermPageVisible: false,
      dialogMenusVisible: false,
      dialogStatus: '',
      textMap: {
        update: '修改角色信息',
        create: '添加角色信息',
        perm: '设置权限信息'
      },
      rules: {
        roleCode: [
          { required: true, message: '请填写角色值', trigger: 'blur' }
        ],
        roleName: [{ required: true, message: '请填写角色值', trigger: 'blur' }]
      },
      permsSelection: [],
      roleId: null,
      roleMenus: []
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
        }, 1 * 1000)
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
        roleCode: '',
        roleName: '',
        remark: ''
      }
    },
    // 角色信息创建
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 角色信息修改
    handleUpdate(row) {
      this.resetTemp()
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 角色信息提交
    saveData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          save(this.temp).then(data => {
            this.$notify({
              title: '成功',
              message: '角色' + this.textMap[this.dialogStatus] + '成功',
              type: 'success',
              duration: 2000
            })
            this.dialogFormVisible = false
            this.listQuery.page = 1
            this.listQuery.role = ''
            this.getList()
          }).catch(error => {
            Message({
              message: error.msg,
              type: 'error',
              duration: 5 * 1000
            })
          })
        }
      })
    },
    // 角色信息删除
    handleDelete(row, state) {
      this.$confirm(
        '确认' + (state === 1 ? '启用' : '禁用') + '该角色吗?',
        '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: state === 1 ? 'success' : 'error'
        }
      ).then(() => {
        save({
          id: row.id,
          state: state,
          roleCode: row.roleCode
        }).then(data => {
          this.$notify({
            title: '成功',
            message: '角色' + (state === 1 ? '启动' : '禁用') + '成功',
            type: 'success',
            duration: 2000
          })
          this.listQuery.page = 1
          this.listQuery.role = ''
          this.getList()
        }).catch(error => {
          Message({
            message: error.msg,
            type: 'error',
            duration: 5 * 1000
          })
        })
      }).catch(() => {})
    },
    // 查看详情
    viewRole(id) {
      this.$router.push({ path: '/sys/role/view', query: { id: id }})
    },
    // 权限信息设置
    getPermsList(row) {
      if (row !== undefined) {
        this.permsQuery.perm = ''
      }
      this.permListLoading = true
      this.dialogPermPageVisible = true
      this.permsQuery.roleId = row === undefined ? this.permsQuery.roleId : row.id
      fetchRolePermList(this.permsQuery).then(data => {
        this.permList = data.list
        this.permTotal = data.totalCount
        // Just to simulate the time of the request
        setTimeout(() => {
          if (this.permList) {
            this.permList.forEach((item, index) => {
              if (item.checked) {
                this.$refs.permsTable.toggleRowSelection(item, true)
              }
            })
          }
          this.permListLoading = false
        }, 1 * 1000)
      })
    },
    // 条件查询
    handlePermPageFilter() {
      this.permsQuery.page = 1
      this.getPermsList()
    },
    // 权限信息设置
    handlePermsChange(val) {
      this.permsSelection = []
      val.forEach((item, index) => {
        this.permsSelection.push(item.id)
      })
    },
    savePermsData() {
      this.$confirm(
        '确认权限' + (this.permsSelection.length > 0 ? '授予' : '撤销') + '吗?',
        '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'success'
        }
      ).then(() => {
        savePerms({
          roleId: this.permsQuery.roleId,
          permIds: this.permsSelection
        }).then(data => {
          this.$notify({
            title: '成功',
            message: '权限' + (this.permsSelection.length > 0 ? '授予' : '撤销') + '成功',
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
    },
    // 菜单信息选择
    handleMenus(row) {
      this.dialogMenusVisible = true
      this.roleId = row.id
    },
    showLoading() {
      if (this.$refs.roleMenus) { this.$refs.roleMenus.menuLoading = true }
    },
    openRoleMenus() {
      this.$refs.roleMenus.initRoleMenus(this.roleId)
    },
    // 保存角色菜单列表
    saveRoleMenus() {
      const menusIds = this.$refs.roleMenus.$refs.tree.getCheckedKeys().concat(this.$refs.roleMenus.$refs.tree.getHalfCheckedKeys())
      this.$confirm(
        '确认菜单' + (menusIds.length > 0 ? '授予' : '撤销') + '吗?',
        '提示',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'success'
        }
      ).then(() => {
        saveMenus({
          roleId: this.roleId,
          menuIds: menusIds
        })
          .then(data => {
            this.$notify({
              title: '成功',
              message: '菜单' + (menusIds.length > 0 ? '授予' : '撤销') + '成功',
              type: 'success',
              duration: 2000
            })
          })
          .catch(error => {
            Message({
              message: error.msg,
              type: 'error',
              duration: 5 * 1000
            })
          })
      })
        .catch(() => {})
    }
  }
}
</script>

<style lang="scss">
.menus {
  .el-dialog__body {
    padding: 10px;
    overflow: hidden;
    overflow-y: auto;
  }
}
</style>
