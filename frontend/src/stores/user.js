import { defineStore } from "pinia";

const ADMIN_SESSION_KEY = "fnAdminSession";

export const useUserStore = defineStore("user", {
    state: () => ({
        user: JSON.parse(localStorage.getItem("user")) || null,
        adminAuthed: localStorage.getItem(ADMIN_SESSION_KEY) === "true"
    }),

    actions: {
        login(user){
            this.user = user;
            localStorage.setItem("user", JSON.stringify(user));
        },

        logout(){
            this.user = null;
            localStorage.removeItem("user");
            this.revokeAdminAccess();
        },

        grantAdminAccess(){
            this.adminAuthed = true;
            localStorage.setItem(ADMIN_SESSION_KEY, "true");
        },

        revokeAdminAccess(){
            this.adminAuthed = false;
            localStorage.removeItem(ADMIN_SESSION_KEY);
        }
    }
});
