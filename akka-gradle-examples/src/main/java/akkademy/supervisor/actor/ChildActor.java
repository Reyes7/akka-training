package akkademy.supervisor.actor;

import akka.actor.UntypedAbstractActor;
import akkademy.supervisor.model.Command;
import akkademy.supervisor.model.InputMessage;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.List;

@Log
public class ChildActor extends UntypedAbstractActor {

    private List<Integer> numbers = new ArrayList<>();

    @Override
    public void onReceive(Object message){
        log.info("Child Actor received message: "+message.toString());

        InputMessage inputMessage = (InputMessage) message;
        Command command = inputMessage.getCommand();
        if (Command.ADD.equals(command)) {
            numbers.add(Integer.parseInt(inputMessage.getValue()));
        }

        Integer sum = 0;
        for (Integer number : numbers) {
            sum += number;
        }

        getSender().tell("Total sum: " + sum.toString(), getSelf());
    }



    @Override
    public void postStop() throws Exception {
        log.info("I will die");
    }
}
