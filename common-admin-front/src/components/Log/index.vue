<template>
  <div>
    <!-- 表格信息 -->
    <el-table
      v-loading="listLoading"
      :data="logList"
      highlight-current-row
      style="width: 100%;"
      align="center"
    >
      <el-table-column
        label="日志编号"
        prop="id"
        align="center"
        min-width="30%"
      >
        <template slot-scope="{ row }">
          {{ row.id }}
        </template>
      </el-table-column>
      <el-table-column label="日志内容" align="center" min-width="20%">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.msg }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作人" align="center" min-width="20%">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.uname }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时间" min-width="30%" align="center">
        <template slot-scope="{ row }">
          <div>
            创建于
            <span>{{ row.ctime | parseTime("{y}-{m}-{d} {h}:{i}") }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        label="查看提交对象"
        align="center"
        min-width="20%"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="{ row }">
          <el-button type="primary" size="mini" @click="logDetail(row)">
            查看
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

    <!-- 用户信息新增修改-->
    <el-dialog
      title="操作对象内容"
      :show-close="false"
      :visible.sync="dialogVisible"
    >
      <div
        slot="title"
        style="border-bottom: 1px solid #dcdfe6;width:100%;padding-bottom: 5px;"
      >
        <span>操作对象内容</span>
      </div>
      <div class="editor-container">
        <json-editor v-model="detail" />
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">
          取消
        </el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import JsonEditor from '@/components/JsonEditor'
import { fetchOperList } from '@/api/log/operate_log'
import Pagination from '@/components/Pagination'
export default {
  components: { Pagination, JsonEditor },
  props: {
    refId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      listLoading: true,
      listQuery: {
        refId: this.refId,
        curPage: 1,
        limit: 10
      },
      total: 0,
      dialogVisible: false,
      logList: [],
      detail: ''
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取列表数据
    getList() {
      this.listLoading = true
      fetchOperList(this.listQuery).then(data => {
        this.logList = data.list
        this.total = data.totalCount
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1 * 500)
      })
    },
    // 用户信息创建
    logDetail(row) {
      // 服务详细信息
      this.detail = JSON.parse(row.res)
      this.dialogVisible = true
    }
  }
}
</script>
