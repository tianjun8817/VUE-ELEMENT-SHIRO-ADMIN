import Vue from 'vue'
import Router from 'vue-router'

/* Layout */
import Layout from '@/layout'

Vue.use(Router)

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'             the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children: [{
      path: 'home',
      component: () => import('@/views/home/index')
    }],
    hidden: true
  },
  {
    path: '/sys',
    component: Layout,
    redirect: '/home',
    alwaysShow: true,
    code: 'sys',
    name: '系统管理',
    meta: {
      title: '系统管理'
    },
    children: [
      {
        meta: {
          title: '用户管理'
        },
        name: '用户管理',
        path: 'user/info',
        code: 'user_info',
        component: () => import('@/views/sys/user/info')
      },
      {
        meta: {
          title: '用户信息详情'
        },
        name: '用户信息详情',
        path: 'user/view',
        code: 'user_view',
        component: () => import('@/views/sys/user/view'),
        hidden: true
      },
      {
        meta: {
          title: '角色管理'
        },
        name: '角色管理',
        path: 'role/info',
        code: 'role_info',
        component: () => import('@/views/sys/role/info')
      },
      {
        meta: {
          title: '权限信息管理'
        },
        name: '权限信息管理',
        path: 'perm/info',
        code: 'perm_info',
        component: () => import('@/views/sys/perm/info')
      },
      {
        meta: {
          title: '权限信息详情'
        },
        name: '权限信息详情',
        path: 'perm/view',
        code: 'perm_view',
        component: () => import('@/views/sys/perm/view'),
        hidden: true
      },
      {
        meta: {
          title: '角色信息详情'
        },
        name: '角色信息详情',
        path: 'role/view',
        code: 'role_view',
        component: () => import('@/views/sys/role/view'),
        hidden: true
      }
    ]
  },
  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
