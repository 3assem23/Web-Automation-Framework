package tests;

import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ProductsPage;
import utils.JsonDataReader;

import java.util.List;
import java.util.Map;

@Epic("E-Commerce")
@Feature("Products Page")

public class ProductsTest extends TestBase {

    @Test(priority = 1, groups = {"smoke", "products"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products page displays correct title 'Products'")
    public void verifyProductsTitleTest() {
        loginAsUser().assertProductsTitle("Products");
    }

    @Test(priority = 2, groups = {"smoke", "cart"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can add 2 items to cart and cart badge updates correctly")
    public void Add2ItemsTest() {
        List<Map<String, String>> allProducts = JsonDataReader.getProducts();
        Map<String, String> product1 = allProducts.get(0);
        Map<String, String> product2 = allProducts.get(1);

        ProductsPage products = loginAsUser();

        products.addToCartById(product1.get("addButtonId"))
                .addToCartById(product2.get("addButtonId"))
                .assertCartBadgeCount(2);
    }

    @Test(priority = 3, groups = {"regression", "cart"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can add all 6 products to cart and badge shows 6")
    public void AddAllItemsTest() {
        int totalProducts = JsonDataReader.getProducts().size();

        loginAsUser()
                .addAllItems()
                .assertCartBadgeCount(totalProducts);
    }

    @Test(priority = 4, groups = {"regression", "cart"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that user can add all items then remove all items from cart")
    public void RemoveAllItemsTest() {
        int totalProducts = JsonDataReader.getProducts().size();

        loginAsUser()
                .addAllItems()
                .assertCartBadgeCount(totalProducts)
                .removeAllItems()
                .assertCartBadgeCount(0);
    }

    @Test(priority = 5, groups = {"smoke", "cart"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can add and then remove a single product from cart")
    public void AddRemoveTest() {
        Map<String, String> product = JsonDataReader.getProducts().get(0);
        ProductsPage products = loginAsUser();

        products.addToCartById(product.get("addButtonId"))
                .assertCartBadgeCount(1)
                .removeFromCartById(product.get("removeButtonId"))
                .assertCartBadgeCount(0);
    }

    @Test(priority = 6, groups = {"regression", "cart"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that cart maintains correct state through multiple add/remove operations")
    public void AddRemoveAddTest() {
        Map<String, String> product = JsonDataReader.getProducts().get(0);
        ProductsPage products = loginAsUser();

        products.addToCartById(product.get("addButtonId"))
                .assertCartBadgeCount(1)
                .removeFromCartById(product.get("removeButtonId"))
                .assertCartBadgeCount(0)
                .addToCartById(product.get("addButtonId"))
                .assertCartBadgeCount(1);
    }

    @Test(priority = 7, groups = {"regression", "sorting"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products can be sorted alphabetically from A to Z")
    public void sortNameAToZTest() {
        loginAsUser()
                .sortByNameAToZ()
                .assertSortedByNameAscending();
    }

    @Test(priority = 8, groups = {"regression", "sorting"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products can be sorted alphabetically from Z to A")
    public void sortNameZToATest() {
        loginAsUser()
                .sortByNameZToA()
                .assertSortedByNameDescending();
    }

    @Test(priority = 9, groups = {"regression", "sorting"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products can be sorted by price from low to high")
    public void sortPriceLowToHighTest() {
        loginAsUser()
                .sortByPriceLowToHigh()
                .assertSortedByPriceAscending();
    }

    @Test(priority = 10, groups = {"regression", "sorting"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that products can be sorted by price from high to low")
    public void sortPriceHighToLowTest() {
        loginAsUser()
                .sortByPriceHighToLow()
                .assertSortedByPriceDescending();
    }

    @Test(priority = 11, groups = {"regression", "navigation"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that clicking on product name navigates to product details page with correct information")
    public void testOpenProductByNameAndVerifyDetails() {
        Map<String, String> product = JsonDataReader.getProducts().get(0);
        loginAsUser()
                .openProductByName(product.get("name"))
                .assertItemTitle(product.get("name"))
                .assertItemPrice(product.get("price"))
                .assertDescriptionVisible();
    }

    @Test(priority = 12, groups = {"regression", "navigation"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that clicking on product image navigates to product details page with correct information")
    public void testOpenProductByImageAndVerifyDetails() {
        Map<String, String> product = JsonDataReader.getProducts().get(0);
        loginAsUser()
                .openProductByImage(0)
                .assertItemTitle(product.get("name"))
                .assertItemPrice(product.get("price"))
                .assertDescriptionVisible();
    }

    @Test(priority = 13, groups = {"regression", "navigation"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all products have accessible detail pages and display correct information when clicked by name")
    public void testVerifyAllProductsByName() {
        ProductsPage products = loginAsUser();
        List<Map<String, String>> allProducts = JsonDataReader.getProducts();

        for (Map<String, String> product : allProducts) {
            products.openProductByName(product.get("name"))
                    .assertItemTitle(product.get("name"))
                    .assertDescriptionVisible()
                    .backToProducts();
        }
    }

    @Test(priority = 14, groups = {"regression", "navigation"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that all product images are clickable and navigate to correct product details pages")
    public void testVerifyAllProductsByImage() {
        ProductsPage products = loginAsUser();
        List<Map<String, String>> allProducts = JsonDataReader.getProducts();

        for (int i = 0; i < allProducts.size(); i++) {
            String expectedName = allProducts.get(i).get("name");
            String expectedPrice = allProducts.get(i).get("price");

            products.openProductByImage(i)
                    .assertItemTitle(expectedName)
                    .assertItemPrice(expectedPrice)
                    .assertDescriptionVisible()
                    .backToProducts();
        }
    }

    @Test(priority = 15, groups = {"smoke", "navigation"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that user can navigate to shopping cart page from products page")
    public void GotoCartTest() {
        loginAsUser()
                .goToCart()
                .assertAtCartPage();
    }
}