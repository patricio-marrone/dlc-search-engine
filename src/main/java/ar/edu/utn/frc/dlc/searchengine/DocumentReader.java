package ar.edu.utn.frc.dlc.searchengine;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DocumentReader {
	
	public Map<String, Long> readDocument(Scanner scanner) {
		int wordsRead = 0;
		Map<String, Long> concordance = new HashMap<String, Long>();
		while (scanner.hasNext()) {
			String currentWord = scanner.next();
			if (currentWord.length() > 50) {
				//Word is too long. Something smells funny...
				System.out.println("Word was too long and was discarded: " + currentWord);
				continue;
			}
			currentWord = currentWord.toLowerCase();
			if (concordance.containsKey(currentWord)) {
				long incrementedCount = concordance.get(currentWord) + 1;
				concordance.put(currentWord, incrementedCount);
			} else {
				concordance.put(currentWord, 1l);
			}
			wordsRead++;
			if (concordance.size() > 50000) {
				System.err.println("There's something wrong about this file. Discarding...");
				concordance.clear();
				break;
			}
		}
		System.out.println(wordsRead + ", Concordance size: " + concordance.size());
		return concordance;
	}
}
