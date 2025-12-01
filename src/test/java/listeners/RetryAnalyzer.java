package listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

import java.util.concurrent.atomic.AtomicInteger;


public class RetryAnalyzer implements IRetryAnalyzer {

    // Thread-safe counter for parallel execution
    private final AtomicInteger retryCount = new AtomicInteger(0);
    private static final int MAX_RETRY_COUNT = 2; // Retry failed tests 2 times


    @Override
    public boolean retry(ITestResult result) {
        if (retryCount.get() < MAX_RETRY_COUNT) {
            int currentAttempt = retryCount.incrementAndGet();

            System.out.println("Retrying test: " + result.getMethod().getMethodName()
                    + " (Attempt " + (currentAttempt + 1) + " of " + (MAX_RETRY_COUNT + 1) + ")");

            return true;
        }

        return false;
    }



    public int getRetryCount() {
        return retryCount.get();
    }



    public void reset() {
        retryCount.set(0);
    }
}