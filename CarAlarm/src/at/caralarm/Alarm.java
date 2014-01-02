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

  public void setFlashAndSound(boolean activate) {
    this.flash_light_on = activate;
    this.alarm_sound_on = activate;
  }

  public void setFlashLightOn(boolean flash_light_on) {
    this.flash_light_on = flash_light_on;
  }

  public void setAlarmSoundOn(boolean alarm_sound_on) {
    this.alarm_sound_on = alarm_sound_on;
  }

  public boolean isFlashLightOn() {
    return flash_light_on;
  }

  public boolean isAlarmSoundOn() {
    return alarm_sound_on;
  }

}
