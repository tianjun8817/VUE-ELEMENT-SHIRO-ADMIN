<template>
  <div class="app-container">
    <div v-if="role">
      <el-row :gutter="20">
        <el-col :span="7" :xs="24">
          <about :role="role" />
        </el-col>
        <el-col :span="17" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="相关用户" name="role">
                <user :role-id="role.id" />
              </el-tab-pane>
              <el-tab-pane label="操作日志" name="log" :lazy="true">
                <log :role-id="role.id" />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>

      </el-row>
    </div>
  </div>
</template>

<script>
import { view } from '@/api/sys/role'
import Log from '@/components/Log'
import About from './components/About'
import User from './components/User'

export default {
  name: 'RoleView',
  components: { About, User, Log },
  data() {
    return {
      role: {},
      activeTab: 'role',
      roleShow: false,
      logShow: false
    }
  },
  created() {
    view({ id: this.$route.query.id }).then(data => {
      this.role = data
    })
  },
  methods: {
  }
}
</script>
