package tests;

import drivers.WebDriverFactory;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.*;
import pages.LoginPage;
import pages.ProductsPage;
import utils.EnvFactory;

public class TestBase {

    protected WebDriver driver;

    @BeforeMethod(alwaysRun = true)
    @Step("Setup: Initialize browser and navigate to application")
    public void setup() {
        driver = WebDriverFactory.initdriver();
        driver.get(EnvFactory.getBaseUrl());
    }

    @AfterMethod(alwaysRun = true)
    @Step("Teardown: Close browser and cleanup")
    public void tearDown() {
        WebDriverFactory.quitdriver();
    }

    @Step("Login as standard user")
    protected ProductsPage loginAsUser() {
        return new LoginPage(driver)
                .login(EnvFactory.getUsername(), EnvFactory.getPassword())
                .isloggedin(LoginPage.INVENTORY_URL);
    }

    @Step("Login with custom credentials: {username}")
    protected LoginPage loginWith(String username, String password) {
        return new LoginPage(driver)
                .login(username, password);
    }
}
