import { createRouter, createWebHistory } from "vue-router";

import Home from "../views/Home.vue";
import Products from "../views/Products.vue";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Shop from "../views/Shop.vue";
import Ai from "../views/Ai.vue";
import ProductDetails from "../views/ProductDetails.vue";
const routes = [
  { path: "/", component: Home },
  { path: "/products", component: Products },
  { path: "/login", component: Login },
  { path: "/register", component: Register },
  { path: "/Shop", component: Shop },
  { path: "/Ai", component: Ai },
  {path: "/product/:id",component: ProductDetails}
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;