package ar.edu.utn.frc.dlc.searchengine.sqlite;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import ar.edu.utn.frc.dlc.searchengine.Document;

public class Word {

  private String word;
  private Integer count = 0;
  private Integer currentBlock;
  private Integer id;
  private List<PostingEntry> entries; 

  
  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }

  public Integer getCurrentBlock() {
    return currentBlock;
  }

  public void setCurrentBlock(Integer currentBlock) {
    this.currentBlock = currentBlock;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public synchronized void addPostingEntry(PostingEntry postingEntry) {
    if (entries == null) {
      entries = new ArrayList<PostingEntry>(32);
    }
    entries.add(postingEntry);
    count++;
  }
  
  public synchronized List<PostingEntry> flushEntries() {
    List<PostingEntry> flushedEntries = this.entries;
    this.entries = null;
    return flushedEntries;
  }

  public Iterator<PostingEntry> getPostingIterator() {
    return entries.iterator();
  }

  public boolean hasUpdates() {
    return entries != null;
  }
}
