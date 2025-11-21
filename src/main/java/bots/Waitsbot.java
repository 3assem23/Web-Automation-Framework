package bots;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Waitsbot {

    private final WebDriver driver;

    public Waitsbot(WebDriver driver) {
        this.driver = driver;
    }

    public FluentWait<WebDriver> fluentwait() {
        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(15))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(ElementNotInteractableException.class)
                .ignoring(ElementClickInterceptedException.class);
    }

    public void waitForPresence(By locator) {
        fluentwait().until(d -> !d.findElements(locator).isEmpty());
    }

    public void waitForVisibility(By locator) {
        fluentwait().until(d -> {
            try {
                WebElement e = d.findElement(locator);
                return e.isDisplayed();
            } catch (Exception ex) {
                return false;
            }
        });
    }

    public void waitForElementToDisappear(By locator) {
        fluentwait().until(d -> d.findElements(locator).isEmpty());
    }
}
