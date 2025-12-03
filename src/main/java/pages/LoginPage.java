package pages;

import bots.ActionsBot;
import io.qameta.allure.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class LoginPage extends BasePage<LoginPage> {


    //LOCATORS
    private final By usernameField = By.id("user-name");
    private final By passwordField = By.id("password");
    private final By loginButton = By.id("login-button");
    private final By errorMsg = By.cssSelector("h3[data-test='error']");

    //CONSTANTS
    public static final String INVENTORY_URL = "https://www.saucedemo.com/inventory.html";

    //CONSTRUCTOR
    public LoginPage(WebDriver driver) {
        super(driver);
        log.info("Navigated to Login Page");
    }

    //ACTIONS

    @Step("Enter credentials and click login - Username: {username}")
    public LoginPage login(String username, String password) {
        log.info("Entering username: {}", username);
        actionsbot.type(usernameField, username == null ? "" : username);

        log.info("Entering password");
        actionsbot.type(passwordField, password == null ? "" : password);

        log.info("Clicking login button");
        actionsbot.click(loginButton);

        return this;
    }

    @Step("Verify successful login - Expected URL: {expectedUrl}")
    public ProductsPage isloggedin(String expectedUrl) {
        log.info("Verifying login success - Expected URL: {}", expectedUrl);
        String actualUrl = driver.getCurrentUrl();

        Assert.assertEquals(actualUrl, expectedUrl,
                "Login did not redirect to inventory page");

        log.info("Login successful - Redirected to: {}", actualUrl);
        return new ProductsPage(driver);
    }

    //GETTERS

    @Step("Get error message from login page")
    public String getErrorMessage() {
        String errorMessage = actionsbot.getText(errorMsg);
        log.info("Retrieved error message: {}", errorMessage);
        return errorMessage;
    }

    //ASSERTIONS

    @Step("Verify error message: {expectedMessage}")
    public LoginPage assertInvalidLoginMessage(String expectedMessage) {
        String actual = getErrorMessage();
        log.info("Validating error message - Expected: {} | Actual: {}", expectedMessage, actual);

        Assert.assertEquals(actual, expectedMessage,
                "Error message mismatch for invalid login");

        log.info("Error message validated successfully");
        return this;
    }
}