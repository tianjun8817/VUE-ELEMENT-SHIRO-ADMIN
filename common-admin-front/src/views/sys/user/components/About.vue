<template>
  <el-card style="margin-bottom:20px;">
    <div slot="header" class="clearfix">
      <div class="user-bio"><svg-icon icon-class="component" /><span>基本信息</span></div>
    </div>
    <div class="user-profile">
      <el-row :gutter="5">
        <el-col :span="9">
          <span class="message-title">
            用户编号
          </span>
        </el-col>
        <el-col :span="10">
          <span>
            {{ user.id }}
          </span>
        </el-col>
      </el-row>
    </div>
    <div class="user-profile">
      <el-row :gutter="5">
        <el-col :span="9">
          <span class="message-title">
            用户名
          </span>
        </el-col>
        <el-col :span="10">
          <span>
            {{ user.username }}
          </span>
        </el-col>
      </el-row>
    </div>
    <div class="user-profile">
      <el-row :gutter="5">
        <el-col :span="9">
          <span class="message-title">
            手机号码
          </span>
        </el-col>
        <el-col :span="10">
          <span>
            {{ user.mobile }}
          </span>
        </el-col>
      </el-row>
    </div>
    <div class="user-profile">
      <el-row :gutter="5">
        <el-col :span="9">
          <span class="message-title">
            邮箱
          </span>
        </el-col>
        <el-col :span="10">
          <span>
            {{ user.email }}
          </span>
        </el-col>
      </el-row>
    </div>
  </el-card>
</template>

<script>

export default {
  props: {
    user: {
      type: Object,
      default: () => {
        return {
          id: '',
          username: '',
          email: '',
          mobile: ''
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

.user-profile {
  padding-bottom: 20px;
  .user-name {
    font-weight: bold;
  }

  .box-center {
    padding-top: 10px;
  }

  .user-user {
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

  .user-follow {
    padding-top: 20px;
  }
}

.user-bio {

  span {
    padding-left: 4px;
  }

  .user-bio-section {
    font-size: 14px;
    padding: 15px 0;

    .user-bio-section-header {
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
