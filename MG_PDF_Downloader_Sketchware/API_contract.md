# Authorized catalog API contract

The app expects either a top-level JSON object with a `books` array or a plain JSON array.

Required fields:

- `id`: string, unique book ID
- `title`: string
- `author`: string
- `cover_url`: string or null
- `description`: string or null
- `price_type`: `free` or `paid`
- `download_allowed`: boolean
- `pdf_url`: HTTPS URL only when download is authorized; otherwise null
- `official_page_url`: HTTPS page URL

Optional fields retained by the native UI:

- `author_url`: official author page
- `category_url`: official category page
- `file_size`: display metadata such as `16.41 MB`
- `pages`: page count
- `download_count`: official displayed download count

The app keeps these links as data; changing the UI does not rewrite or remove them.


```json
{
  "id": "b-002",
  "title": "Paid book",
  "author": "Author",
  "price_type": "paid",
  "download_allowed": false,
  "pdf_url": null,
  "official_page_url": "https://authorized.example/books/b-002"
}
```

The client deliberately checks both `price_type` and `download_allowed`. Do not infer permission from title, category, file size, or whether a URL happens to be publicly reachable. The API owner should set `download_allowed` server-side.
