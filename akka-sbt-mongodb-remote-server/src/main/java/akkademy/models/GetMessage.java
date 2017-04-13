package akkademy.models;

import java.io.Serializable;

public class GetMessage implements Serializable{
    private final String id;

    public GetMessage(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
