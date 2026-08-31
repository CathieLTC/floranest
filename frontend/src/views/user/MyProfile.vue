<template>
  <div class="profile-page">
    <section class="profile-header">
      <div class="profile-left">
        <div class="profile-photo">
          <img :src="profileImage" class="profile-image"/>
          <input ref="fileInput" type="file" accept="image/*" class="hidden-input" @change="changePhoto"/>
          <el-button type="success" plain @click="fileInput.click()">Change Photo</el-button>
        </div>
        <div>
          <h1>{{ user.fullName }}</h1>
          <p>{{ user.email }}</p>
        </div>
      </div>
    </section>
        <!-- =========================
              DASHBOARD CARDS
        ========================= -->
    <section class="dashboard">
      <div class="card">
        <h2>{{stats.orders}}</h2>
        <p>📦 Orders</p>
      </div>

      <div class="card">
        <h2>{{stats.wishlist}}</h2>
        <p>❤️ Wishlist</p>
      </div>

      <div class="card">
        <h2>{{stats.cart}}</h2>
        <p>🛒 Cart Items</p>
      </div>

      <div class="card">
        <h2>{{stats.plants}}</h2>
        <p>🌿 Plants Bought</p>
      </div>
    </section>
        <!-- =========================
              PERSONAL INFORMATION
        ========================= -->
    <section class="section">
      <h2>👤 Personal Information</h2>
      <div class="form-grid">
        <el-input v-model="user.fullName" placeholder="Full Name"/>
        <el-input v-model="user.email" placeholder="Email"/>
        <el-input v-model="user.phone" placeholder="Phone"/>
        <el-input v-model="user.country" placeholder="Country"/>
        <el-input v-model="user.city" placeholder="City"/>
        <el-input v-model="user.address" placeholder="Address"/>
      </div>
      <el-button type="success" :loading="profileSaving" @click="saveProfile">Save Changes</el-button>
    </section>
      <!-- =========================
              ADDRESS BOOK
        ========================= -->
    <section class="section">
      <div class="section-header">
        <h2>📍 Shipping Addresses</h2>
        <el-button type="success" plain @click="openAddAddress">Add New Address</el-button>
      </div>
      <div class="address-card" v-for="(address,index) in addresses" :key="index">
        <h3>{{address.title}}</h3>
        <p>{{address.address}}</p>
        <p>{{address.city}},{{address.country}}</p>

        <div class="address-buttons">
          <el-button size="small" @click="openEditAddress(index)">Edit</el-button>
          <el-button type="danger" size="small" @click="deleteAddress(index)">Delete</el-button>
        </div>
      </div>
      <!-- ADDRESS FORM -->
      <div v-if="showAddressForm" class="edit-box">
        <h3>{{editingIndex === null ?"Add New Address":"Edit Address"}}</h3>
        <el-input v-model="addressForm.title" placeholder="Address Name"/>
        <el-input v-model="addressForm.address" placeholder="Street Address"/>
        <el-input v-model="addressForm.city" placeholder="City"/>
        <el-input v-model="addressForm.country" placeholder="Country"/>
        <el-button type="success" @click="saveAddress">Save Address</el-button>
        <el-button @click="showAddressForm=false">Cancel</el-button>
      </div>
    </section>

      <!-- =========================
            PAYMENT METHODS
      ========================= -->
    <section class="section">
    <div class="section-header">
      <h2>💳 Payment Methods</h2>
      <el-button type="success" plain @click="openAddCard"> Add Card</el-button>
    </div>
    <div class="payment-card" v-for="(card,index) in cards" :key="index">
    <div class="payment-info">
      <h3>💳 {{card.type}}</h3>
      <p>**** **** **** {{card.number}}</p>
      <p>Expiry: {{card.expiry}}</p>
    </div>



    <el-button
    type="danger"
    size="small"
    @click="removeCard(index)"
    >
    Remove
    </el-button>



    </div>





    <!-- ADD CARD FORM -->


    <div
    v-if="showCardForm"
    class="edit-box"
    >


    <h3>
    Add New Card
    </h3>



    <el-input
    v-model="cardForm.type"
    placeholder="Card Type (Visa/Mastercard)"
    />



    <el-input
    v-model="cardForm.number"
    placeholder="Card Number"
    />



    <el-input
    v-model="cardForm.expiry"
    placeholder="Expiry Date"
    />



    <el-button
    type="success"
    @click="saveCard"
    >
    Save Card
    </el-button>



    <el-button
    @click="showCardForm=false"
    >
    Cancel
    </el-button>



    </div>



    </section>

    <!-- =========================
              WISHLIST
    ========================= -->
    <section class="section">


    <div class="section-header">


    <h2>❤️ Wishlist</h2>


    <router-link to="/wishlist">

    <el-button type="success">

    View Wishlist

    </el-button>

    </router-link>



    </div>





    <div class="wishlist-grid">



    <div
    class="wishlist-card"
    v-for="plant in wishlist"
    :key="plant.name"
    >


    <img
    :src="plant.image"
    />



    <h3>
    {{plant.name}}
    </h3>



    </div>



    </div>


    </section>

    <!-- =========================
          NOTIFICATIONS
    ========================= -->
    <section class="section">


    <h2>
    🔔 Notification Settings
    </h2>




    <div class="setting">


    <span>
    Email Notifications
    </span>


    <el-switch
    v-model="notifications.email"
    />


    </div>





    <div class="setting">


    <span>
    Order Updates
    </span>


    <el-switch
    v-model="notifications.orders"
    />


    </div>





    <div class="setting">


    <span>
    Promotional Offers
    </span>


    <el-switch
    v-model="notifications.promotions"
    />


    </div>



    </section>
    <!-- =========================
          RECENT ORDERS
    ========================= -->
    <section class="section">


    <div class="section-header">


    <h2>
    📦 Recent Orders
    </h2>



    <router-link to="/orders">

    <el-button type="success">
    View All
    </el-button>

    </router-link>


    </div>






    <table class="orders-table">


    <thead>

    <tr>

    <th>
    Order ID
    </th>


    <th>
    Status
    </th>


    <th>
    Total
    </th>


    </tr>

    </thead>



    <tbody>



    <tr
    v-for="order in recentOrders"
    :key="order.id"
    >


    <td>
    #{{order.id}}
    </td>



    <td>


    <span
    class="status"
    :class="order.status.toLowerCase()"
    >

    {{order.status}}

    </span>


    </td>



    <td>
    ${{order.total}}
    </td>



    </tr>


    </tbody>



    </table>


    </section>
    <!-- =========================
          PASSWORD
    ========================= -->
    <section class="section">


    <h2>
    🔒 Change Password
    </h2>




    <div class="form-grid">


    <el-input
    v-model="password.current"
    type="password"
    placeholder="Current Password"
    show-password
    />



    <el-input
    v-model="password.new"
    type="password"
    placeholder="New Password"
    show-password
    />



    <el-input
    v-model="password.confirm"
    type="password"
    placeholder="Confirm Password"
    show-password
    />



    </div>




    <el-button
    type="warning"
    @click="updatePassword"
    >
    Update Password
    </el-button>



    </section>
    <!-- =========================
              LOGOUT
    ========================= -->
    <section class="logout">


    <el-button
    type="danger"
    size="large"
    @click="logout"
    >
    Logout
    </el-button>


    </section>
  </div>
