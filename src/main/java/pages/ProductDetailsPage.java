package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class ProductDetailsPage extends BasePage<ProductDetailsPage> {

    // LOCATORS
    private final By TITLE = By.className("inventory_details_name");
    private final By DESCRIPTION = By.className("inventory_details_desc");
    private final By PRICE = By.className("inventory_details_price");
    private final By ADD_TO_CART = By.cssSelector("button[id^='add-to-cart']");
    private final By REMOVE_FROM_CART = By.cssSelector("button[id^='remove']");
    private final By BACK_BUTTON = By.id("back-to-products");

    // CONSTRUCTOR
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    // ASSERTIONS

    @Step("Verify product title: {expectedName}")
    public ProductDetailsPage assertItemTitle(String expectedName) {
        log.info("Asserting product title");
        log.info("Expected title: " + expectedName);

        String actual = driver.findElement(TITLE).getText();
        Assert.assertEquals(actual, expectedName, "Product title mismatch!");

        log.info("Actual title matched: " + actual);
        return this;
    }

    @Step("Verify product price: {expectedPrice}")
    public ProductDetailsPage assertItemPrice(String expectedPrice) {
        log.info("Asserting product price");
        log.info("Expected price: " + expectedPrice);

        String actual = driver.findElement(PRICE).getText();
        Assert.assertEquals(actual, expectedPrice, "Product price mismatch!");

        log.info("Actual price matched: " + actual);
        return this;
    }

    @Step("Verify product description is visible")
    public ProductDetailsPage assertDescriptionVisible() {
        log.info("Checking if description is visible");

        Assert.assertTrue(driver.findElement(DESCRIPTION).isDisplayed(),
                "Description NOT visible!");

        log.info("Description is visible");
        return this;
    }

    // ACTIONS

    @Step("Add product to cart from details page")
    public ProductDetailsPage addToCart() {
        log.info("Adding product to cart");
        actionsbot.click(ADD_TO_CART);
        log.info("Product added successfully");
        return this;
    }

    @Step("Remove product from cart from details page")
    public ProductDetailsPage removeFromCart() {
        log.info("Removing product from cart");
        actionsbot.click(REMOVE_FROM_CART);
        log.info("Product removed successfully");
        return this;
    }

    // NAVIGATION

    @Step("Click 'Back to Products' button")
    public ProductsPage backToProducts() {
        log.info("Navigating back to products page");
        actionsbot.click(BACK_BUTTON);
        log.info("Returned to products page");
        return new ProductsPage(driver);
    }
}
