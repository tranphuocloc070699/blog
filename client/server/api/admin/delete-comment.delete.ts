import {
    IErrorObject,
    IResponseObject
} from "../../../static/types/default.type";
import HttpStatusCode from "../../../utils/httpStatusCode";

import { FetchError } from "ohmyfetch";

const config = useRuntimeConfig();

export default defineEventHandler(
  async (event): Promise<IResponseObject | IErrorObject> => {
    const authorization = event.node.req.headers["authorization"];
    return new Promise(async (resolve, reject) => {

      const id = await readBody<number>(event);
      $fetch
        .raw(`${config.BASE_URL_SERVER}/api/v1/admin/story/comment/${id}`, {
          method: "DELETE",
          headers: {
            ...(authorization && { Authorization: authorization }),
          },
          withCredentials: true,
        })
        .then((data) => {
          resolve(data._data as IResponseObject)
        })
        .catch((error: FetchError) => {
            console.log({error})
          event.node.res.statusCode =
            error.response?.status || HttpStatusCode.BAD_REQUEST.code;
          let errorTransform: IErrorObject = {
            timestamp: new Date(),
            status: HttpStatusCode.INTERNAL_SERVER_ERROR.name,
            errors: [
              `Something happen with the server, please try again or contact with administator`,
            ],
            message: HttpStatusCode.INTERNAL_SERVER_ERROR.name,
            path: "/api/story/comment",
          };
          if (error.response) {
            errorTransform = error.response._data as IErrorObject;
          } else {
            console.log("[story/delete-comment] error ", error.cause);
          }
          console.log({errorTransform})
          reject({ ...errorTransform });
        });
    });
  }
);
