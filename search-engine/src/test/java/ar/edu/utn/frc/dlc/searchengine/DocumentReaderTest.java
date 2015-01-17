package ar.edu.utn.frc.dlc.searchengine;

import java.io.File;
import java.util.Map;
import java.util.Scanner;

import junit.framework.TestCase;

public class DocumentReaderTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testReadDocument() {
		String document = "The quick brown fox jumps over the lazy dog";
		Scanner scanner = new Scanner(document);
		DocumentReader reader = new DocumentReader();
		Map<String, Long> concordance = reader.readDocument(scanner);
		System.out.println(concordance);
		assertEquals((Long)2l, concordance.get("the"));
		assertEquals((Long)1l, concordance.get("quick"));
		assertEquals((Long)1l, concordance.get("brown"));
		assertEquals((Long)1l, concordance.get("fox"));
		assertEquals((Long)1l, concordance.get("jumps"));
		assertEquals((Long)1l, concordance.get("over"));
		assertEquals((Long)1l, concordance.get("lazy"));
		assertEquals((Long)1l, concordance.get("dog"));
		assertEquals(null, concordance.get("match"));
		assertEquals(null, concordance.get(""));
	}
}
