package tests;

import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ProductsPage;
import utils.AllureLogger;
import utils.JsonDataReader;

import java.util.List;
import java.util.Map;


@Epic("E-Commerce")
@Feature("Products Page")
public class ProductsTest extends TestBase {

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


    // TESTS

    @Test(
            priority = 1,
            groups = {"smoke", "products"}
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
            groups = {"smoke", "cart"},
            dataProvider = "twoProducts"
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("User can add products to cart")
    public void Add2ItemsTest(Map<String, String> product1, Map<String, String> product2) {
        AllureLogger.logStep("Adding 2 items to cart");
        AllureLogger.logInfo("Product 1: " + product1.get("name"));
        AllureLogger.logInfo("Product 2: " + product2.get("name"));

        ProductsPage products = loginAsUser();

        products.addToCartById(product1.get("addButtonId"))
                .addToCartById(product2.get("addButtonId"))
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

        int totalProducts = JsonDataReader.getProducts().size();

        loginAsUser()
                .addAllItems()
                .assertCartBadgeCount(totalProducts);

        AllureLogger.logStep("All " + totalProducts + " items added successfully");
    }


    @Test(
            priority = 4,
            groups = {"regression", "cart"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can remove all products from cart")
    public void RemoveAllItemsTest() {
        AllureLogger.logStep("Testing add all then remove all");

        int totalProducts = JsonDataReader.getProducts().size();

        loginAsUser()
                .addAllItems()
                .assertCartBadgeCount(totalProducts)
                .removeAllItems()
                .assertCartBadgeCount(0);

        AllureLogger.logStep("Cart successfully emptied");
    }


    @Test(
            priority = 5,
            groups = {"smoke", "cart"},
            dataProvider = "singleProduct"
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("User can remove products from cart")
    public void AddRemoveTest(Map<String, String> product) {
        AllureLogger.logStep("Testing add and remove single item");
        AllureLogger.logInfo("Product: " + product.get("name"));

        ProductsPage products = loginAsUser();

        products.addToCartById(product.get("addButtonId"))
                .assertCartBadgeCount(1)
                .removeFromCartById(product.get("removeButtonId"))
                .assertCartBadgeCount(0);

        AllureLogger.logStep("Add/Remove cycle completed");
    }


    @Test(
            priority = 6,
            groups = {"regression", "cart"},
            dataProvider = "singleProduct"
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Cart maintains correct state")
    public void AddRemoveAddTest(Map<String, String> product) {
        AllureLogger.logStep("Testing add-remove-add cycle");
        AllureLogger.logInfo("Product: " + product.get("name"));

        ProductsPage products = loginAsUser();

        products.addToCartById(product.get("addButtonId"))
                .assertCartBadgeCount(1)
                .removeFromCartById(product.get("removeButtonId"))
                .assertCartBadgeCount(0)
                .addToCartById(product.get("addButtonId"))
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
            groups = {"regression", "navigation"},
            dataProvider = "singleProduct"
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can view product details")
    public void OpenProductByNameAndVerifyDetailsTest(Map<String, String> product) {
        AllureLogger.logStep("Testing product navigation by name");
        AllureLogger.logInfo("Product: " + product.get("name"));

        loginAsUser()
                .openProductByName(product.get("name"))
                .assertItemTitle(product.get("name"))
                .assertItemPrice(product.get("price"))
                .assertDescriptionVisible();

        AllureLogger.logStep("Product details verified");
    }


    @Test(
            priority = 12,
            groups = {"regression", "navigation"},
            dataProvider = "singleProduct"
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can view product details")
    public void OpenProductByImageAndVerifyDetailsTest(Map<String, String> product) {
        AllureLogger.logStep("Testing product navigation by image");

        loginAsUser()
                .openProductByImage(0)
                .assertItemTitle(product.get("name"))
                .assertItemPrice(product.get("price"))
                .assertDescriptionVisible();

        AllureLogger.logStep("Product image navigation verified");
    }


    @Test(
            priority = 13,
            groups = {"regression", "navigation"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("All products have accessible detail pages")
    public void VerifyAllProductsByNameTest() {
        AllureLogger.logStep("Verifying all products via name links");

        ProductsPage products = loginAsUser();
        List<Map<String, String>> allProducts = JsonDataReader.getProducts();

        for (Map<String, String> product : allProducts) {
            AllureLogger.logInfo("Checking product: " + product.get("name"));
            products.openProductByName(product.get("name"))
                    .assertItemTitle(product.get("name"))
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
    public void VerifyAllProductsByImageTest() {
        AllureLogger.logStep("Verifying all products via image links");

        ProductsPage products = loginAsUser();
        List<Map<String, String>> allProducts = JsonDataReader.getProducts();

        for (int i = 0; i < allProducts.size(); i++) {
            String expectedName = allProducts.get(i).get("name");
            String expectedPrice = allProducts.get(i).get("price");

            AllureLogger.logInfo("Checking product image " + (i + 1) + ": " + expectedName);

            products.openProductByImage(i)
                    .assertItemTitle(expectedName)
                    .assertItemPrice(expectedPrice)
                    .assertDescriptionVisible()
                    .backToProducts();
        }

        AllureLogger.logStep("All products verified via image navigation");
    }


    @Test(
            priority = 15,
            groups = {"smoke", "navigation"}
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