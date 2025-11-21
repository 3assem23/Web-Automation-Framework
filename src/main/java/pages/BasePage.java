package pages;

import bots.ActionsBot;
import bots.Waitsbot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public abstract class BasePage<T extends BasePage<T>> {

    protected final WebDriver driver;
    protected final ActionsBot actionsbot;
    protected final Waitsbot waitsbot;

    private final By CART_BADGE = By.className("shopping_cart_badge");
    private final By CART_ICON = By.className("shopping_cart_link");

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.actionsbot = new ActionsBot(driver);
        this.waitsbot = new Waitsbot(driver);
    }


    public CartPage goToCart() {
        actionsbot.click(CART_ICON);
        return new CartPage(driver);
    }


    public int getCartBadgeCount() {
        String text = actionsbot.getText(CART_BADGE);
        return (text == null || text.isEmpty()) ? 0 : Integer.parseInt(text);
    }


    @SuppressWarnings("unchecked")
    public T assertCartBadgeCount(int expectedCount) {
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
        Assert.assertEquals(actual, expected, "Page title mismatch");
        return (T) this;
    }
}
