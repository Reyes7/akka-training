package akkademy.mongoDB;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageRepository messageRepository;

    public void addMessage(Message message){
        messageRepository.save(message);
    }

    public Message getMessageById(String id){
        return messageRepository.findOne(id);
    }

    public void deleteAllMessages(){
        messageRepository.deleteAll();
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }
}
