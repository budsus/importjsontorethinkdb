package id.budsus.mongodb;

import java.io.IOException;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;

import id.budsus.files.FileTools;

public class MongoDBTools {
    private String uri = "mongodb://budsus:simbah@192.168.0.107:27017/codesearchnet";
    private String logFile;

    public void setLogFile(String file) {
        this.logFile = file;
    }
    public String getLogFile() {
        return this.logFile;
    }

    public void insertDoc(Document doc) throws IOException  {
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("codesearchnet");
            MongoCollection<Document> collection = database.getCollection("train");
            try {
                InsertOneResult result = collection.insertOne(doc);
                System.out.println(".");
                // System.out.println("Success! Inserted document id: " + result.getInsertedId());
            } catch (MongoException me) {
                FileTools.writeStringToFile(getLogFile(), doc.getString("func_name") + ", " + doc.getString("language") + ", " + doc.getString("sha"));
            }
        }
    }
}
