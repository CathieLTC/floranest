import { createRouter, createWebHistory } from "vue-router";

// Home
import Home from "@/views/home/Home.vue";

// Auth
import Login    from "@/views/auth/Login.vue";
import Register from "@/views/auth/Register.vue";

// Product
import ProductDetails from "@/views/product/ProductDetails.vue";
import Shop          from "@/views/product/Shop.vue";

// Cart
import Cart from "@/views/cart/Cart.vue";

// Order
import Checkout         from "@/views/order/Checkout.vue";
import Payment          from "@/views/order/Payment.vue";
import OrderConfirmation from "@/views/order/OrderConfirmation.vue";
import MyOrders         from "@/views/order/MyOrders.vue";
import OrderHistory     from "@/views/order/OrderHistory.vue";

// User
import MyProfile from "@/views/user/MyProfile.vue";

// AI
import AiAssistant from "@/views/ai/AiAssistant.vue";
import PlantCare   from "@/views/ai/PlantCare.vue";

const routes = [
  // Home
  { path: "/",           component: Home },

  // Auth
  { path: "/login",      component: Login },
  { path: "/register",   component: Register },

  // Product
  // "/products" is kept as an alias for "/shop" so old links/bookmarks
  // still work, but Shop.vue is now the single canonical listing page.
  { path: "/products",       redirect: (to) => ({ path: "/shop", query: to.query }) },
  { path: "/product/:id",    component: ProductDetails },
  { path: "/shop",           component: Shop },

  // Cart
  { path: "/cart",           component: Cart },

  // Order
  { path: "/checkout",            component: Checkout },
  { path: "/payment",             component: Payment },
  { path: "/order-confirmation",  component: OrderConfirmation },
  { path: "/my-orders",           component: MyOrders },
  { path: "/order-history",       component: OrderHistory },

  // User
  { path: "/my-profile",     component: MyProfile },

  // AI
  { path: "/ai",             component: AiAssistant },
  { path: "/plant-care",     component: PlantCare },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;