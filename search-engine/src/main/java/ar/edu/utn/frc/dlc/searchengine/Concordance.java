package ar.edu.utn.frc.dlc.searchengine;

import java.io.Serializable;

public interface Concordance extends Iterable<ConcordanceEntry>, Serializable {
  public void put(String word) throws ConcordanceTooLargeException;
  public ConcordanceEntry get(String word);
  
  public long getWordCount();
  public int size();
  public void setDocument(Document document);
  public Document getDocument();
  public void put(String word, boolean inTitle, boolean inAuthor) throws ConcordanceTooLargeException;
}
