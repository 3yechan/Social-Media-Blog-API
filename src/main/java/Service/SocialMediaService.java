package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

import java.sql.Connection;
import java.util.List;
import java.util.ArrayList;

public class SocialMediaService {
    AccountDAO accountDAO;
    MessageDAO messageDAO;
    AccountService accountService;
    MessageService messageService;
   
    public SocialMediaService() {
        accountDAO = new AccountDAO();
        messageDAO = new MessageDAO();
    }


   

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    // User registration logic
    // public Account registerUser(String username, String password) {
    //     if (username.isEmpty() || password.length() < 4) {
    //         return null; 
    //     }

    //     // Check if username already exists
    //     if (accountDAO.isUsernameTaken(username)) {
    //         return null;
    //     }

    //     // Create and save the new account
    //     Account account = new Account(0, username, password);
    //     accountDAO.save(account);
    //     return account;
    // }

    // User login logic
    public Account loginUser(String username, String password) {
        Account account = accountDAO.getByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account; // Login successful
        }
        return null; // Login failed
    }

    // Method to retrieve all messages from the database
    public List<Message> getAllMessages() {
        return messageDAO.getAll();
    }

    // Method to retrieve all messages from the database for a specific user
    public List<Message> getAllMessagesForUser(int userId) {
        // Implement the logic to retrieve messages for a specific user using messageDAO
        // You can use the messageDAO.getAll() method and then filter the messages for the specific user
        List<Message> allMessages = messageDAO.getAll();
        List<Message> messagesForUser = new ArrayList<>();
        for (Message message : allMessages) {
            if (message.getPosted_by() == userId) {
                messagesForUser.add(message);
            }
        }
        return messagesForUser;
    }

    // Add more methods as needed for message-related operations

    // Method to post a new message
    // public void postMessage(Message message) {
    //     // Implement the logic to post a new message using messageDAO
    //     // You can use the messageDAO.save(message) method to save the new message
    //     messageDAO.save(message, );
    // }

    // Method to update a message
    public void updateMessage(Message message) {
        // Implement the logic to update a message using messageDAO
        // You can use the messageDAO.update(message) method to update the message
        messageDAO.update(message);
    }

    // Method to delete a message
    public void deleteMessage(int messageId) {
        // Implement the logic to delete a message using messageDAO
        // You can use the messageDAO.delete(messageId) method to delete the message
        messageDAO.delete(messageId);
    }
}
