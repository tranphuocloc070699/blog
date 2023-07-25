// import getCookie from "~/utils/getCookie";
import { useAuthStore } from "~/stores/auth.store";

export default defineNuxtRouteMiddleware(async (from,to) => {
  if(process.server) return;
  const authStore = useAuthStore()
    if(authStore.auth.user.role!=="ADMIN" && to.fullPath!=="/"){
       return navigateTo('/')
    }
  })