package ar.edu.utn.frc.dlc.searchengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import ar.edu.utn.frc.dlc.searchengine.sqlite.DAL;
import ar.edu.utn.frc.dlc.searchengine.sqlite.PostingEntry;

public class DictionaryReader {
  DAL dal;
  
  public DAL getDal() {
    return dal;
  }

  public void setDal(DAL dal) {
    this.dal = dal;
  }

  public Iterator<PostingEntry> getPostingIterator(String key) {
    return dal.getPostingIterator(key);
  }
  
  public Integer getWordPostingCount(String key) {
    return dal.getWordPostingCount(key);
  }
  
  public Integer getDocumentCount() {
    return dal.getDocumentCount();
  }
  
  public Map<Integer, Document> getDocumentMap() {
   return dal.getDocumentMap();
  }
}