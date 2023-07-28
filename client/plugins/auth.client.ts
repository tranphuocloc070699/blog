
import { useAuthStore } from "~/stores/auth.store";

export default defineNuxtPlugin(async nuxtApp => {

    if(process.server) return;
    const auth = useAuthStore();
    await auth.authenticate();
})