package com.mg.pdfdownloader;

import org.json.JSONObject;

/** Immutable book record returned by the authorized catalog API. */
public final class Book {
    public final String id;
    public final String title;
    public final String author;
    public final String coverUrl;
    public final String description;
    public final String priceType;
    public final boolean downloadAllowed;
    public final String pdfUrl;
    public final String authorUrl;
    public final String categoryUrl;
    public final String fileSize;
    public final String pages;
    public final String downloadCount;
    public final String officialPageUrl;

    /** Backward-compatible constructor for existing Sketchware snippets. */
    public Book(String id, String title, String author, String coverUrl,
                String description, String priceType, boolean downloadAllowed,
                String pdfUrl, String officialPageUrl) {
        this(id, title, author, coverUrl, description, priceType, downloadAllowed,
                pdfUrl, officialPageUrl, "", "", "", "", "");
    }

    public Book(String id, String title, String author, String coverUrl,
                String description, String priceType, boolean downloadAllowed,
                String pdfUrl, String officialPageUrl, String authorUrl,
                String categoryUrl, String fileSize, String pages,
                String downloadCount) {
        this.id = id == null ? "" : id;
        this.title = title == null ? "Untitled" : title;
        this.author = author == null ? "" : author;
        this.coverUrl = coverUrl == null ? "" : coverUrl;
        this.description = description == null ? "" : description;
        this.priceType = priceType == null ? "unknown" : priceType;
        this.downloadAllowed = downloadAllowed;
        this.pdfUrl = pdfUrl == null ? "" : pdfUrl;
        this.officialPageUrl = officialPageUrl == null ? "" : officialPageUrl;
        this.authorUrl = authorUrl == null ? "" : authorUrl;
        this.categoryUrl = categoryUrl == null ? "" : categoryUrl;
        this.fileSize = fileSize == null ? "" : fileSize;
        this.pages = pages == null ? "" : pages;
        this.downloadCount = downloadCount == null ? "" : downloadCount;
    }

    public static Book fromJson(JSONObject object) {
        Object rawAllowed = object.opt("download_allowed");
        boolean allowed = false;
        if (rawAllowed instanceof Boolean) {
            allowed = (Boolean) rawAllowed;
        } else if (rawAllowed != null) {
            allowed = "true".equalsIgnoreCase(String.valueOf(rawAllowed));
        }

        return new Book(
                object.optString("id", ""),
                object.optString("title", "Untitled"),
                object.optString("author", ""),
                object.optString("cover_url", ""),
                object.optString("description", ""),
                object.optString("price_type", "unknown"),
                allowed,
                object.optString("pdf_url", ""),
                object.optString("official_page_url", ""),
                object.optString("author_url", ""),
                object.optString("category_url", ""),
                object.optString("file_size", ""),
                object.optString("pages", ""),
                object.optString("download_count", "")
        );
    }

    public boolean isDownloadable() {
        return "free".equalsIgnoreCase(priceType)
                && downloadAllowed
                && pdfUrl.startsWith("https://");
    }

    public String listLabel() {
        if ("free".equalsIgnoreCase(priceType) && downloadAllowed) {
            return title + "\n" + author + "\nFREE • OFFICIAL DOWNLOAD";
        }
        if ("paid".equalsIgnoreCase(priceType)) {
            return title + "\n" + author + "\nPAID • OPEN OFFICIAL PAGE";
        }
        return title + "\n" + author + "\nOPEN OFFICIAL PAGE";
    }
}
