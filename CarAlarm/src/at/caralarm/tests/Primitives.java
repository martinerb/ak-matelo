package at.caralarm.tests;

import java.lang.reflect.Field;

import org.junit.Assert;

import at.caralarm.Alarm;
import at.caralarm.Alarmsystem;
import at.caralarm.Alarmsystem.SYSTEMSTATES;
import at.caralarm.Doors;
import at.caralarm.PinCode;
import at.caralarm.Timer;

public class Primitives {

  public Primitives() {
  }

  public static void resetFactorys() throws Exception {
    System.out.println("Reset Factorys!!");

    // Reset instance Variables to null-pointer to get new classes every test
    // case
    Field field;
    field = Alarm.class.getDeclaredField("instance");
    field.setAccessible(true);
    field.set(PinCode.class, null);

    field = Alarmsystem.class.getDeclaredField("instance");
    field.setAccessible(true);
    field.set(PinCode.class, null);

    field = Doors.class.getDeclaredField("instance");
    field.setAccessible(true);
    field.set(PinCode.class, null);

    field = PinCode.class.getDeclaredField("instance");
    field.setAccessible(true);
    field.set(PinCode.class, null);

    field = Timer.class.getDeclaredField("instance");
    field.setAccessible(true);
    field.set(PinCode.class, null);

    System.gc();
  }

  // ---------------------------------------------------------------------

  // InittoOAU
  public void init() {
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    Assert.assertEquals("OpenAndUnlocked => ClosedAndUnlocked wrong startstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.OPENANDUNLOCKED);
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());
  }

  // OpenAndUnlocked => ClosedAndUnlocked
  public void OAUtoCAU() {
    // precondition
    Assert.assertEquals("OpenAndUnlocked => ClosedAndUnlocked wrong startstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.OPENANDUNLOCKED);
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());

    Doors.getInstance().setDoorsClosed(true);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    Assert.assertEquals("OpenAndUnlocked => ClosedAndUnlocked wrong endstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.CLOSEDANDUNLOCKED);
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());
  }

  // ClosedAndUnlocked => OpenAndUnlocked
  public void CAUtoOAU() {
    // precondition
    Assert.assertEquals("ClosedAndUnlocked => OpenAndUnlocked wrong startstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.CLOSEDANDUNLOCKED);
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());

    Doors.getInstance().setDoorsClosed(false);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    Assert.assertEquals("ClosedAndUnlocked => OpenAndUnlocked wrong endstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.OPENANDUNLOCKED);
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());

  }

