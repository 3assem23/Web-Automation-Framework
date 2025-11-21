package utils;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;


public class AllureLogger {

    @Step("{stepDescription}")
    public static void logStep(String stepDescription) {
        System.out.println(stepDescription);
    }

    public static void logInfo(String message) {
        Allure.step(message);
        System.out.println(message);
    }

    public static void logWarning(String message) {
        Allure.step(message);
        System.out.println(message);
    }

    public static void logError(String message) {
        Allure.step("✗ " + message);
        System.out.println("  ✗ " + message);
    }

    public static void attachText(String name, String content) {
        Allure.addAttachment(name, "text/plain", content, ".txt");
    }

    public static void attachJson(String name, String jsonContent) {
        Allure.addAttachment(name, "application/json", jsonContent, ".json");
    }
}