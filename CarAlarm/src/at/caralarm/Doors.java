package at.caralarm;

public class Doors {

  private boolean doors_closed; // true if closed
  private boolean boot_lid_closed; // true if closed
  private boolean doors_locked;

  private static Doors instance;

  public static Doors getInstance() {
    if ( instance == null )
      instance = new Doors();
    return instance;
  }

  private Doors() {
    this.doors_closed = false;
    this.doors_locked = false;
    this.boot_lid_closed = false;
  }

  public boolean allDoorsClosed() {
    return doors_closed;
  }

  public void setDoorsClosed( boolean doors_closed ) {
    this.doors_closed = doors_closed;
  }

  public boolean allDoorsLocked() {
    return doors_locked;
  }

  public void setDoorsLocked(boolean doors_locked) {
    this.doors_locked = doors_locked;
  }

  public boolean isBootLidClosed() {
    return boot_lid_closed;
  }

  public void setBootLidClosed( boolean boot_lid_closed ) {
    this.boot_lid_closed = boot_lid_closed;
  }

}
