package at.caralarm;

public class Alarmsystem {
  private int key_code;

  private Alarmsystem() {
    this.key_code = 12345;
  }

  public void setKey_code( int key_code ) {
    this.key_code = key_code;
  }

}
