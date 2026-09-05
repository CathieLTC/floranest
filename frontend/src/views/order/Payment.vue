<template>

    <div class="payment-page">
        <h1>💳 Payment</h1>

        <template v-if="checkout">
            <div class="payment-grid">
                <div class="left">
                    <h2>Payment Details</h2>

                    <template v-if="checkout.shipping.payment === 'card'">
                        <el-input v-model="card.name"   placeholder="Card Holder Name" />
                        <el-input v-model="card.number" placeholder="Card Number" />
                        <div class="row">
                            <el-input v-model="card.expiry" placeholder="MM/YY" />
                            <el-input v-model="card.cvv"    placeholder="CVV" />
                        </div>
                    </template>

                    <p v-else class="cod-note">
                        Pay with
                        <strong>{{ paymentName }}</strong> — no card details needed.
                    </p>

                    <el-button
                        type="success"
                        class="pay-btn"
                        :loading="loading"
                        @click="payNow"
                    >
                        Pay Now
                    </el-button>
                </div>

                <div class="right">
                    <h2>Order Summary</h2>
                    <p><strong>Name:</strong> {{ checkout.shipping.name }}</p>
                    <p><strong>Address:</strong> {{ checkout.shipping.address }}, {{ checkout.shipping.city }}<span v-if="checkout.shipping.country">, {{ checkout.shipping.country }}</span></p>
                    <p><strong>Phone:</strong> {{ checkout.shipping.phone }}</p>
                    <p><strong>Payment:</strong> {{ paymentName }}</p>
                    <hr>
                    <div v-for="item in checkout.cart" :key="item.cartId" class="item">
                        <span>{{ item.productName }} x{{ item.quantity }}</span>
                        <span>${{ (item.price * item.quantity).toFixed(2) }}</span>
                    </div>
                    <hr>
                    <h2>Total: ${{ Number(checkout.total).toFixed(2) }}</h2>
                </div>
            </div>
        </template>

        <p v-else class="cod-note">
            No checkout details found. Redirecting to checkout…
        </p>
    </div>

</template>

<script setup>
    import api from "@/api/axios";
    import { ref, computed } from "vue";
    import { useRouter } from "vue-router";
    import { ElMessage } from "element-plus";
    import { useUserStore } from "@/stores/user";

    const router = useRouter();
    const userStore = useUserStore();
    const user = userStore.user;

    const checkoutData = JSON.parse(sessionStorage.getItem("checkoutData") || "null");
    const checkout = ref(checkoutData);

    if (!user) {
        router.replace("/login");
    } else if (!checkout.value) {
        ElMessage.warning("No checkout details found — please start again.");
        router.replace("/checkout");
    }

    const card = ref({ name: "", number: "", expiry: "", cvv: "" });
    const loading = ref(false);

    const paymentName = computed(() => {
        switch (checkout.value?.shipping?.payment) {
            case "card":    return "Credit Card";
            case "paypal":  return "PayPal";
            default:        return "Cash on Delivery";
        }
    });

    const payNow = async () => {

        if (loading.value || !checkout.value || !user) return;

        if (checkout.value.shipping.payment === "card") {
            if (!card.value.name || !card.value.number ||
                !card.value.expiry || !card.value.cvv) {
                ElMessage.warning("Please complete card information.");
                return;
            }
        }

        loading.value = true;

        try {
            // Build the full address string from shipping form fields
            const { shipping } = checkout.value;
            const fullAddress =
                [shipping.address, shipping.city, shipping.country]
                    .filter(Boolean)
                    .join(", ");

            // Send userId in path, address in request body
            const response = await api.post(
                `/orders/checkout/${user.userId}`,
                { address: fullAddress }
            );

            sessionStorage.setItem("order", JSON.stringify(response.data));
            sessionStorage.removeItem("checkoutData");
            router.push("/order-confirmation");

        } catch (error) {
            console.error(error);
            ElMessage.error("Checkout failed. Please try again.");
        } finally {
            loading.value = false;
        }
    };
</script>

<style scoped>

    .payment-page {
        padding: 50px;
        background: transparent;
        min-height: 100vh;
    }

    .payment-grid {
        display: grid;
        grid-template-columns: 1fr 1fr;
        gap: 30px;
    }

    .left, .right {
        background: white;
        padding: 25px;
        border-radius: 15px;
        box-shadow: 0 5px 15px rgba(0,0,0,.08);
    }

    .left .el-input { margin-bottom: 15px; }

    .row { display: flex; gap: 15px; }

    .cod-note {
        background: #f5fff7;
        border: 1px solid #d5ecd8;
        border-radius: 10px;
        padding: 15px 18px;
        color: #555;
    }

    .pay-btn { width: 100%; margin-top: 20px; }

    .item {
        display: flex;
        justify-content: space-between;
        margin: 10px 0;
    }

    @media (max-width: 768px) {
        .payment-grid { grid-template-columns: 1fr; }
    }

</style>