# MG PDF Downloader — Sketchware Pro Template

ဒီ pack ၏ UI သည် **Native Android Views** ဖြစ်ပါသည်။ WebView မသုံးပါ။ App တွင် Toolbar၊ Search၊ Book Card၊ Cover၊ Download/Open Website button များကို ကိုယ်ပိုင် design ဖြင့် ပြသပါသည်။ Website မှာရှိသော `official_page_url`, `cover_url`, author/category links များကို မူရင်းအတိုင်း data field အဖြစ် ထိန်းထားပြီး UI သာ ပြောင်းထားပါသည်။

> အရေးကြီး: `free` ဟု ပြထားခြင်းတစ်ခုတည်းသည် download/ပြန်လည်ဖြန့်ဝေခွင့်ကို မသက်သေပြပါ။ Website/စာအုပ်ပိုင်ရှင်က ခွင့်ပြုထားသော `pdf_url` နှင့် API ကိုသာ ထည့်သုံးပါ။ ဒီ pack ထဲတွင် mbookshelf ၏ direct PDF URL ကို active default အဖြစ် မထည့်ထားပါ။

## ပါဝင်သောဖိုင်များ

- `src/com/mg/pdfdownloader/Book.java` — catalog book model
- `src/com/mg/pdfdownloader/CatalogLoader.java` — HTTPS JSON loader
- `src/com/mg/pdfdownloader/PdfDownloadHelper.java` — authorized PDF download, progress, open
- `src/com/mg/pdfdownloader/DownloadHistoryDb.java` — SQLite download history
- `src/com/mg/pdfdownloader/MBookshelfOfficialDownloader.java` — MmBookshelf ၏ official Download form flow
- `src/com/mg/pdfdownloader/NativeBookAdapter.java` — WebView မသုံးသော native book-card UI
- `Sketchware_MainActivity_Logic.java` — generic authorized JSON catalog mode
- `Sketchware_MmBookshelf_Logic.java` — MmBookshelf official form mode အတွက် click/download logic
- `Sketchware_Native_UI_Logic.java` — custom native UI setup/filter logic
- `NATIVE_UI_DESIGN.md` — App UI layout tree၊ colors နှင့် View IDs
- `native_ui_colors.txt` — UI color palette
- `MmBookshelf_MODE.md` — Website-specific mode အသုံးပြုနည်း
- `GUIDE_ALIGNMENT.md` — GitHub Sketchware Structures guide နှင့် pack mapping
- `AndroidManifest_additions.xml` — လိုအပ်သော permission
- `catalog.example.json` — generic authorized API အတွက် example
- `catalog.mbookshelf.example.json` — MmBookshelf official page record example
- `API_contract.md` — API response သတ်မှတ်ချက်

## Sketchware Pro ဖြင့် ထည့်သွင်းနည်း

1. Sketchware Pro တွင် project အသစ်ဖန်တီးပါ။
   - App name: `MG PDF Downloader`
   - Package: `com.mg.pdfdownloader`
   - Minimum SDK: 29 (Android 10)
2. Main screen တွင် အောက်ပါ view များထည့်ပါ။ ID များကို ဒီအတိုင်းထားလျှင် snippet ကို တိုက်ရိုက်ညှိရလွယ်ပါသည်။
   - `EditText` — `edittext_search`
   - `ListView` — `listview_books`
   - `ProgressBar` — `progressbar_catalog`
   - `TextView` — `textview_empty`
