package akkademy.mongoDB;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class DatabaseConfig {

    private static DB connectToDatabase(String databaseName) {
        MongoClient mongoClient = new MongoClient();
        return mongoClient.getDB(databaseName);
    }

    static DBCollection getAccessToTable(String databaseName, String table) {
        DB testDb = connectToDatabase(databaseName);
        return testDb.getCollection(table);
    }
}
