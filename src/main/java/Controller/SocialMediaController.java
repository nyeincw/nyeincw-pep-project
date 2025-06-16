package Controller;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Service.AccountService;

import Model.Message;
import Service.MessageService;

import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController(){
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::postAccountHandler);
        app.post("/login", this::postLoginAccountHandler);
        app.post("/messages", this::postMessageHandler);
        app.get("/messages",this::getALLMessagesHandler);
        app.get("/messages/{message_id}",this::getMessageByIDHandler);
        app.delete("/messages/{message_id}",this::deleteMessageHandler);
        app.patch("/messages/{message_id}",this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getALLMessagesbyAccountHandler);

        

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void postAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper=new ObjectMapper();
        Account account=mapper.readValue(ctx.body(),Account.class);
        Account registeredAccount=accountService.registerAccount(account);
        if (registeredAccount != null) {
        ctx.status(200); // Created
        ctx.json(registeredAccount);
        } else {
        ctx.status(400);
        }        
    }

    private void postLoginAccountHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account credentials  = mapper.readValue(ctx.body(),Account.class);
        Account Authenticated = accountService.loginAccount(credentials.getUsername(),credentials.getPassword());
        if(Authenticated != null){
            ctx.json(mapper.writeValueAsString(Authenticated));            
        }else{
            ctx.status(401);
        }
    }

    private void postMessageHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(),Message.class);
        Message createMessage = messageService.createMessage(message);
        if(createMessage!=null){
            ctx.json(mapper.writeValueAsString(createMessage));
        }else{
            ctx.status(400);
        }
    }

    private void getALLMessagesHandler(Context ctx) throws JsonProcessingException{
        List<Message> messages = messageService.getAllMessages();        
            if (messages != null){
                ctx.json(messages);
            }else{
                ctx.status(400);
            }
        
    }

    private void getMessageByIDHandler(Context ctx) throws JsonProcessingException{
        
            int id =Integer.parseInt(ctx.pathParam("message_id"));
            Message message = messageService.getMessagebyID(id);            
            ctx.status(200);
            if (message != null) {
                ctx.json(message);  // Return the message JSON if found
            } else {
                ctx.result("");  // Return empty body if not found
            }            
            
    }

    private void deleteMessageHandler(Context ctx) throws JsonProcessingException{
            /**
             * - The deletion of an existing message should remove an existing message from the database. 
             * If the message existed, the response body should contain the now-deleted message. 
             * The response status should be 200, which is the default.
             * - If the message did not exist, the response status should be 200, 
             * but the response body should be empty. This is because the DELETE verb is intended to be idempotent, 
             * ie, multiple calls to the DELETE endpoint should respond with the same type of response.
             **/
            int id=Integer.parseInt(ctx.pathParam("message_id"));
            Message message=messageService.getMessagebyID(id);          
            ctx.status(200);
            if (message != null){  
                messageService.deleteMessage(id);                             
                ctx.json(message);
            }else{                
                ctx.result("");
            }            
        
    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException{
        /*
         * As a user, I should be able to submit a PATCH request on the endpoint PATCH localhost:8080/messages/{message_id}. 
         * The request body should contain a new message_text values to replace the message identified by message_id. 
         * The request body can not be guaranteed to contain any other information.
         * - The update of a message should be successful if and only if the message id already exists 
         * and the new message_text is not blank and is not over 255 characters. 
         * If the update is successful, the response body should contain the full updated message 
         * (including message_id, posted_by, message_text, and time_posted_epoch), 
         * and the response status should be 200, which is the default.
         * The message existing on the database should have the updated message_text.
         * - If the update of the message is not successful for any reason, the response status should be 400. (Client error)
         */
            ObjectMapper mapper = new ObjectMapper();
            int id =Integer.parseInt(ctx.pathParam("message_id"));
            Message message=mapper.readValue(ctx.body(),Message.class);
            String newText = message.getMessage_text();

            Message existedMessage=messageService.getMessagebyID(id);
            if (existedMessage == null) {
                ctx.status(400);                
            }

            Message updatedMessage = messageService.updateMessage(id, newText);
            if (updatedMessage != null) {
                ctx.status(200);  // success
                ctx.json(updatedMessage);
            } else {
                ctx.status(400);  // invalid input like blank or too long text
            }           
        
    }

    private void getALLMessagesbyAccountHandler(Context ctx) throws JsonProcessingException{
        
            int id =Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getAllMessagesbyAccountID(id);
            if (messages != null){
                ctx.json(messages);
            }else{
                ctx.status(400);
            }
         
    }
}


