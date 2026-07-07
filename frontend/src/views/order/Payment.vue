<template>

    <div class="payment-page">
        <h1>💳 Payment</h1>
        <div class="payment-grid">
            <div class="left">
                <h2>Payment Details</h2>
                <el-input v-model="card.name"   placeholder="Card Holder Name" />
                <el-input v-model="card.number" placeholder="Card Number" />
                <div class="row">
                    <el-input v-model="card.expiry" placeholder="MM/YY" />
                    <el-input v-model="card.cvv"    placeholder="CVV" />
                </div>

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
                <p><strong>Address:</strong> {{ checkout.shipping.address }}, {{ checkout.shipping.city }}</p>
                <p><strong>Phone:</strong> {{ checkout.shipping.phone }}</p>
                <p><strong>Payment:</strong> {{ paymentName }}</p>
                <hr>
                <div v-for="item in checkout.cart" :key="item.cartId" class="item">
                    <span>{{ item.productName }} x{{ item.quantity }}</span>
                    <span>${{ item.price * item.quantity }}</span>
                </div>
                <hr>
                <h2>Total: ${{ checkout.total }}</h2>
            </div>
        </div>
    </div>

</template>

<script setup>
    import api from "@/api/axios";
    import { ref, computed } from "vue";
    import { useRouter } from "vue-router";

    const router = useRouter();

    const checkout = ref(
        JSON.parse(sessionStorage.getItem("checkoutData"))
    );

    // Get logged-in user from localStorage
    const user = JSON.parse(localStorage.getItem("user"));

    const card = ref({ name: "", number: "", expiry: "", cvv: "" });
    const loading = ref(false);

    const paymentName = computed(() => {
        switch (checkout.value.shipping.payment) {
            case "card":    return "Credit Card";
            case "paypal":  return "PayPal";
            default:        return "Cash on Delivery";
        }
    });

    const payNow = async () => {

        if (checkout.value.shipping.payment === "card") {
            if (!card.value.name || !card.value.number ||
                !card.value.expiry || !card.value.cvv) {
                alert("Please complete card information.");
                return;
            }
        }

        loading.value = true;

        try {
            // Build the full address string from shipping form fields
            const fullAddress =
                checkout.value.shipping.address + ", " +
                checkout.value.shipping.city;

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
            alert("Checkout failed. Please try again.");
        }

        loading.value = false;
    };
</script>

<style scoped>

    .payment-page {
        padding: 50px;
        background: #f5fff7;
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