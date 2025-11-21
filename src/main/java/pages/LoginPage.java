package pages;

import bots.ActionsBot;
import io.qameta.allure.Step;
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
    }

    //ACTIONS

    @Step("Enter credentials and click login - Username: {username}")
    public LoginPage login(String username, String password) {
        actionsbot.type(usernameField, username == null ? "" : username);
        actionsbot.type(passwordField, password == null ? "" : password);
        actionsbot.click(loginButton);
        return this;
    }

    @Step("Verify successful login - Expected URL: {expectedUrl}")
    public ProductsPage isloggedin(String expectedUrl) {
        Assert.assertEquals(driver.getCurrentUrl(), expectedUrl,
                "Login did not redirect to inventory page");
        return new ProductsPage(driver);
    }

    //GETTERS

    @Step("Get error message from login page")
    public String getErrorMessage() {
        return actionsbot.getText(errorMsg);
    }

    //ASSERTIONS

    @Step("Verify error message: {expectedMessage}")
    public LoginPage assertInvalidLoginMessage(String expectedMessage) {
        String actual = getErrorMessage();
        Assert.assertEquals(actual, expectedMessage,
                "Error message mismatch for invalid login");
        return this;
    }
}