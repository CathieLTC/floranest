import { createRouter, createWebHistory } from "vue-router";

// Home
import Home from "@/views/home/Home.vue";

// Auth
import Login    from "@/views/auth/Login.vue";
import Register from "@/views/auth/Register.vue";

// Product
import Products       from "@/views/product/Products.vue";
import ProductDetails from "@/views/product/ProductDetails.vue";
import Shop           from "@/views/product/Shop.vue";
import SearchResults  from "@/views/product/SearchResults.vue";
import ToolsPots      from "@/views/product/ToolsPots.vue";

// Cart
import Cart from "@/views/cart/Cart.vue";

// Order
import Checkout         from "@/views/order/Checkout.vue";
import Payment          from "@/views/order/Payment.vue";
import OrderConfirmation from "@/views/order/OrderConfirmation.vue";
import MyOrders         from "@/views/order/MyOrders.vue";

// User
import MyProfile from "@/views/user/MyProfile.vue";

// AI
import AiAssistant from "@/views/ai/AiAssistant.vue";

// Reviews
import Reviews from "@/views/reviews/Reviews.vue";

// Wishlist
import Wishlist from "@/views/wishlist/Wishlist.vue";

// About
import About from "@/views/about/About.vue";

// Admin
import AdminDashboard from "@/views/admin/AdminDashboard.vue";
import { useUserStore } from "@/stores/user";

const routes = [
  // Home
  { path: "/",           component: Home },

  // Auth
  { path: "/login",      component: Login },
  { path: "/register",   component: Register },

  // Product
  { path: "/product/:id",    component: ProductDetails },
  { path: "/shop",           component: Shop },
  { path: "/products",       component: Products },
  { path: "/search",         component: SearchResults },
  { path: "/tools-and-pots", component: ToolsPots },

  // Cart
  { path: "/cart",           component: Cart },

  // Order
  { path: "/checkout",            component: Checkout },
  { path: "/payment",             component: Payment },
  { path: "/order-confirmation",  component: OrderConfirmation },
  { path: "/my-orders",           component: MyOrders },

  // User
  { path: "/my-profile",     component: MyProfile },

  // AI
  { path: "/ai",             component: AiAssistant },

  // Reviews
  { path: "/reviews",        component: Reviews },

  // Wishlist
  { path: "/wishlist",       component: Wishlist },

  // About
  { path: "/about",          component: About },

  // Admin
  { path: "/admin",          component: AdminDashboard, meta: { requiresAdmin: true } },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAdmin)) {
    const userStore = useUserStore();
    const user = userStore.user;
    const isAdminUser =
      !!user && String(user.role || "").toLowerCase() === "admin";
    // Allow admins either by role (existing accounts) or by the
    // password-based admin session granted on the Sign Up page.
    if (isAdminUser || userStore.adminAuthed) {
      next();
    } else {
      next({ path: "/login" }); // Redirect to login or home page
    }
  } else {
    next();
  }
});

export default router;
