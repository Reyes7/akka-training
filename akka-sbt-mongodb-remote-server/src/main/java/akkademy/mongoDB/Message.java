package akkademy.mongoDB;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable{
    private final String id;
    private final Date sendDate;
    private final String message;

    public Message(String id, Date sendDate, String message) {
        this.id = id;
        this.sendDate = sendDate;
        this.message = message;
    }

    public Message(DBObject dbObject){
        this.id = (String) dbObject.get("id");
        this.sendDate = (Date) dbObject.get("sendDate");
        this.message = (String) dbObject.get("message");
    }

    public DBObject covertToDbObject(){
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start();
        builder.append("id", this.id);
        builder.append("sendDate", this.sendDate);
        builder.append("message", this.message);
        return builder.get();
    }

    public String getId() {
        return id;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public String getMessage() {
        return message;
    }
}
