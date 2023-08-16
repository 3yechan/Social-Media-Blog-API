package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    // Method to check if Username is in the database
    public boolean isUsernameTaken(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT COUNT(*) FROM Account WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0; // If count > 0, username is taken
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // In case of any exception, assume username is not taken
    }

    // Method to save a new Account to the database
    public void save(Account account, int lastAccountId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO Account (account_id, username, password) VALUES (?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, lastAccountId+1);
            statement.setString(2, account.getUsername());
            statement.setString(3, account.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
    }

    // Method to retrieve an Account by username from the database
    public Account getByUsername(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "SELECT * FROM Account WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int accountId = resultSet.getInt("account_id");
                String password = resultSet.getString("password");
                return new Account(accountId, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
        return null;
    }

    // Method to retrieve all Accounts from the database
    public List<Account> getAll() {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Account";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int accountId = resultSet.getInt("account_id");
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                accounts.add(new Account(accountId, username, password));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
        return accounts;
    }

    // Method to update an Account in the database
    public void update(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "UPDATE Account SET username = ?, password = ? WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, account.getUsername());
            statement.setString(2, account.getPassword());
            statement.setInt(3, account.getAccount_id());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
    }

    // Method to delete an Account from the database
    public void delete(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "DELETE FROM Account WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, accountId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
    }

    public Account getUserById(int userId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            
            String sql = "SELECT * FROM Account WHERE account_id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                return new Account(userId, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
        }
        return null;
    }
}
