<template>
  <div>
    <button
      @click="openModal"
      class="text-xs text-white outline-none border-none"
    >
      SIGN IN
    </button>
    <Modal :isOpen="isOpen">
      <div class="w-96">
        <div class="text-end">
          <Icon
            name="ph:x-fill"
            class="text-2xl text-gray-500 cursor-pointer"
            @click="closeModal"
          />
        </div>
        <h3
          class="mt-6 uppercase text-center text-gray_5d5 text-2xl font-semibold"
        >
          Sign In
        </h3>
        <h4 
        v-for="(error,index) in apiErrorObject.errors"
        :key="index"
        before="" class="mt-4 p-1 text-center text-red-500 relative
        before:content-[attr(before)] before:absolute before:inset-0 
        before:bg-current before:opacity-20 before:rounded-sm
        ">{{ error }}</h4>

        <form action="" class="my-4">
          <InputValidate
            label="Email"
            name="Email"
            placeholder="Your Email"
            v-model="formData.email"
            @handleChange="v$.email.$touch"
            @onResetError="v$.email.$reset"
            :errors="v$.email.$errors"
          />
          <InputValidate
            label="Password"
            name="Password"
            type="password"
            placeholder="Your password"
            v-model="formData.password"
            @handleChange="v$.password.$touch"
            @onResetError="v$.password.$reset"
            :errors="v$.password.$errors"
          />
        </form>

        <div class="text-end mt-10">
          <button
            @click="submitForm"
            class="outline-none border-none bg-blue_557 py-2 px-4 rounded-md w-24 text-white"
          >
            Sign In
          </button>
    
        </div>
      </div>
    </Modal>
    <ClientOnly>
      <notifications/>
    </ClientOnly>
  </div>
</template>

<script setup lang="ts">

import { useVuelidate } from "@vuelidate/core";
import { useAuthStore } from "~/stores/auth.store";
import {IErrorObject} from "~/static/types/default.type"
import {signInRule} from '~/utils/validate/auth.rule'
import {TEMPORARY_ID} from "~/utils/constants";
import HttpStatusCode from "~/utils/httpStatusCode";
import { useNotification } from "@kyvg/vue3-notification";
const store = useAuthStore();
const formData = reactive({
  email: "",
  password: "",
});

const apiErrorObject = ref<IErrorObject>({
  errors:[],
  timestamp:new Date,
    status:'',
    message:'',
    path:''
}); 
const v$ = useVuelidate(signInRule, formData);
const { notify } = useNotification();


const submitForm = async () => {
  v$.value.$validate();
  if (!v$.value.$error) {
    const response = await  store.signin({...formData})
   
    if(response.status===HttpStatusCode.OK.name){
      if(localStorage.getItem(TEMPORARY_ID)){
        localStorage.removeItem(TEMPORARY_ID);
      }
      closeModal();
      notify({
        title:response.status,
        text:response.message,
        type:'success'
      })
    }else{
      Object.assign(apiErrorObject.value,response as IErrorObject)
    }
  }
};

const isOpen = ref(false);
const openModal = () => {
  isOpen.value = true;
};
const closeModal = () => {
  v$.value.$reset();
  formData.email = "";
  formData.password = "";
  isOpen.value = false;
};
</script>

<style scoped></style>
