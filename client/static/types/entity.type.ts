export interface IUser{
    id: number;
    email: string;
    role: string;
    name: string;
    stories: IStory[]
}

export interface IStory{
    id: number;
    thumbnail: string;
    title: string;
    slug: string;
    toc: IToc[];
    upvote:number[]
    content:string;
    preContent:string;
    comments:IComment[]
    createdAt:Date;
    updatedAt:Date;
    author: IUser
}

export interface IComment{
    id: number;
    name:string;
    email:string;
    content:string;
    createdAt:Date;
    user:IUser;
    story:IStory
}

export interface IToc{
    title:string;
    link:string;
    type:string
}