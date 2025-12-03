package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.components.CartComponent;

public class CartPage extends BasePage<CartPage> {

    private final CartComponent cart;

    // LOCATORS
    private final By cartTitle = By.className("title");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    // CONSTRUCTOR
    public CartPage(WebDriver driver) {
        super(driver);
        this.cart = new CartComponent(driver);
    }

    // ASSERTIONS

    @Step("Verify user is on cart page")
    public CartPage assertAtCartPage() {
        log.info("Asserting user is on cart page");
        String actual = actionsbot.getText(cartTitle).trim();

        log.info("Actual title: " + actual);
        Assert.assertEquals(actual, "Your Cart", "Not at cart page!");

        log.info("User is on Cart page");
        return this;
    }

    @Step("Verify cart contains {expected} items")
    public CartPage assertCartItemCount(int expected) {
        log.info("Asserting cart item count");
        int actual = cart.getItemCount();

        log.info("Expected count: " + expected);
        log.info("Actual count: " + actual);

        Assert.assertEquals(actual, expected, "Cart count mismatch");
        log.info("Cart item count is correct");
        return this;
    }

    @Step("Verify cart contains product: {product}")
    public CartPage assertCartContains(String product) {
        log.info("Checking cart contains product: " + product);

        boolean exists = cart.getItemNames().contains(product);
        log.info("Product found? " + exists);

        Assert.assertTrue(exists, "Cart does not contain: " + product);
        log.info("Cart contains product successfully");
        return this;
    }

    // ACTIONS

    @Step("Remove item from cart by ID: {id}")
    public CartPage removeItemById(String id) {
        log.info("Removing item by ID: " + id);
        cart.removeItemById(id);
        log.info("Item removed successfully");
        return this;
    }

    @Step("Remove item from cart by name: {name}")
    public CartPage removeItemByName(String name) {
        log.info("Removing item by name: " + name);
        cart.removeItemByName(name);
        log.info("Item removed successfully");
        return this;
    }

    @Step("Remove all items from cart")
    public CartPage removeAllItems() {
        log.info("Removing all items from cart");
        cart.removeAllItems();
        log.info("All items removed successfully");
        return this;
    }

    // NAVIGATION

    @Step("Click 'Checkout' button")
    public CheckoutPage proceedToCheckout() {
        log.info("Clicking checkout button");
        actionsbot.click(checkoutButton);
        log.info("Navigated to checkout page");
        return new CheckoutPage(driver);
    }

    @Step("Click 'Continue Shopping' button")
    public ProductsPage continueShopping() {
        log.info("Clicking continue shopping button");
        actionsbot.click(continueShoppingButton);
        log.info("Navigated back to products page");
        return new ProductsPage(driver);
    }
}
