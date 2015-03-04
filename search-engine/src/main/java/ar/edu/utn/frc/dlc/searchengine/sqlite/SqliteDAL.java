package ar.edu.utn.frc.dlc.searchengine.sqlite;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.OperationNotSupportedException;

import ar.edu.utn.frc.dlc.searchengine.Document;

public class SqliteDAL implements DAL {
  
  private static int lastDocument = 0;
  private static final String DOCUMENT = "DOCUMENT";
  private static final String WORD = "WORD";
  private static final String POSTING = "POSTING";
  private static final String DOCUMENT_CREATE_SQL = "CREATE TABLE IF NOT EXISTS DOCUMENT "
      + "(URL TEXT PRIMARY KEY, NAME TEXT NOT NULL, CODE TEXT NOT NULL)";
  
  private static final String WORD_CREATE_SQL = "CREATE TABLE IF NOT EXISTS WORD "
      + "(NAME TEXT PRIMARY KEY, POSTINGS_LENGTH INT, POSTINGS BLOB)";
  private static final String POSTING_CREATE_SQL = "CREATE TABLE IF NOT EXISTS POSTING "
      + "(WORD_ID INT, SEQUENCE INT, NAME TEXT NOT NULL, URL TEXT NOT NULL, POSTINGS BLOB, PRIMARY KEY (WORD_ID, SEQUENCE))";

  
  Connection connection = null;

  /* (non-Javadoc)
   * @see ar.edu.utn.frc.dlc.searchengine.sqlite.DAL#open()
   */
  public Connection open() {
    if (connection == null) {
      try {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:searc-engine.db");
        connection.setAutoCommit(false);
      } catch (Exception e) {
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(0);
      }
    }
    return connection;
  }

  /* (non-Javadoc)
   * @see ar.edu.utn.frc.dlc.searchengine.sqlite.DAL#createDatabase()
   */
  public boolean createDatabase() throws SQLException {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate(DOCUMENT_CREATE_SQL);
    stmt.executeUpdate(WORD_CREATE_SQL);
    stmt.executeUpdate(POSTING_CREATE_SQL);
    stmt.close();

    
    return true;
  }

  /* (non-Javadoc)
   * @see ar.edu.utn.frc.dlc.searchengine.sqlite.DAL#dropDatabase()
   */
  public void dropDatabase() throws SQLException {
    String[] tables = {WORD, POSTING, DOCUMENT};
    Statement statement = connection.createStatement();
    for (String table : tables) {
      statement.execute("DROP TABLE IF EXISTS " + table);
    }
  }
  
  /* (non-Javadoc)
   * @see ar.edu.utn.frc.dlc.searchengine.sqlite.DAL#close()
   */
  public void close() throws SQLException {
   this.connection.close();
   this.connection = null;
  }
  
  /* (non-Javadoc)
   * @see ar.edu.utn.frc.dlc.searchengine.sqlite.DAL#addDocument(ar.edu.utn.frc.dlc.searchengine.Document)
   */
  public Document addDocument(Document document) throws SQLException {
    String sql = "INSERT INTO " + DOCUMENT + "(URL, NAME, CODE) VALUES (?, ?, ?)";
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setString(1, document.getPath());
    statement.setString(2, document.getPath());
    statement.setInt(3, ++lastDocument);
    document.setId(lastDocument);
    statement.execute();
    return document;
  }

  /* (non-Javadoc)
   * @see ar.edu.utn.frc.dlc.searchengine.sqlite.DAL#flushPostings(ar.edu.utn.frc.dlc.searchengine.sqlite.Word, java.util.List)
   */
  public void flushPostings(Word word, List<PostingEntry> postingEntries) throws SQLException, IOException {
    String sql = "SELECT NAME, POSTINGS FROM WORD WHERE NAME = ?";
    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setString(1, word.getWord());
    ResultSet resultset = statement.executeQuery();
    if (resultset.next()) {
      byte[] storedEntries = resultset.getBytes(2);
      byte[] serializedEntries = serializePostings(postingEntries);
      ByteArrayOutputStream blobBytes = new ByteArrayOutputStream(storedEntries.length + serializedEntries.length);
      blobBytes.write(storedEntries);
      blobBytes.write(serializedEntries);
      
      String updateSql = " UPDATE WORD SET POSTINGS_LENGTH = ?, POSTINGS = ? WHERE NAME = ?";
      PreparedStatement updateStatement = connection.prepareStatement(updateSql);
      updateStatement.setInt(1, word.getCount());
      updateStatement.setBytes(2, blobBytes.toByteArray());
      updateStatement.setString(3, word.getWord());
      updateStatement.execute();
    }  else {
      byte[] serializedEntries = serializePostings(postingEntries);
      String insertSql = "INSERT OR REPLACE INTO WORD (NAME, POSTINGS_LENGTH, POSTINGS) VALUES (?, ?, ?)";
      PreparedStatement insertStatement = connection.prepareStatement(insertSql);
      insertStatement.setString(1, word.getWord());
      insertStatement.setInt(2, word.getCount());
      insertStatement.setBytes(3, serializedEntries);
      insertStatement.execute();
    }
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

  private List<PostingEntry> deserializePostings(Blob postingBytes) throws SQLException, IOException {
    List<PostingEntry> retrievedPostings = new ArrayList<PostingEntry>(50);
    DataInputStream stream = new DataInputStream(postingBytes.getBinaryStream());
    while(true) {
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

  /* (non-Javadoc)
   * @see ar.edu.utn.frc.dlc.searchengine.sqlite.DAL#commit()
   */
  public void commit() throws SQLException {
    System.out.println("Commiting to database...");
    connection.commit(); 
  }

  public Iterator<PostingEntry> getPostingIterator(String key) {
    // TODO Auto-generated method stub
    throw new RuntimeException("Not implemented");
  }

  public Map<Integer, Document> getDocumentMap() {
    throw new RuntimeException("Not implemented");
  }

  public Integer getWordPostingCount(String key) {
    return null;
  }

  public Integer getDocumentCount() {
    // TODO Auto-generated method stub
    return null;
  }
}
