package ar.edu.utn.frc.dlc.searchengine;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class GutenbergBookReader {
  
  private PersistenceHandler handler;
  private DocumentReader reader;
  static int entries = 0; 

  public PersistenceHandler getHandler() {
    return handler;
  }



  public void setHandler(PersistenceHandler handler) {
    this.handler = handler;
  }

  public GutenbergBookReader(PersistenceHandler handler, DocumentReader reader) {
    super();
    this.handler = handler;
    this.reader = reader;
  }



  public DocumentReader getReader() {
    return reader;
  }



  public void setReader(DocumentReader reader) {
    this.reader = reader;
  }



  public void readFile(File file) throws ZipException, IOException {
    if (file.isDirectory()) {
      File[] filesInDirectory = file.listFiles();
      for (File fileInDirectory : filesInDirectory) {
        this.readFile(fileInDirectory);
      }
    } else {
      ZipFile zipFile = new ZipFile(file);
      System.out.println("Reading " + file + "...");
      Enumeration<? extends ZipEntry> entries = zipFile.entries();
      
      while (entries.hasMoreElements()) {
        ZipEntry entry = entries.nextElement();
        Scanner scanner = new Scanner(zipFile.getInputStream(entry));
        System.out.println("Entries read: " + (++GutenbergBookReader.entries));
        Concordance concordance;
        try {
          concordance = reader.readDocument(scanner, new HashMapConcordance());
          Document document = new Document();
          document.setPath(file.getAbsolutePath());
          concordance.setDocument(document);
          if (handler != null) {
            handler.persistConcordance(concordance);
          }
        } catch (ConcordanceTooLargeException e) {
          //Document is weird. Skip
          e.printStackTrace();
        }
        
      }
      zipFile.close();
    }
  }
}
