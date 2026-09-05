package com.mg.pdfdownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Looper;
import android.util.LruCache;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Native custom card adapter; no WebView and no external image library. */
public final class NativeBookAdapter extends BaseAdapter {
    public interface Listener {
        void onDownload(Book book);
        void onOpenOfficialPage(Book book);
    }

    private final android.content.Context context;
    private final List<Book> books;
    private final Listener listener;
    private final Handler main = new Handler(Looper.getMainLooper());
    private static final ExecutorService IMAGE_EXECUTOR = Executors.newFixedThreadPool(3);
    private static final LruCache<String, Bitmap> IMAGE_CACHE = new LruCache<String, Bitmap>(8 * 1024) {
        @Override protected int sizeOf(String key, Bitmap value) {
            return value.getByteCount() / 1024;
        }
    };

    public NativeBookAdapter(android.content.Context context, List<Book> books, Listener listener) {
        this.context = context;
        this.books = books;
        this.listener = listener;
    }

    @Override public int getCount() { return books.size(); }
    @Override public Object getItem(int position) { return books.get(position); }
    @Override public long getItemId(int position) { return position; }

    @Override public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = buildHolder();
            convertView = holder.root;
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        Book book = books.get(position);
        holder.cover.setTag(book.coverUrl);
        holder.cover.setImageResource(android.R.drawable.ic_menu_gallery);
        holder.title.setText(book.title);
        holder.author.setText(book.author.length() == 0 ? "Unknown author" : book.author);
        holder.meta.setText(metaText(book));

        boolean canUseOfficialDownload = "free".equalsIgnoreCase(book.priceType)
                && book.downloadAllowed;
        holder.action.setText(canUseOfficialDownload ? "Download" : "Open Website");
        holder.action.setOnClickListener(v -> {
            if (canUseOfficialDownload) listener.onDownload(book);
            else listener.onOpenOfficialPage(book);
        });
        loadCover(book.coverUrl, holder.cover);
        return convertView;
    }

    private Holder buildHolder() {
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.HORIZONTAL);
        root.setGravity(Gravity.CENTER_VERTICAL);
        root.setPadding(dp(12), dp(10), dp(12), dp(10));
        root.setBackground(roundRect(Color.WHITE, dp(16)));

        ImageView cover = new ImageView(context);
        cover.setScaleType(ImageView.ScaleType.CENTER_CROP);
        root.addView(cover, new LinearLayout.LayoutParams(dp(88), dp(124)));

        LinearLayout details = new LinearLayout(context);
        details.setOrientation(LinearLayout.VERTICAL);
        details.setPadding(dp(12), 0, 0, 0);
        LinearLayout.LayoutParams detailParams = new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        root.addView(details, detailParams);

        TextView title = new TextView(context);
        title.setTextColor(Color.rgb(27, 38, 59));
        title.setTextSize(16);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setMaxLines(2);
        details.addView(title, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView author = new TextView(context);
        author.setTextColor(Color.rgb(92, 103, 125));
        author.setTextSize(13);
        LinearLayout.LayoutParams authorParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        authorParams.topMargin = dp(5);
        details.addView(author, authorParams);

        TextView meta = new TextView(context);
        meta.setTextColor(Color.rgb(117, 128, 150));
        meta.setTextSize(12);
        LinearLayout.LayoutParams metaParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        metaParams.topMargin = dp(4);
        details.addView(meta, metaParams);

        Button action = new Button(context);
        action.setAllCaps(false);
        action.setTextColor(Color.WHITE);
        action.setTextSize(12);
        action.setPadding(dp(8), 0, dp(8), 0);
        action.setBackground(roundRect(Color.rgb(44, 126, 205), dp(10)));
        LinearLayout.LayoutParams actionParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, dp(40));
        actionParams.topMargin = dp(10);
        details.addView(action, actionParams);

        LinearLayout.LayoutParams rootParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rootParams.setMargins(dp(10), dp(6), dp(10), dp(6));
        root.setLayoutParams(rootParams);
        return new Holder(root, cover, title, author, meta, action);
    }

    private String metaText(Book book) {
        StringBuilder info = new StringBuilder();
        if (book.fileSize.length() > 0) info.append(book.fileSize);
        if (book.pages.length() > 0) {
            if (info.length() > 0) info.append("  •  ");
            info.append(book.pages).append(" pages");
        }
        if ("free".equalsIgnoreCase(book.priceType) && book.downloadAllowed) {
            if (info.length() > 0) info.append("  •  ");
            info.append("FREE  •  Official download");
            return info.toString();
        }
        if ("paid".equalsIgnoreCase(book.priceType)) {
            if (info.length() > 0) info.append("  •  ");
            info.append("PAID  •  Official website");
            return info.toString();
        }
        if (info.length() > 0) info.append("  •  ");
        info.append("Official website");
        return info.toString();
    }

    private void loadCover(String url, ImageView target) {
        if (url == null || !url.startsWith("https://")) return;
        Bitmap cached = IMAGE_CACHE.get(url);
        if (cached != null) {
            target.setImageBitmap(cached);
            return;
        }
        IMAGE_EXECUTOR.execute(() -> {
            Bitmap bitmap = null;
            HttpURLConnection connection = null;
            InputStream input = null;
            try {
                connection = (HttpURLConnection) new URL(url).openConnection();
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(15000);
                connection.setRequestProperty("User-Agent", "MG-PDF-Downloader/1.0");
                input = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(input);
                if (bitmap != null) IMAGE_CACHE.put(url, bitmap);
            } catch (Exception ignored) {
            } finally {
                if (input != null) try { input.close(); } catch (Exception ignored) { }
                if (connection != null) connection.disconnect();
            }
            Bitmap result = bitmap;
            if (result != null) {
                main.post(() -> {
                    if (url.equals(String.valueOf(target.getTag()))) target.setImageBitmap(result);
                });
            }
        });
    }

    private GradientDrawable roundRect(int color, int radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(color);
        drawable.setCornerRadius(radius);
        return drawable;
    }

    private int dp(int value) {
        return Math.round(value * context.getResources().getDisplayMetrics().density);
    }

    private static final class Holder {
        final LinearLayout root;
        final ImageView cover;
        final TextView title;
        final TextView author;
        final TextView meta;
        final Button action;

        Holder(LinearLayout root, ImageView cover, TextView title,
               TextView author, TextView meta, Button action) {
            this.root = root;
            this.cover = cover;
            this.title = title;
            this.author = author;
            this.meta = meta;
            this.action = action;
        }
    }
}
