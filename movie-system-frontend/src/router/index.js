import { createRouter, createWebHistory } from "vue-router";
import LoginView from '../views/Login.vue'
import LayoutView from '../views/Layout.vue'
import HomeView from '../views/Home.vue'
import MovieDetailView from '../views/MovieDetail.vue'
import AdminLayoutView from "../views/admin/AdminLayout.vue";
import MovieManageView from "../views/admin/MovieManage.vue";
import DashboardView from "../views/admin/Dashboard.vue";
import UserCenterView from "../views/user/UserCenter.vue";
import ActorManageView from "../views/admin/ActorManage.vue";
import DirectorManageView from "../views/admin/DirectorManage.vue";
import UserManageView from "../views/admin/UserManage.vue";
import ReviewManageView from "../views/admin/ReviewManage.vue";
import ActorDetailView from "../views/ActorDetail.vue";
import DirectorDetailView from "../views/DirectorDetail.vue";
import SearchResultView from "../views/SearchResult.vue";

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: LoginView,
        meta: { title: '用户登录' }
    },
    {
        path: '/',
        name: 'Layout',
        component: LayoutView,
        redirect: '/home',
        children: [
            {
                path: 'home',
                name: 'Home',
                component: HomeView,
                meta: { title: '首页' }
            },
            {
                path: '/movie/:id',
                name: 'MovieDetail',
                component: MovieDetailView,
                meta: { title: '电影详情' }
            },
            {
                path: 'user-center',
                name: 'UserCenter',
                component: UserCenterView,
                meta: { title: '个人中心' }
            },
            {
                path: '/actor/:id',
                name: 'ActorDetail',
                component: ActorDetailView,
                meta: { title: '演员详情' }
            },
            {
                path: '/director/:id',
                name: 'DirectorDetail',
                component: DirectorDetailView,
                meta: { title: '导演详情' }
            },
            {
                path: '/search',
                name: 'SearchResult',
                component: SearchResultView,
                meta: { title: '搜索结果' }
            }
        ]
    },
    {
        path: '/admin',
        component: AdminLayoutView,
        meta: { title: '管理员' },
        redirect: '/admin/dashboard',
        //路由守卫：只有管理员能进
        beforeEnter: (to, from, next) => {
            const user = JSON.parse(localStorage.getItem('user') || '{}')
            if (user.role === 'admin') {
                next()
            } else {
                alert('无权访问')
                next('/login')
            }
        },
        children: [
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: DashboardView,
                meta: { title: '系统概览' }
            },
            {
                path: 'movie',
                name: 'AdminMovie',
                component: MovieManageView,
                meta: { title: '电影管理' }
            },
            {
                path: 'actor',
                name: 'ActorManage',
                component: ActorManageView,
                meta: { title: '演员管理' }
            },
            {
                path: 'director',
                name: 'DirectorManage',
                component: DirectorManageView,
                meta: { title: '导演管理' }
            },
            {
                path: 'user',
                name: 'UserManage',
                component: UserManageView,
                meta: { title: '用户管理' }
            },
            {
                path: 'review',
                name: 'ReviewManage',
                component: ReviewManageView,
                meta: { title: '影评管理' }
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to, from, next) => {
    // 如果路由有 meta.title，就设置文档标题
    if (to.meta.title) {
        document.title = `${to.meta.title} - 电影评分系统`
    } else {
        document.title = '电影评分系统' // 默认标题
    }
    next()
})

export default router