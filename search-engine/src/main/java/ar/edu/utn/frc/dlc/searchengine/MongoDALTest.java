package ar.edu.utn.frc.dlc.searchengine;

import java.util.Iterator;

import ar.edu.utn.frc.dlc.searchengine.sqlite.DAL;
import ar.edu.utn.frc.dlc.searchengine.sqlite.PostingEntry;

public class MongoDALTest {

  public static void main(String[] args) {
 
    DAL dal = new MongoDAL();
    dal.open();
    DictionaryReader reader = new DictionaryReader();
    reader.setDal(dal);
    reader.getDocumentMap();
    Iterator<PostingEntry> resultIterator = reader.getPostingIterator("the");
    while (resultIterator.hasNext()) {
      PostingEntry entry = resultIterator.next();
      System.out.println("Result: " + entry.getDocument().getTitle() + ", Freq: " + entry.getFrequency() + ", Author: " + entry.getDocument().getAuthor() + ", url: " + entry.getDocument().getPath());
    }
    
    System.out.println("willywonka: " + reader.getWordPostingCount("williwonka"));
    System.out.println("experience: " + reader.getWordPostingCount("experience"));
    System.out.println("dog: " + reader.getWordPostingCount("dog"));
    System.out.println("amsterdam: " + reader.getWordPostingCount("amsterdam"));
    System.out.println("hyperspace: " + reader.getWordPostingCount("hyperspace"));
    System.out.println("Total documents: " + reader.getDocumentCount());
  }
}
