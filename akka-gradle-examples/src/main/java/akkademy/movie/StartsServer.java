package akkademy.movie;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.pattern.PatternsCS;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import akka.util.Timeout;
import akkademy.movie.actor.StorageActor;
import akkademy.movie.model.InfoMovieMessage;
import akkademy.movie.model.InfoReplyMovieMessage;
import akkademy.movie.model.ViewMovieMessage;
import scala.concurrent.duration.Duration;
import java.io.IOException;
import java.util.concurrent.CompletionStage;

public class StartsServer extends AllDirectives {

    private ActorSystem actorSystem;
    private ActorRef storageActor;

    private StartsServer() {
        this.actorSystem = ActorSystem.create();
        storageActor = actorSystem.actorOf(Props.create(StorageActor.class), "storage-actor");
    }

    public static void main(String[] args) throws IOException {
        final StartsServer server = new StartsServer();
        server.run();
    }

    private Route createRoute(){
        Route watchRoute = parameter("movie", movieName -> {
            storageActor.tell(new ViewMovieMessage(movieName), null);
            return complete("OK");
        });

        Route infoRoute = parameter("movie", movieName ->
            onSuccess(() -> {
                Timeout timeout = new Timeout(Duration.create(5, "seconds"));
                return PatternsCS.ask(storageActor, new InfoMovieMessage(movieName), timeout);
                }, extraction -> complete(((InfoReplyMovieMessage) extraction).getViews().toString())
            )
        );

        return get(() -> concat(
                pathSingleSlash(() -> complete(
                        HttpEntities.create(ContentTypes.TEXT_HTML_UTF8, "<html><body>;)</body></html>"))),
                path("watch", () -> watchRoute),
                path("info", () -> infoRoute)
        ));
    }

    private void run() throws IOException {
        final Http http = Http.get(actorSystem);
        final ActorMaterializer materializer = ActorMaterializer.create(actorSystem);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow
                = this.createRoute().flow(actorSystem, materializer);
        final CompletionStage<ServerBinding> binding
                = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);

        System.out.println("Type RETURN to exit");
        System.in.read();

        binding
                .thenCompose(ServerBinding::unbind)
                .thenAccept(unbound -> actorSystem.terminate());
    }
}
