/********************************************/ 
/**************** Request *******************/
/********************************************/ 



/*********************************************/ 
/**************** Response *******************/
/*********************************************/ 
export interface IResponseObject{
    timestamp:Date,
    status:string,
    message:string,
    path:string
}
export interface IErrorObject extends IResponseObject{
    errors:string[],
}
