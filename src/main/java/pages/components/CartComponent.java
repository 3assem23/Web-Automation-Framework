package pages.components;

import bots.ActionsBot;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.stream.Collectors;


public class CartComponent {

    private final WebDriver driver;
    private final ActionsBot actions;

    //LOCATORS
    private final By cartItems = By.cssSelector(".cart_item");

    private final By itemName = By.className("inventory_item_name");
    private final By itemPrice = By.className("inventory_item_price");
    private final By removeButton = By.cssSelector("button[id^='remove']");

    public CartComponent(WebDriver driver) {
        this.driver = driver;
        this.actions = new ActionsBot(driver);
    }

    //HELPERS

    private List<WebElement> getAllItems() {
        return driver.findElements(cartItems);
    }

    public int getItemCount() {
        return getAllItems().size();
    }

    public List<String> getItemNames() {
        return getAllItems().stream()
                .map(i -> i.findElement(itemName).getText().trim())
                .collect(Collectors.toList());
    }

    public List<String> getItemPrices() {
        return getAllItems().stream()
                .map(i -> i.findElement(itemPrice).getText().trim())
                .collect(Collectors.toList());
    }

    //ACTIONS

    public CartComponent removeItemByName(String product) {
        for (WebElement item : getAllItems()) {
            String name = item.findElement(itemName).getText().trim();
            if (name.equals(product)) {
                item.findElement(removeButton).click();
                break;
            }
        }
        return this;
    }



    public CartComponent removeItemById(String id) {
        actions.click(By.id(id));
        return this;
    }


    public CartComponent removeAllItems() {
        for (WebElement item : getAllItems()) {
            item.findElement(removeButton).click();
        }
        return this;
    }

}
