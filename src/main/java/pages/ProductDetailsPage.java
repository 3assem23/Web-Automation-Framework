package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;


public class ProductDetailsPage extends BasePage<ProductDetailsPage> {

    //LOCATORS
    private final By TITLE = By.className("inventory_details_name");
    private final By DESCRIPTION = By.className("inventory_details_desc");
    private final By PRICE = By.className("inventory_details_price");
    private final By ADD_TO_CART = By.cssSelector("button[id^='add-to-cart']");
    private final By REMOVE_FROM_CART = By.cssSelector("button[id^='remove']");
    private final By BACK_BUTTON = By.id("back-to-products");

    //CONSTRUCTOR
    public ProductDetailsPage(WebDriver driver) {
        super(driver);
    }

    //ASSERTIONS

    @Step("Verify product title: {expectedName}")
    public ProductDetailsPage assertItemTitle(String expectedName) {
        String actual = driver.findElement(TITLE).getText();
        Assert.assertEquals(actual, expectedName, "Product title mismatch!");
        return this;
    }

    @Step("Verify product price: {expectedPrice}")
    public ProductDetailsPage assertItemPrice(String expectedPrice) {
        String actual = driver.findElement(PRICE).getText();
        Assert.assertEquals(actual, expectedPrice, "Product price mismatch!");
        return this;
    }

    @Step("Verify product description is visible")
    public ProductDetailsPage assertDescriptionVisible() {
        Assert.assertTrue(driver.findElement(DESCRIPTION).isDisplayed(),
                "Description NOT visible!");
        return this;
    }

    //ACTIONS

    @Step("Add product to cart from details page")
    public ProductDetailsPage addToCart() {
        actionsbot.click(ADD_TO_CART);
        return this;
    }

    @Step("Remove product from cart from details page")
    public ProductDetailsPage removeFromCart() {
        actionsbot.click(REMOVE_FROM_CART);
        return this;
    }

    //NAVIGATION

    @Step("Click 'Back to Products' button")
    public ProductsPage backToProducts() {
        actionsbot.click(BACK_BUTTON);
        return new ProductsPage(driver);
    }
}