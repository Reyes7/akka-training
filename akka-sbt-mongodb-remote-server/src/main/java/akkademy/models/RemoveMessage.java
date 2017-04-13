package akkademy.models;

import java.io.Serializable;

public class RemoveMessage implements Serializable{
    private final String id;

    public RemoveMessage(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
