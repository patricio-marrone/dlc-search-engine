package ar.edu.utn.frc.dlc.searchengine;

import java.net.UnknownHostException;
import java.util.Iterator;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class PersistenceHandler {
  private DB dataBase;
  
  public PersistenceHandler() throws UnknownHostException  {
    dataBase = connectMongo("search-engine");
  }
  
  public void persistConcordance(Concordance concordance) throws UnknownHostException {

    DBCollection words = dataBase.getCollection("words");

     BulkWriteOperation builder = words.initializeUnorderedBulkOperation();
    for (ConcordanceEntry entry : concordance) {
      BasicDBObject posting = new BasicDBObject();
      posting.put("word", entry.getWord());
      posting.put("frequency", entry.getFrequency());
      posting.put("document", concordance.getDocument().getPath());
      builder.insert(posting);
    }
    builder.execute();
  }
  
  private DB connectMongo(String database) throws UnknownHostException {
    // To directly connect to a single MongoDB server (note that this will
    // not auto-discover the primary even
    // if it's a member of a replica set:
    MongoClient mongoClient = new MongoClient("localhost", 27017);
    DB db = mongoClient.getDB(database);
    return db;
  }
}
