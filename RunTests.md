# 🚀 Test Execution Guide

## Quick Start

### 1. Run All Tests
```bash
mvn clean test
```

### 2. Generate Allure Report
```bash
mvn allure:serve
```

---

## Execution Options

### By Suite

| Command | Suite | Tests | Duration |
|---------|-------|-------|----------|
| `mvn test -DsuiteXmlFile=src/test/resources/testng-suites/smoke-suite.xml` | Smoke | 8 | ~3 min |
| `mvn test -DsuiteXmlFile=src/test/resources/testng-suites/regression-suite.xml` | Regression | 45+ | ~15 min |
| `mvn test -DsuiteXmlFile=src/test/resources/testng-suites/login-suite.xml` | Login | 3 | ~1 min |

### By Test Class
```bash
# Run specific test class
mvn test -Dtest=LoginTest
mvn test -Dtest=ProductsTest
mvn test -Dtest=CartTests
mvn test -Dtest=CheckoutTests
mvn test -Dtest=ProductDetailsPageTest
```

### By Test Group
```bash
# Critical tests only
mvn test -Dgroups=smoke

# Shopping cart tests
mvn test -Dgroups=cart

# Form validation tests
mvn test -Dgroups=validation

# All regression tests
mvn test -Dgroups=regression
```

### By Priority
```bash
# Run high priority tests only (priority 1-3)
mvn test -DpriorityRange=1-3
```

---

## Browser Configuration

Edit `src/test/resources/config/env.json`:
```json
{
  "browser": "chrome",    // Options: "chrome", "edge"
  "headless": false       // true = headless, false = GUI
}
```

### Run Tests in Different Browsers

**Chrome (Default):**
```bash
mvn test
```

**Edge:**
```json
// Change env.json
{
  "browser": "edge"
}
```

**Headless Mode:**
```json
{
  "headless": true
}
```

---

## Parallel Execution

Edit `regression-suite.xml`:
```xml
<suite name="Regression Suite" parallel="classes" thread-count="3">
```

**Parallel Options:**
- `parallel="false"` - Sequential execution
- `parallel="classes"` - Run test classes in parallel
- `parallel="methods"` - Run test methods in parallel
- `thread-count="3"` - Number of parallel threads

---

## CI/CD Integration

### GitHub Actions

Create `.github/workflows/tests.yml`:
```yaml
name: Automated Tests

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  test:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 25
      uses: actions/setup-java@v3
      with:
        java-version: '25'
        distribution: 'temurin'
        
    - name: Run Tests
      run: mvn clean test
      
    - name: Generate Allure Report
      if: always()
      run: mvn allure:report
      
    - name: Upload Allure Results
      if: always()
      uses: actions/upload-artifact@v3
      with:
        name: allure-results
        path: target/allure-results
```

### Jenkins Pipeline

Create `Jenkinsfile`:
```groovy
pipeline {
    agent any
    
    tools {
        maven 'Maven 3.9'
        jdk 'JDK 25'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Run Tests') {
            steps {
                sh 'mvn clean test'
            }
        }
        
        stage('Generate Report') {
            steps {
                allure([
                    includeProperties: false,
                    jdk: '',
                    properties: [],
                    reportBuildPolicy: 'ALWAYS',
                    results: [[path: 'target/allure-results']]
                ])
            }
        }
    }
    
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}
```

---

## Troubleshooting

### Common Issues

**1. Tests fail to start:**
```bash
# Clean and reinstall dependencies
mvn clean install -DskipTests
```

**2. Browser driver issues:**
```bash
# Ensure browser is installed
google-chrome --version
microsoft-edge --version
```

**3. Allure report not generating:**
```bash
# Install Allure CLI
brew install allure  # Mac
choco install allure  # Windows

# Generate manually
allure serve target/allure-results
```

**4. Port already in use (Allure):**
```bash
# Kill process on port 8080
lsof -ti:8080 | xargs kill -9  # Mac/Linux
netstat -ano | findstr :8080   # Windows
```

---

## Best Practices

### Before Committing
```bash
# Run smoke tests
mvn test -Dgroups=smoke

# Check for compilation errors
mvn clean compile

# Verify all tests pass
mvn clean test
```

### Before Deployment
```bash
# Full regression suite
mvn clean test -DsuiteXmlFile=src/test/resources/testng-suites/regression-suite.xml

# Generate and review report
mvn allure:serve
```