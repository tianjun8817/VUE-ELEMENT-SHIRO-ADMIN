import router from './router'
import store from './store'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import { getToken } from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import { Message } from 'element-ui'
// import Layout from '@/layout'
// const _import = require('./router/_import_' + process.env.NODE_ENV) // 获取组件的方法

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['/login', '/logout'] // no redirect whitelist

router.beforeEach(async(to, from, next) => {
  // 设置bar
  NProgress.start()

  // 设置页面头
  document.title = getPageTitle(to.meta.title)

  // 获取当前登录的token
  const hasToken = getToken()

  // 前台已经登录
  if (hasToken) {
    // 判断是否登录
    if (to.path === '/login') {
      // 如果已经登录完成,跳转到首页
      next()
      // 进度条
      NProgress.done()
      // eslint-disable-next-line brace-style
    }
    // 不是登录页面
    else {
      // 获取当前用户名称
      const menus = store.getters.menus
      // 已经存在用户名称
      if (menus.length > 0) {
        // router.addRoutes(menus)
        next()
        // eslint-disable-next-line brace-style
      }
      // 不存在,再次获取
      else {
        try {
          // 获取用户信息
          const { menus, roles, perms } = await store.dispatch('user/getInfo')
          // dynamically add accessible routes
          const accessRoutes = await store.dispatch('menu/generateRoutes', menus)
          await store.dispatch('auth/userAuth', { roles, perms })
          // set the replace: true, so the navigation will not leave a history record
          if (accessRoutes && accessRoutes.length > 0) {
            next({ ...to, replace: true })
          } else {
            Message({
              message: '没有任何菜单权限,请确认权限信息',
              type: 'error',
              duration: 5 * 1000
            })
            NProgress.done()
          }
        } catch (error) {
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/
    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
