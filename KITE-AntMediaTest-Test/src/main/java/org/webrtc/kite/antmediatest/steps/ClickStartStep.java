package org.webrtc.kite.antmediatest.steps;


import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.steps.TestStep;
import org.webrtc.kite.antmediatest.pages.MainPage;

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