</template>
<script setup>

import { ref, onMounted } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import api from "@/api/axios";
import { useUserStore } from "@/stores/user";

const router = useRouter();
const userStore = useUserStore();
const loggedInUser = userStore.user;

if (!loggedInUser) {
  router.push("/login");
}

/* =========================
      PROFILE IMAGE
========================= */
const fileInput = ref(null);
const profileImage = ref(
  "https://i.pravatar.cc/200"
);
onMounted(()=>{

const savedImage =
localStorage.getItem("profileImage");


if(savedImage){

profileImage.value = savedImage;

}

loadProfile();

});




const changePhoto=(event)=>{


const file =
event.target.files[0];


if(!file) return;



const reader = new FileReader();



reader.onload=()=>{


profileImage.value =
reader.result;



localStorage.setItem(
"profileImage",
reader.result
);



ElMessage.success(
"Profile picture updated"
);



};



reader.readAsDataURL(file);



};









/* =========================
        USER INFO
========================= */


const user = ref({

userId: null,

fullName:"",

email:"",

phone:"",

country:"",

city:"",

address:""

});

const profileLoading = ref(false);
const profileSaving = ref(false);

const loadProfile = async () => {

  if (!loggedInUser) return;

  profileLoading.value = true;

  try {

    const response = await api.get(`/users/${loggedInUser.userId}`);

    user.value = {
      userId: response.data.userId,
      fullName: response.data.fullName || "",
      email: response.data.email || "",
      phone: response.data.phone || "",
      country: response.data.country || "",
      city: response.data.city || "",
      address: response.data.address || ""
    };

  } catch (error) {

    console.error(error);
    ElMessage.error("Failed to load your profile.");

  } finally {

    profileLoading.value = false;

  }

};

