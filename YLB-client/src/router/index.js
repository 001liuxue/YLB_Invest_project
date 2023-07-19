import { createRouter, createWebHistory } from 'vue-router'
import IndexView from '@/views/IndexView'

const routes = [
  {
    path: '/',
    name: 'indexView',
    //首页(主页)
    component: IndexView
  },
  {
    path: '/about',
    name: 'about',
    //懒加载  等到需要访问该页面时才会去加载，节省内存消耗
    component: () => import('../views/AboutView.vue')
  },
  {
    path: '/page/product/list',
    name: 'productList',
    //查看更多产品列表页面
    component: () => import('../views/ProductList.vue')
  },
  {
    path: '/page/product/detail',
    name: 'productDetail',
    //查看更多产品列表页面
    component: () => import('../views/ProductDetail.vue')
  },
  {
    path: '/page/user/register',
    name: 'RegisterView',
    //用户注册页面
    component: () => import('../views/RegisterView.vue')
  },
  {
    path: '/page/user/login',
    name: 'LoginView',
    //用户登录页面
    component: () => import('../views/LoginView.vue')
  },
  {
    path: '/page/user/realname',
    name: 'RealNameView',
    //用户实名认证页面
    component: () => import('../views/RealNameView.vue')
  },
  {
    path: '/page/user/center',
    name: 'UserCenterView',
    //用户个人中心页面
    component: () => import('../views/UserCenterView.vue')
  },
  {
    path: '/page/user/pay',
    name: 'UserPayView',
    //用户个人中心页面
    component: () => import('../views/UserPayView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
