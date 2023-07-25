<template>
    <div class="mb-4 rounded-lg border border-gray-100 p-4">
       <div class="flex items-center ">
        <img 
        class="h-6 w-6 rounded-full"
        src="https://img.redbull.com/images/c_crop,x_484,y_0,h_716,w_573/c_fill,w_400,h_540/q_auto:low,f_auto/redbullcom/2019/03/22/e8f08713-04ff-4d2a-87cd-33aeb9ed13a7/teemo-league-of-legends-champion"/>
        <h4 class="font-medium ml-2">{{props.name  }}</h4>
       </div>
       <p class="mt-1 text-gray-700 text-sm pl-8">{{ props.content }}</p>
       <p class="mt-1 text-gray-700 text-xs text-end">{{ dateFormater }}</p>
    </div>
</template>

<script setup lang="ts">
import { IComment } from '~/static/types/entity.type';
import {formatDate} from '~/utils/converter'
    const props = defineProps({
       content:String,
        createdAt:String,
        name:String

    })

    const dateFormater = computed(() =>{
        return formatDate(`${props.createdAt}`)
    })
</script>

<style scoped>

</style>