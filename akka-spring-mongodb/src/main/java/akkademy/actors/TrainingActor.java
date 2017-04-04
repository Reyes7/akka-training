package akkademy.actors;

import akka.actor.AbstractActor;
import akka.japi.pf.ReceiveBuilder;
import akkademy.mongoDB.Message;
import akkademy.mongoDB.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("trainingActor")
@Scope("prototype")
public class TrainingActor extends AbstractActor{

    private final Logger LOGGER = LoggerFactory.getLogger(TrainingActor.class.getName());

    @Autowired
    MessageService messageService;

    private TrainingActor(){
        receive(ReceiveBuilder.match(Message.class, message ->{
            LOGGER.info("Received set request - id: {} message: {}",message.getId(), message.getMessage());
            messageService.addMessage(message);
        })
                .matchAny(unknownMessage -> LOGGER.info("received unknown message {}", unknownMessage))
                .build());

    }
}
