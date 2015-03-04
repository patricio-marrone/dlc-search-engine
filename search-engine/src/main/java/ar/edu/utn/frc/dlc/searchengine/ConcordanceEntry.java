package ar.edu.utn.frc.dlc.searchengine;

import java.io.Serializable;

public class ConcordanceEntry implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 2L;
  public double idf;
  public long frequency;
  public String word;
  public boolean inTitle = false;
  public boolean inAuthor = false;
  
  public double getIdf() {
    return idf;
  }
  public void setIdf(double idf) {
    this.idf = idf;
  }
  public long getFrequency() {
    return frequency;
  }
  public void setFrequency(long frequency) {
    this.frequency = frequency;
  }
  public String getWord() {
    return word;
  }
  public void setWord(String word) {
    this.word = word;
  }
  public boolean isInTitle() {
    return inTitle;
  }
  public void setInTitle(boolean inTitle) {
    this.inTitle = inTitle;
  }
  public boolean isInAuthor() {
    return inAuthor;
  }
  public void setInAuthor(boolean inAuthor) {
    this.inAuthor = inAuthor;
  }
}
