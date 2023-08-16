package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import java.sql.*;
import Model.*;
import DAO.*;
import Service.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import Util.ConnectionUtil;


public class SocialMediaController {
    SocialMediaService socialMediaService;
    AccountService accountService;
    MessageService messageService; 

//----------------------------------------------------------
    ObjectMapper mapper = new ObjectMapper();

    public SocialMediaController() {
        socialMediaService = new SocialMediaService();
        messageService = new MessageService();
        accountService = new AccountService();
    }
    

    public Javalin startAPI() {
        
        Javalin app = Javalin.create();
        app.post("/register", this::registerHandler);
        app.post("/messages", this::createMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByMessageIdHandler);
        app.get("/accounts/{user_id}/messages", this::getAllMessagesForUserHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);
        app.patch("/messages/{message_id}", this::updateMessageTextHandler);
        app.post("/login", this::loginHandler);
        // app.post("/register", this::registerUserHandler); 
        
        return app;
        
    }

  
    private void registerHandler(Context context) throws JsonProcessingException {

        //Account newAccount = mapper.readValue(context.body(), Account.class);
        Account newAccount = context.bodyAsClass(Account.class);
        
        // Validate the new account data using the AccountService
        if (accountService.isValidUsername(newAccount.getUsername()) && accountService.isValidPassword(newAccount.getPassword())) {
                System.out.println("Checkpoint"); 
            if (!accountService.isUsernameTaken(newAccount.getUsername())) {
                int newAccountId = accountService.registerAccount(newAccount); // Use the AccountService to register the account
                System.out.println("we created successfully");
                newAccount.account_id = newAccountId;
                context.status(200).json(newAccount);
            } else {
                context.status(400);
            }
        } else {
            context.status(400);
        }
        
    }

    private void createMessageHandler(Context context) {

        // Connection connection = ConnectionUtil.getConnection();
        // Parse the JSON body from the request to get the new message details
        Message newMessage = context.bodyAsClass(Message.class);
        // Validate the new message data using the MessageService
        if (messageService.isValidMessageText(newMessage.getMessage_text())) {

            // Check if the posted_by user exists in the database
            if (accountService.doesUserExist(newMessage.getPosted_by())) {
                // Create the message using the MessageService and MessageDAO
                int assignedId = messageService.createMessage(newMessage);
                newMessage.message_id = assignedId; 
                context.status(200).json(newMessage);
            } else {
                context.status(400);
            }
        } else {
            context.status(400);
        }
    }

    private void deleteMessageByMessageIdHandler(Context context) {
        // Connection connection = ConnectionUtil.getConnection();
        int messageId = Integer.parseInt(context.pathParam("message_id"));
    
        // Check if the message exists in the database
        Message messageToDelete = messageService.getMessageById(messageId);
        if (messageToDelete != null) {
            // Delete the message using the MessageService and MessageDAO
            messageService.deleteMessage(messageId);
            context.status(200).json(messageToDelete);
        } else {
            context.status(200);
        }
    }

    private void getAllMessagesForUserHandler(Context context) { 
        // Connection connection = ConnectionUtil.getConnection();
        int userId = Integer.parseInt(context.pathParam("user_id"));
    
        // Get all messages for the specified user using the MessageService and MessageDAO
        List<Message> messagesForUser = messageService.getAllMessagesForUser(userId);
        context.status(200).json(messagesForUser);
    }

    private void getAllMessagesHandler(Context context) {
        // Connection connection = ConnectionUtil.getConnection();
        // Get all messages using the MessageService and MessageDAO
        List<Message> allMessages = messageService.getAllMessages();
        context.status(200).json(allMessages);
    }

    private void getMessageByMessageIdHandler(Context context) {
        // Connection connection = ConnectionUtil.getConnection();
        // Extract the message ID from the request parameters
        int messageId = Integer.parseInt(context.pathParam("message_id"));
    
        // Get the message by its ID using the MessageService and MessageDAO
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            context.status(200).json(message);
        } else {
            context.status(200).result(""); // Return an empty response if the message is not found
        }
    }

    private void updateMessageTextHandler(Context context) {
        // Extract the message ID from the request parameters
        int messageId = Integer.parseInt(context.pathParam("message_id"));

    
        // Get the updated message text from the request body
        Message updatedMessage = context.bodyAsClass(Message.class);
        //System.out.println(messageText);
    
        // Check if the message ID exists in the database
        Message message = messageService.getMessageById(messageId);
        if (message != null) {
            // Validate the updated message text
            if (messageService.isValidMessageText(updatedMessage.message_text)) {
                // Update the message text using the MessageService and MessageDAO
                messageService.updateMessage(message);
                // Fetch the updated message from the database to include in the response
                message = messageService.getMessageById(messageId);
                message.message_text = updatedMessage.message_text;
                context.status(200).json(message);
            } else {
                context.status(400);
            }
        } else {
            context.status(400);
        }
    }

    private void loginHandler(Context context) {
        Account userCredentials = context.bodyAsClass(Account.class);
    
        // Validate the user's credentials using the AccountService
        if (accountService.isValidUsername(userCredentials.getUsername())) {
            Account user = accountService.loginUser(userCredentials.getUsername(), userCredentials.getPassword());
            if (user != null) {
                context.status(200).json(user);
            } else {
                context.status(401);
            }
        } else {
            context.status(401);
        }
    }

    // private void registerUserHandler(Context context) {
    //     Account newUser = context.bodyAsClass(Account.class);
    
    //     if (newUser.getUsername().isEmpty() || newUser.getPassword().length() < 4) {
    //         context.status(400).result("Invalid username or password");
    //     } else if (accountService.isValidUsername(newUser.getUsername())) {
    //         context.status(400).result("Username already exists");
    //     } else {
    //         Account registeredUser = accountService.registerUser(newUser.getUsername(), newUser.getPassword());
    //         context.status(200).json(registeredUser);
    //     }
    // }

    


}