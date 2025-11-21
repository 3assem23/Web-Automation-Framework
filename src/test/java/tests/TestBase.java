package tests;

import drivers.WebDriverFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.ProductsPage;
import utils.AllureLogger;
import utils.EnvFactory;


public class TestBase {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    @Step("Setup: Initialize browser and navigate to application")
    public void setup() {
        AllureLogger.logInfo("Initializing WebDriver");
        driver = WebDriverFactory.initdriver();

        AllureLogger.logInfo("Navigating to: " + EnvFactory.getBaseUrl());
        driver.get(EnvFactory.getBaseUrl());

        AllureLogger.logInfo("Browser setup completed successfully");
    }

    @AfterMethod(alwaysRun = true)
    @Step("Teardown: Close browser and cleanup")
    public void tearDown() {
        AllureLogger.logInfo("Closing browser and cleaning up resources");
        WebDriverFactory.quitdriver();
    }

    @Step("Login as standard user")
    protected ProductsPage loginAsUser() {
        AllureLogger.logStep("Logging in with username: " + EnvFactory.getUsername());

        return new LoginPage(driver)
                .login(EnvFactory.getUsername(), EnvFactory.getPassword())
                .isloggedin(LoginPage.INVENTORY_URL);
    }

    @Step("Login with custom credentials: {username}")
    protected LoginPage loginWith(String username, String password) {
        AllureLogger.logStep("Attempting login with username: " + username);

        return new LoginPage(driver)
                .login(username, password);
    }
}