<template>
  <div>
    <div class="mt-8 mx-auto max-w-7xl w-full grid gap-8 px-4 grid-auto-fill">
      <CardItem
        v-for="(item, index) in data?.data.content"
        :key="index"
        :id="Number(item.id)"
        :title="`${item.title}`"
        :slug="`${item.slug}`"
        :pre-content="`${item.preContent}`"
        :thumbnail="`${item.thumbnail}`"
        :created-at="`${item.createdAt}`"
        :comments="item.comments && item.comments.length || 0"
        :up-votes="item.upvote"
      />
    </div>
  </div>
</template>

<script setup lang="ts">
import {
  IGetStoryPagination,
  IStoryPaginationResponse,
} from "~/static/types/story.type";

definePageMeta({
  layout: "default",
});

const pagination = ref<IGetStoryPagination>({
  created_at: "desc",
  items_per_page: 10,
  current_page: 0,
});

const { data, error } = useFetch<IStoryPaginationResponse>(
  "/api/story/pagination",
  {
    method: "POST",
    body: pagination,
  }
);

onMounted(() => {
  if (error.value) {
    console.log({ error: error.value });
  }
});
</script>

<style lang="scss" scoped></style>
