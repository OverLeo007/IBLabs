import { createRouter, createWebHashHistory } from "vue-router";

import Index from "@/pages/Index.vue";
import Login from "@/pages/Login.vue";
import Register from "@/pages/Register.vue";


const routes = [
    { path: '/', component: Index},
    { path: '/login', component: Login},
    { path: '/register', component: Register},
];

export default createRouter({
    history: createWebHashHistory(),
    routes,
});