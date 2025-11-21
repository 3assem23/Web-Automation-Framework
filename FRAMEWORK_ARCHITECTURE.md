# 🏗 Framework Architecture Documentation

## Overview

This document provides technical details about the framework's architecture, design decisions, and implementation patterns.

---

## Architectural Layers

### 1. Test Layer (`src/test/java/tests/`)

**Responsibility:** Define test scenarios and assertions

**Key Classes:**
- `TestBase.java` - Base class with setup/teardown and common helpers
- `LoginTest.java` - Authentication test scenarios
- `ProductsTest.java` - Product listing and cart tests
- `CartTests.java` - Shopping cart functionality
- `CheckoutTests.java` - Checkout process tests
- `ProductDetailsPageTest.java` - Product details tests

**Pattern:** Test methods use fluent page objects for readability
```java
// Example: Fluent test flow
loginAsUser()
    .addToCartById(BACKPACK_ID)
    .goToCart()
    .proceedToCheckout()
    .completeOrder("John", "Doe", "12345");
```

---

### 2. Page Layer (`src/main/java/pages/`)

**Responsibility:** Encapsulate page elements and actions (Page Object Model)

**Key Principles:**
- Each page is a separate class
- Locators are private constants
- Methods return page objects (fluent interface)
- No assertions in page objects (separation of concerns)
- `@Step` annotations for Allure reporting

**Generic Base Page:**
```java
public abstract class BasePage<T extends BasePage<T>> {
    // Generic type enables fluent returns of concrete page types
}
```

**Example Implementation:**
```java
@Step("Add item to cart - Button ID: {addButtonId}")
public ProductsPage addToCartById(String addButtonId) {
    actionsbot.click(By.id(addButtonId));
    return this;  // Fluent interface
}
```

---

### 3. Component Layer (`src/main/java/pages/components/`)

**Responsibility:** Reusable page components used across multiple pages

**Example: CartComponent**
- Used by both `CartPage` and `ProductsPage`
- Encapsulates cart item operations
- Promotes code reusability (DRY principle)
```java
public class CartComponent {
    // Reusable cart operations
    public List<String> getItemNames() { ... }
    public void removeItemById(String id) { ... }
}
```

---

### 4. Bot Layer (`src/main/java/bots/`)

**Responsibility:** Intelligent WebDriver wrappers with built-in waits

**ActionsBot Features:**
- Smart waits before every action
- Automatic scrolling to elements
- JavaScript fallback for stubborn clicks
- Exception handling with retry logic

**Waitsbot Features:**
- Fluent wait configuration (15s timeout, 200ms polling)
- Ignores common exceptions (StaleElement, ElementNotInteractable)
- Visibility, presence, and disappearance waits
```java
// ActionsBot in action
public void click(By locator) {
    waitsbot.waitForPresence(locator);
    waitsbot.fluentwait().until(d -> {
        WebElement el = d.findElement(locator);
        scrollTo(el);
        try {
            el.click();
        } catch (WebDriverException e) {
            jsClick(el);  // Fallback
        }
        return true;
    });
}
```

---

### 5. Driver Layer (`src/main/java/drivers/`)

**Responsibility:** WebDriver initialization and management

**Factory Pattern Implementation:**
```
AbstractDriver (Abstract Factory)
    ↓
WebDriverFactory (Concrete Factory)
    ↓
├── ChromeFactory (Concrete Product)
└── EdgeFactory (Concrete Product)
```

**ThreadLocal for Parallel Execution:**
```java
private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

public static WebDriver initdriver() {
    WebDriver driver = ThreadGuard.protect(
        getDriverFactory(browser, headless).createDriver()
    );
    driverThreadLocal.set(driver);
    return driver;
}
```

**Benefits:**
- Thread-safe for parallel test execution
- Each thread has its own WebDriver instance
- No cross-thread contamination

---

### 6. Utility Layer (`src/main/java/utils/`)

**Responsibility:** Configuration, data management, and helpers

**Key Classes:**

**EnvFactory:**
- Singleton pattern
- Loads configuration from `env.json`
- Provides environment-specific settings

**JsonDataReader:**
- Centralized JSON parsing
- Loads test data from JSON files
- Supports multiple data files (login, checkout, products)

**AllureLogger:**
- Wrapper for Allure API
- Provides consistent logging methods
- Attaches data to reports

---

## Design Patterns Deep Dive

### 1. Page Object Model (POM)

**Problem:** Tests directly interacting with WebDriver become brittle and hard to maintain.

**Solution:** Encapsulate page structure and behavior in page classes.

