package ar.edu.utn.frc.dlc.searchengine.sqlite;
//package ar.edu.utn.frc.dlc.searchengine.sqlite;
//
//import java.io.BufferedInputStream;
//import java.io.ByteArrayOutputStream;
//import java.io.DataInputStream;
//import java.io.DataOutputStream;
//import java.io.EOFException;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.sql.Blob;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.List;
//
//import com.almworks.sqlite4java.SQLite;
//import com.almworks.sqlite4java.SQLiteConnection;
//import com.almworks.sqlite4java.SQLiteException;
//import com.almworks.sqlite4java.SQLiteStatement;
//
//import ar.edu.utn.frc.dlc.searchengine.Document;
//
//public class SqliteDAL {
//  
//  private static int lastDocument = 0;
//  private static final String DOCUMENT = "DOCUMENT";
//  private static final String WORD = "WORD";
//  private static final String POSTING = "POSTING";
//  private static final String DOCUMENT_CREATE_SQL = "CREATE TABLE IF NOT EXISTS DOCUMENT "
//      + "(URL TEXT PRIMARY KEY, NAME TEXT NOT NULL, CODE TEXT NOT NULL)";
//  
//  private static final String WORD_CREATE_SQL = "CREATE TABLE IF NOT EXISTS WORD "
//      + "(NAME TEXT PRIMARY KEY, POSTINGS_LENGTH INT, POSTINGS BLOB)";
//  private static final String POSTING_CREATE_SQL = "CREATE TABLE IF NOT EXISTS POSTING "
//      + "(WORD_ID INT, SEQUENCE INT, NAME TEXT NOT NULL, URL TEXT NOT NULL, POSTINGS BLOB, PRIMARY KEY (WORD_ID, SEQUENCE))";
//
//  
//  SQLiteConnection connection = null;
//
//  public SQLiteConnection open() throws SQLiteException {
//    if (connection == null) {
//      connection = new SQLiteConnection(new File("search-engine.db"));
//      connection.open();
//    }
//    connection.exec("BEGIN"); 
//    return connection;
//  }
//
//  public boolean createDatabase() throws SQLiteException {
//    connection.exec(DOCUMENT_CREATE_SQL);
//    connection.exec(WORD_CREATE_SQL);
//    connection.exec(POSTING_CREATE_SQL);
//    return true;
//  }
//
//  public void dropDatabase() throws SQLiteException  {
//    String[] tables = {WORD, POSTING, DOCUMENT};
//    for (String table : tables) {
//      connection.exec("DROP TABLE IF EXISTS " + table);
//    }
//  }
//  
//  public void close() throws SQLException {
//   this.connection.dispose();
//   this.connection = null;
//  }
//  
//  public Document addDocument(Document document) throws SQLiteException {
//    String sql = "INSERT INTO " + DOCUMENT + "(URL, NAME, CODE) VALUES (?URL, ?NAME, ?CODE)";
//    SQLiteStatement statement = connection.prepare(sql);
//    statement.bind("URL", document.getPath());
//    statement.bind("NAME", document.getPath());
//    statement.bind("CODE", ++lastDocument);
//    document.setId(lastDocument);
//    statement.step();
//    return document;
//  }
//
//  public void flushPostings(Word word, List<PostingEntry> postingEntries) throws IOException, SQLiteException {
//    String sql = "SELECT NAME, POSTINGS FROM WORD WHERE NAME = ?";
//    SQLiteStatement statement = connection.prepare(sql);
//    statement.bind(1, word.getWord());
//    if (statement.step()) {
//      byte[] storedEntries = statement.columnBlob(2);
//      byte[] serializedEntries = serializePostings(postingEntries);
//      ByteArrayOutputStream blobBytes = new ByteArrayOutputStream(storedEntries.length + serializedEntries.length);
//      blobBytes.write(storedEntries);
//      blobBytes.write(serializedEntries);
//      
//      SQLiteStatement updateStatement = connection.prepare("UPDATE WORD SET POSTINGS_LENGTH = ?POSTING_LENGTH, POSTINGS = ?POSTINGS WHERE NAME = ?NAME");
//      
//      updateStatement.bind("POSTING_LENGTH", word.getCount());
//      updateStatement.bind("NAME", word.getWord());
//      updateStatement.bind("POSTINGS", blobBytes.toByteArray());
//      updateStatement.stepThrough();
//    }  else {
//      byte[] serializedEntries = serializePostings(postingEntries);
//      String insertSql = "INSERT OR REPLACE INTO WORD (NAME, POSTINGS_LENGTH, POSTINGS) VALUES (?, ?, ?)";
//      SQLiteStatement insertStatement = connection.prepare(insertSql);
//      insertStatement.bind(1, word.getWord());
//      insertStatement.bind(2, word.getCount());
//      insertStatement.bind(3, serializedEntries);
//      insertStatement.stepThrough();
//    }
//  }
//
//  private byte[] serializePostings(List<PostingEntry> entries) throws IOException {
//    ByteArrayOutputStream byteArrayStream = new ByteArrayOutputStream();
//    DataOutputStream stream = new DataOutputStream(byteArrayStream);
//    for (PostingEntry entry : entries) {
//      stream.writeInt(entry.getDocumentCode());
//      stream.writeLong(entry.getFrequency());
//    }
//    return byteArrayStream.toByteArray();
//  }
//
//  private List<PostingEntry> deserializePostings(Blob postingBytes) throws SQLException, IOException {
//    List<PostingEntry> retrievedPostings = new ArrayList<PostingEntry>(50);
//    DataInputStream stream = new DataInputStream(postingBytes.getBinaryStream());
//    while(true) {
//      try {
//        int documentCode = stream.readInt();
//        long frequency = stream.readLong();
//        PostingEntry retrievedEntry = new PostingEntry();
//        retrievedEntry.setDocumentCode(documentCode);
//        retrievedEntry.setFrequency(frequency);
//        retrievedPostings.add(retrievedEntry);
//      } catch (EOFException e) {
//        break;
//      }
//    }
//    return retrievedPostings;
//  }
//
//  public void commit() throws SQLiteException {
//    System.out.println("Commiting to database...");
//    connection.exec("COMMIT"); 
//  }
//}
