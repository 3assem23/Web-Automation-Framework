package tests;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.ProductsPage;
import utils.AllureLogger;


@Epic("E-Commerce")
@Feature("Shopping Cart")
public class CartTests extends TestBase {


    @Test(
            priority = 1,
            groups = {"smoke", "regression", "cart"}
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("Cart displays added items correctly")
    public void AddTwoItemsAndVerifyCartTest() {
        AllureLogger.logStep("Starting test: Add 2 items and verify cart");

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .addToCartById(products.BIKE_LIGHT_ID)
                .assertCartBadgeCount(2)
                .goToCart()
                .assertAtCartPage()
                .assertCartItemCount(2)
                .assertCartContains("Sauce Labs Backpack")
                .assertCartContains("Sauce Labs Bike Light");

        AllureLogger.logStep("Cart verification completed");
    }


    @Test(
            priority = 2,
            groups = {"smoke", "regression", "cart"},
            dependsOnMethods = {"AddTwoItemsAndVerifyCartTest"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("User can remove items from cart")
    public void RemoveItemFromCartTest() {
        AllureLogger.logStep("Starting test: Remove item from cart");

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .assertCartBadgeCount(1)
                .goToCart()
                .removeItemById(products.REMOVE_BACKPACK_ID)
                .assertCartItemCount(0);

        AllureLogger.logStep("Item removed successfully");
    }


    @Test(
            priority = 3,
            groups = {"regression", "cart", "navigation"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can continue shopping from cart")
    public void ContinueShoppingTest() {
        AllureLogger.logStep("Starting test: Continue shopping");

        ProductsPage products = loginAsUser();

        products.addToCartById(products.ONESIE_ID)
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

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .addToCartById(products.BIKE_LIGHT_ID)
                .addToCartById(products.ONESIE_ID)
                .assertCartBadgeCount(3)
                .goToCart()
                .removeAllItems()
                .assertCartItemCount(0);

        AllureLogger.logStep("Cart emptied, badge disappeared");
    }


    @Test(
            priority = 5,
            groups = {"regression", "cart"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Cart maintains state across navigation")
    public void VerifyCartPersistsItemsAcrossNavigation() {
        AllureLogger.logStep("Testing cart persistence");

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .assertCartBadgeCount(1);

        CartPage cartPage = products.goToCart();
        cartPage.assertCartItemCount(1)
                .assertCartContains("Sauce Labs Backpack")
                .continueShopping()
                .goToCart()
                .assertCartItemCount(1)
                .assertCartContains("Sauce Labs Backpack");

        AllureLogger.logStep("Cart persistence verified");
    }
}