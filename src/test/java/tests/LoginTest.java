package tests;

import io.qameta.allure.*;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pages.LoginPage;
import utils.JsonDataReader;

import java.util.List;
import java.util.Map;

@Epic("Authentication")
@Feature("User Login")

public class LoginTest extends TestBase {

    @DataProvider(name = "invalidCredentials")
    public Object[][] invalidCredentials() {
        List<Map<String, String>> list = JsonDataReader.getInvalidLogins();
        Object[][] out = new Object[list.size()][1];
        for (int i = 0; i < list.size(); i++) {
            out[i][0] = list.get(i);
        }
        return out;
    }

    @Test(priority = 0, groups = {"smoke", "sanity"})
    @Severity(SeverityLevel.NORMAL)
    @Description("Verify that login page loads successfully and all elements are visible")
    public void verifyLoginPageLoads() {
        new LoginPage(driver);
    }

    @Test(priority = 1, groups = {"smoke", "login"})
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that user can successfully login with valid username and password")
    public void validLoginTest() {
        Map<String, String> creds = JsonDataReader.getValidLogin();

        new LoginPage(driver)
                .login(creds.get("username"), creds.get("password"))
                .isloggedin(LoginPage.INVENTORY_URL);
    }

    @Test(dataProvider = "invalidCredentials", priority = 2, groups = {"regression", "login", "negative"})
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that system shows appropriate error messages for invalid login attempts")
    public void invalidLoginTest(Map<String, String> testData) {
        new LoginPage(driver)
                .login(testData.get("username"), testData.get("password"))
                .assertInvalidLoginMessage(testData.get("expectedError"));
    }
}