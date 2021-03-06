package at.caralarm;

public class Timer {

  private static final int INIT_SOUND_TIME = 30;
  private static final int INIT_FLASH_TIME = 300;
  private static final int INIT_ALARM_TIME = 20;

  private static Timer instance;

  private int deactivate_sound_time;
  private int deactivate_flash_time;
  private int activate_alarm_time;

  public static Timer getInstance() {
    if ( instance == null )
      instance = new Timer();
    return instance;
  }

  private Timer() {
    this.deactivate_sound_time = 0;
    this.deactivate_flash_time = 0;
    this.activate_alarm_time = 0;
  }

  public void initDeactivateAlarmTime() {
    this.deactivate_flash_time = INIT_FLASH_TIME;
    this.deactivate_sound_time = INIT_SOUND_TIME;
  }

  public int getDeactivateSoundTime() {
    return deactivate_sound_time;
  }

  public int getDeactivateFlashTime() {
    return deactivate_flash_time;
  }

  public int getActivateAlarmTime() {
    return activate_alarm_time;
  }

  public void initActivateAlarmTime() {
    this.activate_alarm_time = INIT_ALARM_TIME;
  }

  public void increaseTime( int value ) {
    deactivate_sound_time = (deactivate_sound_time >= value) ? (deactivate_sound_time - value)
        : 0;
    deactivate_flash_time = (deactivate_flash_time >= value) ? (deactivate_flash_time - value)
        : 0;
    activate_alarm_time = (activate_alarm_time >= value) ? (activate_alarm_time - value)
        : 0;
  }
}
