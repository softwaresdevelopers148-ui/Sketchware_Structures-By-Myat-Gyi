package com.mg.pdfdownloader;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Uses the same public, official form flow as the MmBookshelf website:
 * GET book page -> read CSRF token and uid -> POST /download-book ->
 * receive the server-generated PDF URL.
 *
 * It does not guess PDF URLs, bypass access controls, or process a batch.
 */
public final class MBookshelfOfficialDownloader {
    private static final String OFFICIAL_HOST = "mbookshelf.naingdroidapps.com";
    private static final String DOWNLOAD_PATH = "/download-book";
    private static final String USER_AGENT = "MG-PDF-Downloader/1.0 (official-link-client)";

    // The current website returns files from this official file host.
    // Add another host only when it is confirmed by the website owner.
    private static final String[] ALLOWED_FILE_HOSTS = {
            "mbshelf.naingdroidapps.com",
            "mbookshelf.naingdroidapps.com"
    };

    private static final Pattern FORM_PATTERN = Pattern.compile(
            "<form\\b[^>]*\\bid\\s*=\\s*[\\\"']main-form[\\\"'][^>]*>",
            Pattern.CASE_INSENSITIVE);
    private static final Pattern INPUT_PATTERN = Pattern.compile(
            "<input\\b[^>]*>", Pattern.CASE_INSENSITIVE);
    private static final Pattern ATTRIBUTE_PATTERN = Pattern.compile(
            "([a-zA-Z_:][-a-zA-Z0-9_:.]*)\\s*=\\s*([\\\"'])(.*?)\\2",
            Pattern.CASE_INSENSITIVE | Pattern.DOTALL);

    private MBookshelfOfficialDownloader() { }

    public interface Callback {
        void onSuccess(String officialDownloadUrl);
        void onError(String message);
    }

