package ar.edu.utn.frc.dlc.searchengine.indexer;

import ar.edu.utn.frc.dlc.searchengine.Concordance;
import ar.edu.utn.frc.dlc.searchengine.ConcordanceEntry;
import ar.edu.utn.frc.dlc.searchengine.Document;
import ar.edu.utn.frc.dlc.searchengine.sqlite.PostingEntry;

public class PostingExtractor {
  private Dictionary dictionary;

  public Dictionary getDictionary() {
    return dictionary;
  }

  public void setDictionary(Dictionary dictionary) {
    this.dictionary = dictionary;
  }
  
  public PostingExtractor(Dictionary dictionary) {
    this.dictionary = dictionary;
  }
  
  public void addConcordance(Concordance concordance) {
    Document concordanceDocument = concordance.getDocument();
    for (ConcordanceEntry entry : concordance) {
      PostingEntry postingEntry = new PostingEntry();
      postingEntry.setDocument(concordanceDocument);
      postingEntry.setFrequency(entry.getFrequency());
      postingEntry.inAuthor = entry.inAuthor;
      postingEntry.inTitle = entry.inTitle;
      dictionary.addPosting(entry.getWord(), postingEntry); 
    }
  }
}
