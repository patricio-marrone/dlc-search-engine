package ar.edu.utn.frc.dlc.searchengine;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DocumentReader {

  public Concordance readDocument(Scanner scanner, Concordance concordance) throws ConcordanceTooLargeException {
    Document document = new Document();
    concordance.setDocument(document);
    String title = findByRegex(scanner, document, "Title: (.*)$");
    String[] titleWords = cleanAndSplit(title);
    for (String word : titleWords) {
      concordance.put(word, true, false);
    }
    document.setTitle(title);
    
    String author = findByRegex(scanner, document, "(?:Author|Editor): (.*)$");
    String[] authorWords = cleanAndSplit(author);
    for (String word : authorWords) {
      concordance.put(word, false, true);
    }
    document.setAuthor(author);
    
    String language = findByRegex(scanner, document, "Language: (.*)$").trim();
    if (!language.equals("English")) {
      throw new LanguageNotSupportedException(language);
    }
    
    
    findByRegex(scanner, document, "(.+\\*\\*\\*)");
    scanner.useDelimiter("(?:[^\\w]|_)+");
    while (scanner.hasNext()) {
      String currentWord = scanner.next().toLowerCase();

      //String[] words = cleanAndSplit(currentWord);
      
      //for (String word : words) {
      if (!currentWord.trim().isEmpty()) {
        if (currentWord.length() > 50) {
          // Word is too long. Something smells funny...
          System.out.println("Word was too long and was discarded: " + currentWord);
          continue;
        }
        concordance.put(currentWord);
      }
      //}
      
    }
    System.out.println("Words Read: " + concordance.getWordCount() + ", Concordance size: " + concordance.size());
    return concordance;
  }

  
  private String findByRegex(Scanner scanner, Document document, String regex) {
    Pattern pattern = Pattern.compile(regex);
    while (scanner.hasNext()) {
      String line = scanner.nextLine();
      Matcher matcher = pattern.matcher(line);
      if (!matcher.find()) {
        continue;
      }
      String match = matcher.group(1);
      if (match != null) {
        return match.trim(); 
      }
    }
    return null;
  }


  public String[] cleanAndSplit(String token) {
    String[] cleanWords = token.toLowerCase().split("(?:[^\\w]|_)+");
    return cleanWords;
  }
}