3. `src/com/mg/pdfdownloader/` အောက်က Java files များကို Sketchware Pro ၏ **Add source directly / Java source** နည်းဖြင့် ထည့်ပါ။ Package name မတူပါက file တိုင်း၏ `package` line ကို project package နှင့်ညှိပါ။
4. `AndroidManifest_additions.xml` ထဲက permission ကို Manifest တွင် ထည့်ပါ။
5. Generic JSON mode သုံးမည်ဆိုလျှင် `Sketchware_MainActivity_Logic.java` ကို MainActivity ထဲထည့်ပါ။ **Native UI mode သုံးမည်ဆိုလျှင်** `NativeBookAdapter.java` နှင့် `Sketchware_Native_UI_Logic.java` ကို ထည့်ပြီး `onCreate` မှာ `setupMgNativeUi();` ခေါ်ပါ။ UI hierarchy နှင့် colors ကို `NATIVE_UI_DESIGN.md` တွင်ကြည့်ပါ။
6. Download action အတွက် `MBookshelfOfficialDownloader.java` နှင့် `Sketchware_MmBookshelf_Logic.java` ကို ထည့်ပါ။ Native UI listener က `mgMbookshelfDownloadOrOpen(...)` ကို ခေါ်ပေးပါသည်။
7. `CATALOG_URL` ကို ကိုယ်ပိုင် **ခွင့်ပြုထားသော HTTPS JSON endpoint** ဖြင့် အစားထိုးပါ။ MmBookshelf mode အတွက် `catalog.mbookshelf.example.json` ကို စမ်းသပ် catalog အဖြစ် သုံးနိုင်ပါသည်။
8. Build → Test လုပ်ပါ။ Android 10+ တွင် PDF ကို app-specific Downloads folder ထဲသိမ်းပြီး DownloadManager URI ဖြင့် ဖွင့်ပါမည်။

## App behavior

- `price_type = free` AND `download_allowed = true` → Download action
- `price_type = paid` → Download မလုပ်၊ `official_page_url` ကို ဖွင့်
- Free ဖြစ်သော်လည်း `download_allowed = false` → Official page ကို ဖွင့်
- Generic mode တွင် `pdf_url` မဟုတ်သော HTTP/မမှန်သော URL → Download မလုပ်
- Download progress ကို App UI နှင့် Android notification တွင် ပြ
- Download ပြီးလျှင် PDF reader ဖြင့် ဖွင့်နိုင်
- Download history ကို SQLite တွင် သိမ်း

## MmBookshelf Official Download Form mode

`MBookshelfOfficialDownloader.java` သည် Website ၏ မူရင်း Download ခလုတ်အတိုင်း—book page ကို GET လုပ်၊ hidden `_token`/`uid` ဖတ်၊ official `/download-book` form ကို POST လုပ်ပြီး server ပြန်ပေးသော HTTPS file URL ကိုသာ DownloadManager သို့ ပို့ပါသည်။

- Direct PDF URL ကို app ထဲ hard-code မလုပ်ပါ။
- Official page host နှင့် official file host ကို စစ်ပြီးမှ ဆက်လုပ်ပါသည်။
- တစ်ကြိမ်လျှင် user ရွေးထားသော စာအုပ်တစ်အုပ်သာ resolve လုပ်ပါသည်။
- Form/token မတွေ့ပါက သို့မဟုတ် server က URL မပြန်ပါက official page ကို ဖွင့်ပေးပါသည်။
- `MmBookshelf_MODE.md` ထဲတွင် integration အဆင့်များရှိပါသည်။

## API မရှိလျှင်

Website HTML ကို scrape လုပ်ပြီး link များကို အလိုအလျောက်ကူးယူမည့်အစား Website/စာအုပ်ပိုင်ရှင်ထံမှ catalog API/JSON တောင်းပါ။ API မပေးနိုင်ပါက ဒီ App ကို official website viewer/link launcher အဖြစ်သာ အသုံးပြုပါ။

## Security checklist

- API နှင့် PDF URL များသည် HTTPS ဖြစ်ရမည်။
- Paid book အတွက် `download_allowed` ကို server-side `false` ပြန်ပေးပါ။
- Private token/password များကို APK ထဲ hard-code မလုပ်ပါနှင့်။
- Direct link ပြောင်းလဲနိုင်သော signed URL များရှိပါက Website/API က runtime တွင် ပြန်ပေးပါ။
- Website HTML/endpoint ပြောင်းလဲပါက official adapter ကို ပြန်ညှိရန် လိုနိုင်ပါသည်။
- Bulk download မလုပ်ပါနှင့်။ Owner က တိတိကျကျ ခွင့်ပြုထားသော use case များတွင်သာ rate limit နှင့် owner terms ကို လိုက်နာပါ။
