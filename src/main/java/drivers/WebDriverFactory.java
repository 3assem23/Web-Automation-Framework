package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;
import utils.EnvFactory;

public class WebDriverFactory {

    private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private static Abstractdriver getDriverFactory(String browser, boolean headless) {
        return switch (browser.toLowerCase()) {
            case "chrome" -> new ChromeFactory(headless);
            case "edge" -> new EdgeFactory(headless);
            case "firefox" -> new FirefoxFactory(headless);
            default -> throw new IllegalArgumentException("Unsupported browser: " + browser);
        };
    }

    public static WebDriver initdriver() {
        String browser = EnvFactory.getBrowser();
        boolean headless = EnvFactory.isHeadless();
        WebDriver created = ThreadGuard.protect(
                getDriverFactory(browser, headless).createDriver()
        );
        driverThreadLocal.set(created);
        return driverThreadLocal.get();
    }

    public static WebDriver getDriver() {
        return driverThreadLocal.get();
    }

    public static void quitdriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}