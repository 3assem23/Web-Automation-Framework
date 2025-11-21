package listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class SuiteListener implements ISuiteListener {

    private LocalDateTime suiteStartTime;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    @Override
    public void onStart(ISuite suite) {
        suiteStartTime = LocalDateTime.now();

        System.out.println("\n");
        System.out.println("----------------------------------------------------------------------");
        System.out.println("                    SAUCE DEMO AUTOMATION FRAMEWORK                   ");
        System.out.println(" ******************************************************************** ");
        System.out.println("   Suite: " + String.format(suite.getName()) );
        System.out.println("   Start Time: " + String.format( suiteStartTime.format(formatter)) );
        System.out.println("   Parallel Execution: " + String.format( suite.getParallel()));
        System.out.println("----------------------------------------------------------------------");
        System.out.println();
    }


    @Override
    public void onFinish(ISuite suite) {
        LocalDateTime suiteEndTime = LocalDateTime.now();
        long durationSeconds = java.time.Duration.between(suiteStartTime, suiteEndTime).getSeconds();

        System.out.println("----------------------------------------------------------------------");
        System.out.println("                     SUITE EXECUTION SUMMARY                         ");
        System.out.println("**********************************************************************");
        System.out.println("   Suite: " + String.format(suite.getName()));
        System.out.println("   End Time: " + String.format( suiteEndTime.format(formatter)) );
        System.out.println("   Duration: " + String.format( formatDuration(durationSeconds)) );
        System.out.println(" ---------------------------------------------------------------------");
        System.out.println("\n Allure results saved to: target/allure-results");
        System.out.println(" Generate report with: mvn allure:serve\n");
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
}