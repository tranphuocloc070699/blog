import axios, { AxiosError } from "axios";
import {
  IErrorObject,
  IResponseObject,
} from "../../../static/types/default.type";
import HttpStatusCode from "../../../utils/httpStatusCode";

import { convertMultipartToFormData } from "../../../utils/converter";
import { FetchError, FetchResponse } from "ohmyfetch";
import { IStoryResponse } from "../../../static/types/story.type";

const config = useRuntimeConfig();

export default defineEventHandler(
  async (event): Promise<IStoryResponse | IErrorObject> => {
    return new Promise(async (resolve, reject) => {
      const authorization = event.node.req.headers["authorization"];
      const body = await readMultipartFormData(event);

      if (!body) {
        let bodyNotFound: IErrorObject = {
          timestamp: new Date(),
          status: HttpStatusCode.BAD_REQUEST.name,
          errors: ["[admin - upload-thumbnail] body"],
          message: HttpStatusCode.BAD_REQUEST.name,
          path: "/api/admin/upload-thumbnail",
        };
         reject(bodyNotFound);
      }

      const form = await convertMultipartToFormData(body);

      // for (let [key, value] of form) {
      //   console.log(`${key}: ${value}`);
      // }

      $fetch
        .raw(`${config.BASE_URL_SERVER}/api/v1/admin/story`, {
          method: "PUT",
          headers: {
            ...(authorization && { Authorization: authorization }),
          },
          withCredentials: true,
          body: form,
        })
        .then((data) => {
          resolve(data._data as IStoryResponse)
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
            path: "/api/auth/authenticate",
          };
          if (error.response) {
            errorTransform = error.response._data as IErrorObject;
          } else {
            console.log("[admin/create-story] ", error.cause);
          }
          console.log({errorTransform})
          reject({ ...errorTransform });
        });
    });
  }
);
