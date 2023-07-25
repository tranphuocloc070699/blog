<template>
  <ClientOnly>
    <div class="bg-slate-50 h-full pb-10">
      <div class="max-w-7xl w-full px-4 mx-auto">
        <h3
          class="pt-8 uppercase text-xl text-center text-blue_739 font-medium"
        >
          Create story
        </h3>

        <InputValidate
          label="Title"
          name="title"
          placeholder="Title"
          v-model="dto.title"
          @handleChange="v$.title.$touch"
          @onResetError="v$.title.$reset"
          inputClass="bg-white "
          :errors="v$.title.$errors"
        />

        <InputValidate
          label="Slug(Leave it empty if you want slug like title)"
          name="slug"
          placeholder="slug"
          v-model="dto.slug"
        />

        <div class="flex items-end">
          <InputValidate
          label="Thumbnail"
          name="thumbnail"
          placeholder="Thumbnail"
          type="file"
          @handleChange="
            (e) => {
              thumbnailHandler(e);
            }
          "
          :errors="v$.thumbnail.$errors"
        />
        <img class="ml-8 mb-4 w-16 h-16 object-cover rounded-lg" :src="thumbnailTemporaryUrl" />
        </div>
     
        <InputValidate
          label="Pre Content"
          name="pre_content"
          placeholder="Pre content"
          v-model="dto.pre_content"
          @handleChange="v$.pre_content.$touch"
          @onResetError="v$.pre_content.$reset"
          inputClass="bg-white"
          :errors="v$.pre_content.$errors"
        />

        <div class="mt-4">
          <p class="font-normal mb-2">Content</p>
          <Editor
            v-model="dto.content"
            ref="editor"
           
            :init="{
            height: 500,
            menubar: false,
            images_upload_handler: uploadImageHandle,
            plugins: ['codesample', 'anchor', 'link', 'help', 'table', 'image'],
            toolbar:
              'undo redo | styles | \
            alignleft aligncenter alignright alignjustify | \
            outdent indent | codesample| link | table | image | \
            help | custom-remove-image | customTOC',
            setup: (editor : TinyMCEEditor) => {
              editor.ui.registry.addButton('custom-remove-image', {
                text: 'Remove Image',
                onAction: (api) => {
                  removeImageHandler(editor)
                  api.isEnabled = () => false
                }
              });
              editor.ui.registry.addButton('customTOC', {
                text: 'TOC',
                onAction: () => {
                  customTOCHandler(editor)
                },
              });
              
            },
          }"
          />
        </div>

        <div class="text-end mt-8">
          <button class="outline-none border-none px-4 py-2 rounded-sm text-gray-800 bg-gray-200 mr-6"
          @click="handleBack"
          >Back</button>
          <button
            class="outline-none border-none px-4 py-2 rounded-sm text-white bg-[#374151]"
            @click="submitForm"
          >
          <svg v-show="adminStore.loading" aria-hidden="true" role="status" class="inline mr-3 w-4 h-4 text-white animate-spin" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
    <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="#E5E7EB"></path>
    <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentColor"></path>
    </svg>
       
            {{ adminStore.loading ? 'Loading...'  :  'Submit' }}
          </button>
        </div>
      </div>
    </div>
 
      <notifications />
 
  </ClientOnly>
</template>

<script setup lang="ts">
import {
  ICreateStoryInput,
  IImageResponse,
  ITocItem,
} from "~/static/types/story.type";
import { useVuelidate } from "@vuelidate/core";
import { createStoryRule } from "~/utils/validate/story.rule";
import { useAdminStore } from "~/stores/admin.store";
import { ref } from "vue";
import { Editor as TinyMCEEditor } from "tinymce";
import { convertedSentence, convertObjectToFormData } from "~/utils/converter";
import {SAVE_STORY_WHEN_UNMOUNTED_ID,
  SAVE_TEMPORARY_THUMBNAIL_STORY_WHEN_UNMOUNTED_ID} from '~/utils/constants';
  import { useNotification } from "@kyvg/vue3-notification";
definePageMeta({
  layout: "default",
  middleware:'private-route'
});
const { notify } = useNotification();
const adminStore = useAdminStore();


const dto = reactive<ICreateStoryInput>({
  title: "",
  slug: "",
  content: "",
  pre_content:"",
  toc: [],
  thumbnail: null,
});

const thumbnailTemporaryUrl = ref<string>('');
const saveBeforeUnmounted = ref<boolean>(true);

