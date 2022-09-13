package org.webrtc.kite.janustutorial;

import io.cosmosoftware.kite.steps.ScreenshotStep;
import org.webrtc.kite.janustutorial.checks.ReceivedVideoCheck;
import org.webrtc.kite.janustutorial.steps.ClickStartStep;
import org.webrtc.kite.janustutorial.steps.CollectStatsStep;
import org.webrtc.kite.janustutorial.steps.OpenUrlStep;
import org.webrtc.kite.janustutorial.steps.StartStreamStep;
import org.webrtc.kite.tests.KiteBaseTest;
import org.webrtc.kite.tests.TestRunner;


public class KiteJanusTutorialTest extends KiteBaseTest {


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
    runner.addStep(new StartStreamStep(runner));
    runner.addStep(new ReceivedVideoCheck(runner));
    runner.addStep(new ScreenshotStep(runner));
    runner.addStep(new CollectStatsStep(runner));
  }

}
