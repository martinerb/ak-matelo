package at.caralarm;

public class PinCode {

  private static PinCode instance;
  private int new_pin_code;
  private int submitted_pin_code;
  private boolean lock_request;
  private boolean unlock_request;
  private boolean change_pin_code;
  
  private PinCode() {
    lock_request = false;
    unlock_request = false;
    this.change_pin_code = false;
    new_pin_code = 0;
    submitted_pin_code = 0;
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

  public void setChangePinCode(boolean set_pin_code) {
    this.change_pin_code = set_pin_code;
  }
  
  public boolean getChangePinCode() {
    boolean tmp = this.change_pin_code;
    change_pin_code = false;
    return tmp;
  }
  public int getSubmittedPinCode() {
    int pin_code = submitted_pin_code;
    submitted_pin_code = 0;
    return pin_code;
  }

  public void setSubmittedPinCode(int submitted_pin_code) {
    this.submitted_pin_code = submitted_pin_code;
  }

  public boolean isLockRequest() {
    boolean tmp_request = lock_request;
    lock_request = false;
    return tmp_request;
  }

  public void setLockRequest(boolean lock_request) {
    this.lock_request = lock_request;
  }

  public boolean isUnlockRequest() {
    boolean tmp_request = unlock_request;
    unlock_request = false;
    return tmp_request;
  }

  public void setUnlockRequest(boolean unlock_request) {
    this.unlock_request = unlock_request;
  }

}
