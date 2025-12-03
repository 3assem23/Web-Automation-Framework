# 🚀 Test Execution Guide

## Quick Start

```bash
# Run all tests
mvn clean test

# Generate report
mvn allure:serve
```
┌─────────────────────────────────────────────────────────────┐
│                 SMOKE TESTS (13 tests)                      │
│                 Critical Path - 3 min                       │
├─────────────────────────────────────────────────────────────┤
│  LoginTest                                                  │
│     └── validLoginTest                                      │
│  ProductsTest                                               │
│     ├── verifyProductsTitleTest                             │
│     ├── Add2ItemsTest                                       │
│     ├── AddRemoveTest                                       │
│     └── GotoCartTest                                        │
│  ProductDetailsPageTest                                     │
│     ├── verifyProductTitleAndPriceTest                      │
│     ├── addAndRemoveProductTest                             │
│     └── addProductAndGoToCartTest                           │
│  CartTests                                                  │
│     ├── AddTwoItemsAndVerifyCartTest                        │
│     └── RemoveItemFromCartTest                              │
│  CheckoutTests                                              │
│     ├── SuccessfulCheckoutTest                              │
│     └── VerifyCheckoutPricesTest                            │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│              REGRESSION TESTS (32 tests)                    │
│              Full Coverage - 12 min                         │
├─────────────────────────────────────────────────────────────┤
│  All tests tagged with [regression] EXCEPT smoke tests      │
│  (to avoid duplication)                                     │
└─────────────────────────────────────────────────────────────┘
---

## Run by Suite

| Command | Tests | Duration |
|---------|-------|----------|
| `mvn test -DsuiteXmlFile=src/test/resources/testng-suites/smoke-suite.xml` | 13    |
| `mvn test -DsuiteXmlFile=src/test/resources/testng-suites/regression-suite.xml` | 32     |  

---

## Run by Test Class

```bash
mvn test -Dtest=LoginTest
mvn test -Dtest=ProductsTest
mvn test -Dtest=CheckoutTests
```

---

## Run by Group

```bash
mvn test -Dgroups=smoke
mvn test -Dgroups=cart
mvn test -Dgroups=validation
```

---

## Browser Configuration

Edit `src/test/resources/config/env.json`:

```json
{
  "browser": "chrome",    // Options: "chrome", "edge", "firefox"
  "headless": false       // true = headless, false = GUI
}
```

---

## Parallel Execution

Edit suite XML file:

```xml
<suite name="Suite" parallel="classes" thread-count="3">
```

**Options:**
- `parallel="false"` - Sequential
- `parallel="classes"` - Parallel by class
- `parallel="methods"` - Parallel by method

---

## Troubleshooting

**Clean dependencies:**
```bash
mvn clean install -DskipTests
```

**Generate Allure report manually:**
```bash
allure serve target/allure-results
```

**Kill Allure port:**
```bash
lsof -ti:8080 | xargs kill -9  # Mac/Linux
netstat -ano | findstr :8080   # Windows
```