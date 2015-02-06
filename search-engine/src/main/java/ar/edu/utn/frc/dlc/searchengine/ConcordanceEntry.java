package ar.edu.utn.frc.dlc.searchengine;

public class ConcordanceEntry {
  private double idf;
  private long frequency;
  private String word;
  
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
}
