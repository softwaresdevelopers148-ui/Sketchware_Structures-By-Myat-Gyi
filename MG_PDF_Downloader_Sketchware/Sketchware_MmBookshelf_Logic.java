/*
 * Use this in place of mgDownloadOrOpen(...) when using the MmBookshelf
 * Official Download Form mode.
 *
 * Add this import to MainActivity:
 * import com.mg.pdfdownloader.MBookshelfOfficialDownloader;
 *
 * Your catalog record must contain:
 * price_type, download_allowed, title, official_page_url.
 * The app does not store or guess a PDF URL. The official website returns it
 * after its own Download form is submitted.
 */

private void mgMbookshelfDownloadOrOpen(Book book) {
    boolean markedFree = "free".equalsIgnoreCase(book.priceType);
    if (!markedFree || !book.downloadAllowed) {
        mgOpenOfficialPage(book.officialPageUrl);
        return;
    }

    if (book.officialPageUrl == null || book.officialPageUrl.length() == 0) {
        Toast.makeText(this, "Official book page is unavailable", Toast.LENGTH_SHORT).show();
        return;
    }

    progressbar_catalog.setVisibility(View.VISIBLE);
    MBookshelfOfficialDownloader.resolve(this, book.officialPageUrl,
            new MBookshelfOfficialDownloader.Callback() {
        @Override public void onSuccess(String officialDownloadUrl) {
            try {
                // Construct a temporary authorized record from the URL returned
                // by the official website. It is never guessed by the app.
                Book authorizedBook = new Book(
                        book.id,
                        book.title,
                        book.author,
                        book.coverUrl,
                        book.description,
                        "free",
                        true,
                        officialDownloadUrl,
                        book.officialPageUrl
                );
                long downloadId = PdfDownloadHelper.enqueue(MainActivity.this, authorizedBook);
                new DownloadHistoryDb(MainActivity.this).saveQueued(authorizedBook, downloadId);

                progressbar_catalog.setIndeterminate(false);
                progressbar_catalog.setMax(100);
                PdfDownloadHelper.monitor(MainActivity.this, downloadId,
                        (status, downloaded, total, percent) -> {
                    progressbar_catalog.setProgress(percent);
                    if (status == android.app.DownloadManager.STATUS_SUCCESSFUL) {
                        progressbar_catalog.setVisibility(View.GONE);
                        new DownloadHistoryDb(MainActivity.this)
                                .updateProgress(downloadId, "complete", 100);
                        Toast.makeText(MainActivity.this,
                                "PDF downloaded", Toast.LENGTH_SHORT).show();
                    } else if (status == android.app.DownloadManager.STATUS_FAILED) {
                        progressbar_catalog.setVisibility(View.GONE);
                        new DownloadHistoryDb(MainActivity.this)
                                .updateProgress(downloadId, "failed", percent);
                        Toast.makeText(MainActivity.this,
                                "Download failed", Toast.LENGTH_LONG).show();
                    } else {
                        new DownloadHistoryDb(MainActivity.this)
                                .updateProgress(downloadId, "downloading", percent);
                    }
                });
            } catch (Exception error) {
                progressbar_catalog.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }

        @Override public void onError(String message) {
            progressbar_catalog.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,
                    message, Toast.LENGTH_LONG).show();
            // User can still open the official page and use its own button.
            mgOpenOfficialPage(book.officialPageUrl);
        }
    });
}
