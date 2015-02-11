package ar.edu.utn.frc.dlc.searchengine;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PersistenceHandlerTest {
  PersistenceHandler handler;
  @BeforeClass
  public static void setUpBeforeClass() throws Exception {}

  @AfterClass
  public static void tearDownAfterClass() throws Exception {}

  @Before
  public void setUp() throws Exception {
    handler = new PersistenceHandler();
    
  }

  @After
  public void tearDown() throws Exception {}

  @Test
  public final void testShowIndex() {
    handler.showIndex();
  }

}
