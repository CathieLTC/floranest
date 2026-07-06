import { defineStore } from "pinia";
import { ElMessage } from "element-plus";
import api from "@/api/axios";

export const useCartStore = defineStore("cart", {

    actions: {

        async addToCart(product) {

            const user = JSON.parse(localStorage.getItem("user"));

            if (!user) {
                ElMessage.warning("Please login first.");
                return;
            }

            try {

                await api.post("/cart", {
                    userId: user.userId,
                    productId: product.productId,
                    quantity: 1
                });

                ElMessage.success("Added to cart!");

            } catch (error) {

                console.error(error);

                ElMessage.error("Failed to add item.");

            }

        }

    }

});