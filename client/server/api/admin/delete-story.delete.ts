import axios, { AxiosError } from "axios";
import { IErrorObject, IResponseObject } from "../../../static/types/default.type";
import HttpStatusCode from "../../../utils/httpStatusCode";

const config = useRuntimeConfig();
//: Promise<IResponseObject | IErrorObject>
export default defineEventHandler(async (event)  => {
  const authorization = event.node.req.headers["authorization"];
  const body = await readBody(event);
 

  if (!body) {
    let bodyNotFound: IErrorObject = {
      timestamp: new Date(),
      status: HttpStatusCode.BAD_REQUEST.name,
      errors: ["[admin - delete-story] body"],
      message: HttpStatusCode.BAD_REQUEST.name,
      path: "/api/admin/delete-story",
    };
    return bodyNotFound;
  }
  return new Promise((resolve,reject) =>{
    axios
    .delete(`${config.BASE_URL_SERVER}/api/v1/admin/story/${body}`, {
      headers: {
        ...(authorization && { Authorization: authorization }),
      },
      withCredentials: true,
    })
    .then((data) => {
      const dataTransform: IResponseObject = data.data as IResponseObject;
      console.log({dataTransform})
       resolve(dataTransform)
    })
    .catch((error: AxiosError) => {
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
        errorTransform = error.response.data as IErrorObject;
      } else {
        console.log("[admin/upload-thumbnail] ", error.cause);
      }
      console.log({errorTransform})
      reject({ ...errorTransform })
    });
  })
});
