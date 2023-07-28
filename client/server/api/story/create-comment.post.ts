import axios, { AxiosError } from "axios";
import {
  IErrorObject,
  IResponseObject,
} from "../../../static/types/default.type";
import HttpStatusCode from "../../../utils/httpStatusCode";

import { convertMultipartToFormData } from "../../../utils/converter";
import { FetchError, FetchResponse } from "ohmyfetch";
import { IGetStoryPagination, IStoryPaginationResponse, IStoryResponse } from "../../../static/types/story.type";
import { ICommentResponse, ICreateCommentInput } from "../../../static/types/comment.type";

const config = useRuntimeConfig();

export default defineEventHandler(
  async (event): Promise<ICommentResponse | IErrorObject> => {
    return new Promise(async (resolve, reject) => {

      const dto = await readBody<ICreateCommentInput>(event);
 
      $fetch
        .raw(`${config.BASE_URL_SERVER}/api/v1/story/comment`, {
          method: "POST",
          body:dto
        })
        .then((data) => {
          resolve(data._data as ICommentResponse)
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
