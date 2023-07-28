// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({
  runtimeConfig: {
    BASE_URL_SERVER: process.env.BASE_URL_SERVER,
    BASE_URL_CLIENT: process.env.BASE_URL_CLIENT,
    COOKIE_NAME: process.env.COOKIE_NAME,
  },
  $meta: {
    lang: "en",
  },
  //   nitro: {
  //     devProxy: {
  //         "/api": {
  //             target:"http://localhost:4000",
  //             changeOrigin: true,
  //             prependPath: true,
  //         }
  //     }
  // },
  devtools: { enabled: false },
  app: {
    head: {
      title: "My Dev Blog",
      link: [
        {
          rel: "stylesheet",
          href: "https://cdnjs.cloudflare.com/ajax/libs/flowbite/1.7.0/flowbite.min.css",
        },
      ],
      // script:[{src:'_nuxt/assets/js/prism',type:'module'}]
    },
  },

  typescript: {
    strict: true,
  },
  modules: ["nuxt-icon", "@pinia/nuxt"],
  postcss: {
    plugins: {
      tailwindcss: {},
      autoprefixer: {},
    },
  },
  pinia: {
    autoImports: ["defineStore", ["defineStore", "definePiniaStore"]],
  },
  css: [
    "~/assets/css/tailwind.css",
    "~/assets/css/styles.css",
    "~/assets/css/prism.css",
  ],
  devServer: {
    port: 4567,
  },
});
