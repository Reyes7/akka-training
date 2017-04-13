package akkademy;

import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import java.util.concurrent.CompletionStage;
import static akka.pattern.Patterns.ask;
import static scala.compat.java8.FutureConverters.toJava;
import akkademy.models.GetMessage;
import akkademy.models.RemoveMessage;
import akkademy.mongoDB.Message;
import scala.concurrent.Future;

public class Client {
    private final ActorSystem actorSystem = ActorSystem.create("actor-system");
    private final ActorSelection actorSelection;

    public Client(String address){
        actorSelection = actorSystem.actorSelection("akka.tcp://actor-system@" + address + "/user/db-actor");
    }

    public CompletionStage saveMessage(Message message) {
        return toJava(ask(actorSelection, message, 2000));
    }

    public CompletionStage<Message> getMessage(String id){
        Future futureMessage = ask(actorSelection, new GetMessage(id), 2000);
        CompletionStage<Message> messageCompletionStage = toJava(futureMessage);
        return messageCompletionStage;
    }

    public CompletionStage removeMessage(String id){
        return toJava(ask(actorSelection, new RemoveMessage(id), 2000));
    }
}
