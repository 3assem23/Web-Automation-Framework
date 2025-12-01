package listeners;

import drivers.WebDriverFactory;
import io.qameta.allure.Allure;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.ByteArrayInputStream;


public class TestListener implements ITestListener {


    @Override
    public void onStart(ITestContext context) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("  TEST SUITE STARTED: " + context.getName());
        System.out.println("  Total Tests: " + context.getAllTestMethods().length);
        System.out.println("-------------------------------------------------------------");
    }


    @Override
    public void onFinish(ITestContext context) {
        System.out.println("-------------------------------------------------------------");
        System.out.println("  TEST SUITE FINISHED: " + context.getName());
        System.out.println("  Passed: " + context.getPassedTests().size());
        System.out.println("  Failed: " + context.getFailedTests().size());
        System.out.println("  Skipped: " + context.getSkippedTests().size());
        System.out.println("-------------------------------------------------------------\n");
    }

    @Override
    public void onTestStart(ITestResult result) {
        System.out.println("\n▶ STARTING TEST: " + getTestName(result));
    }


    @Override
    public void onTestSuccess(ITestResult result) {
        System.out.println("✔ PASSED: " + getTestName(result));
    }


    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("✗ FAILED: " + getTestName(result));
        System.out.println("  Reason: " + result.getThrowable().getMessage());

        captureScreenshot(result);

        Allure.addAttachment("Failure Reason", result.getThrowable().getMessage());

        Allure.addAttachment("Stack Trace", "text/plain",
                getStackTrace(result.getThrowable()), ".txt");
    }


    @Override
    public void onTestSkipped(ITestResult result) {
        System.out.println("⊘ SKIPPED: " + getTestName(result));
        if (result.getThrowable() != null) {
            System.out.println("  Reason: " + result.getThrowable().getMessage());
        }
    }


    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
        System.out.println("⚠ FAILED (Within Success %): " + getTestName(result));
    }


    @Override
    public void onTestFailedWithTimeout(ITestResult result) {
        System.out.println("⏱ TIMEOUT: " + getTestName(result));
        onTestFailure(result);
    }



    private String getTestName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName() + "." +
                result.getMethod().getMethodName();
    }



    private void captureScreenshot(ITestResult result) {
        try {
            WebDriver driver = WebDriverFactory.getDriver();
            if (driver != null) {
                byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

                // Attach to Allure
                Allure.addAttachment(
                        "Screenshot - " + result.getMethod().getMethodName(),
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        ".png"
                );

                System.out.println("  📸 Screenshot captured and attached to report");
            }
        } catch (Exception e) {
            System.out.println("  ⚠ Failed to capture screenshot: " + e.getMessage());
        }
    }



    private String getStackTrace(Throwable throwable) {
        if (throwable == null) return "";

        StringBuilder sb = new StringBuilder();
        sb.append(throwable.toString()).append("\n");

        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append("\tat ").append(element.toString()).append("\n");
        }

        if (throwable.getCause() != null) {
            sb.append("Caused by: ").append(getStackTrace(throwable.getCause()));
        }

        return sb.toString();
    }



    @Attachment(value = "Failure Screenshot", type = "image/png")
    public byte[] saveScreenshot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}