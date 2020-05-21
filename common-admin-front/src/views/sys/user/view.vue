<template>
  <div class="app-container">
    <div v-if="user">
      <el-row :gutter="20">
        <el-col :span="7" :xs="24">
          <about :user="user" />
        </el-col>
        <el-col :span="17" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="相关角色" name="role">
                <role :user-id="user.id" />
              </el-tab-pane>
              <el-tab-pane label="相关菜单" name="menu" :lazy="true">
                <user-menu :user-id="user.id" />
              </el-tab-pane>
              <el-tab-pane label="操作日志" name="log" :lazy="true">
                <log :ref-id="user.id" />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { view } from '@/api/sys/user'
import Log from '@/components/Log'
import About from './components/About'
import Role from './components/Role'
import UserMenu from './components/UserMenu'

export default {
  name: 'UserView',
  components: { Log, UserMenu, About, Role },
  data() {
    return {
      user: {},
      activeTab: 'role',
      roleShow: false,
      logShow: false
    }
  },
  created() {
    view({ id: this.$route.query.id }).then(data => {
      this.user = data
    })
  },
  methods: {
  }
}
</script>
