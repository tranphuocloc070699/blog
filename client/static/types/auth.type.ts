import { IResponseObject } from "./default.type";
import { IUser } from "./entity.type";


/********************************************/ 
/**************** Request *******************/
/********************************************/ 
export interface IUserRegisterInput{
    name: string;
    email: string;
    password: string
}
export interface IUserSigninInput{
    email: string;
    password: string
}
/*********************************************/ 
/**************** Response *******************/
/*********************************************/ 
export interface IUserResponse extends IResponseObject{
    accessToken:string;
    data:{
        user:IUser
    }
}