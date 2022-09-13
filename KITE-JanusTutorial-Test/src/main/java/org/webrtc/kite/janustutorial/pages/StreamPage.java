package org.webrtc.kite.janustutorial.pages;

import io.cosmosoftware.kite.pages.BasePage;
import io.cosmosoftware.kite.interfaces.Runner;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import static io.cosmosoftware.kite.util.TestUtils.waitAround;
import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;

public class StreamPage extends BasePage {

    @FindBy(xpath= "//*[@id=\"streamset\"]")
    private WebElement streamsListButton;

    @FindBy(xpath= "//*[@id=\"1\"]")
    private WebElement chooseLiveStreamButton;

    @FindBy(xpath= "//*[@id=\"watch\"]")
    private WebElement watchButton;

    @FindBy(xpath= "//*[@id=\"remotevideov\"]")
    private WebElement video;

    public StreamPage(Runner runner) {
        super(runner);
    }

    public void clickStreamsList() {
        WebDriverWait wait = new WebDriverWait(webDriver, 20);
        wait.until(ExpectedConditions.visibilityOf(streamsListButton));
        streamsListButton.click();
    }

    public void clickChooseLiveStreamButton() {
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.visibilityOf(chooseLiveStreamButton));
        chooseLiveStreamButton.click();
    }

    public void clickWatchButton() {
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.visibilityOf(watchButton));
        watchButton.click();
    }

    public WebElement getVideo() {
        return this.video;
    }

}
