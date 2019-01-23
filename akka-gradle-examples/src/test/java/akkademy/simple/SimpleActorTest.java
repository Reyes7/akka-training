package akkademy.simple;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ActorSystem;
import akka.pattern.PatternsCS;
import akka.testkit.TestActorRef;
import akka.testkit.javadsl.TestKit;
import akka.util.Timeout;
import akkademy.simple.actor.SimpleActor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import scala.concurrent.duration.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import static org.junit.Assert.assertEquals;

public class SimpleActorTest {

    ActorSystem actorSystem;

    @Before
    public void setUp() throws Exception {
        actorSystem = ActorSystem.create();
    }

    @After
    public void tearDown() throws Exception {
        TestKit.shutdownActorSystem(actorSystem);
    }

    @Test
    public void testTellHello() {
        Props props = Props.create(SimpleActor.class);
        TestActorRef<SimpleActor> testActorRef = TestActorRef.create(actorSystem, props, "simple-actor-test");

        // access actor methods and state
        // underlyingActor() open up the state of the
        SimpleActor simpleActor = testActorRef.underlyingActor();

        // actor getGreeting();
        testActorRef.tell("Hello", null);
    }

    @Test
    public void tellAskHell() {
        Props props = Props.create(SimpleActor.class);
        TestActorRef<SimpleActor> testActorRef = TestActorRef.create(actorSystem, props, "simple-actor-test");
        CompletableFuture<Object> future = PatternsCS
                .ask(testActorRef, "Hello", 1000)
                .toCompletableFuture();

        Object receivedMessage = future.join();
        assertEquals("World !", receivedMessage);
    }

    @Test
    public void testTellWithProbe() {
        TestKit probe = new TestKit(actorSystem);
        Props props = Props.create(SimpleActor.class);
        ActorRef actorRef = actorSystem.actorOf(props);

        //send two messages
        actorRef.tell("Hello", probe.getRef());
        actorRef.tell("Hello", probe.getRef());

        //so received two replies
        probe.expectMsgAllOf(probe.duration("1 seconds"), "World !");
        probe.expectMsgAllOf(probe.duration("1 seconds"), "World !");
    }

    @Test(timeout = 2_000)
    public void countTest() {
        Props props = Props.create(SimpleActor.class);
        TestActorRef<SimpleActor> testActorRef = TestActorRef.create(actorSystem, props, "simple-actor-test");
        SimpleActor simpleActor = testActorRef.underlyingActor();

        int iterations = 5;
        for(int i = 0; i<iterations; i++){
            testActorRef.tell("Status", null);
        }

        //ask for count
        Timeout timeout = new Timeout(Duration.create(5, "seconds"));
        CompletionStage<Object> countCompletionStage = PatternsCS.ask(testActorRef, "Count", timeout);
        CompletableFuture<Object> ask = countCompletionStage.toCompletableFuture();
        assertEquals(5, ask.join());
    }
}
