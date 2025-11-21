package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProductsPage extends BasePage<ProductsPage> {

    //LOCATORS
    private final By productsTitle = By.className("title");
    private static final By PRODUCT_NAMES = By.className("inventory_item_name");
    private static final By PRODUCT_IMAGES = By.xpath("//div[@class='inventory_item_img']/a/img");
    private static final By PRODUCT_PRICES = By.className("inventory_item_price");
    private static final By SORT_DROPDOWN = By.className("product_sort_container");

    //PRODUCT BUTTON IDs
    public final String BACKPACK_ID = "add-to-cart-sauce-labs-backpack";
    public final String BIKE_LIGHT_ID = "add-to-cart-sauce-labs-bike-light";
    public final String BOLT_TSHIRT_ID = "add-to-cart-sauce-labs-bolt-t-shirt";
    public final String FLEECE_JACKET_ID = "add-to-cart-sauce-labs-fleece-jacket";
    public final String ONESIE_ID = "add-to-cart-sauce-labs-onesie";
    public final String RED_TSHIRT_ID = "add-to-cart-test.allthethings()-t-shirt-(red)";

    public final String REMOVE_BACKPACK_ID = "remove-sauce-labs-backpack";
    public final String REMOVE_BIKE_LIGHT_ID = "remove-sauce-labs-bike-light";
    public final String REMOVE_BOLT_TSHIRT_ID = "remove-sauce-labs-bolt-t-shirt";
    public final String REMOVE_JACKET_ID = "remove-sauce-labs-fleece-jacket";
    public final String REMOVE_ONESIE_ID = "remove-sauce-labs-onesie";
    public final String REMOVE_RED_TSHIRT_ID = "remove-test.allthethings()-t-shirt-(red)";

    //BUTTON ARRAYS
    public final String[] ALL_ADD_BUTTONS = {
            BACKPACK_ID, BIKE_LIGHT_ID, BOLT_TSHIRT_ID,
            FLEECE_JACKET_ID, ONESIE_ID, RED_TSHIRT_ID
    };

    public final String[] ALL_REMOVE_BUTTONS = {
            REMOVE_BACKPACK_ID, REMOVE_BIKE_LIGHT_ID, REMOVE_BOLT_TSHIRT_ID,
            REMOVE_JACKET_ID, REMOVE_ONESIE_ID, REMOVE_RED_TSHIRT_ID
    };

    //CONSTRUCTOR
    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    //ASSERTIONS

    @Step("Verify products page title: {expected}")
    public ProductsPage assertProductsTitle(String expected) {
        Assert.assertEquals(actionsbot.getText(productsTitle), expected,
                "Page title mismatch");
        return this;
    }


    @Step("Verify products are sorted by name A to Z")
    public ProductsPage assertSortedByNameAscending() {
        List<String> uiNames = getProductNames();
        List<String> sorted = new ArrayList<>(uiNames);
        Collections.sort(sorted);
        Assert.assertEquals(uiNames, sorted, "Names are NOT sorted A -> Z");
        return this;
    }

    @Step("Verify products are sorted by name Z to A")
    public ProductsPage assertSortedByNameDescending() {
        List<String> uiNames = getProductNames();
        List<String> sorted = new ArrayList<>(uiNames);
        sorted.sort(Collections.reverseOrder());
        Assert.assertEquals(uiNames, sorted, "Names are NOT sorted Z -> A");
        return this;
    }

    @Step("Verify products are sorted by price Low to High")
    public ProductsPage assertSortedByPriceAscending() {
        List<Double> ui = getProductPrices();
        List<Double> sorted = new ArrayList<>(ui);
        Collections.sort(sorted);
        Assert.assertEquals(ui, sorted, "Prices NOT sorted Low -> High");
        return this;
    }

    @Step("Verify products are sorted by price High to Low")
    public ProductsPage assertSortedByPriceDescending() {
        List<Double> ui = getProductPrices();
        List<Double> sorted = new ArrayList<>(ui);
        sorted.sort(Collections.reverseOrder());
        Assert.assertEquals(ui, sorted, "Prices NOT sorted High -> Low");
        return this;
    }

    //CART ACTIONS

    @Step("Add item to cart - Button ID: {addButtonId}")
    public ProductsPage addToCartById(String addButtonId) {
        actionsbot.click(By.id(addButtonId));
        return this;
    }

    @Step("Remove item from cart - Button ID: {removeButtonId}")
    public ProductsPage removeFromCartById(String removeButtonId) {
        actionsbot.click(By.id(removeButtonId));
        return this;
    }

    @Step("Add all products to cart (6 items)")
    public ProductsPage addAllItems() {
        for (String id : ALL_ADD_BUTTONS) {
            actionsbot.click(By.id(id));
        }
        return this;
    }

    @Step("Remove all products from cart")
    public ProductsPage removeAllItems() {
        for (String id : ALL_REMOVE_BUTTONS) {
            actionsbot.click(By.id(id));
        }
        return this;
    }

    //SORTING ACTIONS


    private ProductsPage sort(String visibleText) {
        WebElement dropdown = driver.findElement(SORT_DROPDOWN);
        new Select(dropdown).selectByVisibleText(visibleText);
        return this;
    }

    @Step("Sort products by name (A to Z)")
    public ProductsPage sortByNameAToZ() {
        return sort("Name (A to Z)");
    }

    @Step("Sort products by name (Z to A)")
    public ProductsPage sortByNameZToA() {
        return sort("Name (Z to A)");
    }

    @Step("Sort products by price (Low to High)")
    public ProductsPage sortByPriceLowToHigh() {
        return sort("Price (low to high)");
    }

    @Step("Sort products by price (High to Low)")
    public ProductsPage sortByPriceHighToLow() {
        return sort("Price (high to low)");
    }

    //NAVIGATION

    @Step("Open product details by name: {name}")
    public ProductDetailsPage openProductByName(String name) {
        driver.findElement(By.linkText(name)).click();
        return new ProductDetailsPage(driver);
    }

    @Step("Open product by image index: {index}")
    public ProductDetailsPage openProductByImage(int index) {
        List<WebElement> images = driver.findElements(PRODUCT_IMAGES);
        WebElement targetImage = images.get(index);

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", targetImage);

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        targetImage.click();

        return new ProductDetailsPage(driver);
    }


    //GETTERS

    @Step("Get all product names from page")
    public List<String> getAllProductNames() {
        return driver.findElements(PRODUCT_NAMES)
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    @Step("Get all product images from page")
    public List<WebElement> getAllProductImages() {
        return driver.findElements(PRODUCT_IMAGES);
    }

    private List<String> getProductNames() {
        return driver.findElements(PRODUCT_NAMES)
                .stream()
                .map(WebElement::getText)
                .toList();
    }

    private List<Double> getProductPrices() {
        return driver.findElements(PRODUCT_PRICES)
                .stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }
}