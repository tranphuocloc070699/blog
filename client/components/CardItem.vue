<template>
  <a
    class="flex flex-col justify-between border border-df rounded cursor-pointer hover:shadow transition-shadow"
    :href="`/story/${slug}`"
  >
    <div>
      <img
        :src="thumbnail"
        alt="Image"
        class="w-full h-48 object-cover rounded-t-sm"
      />
      <div class="p-4">
        <h3 class="text-xl mb-1">{{ title }}</h3>
        <h4 class="text-sm text-gray-500">
          {{ preContent }}
        </h4>
      </div>
    </div>

    <div class="flex items-center justify-between p-4">
      <h4 class="text-sm text-gray-500">{{ date }}</h4>
      <div class="w-20 flex justify-between">
        <span class="text-xs flex items-end">
    
          <Icon
            v-if="isUpVoted"
            name="ph:heart-fill"
            color="#557a95"
            class="text-2xl"
          />

          <Icon v-else name="ph:heart-thin" class="text-2xl text-gray-500 mr-[2px]" />
          {{ upVotes?.length }}
        </span>
        <span class="text-xs flex items-end">
          <Icon name="iconamoon:comment-thin" class="text-2xl text-gray-500 mr-[2px]" />
          {{ comments }}
        </span>
      </div>
    </div>
  </a>
</template>

<script setup lang="ts">
import { useAuthStore } from "~/stores/auth.store";
import { formatDate } from "~/utils/converter";

const props = defineProps({
  id: Number,
  thumbnail: String,
  title: String,
  preContent: String,
  slug: String,
  createdAt: String,
  comments: Number,
  upVotes: Array<number>,
});

const isUpVoted = ref<boolean>(false)

const date = computed(() => {
  if (props.createdAt) return formatDate(`${props.createdAt}`);
});
const authStore = useAuthStore();

onMounted(() =>{
  if (props.upVotes && props.upVotes.length > 0) {
    if (authStore.auth.logged) {

      if (props.upVotes.includes(authStore.auth.user.id)) isUpVoted.value = true;
    } else {
      if (props.upVotes.includes(authStore.auth.temporaryId)) isUpVoted.value = true
    }
  }


})

</script>

<style scoped></style>
