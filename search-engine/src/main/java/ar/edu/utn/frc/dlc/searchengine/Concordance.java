package ar.edu.utn.frc.dlc.searchengine;

public interface Concordance {
  public void put(String word) throws ConcordanceTooLargeException;
  public Long get(String word);
  
  public long getWordCount();
  public int size();
}