const saveProfile = async () => {

  if (!loggedInUser) return;

  if (!user.value.fullName || !user.value.email) {
    ElMessage.warning("Full name and email are required.");
    return;
  }

  profileSaving.value = true;

  try {

    await api.put(`/users/${loggedInUser.userId}`, {
      fullName: user.value.fullName,
      email: user.value.email,
      phone: user.value.phone,
      country: user.value.country,
      city: user.value.city,
      address: user.value.address
    });

    ElMessage.success("Profile saved successfully");

  } catch (error) {

    console.error(error);
    ElMessage.error("Failed to save your profile.");

  } finally {

    profileSaving.value = false;

  }

};








/* =========================
       DASHBOARD
========================= */


const stats = ref({

orders:12,

wishlist:8,

cart:3,

plants:21

});








/* =========================
       ADDRESS SYSTEM
========================= */


const addresses = ref([


{

title:"Home",

address:"123 Green Street",

city:"Accra",

country:"Ghana"

}


]);




const showAddressForm =
ref(false);



const editingIndex =
ref(null);




const addressForm = ref({

title:"",

address:"",

city:"",

country:""

});






const openAddAddress=()=>{


editingIndex.value=null;



addressForm.value={

title:"",

address:"",

city:"",

country:""

};



showAddressForm.value=true;



};








const openEditAddress=(index)=>{


editingIndex.value=index;



addressForm.value={

...addresses.value[index]

};



showAddressForm.value=true;



};







const saveAddress=()=>{


if(editingIndex.value===null){


addresses.value.push({

...addressForm.value

});


}

else{


addresses.value[editingIndex.value]={

...addressForm.value

};


}




showAddressForm.value=false;



ElMessage.success(
"Address saved"
);



};






const deleteAddress=(index)=>{


addresses.value.splice(index,1);



ElMessage.success(
"Address deleted"
);



};









/* =========================
       PAYMENT CARDS
========================= */


const cards = ref([


{

type:"Visa",

number:"1234",

expiry:"12/28"

}


]);





const showCardForm =
ref(false);





const cardForm = ref({

type:"",

number:"",

expiry:""

});





const openAddCard=()=>{


cardForm.value={

type:"",

number:"",

expiry:""

};


showCardForm.value=true;


};






const saveCard=()=>{


cards.value.push({

...cardForm.value

});



showCardForm.value=false;



ElMessage.success(
"Card added"
);



};






const removeCard=(index)=>{


cards.value.splice(index,1);



ElMessage.success(
"Card removed"
);



};









/* =========================
       WISHLIST
========================= */


const wishlist = ref([


{

name:"Snake Plant",

image:
"https://images.unsplash.com/photo-1593691509543-c55fb32e5cee?w=300"

},



{

name:"Monstera",

image:
"https://images.unsplash.com/photo-1614594575908-9e1f2e2c5e7e?w=300"

}



]);









/* =========================
       NOTIFICATIONS
========================= */


const notifications = ref({

email:true,

orders:true,

promotions:false

});









/* =========================
       ORDERS
========================= */


const recentOrders = ref([


{

id:1001,

status:"Delivered",

total:65

},



{

id:1002,

status:"Processing",

total:40

},



{

id:1003,

status:"Shipped",

total:80

}


]);









/* =========================
       PASSWORD
========================= */


const password = ref({

current:"",

new:"",

confirm:""

});


const updatePassword = async () => {

  if (!password.value.current ||
      !password.value.new ||
      !password.value.confirm) {

    ElMessage.warning("Please complete all password fields.");
    return;

  }

  if (password.value.new !== password.value.confirm) {

    ElMessage.error("Passwords do not match.");
    return;

  }

  try {

    const response = await api.put(
      `/users/${loggedInUser.userId}/password`,
      {
        currentPassword: password.value.current,
        newPassword: password.value.new
      }
    );

    ElMessage.success(response.data);

    password.value = {
      current: "",
      new: "",
      confirm: ""
    };

  } catch (error) {

    console.error(error);

    ElMessage.error(
      error.response?.data || "Failed to update password."
    );

  }

};

/* =========================
          LOGOUT
========================= */


const logout=()=>{


localStorage.removeItem(
"loggedUser"
);



ElMessage.warning(
"Logged out"
);



};