**Implementation:**
```java
public class LoginPage {
    // Locators (encapsulated)
    private final By usernameField = By.id("user-name");
    
    // Actions (public interface)
    public LoginPage login(String user, String pass) {
        // Implementation
        return this;
    }
}
```

**Benefits:**
- Single source of truth for page structure
- Easy to update when UI changes
- Tests remain readable and maintainable

---

### 2. Fluent Interface Pattern

**Problem:** Multiple actions create deeply nested code.

**Solution:** Chain method calls by returning `this` or next page.

**Before (Traditional):**
```java
LoginPage loginPage = new LoginPage(driver);
loginPage.enterUsername("user");
loginPage.enterPassword("pass");
loginPage.clickLogin();
ProductsPage products = loginPage.getProductsPage();
products.addToCart("Backpack");
```

**After (Fluent):**
```java
new LoginPage(driver)
    .login("user", "pass")
    .addToCart("Backpack");
```

**Benefits:**
- Improved readability
- Less boilerplate code
- Natural language flow

---

### 3. Factory Pattern (WebDriver Creation)

**Problem:** Need to support multiple browsers with different configurations.

**Solution:** Factory pattern with strategy for browser creation.

**Structure:**
```java
// Abstract factory
public abstract class Abstractdriver {
    public abstract WebDriver createDriver();
}

// Concrete factories
public class ChromeFactory extends Abstractdriver {
    public WebDriver createDriver() {
        return new ChromeDriver(getOptions());
    }
}

// Factory method
public static WebDriver initdriver() {
    String browser = EnvFactory.getBrowser();
    return getDriverFactory(browser, headless).createDriver();
}
```

**Benefits:**
- Easy to add new browsers (Firefox, Safari)
- Centralized configuration
- Follows Open/Closed Principle

---

### 4. Component Pattern

**Problem:** Same UI component (cart) appears on multiple pages.

**Solution:** Extract component into reusable class.

**Usage:**
```java
// CartPage uses CartComponent
public class CartPage {
    private final CartComponent cart;
    
    public CartPage removeItem(String id) {
        cart.removeItemById(id);
        return this;
    }
}

// ProductsPage also uses CartComponent
public class ProductsPage {
    // Could use same component if needed
}
```

