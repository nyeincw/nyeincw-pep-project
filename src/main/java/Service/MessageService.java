package Service;

import Model.Account;
import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService(){
        messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO messageDAO){
        this.messageDAO = messageDAO;
    }

    public Message createMessage(Message message){
        String messageText = message.getMessage_text();
        Integer posted_by = message.getPosted_by();

        List<Message> existingUser = messageDAO.getAllMessagesByAccount(posted_by);
        //only if the message_text is not blank, is not over 255 characters, and posted_by refers to a real, existing user.
        if(messageText == null || messageText.length()>255 || existingUser==null || messageText.isBlank()){
            return null;
        }

        return messageDAO.insertMessage(message);

    }

    public List<Message> getAllMessages(){
        return messageDAO.getAllMessages();
    }

    public Message getMessagebyID(int id){
        return messageDAO.getMessageByID(id);
    }

    public Message deleteMessage(int id){
        return messageDAO.deleteMessage(id);
    }

    public Message updateMessage(int id, String newText){
        if(newText == null || newText.length()>255 || newText.isBlank()){
            return null;
        }
        return messageDAO.updateMessage(id,newText);
    }

    public List<Message> getAllMessagesbyAccountID(int posted_by){
        return messageDAO.getAllMessagesByAccount(posted_by);
    }


    
}
