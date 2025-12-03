package tests;

import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ProductDetailsPage;
import pages.ProductsPage;
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

    @Test(priority = 1, groups = {"smoke", "product-details"}, dataProvider = "singleProduct")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify product details display correctly")
    public void verifyProductTitleAndPriceTest(Map<String, String> product) {

        ProductDetailsPage productDetails = loginAsUser()
                .openProductByName(product.get("name"));

        productDetails.assertItemTitle(product.get("name"))
                .assertItemPrice(product.get("price"))
                .assertDescriptionVisible();
    }

    @Test(priority = 2, groups = {"smoke", "product-details", "cart"}, dataProvider = "singleProduct")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can manage cart from product details")
    public void addAndRemoveProductTest(Map<String, String> product) {

        ProductDetailsPage productDetails = loginAsUser()
                .openProductByName(product.get("name"));

        productDetails.addToCart()
                .assertCartBadgeCount(1)
                .removeFromCart()
                .assertCartBadgeCount(0);
    }

    @Test(priority = 3, groups = {"regression", "product-details", "navigation"}, dataProvider = "singleProduct")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify user can navigate back to products")
    public void backToProductsTest(Map<String, String> product) {

        ProductsPage products = loginAsUser()
                .openProductByName(product.get("name"))
                .backToProducts();

        products.assertProductsTitle("Products");
    }

    @Test(priority = 4, groups = {"regression", "product-details"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify all products have valid detail pages")
    public void verifyAllProductsIndividuallyByNameTest() {

        ProductsPage products = loginAsUser();
        List<Map<String, String>> allProducts = JsonDataReader.getProducts();

        for (Map<String, String> product : allProducts) {
            products.openProductByName(product.get("name"))
                    .assertItemTitle(product.get("name"))
                    .assertDescriptionVisible()
                    .backToProducts();
        }
    }

    @Test(priority = 5, groups = {"regression", "product-details"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify all product images link correctly")
    public void verifyAllProductsIndividuallyByImageTest() {

        ProductsPage products = loginAsUser();
        List<Map<String, String>> allProducts = JsonDataReader.getProducts();

        for (int i = 0; i < allProducts.size(); i++) {
            String expectedName = allProducts.get(i).get("name");

            products.openProductByImage(i)
                    .assertItemTitle(expectedName)
                    .assertDescriptionVisible()
                    .backToProducts();
        }
    }

    @Test(priority = 6, groups = {"smoke", "product-details", "cart"}, dataProvider = "singleProduct")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify user can add product and go to cart")
    public void addProductAndGoToCartTest(Map<String, String> product) {

        loginAsUser()
                .openProductByName(product.get("name"))
                .addToCart()
                .goToCart()
                .assertAtCartPage();
    }

    @Test(priority = 7, groups = {"regression", "product-details", "cart"}, dataProvider = "secondProduct")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify cart badge maintains accuracy")
    public void verifyMultipleAddRemoveCycles(Map<String, String> product) {

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
    }
}
