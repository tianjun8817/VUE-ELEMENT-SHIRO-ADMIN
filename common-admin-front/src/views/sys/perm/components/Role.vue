<template>
  <div>
    <!-- 表格信息 -->
    <el-table
      v-loading="listLoading"
      :data="roleList"
      highlight-current-row
      style="width: 100%;"
      align="center"
    >
      <el-table-column label="角色编号" prop="id" align="center" min-width="30%">
        <template slot-scope="{ row }">
          <el-button type="text" icon="el-icon-view" @click="viewRole(row.id)">{{ row.id }}</el-button>
        </template>
      </el-table-column>
      <el-table-column label="角色名称" align="center" min-width="20%">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.roleName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="角色CODE" align="center" min-width="20%">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.roleCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="时间" min-width="30%" align="center">
        <template slot-scope="{ row }">
          <div>
            创建于 <span>{{ row.ctime | parseTime("{y}-{m}-{d} {h}:{i}") }}</span>
          </div>
          <div v-if="row.utime">
            修改于 <span>{{ row.utime | parseTime("{y}-{m}-{d} {h}:{i}") }}</span>
          </div>
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
  </div>
</template>

<script>
import { fetchPermRoleList } from '@/api/sys/perm'
import Pagination from '@/components/Pagination'

export default {
  components: { Pagination },
  props: {
    permId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      listLoading: true,
      listQuery: {
        permId: this.$route.query.id,
        curPage: 1,
        limit: 10
      },
      total: 0,
      roleList: [
      ]
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取列表数据
    getList() {
      this.listLoading = true
      fetchPermRoleList(this.listQuery).then(data => {
        this.roleList = data.list
        this.total = data.totalCount
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1 * 500)
      })
    },
    // 查看详情
    viewRole(id) {
      this.$router.push({ path: '/sys/role/view', query: { id: id }})
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
