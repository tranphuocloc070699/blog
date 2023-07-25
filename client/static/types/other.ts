export interface IMultipartFormData{
    name: string;
    filename?:string;
    type?:string;
    data:Buffer
}