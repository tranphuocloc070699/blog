<template>
  <div class="flex items-start">
    
    <Sidebar/>
     
      <AdminCardTable v-if="!pending && stories.length>0" :content="stories"/>

  </div>
</template>

<script setup lang="ts">
import { useAdminStore } from '~/stores/admin.store';
import { IGetStoryPagination, IStoryPaginationResponse } from '~/static/types/story.type';
const adminStore = useAdminStore()
definePageMeta({
  middleware:'private-route'
})

const stories = computed(() =>{
  return adminStore.stories
})



const pagination = ref<IGetStoryPagination>({
  created_at: "desc",
  items_per_page: 10,
  current_page: 0,
});

const { data,error,pending } = useFetch<IStoryPaginationResponse>("/api/story/pagination", {
  method: "POST",
  body: pagination,
});

onMounted( () =>{
  if(error.value){
    console.log({error:error.value})
  }
  if(data.value?.data.content){
    console.log(data.value?.data.content)
    adminStore.stories = data.value?.data.content
  }
})
</script>

<style scoped></style>
