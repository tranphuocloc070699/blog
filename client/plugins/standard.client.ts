

import 'flowbite/dist/flowbite.min.js';
import   '~/assets/js/prism'
import Editor from '@tinymce/tinymce-vue';
import Notification from '@kyvg/vue3-notification'
export default defineNuxtPlugin(nuxtApp => {
        nuxtApp.vueApp.component('Editor',Editor);
        nuxtApp.vueApp.use(Notification)
 
})