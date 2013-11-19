package at.caralarm;

public class Alarmsystem {
  private enum SYSTEMSTATES {
    INIT, OPENANDUNLOCKED, CLOSEDANDUNLOCKED, OPENANDLOCKED, CLOSEDANDLOCKED, SETPINCODE, ARMED, ALARM, SILENTANDOPEN
  }

  private SYSTEMSTATES currentState;
  private int current_pin_code;
  private int wrong_new_pin_count;
  private int wrong_un_lock_pin_count;
  private boolean set_pin_code;

  private Alarmsystem() {
    currentState = SYSTEMSTATES.INIT;
    this.current_pin_code = 123; // muss ein 3-Stelliger Code sein!!
    this.wrong_new_pin_count = 3;
    this.wrong_un_lock_pin_count = 3;
    this.set_pin_code = false;
  }

  public void setKeyCode(int key_code) {
    this.current_pin_code = key_code;
  }

  public void setSetPinCode(boolean set_pin_code) {
    this.set_pin_code = set_pin_code;
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

      case SETPINCODE:
        setPinCode();
        break;

      case ARMED:
        armed();
        break;

      case ALARM:

        break;

      case SILENTANDOPEN:

        break;

      default:
        assert false : "Not Declared State Reached!!";
        break;
      }
    } catch (AlarmException e) {
      System.out.println("ACTIVATE ALARM!!");
      currentState = SYSTEMSTATES.ALARM;
    }

  }

  // --------------------------- Transition Methods ---------------------------

  private void openAndUnlocked() throws AlarmException {
    if (Doors.getInstance().allDoorsClosed()) {
      currentState = SYSTEMSTATES.CLOSEDANDUNLOCKED;
    } else if (PinCode.getInstance().isLockRequest() && checkUnLockRequest(true)) {
      currentState = SYSTEMSTATES.OPENANDLOCKED;
    } else if (set_pin_code) {
      currentState = SYSTEMSTATES.SETPINCODE;
    }
  }

  private void closedAndUnlocked() throws AlarmException {
    if (!Doors.getInstance().allDoorsClosed()) {
      currentState = SYSTEMSTATES.OPENANDUNLOCKED;
    } else if (PinCode.getInstance().isLockRequest() && checkUnLockRequest(true)) {
      Timer.getInstance().initActivateAlarmTime();
      currentState = SYSTEMSTATES.CLOSEDANDLOCKED;
    } else if (set_pin_code) {
      currentState = SYSTEMSTATES.SETPINCODE;
    }
  }

  private void openAndLocked() throws AlarmException {
    if (PinCode.getInstance().isUnlockRequest() && checkUnLockRequest(false)) {
      currentState = SYSTEMSTATES.OPENANDUNLOCKED;
    } else if (Doors.getInstance().allDoorsClosed()) {
      Timer.getInstance().initActivateAlarmTime();
      currentState = SYSTEMSTATES.CLOSEDANDLOCKED;
    }
  }

  private void closedAndLocked() throws AlarmException {
    if (PinCode.getInstance().isUnlockRequest() && checkUnLockRequest(false)) {
      currentState = SYSTEMSTATES.CLOSEDANDUNLOCKED;
    } else if (!Doors.getInstance().allDoorsClosed()) {
      currentState = SYSTEMSTATES.OPENANDLOCKED;
    } else if (Timer.getInstance().getActivateAlarmTime() <= 0) {
      System.out.println("CLOSEDANDLOCKED: goto ARMED");
      currentState = SYSTEMSTATES.ARMED;
    }
  }

  private void setPinCode() throws AlarmException {
    if (PinCode.getInstance().getSubmittedPinCode() == current_pin_code) {
      int new_pin_code = PinCode.getInstance().getNewPinCode();
      if (new_pin_code >= 100 && new_pin_code < 1000) {
        wrong_new_pin_count = 3;
        current_pin_code = new_pin_code;
        System.out.println("newPinSet");
      }
    } else {
      --wrong_new_pin_count;
      if (wrong_new_pin_count <= 0)
        throw new AlarmException();
    }

    currentState = SYSTEMSTATES.OPENANDUNLOCKED;
  }
  
  private void armed() throws AlarmException {
    if (PinCode.getInstance().isUnlockRequest() && checkUnLockRequest(false)) {
      currentState = SYSTEMSTATES.CLOSEDANDUNLOCKED;
      System.out.println("ARMED: goto CLOSEDANDUNLOCKED");
    } else if (!Doors.getInstance().allDoorsClosed()) {
      System.out.println("ARMED: goto ALARM");
      throw new AlarmException();
    }
  }

  // ----------------------------- Helper Methods -----------------------------

  private boolean checkUnLockRequest(boolean update_lockstate) throws AlarmException {
    if (current_pin_code == PinCode.getInstance().getSubmittedPinCode()) {
      Doors.getInstance().setDoorsLocked(update_lockstate);
      wrong_un_lock_pin_count = 3;
      return true;
    }

    --wrong_un_lock_pin_count;
    if (wrong_un_lock_pin_count <= 0)
      throw new AlarmException();

    return false;
  }

}
