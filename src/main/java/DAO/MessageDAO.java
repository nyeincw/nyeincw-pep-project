package DAO;

import Util.ConnectionUtil;
import Model.Account;
import Model.Message;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    
    //retrieve all messages
    /**
     * Retrieve all messages from the Message table.
     * @return all Messages.
     */
    public List<Message> getAllMessages(){
        Connection connection= ConnectionUtil.getConnection();
        List<Message> messages= new ArrayList<>();
        try{
            String sql="Select * from message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()){
                Message message=new Message(rs.getInt("message_id"),
                                            rs.getInt("posted_by"),
                                            rs.getString("message_text"),
                                            rs.getLong("time_posted_epoch"));
                messages.add(message);
            }      
    
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    return messages;
    }
    //retrieve a message by its ID.
    /**
     * Retrieve a message from the Message table, identified by its message ID.
     * @return a message identified by message ID.
     */
    public Message getMessageByID(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql="Select * from message where message_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()){
                Message message=new Message(rs.getInt("message_id"), rs.getInt("posted_by"),                                   
                                    rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                return message;
            }
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    //retrieve all messages written by a particular user
    /**
     * Retrieve all messages from the Message table, identified by its account ID.
     * @return all messages identified by posted_by ID.
     */
    public List<Message> getAllMessagesByAccount(int posted_by){
        Connection connection=ConnectionUtil.getConnection();
        List<Message> messagesByAccount = new ArrayList<>();
        try {
            String sql="Select * from message where posted_by=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,posted_by);
            ResultSet rs=preparedStatement.executeQuery();
            while(rs.next()){
                Message message=new Message(rs.getInt("message_id"),rs.getInt("posted_by"),
                                    rs.getString("message_text"),
                                    rs.getLong("time_posted_epoch"));
                messagesByAccount.add(message);
            }            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return messagesByAccount;
    }
    //creation of new messages
    /**
     * Insert a message into the Message table.
     */
    public Message insertMessage(Message message){
        Connection connection=ConnectionUtil.getConnection();
        try {
            String sql = "Insert into message(posted_by,message_text,time_posted_epoch) values (?,?,?)";
            PreparedStatement preparedStatement=connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1,message.getPosted_by());
            preparedStatement.setString(2,message.getMessage_text());
            preparedStatement.setLong(3,message.getTime_posted_epoch());
            int rowsInserted = preparedStatement.executeUpdate();
        if (rowsInserted == 1) {
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generated_message_id = pkeyResultSet.getInt(1);
                return new Message(generated_message_id, message.getPosted_by(), message.getMessage_text(),message.getTime_posted_epoch());
            }
        }
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return null;
    }
    
    //delete a message identified by a message ID
    /**
     * Delete a message from the Message table.
     */
    public Message deleteMessage(int id){
        Connection connection=ConnectionUtil.getConnection();
        try {
            String sql = "Delete from message where message_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setInt(1,id);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Message message = new Message(rs.getInt("message_id"),rs.getInt("posted_by"),
                                        rs.getString("message_text"),rs.getLong("time_posted_epoch"));
                return message;  
            }
                      
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    //update a message text identified by a message ID
    /**
     * Update a message to the Message table.
     */
    public Message updateMessage(int id, String newMessageText){
        Connection connection=ConnectionUtil.getConnection();
        try {
            String sql = "Update message set message_text=? where message_id=?";
            PreparedStatement preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1,newMessageText);
            preparedStatement.setInt(2, id);
            int rowsUpdated = preparedStatement.executeUpdate();
            if(rowsUpdated>0){
                Message updatedMessage = getMessageByID(id);
                return updatedMessage;
            }             
            
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
