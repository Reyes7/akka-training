package akkademy;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akkademy.actors.MongoDbActor;

public class Application {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create("actor-system");
        actorSystem.actorOf(Props.create(MongoDbActor.class), "db-actor");
    }
}
