package at.caralarm;

public class Alarm {

  private boolean flash_light_on;
  private boolean alarm_sound_on;

  private static Alarm instance;

  public static Alarm getInstance() {
    if ( instance == null )
      instance = new Alarm();
    return instance;
  }

  private Alarm() {
    this.flash_light_on = false;
    this.alarm_sound_on = false;
  }

}
