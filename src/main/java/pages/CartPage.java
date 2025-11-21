package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.components.CartComponent;


public class CartPage extends BasePage<CartPage> {

    private final CartComponent cart;

    //LOCATORS
    private final By cartTitle = By.className("title");
    private final By checkoutButton = By.id("checkout");
    private final By continueShoppingButton = By.id("continue-shopping");

    //CONSTRUCTOR
    public CartPage(WebDriver driver) {
        super(driver);
        this.cart = new CartComponent(driver);
    }

    //ASSERTIONS

    @Step("Verify user is on cart page")
    public CartPage assertAtCartPage() {
        Assert.assertEquals(actionsbot.getText(cartTitle).trim(), "Your Cart",
                "Not at cart page!");
        return this;
    }

    @Step("Verify cart contains {expected} items")
    public CartPage assertCartItemCount(int expected) {
        Assert.assertEquals(cart.getItemCount(), expected,
                "Cart count mismatch");
        return this;
    }

    @Step("Verify cart contains product: {product}")
    public CartPage assertCartContains(String product) {
        Assert.assertTrue(cart.getItemNames().contains(product),
                "Cart does not contain: " + product);
        return this;
    }

    //CART ACTIONS

    @Step("Remove item from cart by ID: {id}")
    public CartPage removeItemById(String id) {
        cart.removeItemById(id);
        return this;
    }

    @Step("Remove item from cart by name: {name}")
    public CartPage removeItemByName(String name) {
        cart.removeItemByName(name);
        return this;
    }

    @Step("Remove all items from cart")
    public CartPage removeAllItems() {
        cart.removeAllItems();
        return this;
    }

    //NAVIGATION

    @Step("Click 'Checkout' button")
    public CheckoutPage proceedToCheckout() {
        actionsbot.click(checkoutButton);
        return new CheckoutPage(driver);
    }

    @Step("Click 'Continue Shopping' button")
    public ProductsPage continueShopping() {
        actionsbot.click(continueShoppingButton);
        return new ProductsPage(driver);
    }
}