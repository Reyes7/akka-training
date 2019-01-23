package akkademy.custom.actor;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;

public class CustomActor<T> implements Runnable {

    private final Queue<T> mailbox;
    private BiConsumer<CustomActor<T>, T> actionHandler;
    private BiConsumer<CustomActor<T>, Throwable> errorHandler;

    public CustomActor(BiConsumer<CustomActor<T>, T> actionHandler, BiConsumer<CustomActor<T>, Throwable> errorHandler) {
        this.mailbox = new ConcurrentLinkedQueue();
        this.actionHandler = actionHandler;
        this.errorHandler = errorHandler;
    }

    public static <T> CustomActor<T> create(BiConsumer<CustomActor<T>, T> actionHandler, BiConsumer<CustomActor<T>, Throwable> errorHandler) {
        return new CustomActor<>(actionHandler, errorHandler);
    }

    /**
     * add message to mailbox
     */
    public void send(T message){
        mailbox.offer(message);
    }

    /**
     * read and process messages in mailbox
     */
    @Override
    public void run() {
        while(true) {
            T message = mailbox.poll();
            if (message != null) {
                try {
                    actionHandler.accept(this, message);
                } catch (Exception e) {
                    errorHandler.accept(this, e);
                }
            }
        }
    }
}
