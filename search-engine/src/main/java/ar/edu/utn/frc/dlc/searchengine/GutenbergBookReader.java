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
	

  DocumentReader reader;
  static int entries = 0;
  {
    reader = new DocumentReader();
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
        try {
          reader.readDocument(scanner, new HashMapConcordance());
        } catch (ConcordanceTooLargeException e) {
          //Document is weird. Skip
          e.printStackTrace();
        }
        
      }
      zipFile.close();
    }
  }
}
