package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeFactory extends Abstractdriver {

    private final boolean headless;

    public ChromeFactory(boolean headless) {
        this.headless = headless;
    }

    private ChromeOptions getOptions() {
        ChromeOptions options = new ChromeOptions();

        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--disable-popup-blocking");
        options.addArguments("--disable-infobars");
        options.addArguments("--start-maximized");

        if (headless)
            options.addArguments("--headless=new");

        return options;
    }

    @Override
    public WebDriver createDriver() {
        return new ChromeDriver(getOptions());
    }
}
