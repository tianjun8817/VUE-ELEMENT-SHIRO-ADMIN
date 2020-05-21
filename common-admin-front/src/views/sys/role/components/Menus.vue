<template>
  <el-card>
    <div class="menus-profile">
      <el-tree
        ref="tree"
        v-loading="menuLoading"
        :data="menus"
        :show-checkbox="true"
        node-key="id"
        :props="defaultProps"
        el-tree
        default-expand-all
      />
    </div>
  </el-card>
</template>

<script>

import { roleMenus } from '@/api/sys/role'
export default {
  data() {
    return {
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      menus: [],
      menuLoading: true
    }
  },
  methods: {
    // 复选框点击事件
    initRoleMenus(roleId) {
      setTimeout(() => {
        roleMenus({ roleId: roleId }).then(data => {
          this.menus = data
          this.menuTree()
        })
        this.menuLoading = false
      }, 1 * 1000)
    },
    menuTree() {
      const checkFunc = (data) => {
        data.forEach((item) => {
          if (item.checked) {
            this.$nextTick(function() {
              this.$refs.tree.setChecked(item.id, true)
            })
            if (item.children) {
              checkFunc(item.children)
            }
          }
        })
      }
      if (this.menus) {
        checkFunc(this.menus)
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.box-center {
  margin: 0 auto;
  display: table;
}

.text-muted {
  color: #777;
}

.menus-profile {
  padding-bottom: 20px;
  .menus-name {
    font-weight: bold;
  }

  .box-center {
    padding-top: 10px;
  }

  .menus-role {
    padding-top: 10px;
    font-weight: 400;
    font-size: 14px;
  }

  .box-social {
    padding-top: 30px;

    .el-table {
      border-top: 1px solid #dfe6ec;
    }
  }

  .menus-follow {
    padding-top: 20px;
  }
}

.menus-bio {
  span {
    padding-left: 4px;
  }

  .menus-bio-section {
    font-size: 14px;
    padding: 15px 0;

    .menus-bio-section-header {
      border-bottom: 1px solid #dfe6ec;
      padding-bottom: 10px;
      margin-bottom: 10px;
      font-weight: bold;
    }
  }
}
.message-title {
  font-size: 16px;
  color: #333;
  font-weight: bold;
  padding-right: 8px;
}
</style>
