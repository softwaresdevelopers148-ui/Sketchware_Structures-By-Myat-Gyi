/*
 * Sketchware Pro integration snippet.
 *
 * Add these imports to MainActivity:
 * import android.content.Intent;
 * import android.net.Uri;
 * import android.view.View;
 * import android.widget.ArrayAdapter;
 * import android.widget.Toast;
 * import android.text.Editable;
 * import android.text.TextWatcher;
 * import java.util.ArrayList;
 * import java.util.List;
 * import com.mg.pdfdownloader.Book;
 * import com.mg.pdfdownloader.CatalogLoader;
 * import com.mg.pdfdownloader.DownloadHistoryDb;
 * import com.mg.pdfdownloader.PdfDownloadHelper;
 *
 * Replace the view names below if your Sketchware IDs are different.
 */

private final ArrayList<Book> mgAllBooks = new ArrayList<>();
private final ArrayList<Book> mgVisibleBooks = new ArrayList<>();
private final ArrayList<String> mgLabels = new ArrayList<>();
private ArrayAdapter<String> mgAdapter;
private final String CATALOG_URL = "https://YOUR-AUTHORIZED-DOMAIN.example/catalog.json";

/* Call this once from onCreate after setContentView(...) */
private void setupMgDownloader() {
    mgAdapter = new ArrayAdapter<String>(
            this,
            android.R.layout.simple_list_item_1,
            mgLabels
    );
    listview_books.setAdapter(mgAdapter);

    listview_books.setOnItemClickListener((parent, view, position, id) -> {
        if (position >= 0 && position < mgVisibleBooks.size()) {
            mgDownloadOrOpen(mgVisibleBooks.get(position));
        }
    });

    edittext_search.addTextChangedListener(new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            mgApplyFilter(s == null ? "" : s.toString());
        }
        @Override public void afterTextChanged(Editable s) { }
    });

    mgLoadCatalog();
}

private void mgLoadCatalog() {
    progressbar_catalog.setVisibility(View.VISIBLE);
    CatalogLoader.load(this, CATALOG_URL, new CatalogLoader.Callback() {
        @Override public void onSuccess(List<Book> books) {
            progressbar_catalog.setVisibility(View.GONE);
            mgAllBooks.clear();
            mgAllBooks.addAll(books);
            mgApplyFilter(edittext_search.getText().toString());
        }

        @Override public void onError(String message) {
            progressbar_catalog.setVisibility(View.GONE);
            textview_empty.setText("Catalog load failed: " + message);
            textview_empty.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
    });
}

private void mgApplyFilter(String query) {
    String q = query == null ? "" : query.trim().toLowerCase();
    mgVisibleBooks.clear();
    mgLabels.clear();

    for (Book book : mgAllBooks) {
        String searchable = (book.title + " " + book.author).toLowerCase();
        if (q.length() == 0 || searchable.contains(q)) {
            mgVisibleBooks.add(book);
            mgLabels.add(book.listLabel());
        }
    }

    mgAdapter.notifyDataSetChanged();
    textview_empty.setVisibility(mgVisibleBooks.isEmpty() ? View.VISIBLE : View.GONE);
    if (mgVisibleBooks.isEmpty()) textview_empty.setText("No books found");
}

private void mgDownloadOrOpen(Book book) {
    if (!book.isDownloadable()) {
        mgOpenOfficialPage(book.officialPageUrl);
        return;
    }

    try {
        long downloadId = PdfDownloadHelper.enqueue(this, book);
        new DownloadHistoryDb(this).saveQueued(book, downloadId);
        progressbar_catalog.setVisibility(View.VISIBLE);
        progressbar_catalog.setIndeterminate(false);
        progressbar_catalog.setMax(100);

        PdfDownloadHelper.monitor(this, downloadId, (status, downloaded, total, percent) -> {
            progressbar_catalog.setProgress(percent);
            String statusText = "Downloading " + percent + "%";
            if (status == android.app.DownloadManager.STATUS_SUCCESSFUL) {
                statusText = "Download complete";
                progressbar_catalog.setVisibility(View.GONE);
                new DownloadHistoryDb(this).updateProgress(downloadId, "complete", 100);
                Toast.makeText(this, "PDF downloaded", Toast.LENGTH_SHORT).show();
            } else if (status == android.app.DownloadManager.STATUS_FAILED) {
                statusText = "Download failed";
                progressbar_catalog.setVisibility(View.GONE);
                new DownloadHistoryDb(this).updateProgress(downloadId, "failed", percent);
                Toast.makeText(this, "Download failed", Toast.LENGTH_LONG).show();
            } else {
                new DownloadHistoryDb(this).updateProgress(downloadId, "downloading", percent);
            }
            textview_empty.setText(statusText);
        });
    } catch (Exception error) {
        Toast.makeText(this, error.getMessage(), Toast.LENGTH_LONG).show();
    }
}

private void mgOpenOfficialPage(String pageUrl) {
    if (pageUrl == null || !pageUrl.startsWith("https://")) {
        Toast.makeText(this, "Official page is unavailable", Toast.LENGTH_SHORT).show();
        return;
    }
    try {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(pageUrl));
        startActivity(intent);
    } catch (Exception error) {
        Toast.makeText(this, "No browser found", Toast.LENGTH_SHORT).show();
    }
}
