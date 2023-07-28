import axios, { AxiosError } from "axios";
import {
  IErrorObject,
  IResponseObject,
} from "../../../static/types/default.type";
import HttpStatusCode from "../../../utils/httpStatusCode";

import { convertMultipartToFormData } from "../../../utils/converter";
import { FetchError, FetchResponse } from "ohmyfetch";
import { IGetStoryPagination, IStoryPaginationResponse, IStoryResponse } from "../../../static/types/story.type";

const config = useRuntimeConfig();

export default defineEventHandler(
  async (event): Promise<IStoryPaginationResponse | IErrorObject> => {
    return new Promise(async (resolve, reject) => {

      const paginationRequest = await readBody<IGetStoryPagination>(event);
      $fetch
        .raw(`${config.BASE_URL_SERVER}/api/v1/story/paginating`, {
          method: "GET",
          params:{
            ...paginationRequest
          }
        })
        .then((data) => {
          resolve(data._data as IStoryPaginationResponse)
        })
        .catch((error: FetchError) => {
          event.node.res.statusCode =
            error.response?.status || HttpStatusCode.BAD_REQUEST.code;
          let errorTransform: IErrorObject = {
            timestamp: new Date(),
            status: HttpStatusCode.INTERNAL_SERVER_ERROR.name,
            errors: [
              `Something happen with the server, please try again or contact with administator`,
            ],
            message: HttpStatusCode.INTERNAL_SERVER_ERROR.name,
            path: "/api/story/pagination",
          };
          if (error.response) {
            errorTransform = error.response._data as IErrorObject;
          } else {
            console.log("[story/pagination] ", error.cause);
          }
          reject({ ...errorTransform });
        });
    });
  }
);
