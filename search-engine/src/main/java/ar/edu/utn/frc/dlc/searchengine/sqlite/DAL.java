package ar.edu.utn.frc.dlc.searchengine.sqlite;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.List;

import ar.edu.utn.frc.dlc.searchengine.Document;



public interface DAL {

  public abstract Connection open();

  public abstract boolean createDatabase() throws SQLException;

  public abstract void dropDatabase() throws SQLException;

  public abstract void close() throws SQLException;

  public abstract Document addDocument(Document document) throws SQLException;

  public abstract void flushPostings(Word word, List<PostingEntry> postingEntries)
      throws SQLException, IOException;

  public abstract void commit() throws SQLException;

  public abstract Iterator<PostingEntry> getPostingIterator(String key);

}
