package akkademy.supervisor;

import akka.actor.*;
import akka.japi.pf.DeciderBuilder;
import akka.pattern.PatternsCS;
import akka.testkit.TestProbe;
import akka.testkit.javadsl.TestKit;
import akkademy.supervisor.actor.ChildActor;
import akkademy.supervisor.actor.ParentActor;
import akkademy.supervisor.model.Command;
import akkademy.supervisor.model.InputMessage;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static akka.pattern.Patterns.ask;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SupervisorActorTest {
    ActorSystem actorSystem;

    @Before
    public void setUp() throws Exception {
        actorSystem = ActorSystem.create();
    }

    @After
    public void tearDown() throws Exception {
        TestKit.shutdownActorSystem(actorSystem);
    }

    /**
     *      SupervisorStrategy.escalate()
     */

    @Test
    public void doesChildSumNumbersProperly() {
        OneForOneStrategy strategy = new OneForOneStrategy(false, DeciderBuilder.
                match(NumberFormatException.class, e -> SupervisorStrategy.escalate()).build());

        Props props = Props.create(ParentActor.class, strategy);
        ActorRef actorRef = actorSystem.actorOf(props);
        actorRef.tell(new InputMessage(Command.ADD, "1"), ActorRef.noSender());
        actorRef.tell(new InputMessage(Command.ADD, "5"), ActorRef.noSender());
        CompletableFuture<Object> future = PatternsCS
                .ask(actorRef, new InputMessage(Command.ADD, "6"), 1000)
                .toCompletableFuture();

        Object receivedMessage = future.join();
        assertEquals("Total sum: 12", receivedMessage);
    }

    /**
     *      SupervisorStrategy.restart()
     */

    @Test
    public void doesChildActorThrowsExceptionAndReturnSumAfter() throws NumberFormatException{
        OneForOneStrategy strategy = new OneForOneStrategy(false, DeciderBuilder.
                match(NumberFormatException.class, e -> SupervisorStrategy.restart()).build());

        Props props = Props.create(ParentActor.class, strategy);
        ActorRef actorRef = actorSystem.actorOf(props);
        actorRef.tell(new InputMessage(Command.ADD, "1"), ActorRef.noSender());
        actorRef.tell(new InputMessage(Command.ADD, "blablabla"), ActorRef.noSender());
        actorRef.tell(new InputMessage(Command.ADD, "4"), ActorRef.noSender());
        CompletableFuture<Object> future = PatternsCS
                .ask(actorRef, new InputMessage(Command.ADD, "6"), 1000)
                .toCompletableFuture();

        Object receivedMessage = future.join();
        assertEquals("Total sum: 10", receivedMessage);
    }

    /**
     *      SupervisorStrategy.resume()
     */

    @Test
    public void doesChildActorNotInterruptSummingWhenException(){
        OneForOneStrategy strategy = new OneForOneStrategy(false, DeciderBuilder.
                match(NumberFormatException.class, e -> SupervisorStrategy.resume()).build());

        Props props = Props.create(ParentActor.class, strategy);
        ActorRef actorRef = actorSystem.actorOf(props);
        actorRef.tell(new InputMessage(Command.ADD, "7"), ActorRef.noSender());
        actorRef.tell(new InputMessage(Command.ADD, "blablabla"), ActorRef.noSender());
        actorRef.tell(new InputMessage(Command.ADD, "3"), ActorRef.noSender());
        CompletableFuture<Object> future = PatternsCS
                .ask(actorRef, new InputMessage(Command.ADD, "6"), 1000)
                .toCompletableFuture();

        Object receivedMessage = future.join();
        assertEquals("Total sum: 16", receivedMessage);
    }

    /**
     *      SupervisorStrategy.stop()
     */

    @Test
    public void doesChildActorDiedWhenException() throws Exception {
        OneForOneStrategy strategy = new OneForOneStrategy(false, DeciderBuilder.
                match(Throwable.class, e -> SupervisorStrategy.stop()).build());

        Props parentProps = Props.create(ParentActor.class, strategy);
        ActorRef parentActorRef = actorSystem.actorOf(parentProps);

        parentActorRef.tell(new InputMessage(Command.ADD, "7"), ActorRef.noSender());
        parentActorRef.tell(new InputMessage(Command.ADD, "blablabla"), ActorRef.noSender());
        boolean isNotParentReceiveReply = PatternsCS
                .ask(parentActorRef, new InputMessage(Command.ADD, "3"), 1000)
                .toCompletableFuture()
                .completeExceptionally(new TimeoutException());
        Assert.assertTrue(isNotParentReceiveReply);
    }
}
