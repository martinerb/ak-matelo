package at.caralarm;

public class Alarmsystem {
  public enum SYSTEMSTATES {
    INIT, OPENANDUNLOCKED, CLOSEDANDUNLOCKED, OPENANDLOCKED, CLOSEDANDLOCKED, PINENTRY, ARMED, ALARM, SILENTANDOPEN
  }

  private static Alarmsystem instance;
  private SYSTEMSTATES currentState;
  private int current_pin_code;
  private int wrong_change_pin_count;
  private int wrong_unlock_pin_count;
  
  private Alarmsystem() {
    currentState = SYSTEMSTATES.INIT;
    this.current_pin_code = 100; // muss ein 3-Stelliger Code sein!!
    this.wrong_change_pin_count = 3;
    this.wrong_unlock_pin_count = 3;
  }
  
  public int getWrongChangePinCount() {
    return wrong_change_pin_count;
  }
  
  public int getWrongUnlockPinCount() {
    return wrong_unlock_pin_count;
  }  

  public static Alarmsystem getInstance() {
    if ( instance == null )
      instance = new Alarmsystem();
    return instance;
  }
  
  public SYSTEMSTATES getSystemstate() {
    return currentState; 
  }
  public void setKeyCode(int key_code) {
    this.current_pin_code = key_code;
  }
  
  public int getCurrentPinCode() {
    return current_pin_code;
  }

  public void stateTransition() {
    try {
      switch (currentState) {
      case INIT:
        currentState = SYSTEMSTATES.OPENANDUNLOCKED;
        break;

      case OPENANDUNLOCKED:
        openAndUnlocked();
        break;

      case CLOSEDANDUNLOCKED:
        closedAndUnlocked();
        break;

      case OPENANDLOCKED:
        openAndLocked();
        break;

      case CLOSEDANDLOCKED:
        closedAndLocked();
        break;

      case PINENTRY:
        pinEntry();
        break;

      case ARMED:
        armed();
        break;

      case ALARM:
        alarm();
        break;

      case SILENTANDOPEN:
        silentAndOpen();
        break;

      default:
        assert false : "Not Declared State Reached!!";
        break;
      }
    } catch (AlarmException e) {
      Timer.getInstance().initDeactivateAlarmTime();
      Alarm.getInstance().setFlashAndSound(true);
      System.out.println("ACTIVATE ALARM!!");
      currentState = SYSTEMSTATES.ALARM;
    }

  }

  // --------------------------- Transition Methods ---------------------------

  private void openAndUnlocked() throws AlarmException {
    if (Doors.getInstance().allDoorsClosed()) {
      currentState = SYSTEMSTATES.CLOSEDANDUNLOCKED;
    } else if (PinCode.getInstance().isLockRequest()) {
      Doors.getInstance().setDoorsLocked(true);
      currentState = SYSTEMSTATES.OPENANDLOCKED;
    } else if (PinCode.getInstance().getChangePinCode()) {
      currentState = SYSTEMSTATES.PINENTRY;
    }
  }

  private void closedAndUnlocked() throws AlarmException {
    if (!Doors.getInstance().allDoorsClosed()) {
      currentState = SYSTEMSTATES.OPENANDUNLOCKED;
    } else if (PinCode.getInstance().isLockRequest()) {
      Doors.getInstance().setDoorsLocked(true);
      Timer.getInstance().initActivateAlarmTime();
      currentState = SYSTEMSTATES.CLOSEDANDLOCKED;
    } else if (PinCode.getInstance().getChangePinCode()) {
      currentState = SYSTEMSTATES.PINENTRY;
    }
  }

  private void openAndLocked() throws AlarmException {
    if (PinCode.getInstance().isUnlockRequest() && tryToSetLockState(false)) {
      currentState = SYSTEMSTATES.OPENANDUNLOCKED;
    } else if (Doors.getInstance().allDoorsClosed()) {
      Timer.getInstance().initActivateAlarmTime();
      currentState = SYSTEMSTATES.CLOSEDANDLOCKED;
    }
  }

  private void closedAndLocked() throws AlarmException {
    if (PinCode.getInstance().isUnlockRequest() && tryToSetLockState(false)) {
      currentState = SYSTEMSTATES.CLOSEDANDUNLOCKED;
    } else if (!Doors.getInstance().allDoorsClosed()) {
      currentState = SYSTEMSTATES.OPENANDLOCKED;
    } else if (Timer.getInstance().getActivateAlarmTime() <= 0) {
      System.out.println("CLOSEDANDLOCKED: goto ARMED");
      currentState = SYSTEMSTATES.ARMED;
    }
  }

  private void pinEntry() throws AlarmException {
    int submitted_pin_code = PinCode.getInstance().getSubmittedPinCode();
    if (submitted_pin_code == 0) // no key send yet
      return;

    if (submitted_pin_code == current_pin_code) {
      wrong_change_pin_count = 3;
      int new_pin_code = PinCode.getInstance().getNewPinCode();
      if (new_pin_code >= 100 && new_pin_code < 1000) {
        current_pin_code = new_pin_code;
        System.out.println("newPinSet");
      } else {
        System.out.println("WARNING: Not allowed new PinCode -> PinCode NOT changed!!");
      }
    } else {
      --wrong_change_pin_count;
      if (wrong_change_pin_count <= 0) {
        wrong_change_pin_count = 3;
        Doors.getInstance().setDoorsLocked(true);
        throw new AlarmException();
      }
    }
    currentState = SYSTEMSTATES.OPENANDUNLOCKED;
  }
  
  private void armed() throws AlarmException {
    if (PinCode.getInstance().isUnlockRequest() && tryToSetLockState(false)) {
      currentState = SYSTEMSTATES.CLOSEDANDUNLOCKED;
      System.out.println("ARMED: goto CLOSEDANDUNLOCKED");
    } else if (!Doors.getInstance().allDoorsClosed()) {
      System.out.println("ARMED: goto ALARM");
      throw new AlarmException();
    }
  }

  private void alarm() throws AlarmException {
    if (PinCode.getInstance().isUnlockRequest() && tryToSetLockState(false)) {
      Alarm.getInstance().setFlashAndSound(false);
      currentState = SYSTEMSTATES.OPENANDUNLOCKED;
    }
    if (Timer.getInstance().getDeactivateSoundTime() <= 0) {
      Alarm.getInstance().setAlarmSoundOn(false);
    }
    if (Timer.getInstance().getDeactivateFlashTime() <= 0) {
      Alarm.getInstance().setFlashAndSound(false);
      currentState = SYSTEMSTATES.SILENTANDOPEN;
    }
  }

  private void silentAndOpen() throws AlarmException {
    if (PinCode.getInstance().isUnlockRequest() && tryToSetLockState(false)) {
      currentState = SYSTEMSTATES.OPENANDUNLOCKED;
    } else if (Doors.getInstance().allDoorsClosed()) {
      currentState = SYSTEMSTATES.ARMED;
    }
  }

  // ----------------------------- Helper Methods -----------------------------

  private boolean tryToSetLockState(boolean update_lockstate) throws AlarmException {
    if (current_pin_code == PinCode.getInstance().getSubmittedPinCode()) {
      Doors.getInstance().setDoorsLocked(update_lockstate);
      wrong_unlock_pin_count = 3;
      return true;
    }

    if (currentState == SYSTEMSTATES.ARMED) {
      --wrong_unlock_pin_count;
      if (wrong_unlock_pin_count <= 0) {
       wrong_unlock_pin_count = 3;
        throw new AlarmException();
      }
    }

    return false;
  }

}
