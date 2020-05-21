<template>
  <div class="app-container">
    <div v-if="perm">
      <el-row :gutter="20">

        <el-col :span="7" :xs="24">
          <about :perm="perm" />
        </el-col>

        <el-col :span="17" :xs="24">
          <el-card>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="相关角色" name="role">
                <role :perm-id="perm.id" />
              </el-tab-pane>
              <el-tab-pane label="操作日志" name="log" :lazy="true">
                <log :ref-id="perm.id" />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>

      </el-row>
    </div>
  </div>
</template>

<script>
import { view } from '@/api/sys/perm'
import Log from '@/components/Log'
import About from './components/About'
import Role from './components/Role'

export default {
  name: 'PermView',
  components: { About, Role, Log },
  data() {
    return {
      perm: {},
      activeTab: 'role',
      roleShow: false,
      logShow: false
    }
  },
  created() {
    view({ id: this.$route.query.id }).then(data => {
      this.perm = data
    })
  },
  methods: {
  }
}
</script>
