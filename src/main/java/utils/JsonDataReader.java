package utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;


public class JsonDataReader {

    private static JsonObject loginData;
    private static JsonObject checkoutData;
    private static JsonObject productsData;

    static {
        try {
            // Load all JSON test data files
            loginData = loadJsonFile("testdata/loginData.json");
            checkoutData = loadJsonFile("testdata/checkoutData.json");
            productsData = loadJsonFile("testdata/productsData.json");
        } catch (Exception e) {
            throw new RuntimeException("Failed to load JSON test data files", e);
        }
    }


    private static JsonObject loadJsonFile(String filePath) {
        try {
            InputStream is = JsonDataReader.class.getClassLoader().getResourceAsStream(filePath);
            if (is == null) {
                throw new RuntimeException(filePath + " not found in resources");
            }
            JsonReader reader = new JsonReader(new InputStreamReader(is));
            return new Gson().fromJson(reader, JsonObject.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to read " + filePath, e);
        }
    }

    //LOGIN DATA

    public static Map<String, String> getValidLogin() {
        JsonObject valid = loginData.getAsJsonObject("valid");
        return jsonObjectToMap(valid);
    }


    public static List<Map<String, String>> getInvalidLogins() {
        JsonArray arr = loginData.getAsJsonArray("invalid");
        List<Map<String, String>> out = new ArrayList<>();
        for (JsonElement el : arr) {
            out.add(jsonObjectToMap(el.getAsJsonObject()));
        }
        return out;
    }

    //CHECKOUT DATA

    public static List<Map<String, String>> getValidCustomers() {
        JsonArray arr = checkoutData.getAsJsonArray("validCustomers");
        List<Map<String, String>> out = new ArrayList<>();
        for (JsonElement el : arr) {
            out.add(jsonObjectToMap(el.getAsJsonObject()));
        }
        return out;
    }

    public static List<Map<String, String>> getInvalidCustomers() {
        JsonArray arr = checkoutData.getAsJsonArray("invalidCustomers");
        List<Map<String, String>> out = new ArrayList<>();
        for (JsonElement el : arr) {
            out.add(jsonObjectToMap(el.getAsJsonObject()));
        }
        return out;
    }

    public static Map<String, String> getValidCustomer(int index) {
        List<Map<String, String>> customers = getValidCustomers();
        if (index >= 0 && index < customers.size()) {
            return customers.get(index);
        }
        return customers.get(0);
    }

    //PRODUCTS DATA

    public static List<Map<String, String>> getProducts() {
        JsonArray arr = productsData.getAsJsonArray("products");
        List<Map<String, String>> out = new ArrayList<>();
        for (JsonElement el : arr) {
            out.add(jsonObjectToMap(el.getAsJsonObject()));
        }
        return out;
    }

    public static Map<String, String> getProductByName(String productName) {
        List<Map<String, String>> products = getProducts();
        for (Map<String, String> product : products) {
            if (product.get("name").equals(productName)) {
                return product;
            }
        }
        return null;
    }

    //HELPER METHODS

    private static Map<String, String> jsonObjectToMap(JsonObject obj) {
        Map<String, String> map = new LinkedHashMap<>();
        for (Map.Entry<String, JsonElement> entry : obj.entrySet()) {
            JsonElement v = entry.getValue();
            map.put(entry.getKey(), v.isJsonNull() ? null : v.getAsString());
        }
        return map;
    }
}