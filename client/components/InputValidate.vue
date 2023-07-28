<template>
  <div class="mb-4">
    <label :for="props.name" class="font-normal">{{ props.label }}</label>
   
    <textarea 
    v-if="props.type==='textarea'"
    :name="props.name"
      :type="props.type || 'text'"
      :placeholder="props.placeholder"
      :value="props.modelValue"
      autocomplete="off"
      @input="updateValue"
      @change="onChange"
      class="h-20 bg-opacity-20 w-full rounded-md border  block border-gray-200 bg-white py-1 px-3 mt-1 text-base leading-8 outline-none transition-colors duration-200 ease-in-out placeholder:text-gray-500 focus:border-blue_739  focus:ring-2 focus:ring-transparent"
      :class="{
        'border-red-500 focus:border-red-500 placeholder:text-red-500': invalid,
        [`${props.inputClass}`]: props.inputClass,
      }"
    />
    <input
    v-else
      :name="props.name"
      :type="props.type || 'text'"
      :placeholder="props.placeholder"
      :value="props.modelValue"
      autocomplete="off"
      @input="updateValue"
      @change="onChange"
      class="bg-opacity-20 w-full rounded-md border  block border-gray-200 bg-white py-1 px-3 mt-1 text-base leading-8 outline-none transition-colors duration-200 ease-in-out placeholder:text-gray-500 focus:border-blue_739  focus:ring-2 focus:ring-transparent"
      :class="{
        'border-red-500 focus:border-red-500 placeholder:text-red-500': invalid,
        [`${props.inputClass}`]: props.inputClass,
      }"
    />
    <p
      v-for="(error, index) in props.errors"
      :key="index"
      class="mt-1 text-sm text-red-600"
    >
      {{ error.$message }}
    </p>
  </div>
</template>

<script lang="ts" setup>
import { ErrorObject } from "@vuelidate/core";

const props = defineProps({
  label: String,
  placeholder: String,
  name: String,
  modelValue: String,
  type: String,
  inputClass: String,
  errors: Array as () => ErrorObject[],
});

const invalid = computed(() => {
  return props.errors && props.errors?.length > 0;
});

const emit = defineEmits(["update:modelValue", "handleChange", "onResetError"]);

const updateValue = (event: Event) => {
  emit("update:modelValue", (event.target as HTMLInputElement).value);
  if (invalid) {
    emit("onResetError");
  }
};
const onChange = (e: Event) => {

  emit("handleChange", e);
};
</script>

<style lang="scss"></style>
