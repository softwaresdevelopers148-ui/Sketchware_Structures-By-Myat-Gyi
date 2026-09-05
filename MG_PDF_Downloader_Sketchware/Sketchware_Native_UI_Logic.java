/* Native UI integration for Sketchware Pro. Use this instead of the
 * ArrayAdapter section in Sketchware_MainActivity_Logic.java.
 * No WebView is used. The website URLs remain data fields and are opened
 * only when the official page action is needed.
 *
 * Imports to add:
 * import android.view.View;
 * import android.widget.Toast;
 * import android.text.Editable;
 * import android.text.TextWatcher;
 * import java.util.ArrayList;
 * import java.util.List;
 * import com.mg.pdfdownloader.Book;
 * import com.mg.pdfdownloader.CatalogLoader;
 * import com.mg.pdfdownloader.NativeBookAdapter;
 */

private final ArrayList<Book> mgAllBooks = new ArrayList<>();
private final ArrayList<Book> mgVisibleBooks = new ArrayList<>();
private NativeBookAdapter mgNativeAdapter;
private final String CATALOG_URL = "https://YOUR-AUTHORIZED-DOMAIN.example/catalog.mbookshelf.json";

/* Call once from onCreate after setContentView(...). */
private void setupMgNativeUi() {
    mgNativeAdapter = new NativeBookAdapter(this, mgVisibleBooks,
            new NativeBookAdapter.Listener() {
        @Override public void onDownload(Book book) {
            mgMbookshelfDownloadOrOpen(book);
        }

        @Override public void onOpenOfficialPage(Book book) {
            mgOpenOfficialPage(book.officialPageUrl);
        }
    });
    listview_books.setAdapter(mgNativeAdapter);
    listview_books.setDivider(null);
    listview_books.setSelector(android.R.color.transparent);

    edittext_search.addTextChangedListener(new TextWatcher() {
        @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
            mgNativeApplyFilter(s == null ? "" : s.toString());
        }
        @Override public void afterTextChanged(Editable s) { }
    });
    mgNativeLoadCatalog();
}

private void mgNativeLoadCatalog() {
    progressbar_catalog.setVisibility(View.VISIBLE);
    CatalogLoader.load(this, CATALOG_URL, new CatalogLoader.Callback() {
        @Override public void onSuccess(List<Book> books) {
            progressbar_catalog.setVisibility(View.GONE);
            mgAllBooks.clear();
            mgAllBooks.addAll(books);
            mgNativeApplyFilter(edittext_search.getText().toString());
        }

        @Override public void onError(String message) {
            progressbar_catalog.setVisibility(View.GONE);
            textview_empty.setText("Catalog load failed: " + message);
            textview_empty.setVisibility(View.VISIBLE);
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
        }
    });
}

private void mgNativeApplyFilter(String query) {
    String q = query == null ? "" : query.trim().toLowerCase();
    mgVisibleBooks.clear();
    for (Book book : mgAllBooks) {
        String searchable = (book.title + " " + book.author).toLowerCase();
        if (q.length() == 0 || searchable.contains(q)) mgVisibleBooks.add(book);
    }
    if (mgNativeAdapter != null) mgNativeAdapter.notifyDataSetChanged();
    textview_empty.setVisibility(mgVisibleBooks.isEmpty() ? View.VISIBLE : View.GONE);
    if (mgVisibleBooks.isEmpty()) textview_empty.setText("No books found");
}
