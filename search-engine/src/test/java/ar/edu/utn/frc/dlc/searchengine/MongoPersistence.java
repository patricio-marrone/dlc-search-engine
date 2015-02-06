package ar.edu.utn.frc.dlc.searchengine;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.MongoClient;

public class MongoPersistence {
  public void storeConcordance() {

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
