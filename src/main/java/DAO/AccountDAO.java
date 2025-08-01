package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    /**
     * Retrieve all accounts from the Account table.
     * @return all Accounts.
     */

    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        
        try {
            String sql = "Select * from account";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password")); 
                accounts.add(account);           
            }
            return accounts;
        } catch (SQLException e) {            
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccountByUsername(String username){
        Connection connection = ConnectionUtil.getConnection();
        
        try {
            String sql = "Select * from account where username = ?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1, username);
            ResultSet rs=preparedStatement.executeQuery();
            if(rs.next()){
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));                
            }
        } catch (SQLException e) {            
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Insert an account into Account table.
     */
    public Account createAccount(Account account) {
    Connection connection = ConnectionUtil.getConnection();
    try {
        String sql = "INSERT INTO account(username, password) VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, account.getUsername());
        preparedStatement.setString(2, account.getPassword());

        int rowsInserted = preparedStatement.executeUpdate();
        if (rowsInserted == 1) {
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_account_id = pkeyResultSet.getInt(1);
                return new Account(generated_account_id, account.getUsername(), account.getPassword());
            }
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return null;
    }
    
}
