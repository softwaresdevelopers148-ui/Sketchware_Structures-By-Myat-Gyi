package com.mg.pdfdownloader;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Loads only an HTTPS JSON catalog supplied by the content owner. */
public final class CatalogLoader {
    private CatalogLoader() { }

    public interface Callback {
        void onSuccess(List<Book> books);
        void onError(String message);
    }

    public static void load(Context context, String endpoint, Callback callback) {
        final Handler main = new Handler(Looper.getMainLooper());
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String body = getText(endpoint);
                List<Book> result = parse(body);
                main.post(() -> callback.onSuccess(result));
            } catch (Exception error) {
                String message = error.getMessage() == null ? "Catalog load failed" : error.getMessage();
                main.post(() -> callback.onError(message));
            } finally {
                executor.shutdown();
            }
        });
    }

    private static String getText(String endpoint) throws Exception {
        if (endpoint == null || !endpoint.startsWith("https://")) {
            throw new IllegalArgumentException("Catalog endpoint must use HTTPS");
        }

        HttpURLConnection connection = null;
        InputStream input = null;
        try {
            connection = (HttpURLConnection) new URL(endpoint).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(20000);
            connection.setRequestProperty("Accept", "application/json");
            connection.setInstanceFollowRedirects(true);

            int code = connection.getResponseCode();
            input = (code >= 200 && code < 300)
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            if (input == null) throw new Exception("HTTP " + code);

            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(input, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line);
            }
            reader.close();
            if (code < 200 || code >= 300) throw new Exception("HTTP " + code);
            return output.toString();
        } finally {
            if (input != null) {
                try { input.close(); } catch (Exception ignored) { }
            }
            if (connection != null) connection.disconnect();
        }
    }

    private static List<Book> parse(String body) throws Exception {
        ArrayList<Book> books = new ArrayList<>();
        String trimmed = body == null ? "" : body.trim();
        JSONArray array;
        if (trimmed.startsWith("[")) {
            array = new JSONArray(trimmed);
        } else {
            JSONObject root = new JSONObject(trimmed);
            array = root.optJSONArray("books");
            if (array == null) array = root.optJSONArray("data");
            if (array == null) throw new Exception("JSON must contain a books array");
        }

        for (int index = 0; index < array.length(); index++) {
            JSONObject item = array.optJSONObject(index);
            if (item != null) books.add(Book.fromJson(item));
        }
        return books;
    }
}
