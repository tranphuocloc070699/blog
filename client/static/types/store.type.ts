import { IUser } from "./entity.type";

export interface IAuthStore{
    accessToken:String;
    logged:Boolean;
    user:IUser;
    temporaryId:number;
}