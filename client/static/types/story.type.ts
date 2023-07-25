/********************************************/
/**************** Request *******************/

import { IResponseObject } from "./default.type";
import { IStory } from "./entity.type";

/********************************************/
export interface ICreateStoryInput {
  title: string;
  slug: string;
  content: string;
  pre_content:string;
  toc: ITocItem[];
  thumbnail: File | null;
}

export interface IUpVoteInput{
  id:number;
  story_id:number;
}

export interface IUpdateStoryInput {
  id:number;
  title: string;
  slug: string;
  content: string;
  preContent:string;
  toc: ITocItem[];
  thumbnail: string;
}
export interface IGetStoryPagination{
    current_page:number;
    items_per_page:number;
    created_at:'desc' | 'asc'
}

export interface ITocItem {
  link: string;
  title: string;
  type: string;
}

/*********************************************/
/**************** Response *******************/
/*********************************************/
export interface IStoryResponse extends IResponseObject {
  data: {
    story: IStory;
  };
}
export interface IStoryPaginationResponse extends IResponseObject {
  data: {
    content: IStory[];
    pageable: {
      sort: {
        empty: boolean;
        unsorted: boolean;
        sorted: boolean;
      };
      offset: number;
      pageNumber: number;
      pageSize: number;
      paged: boolean;
      unpaged: boolean;
    };
    last: boolean;
    totalPages: number;
    totalElements: number;
    first: boolean;
    size: number;
    number: number;
    sort: {
      empty: boolean;
      unsorted: boolean;
      sorted: boolean;
    };
    numberOfElements: number;
    empty: boolean;
  };
}

export interface IImageResponse extends IResponseObject {
  accessToken: String;
  data: {
    url: String;
  };
}
