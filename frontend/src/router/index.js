import { createRouter, createWebHistory } from 'vue-router'
import Home from "../views/Home.vue";
import Login from "../views/Login.vue";
import Register from "../views/Register.vue";
import Products from "../views/Products.vue";
import ProductDetails from "../views/ProductDetails.vue";
import Cart from "../views/Cart.vue";
import Checkout from "../views/Checkout.vue";
import Orders from "../views/Orders.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
      { path: "/", component: Home },
      { path: "/login", component: Login },
      { path: "/register", component: Register },
      { path: "/products", component: Products },
      { path: "/product/:id", component: ProductDetails },
      { path: "/cart", component: Cart },
      { path: "/checkout", component: Checkout },
      { path: "/orders", component: Orders },
  ],
})

export default router
