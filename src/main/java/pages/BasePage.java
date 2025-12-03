package pages;

import bots.ActionsBot;
import bots.Waitsbot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public abstract class BasePage<T extends BasePage<T>> {

    protected final WebDriver driver;
    protected final ActionsBot actionsbot;
    protected final Waitsbot waitsbot;
    protected final Logger log = LogManager.getLogger(getClass());

    private final By CART_BADGE = By.className("shopping_cart_badge");
    private final By CART_ICON = By.className("shopping_cart_link");

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.actionsbot = new ActionsBot(driver);
        this.waitsbot = new Waitsbot(driver);
    }

    // SAFE logging method
    protected void logInfo(String message) {
        log.info(message);
    }

    public CartPage goToCart() {
        logInfo("Navigating to Cart page");
        actionsbot.click(CART_ICON);
        return new CartPage(driver);
    }

    public int getCartBadgeCount() {
        String text = actionsbot.getText(CART_BADGE);
        logInfo("Reading cart badge count: " + text);
        return (text == null || text.isEmpty()) ? 0 : Integer.parseInt(text);
    }

    @SuppressWarnings("unchecked")
    public T assertCartBadgeCount(int expectedCount) {
        logInfo("Asserting cart badge count = " + expectedCount);
        if (expectedCount == 0) {
            waitsbot.waitForElementToDisappear(CART_BADGE);
        } else {
            int actual = getCartBadgeCount();
            Assert.assertEquals(actual, expectedCount, "Cart badge count mismatch");
        }
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    protected T assertTitleEquals(By locator, String expected) {
        String actual = actionsbot.getText(locator);
        logInfo("Asserting page title: expected '" + expected + "', actual '" + actual + "'");
        Assert.assertEquals(actual, expected, "Page title mismatch");
        return (T) this;
    }
}