const v$ = useVuelidate(createStoryRule, dto);
const router = useRouter();
onMounted(() =>{
  if(localStorage.getItem(SAVE_STORY_WHEN_UNMOUNTED_ID)){
    const dtoFromLocalStorage : ICreateStoryInput = JSON.parse(localStorage.getItem(SAVE_STORY_WHEN_UNMOUNTED_ID)!);
    dto.title= dtoFromLocalStorage.title
    dto.slug= dtoFromLocalStorage.slug
    dto.content= dtoFromLocalStorage.content
    dto.pre_content= dtoFromLocalStorage.pre_content
    dto.toc= dtoFromLocalStorage.toc
    dto.thumbnail= dtoFromLocalStorage.thumbnail
  }
  if(localStorage.getItem(SAVE_TEMPORARY_THUMBNAIL_STORY_WHEN_UNMOUNTED_ID)){
    thumbnailTemporaryUrl.value = localStorage.getItem(SAVE_TEMPORARY_THUMBNAIL_STORY_WHEN_UNMOUNTED_ID)!;
  }
  window.addEventListener('beforeunload', (event) => {
  event.preventDefault();
  console.log('onBeforeUnmount')
  alert('hello')
  saveDataToLocalStorage();
});
})

const thumbnailHandler = async (e: any) => {
  v$.value.thumbnail.$touch;
  const file = e.target["files"][0];
  
  if (file) {
    dto.thumbnail = file;
    thumbnailTemporaryUrl.value = window.URL.createObjectURL(file)
    console.log(thumbnailTemporaryUrl.value)
  }
};
const removeDataFromLocalStorage = () =>{
  if(localStorage.getItem(SAVE_STORY_WHEN_UNMOUNTED_ID)){
    localStorage.removeItem(SAVE_STORY_WHEN_UNMOUNTED_ID)
  }
  if(localStorage.getItem(SAVE_TEMPORARY_THUMBNAIL_STORY_WHEN_UNMOUNTED_ID)){
    localStorage.removeItem(SAVE_TEMPORARY_THUMBNAIL_STORY_WHEN_UNMOUNTED_ID)
  }
}

const saveDataToLocalStorage = () =>{
  if(saveBeforeUnmounted.value){
    localStorage.setItem(SAVE_STORY_WHEN_UNMOUNTED_ID,JSON.stringify(dto))
    localStorage.setItem(SAVE_TEMPORARY_THUMBNAIL_STORY_WHEN_UNMOUNTED_ID,thumbnailTemporaryUrl.value);
  }
}

const uploadImageHandle = async (blobInfo: any, _: any) => {
  const formData = new FormData();
  formData.append("file", blobInfo.blob(), blobInfo.filename());

  return await adminStore
    .uploadImage(formData)
    .then((data) => {
      const imageObject = data as IImageResponse;
      return imageObject.data.url;
    })
    .catch((err) => {
 
      console.log("[client] uploadImageHandle", err);
    });
};

const removeImageHandler = (editor: TinyMCEEditor) => {
  if (editor.selection.getNode().nodeName === "IMG") {
    const selectedNode = editor.selection.getNode();
    const src = selectedNode.getAttribute("src") || "";
    adminStore
      .deleteImage(src)
      .then((data) => {
        console.log("delete image", data);
        editor.dom.remove(selectedNode);
      })
      .catch((error) => {
        alert("[remove-image] error");
        console.log({ error });
      });
  }
};

const customTOCHandler = (editor: TinyMCEEditor) => {
  dto.toc = [];
  let tocTemp: ITocItem[] = [];
  const headings: HTMLElement[] = editor.dom.select("h1, h2, h3, h4, h5, h6");
  headings.forEach((heading, index) => {
    if (heading.id === "") {
      const headingText: string = heading.innerText;
      const headingId = convertedSentence(headingText);
      heading.setAttribute("id", headingId);
      tocTemp.push({
        title: headingText,
        link: headingId,
        type: heading.nodeName,
      });
    }
  });
  console.log({ tocTemp });
  notify({
    type:'info',
    text:'create toc successfully!,check console'
  })
  dto.toc = tocTemp;
};

const submitForm = async () => {
  if (dto.slug.trim().length === 0) {
    dto.slug = convertedSentence(dto.title);
  }
  v$.value.$validate();
  if (!v$.value.$error) {
    removeDataFromLocalStorage();
    saveBeforeUnmounted.value = false;
    const formData = convertObjectToFormData(dto);
    adminStore
      .createStory(formData)
      .then((data) => {
        console.log({ data });
        notify({
          type:'success',
          title:data.status,
          text:data.message
        })
        router.push('/admin')
      })
      .catch((error) => {
        notify({
          type:'danger',
          title:'error',
        })
        console.log({ error });
      });
  }
};

const handleBack = () =>{
  removeDataFromLocalStorage();
  saveBeforeUnmounted.value = false;
  router.push('/admin')
}

</script>

<style scoped></style>
