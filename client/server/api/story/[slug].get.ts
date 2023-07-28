
import { FetchError } from "ohmyfetch";
import {
    IErrorObject
} from "../../../static/types/default.type";

import { IStoryResponse } from "../../../static/types/story.type";
import HttpStatusCode from "../../../utils/httpStatusCode";

const config = useRuntimeConfig();

export default defineEventHandler(
  async (event): Promise<IStoryResponse | IErrorObject> => {
    const slug = event.context.params?.slug

    return new Promise<IStoryResponse | IErrorObject>(async (resolve, reject)=> {
      $fetch
      .raw(`${config.BASE_URL_SERVER}/api/v1/story/${slug}`, {
        method: "GET"
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
          path: `/api/story/${slug}`,
        };
        if (error.response) {
          errorTransform = error.response._data as IErrorObject;
        } else {
          console.log(`[/api/story/${slug}] `, error.cause);
        }

        reject({ ...errorTransform });
      });
    });
  }
);
