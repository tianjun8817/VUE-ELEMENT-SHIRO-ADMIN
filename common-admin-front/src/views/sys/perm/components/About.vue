<template>
  <el-card style="margin-bottom:20px;">
    <div slot="header" class="clearfix">
      <div class="perm-bio"><svg-icon icon-class="component" /><span>基本信息</span></div>
    </div>
    <div class="perm-profile">
      <el-row :gutter="5">
        <el-col :span="9">
          <span class="message-title">
            权限编号
          </span>
        </el-col>
        <el-col :span="10">
          <span>
            {{ perm.id }}
          </span>
        </el-col>
      </el-row>
    </div>
    <div class="perm-profile">
      <el-row :gutter="5">
        <el-col :span="9">
          <span class="message-title">
            权限值
          </span>
        </el-col>
        <el-col :span="10">
          <span>
            {{ perm.perm }}
          </span>
        </el-col>
      </el-row>
    </div>
    <div class="perm-bio">
      <div class="perm-education perm-bio-section">
        <div class="perm-bio-section-header"><svg-icon icon-class="eye-open" /><span>权限值说明</span></div>
        <div class="perm-bio-section-body">
          <div class="text-muted">
            {{ perm.memo }}
          </div>
        </div>
      </div>
    </div>
  </el-card>
</template>

<script>

export default {
  props: {
    perm: {
      type: Object,
      default: () => {
        return {
          id: '',
          perm: ''
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

.perm-profile {
  padding-bottom: 20px;
  .perm-name {
    font-weight: bold;
  }

  .box-center {
    padding-top: 10px;
  }

  .perm-role {
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

  .perm-follow {
    padding-top: 20px;
  }
}

.perm-bio {

  span {
    padding-left: 4px;
  }

  .perm-bio-section {
    font-size: 14px;
    padding: 15px 0;

    .perm-bio-section-header {
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
