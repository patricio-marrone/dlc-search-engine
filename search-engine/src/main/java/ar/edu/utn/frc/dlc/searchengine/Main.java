package ar.edu.utn.frc.dlc.searchengine;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Scanner;
import java.util.zip.ZipException;

import ar.edu.utn.frc.dlc.searchengine.indexer.Dictionary;
import ar.edu.utn.frc.dlc.searchengine.indexer.PostingExtractor;
import ar.edu.utn.frc.dlc.searchengine.sqlite.DAL;
import ar.edu.utn.frc.dlc.searchengine.sqlite.PostingEntry;
import ar.edu.utn.frc.dlc.searchengine.sqlite.SqliteDAL;
import ar.edu.utn.frc.dlc.searchengine.sqlite.WordPersistenceDaemon;

public class Main {
  public static void main(String[] args) throws ZipException, IOException, SQLException, InterruptedException, SQLException {
    System.out.println("Processors: " + Runtime.getRuntime().availableProcessors() );
    Thread.sleep(1000);
    final Dictionary dictionary = new Dictionary();
    PostingExtractor extractor = new PostingExtractor(dictionary);
    
    DAL dal = new MongoDAL();
    dal.open();
    dal.dropDatabase();
    dal.createDatabase();
    
    WordPersistenceDaemon daemon = new WordPersistenceDaemon(dictionary, dal);
    new Thread(daemon).start();

    //String path = "/media/pmarrone/Datos/Descargas/guttemberg-test";
    String path = "/home/pmarrone/www.gutenberg.lib.md.us/";
    //String path = "/media/pmarrone/Datos/Descargas/guttemberg/www.gutenberg.lib.md.us/1/0";
    final File gutenbergFolder = new File(path);
    final GutenbergBookReader reader = new GutenbergBookReader(extractor, new DocumentReader());
    reader.setDal(dal);
    Runnable fileReader = new Runnable() {
      public void run() {
        try {
          reader.readFile(gutenbergFolder);
        } catch (ZipException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        } catch (SQLException e) {
          e.printStackTrace();
        }
        System.out.println("Finished!");
        dictionary.finish();
        
        Iterator<PostingEntry> entries = dictionary.getPostingIterator("the");
        while(entries.hasNext()) {
          PostingEntry entry = entries.next();
          System.out.println(entry.getDocument().getPath() + ":" + entry.getFrequency());
        } 
      }
    };
    new Thread(fileReader).start();
     
    Scanner inputScanner = new Scanner(System.in);
    while (true) {
      String command = inputScanner.nextLine();
      Scanner lineScanner = new Scanner(command);

      try {
        String commandName = lineScanner.next();
        int commandValue = 0;
        if (lineScanner.hasNextInt()) {
          commandValue = lineScanner.nextInt();
        }
        
        if (commandName.equals("exit")) {
          reader.setDelay(-1);
          break;
        } else if (commandName.equals("throttle")) {
          reader.setDelay(commandValue > 0 ? commandValue : 0);
        } else if (commandName.equals("betweencommits")) {
          daemon.setOpsBetweenCommits(commandValue);
        } else if (commandName.equals("pausedaemon")) {
          daemon.setPauseBetweenCommits(commandValue);
        }
         
      } catch (Exception e) {
        System.err.println("Invalid command");
      }
      lineScanner.close();
    }
    inputScanner.close();
  }
}
