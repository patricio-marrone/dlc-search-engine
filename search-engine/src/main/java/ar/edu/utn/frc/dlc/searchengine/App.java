package ar.edu.utn.frc.dlc.searchengine;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.DB;

import java.net.UnknownHostException;

/**
 * Hello world!
 *
 */
public class App {
  public static void main(String[] args) throws UnknownHostException {
    System.out.println("Hello World!");
    App app = new App();
    DB index = app.connectMongo("FullTextIndex");
    System.out.println(index);

    DBCollection words = index.getCollection("words");


    BasicDBObject document = new BasicDBObject();
    document.put("name", "mkyong");
    document.put("age", 30);
    // document.put("createdDate", new Date());
    // table.insert(document);
    //
    // /**** Find and display ****/
    // BasicDBObject searchQuery = new BasicDBObject();
    // searchQuery.put("name", "mkyong");
    //
    // DBCursor cursor = table.find(searchQuery);
    //
    // while (cursor.hasNext()) {
    // System.out.println(cursor.next());
    // }
    //
    // /**** Update ****/
    // // search document where name="mkyong" and update it with new values
    // BasicDBObject query = new BasicDBObject();
    // query.put("name", "mkyong");
    //
    // BasicDBObject newDocument = new BasicDBObject();
    // newDocument.put("name", "mkyong-updated");
    //
    // BasicDBObject updateObj = new BasicDBObject();
    // updateObj.put("$set", newDocument);
    //
    // table.update(query, updateObj);
    //
    // /**** Find and display ****/
    // BasicDBObject searchQuery2
    // = new BasicDBObject().append("name", "mkyong-updated");
    //
    // DBCursor cursor2 = table.find(searchQuery2);
    //
    // while (cursor2.hasNext()) {
    // System.out.println(cursor2.next());
    // }

  }

  public DB connectMongo(String database) throws UnknownHostException {
    // To directly connect to a single MongoDB server (note that this will
    // not auto-discover the primary even
    // if it's a member of a replica set:
    MongoClient mongoClient = new MongoClient("localhost", 27017);
    DB db = mongoClient.getDB(database);
    return db;
  }
}
