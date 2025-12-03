package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class FirefoxFactory extends Abstractdriver {

    private final boolean headless;

    public FirefoxFactory(boolean headless) {
        this.headless = headless;
    }

    private FirefoxOptions getOptions() {
        FirefoxOptions options = new FirefoxOptions();

        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");

        if (headless)
            options.addArguments("--headless");

        return options;
    }

    @Override
    public WebDriver createDriver() {
        return new FirefoxDriver(getOptions());
    }
}