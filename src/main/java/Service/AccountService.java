package Service;

import DAO.AccountDAO;
import Model.Account;
import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;
    AccountService accountService;
    int lastAccountId;

    public AccountService() {
        this.accountDAO = new AccountDAO();
        lastAccountId = 1;
    }

    public AccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public boolean isValidUsername(String username) {
        // Implement the validation logic for username here
        if (username != null && !username.trim().isEmpty()  && username.length() < 255) {
            return true;
        }
        return false;
    }

    public boolean isValidPassword(String password) {
        if (password != null && password.length() >=4) {
            return true;
        }
        return false;
    }

    public boolean isUsernameTaken(String username) {
        return accountDAO.isUsernameTaken(username);
    }

    public int registerAccount(Account account) {
        String username = account.getUsername();
        String password = account.getPassword();
        // Here, you can implement additional logic for account registration
        if (!isValidUsername(username)) {
            throw new IllegalArgumentException("Invalid username");
        }
        if (!isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password");
        }
        if (isUsernameTaken(username)) {
            throw new IllegalArgumentException("Username already taken");
        }
        accountDAO.save(account, lastAccountId);
        lastAccountId++;
        return lastAccountId;
    }

    // public void saveAccount(Account account) {
    //     accountDAO.save(account);
    // }

    public Account getAccountByUsername(String username) {
        return accountDAO.getByUsername(username);
    }

    public List<Account> getAllAccounts() {
        return accountDAO.getAll();
    }

    public void updateAccount(Account account) {
        accountDAO.update(account);
    }

    public void deleteAccount(int accountId) {
        accountDAO.delete(accountId);
    }

    // Method to register a new user
    // public Account registerUser(String username, String password) {
    //     if (username.isEmpty() || password.length() < 4) {
    //         return null; // Invalid input, return null
    //     }

    //     // Check if username already exists
    //     if (accountDAO.isUsernameTaken(username)) {
    //         return null; // Username is taken, return null
    //     }

    //     // Create and save the new account
    //     Account account = new Account(0, username, password);
    //     accountDAO.save(account);
    //     return account;
    // }

        // Method for user login
        public Account loginUser(String username, String password) {
            Account account = accountDAO.getByUsername(username);
            if (account != null && account.getPassword().equals(password)) {
                return account; // Login successful
            }
            return null; // Login failed
        }

        public boolean doesUserExist(int userId) {
            return accountDAO.getUserById(userId) != null;
        }
    

}
