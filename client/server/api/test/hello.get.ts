export default defineEventHandler(async (event) =>{

    event.node.res.statusCode =400;
    return {
        hello:'world'
    }
})