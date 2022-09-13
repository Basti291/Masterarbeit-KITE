package org.webrtc.kite.antmediatest;

import io.cosmosoftware.kite.steps.ScreenshotStep;
import org.webrtc.kite.antmediatest.checks.ReceivedVideoCheck;
import org.webrtc.kite.antmediatest.steps.ClickStartStep;
import org.webrtc.kite.antmediatest.steps.CollectStatsStep;
import org.webrtc.kite.antmediatest.steps.OpenUrlStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;

public class KiteAntMediaTestTest extends KiteBaseTest {

  @Override
  protected void payloadHandling() {
    super.payloadHandling();
    if (this.payload != null) {
      //process payload here
    }
  }

  @Override
  public void populateTestSteps(TestRunner runner) {
    runner.addStep(new OpenUrlStep(runner, url));
    runner.addStep(new ClickStartStep(runner));
    runner.addStep(new ReceivedVideoCheck(runner));
    runner.addStep(new ScreenshotStep(runner));
    runner.addStep(new CollectStatsStep(runner));
  }

}
