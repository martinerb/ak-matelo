package at.caralarm;

public class Doors {

  private boolean doors_closed; // true if closed
  private boolean boot_lid_closed; // true if closed
  private static Doors instance;

  public static Doors getInstance() {
    if ( instance == null )
      instance = new Doors();
    return instance;
  }

  private Doors() {
    this.doors_closed = false;
    this.boot_lid_closed = false;
  }

  public boolean isDoors_closed() {
    return doors_closed;
  }

  public void setDoors_closed( boolean doors_closed ) {
    this.doors_closed = doors_closed;
  }

  public boolean isBoot_lid_closed() {
    return boot_lid_closed;
  }

  public void setBoot_lid_closed( boolean boot_lid_closed ) {
    this.boot_lid_closed = boot_lid_closed;
  }

}
