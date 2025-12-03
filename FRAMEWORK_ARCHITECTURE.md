# 🏗 Framework Architecture

## Layer Structure

```
┌─────────────────────────────────┐
│      TEST LAYER                 │  ← Write test scenarios
│  LoginTest, ProductsTest...     │
└────────────┬────────────────────┘
             ↓
┌─────────────────────────────────┐
│      PAGE LAYER (POM)           │  ← Page Objects (locators + actions)
│  LoginPage, ProductsPage...     │  ← logging
└────────────┬────────────────────┘
             ↓
┌─────────────────────────────────┐
│      BOT LAYER                  │  ← Smart WebDriver wrapper
│  ActionsBot, Waitsbot           │  ← Auto-wait, scroll, retry
└────────────┬────────────────────┘
             ↓
┌─────────────────────────────────┐
│      DRIVER LAYER               │  ← WebDriver creation
│  WebDriverFactory               │  ← ThreadLocal for parallel tests
└────────────┬────────────────────┘
             ↓
┌─────────────────────────────────┐
│      UTILITY LAYER              │  ← Config, data
│  EnvFactory, JsonDataReader     │
└─────────────────────────────────┘
```

---

## Design Patterns

### 1. Page Object Model (POM)
**Problem:** Tests directly use WebDriver → hard to maintain  
**Solution:** One class per page

```java
public class LoginPage {
    private final By username = By.id("user-name");
    
    public LoginPage login(String user, String pass) {
        actionsbot.type(username, user);
        return this;
    }
}
```

---

### 2. Fluent Interface
**Problem:** Nested code is hard to read  
**Solution:** Method chaining

```java
// ✅ Readable flow
loginPage
    .login("user", "pass")
    .addToCart("Backpack")
    .goToCart();
```

---

### 3. Factory Pattern
**Problem:** Need to support multiple browsers  
**Solution:** Factory creates drivers

```java
WebDriver driver = WebDriverFactory.initdriver();
// Reads browser from config → creates Chrome/Edge
```

---

### 4. Bot Pattern (Smart Wrapper)
**Problem:** WebDriver needs manual waits everywhere  
**Solution:** Bot handles waits automatically

```java
actionsBot.click(locator);
// Auto: wait → scroll → click → JS fallback if needed
```

---

## TestNG Features

**Groups:**
```java
@Test(groups = {"smoke", "cart"})
```

**Priorities:**
```java
@Test(priority = 1)  // Runs first
```

**Data Providers:**
```java
@DataProvider(name = "users")
public Object[][] users() { return data; }

@Test(dataProvider = "users")
public void test(Map<String, String> user) { }
```

**Dependencies:**
```java
@Test(dependsOnMethods = {"loginTest"})
```

---

## Allure Reporting

**Steps:**
```java
@Step("Login with: {username}")
public void login(String username) { }
```

**Organization:**
```java
@Epic("E-Commerce")           // Business domain
@Story("User adds items")     // User story
@Severity(SeverityLevel.CRITICAL)
```

---

## Thread Safety

**ThreadLocal WebDriver** = Each thread gets its own driver

```java
private static ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

public static WebDriver getDriver() {
    return driverThreadLocal.get();  // Thread-safe
}
```

**Usage:**
```xml
<suite parallel="classes" thread-count="3">
```

---

## Error Handling

**Retry Logic:**
```java
waitsbot.fluentwait().until(d -> {
    try {
        element.click();
        return true;
    } catch (Exception e) {
        return false;  // Retry automatically
    }
});
```

**JS Fallback:**
```java
try {
    element.click();
} catch (WebDriverException e) {
    jsExecutor.executeScript("arguments[0].click();", element);
}
```

---

## Extensibility

### Add New Browser

**1. Create Factory:**
```java
public class EdgeFactory extends Abstractdriver {
    public WebDriver createDriver() {
        return new EdgeDriver(options);
    }
}
```

**2. Update Factory Method:**
```java
case "edge" -> new EdgeFactory(headless);
```

**3. Update Config:**
```json
{"browser": "edge"}
```

---

### Add New Page

**1. Create Page Class:**
```java
public class NewPage extends BasePage<NewPage> {
    private final By locator = By.id("element");
    
    public NewPage doAction() {
        actionsbot.click(locator);
        return this;
    }
}
```

**2. Link from Existing Page:**
```java
public NewPage goToNewPage() {
    actionsbot.click(link);
    return new NewPage(driver);
}
```

**3. Write Tests:**
```java
public class NewPageTest extends TestBase {
    @Test
    public void testNewPage() {
        loginAsUser().goToNewPage().doAction();
    }
}
```

---

## Key Principles

| Principle | Implementation |
|-----------|----------------|
| **DRY** | Reusable components (CartComponent) |
| **Single Responsibility** | Each class has one purpose |
| **Open/Closed** | Easy to extend (add browsers) |
| **Maintainability** | Change UI → update 1 page class |
| **Scalability** | Add pages/tests without breaking existing code |

---

## Summary

✅ **POM** - Maintainable page structure  
✅ **Fluent** - Readable test flow  
✅ **Factory** - Flexible driver creation  
✅ **Bots** - Robust interactions  
✅ **ThreadLocal** - Parallel execution  
✅ **Data-Driven** - JSON test data  
✅ **Allure** - Rich reporting