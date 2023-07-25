<template>
  <transition
 name="modal" appear @after-enter="afterEnter" @after-leave="afterLeave"
  >
    <div
      v-if="props.isOpen"
      class="z-[1000000] fixed inset-0 flex items-center justify-center bg-black bg-opacity-20"
    >
      <div :class="['bg-white rounded p-4',customClass]">
        <slot/>
      </div>
    </div>
  </transition>
</template>

<script setup lang="ts">
const props = defineProps({
  isOpen: Boolean,
  customClass:String
});

// const emit = defineEmits({
//   close: null,
// });

// const closeModal = () => {
//   emit("close");
// };
const afterEnter = () => {
  document.body.classList.add("overflow-hidden");
};
const afterLeave = () => {
  document.body.classList.remove("overflow-hidden");
};
</script>

<style>
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.25s ease;
}

.modal-enter,
.modal-leave-to {
  opacity: 0;
}
</style>
