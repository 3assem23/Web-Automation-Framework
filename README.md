# 🎓 Swag Labs Automation Framework

Enterprise-grade Selenium + TestNG + Allure framework

---

## 🎯 Overview

**Production-ready automation framework** demonstrating:

- ✅ Page Object Model (POM) with Fluent Interface
- ✅ Factory Pattern for WebDriver management
- ✅ Data-Driven Testing with JSON
- ✅ TestNG (listeners, groups, priorities, suites)
- ✅ Allure Reports with detailed steps
- ✅ Clean, maintainable, scalable code

**App Under Test:** [Sauce Demo](https://www.saucedemo.com/)

---

## 🛠 Tech Stack

| Technology | Version | Purpose |
|------------|---------|---------|
| Java | 21 | Language |
| Selenium | 4.38.0 | Automation |
| TestNG | 7.11.0 | Framework |
| Allure | 2.31.0 | Reporting |
| Maven | 3.9+ | Build Tool |

---

## 🏗 Project Structure

```
swag/
├── src/main/java/
│   ├── bots/              # ActionsBot, Waitsbot
│   ├── drivers/           # WebDriver Factory
│   ├── pages/             # Page Objects
│   └── utils/             # Config & Data readers
├── src/test/java/
│   ├── tests/             # Test classes
│   └── listeners/         # TestNG listeners
└── src/test/resources/
    ├── config/            # env.json
    ├── testdata/          # JSON test data
    └── testng-suites/     # TestNG XML suites
```

---

## 🚀 Quick Start

### Prerequisites

```bash
java -version    # JDK 21
mvn -version     # Maven 3.9+
```

### Setup

```bash
git clone <repo-url>
cd swag
mvn clean install -DskipTests
```

### Run Tests

```bash
# All tests
mvn clean test

# Smoke tests (~3 min)
mvn test -DsuiteXmlFile=src/test/resources/testng-suites/smoke-suite.xml

# By group
mvn test -Dgroups=smoke
mvn test -Dgroups=cart

# Single class
mvn test -Dtest=LoginTest
```

### Generate Report

```bash
mvn allure:serve
```

---

## 📊 Test Coverage

| Suite | Tests | Duration |
|-------|-------|---------|
| Master | 45+   | ~15 min |
| Smoke | 13    | ~3 min |
| Regression | 32    | ~5 min |

**Categories:**
- 🔐 Login (3 tests)
- 🛍 Products (15 tests)
- 📋 Product Details (7 tests)
- 🛒 Cart (5 tests)
- 💳 Checkout (7 tests)

---

## 🎨 Design Patterns

### Page Object Model
```java
public class LoginPage {
    private final By usernameField = By.id("user-name");
    
    public LoginPage login(String user, String pass) {
        // Actions
        return this;
    }
}
```

### Fluent Interface
```java
loginPage
    .login("user", "pass")
    .addToCart("Backpack")
    .goToCart();
```

### Factory Pattern
```java
WebDriver driver = WebDriverFactory.initdriver();
```

### Smart Waits
```java
actionsBot.click(locator);  // Auto-wait + scroll + fallback
```

---

## ⭐ Key Features

**TestNG:**
- Groups: `smoke`, `regression`, `cart`, `validation`
- Priorities: `priority = 1, 2, 3`
- Data Providers: JSON-driven
- Listeners: Test, Suite, Retry

**Allure:**
- @Step annotations
- Screenshots on failure
- Epic/Feature/Story organization
- Severity levels

**Robust Interactions:**
- Smart waits (no hardcoded sleeps)
- Auto-scroll to elements
- JavaScript fallback
- Retry logic

---

## 📝 Configuration

Edit `src/test/resources/config/env.json`:

```json
{
  "browser": "chrome",
  "headless": false,
  "baseUrl": "https://www.saucedemo.com/",
  "username": "standard_user",
  "password": "secret_sauce"
}
```

---

## 🤝 Contact

**Mohamed Assem**  
📧 3assem2001@gmail.com  
🔗 [LinkedIn](https://www.linkedin.com/in/mohamedassem01)  
💻 [GitHub](https://github.com/3assem23)

---

## 🎓 Learning Outcomes

This project demonstrates:

1. ✅ Full TestNG structure
2. ✅ Clean code with POM
3. ✅ Multiple design patterns
4. ✅ Data-driven testing
5. ✅ Professional reporting
6. ✅ Industry best practices
7. ✅ Comprehensive documentation

---

**ITI Graduation Project - 2025**  
Software Testing Track