package ar.edu.utn.frc.dlc.searchengine;

public class LanguageNotSupportedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public LanguageNotSupportedException(String message) {
    super(message);
  }
}
