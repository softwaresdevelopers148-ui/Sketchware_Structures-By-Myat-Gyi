# MG PDF Downloader — Native UI Design

ဒီ Design သည် WebView မသုံးပါ။ Website ၏ HTML/CSS ကို မပြသဘဲ App ၏ Native Android Views ဖြင့် UI ကို ကိုယ်ပိုင်စီမံပါသည်။ မူရင်း Website link များကို data field အဖြစ် မပြောင်းလဲဘဲ သိမ်းထားပါသည်။

## Main screen layout

```text
Root LinearLayout (vertical, #F5F7FB)
├── Toolbar (height 60dp, #1976C8)
│   ├── ImageView: app logo (40dp)
│   ├── TextView: MG PDF Downloader (white, 20sp, bold)
│   └── ImageButton: menu/settings
├── EditText: edittext_search
│   └── hint = စာအုပ်အမည် သို့မဟုတ် စာရေးသူ ရှာရန်
├── HorizontalScrollView: category chips
├── TextView: section title = Free Books / စာအုပ်များ
├── ListView: listview_books
│   └── NativeBookAdapter ဖြင့် card item များပြ
└── Bottom navigation (optional)
    ├── Home
    ├── Downloads
    └── Settings
```

## Book card layout

```text
Rounded native card (#FFFFFF, corner 16dp)
├── ImageView: cover (88dp x 124dp)
└── Vertical LinearLayout
    ├── TextView: title (16sp, bold, max 2 lines)
    ├── TextView: author (13sp)
    ├── TextView: FREE • Official download / PAID • Official website
    └── Button: Download OR Open Website
```

`NativeBookAdapter.java` က အထက်ပါ card ကို programmatically ဖန်တီးပြီး cover image ကို HTTPS ဖြင့် asynchronous load လုပ်ပါသည်။ ထို့ကြောင့် XML/WebView/Glide dependency မလိုပါ။

## Optional Book detail screen

```text
Toolbar: Back + Book title
├── Cover image
├── Title / Author / File size / Pages
├── Button: Author page (author_url)
├── Button: Category page (category_url)
├── Button: Download / Open Official Website
└── Description
```

Author/Category buttons should use `Intent.ACTION_VIEW` with the original HTTPS URLs. They should not be rewritten to internal fake links.

## Suggested colors

- Primary blue: `#1976C8`
- Download button: `#2C7ECD`
- App background: `#F5F7FB`
- Main text: `#1B263B`
- Secondary text: `#5C677D`
- Card: `#FFFFFF`

## Sketchware setup

1. Main screen တွင် `EditText` ID `edittext_search`, `ListView` ID `listview_books`, `ProgressBar` ID `progressbar_catalog`, `TextView` ID `textview_empty` ထည့်ပါ။
2. `NativeBookAdapter.java` ကို Java source အဖြစ် ထည့်ပါ။
3. `Sketchware_Native_UI_Logic.java` ကို MainActivity ထဲ ထည့်ပြီး `onCreate` မှာ `setupMgNativeUi();` ခေါ်ပါ။
4. Download action အတွက် `Sketchware_MmBookshelf_Logic.java` ထဲက `mgMbookshelfDownloadOrOpen(...)` ကို အသုံးပြုပါ။
5. Website page URL၊ cover URL၊ author URL၊ category URL များကို JSON ထဲတွင် မူရင်းအတိုင်း ထားပါ။ UI သာ ပြောင်းလဲပြီး link များကို မဖျက်ပါ။

## Link preservation

- `official_page_url` — Official Website သို့ ဖွင့်ရန်
- `cover_url` — Cover ပြရန်
- `author_url` — Author page သို့ ဖွင့်ရန်
- `category_url` — Category page သို့ ဖွင့်ရန်
- PDF URL — Website official form က runtime တွင်ပြန်ပေးမှသာ DownloadManager သို့ ပို့ရန်

## User flow

```text
App Home
→ Native search/category UI
→ Book card ကိုနှိပ်
→ Free + download_allowed ဖြစ်လျှင် official Download form flow
→ Paid/မခွင့်ပြုလျှင် official page ကို ဖွင့်
```
