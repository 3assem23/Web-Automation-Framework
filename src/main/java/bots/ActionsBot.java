package bots;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import java.util.List;

/**
 * ActionsBot: safe interactions - uses Waitsbot, scrolls before actions,
 * supports WebElement and By overloads and JS fallback for clicks.
 */
public class ActionsBot {

    private final WebDriver driver;
    private final Waitsbot waitsbot;

    public ActionsBot(WebDriver driver) {
        this.driver = driver;
        this.waitsbot = new Waitsbot(driver);
    }

    //Click
    public void click(By locator) {
        waitsbot.waitForPresence(locator);
        waitsbot.fluentwait().until(d -> {
            try {
                WebElement el = d.findElement(locator);
                scrollTo(el);
                try {
                    el.click();
                } catch (WebDriverException e) {
                    jsClick(el);
                }
                return true;
            } catch (Exception ignored) {
                return false;
            }
        });
    }

    // Click using a WebElement instance
    public void click(WebElement element) {
        waitsbot.fluentwait().until(d -> {
            try {
                scrollTo(element);
                try {
                    element.click();
                } catch (WebDriverException e) {
                    jsClick(element);
                }
                return true;
            } catch (Exception ignored) {
                return false;
            }
        });
    }

    //Type
    public void type(By locator, String text) {
        waitsbot.waitForVisibility(locator);
        waitsbot.fluentwait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                scrollTo(element);
                element.clear();
                element.sendKeys(text);
                return true;
            } catch (Exception ignored) {
                return false;
            }
        });
    }

    //Get text
    public String getText(By locator) {
        tries:
        return waitsbot.fluentwait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                scrollTo(element);
                String txt = element.getText();
                return (txt != null && !txt.isEmpty()) ? txt : null;
            } catch (Exception e) {
                return null;
            }
        });
    }

    //Find elements after waiting for presence
    public List<WebElement> finds(By locator) {
        waitsbot.waitForPresence(locator);
        return driver.findElements(locator);
    }

    //Clear
    public void clear(By locator) {
        waitsbot.waitForVisibility(locator);
        waitsbot.fluentwait().until(d -> {
            try {
                WebElement el = d.findElement(locator);
                scrollTo(el);
                el.clear();
                return true;
            } catch (Exception ignored) {
                return false;
            }
        });
    }

    //Select by value
    public void selectByValue(By locator, String value) {
        waitsbot.waitForVisibility(locator);
        waitsbot.fluentwait().until(d -> {
            try {
                WebElement el = d.findElement(locator);
                scrollTo(el);
                new org.openqa.selenium.support.ui.Select(el).selectByValue(value);
                return true;
            } catch (Exception ignored) {
                return false;
            }
        });
    }

    //get Attribute
    public String getAttribute(By locator, String attribute) {
        waitsbot.waitForPresence(locator);
        return waitsbot.fluentwait().until(d -> {
            try {
                WebElement el = d.findElement(locator);
                scrollTo(el);
                return el.getAttribute(attribute);
            } catch (Exception e) {
                return null;
            }
        });
    }

    //Scroll to
    private void scrollTo(WebElement element) {
        try {
            new Actions(driver).scrollToElement(element).perform();
        } catch (Exception ignored) {
            // fallback: JS scrollIntoView
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        }
    }

    //JS click
    private void jsClick(WebElement el) {
        try {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", el);
        } catch (Exception ignored) { }
    }
}
