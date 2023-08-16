package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    Connection connection = ConnectionUtil.getConnection();    
    // Method to retrieve a Message by its ID from the database


    public Message getById(int message_id) {
        try {
            String sql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, message_id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                return new Message(message_id, postedBy, messageText, timePostedEpoch);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
        return null;
    }

    
    // Method to retrieve all Messages from the database
    public List<Message> getAll() {
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                int postedBy = resultSet.getInt("posted_by");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                messages.add(new Message(messageId, postedBy, messageText, timePostedEpoch));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
        return messages;
    }

    // Method to save a new Message to the database
    public void save(Message message, int lastMessageId) {
        try {
            String sql = "INSERT INTO Message (message_id, posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, lastMessageId+1);
            statement.setInt(2, message.getPosted_by());
            statement.setString(3, message.getMessage_text());
            statement.setLong(4, message.getTime_posted_epoch());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
    }

    // Method to update a message
    public void update(Message message) {
        try {
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, message.getMessage_text());
            statement.setInt(2, message.getMessage_id());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
    }

    // Method to delete a message
    public void delete(int messageId) {
        try {
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, messageId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
    }

    public List<Message> getAllMessagesForUser(int userId) {
        List<Message> messagesForUser = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Message WHERE posted_by = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int messageId = resultSet.getInt("message_id");
                String messageText = resultSet.getString("message_text");
                long timePostedEpoch = resultSet.getLong("time_posted_epoch");
                messagesForUser.add(new Message(messageId, userId, messageText, timePostedEpoch));
                
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return messagesForUser;
        }
    }
