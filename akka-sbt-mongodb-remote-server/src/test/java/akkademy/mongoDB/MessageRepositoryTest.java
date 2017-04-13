package akkademy.mongoDB;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

public class MessageRepositoryTest {

    MessageRepository messageRepository = new MessageRepository();

    @After
    public void tearDown() throws Exception {
        messageRepository.removeAllMessages();
    }

    @Test
    public void addMessage() throws Exception {
        Message testMessage = new Message("sbt-message",new Date(), "Hello from akka-sbt-mongodb");
        messageRepository.addMessage(testMessage);
        Message messageFromDatabase = messageRepository.getMessage("sbt-message");
        Assert.assertEquals("Hello from akka-sbt-mongodb", messageFromDatabase.getMessage());
    }

    @Test
    public void removeMessage() throws Exception{
        Message testMessage = new Message("remove",new Date(), "I want to be removed");
        messageRepository.addMessage(testMessage);
        messageRepository.removeMessage("remove");
        Message messageFromDatabase = messageRepository.getMessage("remove");
        Assert.assertEquals("Not Found",messageFromDatabase.getMessage());
    }
}