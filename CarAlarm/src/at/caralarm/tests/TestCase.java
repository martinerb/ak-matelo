package at.caralarm.tests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class TestCase {

  @Before
  public void setUp() throws Exception {
    Primitives.resetFactorys();
  }

  @Test
  public void testFirstTestCase() {
    Primitives testCase = new Primitives();

    fail("Not yet implemented");
  }

  @Test
  public void testSecondTestCase() {
    Primitives testCase = new Primitives();

    assertTrue("Not yet implemented", false);
  }

}
