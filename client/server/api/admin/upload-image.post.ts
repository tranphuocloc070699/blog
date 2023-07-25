import axios, { AxiosError } from "axios";
import { IErrorObject } from "../../../static/types/default.type";
import HttpStatusCode from "../../../utils/httpStatusCode";
import { FormData } from "node-fetch-native";
import { IImageResponse } from "../../../static/types/story.type";
import { convertRequestToFile } from "../../../utils/converter";

const config = useRuntimeConfig();

export default defineEventHandler(async (event) => {
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
    return bodyNotFound;
  }

  let file: File | undefined = undefined;
  await convertRequestToFile(body[0])
    .then((data) => {
      file = data as File;
    })
    .catch((error) => {
      console.log("[admin/upload-thumbnail] [upload file]", error);
    });
  const formData = new FormData();

  if (file) {
    formData.append(body[0].name!, file);
  }
  return axios
    .post(
      `${config.BASE_URL_SERVER}/api/v1/admin/story/upload-thumbnail`,
      formData,
      {
        headers: {
          "Content-Type": "multipart/form-data",
          ...(authorization && { Authorization: authorization }),
        },
        withCredentials: true,
      }
    )
    .then((data) => {
      const dataTransform: IImageResponse = data.data as IImageResponse;
      return dataTransform;
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
      return { ...errorTransform };
    });
});
