package listeners;

import drivers.WebDriverFactory;
import io.qameta.allure.Allure;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import org.testng.annotations.ITestAnnotation;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple TestNG Listener - Does everything in ONE file
 */
public class TestNGListener implements ITestListener, ISuiteListener, IAnnotationTransformer {

    private static final Logger log = LogManager.getLogger(TestNGListener.class);
    private LocalDateTime suiteStartTime;

    // ==================== SUITE EVENTS ====================

    @Override
    public void onStart(ISuite suite) {
        suiteStartTime = LocalDateTime.now();
        log.info("╔════════════════════════════════════════════╗");
        log.info("║      TEST SUITE STARTED: {}      ║", suite.getName());
        log.info("╚════════════════════════════════════════════╝");
    }

    @Override
    public void onFinish(ISuite suite) {
        Duration duration = Duration.between(suiteStartTime, LocalDateTime.now());

        int total = suite.getAllMethods().size();
        int passed = suite.getResults().values().stream()
                .mapToInt(r -> r.getTestContext().getPassedTests().size()).sum();
        int failed = suite.getResults().values().stream()
                .mapToInt(r -> r.getTestContext().getFailedTests().size()).sum();

        log.info("╔════════════════════════════════════════════╗");
        log.info("║           TEST SUITE FINISHED              ║");
        log.info("╠════════════════════════════════════════════╣");
        log.info("  Duration: {} minutes", duration.toMinutes());
        log.info("  Total: {}  |  Passed: {}  |  Failed: {}", total, passed, failed);
        log.info("╚════════════════════════════════════════════╝");
    }

    // ==================== TEST EVENTS ====================

    @Override
    public void onTestStart(ITestResult result) {
        log.info("▶ STARTING: {}", getTestName(result));
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("✔ PASSED: {}", getTestName(result));
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("✗ FAILED: {}", getTestName(result));
        log.error("  Reason: {}", result.getThrowable().getMessage());
        captureScreenshot(result);
        Allure.addAttachment("Error", result.getThrowable().getMessage());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("⊘ SKIPPED: {}", getTestName(result));
    }

    // ==================== AUTO RETRY ====================

    @Override
    public void transform(ITestAnnotation annotation, Class testClass,
                          Constructor testConstructor, Method testMethod) {
        annotation.setRetryAnalyzer(RetryAnalyzer.class);
    }

    // ==================== HELPERS ====================

    private String getTestName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName() + "." +
                result.getMethod().getMethodName();
    }

    private void captureScreenshot(ITestResult result) {
        try {
            WebDriver driver = WebDriverFactory.getDriver();
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                Allure.addAttachment("Screenshot", "image/png",
                        new ByteArrayInputStream(screenshot), ".png");
                log.info("  📸 Screenshot captured");
            }
        } catch (Exception e) {
            log.warn("  ⚠ Screenshot failed: {}", e.getMessage());
        }
    }

    // ==================== RETRY ANALYZER ====================

    public static class RetryAnalyzer implements IRetryAnalyzer {
        private final AtomicInteger count = new AtomicInteger(0);
        private static final int MAX = 2;

        @Override
        public boolean retry(ITestResult result) {
            if (count.get() < MAX) {
                count.incrementAndGet();
                LogManager.getLogger(RetryAnalyzer.class)
                        .warn("🔄 Retrying: {}", result.getMethod().getMethodName());
                return true;
            }
            return false;
        }
    }
}