package akkademy.mongoDB;

import com.mongodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Optional;

public class MessageRepository {

    private Logger LOGGER = LoggerFactory.getLogger(MessageRepository.class);
    private DBCollection dbCollection = DatabaseConfig.getAccessToTable("test", "message");

    public void addMessage(Message message) {
        DBObject dbObject = message.covertToDbObject();
        dbCollection.insert(dbObject);
        LOGGER.info("Added message to database");
    }

    public Message getMessage(String id) {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("id", id);
        DBObject dbObject = dbCollection.findOne(basicDBObject);
        return Optional.ofNullable(dbObject)
                .map(o -> {
                    return new Message(dbCollection.findOne(basicDBObject));
                })
                .orElseGet(() -> {
                    return new Message("-1", new Date(), "Not Found");
                });
    }

    public void removeMessage(String id){
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put("id", getMessage(id).getId());
        dbCollection.remove(basicDBObject);
        LOGGER.info("Removed message on id: {}",id);
    }

    public void removeAllMessages(){
        DBCursor dbCursor = dbCollection.find();
        while (dbCursor.hasNext()) {
            dbCollection.remove(dbCursor.next());
        }
        LOGGER.info("Removed all messages from database");
    }
}
