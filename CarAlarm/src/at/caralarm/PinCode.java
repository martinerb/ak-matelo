package at.caralarm;

public class PinCode {

  private static PinCode instance;
  private int new_pin_code;
  private int submitted_pin_code;
  private boolean lock_request;
  private boolean unlock_request;

  private PinCode() {
    lock_request = false;
    unlock_request = false;
  }

  public static PinCode getInstance() {
    if ( instance == null )
      instance = new PinCode();
    return instance;
  }

  public int getNewPinCode() {
    return new_pin_code;
  }

  public void setNewPinCode(int new_pin_code) {
    this.new_pin_code = new_pin_code;
  }

  public int getSubmittedPinCode() {
    return submitted_pin_code;
  }

  public void setSubmittedPinCode(int submitted_pin_code) {
    this.submitted_pin_code = submitted_pin_code;
  }

  public boolean isLockRequest() {
    return lock_request;
  }

  public void setLockRequest(boolean lock_request) {
    this.lock_request = lock_request;
  }

  public boolean isUnlockRequest() {
    return unlock_request;
  }

  public void setUnlockRequest(boolean unlock_request) {
    this.unlock_request = unlock_request;
  }

}
