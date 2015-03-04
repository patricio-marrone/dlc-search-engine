package ar.edu.utn.frc.dlc.searchengine;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class HashMapConcordance implements Concordance {

  private static final long serialVersionUID = 5L;
  private Document document;
  private static final long CONCORDANCE_LIMIT = 50000;
  private Map<String, ConcordanceEntry> concordance = new HashMap<String, ConcordanceEntry>();
  private long wordCount = 0;

  public void put(String word, boolean inTitle, boolean inAuthor) throws ConcordanceTooLargeException {
    wordCount++;
    
    if (concordance.size() > CONCORDANCE_LIMIT) {
      throw new ConcordanceTooLargeException();
    }
    ConcordanceEntry wordEntry = concordance.get(word);
    if (wordEntry != null) {
      wordEntry.setFrequency(wordEntry.getFrequency() + 1);
      wordEntry.inAuthor &= inAuthor;
      wordEntry.inTitle &= inTitle;
    } else {
      wordEntry = new ConcordanceEntry();
      wordEntry.setFrequency(1);
      wordEntry.setWord(word);
      wordEntry.inAuthor = inAuthor;
      wordEntry.inTitle = inTitle;
      concordance.put(word, wordEntry);
    }
  }

  public long getWordCount() {
    return wordCount;
  }

  public int size() {
    return concordance.size();
  }

  public ConcordanceEntry get(String word) {
    return concordance.get(word);
  }
  
  public Document getDocument() {
    return document;
  }

  public void setDocument(Document document) {
    this.document = document;
  }

  public Iterator<ConcordanceEntry> iterator() {
    return HashMapConcordance.this.concordance.values().iterator();
  }

  public void put(String word) throws ConcordanceTooLargeException {
    this.put(word, false, false);
  } 
}
