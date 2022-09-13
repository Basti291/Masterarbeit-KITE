package org.webrtc.kite.janustutorial.steps;


import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.steps.TestStep;
import io.cosmosoftware.kite.interfaces.Runner;
import org.webrtc.kite.janustutorial.pages.MainPage;

public class ClickStartStep extends TestStep {

    private final MainPage mainPage;


    public ClickStartStep(Runner runner) {
        super(runner);
        this.mainPage = new MainPage(runner);
    }


    @Override
    public String stepDescription() {
        return "Click Start ";
    }

    @Override
    protected void step() throws KiteTestException {

        mainPage.clickStart();
    }
}
