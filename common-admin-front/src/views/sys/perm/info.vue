<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-input
        v-model="listQuery.perm"
        placeholder="填写权限值"
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
      <el-table-column label="权限编号" prop="id" align="center" min-width="180px">
        <template slot-scope="{ row }">
          <el-button type="text" icon="el-icon-view" @click="viewPerm(row.id)">{{ row.id }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="权限值" align="center" min-width="150px">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.perm }}</span>
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center" min-width="150px">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.memo }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" min-width="60px" align="center">
        <template slot-scope="{ row }">
          <el-tag :type="row.state | genericStateCSS">{{ row.state | genericStateStr }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建人" min-width="60px" align="center">
        <template slot-scope="{ row }">
          <el-tag>{{ row.cname }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="时间" min-width="150px" align="center">
        <template slot-scope="{ row }">
          <div>
            创建于 <span>{{ row.ctime | parseTime("{y}-{m}-{d} {h}:{i}") }}</span>
          </div>
          <div>
            修改于 <span>{{ row.utime | parseTime("{y}-{m}-{d} {h}:{i}") }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        v-if="hasRole('SUPER_ADMIN')"
        label="操作"
        align="center"
        min-width="230px"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{ row }">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button
            size="mini"
            :type="row.state ===0?'success':'danger'"
            @click="handleDelete(row,row.state === 0 ? 1 : 0)"
          >
            {{ row.state ===0?'启用':'禁用' }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="listQuery.curPage"
      :limit.sync="listQuery.limit"
      @pagination="getList"
    />

    <!-- 权限信息新增修改-->
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
            <el-form-item label="权限值" prop="perm">
              <el-input v-model="temp.perm" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="20">
            <el-form-item label="描述" prop="memo">
              <el-input v-model="temp.memo" type="textarea" :rows="4" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">
          取消
        </el-button>
        <el-button
          type="primary"
          @click="saveData()"
        >
          确认
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { fetchList, save } from '@/api/sys/perm'
import { hasRole } from '@/utils/auth'
import { Message } from 'element-ui'
import Pagination from '@/components/Pagination'
export default {
  name: 'PermInfo',
  components: { Pagination },
  filters: {},
  data() {
    return {
      tableKey: 0,
      list: null,
      total: 0,
      listLoading: true,
      listQuery: {
        curPage: 1,
        limit: 10
      },
      temp: {
        id: '',
        perm: '',
        memo: ''
      },
      dialogFormVisible: false,
      dialogStatus: '',
      textMap: {
        update: '修改权限信息',
        create: '添加权限信息'
      },
      rules: {
        perm: [{ required: true, message: '请填写权限值', trigger: 'blur' }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    hasRole,
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
        perm: '',
        memo: ''
      }
    },
    // 权限信息创建
    handleCreate() {
      this.resetTemp()
      this.dialogStatus = 'create'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 权限信息修改
    handleUpdate(row) {
      this.resetTemp()
      this.temp = Object.assign({}, row) // copy obj
      this.dialogStatus = 'update'
      this.dialogFormVisible = true
      this.$nextTick(() => {
        this.$refs['dataForm'].clearValidate()
      })
    },
    // 权限信息创建提交
    saveData() {
      this.$refs['dataForm'].validate(valid => {
        if (valid) {
          save(this.temp).then(data => {
            this.$notify({
              title: '成功',
              message: '权限值' + this.textMap[this.dialogStatus] + '成功',
              type: 'success',
              duration: 2000
            })
            this.dialogFormVisible = false
            this.listQuery.page = 1
            this.listQuery.perm = ''
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
    // 权限信息删除
    handleDelete(row, state) {
      this.$confirm('确认' + (state === 1 ? '启用' : '禁用') + '该权限值吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: state === 1 ? 'success' : 'error'
      }).then(() => {
        save({
          id: row.id,
          state: state,
          perm: row.perm
        }).then(data => {
          this.$notify({
            title: '成功',
            message: '权限值' + (state === 1 ? '启动' : '禁用') + '成功',
            type: 'success',
            duration: 2000
          })
          this.listQuery.page = 1
          this.listQuery.perm = ''
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
    viewPerm(id) {
      this.$router.push({ path: '/sys/perm/view', query: { id: id }})
    }
  }
}
</script>
