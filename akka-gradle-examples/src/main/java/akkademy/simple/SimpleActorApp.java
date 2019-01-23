package akkademy.simple;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akkademy.simple.actor.SimpleActor;

public class SimpleActorApp {

    public static void main(String[] args) {
        ActorSystem actorSystem = ActorSystem.create();
        ActorRef actorRef = actorSystem
                .actorOf(Props.create(SimpleActor.class), "simple-actor-demo");
        actorRef.tell("Hello !", null);
    }
}
