package akkademy.simple.actor;

import akka.actor.UntypedAbstractActor;
import lombok.extern.java.Log;

@Log
public class SimpleActor extends UntypedAbstractActor {

    private static final String GREETING = "World !";
    private int count = 0;

    @Override
    public void onReceive(Object message) {
        log.info("Received message: "+message);
        log.info("Received message: from thread: "+Thread.currentThread().getName());

        if("Hello".equals(message)){
            getSender().tell(GREETING, getSelf());
            log.info("Send message: "+GREETING);
        } else if("Count".equals(message)){
            getSender().tell(count, getSelf());
            log.info("Send message: "+GREETING);
        } else if("Stop".equals(message)){
            getContext().stop(getSelf());
        }
        count++;
    }

    public int getCount() {
        return count;
    }
}
