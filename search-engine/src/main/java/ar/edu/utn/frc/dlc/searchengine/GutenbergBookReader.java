package ar.edu.utn.frc.dlc.searchengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;




import ar.edu.utn.frc.dlc.searchengine.indexer.PostingExtractor;
import ar.edu.utn.frc.dlc.searchengine.sqlite.DAL;

public class GutenbergBookReader {

  private PostingExtractor handler;
  private DocumentReader reader;
  private ObjectInputStream ois;
  private DAL dal;
  static int entries = 0;

  
  public DAL getDal() {
    return dal;
  }

  public void setDal(DAL dal) {
    this.dal = dal;
  }

  public PostingExtractor getHandler() {
    return handler;
  }

  public void setHandler(PostingExtractor handler) {
    this.handler = handler;
  }

  public GutenbergBookReader(PostingExtractor handler, DocumentReader reader) {
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

  public void readFile(File file) throws ZipException, IOException, SQLException {
    if (entries > 20000) {
      return;
    }
    if (file.isDirectory()) {
      File[] filesInDirectory = file.listFiles();
      for (File fileInDirectory : filesInDirectory) {
        try {
          this.readFile(fileInDirectory);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    } else {
      System.out.println("Reading " + file + "...");
      String fileName = file.getName();
      boolean isConcordance = fileName.endsWith(".con");
      boolean isZipFile = fileName.endsWith(".zip");
      if (!isZipFile) {
        return;
      }
      File concordanceFile = new File(file.getAbsolutePath() + ".con");
      if (concordanceFile.exists()) {
        try {
          ois = new ObjectInputStream(new FileInputStream(concordanceFile));
          Concordance existingConcordance = (Concordance) ois.readObject();
          if (dal != null) {
            dal.addDocument(existingConcordance.getDocument());
          }
          ++GutenbergBookReader.entries;
          System.out.println("Entries read: " + GutenbergBookReader.entries);
          if (handler != null) {
            handler.addConcordance(existingConcordance);
          }
          try {
            Thread.sleep(500);
          } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
          }
          return;
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("A problem was found in an existent concordance: " + concordanceFile);
        }

      }

      ZipFile zipFile = new ZipFile(file);
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
          if (dal != null) {
            dal.addDocument(document);
          }
          concordance.setDocument(document);
          if (handler != null) {
            handler.addConcordance(concordance);
          }
          ObjectOutputStream concordanceFileStream =
              new ObjectOutputStream(new FileOutputStream(concordanceFile));
          concordanceFileStream.writeObject(concordance);
          concordanceFileStream.flush();
          concordanceFileStream.close();
        } catch (ConcordanceTooLargeException e) {
          // Document is weird. Skip
          e.printStackTrace();
        }

      }
      zipFile.close();
    }

  }
}
