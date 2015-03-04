package ar.edu.utn.frc.dlc.searchengine;

import ar.edu.utn.frc.dlc.searchengine.sqlite.DAL;

public class MongoDALTest {

  public static void main(String[] args) {
 
    DAL dal = new MongoDAL();
    dal.open();
    DictionaryReader reader = new DictionaryReader();
    reader.setDal(dal);
    reader.getPostingIterator("menem");
  }
}
