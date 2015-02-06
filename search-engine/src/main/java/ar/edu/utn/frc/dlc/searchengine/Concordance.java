package ar.edu.utn.frc.dlc.searchengine;

import java.util.Iterator;

public interface Concordance extends Iterable<ConcordanceEntry> {
  public void put(String word) throws ConcordanceTooLargeException;
  public Long get(String word);
  
  public long getWordCount();
  public int size();
  public void setDocument(Document document);
  public Document getDocument();
}
