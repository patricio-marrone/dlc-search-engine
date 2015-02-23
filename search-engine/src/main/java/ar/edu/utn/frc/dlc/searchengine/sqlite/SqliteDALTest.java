//package ar.edu.utn.frc.dlc.searchengine.sqlite;
//
//import static org.junit.Assert.*;
//
//import java.sql.SQLException;
//
//import org.junit.After;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import com.almworks.sqlite4java.SQLiteException;
//
//public class SqliteDALTest {
//	private SqliteDAL dal;
//	@BeforeClass
//	public static void setUpBeforeClass() throws Exception {
//	}
//
//	@AfterClass
//	public static void tearDownAfterClass() throws Exception {
//	}
//
//	@Before
//	public void setUp() throws Exception {
//		dal = new SqliteDAL();
//	}
//
//	@After
//	public void tearDown() throws Exception {
//	}
//
//	@Test
//	public void test() {
//
//	}
//	@Test
//	public void connectionTest() throws SQLiteException {
//		dal.open();
//	}
//	
//	@Test
//	public void createDabaseTest() throws SQLException, SQLiteException {
//	  dal.open();
//	  dal.createDatabase();
//	}
//
//}
