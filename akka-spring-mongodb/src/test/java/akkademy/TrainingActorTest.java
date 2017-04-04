package akkademy;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akkademy.config.SpringExtension;
import akkademy.mongoDB.Message;
import akkademy.mongoDB.MessageService;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TrainingActorTest {

    @Autowired
    MessageService messageService;

    @Autowired
    ActorSystem actorSystem;

    @Autowired
    private SpringExtension springExtension;

    @After
    public void tearDown(){
        messageService.deleteAllMessages();
    }

    @Test
    public void actorTest(){
        ActorRef trainingActor = actorSystem.actorOf(springExtension.createConfigurationForActor("trainingActor"), "training-actor");
        trainingActor.tell(new Message("message", new Date(), "Hello Message from Actor !"), ActorRef.noSender());
        Message message = messageService.getMessageById("message");
        assertEquals(message.getMessage(), "Hello Message from Actor !");
    }
}