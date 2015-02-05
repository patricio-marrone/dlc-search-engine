package ar.edu.utn.frc.dlc.searchengine;

import java.util.HashMap;
import java.util.Map;

public class HashMapConcordance implements Concordance {

  private static final long CONCORDANCE_LIMIT = 50000;
  private Map<String, Long> concordance = new HashMap<String, Long>();
  private long wordCount = 0;

  public void put(String word) throws ConcordanceTooLargeException {
    wordCount++;

    if (concordance.size() > CONCORDANCE_LIMIT) {
      throw new ConcordanceTooLargeException();
    }

    if (concordance.containsKey(word)) {
      long incrementedCount = concordance.get(word) + 1;
      concordance.put(word, incrementedCount);
    } else {
      concordance.put(word, 1l);
    }
  }

  public long getWordCount() {
    return wordCount;
  }

  public int size() {
    return concordance.size();
  }

  public Long get(String word) {
    return concordance.get(word);
  } 
}
