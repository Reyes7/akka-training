package akkademy.custom;

import akkademy.custom.actor.CustomActor;
import org.junit.Before;
import org.junit.Test;
import java.util.function.BiConsumer;
import static org.mockito.Mockito.*;

public class CustomActorTest {

    private static final long TIME_OUT=500L;
    private BiConsumer<CustomActor<String>, String> actionHandler = mock(BiConsumer.class);
    private BiConsumer<CustomActor<String>, Throwable> errorHandler = mock(BiConsumer.class);
    private CustomActor<String> actor;

    @Before
    public void setUp() throws Exception {
        actor = CustomActorSystem.spawn(actionHandler, errorHandler);
    }

    @Test
    public void testAcceptMessage(){
        actor.send("Hello");
        attemptUntilPasses(() -> verify(actionHandler, times(1)).accept(actor, "Hello"));
    }

    @Test
    public void testErrorMessage() {
        Exception exception = new RuntimeException("Exception !");
        doThrow(exception).when(actionHandler).accept(actor, "Hello");
        actor.send("Hello");
        attemptUntilPasses(() -> verify(errorHandler, times(1)).accept(actor, exception));
    }

    private static void attemptUntilPasses(Runnable runnable) {
        final long limit = System.currentTimeMillis() + TIME_OUT;

        AssertionError lastThrowable = null;
        while (limit > System.currentTimeMillis()) {
            try {
                runnable.run();
                lastThrowable = null;
                break;
            } catch (final AssertionError t) {
                lastThrowable = t;
            }
        }

        if (lastThrowable != null) {
            throw lastThrowable;
        }
    }
}