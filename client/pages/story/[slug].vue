<template>
  <div class="bg-[#fff] pt-8">
    <div class="w-full max-w-5xl mx-auto" v-if="data">
      <div class="">
        <h1 class="text-3xl font-medium text-center text-gray_5d5">
          {{ data.data.story.title }}
        </h1>
        <Toc :toc="data.data.story.toc" class="" />

        <div
          v-html="data.data.story.content"
          class="mt-8 story-content__styles"
        ></div>
      </div>

      <div class="mt-8 p-4 rounded-lg border border-gray-200">
        <h2 class="mt-4 mb-8 font-semibold text-3xl text-gray_5d5">Comments</h2>

       <ClientOnly>
        <div v-if="!authStore.auth.logged">
          <InputValidate
            label="Nick name"
            name="name"
            type="text"
            placeholder="Your name"
            v-model="dto.name"
            @handleChange="v$.name.$touch"
            @onResetError="v$.name.$reset"
            :errors="v$.name.$errors"
            class="max-w-md"
          />
          <InputValidate
            label="Leave blank if you dont want to get notify in your email"
            name="email"
            type="email"
            placeholder="example@gmail.com"
            v-model="dto.email"
            @handleChange="v$.email.$touch"
            @onResetError="v$.email.$reset"
            :errors="v$.email.$errors"
            class="max-w-md"
          />
        </div>
        <InputValidate
          label="Content"
          name="content"
          type="textarea"
          placeholder="add your question"
          v-model="dto.content"
          @handleChange="v$.content.$touch"
          @onResetError="v$.content.$reset"
          :errors="v$.content.$errors"
          class="max-w-md"
        />

        <button
          @click="handleSendComment"
          class="outline-none border-none bg-blue_557 py-2 px-4 mt-4 rounded-md w-24 text-white"
        >
          SEND
        </button>
       </ClientOnly>

        <div v-show="data.data.story.comments.length > 0">
          <div class="h-[1px] w-full bg-gray-200 my-10"></div>

          <CommentItem
            v-for="(item, index) in data.data.story.comments"
            :key="index"
            :name="item.name"
            :content="item.content"
            :createdAt="`${item.createdAt}`"
          />
        </div>
        <ClientOnly>
          <UpVote v-if="!isUpVoted" @onUpVote="handleUpVote" />
        </ClientOnly>
      </div>
    </div>
    <ClientOnly>
      <notifications />
    </ClientOnly>
  </div>
</template>

<script setup lang="ts">
import useVuelidate from "@vuelidate/core";
import { createCommentRule } from "~/utils/validate/comment.rule";
import { ICreateCommentInput } from "~/static/types/comment.type";
import { IStoryResponse, IUpVoteInput } from "~/static/types/story.type";
import { ICommentResponse } from "~/static/types/comment.type";
import { useAuthStore } from "~/stores/auth.store";
import { useNotification } from "@kyvg/vue3-notification";
import { IResponseObject } from "~/static/types/default.type";
import HttpStatusCode from "~/utils/httpStatusCode";

const { notify } = useNotification();
const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const slug = route.params.slug;
const { data, error } = await useFetch<IStoryResponse>(`/api/story/${slug}`, {
  method: "GET",
});

const dto = reactive<ICreateCommentInput>({
  content: "",
  name: "",
  email: "",
  story_id: 0,
});

const v$ = useVuelidate(createCommentRule, dto);

const isUpVoted = computed((): boolean => {
  if (
    data.value?.data.story &&
    data.value?.data.story.upvote &&
    data.value?.data.story.upvote.length > 0
  ) {
    if (authStore.auth.logged) {
      if (data.value?.data.story.upvote.includes(authStore.auth.user.id))
        return true;
    } else {
      if (data.value?.data.story.upvote.includes(authStore.auth.temporaryId))
        return true;
    }
  }

  return false;
});

onMounted(async () => {
  if (error.value) {
    console.log({ error: error.value });
    router.push("/not-found");
  }
  if (data.value?.data.story.id) {
    dto.story_id = data.value.data.story.id;
    data.value.data.story.comments = data.value.data.story.comments.reverse();
  }
  if (authStore.auth.logged) {
    dto.name = authStore.auth.user.name;
    dto.email = authStore.auth.user.email;
  }
});

const handleSendComment = () => {
  console.log('123')
  v$.value.$validate();
  console.log(v$.value.$errors)
  if (!v$.value.$error) {
    $api<ICommentResponse>("/api/story/create-comment", {
      method: "POST",
      body: dto,
    })
      .then((response) => {
        (dto.content = "");
        data.value?.data.story.comments.unshift(response.data.comment);
        v$.value.$reset();
        notify({
          type:'success',
          title:response.status,
          text:response.message
        })
      })
      .catch((error) => console.log({ error }));
  }
};
const handleUpVote = () => {
  let dto: IUpVoteInput = {
    story_id: data.value!.data.story.id!,
    id: -1,
  };

  if (authStore.auth.logged) {
    dto.id = authStore.auth.user.id;
  } else {
    dto.id = authStore.auth.temporaryId;
  }
  $api<IResponseObject>("/api/story/create-upvote", {
    method: "POST",
    body: dto,
  })
    .then((response) => {
      if (response.status === HttpStatusCode.OK.name) {
        if (data.value?.data.story.upvote) {
          setTimeout(() => {
            data.value?.data.story.upvote.push(dto.id);
          }, 500);
        
        } else {
          const upvoteList: number[] = [dto.id];
          setTimeout(() => {
            data.value!.data.story.upvote = upvoteList;
          }, 500);
        }
        notify({
            title: response.status,
            text: response.message,
            type: "success",
          });
      }
    })
    .catch((error) => {
      notify({
            text: 'error',
            type: "danger",
          });
      console.log({ error });
    });
};
</script>

<style lang="scss">
.story-content__styles {
  pre {
    border-radius: 8px;
  }
  .code-toolbar {
    .toolbar {
      .toolbar-item {
        .copy-to-clipboard-button {
          padding: 8px 16px;
          font-size: 1rem;
          background-color: #557a95;
        }
      }
    }
  }

  h1 {
    display: block;
    font-size: 2em;
    margin-top: 0.67em;
    margin-bottom: 0.33em;
    margin-left: 0;
    margin-right: 0;
    font-weight: bold;
  }
  h2 {
    display: block;
    font-size: 1.5em;
    margin-top: 0.67em;
    margin-bottom: 0.33em;
    margin-left: 0;
    margin-right: 0;
    font-weight: bold;
  }
  h3 {
    display: block;
    font-size: 1.17em;
    margin-top: 0.67em;
    margin-bottom: 0.33em;
    margin-left: 0;
    margin-right: 0;
    font-weight: bold;
  }
  h4 {
    display: block;
    margin-top: 0.67em;
    margin-bottom: 0.33em;
    margin-left: 0;
    margin-right: 0;
    font-weight: bold;
  }
  h5 {
    display: block;
    font-size: 0.83em;
    margin-top: 0.67em;
    margin-bottom: 0.33em;
    margin-left: 0;
    margin-right: 0;
    font-weight: bold;
  }
  h6 {
    display: block;
    font-size: 0.67em;
    margin-top: 2.33em;
    margin-bottom: 2.33em;
    margin-left: 0;
    margin-right: 0;
    font-weight: bold;
  }
}
</style>
