package org.webrtc.kite.janustutorial.steps;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.steps.TestStep;
import io.cosmosoftware.kite.interfaces.Runner;
import org.webrtc.kite.janustutorial.pages.MainPage;
import org.webrtc.kite.janustutorial.pages.StreamPage;

public class StartStreamStep extends TestStep {

    private final StreamPage streamPage;


    public StartStreamStep(Runner runner) {
        super(runner);
        this.streamPage = new StreamPage(runner);
    }


    @Override
    public String stepDescription() {
        return "Start Stream ";
    }

    @Override
    protected void step() throws KiteTestException {
        streamPage.clickStreamsList();
        streamPage.clickChooseLiveStreamButton();
        streamPage.clickWatchButton();
    }
}
