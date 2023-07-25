<template>

    <aside
      id="sidebar-multi-level-sidebar"
      class=" transition-transform -translate-x-full sm:translate-x-0"
      aria-label="Sidebar"
    >
      <div class="h-[100vh] shrink-0 w-72 px-3 py-4 overflow-y-auto bg-gray-50 dark:bg-gray-800">
        <ul class="space-y-2 font-medium">
          <li>
            <a
              href="#"
              class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group"
            >
              <Icon class="text-2xl text-gray-500" name="fa6-solid:house" />
              <span class="ml-3">Dashboard</span>
            </a>
          </li>
          
          <li>
            <button
              type="button"
              class="flex items-center w-full p-2 text-base text-gray-900 transition duration-75 rounded-lg group hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
              aria-controls="dropdown-example"
              data-collapse-toggle="dropdown-example"
            >
              <Icon name="iconamoon:news-fill" class="text-2xl text-gray-500" />
              <span class="flex-1 ml-3 text-left whitespace-nowrap">Story</span>
              <Icon name="mingcute:down-fill" class="text-2xl text-gray-500" />
            </button>
            <ul id="dropdown-example" class="hidden py-2 space-y-2">
              <li>
                <a
                  href="/admin/create-story"
                  class="flex items-center w-full p-2 text-gray-900 transition duration-75 rounded-lg pl-11 group hover:bg-gray-100 dark:text-white dark:hover:bg-gray-700"
                  >Create Story</a
                >
              </li>
            </ul>
          </li>
          <li>
            <a
              href="#"
              class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group"
            >
             <Icon name="mdi:bell" class="text-2xl text-gray-500"/>
              <span class="flex-1 ml-3 whitespace-nowrap">Notify</span>
              <span
                class="inline-flex items-center justify-center w-3 h-3 p-3 ml-3 text-sm font-medium text-blue-800 bg-blue-100 rounded-full dark:bg-blue-900 dark:text-blue-300"
                >3</span
              >
            </a>
          </li>
          <li>
            <a
              href="#"
              class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group"
            >
              <Icon class="text-2xl text-gray-500" name="mdi:users"/>
              <span class="flex-1 ml-3 whitespace-nowrap">Users</span>
            </a>
          </li>
          <li>
            <a
              href="#"
              class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group"
            >
              <Icon class="text-2xl text-gray-500" name="fa6-solid:comments"/>
              <span class="flex-1 ml-3 whitespace-nowrap">Comments</span>
            </a>
          </li>
          <li>
            <div
              @click="signout"
              class="flex items-center p-2 text-gray-900 rounded-lg dark:text-white hover:bg-gray-100 dark:hover:bg-gray-700 group"
            >
              <Icon class="text-2xl text-gray-500" name="lucide:log-out"/>
              <span class="flex-1 ml-3 whitespace-nowrap">Sign Out</span>
            </div>
          </li>
        </ul>
      </div>
    </aside>
</template>

<script setup lang="ts">
const signout = async () =>{
 
}
</script>

<style scoped></style>
