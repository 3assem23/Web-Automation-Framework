package tests;

import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.ProductsPage;
import utils.JsonDataReader;

import java.util.List;
import java.util.Map;

@Epic("E-Commerce")
@Feature("Shopping Cart")
public class CartTests extends TestBase {

    // TESTS

    @Test(
            priority = 1,
            groups = {"smoke", "cart"}
    )
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify cart displays added items correctly")
    public void AddTwoItemsAndVerifyCartTest() {

        List<Map<String, String>> allProducts = JsonDataReader.getProducts();
        Map<String, String> product1 = allProducts.get(0);
        Map<String, String> product2 = allProducts.get(1);

        ProductsPage products = loginAsUser();

        products.addToCartById(product1.get("addButtonId"))
                .addToCartById(product2.get("addButtonId"))
                .assertCartBadgeCount(2)
                .goToCart()
                .assertAtCartPage()
                .assertCartItemCount(2)
                .assertCartContains(product1.get("name"))
                .assertCartContains(product2.get("name"));
    }

    @Test(
            priority = 2,
            groups = {"smoke", "cart"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can remove items from cart")
    public void RemoveItemFromCartTest() {

        Map<String, String> product = JsonDataReader.getProducts().get(0);
        ProductsPage products = loginAsUser();

        products.addToCartById(product.get("addButtonId"))
                .assertCartBadgeCount(1)
                .goToCart()
                .removeItemById(product.get("removeButtonId"))
                .assertCartItemCount(0);
    }

    @Test(
            priority = 3,
            groups = {"regression", "cart", "navigation"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify user can continue shopping from cart")
    public void ContinueShoppingTest() {

        Map<String, String> product = JsonDataReader.getProducts().get(4);
        ProductsPage products = loginAsUser();

        products.addToCartById(product.get("addButtonId"))
                .goToCart()
                .continueShopping()
                .assertProductsTitle("Products");
    }

    @Test(
            priority = 4,
            groups = {"regression", "cart"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify cart badge reflects accurate item count")
    public void VerifyCartBadgeDisappearsWhenEmpty() {

        List<Map<String, String>> products = JsonDataReader.getProducts();

        ProductsPage productsPage = loginAsUser();

        productsPage.addToCartById(products.get(0).get("addButtonId"))
                .addToCartById(products.get(1).get("addButtonId"))
                .addToCartById(products.get(4).get("addButtonId"))
                .assertCartBadgeCount(3)
                .goToCart()
                .removeAllItems()
                .assertCartItemCount(0);
    }

    @Test(
            priority = 5,
            groups = {"regression", "cart"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify cart maintains state across navigation")
    public void VerifyCartPersistsItemsAcrossNavigation() {

        Map<String, String> product = JsonDataReader.getProducts().get(0);
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
    }
}