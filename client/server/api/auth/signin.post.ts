import { IUserResponse } from "../../../static/types/auth.type";
import { IErrorObject } from "../../../static/types/default.type";
import axios, { AxiosError } from "axios";
import HttpStatusCode from "../../../utils/httpStatusCode";

const config = useRuntimeConfig();

export default defineEventHandler(async (event) => {
  
  const response = await axios
    .post(
      `${config.BASE_URL_SERVER}/api/v1/auth/login`,
      await readBody(event),
      {
        headers: {
          "Content-Type": "application/json",
        },
        withCredentials: true,
      }
    )
    .then((data) => {
      const cookie = data.headers["set-cookie"];
      if (cookie && cookie[0]) {
        event.node.res.setHeader('Set-Cookie',cookie);
      }
      const dataTransform: IUserResponse = data.data as IUserResponse;
      return dataTransform;
    })
    .catch((error: AxiosError) => {
      event.node.res.statusCode = error.response?.status || HttpStatusCode.BAD_REQUEST.code
      let errorTransform: IErrorObject = {
        timestamp: new Date(),
        status: HttpStatusCode.INTERNAL_SERVER_ERROR.name,
        errors: [`Something happen with the server, please try again or contact with administator`],
        message: HttpStatusCode.INTERNAL_SERVER_ERROR.name,
        path: "/api/auth/signin",
      };
    
      if (error.response) {
        errorTransform = error.response.data as IErrorObject;
      }else{
        console.log('[auth/signin] ',error.cause)
      }
      return { ...errorTransform };
    });

  return response;
});
