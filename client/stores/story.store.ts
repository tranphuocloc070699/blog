import { FetchError } from "ohmyfetch";
import { IErrorObject, IResponseObject } from "~/static/types/default.type";
import { ICreateStoryInput, IGetStoryPagination, IImageResponse, IStoryPaginationResponse } from "~/static/types/story.type";

export const useStoryStore = defineStore("story", () => {
  const getStoryPagination = async (dto : IGetStoryPagination) : Promise<IStoryPaginationResponse | IErrorObject>  => {
    return new Promise((resolve,reject) => {
        $api(
            "/api/story/pagination",
            {
              method:"POST",
              body:dto
            }
          ).then(data => {
            const response = data as IStoryPaginationResponse
            resolve(response)
          }).catch((error : FetchError ) =>{
            const errorResponse = error.response?._data as IErrorObject
            reject(errorResponse)
          })
      })
  };




  return { getStoryPagination};
});