</script>
<style scoped>

/* =========================
      PAGE
========================= */

.profile-page{

background:#f5fff7;

min-height:100vh;

padding:40px;

}




/* =========================
      PROFILE HEADER
========================= */


.profile-header{

background:white;

padding:30px;

border-radius:20px;

box-shadow:0 5px 15px rgba(0,0,0,.08);

margin-bottom:30px;

}



.profile-left{

display:flex;

align-items:center;

gap:30px;

}



.profile-photo{

display:flex;

flex-direction:column;

align-items:center;

gap:15px;

}



.profile-image{

width:140px;

height:140px;

border-radius:50%;

object-fit:cover;

border:5px solid #2E7D32;

}



.hidden-input{

display:none;

}




.profile-left h1{

color:#2E7D32;

margin-bottom:10px;

}



.profile-left p{

color:#666;

}





/* =========================
      DASHBOARD
========================= */


.dashboard{

display:grid;

grid-template-columns:
repeat(auto-fit,minmax(200px,1fr));

gap:20px;

margin-bottom:30px;

}



.card{

background:white;

padding:25px;

border-radius:15px;

text-align:center;

box-shadow:
0 5px 15px rgba(0,0,0,.08);

transition:.3s;

}



.card:hover{

transform:translateY(-5px);

}



.card h2{

font-size:32px;

color:#2E7D32;

margin:0;

}



.card p{

color:#666;

}







/* =========================
      SECTIONS
========================= */


.section{

background:white;

padding:30px;

border-radius:20px;

box-shadow:
0 5px 15px rgba(0,0,0,.08);

margin-bottom:30px;

}



.section h2{

color:#2E7D32;

margin-bottom:20px;

}





.section-header{

display:flex;

justify-content:space-between;

align-items:center;

margin-bottom:20px;

}








/* =========================
       FORM
========================= */


.form-grid{

display:grid;

grid-template-columns:
repeat(auto-fit,minmax(250px,1fr));

gap:15px;

margin-bottom:20px;

}






/* =========================
      ADDRESS
========================= */


.address-card{

background:#f9fff9;

padding:20px;

border-radius:15px;

border-left:5px solid #2E7D32;

margin-bottom:15px;

}



.address-card h3{

color:#2E7D32;

}



.address-buttons{

display:flex;

gap:10px;

margin-top:15px;

}




.edit-box{

margin-top:25px;

padding:25px;

background:#f5fff7;

border-radius:15px;

}



.edit-box .el-input{

margin-bottom:15px;

}








/* =========================
       PAYMENT
========================= */


.payment-card{

display:flex;

justify-content:space-between;

align-items:center;

background:#f9f9f9;

padding:20px;

border-radius:15px;

margin-bottom:15px;

}



.payment-info h3{

color:#2E7D32;

}









/* =========================
        WISHLIST
========================= */


.wishlist-grid{

display:grid;

grid-template-columns:
repeat(auto-fit,minmax(180px,1fr));

gap:20px;

}



.wishlist-card{

background:#fafafa;

border-radius:15px;

overflow:hidden;

text-align:center;

}



.wishlist-card img{

width:100%;

height:170px;

object-fit:cover;

}



.wishlist-card h3{

color:#2E7D32;

padding:15px;

}








/* =========================
       SETTINGS
========================= */


.setting{

display:flex;

justify-content:space-between;

align-items:center;

padding:15px 0;

border-bottom:1px solid #eee;

}






/* =========================
        ORDERS
========================= */


.orders-table{

width:100%;

border-collapse:collapse;

}



.orders-table th,
.orders-table td{

padding:15px;

border-bottom:1px solid #eee;

text-align:left;

}



.orders-table th{

background:#f5fff7;

}





.status{

padding:6px 15px;

border-radius:20px;

font-size:13px;

font-weight:bold;

}



.delivered{

background:#2E7D32;

color:white;

}



.processing{

background:#fbc02d;

color:#222;

}



.shipped{

background:#1976d2;

color:white;

}








/* =========================
        LOGOUT
========================= */


.logout{

text-align:center;

margin-bottom:30px;

}








/* =========================
       MOBILE
========================= */


@media(max-width:768px){


.profile-left{

flex-direction:column;

text-align:center;

}



.section-header{

flex-direction:column;

gap:15px;

align-items:flex-start;

}



.payment-card{

flex-direction:column;

gap:15px;

align-items:flex-start;

}



.orders-table{

display:block;

overflow-x:auto;

}



}

</style>
