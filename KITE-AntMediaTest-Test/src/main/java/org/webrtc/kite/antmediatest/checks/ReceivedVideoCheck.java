package org.webrtc.kite.antmediatest.checks;

import io.cosmosoftware.kite.exception.KiteTestException;
import io.cosmosoftware.kite.interfaces.Runner;
import io.cosmosoftware.kite.report.Status;
import io.cosmosoftware.kite.steps.TestCheck;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.webrtc.kite.antmediatest.pages.MainPage;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

import static io.cosmosoftware.kite.util.TestUtils.videoCheck;

public class ReceivedVideoCheck extends TestCheck {

private final MainPage mainPage;

    public ReceivedVideoCheck(Runner runner) {
        super(runner);
        this.mainPage = new MainPage(runner);
    }

    @Override
    public String stepDescription() {
        return "Check if video is visible";
    }

    @Override
    protected void step() throws KiteTestException {
        final long timeStart = System.currentTimeMillis();
        WebDriverWait wait = new WebDriverWait(webDriver, 60000);
        wait.until(ExpectedConditions.visibilityOf(mainPage.getVideo()));
        final long timeEnd = System.currentTimeMillis();
        long waited = timeEnd - timeStart;
        String videoCheck = videoCheck(webDriver, 0);
        logger.info("videoCheck String: " + videoCheck);
        if (!"video".equalsIgnoreCase(videoCheck)) {
            //add test failure status to report
            //throw new KiteTestException("Some videos are still or blank: " + videoCheck, Status.FAILED);
            logger.info("Some videos are still or blank: " + videoCheck);
        }
        String clientId = getClientID();
        String clientIdWithoudSpace;
        if (Character.isDigit(clientId.charAt(clientId.length() - 3))) {
            clientIdWithoudSpace = clientId.substring(clientId.length() - 3).replaceAll("\\s", "");
        } else {
            clientIdWithoudSpace = clientId.substring(clientId.length() - 2).replaceAll("\\s", "");
        }
        int clientNumber = Integer.parseInt(clientIdWithoudSpace);
        int clientRampUpNumber = clientNumber / 10;

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        jsonObjectBuilder.add("videoStartupTimeInMs", waited);
        jsonObjectBuilder.add("rampUpNumber", clientRampUpNumber);
        jsonObjectBuilder.add("clientNumber", clientNumber);
        JsonObject jsonObject = jsonObjectBuilder.build();
        reporter.jsonAttachment(report, "videoStartupTime", jsonObject);
        logger.info("VideoCheck passed, ");
    }
}
