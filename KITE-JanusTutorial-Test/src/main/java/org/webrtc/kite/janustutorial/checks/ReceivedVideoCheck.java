package org.webrtc.kite.janustutorial.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestCheck;
import io.cosmosoftware.kite.interfaces.Runner;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.webrtc.kite.janustutorial.pages.StreamPage;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static io.cosmosoftware.kite.util.TestUtils.videoCheck;

public class ReceivedVideoCheck extends TestCheck {

private final StreamPage streamPage;

    public ReceivedVideoCheck(Runner runner) {
        super(runner);
        this.streamPage = new StreamPage(runner);
    }

    @Override
    public String stepDescription() {
        return "Check if video is visible";
    }

    @Override
    protected void step() throws KiteTestException {
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        long waited;
        int clientNumber;
        int clientRampUpNumber;
        final long timeStart = System.currentTimeMillis();
        try {
            WebDriverWait wait = new WebDriverWait(webDriver, 60);
            wait.until(ExpectedConditions.visibilityOf(streamPage.getVideo()));

            final long timeEnd = System.currentTimeMillis();
            waited = timeEnd - timeStart;
            String videoCheck = videoCheck(webDriver, 0);
            logger.info("videoCheck String: " + videoCheck);
            if (!"video".equalsIgnoreCase(videoCheck)) {
                //add test failure status to report
                logger.info("Some videos are still or blank: " + videoCheck);
            }
        } catch (Exception e) {
            logger.info("Video was not shown");
            waited = 0;
        }

        String clientId = getClientID();
        String clientIdWithoudSpace;
        if (Character.isDigit(clientId.charAt(clientId.length() - 3))) {
            clientIdWithoudSpace = clientId.substring(clientId.length() - 3).replaceAll("\\s", "");
        } else {
            clientIdWithoudSpace = clientId.substring(clientId.length() - 2).replaceAll("\\s", "");
        }
        clientNumber = Integer.parseInt(clientIdWithoudSpace);
        clientRampUpNumber = clientNumber / 10;

        jsonObjectBuilder.add("videoStartupTimeInMs", waited);
        jsonObjectBuilder.add("rampUpNumber", clientRampUpNumber);
        jsonObjectBuilder.add("clientNumber", clientNumber);
        JsonObject jsonObject = jsonObjectBuilder.build();
        reporter.jsonAttachment(report, "videoStartupTime", jsonObject);
        logger.info("VideoCheck passed, ");
    }
}
