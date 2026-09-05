# Alignment with Sketchware Structures by Myat Gyi

Reference repository:
https://github.com/softwaresdevelopers148-ui/Sketchware_Structures-By-Myat-Gyi

ဒီ note က repository ထဲက Sketchware training/guide material နဲ့ MG PDF Downloader pack ရဲ့ ဆက်စပ်မှုကို မှတ်တမ်းတင်ထားခြင်းဖြစ်ပါသည်။ GitHub link ကို ထည့်ထားခြင်းက AI model ကို အမြဲတမ်း retrain လုပ်ခြင်းမဟုတ်ပါ။ ဒီ task အတွင်း repository ကို reference အဖြစ် ဖတ်ပြီး အသုံးပြုနိုင်ပါသည်။

## အသုံးပြုခဲ့သော guide အချက်များ

### 007 — Built-in Drag & Drop Widgets

Guide ထဲက Native widgets များ—Linear(H/V), Scroll, CardView, MaterialButton, ImageView, ProgressBar, ListView, RecyclerView—ကို UI design အတွက် reference လုပ်ထားပါသည်။

ဒီ pack ၏ default Native UI သည် external dependency မလိုအောင် `ListView + NativeBookAdapter` ကို သုံးထားပါသည်။ Sketchware Pro တွင် CardView/RecyclerView library အဆင်ပြေပါက နောက်ပိုင်းတွင် RecyclerView card adapter သို့ ပြောင်းနိုင်ပါသည်။

### 008 — Sketchware Block Guide

Custom block ထည့်လိုပါက Guide ၏—

- block `type`
- color
- `spec` parameter placeholders
- `imports`
- embedded `code`

ပုံစံကို လိုက်နာရပါမည်။ MG app ၏ network/download code ကို MainActivity ထဲ တိုက်ရိုက်ပုံသွင်းမည့်အစား Java helper classes အဖြစ် ခွဲထားပြီး Sketchware code မှ method call လုပ်စေထားပါသည်။

### 009 — Sketchware Libraries

Guide က AndroidX/dependency များကို တစ်ခုချင်းထည့်ပြီး build စမ်းရန် သတိပေးထားပါသည်။ ဒီ pack ၏ base version သည် external library မလိုအောင်—

- `HttpURLConnection` — JSON/official form request
- `DownloadManager` — PDF download
- `SQLiteOpenHelper` — history
- `BaseAdapter` — native card list
- `BitmapFactory` — cover image

ကို သုံးထားပါသည်။ ထို့ကြောင့် library conflict လျော့နည်းစေပါသည်။

Optional UI upgrade အတွက် Guide ထဲက Material Components, RecyclerView, Glide ကို တစ်ခုချင်းစီသာ ထည့်ပါ။ Glide သုံးလျှင် `NativeBookAdapter` ၏ built-in image loader ကို Glide code ဖြင့် အစားထိုးပါ။

## MG PDF Downloader mapping

| Requirement | Sketchware/native implementation |
|---|---|
| Custom app UI | `NATIVE_UI_DESIGN.md`, `NativeBookAdapter.java` |
| Search | `Sketchware_Native_UI_Logic.java` |
| Cover image | `cover_url` + native HTTPS image loader |
| Original author/category links | `author_url`, `category_url` fields |
| Official page | `official_page_url` + `Intent.ACTION_VIEW` |
| Official download | `MBookshelfOfficialDownloader.java` |
| Progress | `PdfDownloadHelper.monitor(...)` |
| History | `DownloadHistoryDb.java` |
| Paid/free gate | `price_type` + `download_allowed` |

## Sketchware project rule

- WebView မသုံးပါ။
- Website HTML/CSS ကို UI အဖြစ် မကူးပါ။
- Original URLs များကို data အဖြစ် ထိန်းထားပြီး Native UI ကနေ အသုံးပြုပါ။
- Website official Download form ပြောင်းလဲလျှင် `MBookshelfOfficialDownloader.java` ကိုသာ ပြန်ညှိပါ။
- External library ထည့်မည်ဆိုလျှင် တစ်ခုချင်းထည့်၊ build စမ်း၊ ထို့နောက်နောက်တစ်ခုထည့်ပါ။
