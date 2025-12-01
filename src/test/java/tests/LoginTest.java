package tests;

import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.AllureLogger;
import utils.JsonDataReader;

import java.util.List;
import java.util.Map;


@Epic("Authentication")
@Feature("Login Functionality")
public class LoginTest extends TestBase {

    //DATA PROVIDER


    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        List<Map<String, String>> list = JsonDataReader.getInvalidLogins();
        Object[][] out = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            out[i][0] = list.get(i);
        }
        return out;
    }

    //TESTS

    @Test(
            priority = 0,
            groups = {"smoke", "sanity"}
    )
    @Severity(SeverityLevel.NORMAL)
    @Story("Login page accessibility")
    public void verifyLoginPageLoads() {
        AllureLogger.logStep("Verifying login page accessibility");

        new LoginPage(driver);

        AllureLogger.logInfo("Current URL: " + driver.getCurrentUrl());
        AllureLogger.logInfo("Page Title: " + driver.getTitle());
        AllureLogger.logStep("Login page verified successfully");
    }


    @Test(
            priority = 1,
            groups = {"smoke", "login"}
    )
    @Severity(SeverityLevel.BLOCKER)
    @Story("User can login with valid credentials")
    public void validLoginTest() {
        AllureLogger.logStep("Starting valid login test");

        Map<String, String> creds = JsonDataReader.getValidLogin();
        String username = creds.get("username");
        String password = creds.get("password");

        AllureLogger.logInfo("Username: " + username);

        new LoginPage(driver)
                .login(username, password)
                .isloggedin(LoginPage.INVENTORY_URL);

        AllureLogger.logStep("Valid login test completed successfully");
    }


    @Test(
            dataProvider = "invalidCredentials",
            priority = 2,
            groups = {"regression","login" ,"negative"}
    )
    @Severity(SeverityLevel.CRITICAL)
    @Story("User cannot login with invalid credentials")
    public void invalidLoginTest(Map<String, String> testData) {
        AllureLogger.logStep("Starting invalid login test");

        String username = testData.get("username");
        String password = testData.get("password");
        String expectedError = testData.get("expectedError");

        AllureLogger.logInfo("Username: " + (username.isEmpty() ? "[EMPTY]" : username));
        AllureLogger.logInfo("Password: " + (password.isEmpty() ? "[EMPTY]" : password));
        AllureLogger.logInfo("Expected Error: " + expectedError);

        new LoginPage(driver)
                .login(username, password)
                .assertInvalidLoginMessage(expectedError);

        AllureLogger.logStep("Invalid login test completed");
    }
}