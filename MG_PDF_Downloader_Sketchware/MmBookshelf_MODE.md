# MmBookshelf Official Download Form mode

ဒီ mode က Website ရဲ့ မူရင်း Download ခလုတ်လုပ်ဆောင်ပုံကိုသာ အသုံးပြုပါသည်။ App က direct PDF filename/URL ကို ခန့်မှန်းမထားပါ။

## Website flow

1. `GET https://mbookshelf.naingdroidapps.com/book/...`
2. Page ထဲက `main-form` ၏ `action`၊ hidden `_token` နှင့် `uid` ကိုဖတ်
3. Website သတ်မှတ်ထားသော `POST /download-book` သို့ form data ပို့
4. Server ပြန်ပေးသော HTTPS file URL ကို host စစ်
5. Android DownloadManager ဖြင့် user ရွေးထားသောစာအုပ်တစ်အုပ်ကို download

## Sketchware integration

1. `MBookshelfOfficialDownloader.java` ကို Java source ထည့်ပါ။
2. `Sketchware_MmBookshelf_Logic.java` ကို `Sketchware_MainActivity_Logic.java` ထဲက `mgDownloadOrOpen` အစား အသုံးပြုပါ။
3. `listview_books.setOnItemClickListener` ထဲတွင်—

```java
mgMbookshelfDownloadOrOpen(mgVisibleBooks.get(position));
```

4. `catalog.mbookshelf.example.json` ကို စမ်းသပ် catalog အဖြစ် သုံးပါ။
5. Catalog URL ကို ပြောင်းပြီး App ကို Build လုပ်ပါ။

## Free/Paid behavior

- Catalog record တွင် `price_type=free` နှင့် `download_allowed=true` ဖြစ်မှ official form request လုပ်မည်။
- Paid/မခွင့်ပြုထားသော record များကို download မလုပ်ဘဲ official page ကိုသာ ဖွင့်မည်။
- Official page တွင် form မရှိခြင်း၊ token/uid မတွေ့ခြင်း၊ server က URL မပြန်ခြင်းတို့ဖြစ်လျှင် App က download မလုပ်ဘဲ page ကို ဖွင့်ပေးမည်။

## Important

- ဒီ adapter သည် တစ်ကြိမ်လျှင် user ရွေးထားသော စာအုပ်တစ်အုပ်ကိုသာ resolve လုပ်သည်။ Batch/all-books download queue မပါဝင်ပါ။
- Website ပိုင်ရှင်၏ API/terms နှင့် စာအုပ်မူပိုင်ရှင်ခွင့်ပြုချက်များကို လိုက်နာပါ။
- Website HTML/endpoint ပြောင်းလဲလျှင် adapter ကို ပြန်ညှိရန် လိုနိုင်သည်။
