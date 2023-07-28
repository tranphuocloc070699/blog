import { type NitroFetchRequest } from 'nitropack'
import { useAuthStore } from '~/stores/auth.store'

export function $api<T = unknown, R extends NitroFetchRequest = NitroFetchRequest>(
  request: Parameters<typeof $fetch<T, R>>[0],
  options?: Partial<Parameters<typeof $fetch<T, R>>[1]>,
) {
  const authStore = useAuthStore()

  return $fetch<T, R>(request, {
    ...options,
    headers: {
        ...(authStore.auth.logged && authStore.auth.accessToken
            ? { Authorization: `Bearer ${authStore.auth.accessToken}` }
            : {}),
      ...options?.headers
    }
  })
}