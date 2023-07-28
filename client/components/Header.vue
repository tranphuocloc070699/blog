<template>
  <div>
    <div class="bg-gradient-to-r from-blue_739 to-blue_557 py-2">
      <ClientOnly>
        <div
        v-if="authStore.auth.logged"
        class="max-w-7xl w-full px-4 flex items-center justify-end mx-auto"
      >
        <a href="/admin" class="text-white">
        {{ authStore.auth.user.name }}
        </a>
      </div>
        <div
        v-else
          class="max-w-7xl w-full px-4 flex items-center justify-end mx-auto"
        >
          <SignIn class="mr-4" />
          <Register />
        </div>
      </ClientOnly>

    </div>
    <div class="border-b border-gray-300">
      <div
        class="max-w-7xl w-full px-4 flex items-center justify-between py-4 mx-auto"
      >
        <div class="border border-blue_739 p-1 bg-white flex items-center cursor-pointer hover:scale-110 transition-all">
          <div class="bg-blue_739 font-medium text-white text-center px-4 py-1 text-sm">TTT</div>
          <div class="w-32 text-center font-medium text-slate-700">Tell The Truth</div>
        </div>
        <div class="flex">
          <NuxtLink to="/" class="font-medium text-sm hover:underline">HOME</NuxtLink>
        </div>
        <div></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from "~/stores/auth.store";

const authStore = useAuthStore();
</script>
