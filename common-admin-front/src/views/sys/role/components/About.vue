<template>
  <el-card style="margin-bottom:20px;">
    <div slot="header" class="clearfix">
      <div class="role-bio"><svg-icon icon-class="component" /><span>基本信息</span></div>
    </div>
    <div class="role-profile">
      <el-row :gutter="5">
        <el-col :span="9">
          <span class="message-title">
            角色编号
          </span>
        </el-col>
        <el-col :span="10">
          <span>
            {{ role.id }}
          </span>
        </el-col>
      </el-row>
    </div>
    <div class="role-profile">
      <el-row :gutter="5">
        <el-col :span="9">
          <span class="message-title">
            角色名
          </span>
        </el-col>
        <el-col :span="10">
          <span>
            {{ role.roleName }}
          </span>
        </el-col>
      </el-row>
    </div>
    <div class="role-profile">
      <el-row :gutter="5">
        <el-col :span="9">
          <span class="message-title">
            角色值
          </span>
        </el-col>
        <el-col :span="10">
          <span>
            {{ role.roleCode }}
          </span>
        </el-col>
      </el-row>
    </div>
    <div class="role-bio">
      <div class="role-education role-bio-section">
        <div class="role-bio-section-header"><svg-icon icon-class="eye-open" /><span>角色说明</span></div>
        <div class="role-bio-section-body">
          <div class="text-muted">
            {{ role.remark }}
          </div>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>

export default {
  props: {
    role: {
      type: Object,
      default: () => {
        return {
          id: '',
          roleCode: '',
          roleName: '',
          remark: ''
        }
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

.role-profile {
  padding-bottom: 20px;
  .role-name {
    font-weight: bold;
  }

  .box-center {
    padding-top: 10px;
  }

  .role-role {
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

  .role-follow {
    padding-top: 20px;
  }
}

.role-bio {

  span {
    padding-left: 4px;
  }

  .role-bio-section {
    font-size: 14px;
    padding: 15px 0;

    .role-bio-section-header {
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
