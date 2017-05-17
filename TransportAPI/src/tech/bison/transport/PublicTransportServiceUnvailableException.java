package tech.bison.transport;

public class PublicTransportServiceUnvailableException extends Exception {

  public PublicTransportServiceUnvailableException() {
    super();
  }

  public PublicTransportServiceUnvailableException(String message) {
    super(message);
  }

  public PublicTransportServiceUnvailableException(Exception e) {
    super(e);
  }

  private static final long serialVersionUID = 8199069673947161898L;

}
