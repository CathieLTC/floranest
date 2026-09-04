import { defineStore } from "pinia";
import { ElMessage } from "element-plus";

export const useWishlistStore = defineStore("wishlist", {
    state: () => ({
        items: JSON.parse(localStorage.getItem("wishlist")) || []
    }),

    getters: {
        count: (state) => state.items.length,
        has: (state) => (productId) =>
            state.items.some(item => item.productId === productId)
    },

    actions: {
        toggle(product) {
            if (this.has(product.productId)) {
                this.items = this.items.filter(item => item.productId !== product.productId);
                ElMessage.success("Removed from wishlist.");
            } else {
                this.items.push(product);
                ElMessage.success("Added to wishlist!");
            }
            localStorage.setItem("wishlist", JSON.stringify(this.items));
        },

        remove(productId) {
            this.items = this.items.filter(item => item.productId !== productId);
            localStorage.setItem("wishlist", JSON.stringify(this.items));
        },

        clear() {
            this.items = [];
            localStorage.removeItem("wishlist");
        }
    }
});
