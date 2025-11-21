package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;


public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 2; // Retry failed tests 2 times


    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            System.out.println("🔄 Retrying test: " + result.getMethod().getMethodName()
                    + " (Attempt " + (retryCount + 1) + " of " + (MAX_RETRY_COUNT + 1) + ")");
            return true;
        }
        return false;
    }
}