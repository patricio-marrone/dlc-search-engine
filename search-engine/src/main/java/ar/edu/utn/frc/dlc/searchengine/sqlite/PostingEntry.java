package ar.edu.utn.frc.dlc.searchengine.sqlite;

import ar.edu.utn.frc.dlc.searchengine.Document;

public class PostingEntry {
  private Long frequency;
  private Document document;
  private Integer documentCode;
  
  public Long getFrequency() {
    return frequency;
  }
  public void setFrequency(Long frequency) {
    this.frequency = frequency;
  }
  public Document getDocument() {
    return document;
  }
  public void setDocument(Document document) {
    this.document = document;
  }
  public Integer getDocumentCode() {
    if (document != null) {
      return document.getId();
    } else {
      return documentCode;      
    }
  }
  public void setDocumentCode(Integer documentCode) {
    if (document != null){
      this.document.setId(documentCode);
    }
    this.documentCode = documentCode;
  }
}
