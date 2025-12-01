package tests;

import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ProductDetailsPage;
import pages.ProductsPage;
import utils.AllureLogger;
import utils.JsonDataReader;

import java.util.List;
import java.util.Map;


@Epic("E-Commerce")
@Feature("Product Details Page")
public class ProductDetailsPageTest extends TestBase {

    // DATA PROVIDERS

    @DataProvider(name = "singleProduct")
    public Object[][] singleProduct() {
        Map<String, String> product = JsonDataReader.getProducts().get(0);
        return new Object[][]{{product}};
    }

    @DataProvider(name = "secondProduct")
    public Object[][] secondProduct() {
        Map<String, String> product = JsonDataReader.getProducts().get(1);
        return new Object[][]{{product}};
    }


    // TESTS

    @Test(
            priority = 1,
            groups = {"smoke", "product-details"},
            dataProvider = "singleProduct"
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Product details display correctly")
    public void verifyProductTitleAndPriceTest(Map<String, String> product) {
        AllureLogger.logStep("Verifying product details display");
        AllureLogger.logInfo("Product: " + product.get("name"));

        ProductDetailsPage productDetails = loginAsUser()
                .openProductByName(product.get("name"));

        productDetails.assertItemTitle(product.get("name"))
                .assertItemPrice(product.get("price"))
                .assertDescriptionVisible();

        AllureLogger.logStep("Product details verified");
    }


    @Test(
            priority = 2,
            groups = {"smoke", "product-details", "cart"},
            dataProvider = "singleProduct"
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("User can manage cart from product details")
    public void addAndRemoveProductTest(Map<String, String> product) {
        AllureLogger.logStep("Testing add/remove from details");
        AllureLogger.logInfo("Product: " + product.get("name"));

        ProductDetailsPage productDetails = loginAsUser()
                .openProductByName(product.get("name"));

        productDetails.addToCart()
                .assertCartBadgeCount(1)
                .removeFromCart()
                .assertCartBadgeCount(0);

        AllureLogger.logStep("Add/Remove verified");
    }


    @Test(
            priority = 3,
            groups = {"regression", "product-details", "navigation"},
            dataProvider = "singleProduct"
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can navigate back to products")
    public void backToProductsTest(Map<String, String> product) {
        AllureLogger.logStep("Testing back navigation");
        AllureLogger.logInfo("Product: " + product.get("name"));

        ProductsPage products = loginAsUser()
                .openProductByName(product.get("name"))
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
        List<Map<String, String>> allProducts = JsonDataReader.getProducts();

        for (Map<String, String> product : allProducts) {
            AllureLogger.logInfo("Verifying: " + product.get("name"));
            products.openProductByName(product.get("name"))
                    .assertItemTitle(product.get("name"))
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
        List<Map<String, String>> allProducts = JsonDataReader.getProducts();

        for (int i = 0; i < allProducts.size(); i++) {
            String expectedName = allProducts.get(i).get("name");
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
            groups = {"smoke", "product-details", "cart"},
            dataProvider = "singleProduct"
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("User can add product and go to cart")
    public void addProductAndGoToCartTest(Map<String, String> product) {
        AllureLogger.logStep("Testing add and cart navigation");
        AllureLogger.logInfo("Product: " + product.get("name"));

        loginAsUser()
                .openProductByName(product.get("name"))
                .addToCart()
                .goToCart()
                .assertAtCartPage();

        AllureLogger.logStep("Cart navigation verified");
    }


    @Test(
            priority = 7,
            groups = {"regression", "product-details", "cart"},
            dataProvider = "secondProduct"
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Cart badge maintains accuracy")
    public void verifyMultipleAddRemoveCycles(Map<String, String> product) {
        AllureLogger.logStep("Testing multiple cycles");
        AllureLogger.logInfo("Product: " + product.get("name"));

        ProductDetailsPage productDetails = loginAsUser()
                .openProductByName(product.get("name"));

        productDetails.addToCart()
                .assertCartBadgeCount(1)
                .removeFromCart()
                .assertCartBadgeCount(0);

        productDetails.addToCart()
                .assertCartBadgeCount(1)
                .removeFromCart()
                .assertCartBadgeCount(0);

        productDetails.addToCart()
                .assertCartBadgeCount(1);

        AllureLogger.logStep("Cycles verified");
    }
}