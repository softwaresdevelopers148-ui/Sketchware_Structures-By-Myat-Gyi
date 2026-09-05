package com.mg.pdfdownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

/** Download helper. It refuses paid books, unauthorized records, and non-HTTPS URLs. */
public final class PdfDownloadHelper {
    private PdfDownloadHelper() { }

    public interface ProgressCallback {
        void onProgress(int status, long downloadedBytes, long totalBytes, int percent);
    }

    public static boolean canDownload(Book book) {
        return book != null && book.isDownloadable();
    }

    public static long enqueue(Context context, Book book) {
        if (!canDownload(book)) {
            throw new IllegalArgumentException("This book is not authorized for direct download");
        }

        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager == null) throw new IllegalStateException("DownloadManager is unavailable");

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(book.pdfUrl));
        request.setTitle(book.title);
        request.setDescription("MG PDF Downloader");
        request.setMimeType("application/pdf");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setAllowedOverMetered(true);
        request.setAllowedOverRoaming(false);
        request.setDestinationInExternalFilesDir(
                context,
                Environment.DIRECTORY_DOWNLOADS,
                "MG PDF Downloader/" + safeFileName(book.title) + ".pdf"
        );
        return manager.enqueue(request);
    }

    public static void monitor(Context context, long downloadId, ProgressCallback callback) {
        Handler handler = new Handler(Looper.getMainLooper());
        Runnable poll = new Runnable() {
            @Override public void run() {
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                if (manager == null) return;

                DownloadManager.Query query = new DownloadManager.Query();
                query.setFilterById(downloadId);
                Cursor cursor = manager.query(query);
                int status = DownloadManager.STATUS_FAILED;
                long downloaded = 0L;
                long total = 0L;
                boolean found = false;

                if (cursor != null) {
                    try {
                        if (cursor.moveToFirst()) {
                            found = true;
                            status = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS));
                            downloaded = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                            total = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        }
                    } finally {
                        cursor.close();
                    }
                }

                int percent = (total > 0L)
                        ? (int) Math.min(100L, (downloaded * 100L) / total)
                        : 0;
                callback.onProgress(status, downloaded, total, percent);

                if (found && status != DownloadManager.STATUS_SUCCESSFUL
                        && status != DownloadManager.STATUS_FAILED) {
                    handler.postDelayed(this, 500L);
                }
            }
        };
        handler.post(poll);
    }

    public static void openDownloadedPdf(Context context, long downloadId) {
        DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (manager == null) throw new IllegalStateException("DownloadManager is unavailable");
        Uri uri = manager.getUriForDownloadedFile(downloadId);
        if (uri == null) throw new IllegalStateException("PDF is not downloaded yet");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (!(context instanceof android.app.Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(Intent.createChooser(intent, "Open PDF"));
    }

    private static String safeFileName(String input) {
        String value = input == null ? "book" : input.trim();
        value = value.replaceAll("[\\\\/:*?\"<>|\r\n]+", "_");
        if (value.length() == 0) value = "book";
        return value.length() > 100 ? value.substring(0, 100) : value;
    }
}
