package tests;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.ProductDetailsPage;
import pages.ProductsPage;
import utils.AllureLogger;


@Epic("E-Commerce")
@Feature("Product Details Page")
public class ProductDetailsPageTest extends TestBase {

    @Test(
            priority = 1,
            groups = {"smoke", "regression", "product-details"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Product details display correctly")
    public void verifyProductTitleAndPriceTest() {
        AllureLogger.logStep("Verifying product details display");

        ProductDetailsPage product = loginAsUser()
                .openProductByName("Sauce Labs Backpack");

        product.assertItemTitle("Sauce Labs Backpack")
                .assertItemPrice("$29.99")
                .assertDescriptionVisible();

        AllureLogger.logStep("Product details verified");
    }


    @Test(
            priority = 2,
            groups = {"smoke", "regression", "product-details", "cart"}
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("User can manage cart from product details")
    public void addAndRemoveProductTest() {
        AllureLogger.logStep("Testing add/remove from details");

        ProductDetailsPage product = loginAsUser()
                .openProductByName("Sauce Labs Backpack");

        product.addToCart()
                .assertCartBadgeCount(1)
                .removeFromCart()
                .assertCartBadgeCount(0);

        AllureLogger.logStep("Add/Remove verified");
    }


    @Test(
            priority = 3,
            groups = {"regression", "product-details", "navigation"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can navigate back to products")
    public void backToProductsTest() {
        AllureLogger.logStep("Testing back navigation");

        ProductsPage products = loginAsUser()
                .openProductByName("Sauce Labs Backpack")
                .backToProducts();

        products.assertProductsTitle("Products");

        AllureLogger.logStep("Back navigation verified");
    }


    @Test(
            priority = 4,
            groups = {"regression", "product-details"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("All products have valid detail pages")
    public void verifyAllProductsIndividuallyByNameTest() {
        AllureLogger.logStep("Verifying all products by name");

        ProductsPage products = loginAsUser();

        for (String name : products.getAllProductNames()) {
            AllureLogger.logInfo("Verifying: " + name);
            products.openProductByName(name)
                    .assertItemTitle(name)
                    .assertDescriptionVisible()
                    .backToProducts();
        }

        AllureLogger.logStep("All products verified");
    }


    @Test(
            priority = 5,
            groups = {"regression", "product-details"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("All product images link correctly")
    public void verifyAllProductsIndividuallyByImageTest() {
        AllureLogger.logStep("Verifying all products by image");

        ProductsPage products = loginAsUser();
        int totalProducts = products.getAllProductImages().size();

        for (int i = 0; i < totalProducts; i++) {
            String expectedName = products.getAllProductNames().get(i);
            AllureLogger.logInfo("Verifying image " + (i + 1) + ": " + expectedName);

            products.openProductByImage(i)
                    .assertItemTitle(expectedName)
                    .assertDescriptionVisible()
                    .backToProducts();
        }

        AllureLogger.logStep("All images verified");
    }


    @Test(
            priority = 6,
            groups = {"smoke", "regression", "product-details", "cart"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("User can add product and go to cart")
    public void addProductAndGoToCartTest() {
        AllureLogger.logStep("Testing add and cart navigation");

        loginAsUser()
                .openProductByName("Sauce Labs Backpack")
                .addToCart()
                .goToCart()
                .assertAtCartPage();

        AllureLogger.logStep("Cart navigation verified");
    }


    @Test(
            priority = 7,
            groups = {"regression", "product-details", "cart"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Cart badge maintains accuracy")
    public void verifyMultipleAddRemoveCycles() {
        AllureLogger.logStep("Testing multiple cycles");

        ProductDetailsPage product = loginAsUser()
                .openProductByName("Sauce Labs Bike Light");

        product.addToCart()
                .assertCartBadgeCount(1)
                .removeFromCart()
                .assertCartBadgeCount(0);

        product.addToCart()
                .assertCartBadgeCount(1)
                .removeFromCart()
                .assertCartBadgeCount(0);

        product.addToCart()
                .assertCartBadgeCount(1);

        AllureLogger.logStep("Cycles verified");
    }
}