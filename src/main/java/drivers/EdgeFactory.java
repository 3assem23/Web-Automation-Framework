package drivers;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeFactory extends Abstractdriver {

    private final boolean headless;

    public EdgeFactory(boolean headless) {
        this.headless = headless;
    }

    private EdgeOptions getOptions() {
        EdgeOptions options = new EdgeOptions();

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
        return new EdgeDriver(getOptions());
    }
}
