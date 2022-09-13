package org.webrtc.kite.antmediatest.pages;

import io.cosmosoftware.kite.pages.BasePage;
import io.cosmosoftware.kite.interfaces.Runner;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static io.cosmosoftware.kite.util.WebDriverUtils.loadPage;

public class MainPage extends BasePage {

  @FindBy(xpath= "//*[@id=\"start_play_button\"]")
  private WebElement startButton;
  @FindBy(xpath= "//*[@id=\"remoteVideo\"]")
  private WebElement video;

  public MainPage(Runner runner) {
    super(runner);
  }

  public void open(String url) {
    loadPage(url, 20);
  }

  public void clickStart() {
    WebDriverWait wait = new WebDriverWait(webDriver, 10);
    wait.until(ExpectedConditions.visibilityOf(startButton));
    startButton.click();
  }

  public WebElement getVideo() {
    return this.video;
  }

}
