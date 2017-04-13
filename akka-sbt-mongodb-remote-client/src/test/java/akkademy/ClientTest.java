package akkademy;

import akkademy.mongoDB.Message;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

public class ClientTest {

    Client client = new Client("127.0.0.1:2323");

    @Test
    public void remoteIntegrationTest() throws Exception {
        client.saveMessage(new Message("remote", new Date(), "Hello from remote ;)"));
        Message message = client.getMessage("remote").toCompletableFuture().get();
        Assert.assertEquals("Hello from remote ;)",message.getMessage());

        client.removeMessage("remote");
        Message removedMessage = client.getMessage("remote").toCompletableFuture().get();
        Assert.assertEquals("Not Found", removedMessage.getMessage());
    }
}