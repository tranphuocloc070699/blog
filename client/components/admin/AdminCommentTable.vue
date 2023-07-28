<template>
  <div>
    <p @click="openModal">{{ comments.length }}</p>

    <Modal :is-open="isOpen" :custom-class="'w-[1248px]'">
      <div>
        <div class="text-end">
          <Icon
            name="ph:x-fill"
            class="text-2xl text-gray-500 cursor-pointer"
            @click="closeModal"
          />
        </div>
        <div class="shadow-md sm:rounded-lg">
          <table
            class="text-center w-full my-8 text-sm text-gray-500 dark:text-gray-400"
          >
            <thead
              class="text-xs text-white uppercase bg-blue_739 dark:bg-gray-700 dark:text-gray-400"
            >
              <tr>
                <th scope="col" class="px-6 py-3">ID</th>
                <th scope="col" class="px-6 py-3">Name</th>
                <th scope="col" class="px-6 py-3">Email</th>
                <th scope="col" class="px-6 py-3">Content</th>
                <th scope="col" class="px-6 py-3 whitespace-nowrap">
                  Created At
                </th>
                <th scope="col" class="px-6 py-3">Action</th>
              </tr>
            </thead>
            <tbody>
              <tr
                class="bg-white border-b dark:bg-gray-900 dark:border-gray-700"
                v-for="(item,index) in comments"
                :key="item.id"
              >
                <th
                  scope="row"
                  class="px-6 py-4 font-medium text-gray-900 dark:text-white"
                >
                  {{ item.id }}
                </th>
                <td class="px-6 py-4">
                  {{ item.name }}
                </td>
                <td class="px-6 py-4">
                  {{ item.email }}
                </td>
                <td class="px-6 py-4">
                  {{ item.content }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  {{ dateFormater(`${item.createdAt}`) }}
                </td>
                <td class="px-6 py-4">
                  <div class="flex justify-around items-center">
                    <p
                      class="cursor-pointer font-medium text-red-900 hover:underline"
                      @click="openConfirmDeleteModal(item.id,index)"
                    >
                      DELETE
                    </p>
                  </div>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </Modal>
    <Modal :is-open="confirmDelete.isOpen">
      <div class="w-96 flex flex-col h-56 justify-between">
        <p class="text-xl font-medium py-8 px-4">
          {{
            `Do you really want to delete the comment with ID [ ${confirmDelete.id} ]`
          }}
        </p>
        <div class="flex justify-end">
          <button
            @click="closeConfirmDeleteModal"
            class="outline-none border-none font-medium bg-white py-2 px-4 rounded-md w-24 text-primary"
          >
            Cancel
          </button>
          <button
            @click="handleDeleteComment"
            class="outline-none border-none bg-red-600 py-2 px-4 rounded-md w-24 text-white"
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
import { IResponseObject } from "static/types/default.type";
import { IComment, IStory } from "~/static/types/entity.type";
const isOpen = ref(false);
const confirmDelete = ref({
    isOpen:false,
    id:-1,
    index:-1
});
import { formatDate } from "~/utils/converter";
import { useNotification } from "@kyvg/vue3-notification";
import HttpStatusCode from "~/utils/httpStatusCode";
import { useAdminStore } from "~/stores/admin.store";
const { notify } = useNotification();
const adminStore = useAdminStore();

const props = defineProps({
  data: Array<IComment>,
});

const comments = ref<IComment[]>(props.data || [])
const openModal = () => {
  isOpen.value = true;
};
const closeModal = () => {
  isOpen.value = false;
};
const openConfirmDeleteModal = (id : number,index:number) => {
  confirmDelete.value.isOpen= true;
  confirmDelete.value.id = id
  confirmDelete.value.index = index
};
const closeConfirmDeleteModal = () => {
    confirmDelete.value.isOpen = false;
};
const dateFormater = (dateString: string) => {
  return formatDate(dateString);
}
const handleDeleteComment = () =>{
    $api<IResponseObject>('/api/admin/delete-comment',{
        method:'DELETE',
        body:`${confirmDelete.value.id}`
    }).then(data => {
        if(data.status===HttpStatusCode.OK.name){
            notify({
                type:'success',
                title:data.status,
                text:data.message
            })
            closeConfirmDeleteModal();
            comments.value = comments.value.filter(item => item.id!==confirmDelete.value.id)
        }
    }).catch(error => {
        notify({
            type:'danger',
            text:'error'
        })
        console.log({error})
    })
}



</script>

<style scoped></style>
