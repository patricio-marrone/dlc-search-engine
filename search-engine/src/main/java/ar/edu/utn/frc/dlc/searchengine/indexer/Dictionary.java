package ar.edu.utn.frc.dlc.searchengine.indexer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ar.edu.utn.frc.dlc.searchengine.Concordance;
import ar.edu.utn.frc.dlc.searchengine.ConcordanceEntry;
import ar.edu.utn.frc.dlc.searchengine.Document;
import ar.edu.utn.frc.dlc.searchengine.sqlite.PostingEntry;
import ar.edu.utn.frc.dlc.searchengine.sqlite.Word;

public class Dictionary implements Serializable {

  private static final long serialVersionUID = 459327408956470040L;
  private Map<String, Word> wordCounts = new HashMap<String, Word>(100000);
  private Set<Document> documents = new HashSet<Document>(10000);
  private boolean finished = false;

  public Map<String, Word> getWordCounts() {
    return wordCounts;
  }

  public void setWordCounts(Map<String, Word> wordCounts) {
    this.wordCounts = wordCounts;
  }

  public Set<Document> getDocuments() {
    return documents;
  }

  public void setDocuments(Set<Document> documents) {
    this.documents = documents;
  }

//  public void addConcordance(Concordance concordance, boolean trackDocuments) {
//    if (trackDocuments && !documents.add(concordance.getDocument())) {
//      return;
//    }
//    for (ConcordanceEntry entry : concordance) {
//      Word currentWord = wordCounts.get(entry.getWord());
//      if (currentWord == null) {
//        currentWord = new Word();
//        currentWord.setCount(1);
//        currentWord.setWord(entry.getWord());
//        wordCounts.put(currentWord.getWord(), currentWord);
//      } else {
//        currentWord.setCount(currentWord.getCount() + 1);
//      }
//      PostingEntry postingEntry = new PostingEntry();
//      postingEntry.setDocument(concordance.getDocument());
//      postingEntry.setFrequency(entry.getFrequency());
//      currentWord.addPostingEntry(postingEntry);
//    }
//  }

//  public void addConcordance(Concordance concordance) {
//    addConcordance(concordance, true);
//  }

  public synchronized void addPosting(String wordString, PostingEntry postingEntry) {
    //System.out.println("Adding posting. " + wordString + " found in " + postingEntry.getDocument().getPath() + " " + postingEntry.getFrequency() + " times");
    Word word = wordCounts.get(wordString);
    if (word == null) {
      word = new Word();
      word.setWord(wordString);
      wordCounts.put(wordString, word);
    }
    word.addPostingEntry(postingEntry);
  }

  public Iterator<PostingEntry> getPostingIterator(String key) {
    Word word = wordCounts.get(key);
    if (word == null) {
      return new ArrayList<PostingEntry>().iterator();
    }
    return word.getPostingIterator();
  }
  
  public synchronized List<Word> getWords() {
    ArrayList<Word> wordsCopy = new ArrayList<Word>(wordCounts.size());
    wordsCopy.addAll(wordCounts.values());
    return wordsCopy;
  }
  
  public synchronized void finish() {
    this.finished = true;
  }
  
  public synchronized boolean isFinished() {
    return this.finished;
  }
}
