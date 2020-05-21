<template>
  <div>
    <!-- 表格信息 -->
    <el-table
      v-loading="listLoading"
      :data="menuTree"
      row-key="id"
      default-expand-all
      highlight-current-row
      style="width: 100%;"
      :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
    >
      <el-table-column label="菜单名称" align="letf" min-width="20%">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.name }}</span>
        </template>
      </el-table-column>
      <el-table-column label="菜单CODE" align="letf" min-width="20%">
        <template slot-scope="{ row }">
          <span class="link-type">{{ row.code }}</span>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { fetchUserMenuTree } from '@/api/sys/user'

export default {
  props: {
    userId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      listLoading: true,
      listQuery: {
        userId: this.$route.query.id
      },
      menuTree: [
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
      fetchUserMenuTree(this.listQuery).then(data => {
        this.menuTree = data
        // Just to simulate the time of the request
        setTimeout(() => {
          this.listLoading = false
        }, 1 * 500)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
</style>
