package ar.edu.utn.frc.dlc.searchengine;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class DocumentReader {

  public Concordance readDocument(Scanner scanner, Concordance concordance) throws ConcordanceTooLargeException {

    while (scanner.hasNext()) {
      String currentWord = scanner.next();
      if (currentWord.length() > 50) {
        // Word is too long. Something smells funny...
        System.out.println("Word was too long and was discarded: " + currentWord);
        continue;
      }
      String[] words = cleanAndSplit(currentWord);
      for (String word : words) {
        if (!word.trim().isEmpty()) {
          concordance.put(word);
        }
      }
      
    }
    System.out.println("Words Read: " + concordance.getWordCount() + ", Concordance size: " + concordance.size());
    return concordance;
  }

  public String[] cleanAndSplit(String token) {
    String[] cleanWords = token.toLowerCase().split("[^a-zA-Z\\d\\s]+");
    return cleanWords;
  }
}
