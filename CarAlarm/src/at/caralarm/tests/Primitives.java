package at.caralarm.tests;

import java.lang.reflect.Field;

import at.caralarm.Alarm;
import at.caralarm.Alarmsystem;
import at.caralarm.Doors;
import at.caralarm.PinCode;
import at.caralarm.Timer;

public class Primitives {

  public Primitives() {
  }

  public static void resetFactorys() throws Exception {
    System.out.println("Reset Factorys!!");

    // Reset instance Variables to null-pointer to get new classes every test case
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

  // OpenAndUnlocked => ClosedAndUnlocked
  public void OAUtoCAU() {

  }

  // ClosedAndUnlocked => OpenAndUnlocked
  public void CAUtoOAU() {

  }

  // OpenAndUnlocked => OpenAndLocked
  public void OAUtoOAL() {

  }

  // OpenAndLocked => OpenAndUnlocked
  public void OALtoOAU() {

  }

//---------------------------------------------------------------------

  // ClosedAndUnlocked => ClosedAndLocked
  public void CAUtoCAL() {

  }

  // OpenAndLocked => ClosedAndLocked
  public void OALtoCAL() {

  }

  // ClosedAndLocked => ClosedAndUnlocked
  public void CALtoCAU() {

  }

  // ClosedAndLocked => OpenAndLocked
  public void CALtoOAL() {

  }

//---------------------------------------------------------------------

  // ClosedAndLocked => Armed
  public void CALtoArmed(int time_step) {

  }

  // Armed => ClosedAndUnlocked
  public void ArmedtoCAU() {

  }

  // Armed => Alarm
  public void ArmedtoAlarm() {

  }

//---------------------------------------------------------------------

  // Alarm => OpenAndUnlocked
  public void AlarmtoOAU() {

  }

  // Alarm => SilentAndOpen
  public void AlarmtoSAO() {

  }

  // SilentAndOpen => OpenAndUnlocked
  public void SAOtoOAU() {

  }

  // SilentAndOpen => Armed
  public void SAOtoArmed() {

  }

//---------------------------------------------------------------------

  // OpenAndUnlocked => PinEntry
  public void OAUtoPinEntry() {

  }

  // ClosedAndUnlocked => PinEntry
  public void CAUtoPinEntry() {

  }

  // PinEntry => OpenAndUnlocked
  public void PinEntrytoOAU() {

  }

//---------------------------------------------------------------------

}
