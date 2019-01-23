package akkademy.custom;

import akkademy.custom.actor.CustomActor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

class CustomActorSystem {

    private final static ExecutorService executorService = Executors.newCachedThreadPool();

    static <T> CustomActor<T> spawn(BiConsumer<CustomActor<T>, T> behaviourHandler,
                                    BiConsumer<CustomActor<T>, Throwable> errorHandler) {

        CustomActor<T> actor = CustomActor.create(behaviourHandler, errorHandler);
        executorService.submit(actor);
        return actor;
    }

    static void shutdown() {
        executorService.shutdownNow();
    }
}
