package Service;

import DAO.*;
import Model.Message;
import java.util.List;

public class MessageService {
    MessageDAO messageDAO;
    MessageService messageService;
    int lastMessageId = 1;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public int createMessage(Message message) {
        // Implement the logic to create a new message using the MessageDAO
        messageDAO.save(message, lastMessageId);
        lastMessageId++;
        return lastMessageId;
    }

    public MessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    
    public Message getMessageById(int messageId) {
        // Implement the logic to get a message by its ID using the MessageDAO
        return messageDAO.getById(messageId);
    }
    public List<Message> getAllMessages() {
        // Implement the logic to get all messages using the MessageDAO
        return messageDAO.getAll();
    }
    
    public void updateMessage(Message message) {
        int message_id = message.getMessage_id();
        Message existingMessage = messageDAO.getById(message_id);

        if (existingMessage != null) {
            if (isValidMessageText(message.getMessage_text())) {
                messageDAO.update(message);
            } else {
                throw new IllegalArgumentException("Invalid message text.");
            }
        } else {
            throw new IllegalArgumentException("Message with ID " + message_id + " does not exist.");
        }
    }

    
    public void deleteMessage(int messageId) {
        messageDAO.delete(messageId);
    }
    
    public boolean isValidMessageText(String messageText) {
        return messageText != null && !messageText.isEmpty() && messageText.length() < 255;
    }
    // Method to get all messages for a specific user
    public List<Message> getAllMessagesForUser(int userId) {
        // Implement the logic to retrieve messages for a specific user using messageDAO
        return messageDAO.getAllMessagesForUser(userId);
    }
    

}