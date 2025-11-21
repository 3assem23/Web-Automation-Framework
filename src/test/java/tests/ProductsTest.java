package tests;

import io.qameta.allure.*;
import org.testng.annotations.Test;
import pages.ProductsPage;
import utils.AllureLogger;


@Epic("E-Commerce")
@Feature("Products Page")
public class ProductsTest extends TestBase {


    @Test(
            priority = 1,
            groups = {"smoke", "regression", "products"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Products page displays correctly")
    public void verifyProductsTitleTest() {
        AllureLogger.logStep("Verifying products page title");

        loginAsUser()
                .assertProductsTitle("Products");

        AllureLogger.logStep("Products title verified");
    }


    @Test(
            priority = 2,
            groups = {"smoke", "regression", "cart"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("User can add products to cart")
    public void Add2ItemsTest() {
        AllureLogger.logStep("Adding 2 items to cart");

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .addToCartById(products.BIKE_LIGHT_ID)
                .assertCartBadgeCount(2);

        AllureLogger.logStep("Successfully added 2 items");
    }


    @Test(
            priority = 3,
            groups = {"regression", "cart"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can add all products to cart")
    public void AddAllItemsTest() {
        AllureLogger.logStep("Adding all items to cart");

        loginAsUser()
                .addAllItems()
                .assertCartBadgeCount(6);

        AllureLogger.logStep("All 6 items added successfully");
    }


    @Test(
            priority = 4,
            groups = {"regression", "cart"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can remove all products from cart")
    public void RemoveAllItemsTest() {
        AllureLogger.logStep("Testing add all then remove all");

        loginAsUser()
                .addAllItems()
                .assertCartBadgeCount(6)
                .removeAllItems()
                .assertCartBadgeCount(0);

        AllureLogger.logStep("Cart successfully emptied");
    }


    @Test(
            priority = 5,
            groups = {"smoke", "regression", "cart"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("User can remove products from cart")
    public void AddRemoveTest() {
        AllureLogger.logStep("Testing add and remove single item");

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .assertCartBadgeCount(1)
                .removeFromCartById(products.REMOVE_BACKPACK_ID)
                .assertCartBadgeCount(0);

        AllureLogger.logStep("Add/Remove cycle completed");
    }


    @Test(
            priority = 6,
            groups = {"regression", "cart"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Cart maintains correct state")
    public void AddRemoveAddTest() {
        AllureLogger.logStep("Testing add-remove-add cycle");

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .assertCartBadgeCount(1)
                .removeFromCartById(products.REMOVE_BACKPACK_ID)
                .assertCartBadgeCount(0)
                .addToCartById(products.BACKPACK_ID)
                .assertCartBadgeCount(1);

        AllureLogger.logStep("Cart state maintained correctly");
    }


    @Test(
            priority = 7,
            groups = {"regression", "sorting"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Products can be sorted by name")
    public void sortNameAToZTest() {
        AllureLogger.logStep("Testing sort by name A to Z");

        loginAsUser()
                .sortByNameAToZ()
                .assertSortedByNameAscending();

        AllureLogger.logStep("Products sorted A to Z successfully");
    }


    @Test(
            priority = 8,
            groups = {"regression", "sorting"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Products can be sorted by name")
    public void sortNameZToATest() {
        AllureLogger.logStep("Testing sort by name Z to A");

        loginAsUser()
                .sortByNameZToA()
                .assertSortedByNameDescending();

        AllureLogger.logStep("Products sorted Z to A successfully");
    }


    @Test(
            priority = 9,
            groups = {"regression", "sorting"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Products can be sorted by price")
    public void sortPriceLowToHighTest() {
        AllureLogger.logStep("Testing sort by price Low to High");

        loginAsUser()
                .sortByPriceLowToHigh()
                .assertSortedByPriceAscending();

        AllureLogger.logStep("Products sorted Low to High successfully");
    }


    @Test(
            priority = 10,
            groups = {"regression", "sorting"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Products can be sorted by price")
    public void sortPriceHighToLowTest() {
        AllureLogger.logStep("Testing sort by price High to Low");

        loginAsUser()
                .sortByPriceHighToLow()
                .assertSortedByPriceDescending();

        AllureLogger.logStep("Products sorted High to Low successfully");
    }


    @Test(
            priority = 11,
            groups = {"regression", "navigation"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can view product details")
    public void testOpenProductByNameAndVerifyDetails() {
        AllureLogger.logStep("Testing product navigation by name");

        loginAsUser()
                .openProductByName("Sauce Labs Backpack")
                .assertItemTitle("Sauce Labs Backpack")
                .assertItemPrice("$29.99")
                .assertDescriptionVisible();

        AllureLogger.logStep("Product details verified");
    }


    @Test(
            priority = 12,
            groups = {"regression", "navigation"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can view product details")
    public void testOpenProductByImageAndVerifyDetails() {
        AllureLogger.logStep("Testing product navigation by image");

        loginAsUser()
                .openProductByImage(0)
                .assertItemTitle("Sauce Labs Backpack")
                .assertItemPrice("$29.99")
                .assertDescriptionVisible();

        AllureLogger.logStep("Product image navigation verified");
    }


    @Test(
            priority = 13,
            groups = {"regression", "navigation"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("All products have accessible detail pages")
    public void testVerifyAllProductsByName() {
        AllureLogger.logStep("Verifying all products via name links");

        ProductsPage products = loginAsUser();

        for (String name : products.getAllProductNames()) {
            AllureLogger.logInfo("Checking product: " + name);
            products.openProductByName(name)
                    .assertItemTitle(name)
                    .assertDescriptionVisible()
                    .backToProducts();
        }

        AllureLogger.logStep("All products verified via name navigation");
    }


    @Test(
            priority = 14,
            groups = {"regression", "navigation"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("All product images are clickable")
    public void testVerifyAllProductsByImage() {
        AllureLogger.logStep("Verifying all products via image links");

        ProductsPage products = loginAsUser();
        int totalProducts = products.getAllProductImages().size();

        for (int i = 0; i < totalProducts; i++) {
            String expectedName = products.getAllProductNames().get(i);
            AllureLogger.logInfo("Checking product image " + (i + 1) + ": " + expectedName);

            products.openProductByImage(i)
                    .assertItemTitle(expectedName)
                    .assertDescriptionVisible()
                    .backToProducts();
        }

        AllureLogger.logStep("All products verified via image navigation");
    }


    @Test(
            priority = 15,
            groups = {"smoke", "regression", "navigation"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("User can navigate to cart")
    public void GotoCartTest() {
        AllureLogger.logStep("Testing navigation to cart page");

        loginAsUser()
                .goToCart()
                .assertAtCartPage();

        AllureLogger.logStep("Cart navigation verified");
    }
}