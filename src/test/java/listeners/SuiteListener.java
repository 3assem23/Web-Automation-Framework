package listeners;

import io.qameta.allure.Allure;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import utils.EnvFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;


public class SuiteListener implements ISuiteListener {

    private LocalDateTime suiteStartTime;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public void onStart(ISuite suite) {
        suiteStartTime = LocalDateTime.now();

        addEnvironmentToAllure();

        addSuiteMetadataToAllure(suite);

        System.out.println("\n");
        System.out.println("======================================================================");
        System.out.println("              SAUCE DEMO AUTOMATION FRAMEWORK                         ");
        System.out.println("======================================================================");
        System.out.println("   Suite Name       : " + suite.getName());
        System.out.println("   Start Time       : " + suiteStartTime.format(formatter));
        System.out.println("   Parallel Mode    : " + suite.getParallel());
        System.out.println("   Thread Count     : " + suite.getXmlSuite().getThreadCount());
        System.out.println("   Browser          : " + EnvFactory.getBrowser().toUpperCase());
        System.out.println("   Headless Mode    : " + EnvFactory.isHeadless());
        System.out.println("   Base URL         : " + EnvFactory.getBaseUrl());
        System.out.println("======================================================================");
        System.out.println();
    }


    @Override
    public void onFinish(ISuite suite) {
        LocalDateTime suiteEndTime = LocalDateTime.now();
        long durationSeconds = java.time.Duration.between(suiteStartTime, suiteEndTime).getSeconds();

        int totalTests = suite.getAllMethods().size();
        int passedTests = suite.getResults().values().stream()
                .mapToInt(result -> result.getTestContext().getPassedTests().size())
                .sum();
        int failedTests = suite.getResults().values().stream()
                .mapToInt(result -> result.getTestContext().getFailedTests().size())
                .sum();
        int skippedTests = suite.getResults().values().stream()
                .mapToInt(result -> result.getTestContext().getSkippedTests().size())
                .sum();

        System.out.println("======================================================================");
        System.out.println("                   SUITE EXECUTION SUMMARY                            ");
        System.out.println("======================================================================");
        System.out.println("   Suite Name       : " + suite.getName());
        System.out.println("   End Time         : " + suiteEndTime.format(formatter));
        System.out.println("   Duration         : " + formatDuration(durationSeconds));
        System.out.println("----------------------------------------------------------------------");
        System.out.println("   Total Tests      : " + totalTests);
        System.out.println("    Passed         : " + passedTests);
        System.out.println("    Failed         : " + failedTests);
        System.out.println("    Skipped        : " + skippedTests);
        System.out.println("   Success Rate     : " + calculateSuccessRate(passedTests, totalTests) + "%");
        System.out.println("======================================================================");
        System.out.println("\n Allure results saved to: target/allure-results");
        System.out.println(" Generate report with: mvn allure:serve\n");

        addExecutionSummaryToAllure(suite, durationSeconds, totalTests, passedTests, failedTests, skippedTests);
    }



    private String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        if (hours > 0) {
            return String.format("%d hours, %d minutes, %d seconds", hours, minutes, secs);
        } else if (minutes > 0) {
            return String.format("%d minutes, %d seconds", minutes, secs);
        } else {
            return String.format("%d seconds", secs);
        }
    }



    private double calculateSuccessRate(int passed, int total) {
        if (total == 0) return 0.0;
        return Math.round((passed * 100.0 / total) * 100.0) / 100.0;
    }



    private void addEnvironmentToAllure() {
        try {
            String envFilePath = "src/test/resources/environment.properties";

            if (Files.exists(Paths.get(envFilePath))) {
                Properties props = new Properties();
                FileInputStream fis = new FileInputStream(envFilePath);
                props.load(fis);

                // Create environment file content
                StringBuilder envContent = new StringBuilder();
                props.forEach((key, value) ->
                        envContent.append(key).append("=").append(value).append("\n")
                );

                // Attach to Allure
                Allure.addAttachment("Environment Configuration", "text/plain",
                        envContent.toString(), ".txt");

                fis.close();
            } else {
                System.out.println("⚠ Warning: environment.properties file not found at " + envFilePath);
            }
        } catch (IOException e) {
            System.err.println("⚠ Could not load environment.properties: " + e.getMessage());
        }
    }



    private void addSuiteMetadataToAllure(ISuite suite) {
        StringBuilder metadata = new StringBuilder();
        metadata.append("Suite Information\n");
        metadata.append("=================\n\n");
        metadata.append("Suite Name: ").append(suite.getName()).append("\n");
        metadata.append("Start Time: ").append(suiteStartTime.format(formatter)).append("\n");
        metadata.append("Parallel Mode: ").append(suite.getParallel()).append("\n");
        metadata.append("Thread Count: ").append(suite.getXmlSuite().getThreadCount()).append("\n");
        metadata.append("\nConfiguration\n");
        metadata.append("=============\n\n");
        metadata.append("Browser: ").append(EnvFactory.getBrowser()).append("\n");
        metadata.append("Headless: ").append(EnvFactory.isHeadless()).append("\n");
        metadata.append("Base URL: ").append(EnvFactory.getBaseUrl()).append("\n");

        Allure.addAttachment("Suite Metadata", "text/plain", metadata.toString(), ".txt");
    }


    /**
     * Add execution summary to Allure
     */
    private void addExecutionSummaryToAllure(ISuite suite, long duration, int total,
                                             int passed, int failed, int skipped) {
        StringBuilder summary = new StringBuilder();
        summary.append("Execution Summary\n");
        summary.append("=================\n\n");
        summary.append("Suite: ").append(suite.getName()).append("\n");
        summary.append("Duration: ").append(formatDuration(duration)).append("\n\n");
        summary.append("Test Results\n");
        summary.append("------------\n");
        summary.append("Total Tests: ").append(total).append("\n");
        summary.append(" Passed: ").append(passed).append("\n");
        summary.append(" Failed: ").append(failed).append("\n");
        summary.append(" Skipped: ").append(skipped).append("\n");
        summary.append("Success Rate: ").append(calculateSuccessRate(passed, total)).append("%\n");

        Allure.addAttachment("Execution Summary", "text/plain", summary.toString(), ".txt");
    }
}