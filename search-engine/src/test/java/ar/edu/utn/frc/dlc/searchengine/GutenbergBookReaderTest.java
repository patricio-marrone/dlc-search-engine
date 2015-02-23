//package ar.edu.utn.frc.dlc.searchengine;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.zip.ZipException;
//
//import ar.edu.utn.frc.dlc.searchengine.indexer.PostingExtractor;
//import junit.framework.TestCase;
//
//public class GutenbergBookReaderTest extends TestCase {
//
//  protected void setUp() throws Exception {
//    super.setUp();
//  }
//
//  protected void tearDown() throws Exception {
//    super.tearDown();
//  }
//
//  public final void testReadFile() throws ZipException, IOException {
//    String path;
//    //path = "D:\\Descargas\\guttemberg\\www.gutenberg.lib.md.us\\2\\0";
//    //path = "D:\\Descargas\\guttemberg\\www.gutenberg.lib.md.us";
//    //path = "D:\\Descargas\\guttemberg";
//    path = "/media/pmarrone/Datos/Descargas/guttemberg";
//    PostingExtractor extractor = new PostingExtractor();
//    File gutenbergFolder = new File(path);
//    GutenbergBookReader reader = new GutenbergBookReader(extractor, new DocumentReader(), new Dictionary());
//    reader.readFile(gutenbergFolder);
//    System.out.println("Finished!");
//  }
//  
//  public final void testIndex() {
//
//    
//  }
//}
