package akkademy.movie.actor;

import akka.actor.UntypedAbstractActor;
import akkademy.movie.model.InfoMovieMessage;
import akkademy.movie.model.InfoReplyMovieMessage;
import akkademy.movie.model.ViewMovieMessage;
import lombok.extern.java.Log;
import java.util.HashMap;
import java.util.Map;

@Log
public class StorageActor extends UntypedAbstractActor {

    private Map<String, Integer> movieViews = new HashMap<>();

    public StorageActor() {
        log.info("StorageActor constructor");
    }

    @Override
    public void onReceive(Object message) {
        log.info("Received message: "+message);
        if(message instanceof ViewMovieMessage){
            ViewMovieMessage viewMovieMessage = (ViewMovieMessage) message;
            String movieName = viewMovieMessage.getMovie();
            movieViews.merge(movieName, 1, (value, one) -> value + 1);
        }else if(message instanceof InfoMovieMessage){
            InfoMovieMessage infoMovieMessage = (InfoMovieMessage) message;
            String movieName = infoMovieMessage.getMovie();
            Integer count = movieViews.getOrDefault(movieName, 0);
            getSender().tell(new InfoReplyMovieMessage(movieName, count), getSelf());
        }
    }
}