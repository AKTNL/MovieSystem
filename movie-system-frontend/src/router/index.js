import { createRouter, createWebHistory } from "vue-router";
import LoginView from '../views/Login.vue'
import LayoutView from '../views/Layout.vue'
import HomeView from '../views/Home.vue'
import MovieDetailView from '../views/MovieDetail.vue'

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
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router