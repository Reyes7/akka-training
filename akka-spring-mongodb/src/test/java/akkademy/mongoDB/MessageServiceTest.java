package akkademy.mongoDB;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {

    @Autowired
    MessageService messageService;

    @After
    public void tearDown(){
        messageService.deleteAllMessages();
    }

    @Test
    public void addMessage() throws Exception {
        messageService.addMessage(new Message("First message",new Date(),"Test message"));
        Message message = messageService.getMessageById("First message");
        Assert.assertEquals("Test message",message.getMessage());
    }

    @Test
    public void getAllMessages() throws Exception {
        messageService.addMessage(new Message("First message", new Date(), "Test first message"));
        messageService.addMessage(new Message("Second message", new Date(), "Test second message"));
        messageService.addMessage(new Message("Third message", new Date(), "Test third message"));

        List<Message> massages = messageService.getAllMessages();
        Assert.assertEquals(3,massages.size());
    }
}