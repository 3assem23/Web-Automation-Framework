package tests;

import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.ProductsPage;
import utils.AllureLogger;
import utils.JsonDataReader;

import java.util.List;
import java.util.Map;


@Epic("E-Commerce")
@Feature("Shopping Cart")
public class CartTests extends TestBase {

    // DATA PROVIDERS

    @DataProvider(name = "twoProducts")
    public Object[][] twoProducts() {
        List<Map<String, String>> products = JsonDataReader.getProducts();
        return new Object[][]{{products.get(0), products.get(1)}};
    }

    @DataProvider(name = "singleProduct")
    public Object[][] singleProduct() {
        Map<String, String> product = JsonDataReader.getProducts().get(0);
        return new Object[][]{{product}};
    }

    @DataProvider(name = "thirdProduct")
    public Object[][] thirdProduct() {
        Map<String, String> product = JsonDataReader.getProducts().get(4); // Onesie
        return new Object[][]{{product}};
    }


    // TESTS

    @Test(
            priority = 1,
            groups = {"smoke", "cart"},
            dataProvider = "twoProducts"
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("Cart displays added items correctly")
    public void AddTwoItemsAndVerifyCartTest(Map<String, String> product1, Map<String, String> product2) {
        AllureLogger.logStep("Starting test: Add 2 items and verify cart");
        AllureLogger.logInfo("Product 1: " + product1.get("name"));
        AllureLogger.logInfo("Product 2: " + product2.get("name"));

        ProductsPage products = loginAsUser();

        products.addToCartById(product1.get("addButtonId"))
                .addToCartById(product2.get("addButtonId"))
                .assertCartBadgeCount(2)
                .goToCart()
                .assertAtCartPage()
                .assertCartItemCount(2)
                .assertCartContains(product1.get("name"))
                .assertCartContains(product2.get("name"));

        AllureLogger.logStep("Cart verification completed");
    }


    @Test(
            priority = 2,
            groups = {"smoke", "cart"},
            dataProvider = "singleProduct"
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("User can remove items from cart")
    public void RemoveItemFromCartTest(Map<String, String> product) {
        AllureLogger.logStep("Starting test: Remove item from cart");
        AllureLogger.logInfo("Product: " + product.get("name"));

        ProductsPage products = loginAsUser();

        products.addToCartById(product.get("addButtonId"))
                .assertCartBadgeCount(1)
                .goToCart()
                .removeItemById(product.get("removeButtonId"))
                .assertCartItemCount(0);

        AllureLogger.logStep("Item removed successfully");
    }


    @Test(
            priority = 3,
            groups = {"regression", "cart", "navigation"},
            dataProvider = "thirdProduct"
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can continue shopping from cart")
    public void ContinueShoppingTest(Map<String, String> product) {
        AllureLogger.logStep("Starting test: Continue shopping");
        AllureLogger.logInfo("Product: " + product.get("name"));

        ProductsPage products = loginAsUser();

        products.addToCartById(product.get("addButtonId"))
                .goToCart()
                .continueShopping()
                .assertProductsTitle("Products");

        AllureLogger.logStep("Successfully returned to products page");
    }


    @Test(
            priority = 4,
            groups = {"regression", "cart"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Cart badge reflects accurate item count")
    public void VerifyCartBadgeDisappearsWhenEmpty() {
        AllureLogger.logStep("Testing cart badge when emptying cart");

        List<Map<String, String>> products = JsonDataReader.getProducts();

        ProductsPage productsPage = loginAsUser();

        productsPage.addToCartById(products.get(0).get("addButtonId"))
                .addToCartById(products.get(1).get("addButtonId"))
                .addToCartById(products.get(4).get("addButtonId"))
                .assertCartBadgeCount(3)
                .goToCart()
                .removeAllItems()
                .assertCartItemCount(0);

        AllureLogger.logStep("Cart emptied, badge disappeared");
    }


    @Test(
            priority = 5,
            groups = {"regression", "cart"},
            dataProvider = "singleProduct"
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Cart maintains state across navigation")
    public void VerifyCartPersistsItemsAcrossNavigation(Map<String, String> product) {
        AllureLogger.logStep("Testing cart persistence");
        AllureLogger.logInfo("Product: " + product.get("name"));

        ProductsPage products = loginAsUser();

        products.addToCartById(product.get("addButtonId"))
                .assertCartBadgeCount(1);

        CartPage cartPage = products.goToCart();
        cartPage.assertCartItemCount(1)
                .assertCartContains(product.get("name"))
                .continueShopping()
                .goToCart()
                .assertCartItemCount(1)
                .assertCartContains(product.get("name"));

        AllureLogger.logStep("Cart persistence verified");
    }
}