**Benefits:**
- DRY (Don't Repeat Yourself)
- Consistent behavior across pages
- Easy to test in isolation

---

### 5. Singleton Pattern (Configuration)

**Problem:** Need single configuration instance shared across framework.

**Solution:** Static initialization with singleton access.
```java
public class EnvFactory {
    private static EnvConfig config;
    
    static {
        // Load once
        config = new Gson().fromJson(reader, EnvConfig.class);
    }
    
    public static String getBrowser() {
        return config.browser;
    }
}
```

**Benefits:**
- Single source of truth
- Lazy initialization
- Memory efficient

---

## TestNG Integration

### Test Structure

**1. Groups:**
- `smoke` - Critical path tests
- `regression` - Full test coverage
- `cart` - Shopping cart tests
- `validation` - Form validation tests
- `negative` - Negative test scenarios

**2. Priorities:**
```java
@Test(priority = 1)  // Runs first
public void highPriorityTest() { }

@Test(priority = 2)  // Runs second
public void mediumPriorityTest() { }
```

**3. Dependencies:**
```java
@Test
public void addItemTest() { }

@Test(dependsOnMethods = {"addItemTest"})
public void removeItemTest() { }
```

**4. Data Providers:**
```java
@DataProvider(name = "loginData")
public Object[][] loginData() {
    return new Object[][] {
        {"user1", "pass1"},
        {"user2", "pass2"}
    };
}

@Test(dataProvider = "loginData")
public void loginTest(String user, String pass) { }
```

---

### Listeners

**TestListener (ITestListener):**
- Captures screenshots on failure
- Logs test execution
- Attaches data to Allure

**SuiteListener (ISuiteListener):**
- Tracks suite execution time
- Prints execution summary
- Provides metadata

**RetryAnalyzer:**
- Retries flaky tests automatically
- Configurable retry count (default: 2)
- Improves test stability

---

## Allure Reporting Integration

### @Step Annotations

**Purpose:** Create detailed step-by-step report
```java
@Step("Login with username: {username}")
public LoginPage login(String username, String password) {
    // Implementation
}
```

**Allure Report shows:**
```
✓ Test: validLoginTest
  ↳ Login with username: standard_user
    ↳ Enter credentials and click login
    ↳ Verify successful login
```

### Severity Levels
```java
@Severity(SeverityLevel.BLOCKER)   // Must pass
@Severity(SeverityLevel.CRITICAL)  // Very important
@Severity(SeverityLevel.NORMAL)    // Standard priority
@Severity(SeverityLevel.MINOR)     // Low priority
@Severity(SeverityLevel.TRIVIAL)   // Cosmetic
```

### Test Organization
```java
@Epic("E-Commerce")              // High-level feature
@Feature("Shopping Cart")        // Specific feature
@Story("User can add products") // User story
@Description("Test verifies...")// Detailed description
```

---

## Data-Driven Testing

### JSON Structure

**loginData.json:**
```json
{
  "valid": { "username": "user", "password": "pass" },
  "invalid": [
    { "username": "", "password": "pass", "expectedError": "..." }
  ]
}
```

**JsonDataReader:**
```java
// Read single object
Map<String, String> valid = JsonDataReader.getValidLogin();

// Read array of objects
List<Map<String, String>> invalid = JsonDataReader.getInvalidLogins();
```

**TestNG Integration:**
```java
@DataProvider(name = "invalidLogins")
public Object[][] invalidLogins() {
    List<Map<String, String>> data = JsonDataReader.getInvalidLogins();
    // Convert to Object[][]
    return convertToArray(data);
}

@Test(dataProvider = "invalidLogins")
public void invalidLoginTest(Map<String, String> testData) {
    // Test implementation
}
```

---

## Error Handling & Resilience

### Robust Element Interactions

**1. Automatic Waits:**
```java
// Every action waits for element
actionsbot.click(locator);  // Waits → Scrolls → Clicks
```

**2. Retry Logic:**
```java
waitsbot.fluentwait().until(d -> {
    try {
        // Attempt action
        return true;
    } catch (Exception ignored) {
        return false;  // Retry
    }
});
```

**3. JavaScript Fallback:**
```java
try {
    element.click();
} catch (WebDriverException e) {
    jsClick(element);  // JavaScript click
}
```

### Test Retries

**Flaky test handling:**
```java
@Test(retryAnalyzer = RetryAnalyzer.class)
public void potentiallyFlakyTest() { }
```

**Retry count:** 2 attempts (configurable)

---

## Thread Safety

### ThreadLocal WebDriver

**Problem:** Parallel execution causes driver conflicts.

**Solution:** Each thread gets own WebDriver instance.
```java
private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

public static WebDriver getDriver() {
    return driverThreadLocal.get();  // Thread-safe
}
```

**Usage in Parallel:**
```xml
<suite parallel="classes" thread-count="3">
    <!-- Each test class runs in separate thread -->
</suite>
```

---

## Performance Optimization

### Smart Waits

**Avoid:**
```java
Thread.sleep(5000);  // ❌ Fixed wait
```

**Use:**
```java
waitsbot.waitForVisibility(locator);  // ✓ Dynamic wait
```

### Element Caching

**Avoid:**
```java
// Re-finding elements repeatedly
driver.findElement(locator).click();
driver.findElement(locator).getText();
```

**Better:**
```java
// Bot handles caching internally
actionsbot.click(locator);
String text = actionsbot.getText(locator);
```

---

## Extensibility

### Adding New Browser

**1. Create Factory:**
```java
public class FirefoxFactory extends Abstractdriver {
    public WebDriver createDriver() {
        return new FirefoxDriver(getOptions());
    }
}
```

**2. Update Factory Method:**
```java
private static Abstractdriver getDriverFactory(String browser, boolean headless) {
    return switch (browser.toLowerCase()) {
        case "chrome" -> new ChromeFactory(headless);
        case "edge" -> new EdgeFactory(headless);
        case "firefox" -> new FirefoxFactory(headless);  // Added
        default -> throw new IllegalArgumentException("Unsupported browser");
    };
}
```

**3. Update Configuration:**
```json
{
  "browser": "firefox"
}
```

### Adding New Page

**1. Create Page Class:**
```java
public class NewPage extends BasePage<NewPage> {
    // Locators
    // Actions
    // Assertions
}
```

**2. Update Navigation:**
```java
public NewPage goToNewPage() {
    actionsbot.click(newPageLink);
    return new NewPage(driver);
}
```

**3. Create Tests:**
```java
public class NewPageTest extends TestBase {
    @Test
    public void testNewPage() {
        loginAsUser()
            .goToNewPage()
            .performAction();
    }
}
```

---

## Summary

This framework demonstrates enterprise-grade automation practices:

- ✅ **Maintainable**: POM separates concerns
- ✅ **Scalable**: Factory pattern for extensibility
- ✅ **Reliable**: Smart waits and retry logic
- ✅ **Readable**: Fluent interface for clear tests
- ✅ **Reportable**: Allure integration with detailed steps
- ✅ **Thread-safe**: Parallel execution support
- ✅ **Data-driven**: JSON-based test data
- ✅ **Professional**: Industry-standard patterns and practices