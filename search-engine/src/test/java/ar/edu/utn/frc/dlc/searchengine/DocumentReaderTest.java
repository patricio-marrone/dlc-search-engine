package ar.edu.utn.frc.dlc.searchengine;

import java.util.Arrays;
import java.util.Scanner;

import junit.framework.TestCase;

public class DocumentReaderTest extends TestCase {

  protected void setUp() throws Exception {
    super.setUp();
  }

  protected void tearDown() throws Exception {
    super.tearDown();
  }

  public final void testReadDocument() throws ConcordanceTooLargeException {
    String document = "The quick brown fox jumps over the lazy dog";
    Scanner scanner = new Scanner(document);
    DocumentReader reader = new DocumentReader();
    Concordance concordance = reader.readDocument(scanner, new HashMapConcordance());
    System.out.println(concordance);
    assertEquals((Long) 2l, (Long)concordance.get("the"));
    assertEquals((Long) 1l, (Long)concordance.get("quick"));
    assertEquals((Long) 1l, (Long)concordance.get("brown"));
    assertEquals((Long) 1l, (Long)concordance.get("fox"));
    assertEquals((Long) 1l, (Long)concordance.get("jumps"));
    assertEquals((Long) 1l, (Long)concordance.get("over"));
    assertEquals((Long) 1l, (Long)concordance.get("lazy"));
    assertEquals((Long) 1l, (Long)concordance.get("dog"));
    assertEquals(null, concordance.get("match"));
    assertEquals(null, concordance.get(""));
  }
  
  public final void testCleanAndSplit() {
    String document = "The+-**- quick+-/--*++_brown,.fox((jumps+_-=\"'\\ over%^the*( lazy_dog";
    DocumentReader reader = new DocumentReader();
    
    String[] cleanWords = reader.cleanAndSplit(document);
    assertFalse(Arrays.asList(cleanWords).contains(""));
    System.out.println(Arrays.toString(cleanWords));
  }
}
