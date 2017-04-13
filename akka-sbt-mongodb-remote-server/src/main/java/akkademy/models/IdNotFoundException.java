package akkademy.models;

import java.io.Serializable;

public class IdNotFoundException extends Exception implements Serializable{
    private final String id;

    public IdNotFoundException(String id){
        this.id = id;
    }
}
