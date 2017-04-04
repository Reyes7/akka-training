package akkademy.mongoDB;

import lombok.AccessLevel;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
@Getter(AccessLevel.PUBLIC)
public class Message {
    private final String id;
    private final Date sendDate;
    private final String message;

    public Message(String id, Date sendDate, String message) {
        this.id = id;
        this.sendDate = sendDate;
        this.message = message;
    }
}