    /** Resolve one user-selected official book page. No batch operation is exposed. */
    public static void resolve(Context context, String bookPageUrl, Callback callback) {
        final Handler main = new Handler(Looper.getMainLooper());
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                String result = resolveBlocking(bookPageUrl);
                main.post(() -> callback.onSuccess(result));
            } catch (Exception error) {
                String message = error.getMessage() == null
                        ? "Official download could not be resolved"
                        : error.getMessage();
                main.post(() -> callback.onError(message));
            } finally {
                executor.shutdown();
            }
        });
    }

    private static String resolveBlocking(String pageUrl) throws Exception {
        validateBookPageUrl(pageUrl);

        HttpURLConnection pageConnection = null;
        InputStream pageInput = null;
        try {
            pageConnection = openConnection(pageUrl, "GET");
            int code = pageConnection.getResponseCode();
            pageInput = code >= 200 && code < 300
                    ? pageConnection.getInputStream()
                    : pageConnection.getErrorStream();
            if (pageInput == null || code < 200 || code >= 300) {
                throw new Exception("Book page returned HTTP " + code);
            }

            String html = readText(pageInput);
            String cookieHeader = cookiesFrom(pageConnection);
            String formTag = findFormTag(html);
            if (formTag == null) {
                throw new Exception("Official Download form was not found");
            }

            String action = attribute(formTag, "action");
            String token = inputValue(html, "_token");
            String uid = inputValue(html, "uid");
            if (action.length() == 0 || token.length() == 0 || uid.length() == 0) {
                throw new Exception("Official download form data is incomplete");
            }

            String actionUrl = new URL(new URL(pageUrl), action).toString();
            validateDownloadAction(actionUrl);
            return postForOfficialUrl(actionUrl, pageUrl, cookieHeader, token, uid);
        } finally {
            if (pageInput != null) {
                try { pageInput.close(); } catch (Exception ignored) { }
            }
            if (pageConnection != null) pageConnection.disconnect();
        }
    }

    private static String postForOfficialUrl(String actionUrl, String referer,
                                             String cookieHeader, String token,
                                             String uid) throws Exception {
        HttpURLConnection connection = null;
        InputStream input = null;
        try {
            connection = openConnection(actionUrl, "POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            connection.setRequestProperty("Accept", "text/plain, */*;q=0.01");
            connection.setRequestProperty("Referer", referer);
            if (cookieHeader.length() > 0) connection.setRequestProperty("Cookie", cookieHeader);

            String formBody = "_token=" + encode(token) + "&uid=" + encode(uid);
            OutputStream output = connection.getOutputStream();
            output.write(formBody.getBytes(StandardCharsets.UTF_8));
            output.flush();
            output.close();

            int code = connection.getResponseCode();
            input = code >= 200 && code < 300
                    ? connection.getInputStream()
                    : connection.getErrorStream();
            if (input == null || code < 200 || code >= 300) {
                throw new Exception("Official download request returned HTTP " + code);
            }

            String returned = readText(input).trim();
            String officialUrl = resolveReturnedUrl(actionUrl, returned);
            validateReturnedFileUrl(officialUrl);
            return officialUrl;
        } finally {
            if (input != null) {
                try { input.close(); } catch (Exception ignored) { }
            }
            if (connection != null) connection.disconnect();
        }
    }

    private static HttpURLConnection openConnection(String url, String method) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setRequestMethod(method);
        connection.setConnectTimeout(15000);
        connection.setReadTimeout(30000);
        connection.setInstanceFollowRedirects(true);
        connection.setRequestProperty("User-Agent", USER_AGENT);
        return connection;
    }

    private static void validateBookPageUrl(String pageUrl) throws Exception {
        URL url = new URL(pageUrl);
        if (!"https".equalsIgnoreCase(url.getProtocol())
                || !OFFICIAL_HOST.equalsIgnoreCase(url.getHost())
                || !url.getPath().startsWith("/book/")) {
            throw new Exception("Only official MmBookshelf book pages are accepted");
        }
    }

    private static void validateDownloadAction(String actionUrl) throws Exception {
        URL url = new URL(actionUrl);
        if (!"https".equalsIgnoreCase(url.getProtocol())
                || !OFFICIAL_HOST.equalsIgnoreCase(url.getHost())
                || !DOWNLOAD_PATH.equals(url.getPath())) {
            throw new Exception("Unexpected download action; request stopped");
        }
    }

    private static void validateReturnedFileUrl(String fileUrl) throws Exception {
        URL url = new URL(fileUrl);
        if (!"https".equalsIgnoreCase(url.getProtocol()) || !isAllowedFileHost(url.getHost())) {
            throw new Exception("Returned file host is not an approved official host");
        }
    }

    private static boolean isAllowedFileHost(String host) {
        for (String allowed : ALLOWED_FILE_HOSTS) {
            if (allowed.equalsIgnoreCase(host)) return true;
        }
        return false;
    }

    private static String resolveReturnedUrl(String baseUrl, String returned) throws Exception {
        String value = returned.replace("\\\"", "").trim();
        if (value.length() == 0 || value.startsWith("<")) {
            throw new Exception("Website did not return a download URL");
        }
        return new URL(new URL(baseUrl), value).toString();
    }

    private static String findFormTag(String html) {
        Matcher matcher = FORM_PATTERN.matcher(html);
        return matcher.find() ? matcher.group() : null;
    }

    private static String inputValue(String html, String wantedName) {
        Matcher matcher = INPUT_PATTERN.matcher(html);
        while (matcher.find()) {
            String tag = matcher.group();
            if (wantedName.equalsIgnoreCase(attribute(tag, "name"))) {
                return attribute(tag, "value");
            }
        }
        return "";
    }

    private static String attribute(String tag, String wantedName) {
        Matcher matcher = ATTRIBUTE_PATTERN.matcher(tag);
        while (matcher.find()) {
            if (wantedName.equalsIgnoreCase(matcher.group(1))) {
                return matcher.group(3);
            }
        }
        return "";
    }

    private static String cookiesFrom(HttpURLConnection connection) {
        List<String> values = connection.getHeaderFields().get("Set-Cookie");
        if (values == null) values = connection.getHeaderFields().get("set-cookie");
        if (values == null) return "";

        ArrayList<String> cookies = new ArrayList<>();
        for (String value : values) {
            if (value == null) continue;
            try {
                List<HttpCookie> parsed = HttpCookie.parse(value);
                for (HttpCookie cookie : parsed) {
                    cookies.add(cookie.getName() + "=" + cookie.getValue());
                }
            } catch (Exception ignored) { }
        }
        return join(cookies, "; ");
    }

    private static String readText(InputStream input) throws Exception {
        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(input, StandardCharsets.UTF_8));
        String line;
        while ((line = reader.readLine()) != null) output.append(line).append('\n');
        return output.toString();
    }

    private static String encode(String value) throws Exception {
        return URLEncoder.encode(value, "UTF-8");
    }

    private static String join(List<String> values, String separator) {
        StringBuilder result = new StringBuilder();
        for (String value : values) {
            if (result.length() > 0) result.append(separator);
            result.append(value);
        }
        return result.toString();
    }
}
