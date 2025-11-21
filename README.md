# 🎓 ITI Graduation Project: Swag Labs Automation Framework

**Comprehensive Selenium WebDriver + TestNG + Allure Reports Automation Framework**

---

## 📋 Table of Contents

- [Project Overview](#-project-overview)
- [Technologies Used](#-technologies-used)
- [Framework Architecture](#-framework-architecture)
- [Project Structure](#-project-structure)
- [Installation & Setup](#-installation--setup)
- [Running Tests](#-running-tests)
- [Allure Reports](#-allure-reports)
- [Test Coverage](#-test-coverage)
- [Design Patterns](#-design-patterns)
- [Key Features](#-key-features)
- [Contributing](#-contributing)

---

## 🎯 Project Overview

This project is a **production-ready automation framework** developed for **ITI Graduation Project** requirements. It demonstrates industry-standard automation practices including:

- ✅ **Page Object Model (POM)** with Fluent Interface
- ✅ **Factory Design Pattern** for WebDriver management
- ✅ **Component-Based Architecture** for reusable page components
- ✅ **Data-Driven Testing** using JSON files
- ✅ **TestNG Framework** with listeners, dependencies, priorities, and suites
- ✅ **Allure Reporting** with detailed test steps and screenshots
- ✅ **Clean, maintainable, and scalable code**

**Application Under Test:** [Sauce Demo](https://www.saucedemo.com/)

---

## 🛠 Technologies Used

| Technology | Version | Purpose |
|------------|---------|---------|
| **Java** | 21      | Programming Language |
| **Selenium WebDriver** | 4.38.0  | Browser Automation |
| **TestNG** | 7.11.0  | Test Framework |
| **Allure** | 2.30.0  | Test Reporting |
| **Maven** | 3.9.11    | Build & Dependency Management |
| **Gson** | 2.13.2  | JSON Data Parsing |
| **AspectJ** | 1.9.25  | AOP for Allure @Step |

---

## 🏗 Framework Architecture
```
┌─────────────────────────────────────────────────────────────┐
│                      TEST LAYER                             │
│  (LoginTest, ProductsTest, CartTests, CheckoutTests...)     │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                    PAGE LAYER                               │
│  (LoginPage, ProductsPage, CartPage, CheckoutPage...)       │
│  - Page Object Model (POM)                                  │
│  - Fluent Interface (@return this)                          │
│  - Allure @Step Annotations                                 │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                  COMPONENT LAYER                            │
│  (CartComponent - Reusable page components)                 │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                   BOT LAYER                                 │
│  (ActionsBot, Waitsbot - WebDriver wrappers)                │
│  - Smart waits, scrolling, JS fallback                      │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                 DRIVER LAYER                                │
│  (WebDriverFactory, ChromeFactory, EdgeFactory)             │
│  - Factory Pattern                                          │
│  - ThreadLocal for parallel execution                       │
└──────────────────────┬──────────────────────────────────────┘
                       │
┌──────────────────────▼──────────────────────────────────────┐
│                  UTILITY LAYER                              │
│  (JsonDataReader, EnvFactory, AllureLogger)                 │
│  - Configuration management                                 │
│  - Test data management                                     │
└─────────────────────────────────────────────────────────────┘
```

---

## 📁 Project Structure
```
swag/
│
├── src/
│   ├── main/java/
│   │   ├── bots/
│   │   │   ├── ActionsBot.java          # Smart WebDriver actions
│   │   │   └── Waitsbot.java            # Fluent wait utilities
│   │   │
│   │   ├── drivers/
│   │   │   ├── Abstractdriver.java      # Abstract factory
│   │   │   ├── WebDriverFactory.java    # Driver factory (ThreadLocal)
│   │   │   ├── ChromeFactory.java       # Chrome driver implementation
│   │   │   └── EdgeFactory.java         # Edge driver implementation
│   │   │
│   │   ├── pages/
│   │   │   ├── BasePage.java            # Base page class (generic)
│   │   │   ├── LoginPage.java           # Login page POM
│   │   │   ├── ProductsPage.java        # Products page POM
│   │   │   ├── ProductDetailsPage.java  # Product details POM
│   │   │   ├── CartPage.java            # Cart page POM
│   │   │   ├── CheckoutPage.java        # Checkout pages POM
│   │   │   │
│   │   │   └── components/
│   │   │       └── CartComponent.java   # Reusable cart component
│   │   │
│   │   └── utils/
│   │       ├── EnvFactory.java          # Environment configuration
│   │       ├── JsonDataReader.java      # JSON data reader
│   │       └── AllureLogger.java        # Allure logging utility
│   │
│   └── test/java/
│       ├── tests/
│       │   ├── TestBase.java            # Base test class
│       │   ├── LoginTest.java           # Login tests
│       │   ├── ProductsTest.java        # Products tests
│       │   ├── ProductDetailsPageTest.java
│       │   ├── CartTests.java           # Cart tests
│       │   └── CheckoutTests.java       # Checkout tests
│       │
│       ├── listeners/
│       │   ├── TestListener.java        # ITestListener implementation
│       │   ├── SuiteListener.java       # ISuiteListener implementation
│       │   ├── RetryListener.java       # Retry transformer
│       │   └── RetryAnalyzer.java       # Retry logic
│       │
│       └── resources/
│           ├── config/
│           │   └── env.json             # Environment config
│           │
│           ├── testdata/
│           │   ├── loginData.json       # Login test data
│           │   ├── checkoutData.json    # Checkout test data
│           │   └── productsData.json    # Products test data
│           │
│           ├── testng-suites/
│           │   ├── master-suite.xml     # Master suite (all tests)
│           │   ├── smoke-suite.xml      # Smoke tests
│           │   ├── regression-suite.xml # Regression tests
│           │   └── login-suite.xml      # Login tests only
│           │
│           └── allure.properties        # Allure configuration
│
├── pom.xml                              # Maven configuration
├── .gitignore                           # Git ignore rules
└── README.md                            # This file
```

---

## 🚀 Installation & Setup

### Prerequisites

1. **Java JDK 21** (or compatible version)
```bash
   java -version
```

2. **Maven 3.9.11**
```bash
   mvn -version
```

3. **Google Chrome** or **Microsoft Edge** browser

### Setup Steps

1. **Clone the repository**
```bash
   git clone <your-repo-url>
   cd swag
```

2. **Install dependencies**
```bash
   mvn clean install -DskipTests
```

3. **Verify configuration**
    - Check `src/test/resources/config/env.json`
    - Ensure browser is set to `chrome` or `edge`
    - Set `headless: false` for visual execution

---

## ▶️ Running Tests

### Run All Tests (Master Suite)
```bash
mvn clean test
```

### Run Specific Test Suites
**Smoke Tests** (Critical path - ~2-3 minutes)
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng-suites/smoke-suite.xml
```

**Regression Tests** (Full coverage - ~10-15 minutes)
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng-suites/regression-suite.xml
```

**Login Tests Only**
```bash
mvn clean test -DsuiteXmlFile=src/test/resources/testng-suites/login-suite.xml
```

### Run Tests by Groups
```bash
# Run only smoke tests
mvn clean test -Dgroups=smoke

# Run cart tests
mvn clean test -Dgroups=cart

# Run validation tests
mvn clean test -Dgroups=validation
```

### Run Specific Test Class
```bash
mvn clean test -Dtest=LoginTest
mvn clean test -Dtest=CheckoutTests
```

---

## 📊 Allure Reports

### Generate and View Report

**Option 1: Serve report (opens in browser automatically)**
```bash
mvn allure:serve
```

**Option 2: Generate HTML report**
```bash
mvn allure:report
# Report will be in: target/site/allure-maven-plugin/index.html
```

**Generate report using Allure CLI:**
```bash
allure serve target/allure-results
```

### Allure Report Features

- ✅ **Test execution overview** (passed/failed/skipped)
- ✅ **Detailed test steps** (from @Step annotations)
- ✅ **Screenshots on failure**
- ✅ **Execution timeline**
- ✅ **Test categorization** (Epic, Feature, Story)
- ✅ **Historical trends**
- ✅ **Flaky test detection**

---

## 📝 Test Coverage

### Test Suites

| Suite | Tests | Duration | Purpose |
|-------|-------|----------|---------|
| **Master Suite** | 45+ | ~15 min | Complete test coverage |
| **Smoke Suite** | 8 | ~3 min | Critical path validation |
| **Regression Suite** | 45+ | ~15 min | Full functional testing |
| **Login Suite** | 3 | ~1 min | Authentication testing |

### Test Categories

#### 🔐 Login Tests (3 tests)
- Valid login with correct credentials
- Invalid login scenarios (5 combinations)
- Login page accessibility

#### 🛍 Products Tests (15 tests)
- Product listing verification
- Add/Remove items (single & bulk)
- Sorting (by name & price)
- Product navigation (by name & image)
- Cart badge validation

#### 📋 Product Details Tests (7 tests)
- Product information display
- Add/Remove from details page
- Navigation flows
- All products verification

#### 🛒 Cart Tests (5 tests)
- Cart item management
- Cart badge synchronization
- Continue shopping flow
- Cart persistence

#### 💳 Checkout Tests (7 tests)
- Complete checkout flow
- Form validation
- Price calculations (subtotal, tax, total)
- Data-driven checkout
- Post-checkout navigation

---

## 🎨 Design Patterns

### 1. Page Object Model (POM)
- Each page is represented by a class
- Locators and actions are encapsulated
- Tests interact with pages, not raw WebDriver

### 2. Fluent Interface Pattern
- All page methods return `this` or next page
- Enables method chaining: `page.action1().action2().assert()`
- Improves test readability

### 3. Factory Pattern
- `WebDriverFactory` creates browser instances
- Supports multiple browsers (Chrome, Edge)
- ThreadLocal for parallel execution safety

### 4. Component Pattern
- `CartComponent` - reusable cart functionality
- Used by multiple pages (CartPage, ProductsPage)
- Promotes DRY (Don't Repeat Yourself)

### 5. Singleton Pattern
- `EnvFactory` - single configuration instance
- `JsonDataReader` - single data reader instance

---

## ⭐ Key Features

### ✅ TestNG Structure
- **Listeners**: TestListener, SuiteListener, RetryListener
- **Dependencies**: `dependsOnMethods` for test ordering
- **Priorities**: `priority = 1, 2, 3...` for execution order
- **Groups**: `smoke`, `regression`, `cart`, `validation`
- **Data Providers**: JSON-driven test data
- **Suites**: Master, Smoke, Regression, Module-specific

### ✅ Data-Driven Testing
- **JSON files** for test data (not hardcoded)
- `loginData.json`, `checkoutData.json`, `productsData.json`
- Easily maintainable and extendable
- Supports multiple test scenarios

### ✅ Allure Reporting
- **@Step** annotations for detailed reporting
- **Automatic screenshots** on test failure
- **Rich HTML reports** with charts and graphs
- **Test categorization** (Epic, Feature, Story, Severity)
- **Attachments** (logs, JSON data)

### ✅ Smart WebDriver Interactions
- **ActionsBot**: Handles waits, scrolling, JS fallback
- **Waitsbot**: Fluent waits with polling
- **No StaleElementException**: Robust element handling
- **No hardcoded waits**: Dynamic explicit waits

### ✅ Clean Code Principles
- **Single Responsibility**: Each class has one purpose
- **DRY**: No code duplication
- **Readable**: Clear method and variable names
- **Maintainable**: Easy to extend and modify
- **Well-documented**: Javadoc comments

---

## 📚 Learning Outcomes (ITI Project Requirements)

This project demonstrates:

1. ✅ **Full TestNG Structure**: Listeners, dependencies, priorities, groups, suites
2. ✅ **Clean & Reusable Code**: Page Object Model, Fluent Interface, Components
3. ✅ **Design Patterns**: POM, Fluent, Factory, Component, Singleton
4. ✅ **Data-Driven Testing**: JSON files with multiple test scenarios
5. ✅ **Allure Reporting**: Comprehensive test reports with steps and screenshots
6. ✅ **Professional Documentation**: Complete README, Javadoc, inline comments
7. ✅ **Industry Best Practices**: ThreadLocal drivers, smart waits, error handling

---

## 🤝 Contributing

This is an ITI graduation project. For questions or suggestions:

1. Contact: 3assem2001@gmail.com
2. LinkedIn: www.linkedin.com/in/mohamedassem01
3. GitHub: https://github.com/3assem23

---

## 📄 License

This project is developed for educational purposes as part of ITI graduation requirements.

---

## 🎓 Author

**Mohamed Assem**  
ITI Graduation Project - 2025  
Software Testing Track

---

## 🙏 Acknowledgments

- **ITI (Information Technology Institute)** for providing training
- **Sauce Demo** for providing test application
- **Allure Framework** for excellent reporting capabilities
- **TestNG & Selenium** communities for comprehensive documentation
