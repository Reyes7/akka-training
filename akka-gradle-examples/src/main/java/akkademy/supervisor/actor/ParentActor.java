package akkademy.supervisor.actor;

import akka.actor.*;
import akkademy.supervisor.model.InputMessage;
import lombok.extern.java.Log;

@Log
public class ParentActor extends UntypedAbstractActor {

    private OneForOneStrategy supervisorStrategy;
    private ActorRef childActor;

    public ParentActor(OneForOneStrategy supervisorStrategy) {
        Props props = Props.create(ChildActor.class);
        this.childActor =  getContext().actorOf(props);
        this.supervisorStrategy = supervisorStrategy;
    }

    @Override
    public void onReceive(Object message){
        log.info("Parent Actor received message: "+message.toString());

        if(message instanceof InputMessage) {
            childActor.tell(message, getSender());
        }else if(message instanceof String){
            getSelf().tell(message, null);
        }
    }

    @Override
    public SupervisorStrategy supervisorStrategy() {
        return supervisorStrategy;
    }
}
