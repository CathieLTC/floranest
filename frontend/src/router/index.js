import { createRouter, createWebHistory } from "vue-router";

// Home
import Home from "@/views/home/Home.vue";

// Auth
import Login    from "@/views/auth/Login.vue";
import Register from "@/views/auth/Register.vue";

// Product
import Products    from "@/views/product/Products.vue";
import ProductDetails from "@/views/product/ProductDetails.vue";
import Shop          from "@/views/product/Shop.vue";
import SearchResults from "@/views/product/SearchResults.vue";
import ToolsPots from "@/views/product/ToolsPots.vue";

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
  import ProductManagement from "@/views/admin/ProductManagement.vue";
  import CategoryManagement from "@/views/admin/CategoryManagement.vue";
  import OrderManagement from "@/views/admin/OrderManagement.vue";
  import UserManagement from "@/views/admin/UserManagement.vue";
  import AdminLayout from "@/components/layout/AdminLayout.vue";
  // import { useUserStore } from "@/stores/user";

const routes = [
  {
    path: "/admin",
    component: AdminLayout,
    meta: { requiresAdmin: true },
    children: [
      { path: "", component: AdminDashboard }, // Default admin view
      { path: "products", component: ProductManagement },
      { path: "categories", component: CategoryManagement },
      { path: "orders", component: OrderManagement },
      { path: "users", component: UserManagement },
    ],
  },

  // Home
  { path: "/",           component: Home },

  // Auth
  { path: "/login",      component: Login },
  { path: "/register",   component: Register },

  // Product
  { path: "/product/:id",    component: ProductDetails },
  { path: "/shop",           component: Shop },
  { path: "/products",        component: Products },
  { path: "/search",          component: SearchResults },
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
  { path: "/about", component: About },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// router.beforeEach((to, from, next) => {
//   // `to`, `from`, and `next` are required parameters for Vue Router navigation guards.
//   // `from` is used implicitly by the navigation process.
//   // `next` is replaced by returning values as per Vue Router 4+ guidelines.
//   if (to.matched.some(record => record.meta.requiresAdmin)) {
//     const userStore = useUserStore();
//     const user = userStore.user;
//     if (user && user.role === 'admin') {
//       return true;
//     } else {
//       return { path: '/login' }; // Redirect to login or home page
//     }
//   } else {
//     return true;
//   }
// });

export default router;
