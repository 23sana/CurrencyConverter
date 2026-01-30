package api;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CurrencyAPI {

    public static double getRate(String from, String to) {
        try {
            // Free API — returns all currency rates
            String urlStr = "https://open.er-api.com/v6/latest/" + from;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // DEBUG — shows full API JSON in console
            System.out.println("API RESPONSE:");
            System.out.println(response.toString());

            return extractRate(response.toString(), to);

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Extracts rate like "USD":0.010872
    private static double extractRate(String json, String currency) {
        try {
            String key = "\"" + currency + "\":";
            int index = json.indexOf(key);

            if (index == -1) return -1;

            int start = index + key.length();
            int end = start;

            while (end < json.length() &&
                    (Character.isDigit(json.charAt(end)) || json.charAt(end) == '.')) {
                end++;
            }

            return Double.parseDouble(json.substring(start, end));
        } catch (Exception e) {
            return -1;
        }
    }
}
