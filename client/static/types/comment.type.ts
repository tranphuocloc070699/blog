import { IComment } from "./entity.type";
import { IResponseObject } from "./default.type";
export interface ICreateCommentInput{
    story_id:number;
    email:string;
    name:string;
    content:string;
    paren_comment_id?:number
}




export interface ICommentResponse extends IResponseObject {
    
    data: {
      comment: IComment;
    };
  }