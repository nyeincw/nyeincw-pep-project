package Service;

import Model.Account;
import DAO.AccountDAO;



public class AccountService {
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account registerAccount(Account account){
        String username = account.getUsername();
        String password = account.getPassword();

        //Validation for only if the username is not blank
        if(username == null || username.isBlank()){
            return null;
        }
        //Validation for the password is at least 4 characters long
        if(password==null || password.length()<4){
            return null;
        }

        //Check if an Account with that username does not already exist
        if(accountDAO.getAccountByUsername(username) != null){
            return null;
        }

        return accountDAO.createAccount(account);
    }

    public Account loginAccount(String username,String password){
        if(username == null || username.isBlank() || password == null || password.isBlank()){
            return null;
        }

        Account accountExisted = accountDAO.getAccountByUsername(username);
        if(accountExisted!=null && accountExisted.getPassword().equals(password)){
            return accountExisted;
        }

        return null;
    }

    
    
}
