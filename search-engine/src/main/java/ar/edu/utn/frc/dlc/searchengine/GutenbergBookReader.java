package ar.edu.utn.frc.dlc.searchengine;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
  private static Pattern urlPattern = Pattern.compile(".*(www\\.gutenberg\\.lib\\.md\\.us.*)\\.zip");
  
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
      String fileName = file.getName();
      
      boolean isZipFile = fileName.endsWith(".zip");
      if (!isZipFile) {
        //System.out.println("Ignoring " + file + "...");
        return;
      }
      System.out.println("Reading " + file + "...");
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
          System.out.println("Document read from concordance file: " + existingConcordance.getDocument());
          return;
        } catch (InvalidClassException e) {
          System.out.println("Invalid concordance file found. Deleting.");
          concordanceFile.delete();
        } catch (LanguageNotSupportedException e) {
          System.out.println("The document language is unsupported: " + e.getMessage() + ". Skipping...");
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
          Document document = concordance.getDocument();
          
          Matcher urlMatcher = urlPattern.matcher(file.getAbsolutePath());
          if (urlMatcher.find()) {    
            String url = urlMatcher.group(1) + ".txt";
            document.setPath(url);
          }

          if (dal != null) {
            dal.addDocument(document);
          }
          System.out.println("Document read from zip file: " + document);
          if (handler != null) {
            handler.addConcordance(concordance);
          }
          
          
          ObjectOutputStream concordanceFileStream =
              new ObjectOutputStream(new FileOutputStream(concordanceFile));
          System.out.println("Writing new concordance file: " + concordanceFile);
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
