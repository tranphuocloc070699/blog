import { IUser } from "static/types/entity.type";
import {
  IUserRegisterInput,
  IUserResponse,
  IUserSigninInput,
} from "static/types/auth.type";
import { IErrorObject } from "static/types/default.type";
import HttpStatusCode from "~/utils/httpStatusCode";
import {TEMPORARY_ID} from "~/utils/constants";
import { IAuthStore } from "static/types/store.type";

import { FetchError } from "ohmyfetch";
export const useAuthStore = defineStore("auth", () => {
  const auth = ref<IAuthStore>({
    accessToken: "",
    logged: false,
    user: { id: 0, email: "", role: "", name: "", stories: [] },
    temporaryId:-1
  });

  const register = async (
    credential: IUserRegisterInput
  ): Promise<IUserResponse | IErrorObject> => {
    const response = await $api<IUserResponse | IErrorObject>(
      "/api/auth/register",
      {
        method: "POST",
        body: credential,
      }
    );
    if (response.status === HttpStatusCode.OK.name) {
      const userResponse = response as IUserResponse;
      auth.value.user = userResponse.data.user;
      auth.value.accessToken = userResponse.accessToken;
      auth.value.logged = true;
      return userResponse;
    } else {
      const errorResponse = response as IErrorObject;
      return errorResponse;
    }
  };

  const signin = async (
    credential: IUserSigninInput
  ): Promise<IUserResponse | IErrorObject> => {
    const response = await $api<IUserResponse | IErrorObject>(
      "/api/auth/signin",
      {
        method: "POST",
        body: credential,
        credentials: "include",
      }
    )
      .then((data) => {
        const userResponse = data as IUserResponse;
        auth.value.user = userResponse.data.user;
        auth.value.accessToken = userResponse.accessToken;
        auth.value.logged = true;
        return userResponse;
      })
      .catch((error: FetchError) => {
        const errorResponse = error.response?._data as IErrorObject;
        return errorResponse;
      });
    return response;
  };

  const authenticate = async () => {
    if(localStorage.getItem(TEMPORARY_ID) && !isNaN(+localStorage.getItem(TEMPORARY_ID)!)){
      auth.value.temporaryId = +localStorage.getItem(TEMPORARY_ID)!;
    }

    return $api<IUserResponse | IErrorObject>("/api/auth/authenticate", {
      method: "GET",
    })
      .then((data) => {
        const userResponse = data as IUserResponse;
        auth.value.user = userResponse.data.user;
        auth.value.accessToken = userResponse.accessToken;
        auth.value.logged = true;
      })
      .catch((error: FetchError) => {
        const errorResponse = error.response?._data as IErrorObject;
        if (error.statusCode === HttpStatusCode.FORBIDDEN.code && errorResponse.errors.includes('[auth-authenticate] cookie not found')) {
          if(!localStorage.getItem(TEMPORARY_ID)){
            localStorage.setItem(TEMPORARY_ID,`${Date.now()}`)
          }
        }

        // console.log("[store - validate] ", errorResponse);
      });
  };
  return { auth, register, signin, authenticate };
});
