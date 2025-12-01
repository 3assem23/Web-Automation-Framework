package tests;

import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.ProductsPage;
import utils.AllureLogger;
import utils.EnvFactory;
import utils.JsonDataReader;

import java.util.List;
import java.util.Map;


@Epic("E-Commerce")
@Feature("Checkout Process")
public class CheckoutTests extends TestBase {

    //DATA PROVIDER

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
            groups = {"smoke","checkout"}
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("User can complete checkout successfully")
    public void SuccessfulCheckoutTest() {
        AllureLogger.logStep("Starting successful checkout test");

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

        AllureLogger.logStep("Checkout completed successfully");
    }


    @Test(
            priority = 2,
            dataProvider = "validCustomers",
            groups = {"regression", "checkout"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Checkout works with various customers")
    public void CheckoutWithMultipleCustomersTest(Map<String, String> customer) {

        AllureLogger.logInfo("Testing with: " + customer.get("firstName") + " " + customer.get("lastName"));

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .goToCart()
                .proceedToCheckout()
                .completeOrder(
                        customer.get("firstName"),
                        customer.get("lastName"),
                        customer.get("postalCode")
                );

        AllureLogger.logStep("Customer checkout completed successfully");
    }



    @Test(
            priority = 3,
            groups = {"regression", "checkout", "validation", "Negative"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("Checkout validates required fields")
    public void CheckoutShowsErrorWhenEmpty() {
        AllureLogger.logStep("Testing checkout form validation");

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BIKE_LIGHT_ID)
                .goToCart()
                .proceedToCheckout()
                .clickContinue()
                .assertErrorMessage("Error: First Name is required");

        AllureLogger.logStep("Validation error displayed correctly");
    }


    @Test(
            priority = 4,
            dataProvider = "invalidCustomers",
            groups = {"regression", "checkout", "validation","Negative"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Checkout shows error for each missing field")
    public void CheckoutValidationTest(Map<String, String> customer) {
        AllureLogger.logStep("Testing validation: " + customer.get("expectedError"));

        ProductsPage products = loginAsUser();

        products.addToCartById(products.BACKPACK_ID)
                .goToCart()
                .proceedToCheckout()
                .enterFirstName(customer.get("firstName"))
                .enterLastName(customer.get("lastName"))
                .enterPostalCode(customer.get("postalCode"))
                .clickContinue()
                .assertErrorMessage(customer.get("expectedError"));

        AllureLogger.logStep("Validation test passed");
    }


    @Test(
            priority = 5,
            groups = {"regression", "checkout", "navigation"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("User can return home after checkout")
    public void ContinueToHomeAfterComplete() {
        AllureLogger.logStep("Testing post checkout navigation");

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

        AllureLogger.logStep("Successfully returned to products page");
    }


    @Test(
            priority = 6,
            groups = {"smoke", "checkout", "pricing"}
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("Checkout calculates prices accurately")
    public void VerifyCheckoutPricesTest() {
        AllureLogger.logStep("Testing checkout price calculations");

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

        AllureLogger.logStep("Price calculations verified successfully");
    }


    @Test(
            priority = 7,
            groups = {"regression", "checkout"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Checkout follows correct page flow")
    public void VerifyCheckoutPageFlow() {
        AllureLogger.logStep("Testing checkout page progression");

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

        AllureLogger.logStep("Checkout page flow verified successfully");
    }
}