  // OpenAndUnlocked => OpenAndLocked
  public void OAUtoOAL() {
    // precondition
    Assert.assertEquals("OpenAndUnlocked => OpenAndLocked wrong startstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.OPENANDUNLOCKED);
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());

    PinCode.getInstance().setLockRequest(true);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    Assert.assertEquals("OpenAndUnlocked => OpenAndLocked wrong endstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.OPENANDLOCKED);
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
  }

  // OpenAndLocked => OpenAndUnlocked
  public void OALtoOAU(int pin_code) {
    Assert.assertEquals("OpenAndLocked => OpenAndUnlocked wrong startstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.OPENANDLOCKED);
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());

    boolean right_pin_code = Alarmsystem.getInstance().getCurrentPinCode() == pin_code ? true : false;
    PinCode.getInstance().setSubmittedPinCode(pin_code);
    PinCode.getInstance().setUnlockRequest(true);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    if (right_pin_code) {
      Assert.assertEquals("OpenAndLocked => OpenAndUnlocked wrong endstate!", Alarmsystem.getInstance()
          .getSystemstate(), SYSTEMSTATES.OPENANDUNLOCKED);
      Assert.assertFalse(Doors.getInstance().allDoorsClosed());
      Assert.assertFalse(Doors.getInstance().allDoorsLocked());
    } else {
      Assert.assertEquals("OpenAndLocked => OpenAndUnlocked wrong pin!", Alarmsystem.getInstance().getSystemstate(),
          SYSTEMSTATES.OPENANDLOCKED);
      Assert.assertFalse(Doors.getInstance().allDoorsClosed());
      Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    }
  }

  // ---------------------------------------------------------------------

  // ClosedAndUnlocked => ClosedAndLocked
  public void CAUtoCAL() {
    // precondition
    Assert.assertEquals("ClosedAndUnlocked => ClosedAndLocked wrong startstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.CLOSEDANDUNLOCKED);
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());

    PinCode.getInstance().setLockRequest(true);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    Assert.assertEquals("ClosedAndUnlocked => ClosedAndLocked wrong endstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.CLOSEDANDLOCKED);
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    Assert.assertEquals("wrong aktivate_alarm_time", 20, Timer.getInstance().getActivateAlarmTime());
  }

  // OpenAndLocked => ClosedAndLocked
  public void OALtoCAL() {
    // precondition
    Assert.assertEquals("OpenAndLocked => ClosedAndLocked wrong startstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.OPENANDLOCKED);
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());

    Doors.getInstance().setDoorsClosed(true);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    Assert.assertEquals("OpenAndLocked => ClosedAndLocked wrong endstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.CLOSEDANDLOCKED);
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    Assert.assertEquals("wrong aktivate_alarm_time", 20, Timer.getInstance().getActivateAlarmTime());
  }

  // ClosedAndLocked => ClosedAndUnlocked
  public void CALtoCAU(int pin_code) {
    // precondition
    Assert.assertEquals("ClosedAndLocked => ClosedAndUnlocked wrong startstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.CLOSEDANDLOCKED);
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());

    boolean right_pin_code = Alarmsystem.getInstance().getCurrentPinCode() == pin_code ? true : false;
    PinCode.getInstance().setSubmittedPinCode(pin_code);
    PinCode.getInstance().setUnlockRequest(true);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    if (right_pin_code) {
      Assert.assertEquals("ClosedAndLocked => ClosedAndUnlocked wrong endstate!", Alarmsystem.getInstance()
          .getSystemstate(), SYSTEMSTATES.CLOSEDANDUNLOCKED);
      Assert.assertTrue(Doors.getInstance().allDoorsClosed());
      Assert.assertFalse(Doors.getInstance().allDoorsLocked());
    } else {
      Assert.assertEquals("ClosedAndLocked => ClosedAndUnlocked wrong pincode!", Alarmsystem.getInstance()
          .getSystemstate(), SYSTEMSTATES.CLOSEDANDLOCKED);
      Assert.assertTrue(Doors.getInstance().allDoorsClosed());
      Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    }
  }

  // ClosedAndLocked => OpenAndLocked
  public void CALtoOAL() {
    // precondition
    Assert.assertEquals("ClosedAndLocked => OpenAndLocked wrong startstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.CLOSEDANDLOCKED);
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());

    Doors.getInstance().setDoorsClosed(false);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    Assert.assertEquals("ClosedAndLocked => OpenAndLocked wrong endstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.OPENANDLOCKED);
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
  }

  // ---------------------------------------------------------------------

  // ClosedAndLocked => Armed
  public void CALtoArmed(int time_step) {
    // precondition
    Assert.assertEquals("ClosedAndLocked => Armed wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.CLOSEDANDLOCKED);
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    int rest_time = Timer.getInstance().getActivateAlarmTime() - time_step;
    Timer.getInstance().increaseTime(time_step);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    if ((rest_time == Timer.getInstance().getActivateAlarmTime() || rest_time < 0) && rest_time <= 0) {
      Assert.assertEquals("ClosedAndLocked => Armed wrong endstate!", Alarmsystem.getInstance().getSystemstate(),
          SYSTEMSTATES.ARMED);
      Assert.assertTrue(Doors.getInstance().allDoorsClosed());
      Assert.assertTrue(Doors.getInstance().allDoorsLocked());
      Assert.assertTrue("wrong aktivate_alarm_time", Timer.getInstance().getActivateAlarmTime() <= 0);
    } else {
      Assert.assertEquals("ClosedAndLocked => ClosedAndLocked wrong endstate!", Alarmsystem.getInstance()
          .getSystemstate(), SYSTEMSTATES.CLOSEDANDLOCKED);
      Assert.assertTrue(Doors.getInstance().allDoorsClosed());
      Assert.assertTrue(Doors.getInstance().allDoorsLocked());
      Assert.assertTrue("wrong aktivate_alarm_time", Timer.getInstance().getActivateAlarmTime() > 0);
    }

  }

  // Armed => ClosedAndUnlocked
  public void ArmedtoCAU(int pin_code) {
    // precondition
    Assert.assertEquals("Armed => ClosedAndUnlocked wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.ARMED);
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());

    int counter = Alarmsystem.getInstance().getWrongUnlockPinCount();
    boolean right_pin_code = Alarmsystem.getInstance().getCurrentPinCode() == pin_code ? true : false;
    PinCode.getInstance().setSubmittedPinCode(pin_code);
    PinCode.getInstance().setUnlockRequest(true);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    if (right_pin_code) {
      Assert.assertEquals("Armed => ClosedAndUnlocked wrong endstate!", Alarmsystem.getInstance()
          .getSystemstate(), SYSTEMSTATES.CLOSEDANDUNLOCKED);
      Assert.assertTrue(Doors.getInstance().allDoorsClosed());
      Assert.assertFalse(Doors.getInstance().allDoorsLocked());
    } else {
      if (counter == 1) {
        Assert.assertEquals("Armed => Armed wrong pincode! and no try left", Alarmsystem.getInstance().getSystemstate(),
            SYSTEMSTATES.ALARM);
        Doors.getInstance().setDoorsClosed(false);
      } else {
        Assert.assertEquals("Armed => Armed wrong pincode! at least one try left", Alarmsystem.getInstance().getSystemstate(),
            SYSTEMSTATES.ARMED);
        Assert.assertTrue(Doors.getInstance().allDoorsClosed());
        Assert.assertTrue(Doors.getInstance().allDoorsLocked());
      }
    }
  }

  // Armed => Alarm
  public void ArmedtoAlarmFAS() {
    // precondition
    Assert.assertEquals("Armed => Alarm wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.ARMED);
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    Assert.assertTrue(Timer.getInstance().getActivateAlarmTime() <= 0);

    Doors.getInstance().setDoorsClosed(false);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    Assert.assertEquals("Armed => Alarm wrong endstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.ALARM);
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    Assert.assertTrue(Timer.getInstance().getDeactivateFlashTime() > 0);
    Assert.assertTrue(Timer.getInstance().getDeactivateSoundTime() > 0);

  }

  // ---------------------------------------------------------------------

  // AlarmFAStoOAU
  public void AlarmFAStoOAU(int pin_code) {
    // precondition
    Assert.assertEquals("AlarmFAStoOAU wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.ALARM);
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());

    boolean right_pin_code = Alarmsystem.getInstance().getCurrentPinCode() == pin_code ? true : false;
    PinCode.getInstance().setSubmittedPinCode(pin_code);
    PinCode.getInstance().setUnlockRequest(true);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    if (right_pin_code) {
      Assert.assertEquals("ClosedAndLocked => ClosedAndUnlocked wrong endstate!", Alarmsystem.getInstance()
          .getSystemstate(), SYSTEMSTATES.OPENANDUNLOCKED);
      Doors.getInstance().setDoorsClosed(false);
      Assert.assertFalse(Doors.getInstance().allDoorsLocked());
    } else {
      Assert.assertEquals("Armed => Armed wrong pincode!", Alarmsystem.getInstance().getSystemstate(),
          SYSTEMSTATES.ALARM);
      Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    }
  }

  // AlarmFlash => OpenAndUnlocked
  public void AlarmFlashtoOAU(int pin_code) {
    // precondition
    Assert.assertEquals("Alarm => OpenAndUnlocked wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.ALARM);
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    Assert.assertTrue(Timer.getInstance().getDeactivateSoundTime() <= 0);
    Assert.assertFalse(Timer.getInstance().getDeactivateFlashTime() <= 0);

    boolean right_pin_code = Alarmsystem.getInstance().getCurrentPinCode() == pin_code ? true : false;
    PinCode.getInstance().setSubmittedPinCode(pin_code);
    PinCode.getInstance().setUnlockRequest(true);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    if (right_pin_code) {
      Assert.assertEquals("Alarm => OpenAndUnlocked wrong endstate!", Alarmsystem.getInstance().getSystemstate(),
          SYSTEMSTATES.OPENANDUNLOCKED);
      Doors.getInstance().setDoorsClosed(false);
      Assert.assertFalse(Doors.getInstance().allDoorsLocked());
    } else {
      Assert.assertEquals("Alarm => Alarm wrong pincode!", Alarmsystem.getInstance().getSystemstate(),
          SYSTEMSTATES.ALARM);
      Assert.assertTrue(Doors.getInstance().allDoorsLocked());
      Assert.assertTrue(Timer.getInstance().getDeactivateSoundTime() <= 0);
      Assert.assertFalse(Timer.getInstance().getDeactivateFlashTime() <= 0);
    }
  }

  // Alarm => SilentAndOpen
  public void AlarmtoSAO(int time_step) {
    // precondition
    Assert.assertEquals("Alarm => SilentAndOpen wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.ALARM);
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());

    int rest_time = Timer.getInstance().getDeactivateFlashTime() - time_step;
    Timer.getInstance().increaseTime(time_step);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    if ((rest_time == Timer.getInstance().getDeactivateFlashTime() || rest_time < 0) && rest_time <= 0) {
      Assert.assertEquals("Alarm => SilentAndOpen wrong endstate!", Alarmsystem.getInstance().getSystemstate(),
          SYSTEMSTATES.SILENTANDOPEN);
      Assert.assertFalse(Doors.getInstance().allDoorsClosed());
      Assert.assertTrue(Doors.getInstance().allDoorsLocked());
      Assert.assertTrue("wrong aktivate_alarm_time", Timer.getInstance().getDeactivateFlashTime() <= 0);
    } else {
      Assert.assertEquals("Alarm => Alarm wrong time!", Alarmsystem.getInstance().getSystemstate(), SYSTEMSTATES.ALARM);
      Assert.assertFalse(Doors.getInstance().allDoorsClosed());
      Assert.assertTrue(Doors.getInstance().allDoorsLocked());
      Assert.assertTrue("wrong aktivate_alarm_time", Timer.getInstance().getDeactivateFlashTime() > 0);
    }
  }

  // AlarmFAS => AlarmFlash
  public void AlarmFAStoAlarmFlash(int time_step) {
    // precondition
    Assert.assertEquals("AlarmFAS => AlarmFlash wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.ALARM);
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    Assert.assertTrue(Timer.getInstance().getDeactivateSoundTime() > 0);
    Assert.assertTrue(Timer.getInstance().getDeactivateFlashTime() > 0);

    int rest_time = Timer.getInstance().getDeactivateSoundTime() - time_step;
    Timer.getInstance().increaseTime(time_step);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    if ((rest_time == Timer.getInstance().getDeactivateSoundTime() || rest_time < 0) && rest_time <= 0) {
      Assert.assertEquals("AlarmFAS => AlarmFlash wrong endstate!", Alarmsystem.getInstance().getSystemstate(),
          SYSTEMSTATES.ALARM);
      Assert.assertTrue(Doors.getInstance().allDoorsLocked());
      Assert.assertTrue("wrong aktivate_alarm_time", Timer.getInstance().getDeactivateSoundTime() <= 0);
      Assert.assertTrue("wrong aktivate_alarm_time", Timer.getInstance().getDeactivateFlashTime() > 0);
    } else {
      Assert.assertEquals("Alarm => Alarm wrong time!", Alarmsystem.getInstance().getSystemstate(), SYSTEMSTATES.ALARM);
      Assert.assertFalse(Doors.getInstance().allDoorsClosed());
      Assert.assertTrue(Doors.getInstance().allDoorsLocked());
      Assert.assertTrue("wrong aktivate_alarm_time", Timer.getInstance().getDeactivateSoundTime() > 0);
      Assert.assertTrue("wrong aktivate_alarm_time", Timer.getInstance().getDeactivateFlashTime() > 0);
    }
  }

  // SilentAndOpen => OpenAndUnlocked
  public void SAOtoOAU(int pin_code) {
    // precondition
    Assert.assertEquals("SilentAndOpen => OpenAndUnlocked wrong startstate!", Alarmsystem.getInstance()
        .getSystemstate(), SYSTEMSTATES.SILENTANDOPEN);
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Timer.getInstance().getDeactivateSoundTime() <= 0);
    Assert.assertTrue(Timer.getInstance().getDeactivateFlashTime() <= 0);

    boolean right_pin_code = Alarmsystem.getInstance().getCurrentPinCode() == pin_code ? true : false;
    PinCode.getInstance().setSubmittedPinCode(pin_code);
    PinCode.getInstance().setUnlockRequest(true);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    if (right_pin_code) {
      Assert.assertEquals("SilentAndOpen => OpenAndUnlocked wrong endstate!", Alarmsystem.getInstance()
          .getSystemstate(), SYSTEMSTATES.OPENANDUNLOCKED);
      Doors.getInstance().setDoorsClosed(false);
      Assert.assertFalse(Doors.getInstance().allDoorsLocked());
    } else {
      Assert.assertEquals("SilentAndOpen => SilentAndOpen wrong pincode!", Alarmsystem.getInstance().getSystemstate(),
          SYSTEMSTATES.SILENTANDOPEN);
      Assert.assertTrue(Doors.getInstance().allDoorsLocked());
      Assert.assertTrue(Timer.getInstance().getDeactivateSoundTime() <= 0);
      Assert.assertTrue(Timer.getInstance().getDeactivateFlashTime() <= 0);
    }
  }

  // SilentAndOpen => Armed
  public void SAOtoArmed() {
    // precondition
    Assert.assertEquals("SilentAndOpen => Armed wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.SILENTANDOPEN);
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Timer.getInstance().getDeactivateSoundTime() <= 0);
    Assert.assertTrue(Timer.getInstance().getDeactivateFlashTime() <= 0);

    Doors.getInstance().setDoorsClosed(true);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    Assert.assertEquals("SilentAndOpen => Armed wrong pincode!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.ARMED);
    Assert.assertTrue(Doors.getInstance().allDoorsLocked());
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
    Assert.assertTrue(Timer.getInstance().getDeactivateSoundTime() <= 0);
    Assert.assertTrue(Timer.getInstance().getDeactivateFlashTime() <= 0);
  }

  // ---------------------------------------------------------------------

  // OpenAndUnlocked => PinEntry
  public void OAUtoPinEntry() {
    // precondition
    Assert.assertEquals("OpenAndUnlocked => PinEntry wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.OPENANDUNLOCKED);
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());

    PinCode.getInstance().setChangePinCode(true);
    Alarmsystem.getInstance().stateTransition();

    // precondition
    Assert.assertEquals("OpenAndUnlocked => PinEntry wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.PINENTRY);
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());
    Assert.assertFalse(Doors.getInstance().allDoorsClosed());

  }

  // ClosedAndUnlocked => PinEntry
  public void CAUtoPinEntry() {
    // precondition
    Assert.assertEquals("ClosedAndUnlocked => PinEntry wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.CLOSEDANDUNLOCKED);
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());

    PinCode.getInstance().setChangePinCode(true);
    Alarmsystem.getInstance().stateTransition();

    // precondition
    Assert.assertEquals("ClosedAndUnlocked => PinEntry wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.PINENTRY);
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());
    Assert.assertTrue(Doors.getInstance().allDoorsClosed());
  }

  // PinEntry => OpenAndUnlocked
  public void PinEntrytoOAU(int submitted_pin, int new_pin) {
    // precondition
    Assert.assertEquals("PinEntry => OpenAndUnlocked wrong startstate!", Alarmsystem.getInstance().getSystemstate(),
        SYSTEMSTATES.PINENTRY);
    Assert.assertFalse(Doors.getInstance().allDoorsLocked());

    int counter = Alarmsystem.getInstance().getWrongChangePinCount();
    boolean right_pin_code = Alarmsystem.getInstance().getCurrentPinCode() == submitted_pin ? true : false;
    PinCode.getInstance().setSubmittedPinCode(submitted_pin);
    PinCode.getInstance().setNewPinCode(new_pin);
    Alarmsystem.getInstance().stateTransition();

    // postcondition
    if (right_pin_code) {
      Assert.assertEquals("PinEntry => OpenAndUnlocked wrong endstate!", Alarmsystem.getInstance().getSystemstate(),
          SYSTEMSTATES.OPENANDUNLOCKED);
      Assert.assertFalse(Doors.getInstance().allDoorsLocked());
      Doors.getInstance().setDoorsClosed(false);
      if (new_pin >= 100 && new_pin < 1000) {
        Assert.assertTrue(Alarmsystem.getInstance().getCurrentPinCode() == new_pin);
      }
    } else {
      if (counter == 1) {
        Assert.assertEquals("PinEntry => Alarm wrong pincode!", Alarmsystem.getInstance().getSystemstate(),
            SYSTEMSTATES.ALARM);
        Doors.getInstance().setDoorsClosed(false);
        Doors.getInstance().setDoorsLocked(true);
        Assert.assertTrue(Timer.getInstance().getDeactivateFlashTime() > 0);
        Assert.assertTrue(Timer.getInstance().getDeactivateSoundTime() > 0);
      } else {
        Assert.assertEquals("PinEntry => Alarm wrong pincode at least one try left!", Alarmsystem.getInstance()
            .getSystemstate(), SYSTEMSTATES.OPENANDUNLOCKED);
        Doors.getInstance().setDoorsClosed(false);
        Assert.assertTrue(Alarmsystem.getInstance().getWrongChangePinCount() == (counter - 1));
        Assert.assertTrue(Alarmsystem.getInstance().getWrongChangePinCount() > 0);
      }
    }
  }

  // ---------------------------------------------------------------------

}