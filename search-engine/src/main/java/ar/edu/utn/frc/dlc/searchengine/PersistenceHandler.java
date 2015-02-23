package ar.edu.utn.frc.dlc.searchengine;

import java.net.UnknownHostException;
import java.util.Iterator;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkWriteOperation;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class PersistenceHandler {
  private DB dataBase;
  DBCollection words;
  
  public PersistenceHandler() throws UnknownHostException  {
    dataBase = connectMongo("search-engine");
    words = dataBase.getCollection("words");
  }
  
  public void persistConcordance(Concordance concordance) throws UnknownHostException {

    

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

  public void clear() {
   words.remove(new BasicDBObject());
  }
  
  public void showIndex(String word) {
    BasicDBObject filter = new BasicDBObject();
    filter.put("word", word);
    BasicDBObject order = new BasicDBObject();
    order.put("frequency", -1);
    BasicDBObject keys = new BasicDBObject();
    keys.put("frequency", 1);
    keys.put("_id", 0);
    keys.put("word", 1);
    keys.put("path", 1);
    
    DBCursor results = words.find(filter, keys).batchSize(300);
    int i = 0;
    for (DBObject result : results) {
      i++;
      
      System.out.println(i + ": " + result.get("frequency") + " " + result.get("word") );
    }
  }
}
