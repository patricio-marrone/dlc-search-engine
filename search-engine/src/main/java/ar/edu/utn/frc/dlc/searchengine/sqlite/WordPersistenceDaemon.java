package ar.edu.utn.frc.dlc.searchengine.sqlite;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import ar.edu.utn.frc.dlc.searchengine.indexer.Dictionary;

public class WordPersistenceDaemon implements Runnable {
  private boolean oneMoreTime = true;
  private Dictionary dictionary;
  private DAL dal;
  private int iteration = 1;
  private int pauseBetweenCommits = 60000;
  private int opsBetweenCommits = 300;
  public WordPersistenceDaemon(Dictionary dictionary, DAL dal) {
    super();
    this.dictionary = dictionary;
    this.dal = dal;
  }

  public void run() {
    
    int operations = 0;
    while (oneMoreTime) {
      oneMoreTime = !dictionary.isFinished();
      System.out.println("Word Persistence daemon iteration : " + iteration++);
      Collection<Word> words = dictionary.getWords();
      int size = words.size();
      int i = 0;
      for (Word word : words) { 
        try {
          if (word.hasUpdates()) {
            i++;
            operations++;
            List<PostingEntry> postingEntries = word.flushEntries();
            this.dal.flushPostings(word, postingEntries);
            if (i % opsBetweenCommits == 0) {
              System.out.println("Processing: " + i + " of " + size);
              dal.commit();
              System.out.println("Committed!");
            }
          };
        } catch (IOException e) {
          e.printStackTrace();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
      try {
        System.out.println("Committing...");
        dal.commit();
         Thread.sleep(pauseBetweenCommits);
      } catch (SQLException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } 
    }
    System.out.println("Persistence Daemon done!");
  }

  public void setOpsBetweenCommits(int opsBetweenCommits) {
    this.opsBetweenCommits = opsBetweenCommits;
    
  }

  public void setPauseBetweenCommits(int pauseBetweenCommits) {
    this.pauseBetweenCommits = pauseBetweenCommits;
  }
}
