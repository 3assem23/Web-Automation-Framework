package tests;

import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ProductsPage;
import utils.JsonDataReader;

import java.util.List;
import java.util.Map;

@Epic("E-Commerce")
@Feature("Checkout Process")
public class CheckoutTests extends TestBase {

    // DATA PROVIDERS

    @DataProvider(name = "validCustomers")
    public Object[][] validCustomers() {
        List<Map<String, String>> customers = JsonDataReader.getValidCustomers();
        Object[][] data = new Object[customers.size()][1];

        for (int i = 0; i < customers.size(); i++) {
            data[i][0] = customers.get(i);
        }
        return data;
    }

    @DataProvider(name = "invalidCustomers")
    public Object[][] invalidCustomers() {
        List<Map<String, String>> customers = JsonDataReader.getInvalidCustomers();
        Object[][] data = new Object[customers.size()][1];

        for (int i = 0; i < customers.size(); i++) {
            data[i][0] = customers.get(i);
        }
        return data;
    }


    // TESTS

    @Test(
            priority = 1,
            groups = {"smoke", "checkout"}
    )
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify user can complete checkout successfully")
    public void SuccessfulCheckoutTest() {

        Map<String, String> customer = JsonDataReader.getValidCustomer(0);

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .goToCart()
                .proceedToCheckout()
                .completeOrder(
                        customer.get("firstName"),
                        customer.get("lastName"),
                        customer.get("postalCode")
                );
    }


    @Test(
            priority = 2,
            dataProvider = "validCustomers",
            groups = {"regression", "checkout"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify checkout works with various customers")
    public void CheckoutWithMultipleCustomersTest(Map<String, String> customer) {

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .goToCart()
                .proceedToCheckout()
                .completeOrder(
                        customer.get("firstName"),
                        customer.get("lastName"),
                        customer.get("postalCode")
                );
    }


    @Test(
            priority = 3,
            groups = {"regression", "checkout", "validation", "Negative"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify checkout validates required fields")
    public void CheckoutShowsErrorWhenEmpty() {

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BIKE_LIGHT_ID)
                .goToCart()
                .proceedToCheckout()
                .clickContinue()
                .assertErrorMessage("Error: First Name is required");
    }


    @Test(
            priority = 4,
            dataProvider = "invalidCustomers",
            groups = {"regression", "checkout", "validation", "Negative"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify checkout shows error for each missing field")
    public void CheckoutValidationTest(Map<String, String> customer) {

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .goToCart()
                .proceedToCheckout()
                .enterFirstName(customer.get("firstName"))
                .enterLastName(customer.get("lastName"))
                .enterPostalCode(customer.get("postalCode"))
                .clickContinue()
                .assertErrorMessage(customer.get("expectedError"));
    }


    @Test(
            priority = 5,
            groups = {"regression", "checkout", "navigation"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify user can return home after checkout")
    public void ContinueToHomeAfterComplete() {

        Map<String, String> customer = JsonDataReader.getValidCustomer(1);

        ProductsPage products = loginAsUser();

        products.addToCartById(products.ONESIE_ID)
                .goToCart()
                .proceedToCheckout()
                .completeOrder(
                        customer.get("firstName"),
                        customer.get("lastName"),
                        customer.get("postalCode")
                )
                .backHome()
                .assertProductsTitle("Products");
    }


    @Test(
            priority = 6,
            groups = {"smoke", "checkout", "pricing"}
    )
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify checkout calculates prices accurately")
    public void VerifyCheckoutPricesTest() {

        Map<String, String> customer = JsonDataReader.getValidCustomer(2);

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .addToCartById(products.BOLT_TSHIRT_ID)
                .goToCart()
                .proceedToCheckout()
                .fillInformation(
                        customer.get("firstName"),
                        customer.get("lastName"),
                        customer.get("postalCode")
                )
                .assertAllPriceValues()
                .clickFinish()
                .assertAtCompletePage();
    }


    @Test(
            priority = 7,
            groups = {"regression", "checkout"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify checkout follows correct page flow")
    public void VerifyCheckoutPageFlow() {

        Map<String, String> customer = JsonDataReader.getValidCustomer(0);

        ProductsPage products = loginAsUser();

        products.addToCartById(products.FLEECE_JACKET_ID)
                .goToCart()
                .proceedToCheckout()
                .assertAtInformationPage()
                .fillInformation(
                        customer.get("firstName"),
                        customer.get("lastName"),
                        customer.get("postalCode")
                )
                .assertAtOverviewPage()
                .clickFinish()
                .assertAtCompletePage();
    }
}
