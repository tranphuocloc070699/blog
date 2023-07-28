<template>
  <div v-if="props.toc && props.toc.length > 0" class="mt-5 border border-gray-200 p-4 rounded-lg">
    <h3 class="font-medium text-xl mb-4 text-gray_5d5">Table of content</h3>
    <div class="flex flex-col">
      <span
      :class="[' underline w-fit  mb-1 cursor-pointer text-[#938e93] hover:text-primary transition-colors', spaceHandler(item)]"
      v-for="(item, _) in props.toc"
      @click="handleScrollTo(item)"
    >
      {{ item.title }}
    </span>
    </div>
  </div>
</template>

<script setup lang="ts">
import { IToc } from "static/types/entity.type";
const props = defineProps({
  toc: Array<IToc>,
});
const route = useRoute();
const router = useRouter();

const spaceHandler = (item: IToc): string => {
  switch (item.type) {
    case 'H2':
      return 'font-medium  pl-2'

    case 'H3':
      return 'font-base pl-4'

    default:
      return ''
  }
};

const handleScrollTo = (item : IToc) =>{
  if(route.name){
    router.replace({name:route.name,hash:`#${item.link}`})
  }

}
</script>

<style scoped></style>
