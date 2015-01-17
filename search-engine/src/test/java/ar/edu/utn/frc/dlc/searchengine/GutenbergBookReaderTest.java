package ar.edu.utn.frc.dlc.searchengine;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import junit.framework.TestCase;

public class GutenbergBookReaderTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public final void testReadFile() throws ZipException, IOException {
		File gutenbergFolder = new File("D:\\Descargas\\guttemberg");
		GutenbergBookReader reader = new GutenbergBookReader();
		reader.readFile(gutenbergFolder);
	}

}
