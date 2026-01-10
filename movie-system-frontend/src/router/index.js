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

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: LoginView
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
                component: HomeView
            },
            {
                path: '/movie/:id',
                name: 'MovieDetail',
                component: MovieDetailView
            },
            {
                path: 'user-center',
                name: 'UserCenter',
                component: UserCenterView
            },
            {
                path: '/actor/:id',
                name: 'ActorDetail',
                component: ActorDetailView
            },
            {
                path: '/director/:id',
                name: 'DirectorDetail',
                component: DirectorDetailView
            }
        ]
    },
    {
        path: '/admin',
        component: AdminLayoutView,
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
                component: DashboardView
            },
            {
                path: 'movie',
                name: 'AdminMovie',
                component: MovieManageView
            },
            {
                path: 'actor',
                name: 'ActorManage',
                component: ActorManageView
            },
            {
                path: 'director',
                name: 'DirectorManage',
                component: DirectorManageView
            },
            {
                path: 'user',
                name: 'UserManage',
                component: UserManageView
            },
            {
                path: 'review',
                name: 'ReviewManage',
                component: ReviewManageView
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router