package ar.edu.utn.frc.dlc.searchengine.sqlite;

import java.util.Iterator;

public interface Posting {
  public Iterator<PostingEntry> getPostings(String word);
  
  public Iterator<PostingEntry> getPostings(Word word);
}
