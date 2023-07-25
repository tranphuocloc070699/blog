import type { Config } from 'tailwindcss'

export default <Partial<Config>>{
  content: [
    "./components/**/*.{js,vue,ts}",
    "./layouts/**/*.vue",
    "./pages/**/*.vue",
    "./plugins/**/*.{js,ts}",
    "./nuxt.config.{js,ts}",
  ],
    theme: {
    extend: {
      colors:{
        primary:'#333',
        gray_5d5:'#5d5c61',
        green_379:'#379683',
        blue_739:'#7395ae',
        blue_557:'#557A95',
        beige_b1a:'#b1a296',
        df:'#DFDFDF',
        bg_primary:'#f5f5f7'
      },
      width:{
      }
    }
  }
}