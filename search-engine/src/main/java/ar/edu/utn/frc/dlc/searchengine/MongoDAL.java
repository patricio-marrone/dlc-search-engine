package ar.edu.utn.frc.dlc.searchengine;

import java.io.ByteArrayInputStream;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.BulkUpdateRequestBuilder;
import com.mongodb.BulkWriteOperation;
import com.mongodb.BulkWriteRequestBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
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
  private static Map<Integer, Document> documentMap = null;

  public synchronized Connection open() {
    MongoClient mongoClient;
    try {
      mongoClient = new MongoClient("localhost", 27017);
      dataBase = mongoClient.getDB("search-engine");
      words = dataBase.getCollection("words");
      documents = dataBase.getCollection("documents");
      this.commit();
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
    mongoDocument.put("title", document.getTitle());
    mongoDocument.put("author", document.getAuthor());
    mongoDocument.put("code", document.getId());
    documentBuilder.insert(mongoDocument);
    documentHasOperations = true;
    return document;
  }

  public synchronized void flushPostings(Word word, List<PostingEntry> postingEntries) throws SQLException,
      IOException {
    BasicDBObject posting = new BasicDBObject();

    wordHasOperations = true;
    byte[] postingBytes = serializePostings(postingEntries);
    posting.put("$set", new BasicDBObject("word", word.getWord()));
    posting.put("$set", new BasicDBObject("count", word.getCount()));
    posting.put("$push", new BasicDBObject("postings", postingBytes));
    BulkWriteRequestBuilder requestBuilder = wordBuilder.find(new BasicDBObject("word", word.getWord()));
    requestBuilder.upsert().update(posting);
//    posting.put("word", word.getWord());
//    posting.put("count", word.getCount());
//    posting.put("postings", postingBytes);
//    wordBuilder.insert(posting);
  }

  private byte[] serializePostings(List<PostingEntry> entries) throws IOException {
    ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
    DataOutputStream stream = new DataOutputStream(byteArrayStream);
    for (PostingEntry entry : entries) {
      stream.writeInt(entry.getDocumentCode());
      stream.writeLong(entry.getFrequency());
      int flags = 0x00;
      if (entry.inTitle) {
        flags = (flags | 0x01);
      }
      if (entry.inAuthor) {
        flags = (flags | 0x02);
      }
      stream.writeByte(flags);
    }
    return byteArrayStream.toByteArray();
  }

  private List<PostingEntry> deserializePostings(byte[] postingBytes) throws IOException {
    List<PostingEntry> retrievedPostings = new ArrayList<PostingEntry>(50);
    DataInputStream stream = new DataInputStream(new ByteArrayInputStream(postingBytes));
    while (true) {
      try {
        int documentCode = stream.readInt();
        long frequency = stream.readLong();
        byte flags = stream.readByte();
        PostingEntry retrievedEntry = new PostingEntry();
        retrievedEntry.setDocumentCode(documentCode);
        retrievedEntry.setFrequency(frequency);
        if ((flags & 1) == 1) {
          retrievedEntry.inTitle = true;
        }
        if ((flags & 2) == 1) {
          retrievedEntry.inAuthor = true;
        }
        if (MongoDAL.documentMap == null) {
          this.getDocumentMap();
        }
        retrievedEntry.setDocument(documentMap.get(documentCode));
        retrievedPostings.add(retrievedEntry);
      } catch (EOFException e) {
        break;
      }
    }
    return retrievedPostings;
  }

  public void commit() {
    System.out.println("Committing! ");
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

  public Iterator<PostingEntry> getPostingIterator(String key) {
    DBObject query = new BasicDBObject("word", key);
    DBObject fields = new BasicDBObject("postings", 1);
    fields.put( "_id", 0);
    DBCursor cursor = words.find(query, fields);
    List<PostingEntry> entries = new ArrayList<PostingEntry>();
    while (cursor.hasNext()) {
      DBObject result = cursor.next();
      BasicDBList postings = (BasicDBList) result.get("postings");
      for (Object o : postings) {
          byte[] postingBytes = (byte[]) o;
          try {
            entries.addAll(this.deserializePostings(postingBytes));
          } catch (IOException e) {
            e.printStackTrace();
          }
      }
    }
    return entries.iterator();
  }

  public Map<Integer, Document> getDocumentMap() {
    if (MongoDAL.documentMap != null) {
      return documentMap;
    }
    
    DBCursor cursor = documents.find();
    documentMap = new HashMap<Integer, Document>(cursor.size());

    while (cursor.hasNext()) {
      DBObject result = cursor.next();
      Document document = new Document();
      document.setId((Integer)result.get("code"));
      document.setPath((String) result.get("url"));
      document.setAuthor((String) result.get("author"));
      document.setTitle((String) result.get("title"));
      documentMap.put(document.getId(), document);
    }
    return documentMap;
  }

  public Integer getWordPostingCount(String key) {
    DBObject query = new BasicDBObject("word", key);
    DBObject fields = new BasicDBObject("count", 1);
    DBCursor cursor = words.find(query, fields);
    if (cursor.hasNext()) {
      DBObject result = cursor.next();
      return (Integer) result.get("count");
    }
    return 0;
  }
}
