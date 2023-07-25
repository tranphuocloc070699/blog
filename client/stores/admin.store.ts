import { FetchError } from "ohmyfetch";
import { IErrorObject, IResponseObject } from "static/types/default.type";
import { IStory } from "static/types/entity.type";
import { ICreateStoryInput, IImageResponse } from "~/static/types/story.type";

export const useAdminStore = defineStore("admin", () => {

  const stories  = ref<IStory[]>([])

  const loading = ref(false);
  const uploadImage = async (formData: FormData) : Promise<IImageResponse | IErrorObject>  => {
    const response = await $api(
      "/api/admin/upload-image",
      {
        method: "POST",
        body: formData,
      }
    ).then(data => {
      const fileResponse = data as IImageResponse
      return fileResponse
    }).catch((error : FetchError ) =>{
      const errorResponse = error.response?._data as IErrorObject
      return errorResponse;
    })
    return response;
  };
  const deleteImage = async (url: String) : Promise<IResponseObject | IErrorObject>  => {
    const response = await $api(
      "/api/admin/delete-image",
      {
        method:"DELETE",
        params:{
          url
        }
      }
    ).then(data => {
      const fileResponse = data as IResponseObject
      return fileResponse
    }).catch((error : FetchError ) =>{
      const errorResponse = error.response?._data as IErrorObject
      return errorResponse;
    })
    return response;
  };

  const createStory = async (form : FormData) : Promise<IResponseObject | IErrorObject> =>{
   loading.value = true;
    return new Promise((resolve,reject) => {
      $api(
          "/api/admin/create-story",
          {
            method:"POST",
            body:form
          }
        ).then(data => {
          const response = data as IResponseObject
          resolve(response)
          loading.value = false;
        }).catch((error : FetchError ) =>{
          const errorResponse = error.response?._data as IErrorObject
          reject(errorResponse)
          loading.value = false;
        })
    })
  }

  const updateStory = async (form : FormData) : Promise<IResponseObject | IErrorObject> =>{
    loading.value = true;
     return new Promise((resolve,reject) => {
       $api(
           "/api/admin/update-story",
           {
             method:"POST",
             body:form
           }
         ).then(data => {
           const response = data as IResponseObject
           resolve(response)
           loading.value = false;
         }).catch((error : FetchError ) =>{
           const errorResponse = error.response?._data as IErrorObject
           reject(errorResponse)
           loading.value = false;
         })
     })
   }

  return { loading,uploadImage,deleteImage,createStory,updateStory,stories };
});
