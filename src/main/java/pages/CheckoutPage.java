package pages;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

public class CheckoutPage extends BasePage<CheckoutPage> {

    //LOCATORS
    private final By title = By.className("title");

    private final By firstNameField = By.id("first-name");
    private final By lastNameField = By.id("last-name");
    private final By postalCodeField = By.id("postal-code");

    private final By continueButton = By.id("continue");
    private final By finishButton = By.id("finish");
    private final By backHomeButton = By.id("back-to-products");

    private final By errorMessage = By.cssSelector("h3[data-test='error']");

    private final By itemTotalLabel = By.cssSelector(".summary_subtotal_label");
    private final By taxLabel = By.cssSelector(".summary_tax_label");
    private final By totalLabel = By.cssSelector(".summary_total_label");
    private final By itemPrices = By.cssSelector(".inventory_item_price");

    //CONSTRUCTOR
    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    //ASSERTIONS - PAGE VERIFICATION

    @Step("Verify we are on Checkout Information page")
    public CheckoutPage assertAtInformationPage() {
        log.info("Validating checkout information page...");
        assertTitle("Checkout: Your Information");
        return this;
    }

    @Step("Verify we are on Checkout Overview page")
    public CheckoutPage assertAtOverviewPage() {
        log.info("Validating checkout overview page...");
        assertTitle("Checkout: Overview");
        return this;
    }

    @Step("Verify we are on Checkout Complete page")
    public CheckoutPage assertAtCompletePage() {
        log.info("Validating checkout complete page...");
        assertTitle("Checkout: Complete!");
        return this;
    }

    @Step("Verify error message: {expectedText}")
    public CheckoutPage assertErrorMessage(String expectedText) {
        log.info("Checking error message: " + expectedText);
        Assert.assertEquals(actionsbot.getText(errorMessage).trim(), expectedText);
        return this;
    }

    private void assertTitle(String expected) {
        String actual = actionsbot.getText(title).trim();
        log.info("Expected title: " + expected + " | Actual: " + actual);
        Assert.assertEquals(actual, expected, "Wrong Checkout page");
    }

    //ASSERTIONS - PRICE VALIDATION

    @Step("Verify item subtotal is correct")
    public CheckoutPage assertItemTotalCorrect() {
        log.info("Validating item subtotal...");
        double expected = getSumOfItemPrices();
        double displayed = getDisplayedItemTotal();
        log.info("Expected subtotal: " + expected + " | Displayed: " + displayed);
        Assert.assertEquals(displayed, expected, "Item total mismatch");
        return this;
    }

    @Step("Verify tax calculation is correct")
    public CheckoutPage assertTaxCorrect() {
        log.info("Validating tax value...");
        double expectedTax = getDisplayedItemTotal() * 0.08;
        double displayedTax = getDisplayedTax();
        log.info("Expected tax: " + expectedTax + " | Displayed: " + displayedTax);
        Assert.assertEquals(displayedTax, expectedTax, 0.01, "Tax mismatch");
        return this;
    }

    @Step("Verify final total is correct")
    public CheckoutPage assertTotalCorrect() {
        log.info("Validating final total...");
        double expectedTotal = getDisplayedItemTotal() + getDisplayedTax();
        double displayedTotal = getDisplayedTotal();
        log.info("Expected total: " + expectedTotal + " | Displayed: " + displayedTotal);
        Assert.assertEquals(displayedTotal, expectedTotal, 0.01, "Final Total mismatch");
        return this;
    }

    @Step("Verify all price calculations (Item Total + Tax + Total)")
    public CheckoutPage assertAllPriceValues() {
        log.info("Validating all price values...");
        return assertItemTotalCorrect()
                .assertTaxCorrect()
                .assertTotalCorrect();
    }

    //ACTIONS - FORM FILLING

    @Step("Enter first name: {value}")
    public CheckoutPage enterFirstName(String value) {
        log.info("Typing first name: " + value);
        actionsbot.type(firstNameField, value);
        return this;
    }

    @Step("Enter last name: {value}")
    public CheckoutPage enterLastName(String value) {
        log.info("Typing last name: " + value);
        actionsbot.type(lastNameField, value);
        return this;
    }

    @Step("Enter postal code: {value}")
    public CheckoutPage enterPostalCode(String value) {
        log.info("Typing postal code: " + value);
        actionsbot.type(postalCodeField, value);
        return this;
    }

    @Step("Click 'Continue' button")
    public CheckoutPage clickContinue() {
        log.info("Clicking Continue button");
        actionsbot.click(continueButton);
        return this;
    }

    @Step("Fill checkout information - {fn},{ln},{pc}")
    public CheckoutPage fillInformation(String fn, String ln, String pc) {
        log.info("Filling checkout form: " + fn + " " + ln + " (" + pc + ")");
        return enterFirstName(fn)
                .enterLastName(ln)
                .enterPostalCode(pc)
                .clickContinue();
    }

    @Step("Click 'Finish' button")
    public CheckoutPage clickFinish() {
        log.info("Clicking Finish button");
        actionsbot.click(finishButton);
        return this;
    }

    @Step("Complete full checkout process - Customer: {fn} {ln}")
    public CheckoutPage completeOrder(String fn, String ln, String pc) {
        log.info("Completing full checkout workflow");
        return fillInformation(fn, ln, pc)
                .assertAtOverviewPage()
                .clickFinish()
                .assertAtCompletePage();
    }

    //NAVIGATION

    @Step("Click 'Back Home' button")
    public ProductsPage backHome() {
        log.info("Navigating back to products page");
        actionsbot.click(backHomeButton);
        return new ProductsPage(driver);
    }

    //PRICE HELPERS

    private double extractNumber(String text) {
        return Double.parseDouble(text.replaceAll("[^0-9.]", ""));
    }

    private double getDisplayedItemTotal() {
        return extractNumber(actionsbot.getText(itemTotalLabel));
    }

    private double getDisplayedTax() {
        return extractNumber(actionsbot.getText(taxLabel));
    }

    private double getDisplayedTotal() {
        return extractNumber(actionsbot.getText(totalLabel));
    }

    @Step("Calculate sum of all item prices")
    public double getSumOfItemPrices() {
        double sum = driver.findElements(itemPrices)
                .stream()
                .map(WebElement::getText)
                .map(this::extractNumber)
                .reduce(0.0, Double::sum);

        log.info("Calculated sum of item prices: " + sum);
        return sum;
    }
}
