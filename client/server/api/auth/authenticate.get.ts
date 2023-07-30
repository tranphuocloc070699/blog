import { IUserResponse } from "../../../static/types/auth.type";
import { IErrorObject } from "../../../static/types/default.type";
import axios, { AxiosError } from "axios";
import HttpStatusCode from "../../../utils/httpStatusCode";
const config = useRuntimeConfig();

export default defineEventHandler(async (event) => {
  
  let cookieNotFoundError: IErrorObject = {
    timestamp: new Date(),
    status: HttpStatusCode.FORBIDDEN.name,
    errors: ['[auth-authenticate] cookie not found'],
    message: HttpStatusCode.FORBIDDEN.name,
    path: "/api/auth-authenticate",
  };

  const refreshToken : String | undefined = parseCookies(event)[config.COOKIE_NAME];
 if(!refreshToken){
  event.node.res.statusCode = HttpStatusCode.FORBIDDEN.code;
  return cookieNotFoundError
 }
 console.log(config.BASE_URL_SERVER)
 return axios
    .get(`${config.BASE_URL_SERVER}/api/v1/auth/authenticate`, {
      headers: {
        Cookie: `${config.COOKIE_NAME}=${parseCookies(event)[config.COOKIE_NAME]}`,
        "Content-Type":"application/json",
      },
      withCredentials:true
    })
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
        path: "/api/auth/authenticate",
      };
      if (error.response) {
        errorTransform = error.response.data as IErrorObject;
      }else{
        console.log('[auth/authenticate] ',error.cause)
      }

      return { ...errorTransform };
    });

});
