<template>
  <div class="mx-8 grow">
    <h2 class="text-center my-8 text-2xl">STORIES</h2>
    <div class="relative overflow-x-auto shadow-md sm:rounded-lg">
      <table
        class="text-center w-full text-sm text-gray-500 dark:text-gray-400"
      >
        <thead
          class="text-xs text-white uppercase bg-blue_739 dark:bg-gray-700 dark:text-gray-400"
        >
          <tr>
            <th scope="col" class="px-6 py-3 text-left">Title</th>
            <th scope="col" class="px-6 py-3">Thumbnail</th>
            <th scope="col" class="px-6 py-3">Upvotes</th>
            <th scope="col" class="px-6 py-3">Comments</th>
            <th scope="col" class="px-6 py-3 whitespace-nowrap">Created At</th>
            <th scope="col" class="px-6 py-3 whitespace-nowrap">Updated At</th>
            <th scope="col" class="px-6 py-3">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr
            class="bg-white border-b dark:bg-gray-900 dark:border-gray-700"
            v-for="story in content"
            :key="story.id"
          >
            <th
              scope="row"
              class="text-left px-6 py-4 font-medium text-gray-900 dark:text-white"
            >
              {{ story.title }}
            </th>
            <td class="px-6 py-4">
              <img class="w-10 h-10" :src="`${story.thumbnail}`" alt="" />
            </td>
            <td class="px-6 py-4">
              {{ (story.upvote && story.upvote.length) || 0 }}
            </td>
            <td class="px-6 py-4 hover:bg-slate-50 cursor-pointer" >
              <AdminCommentTable :data="story.comments && story.comments || []"/>
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              {{ dateFormater(`${story.createdAt}`) }}
            </td>
            <td class="px-6 py-4 whitespace-nowrap">
              {{ dateFormater(`${story.updatedAt}`) }}
            </td>
            <td class="px-6 py-4">
              <div class="flex justify-around items-center">
                <NuxtLink
                  :to="`/story/${story.slug}`"
                  class="mr-4 font-medium text-blue-600 dark:text-blue-500 hover:underline"
                  >SHOW</NuxtLink
                >
                <NuxtLink
                  :to="`/admin/update-story/${story.slug}`"
                  class="mr-4 font-medium text-yellow-600 hover:underline"
                  >UPDATE</NuxtLink
                >
                <p
                  :to="`/story/${story.slug}`"
                  class="cursor-pointer font-medium text-red-900 hover:underline"
                  @click="openModal(story)"
                >
                  DELETE
                </p>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <Modal :isOpen="controls.isOpen">
      <div class="w-96 flex flex-col h-56 justify-between">
        <p class="text-xl font-medium py-8 px-4">
          {{ `Do you really want to delete the post with title [ ${controls.title} ]` }}
        </p>
        <div class="flex justify-end">
          <button
            @click="closeModal"
            class="outline-none border-none font-medium bg-white py-2 px-4 rounded-md w-24 text-primary"
          >
            Cancel
          </button>
          <button
            @click="handleDeleteStory()"
            class="outline-none border-none bg-blue_557 py-2 px-4 rounded-md w-24 text-white"
          >
            Delete
          </button>

          
        </div>
      </div>
    </Modal>
    <ClientOnly>
      <notifications />
    </ClientOnly>
  </div>
</template>

<script setup lang="ts">
import { formatDate } from "~/utils/converter";
import { IStory } from "~/static/types/entity.type";
import { IErrorObject, IResponseObject } from "~/static/types/default.type";
import HttpStatusCode from "~/utils/httpStatusCode";
import { useNotification } from "@kyvg/vue3-notification";

const {notify} = useNotification();

const props = defineProps({
  content: Array<IStory>,
});

const content = ref(props.content);

const controls = ref({
  isOpen:false,
  slug:'',
  title:''
});


const dateFormater = (dateString: string) => {
  return formatDate(dateString);
};

const openModal = (story: IStory) => {
  controls.value.isOpen = true;
  controls.value.slug = story.slug;
  controls.value.title = story.title
};
const closeModal = () => {
  controls.value.isOpen  = false;
  controls.value.slug = '';
  controls.value.title = ''
};

const handleDeleteStory = () => {

  $api<IResponseObject>("/api/admin/delete-story", {
    method: "DELETE",
    body: controls.value.slug,
  })
  .then((data) => {
    if(data.status===HttpStatusCode.OK.name){
        content.value = content.value?.filter(item => item.slug!==controls.value.slug)
        notify({
          type:'success',
          title:data.status,
          text:data.message
        })
        closeModal()
      }
    })
    .catch((error : IErrorObject) => {
      notify({
        type:'danger',
        title:'error'
      })
      console.log({ error });
    });


};
</script>

<style scoped></style>
