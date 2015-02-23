package ar.edu.utn.frc.dlc.searchengine;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.BulkUpdateRequestBuilder;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteRequestBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

import ar.edu.utn.frc.dlc.searchengine.sqlite.DAL;
import ar.edu.utn.frc.dlc.searchengine.sqlite.PostingEntry;
import ar.edu.utn.frc.dlc.searchengine.sqlite.Word;

public class MongoDAL implements DAL {
  private static int lastDocument = 0;
  private DB dataBase;
  private DBCollection words;
  private DBCollection documents;
  BulkWriteOperation wordBuilder;
  BulkWriteOperation documentBuilder;
  private boolean documentHasOperations = false;
  private boolean wordHasOperations = false;

  public synchronized Connection open() {
    MongoClient mongoClient;
    try {
      mongoClient = new MongoClient("localhost", 27017);
      dataBase = mongoClient.getDB("search-engine");
      words = dataBase.getCollection("words");
      documents = dataBase.getCollection("documents");
    } catch (UnknownHostException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return null;
  }

  public synchronized boolean createDatabase() throws SQLException {
    return false;
  }

  public synchronized void dropDatabase() throws SQLException {
    words.remove(new BasicDBObject());
    documents.remove(new BasicDBObject());
  }

  public synchronized void close() throws SQLException {
    // TODO Auto-generated method stub

  }

  public synchronized Document addDocument(Document document) throws SQLException {
    BasicDBObject mongoDocument = new BasicDBObject();
    document.setId(++lastDocument);
    mongoDocument.put("url", document.getPath());
    mongoDocument.put("title", document.getPath());
    mongoDocument.put("code", document.getId());
    documentBuilder.insert(mongoDocument);
    documentHasOperations = true;
    return document;
  }

  public synchronized void flushPostings(Word word, List<PostingEntry> postingEntries) throws SQLException,
      IOException {
    BasicDBObject posting = new BasicDBObject();
    //posting.put("word", word.getWord());
    wordHasOperations = true;
    byte[] postingBytes = serializePostings(postingEntries);
    posting.put("$set", new BasicDBObject("word", word.getWord()));
    posting.put("$set", new BasicDBObject("count", word.getCount()));
    posting.put("$push", new BasicDBObject("postings", postingBytes));
    BulkWriteRequestBuilder requestBuilder = wordBuilder.find(new BasicDBObject("word", word.getWord()));
    requestBuilder.upsert().update(posting);
  }

  private byte[] serializePostings(List<PostingEntry> entries) throws IOException {
    ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayStream);
    for (PostingEntry entry : entries) {
      stream.writeInt(entry.getDocumentCode());
      stream.writeLong(entry.getFrequency());
    }
    return byteArrayStream.toByteArray();
  }

  private List<PostingEntry> deserializePostings(Blob postingBytes) throws SQLException,
      IOException {
    List<PostingEntry> retrievedPostings = new ArrayList<PostingEntry>(50);
    DataInputStream stream = new DataInputStream(postingBytes.getBinaryStream());
    while (true) {
      try {
        int documentCode = stream.readInt();
        long frequency = stream.readLong();
        PostingEntry retrievedEntry = new PostingEntry();
        retrievedEntry.setDocumentCode(documentCode);
        retrievedEntry.setFrequency(frequency);
        retrievedPostings.add(retrievedEntry);
      } catch (EOFException e) {
        break;
      }
    }
    return retrievedPostings;
  }

  public void commit() throws SQLException {
    //System.out.println("Committing! ");
    BulkWriteOperation oldWordBuilder;
    BulkWriteOperation oldDocumentBuilder;
    boolean executeWords;
    boolean executeDocuments;
    synchronized (this) {
      oldWordBuilder = wordBuilder;
      oldDocumentBuilder = documentBuilder;
      executeWords = wordHasOperations;
      executeDocuments = documentHasOperations;
      wordBuilder = words.initializeUnorderedBulkOperation();
      documentBuilder = documents.initializeUnorderedBulkOperation();
      wordHasOperations = false;
      documentHasOperations = false;
    }
    if (oldWordBuilder != null && executeWords) {
      oldWordBuilder.execute();
    }
    if (oldDocumentBuilder != null && executeDocuments) {
      oldDocumentBuilder.execute();
    }
    
  }
}
