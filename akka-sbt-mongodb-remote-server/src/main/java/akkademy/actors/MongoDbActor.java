package akkademy.actors;

import akka.actor.AbstractActor;
import akka.actor.Status;
import akka.japi.pf.ReceiveBuilder;
import akkademy.models.GetMessage;
import akkademy.models.IdNotFoundException;
import akkademy.models.RemoveMessage;
import akkademy.mongoDB.Message;
import akkademy.mongoDB.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoDbActor extends AbstractActor {

    private final Logger LOGGER = LoggerFactory.getLogger(MongoDbActor.class);

    private MessageRepository messageRepository = new MessageRepository();

    public MongoDbActor() {
        receive(ReceiveBuilder
                .match(Message.class, this::saveMessage)
                .match(GetMessage.class, this::getMessage)
                .match(RemoveMessage.class, this::removeMessage)
                .matchAny(unknown -> sender().tell(new Status.Failure(new ClassNotFoundException()), self()))
                .build());
    }

    private void saveMessage(Message request) {
        LOGGER.info("Received save message request with message: {}", request.getMessage());
        messageRepository.addMessage(request);
        sender().tell(new Status.Success("Added message to mongoDB"),self());
    }

    private void getMessage(GetMessage request) {
        LOGGER.info("Received get message request {}", request);
        Message messageFromDB = messageRepository.getMessage(request.getId());
        Object response;
        if(messageFromDB != null){
            response = messageFromDB;
        }else{
            response = new Status.Failure(new IdNotFoundException(request.getId()));
        }
        sender().tell(response, self());
    }

    private void removeMessage(RemoveMessage request) {
        LOGGER.info("Received remove message request");
        messageRepository.removeMessage(request.getId());
        sender().tell(new Status.Success("Removed message from mongoDB"),self());
    }
}
