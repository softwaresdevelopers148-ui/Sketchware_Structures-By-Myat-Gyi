# Sketchware Pro Android Libraries

> **မြန်မာလို လေ့လာရန်နှင့် Copy ယူအသုံးပြုရန် စုစည်းထားသောစာရင်း**
>
> Version များသည် 2026-08-23 အခြေအနေအရ စစ်ဆေးထားသော stable/recommended versions ဖြစ်သည်။
> Sketchware Pro version၊ Android Gradle Plugin နှင့် `minSdk` အလိုက် compatibility ကွာနိုင်ပါသည်။

## အရေးကြီးသော သတိပြုရန်

- Project သည် **AndroidX** ဖြစ်ကြောင်း အရင်စစ်ပါ။
- Library အများကြီးကို တစ်ပြိုင်နက် မထည့်ပါနှင့်။ လိုအပ်သည့် library ကိုသာ ထည့်ပါ။
- Glide နှင့် Picasso ထဲမှ image loading အတွက် တစ်ခုသာရွေးပါ။
- Gson နှင့် Fastjson2 ထဲမှ JSON parser တစ်ခုသာရွေးပါ။
- Sketchware Local Library Manager သည် Maven dependency အားလုံးကို Gradle ကဲ့သို့ မဖြေရှင်းနိုင်ပါ။ မရပါက `.aar`/`.jar` နှင့် လိုအပ်သော dependency များကိုပါ ထည့်ရနိုင်ပါသည်။
- Firebase တွင် BOM မအလုပ်လုပ်ပါက Firebase libraries များအားလုံးကို compatible version တစ်မျိုးတည်းဖြင့် ထည့်ပါ။
- `service_role` key ကို Android app ထဲ မထည့်ပါနှင့်။ Supabase တွင် `anon` key + Row Level Security ကိုသုံးပါ။

## အမြန်ဆုံး စတင်ရန် — Recommended Stack

```gradle
implementation 'com.google.android.material:material:1.14.0'
implementation 'com.airbnb.android:lottie:6.7.1'

implementation 'com.squareup.okhttp3:okhttp:5.5.0'
implementation 'com.squareup.retrofit2:retrofit:3.0.0'
implementation 'com.squareup.retrofit2:converter-gson:3.0.0'
implementation 'com.google.code.gson:gson:2.14.0'

implementation 'com.github.bumptech.glide:glide:5.0.3'
```

## 1. Custom UI နှင့် Animations

| Library | Version | Maven Dependency | အသုံးပြုရန် |
|---|---:|---|---|
| ObjectAnimator | Android SDK built-in | မလိုပါ | alpha, rotation, scale, translation animation |
| Layout Animation | Android SDK built-in | မလိုပါ | ViewGroup/List ထဲက view များ ဝင်လာ animation |
| Material Components | 1.14.0 | `implementation 'com.google.android.material:material:1.14.0'` | Button, TextInput, Snackbar, BottomSheet, Chip, FAB |
| Lottie Android | 6.7.1 | `implementation 'com.airbnb.android:lottie:6.7.1'` | After Effects JSON animation |
| ConstraintLayout | 2.2.1 | `implementation 'androidx.constraintlayout:constraintlayout:2.2.1'` | ရှုပ်ထွေးသော responsive layout |
| RecyclerView | 1.4.0 | `implementation 'androidx.recyclerview:recyclerview:1.4.0'` | List/Grid နှင့် custom adapter |
| AndroidX Core | 1.17.0 | `implementation 'androidx.core:core:1.17.0'` | AndroidX compatibility APIs |
| Custom Toast | Built-in | မလိုပါ | Custom layout Toast |
| Custom Dialog | Built-in/Material | မလိုပါ | Custom XML Dialog, AlertDialog, BottomSheet |

### Official Links

- Material Components — https://github.com/material-components/material-components-android
- Lottie Android — https://github.com/airbnb/lottie-android
- ConstraintLayout — https://github.com/androidx/constraintlayout
- AndroidX RecyclerView — https://developer.android.com/jetpack/androidx/releases/recyclerview

## 2. Database Engines

### Firebase

Firebase libraries များကို BOM ဖြင့် version alignment လုပ်ရန် အကြံပြုပါသည်။

```gradle
implementation platform('com.google.firebase:firebase-bom:34.17.0')
implementation 'com.google.firebase:firebase-database'
implementation 'com.google.firebase:firebase-storage'
```

| Library | Version | Maven Dependency | အသုံးပြုရန် |
|---|---:|---|---|
| Firebase BoM | 34.17.0 | `implementation platform('com.google.firebase:firebase-bom:34.17.0')` | Firebase libraries version စီမံရန် |
| Realtime Database | BoM-managed | `implementation 'com.google.firebase:firebase-database'` | Realtime JSON database |
| Firebase Storage | BoM-managed | `implementation 'com.google.firebase:firebase-storage'` | Image/File upload နှင့် download |
| Firebase Auth | BoM-managed | `implementation 'com.google.firebase:firebase-auth'` | Email, Phone, Google login |
| Firestore | BoM-managed | `implementation 'com.google.firebase:firebase-firestore'` | Document database |

Official links:

- Firebase Android SDK — https://github.com/firebase/firebase-android-sdk
- Firebase Database — https://firebase.google.com/docs/database/android/start
- Firebase Storage — https://firebase.google.com/docs/storage/android/start
- Firebase Release Notes — https://firebase.google.com/support/release-notes/android

### Supabase

Sketchware ၏ Java project အတွက် Supabase REST API ကို OkHttp ဖြင့် ခေါ်ခြင်းသည် Kotlin SDK ထက် ပိုလွယ်ပြီး compatibility ပိုကောင်းပါသည်။

```gradle
implementation 'com.squareup.okhttp3:okhttp:5.5.0'
```

```java
Request request = new Request.Builder()
    .url(SUPABASE_URL + "/rest/v1/your_table")
    .addHeader("apikey", SUPABASE_ANON_KEY)
    .addHeader("Authorization", "Bearer " + SUPABASE_ANON_KEY)
    .addHeader("Content-Type", "application/json")
    .build();
```

| Library | Version/Status | Maven Dependency | အသုံးပြုရန် |
|---|---:|---|---|
| Supabase REST + OkHttp | OkHttp 5.5.0 | `implementation 'com.squareup.okhttp3:okhttp:5.5.0'` | Database, Auth, Storage REST API |
| supabase-kt | 2.6.0-rc-1 | `implementation platform('io.github.jan-tennert.supabase:bom:2.6.0-rc-1')` | Kotlin Multiplatform project; Java Sketchware အတွက် မရွေးချယ်သင့် |
| PostgREST Kotlin module | BOM-managed | `implementation 'io.github.jan-tennert.supabase:postgrest-kt'` | Kotlin project မှ database query |

Official links:

- Supabase Kotlin — https://github.com/supabase-community/supabase-kt
- Supabase install docs — https://supabase.com/docs/reference/kotlin/installing
- Supabase REST API — https://supabase.com/docs/reference

### Local Database

| Library | Version | Maven Dependency | အသုံးပြုရန် |
|---|---:|---|---|
| SQLiteOpenHelper | Android SDK built-in | မလိုပါ | Small/local SQLite database |
| Room Runtime | 2.7.2 | `implementation 'androidx.room:room-runtime:2.7.2'` | SQLite အပေါ် type-safe DAO |
| Room Compiler | 2.7.2 | `annotationProcessor 'androidx.room:room-compiler:2.7.2'` | Room code generation |
| Room 3 | 3.0.1 | `implementation 'androidx.room3:room3-runtime:3.0.1'` | Modern Gradle/Kotlin setup အတွက်; legacy Sketchware တွင် မသင့်နိုင် |
| Realm Java | Deprecated | မသုံးရန် | New production project တွင် မရွေးချယ်ပါနှင့် |

Room 2.7.2 သည် legacy Sketchware setup အတွက် Room 3 ထက် compatibility ပိုကောင်းနိုင်ပါသည်။ `room-ktx` ကို version အသစ်များတွင် သီးခြားထည့်ရန် မလိုပါ။

Official links:

- Room — https://developer.android.com/jetpack/androidx/releases/room
- Room guide — https://developer.android.com/training/data-storage/room
- SQLiteOpenHelper — https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper

## 3. Multi-language နှင့် Executors

### Python

| Library | Version/Status | Maven/Gradle | အသုံးပြုရန် |
|---|---:|---|---|
| Chaquopy | 17.0.0 plugin line | `plugins { id 'com.chaquo.python' version '17.0.0' }` | Python interpreter ကို Android app ထဲ embed လုပ်ရန် |
| PyBridge | Stable standard artifact မရှိ | — | Random JAR များအစား Chaquopy ကိုရွေးပါ |

> Chaquopy သည် သာမန် `.aar/.jar` မဟုတ်ဘဲ Gradle plugin + Python runtime ဖြစ်သည်။ Sketchware Local Library Manager ထဲသို့ library တစ်ခုအဖြစ် ထည့်ရုံဖြင့် မရနိုင်ပါ။

Official links:

- Chaquopy — https://chaquo.com/chaquopy/doc/current/
- Chaquopy GitHub — https://github.com/chaquo/chaquopy

### JavaScript နှင့် Code Highlighting

| Library | Version/Status | Maven Dependency | အသုံးပြုရန် |
|---|---:|---|---|
| Android WebView JavaScript | Built-in | မလိုပါ | HTML/JS preview နှင့် simple JS execution |
| Rhino | 1.7.15 | `implementation 'org.mozilla:rhino:1.7.15'` | JavaScript ကို JVM/Android တွင် execute လုပ်ရန် |
| CodeView | 1.3.2 | `implementation 'io.github.kbiakov:codeview:1.3.2'` | Source code display နှင့် syntax highlighting |
| Highlight.js | Web asset | Maven မလို | WebView အတွင်း syntax highlighting |

Untrusted JavaScript ကို WebView/Rhino တွင် မစစ်ဆေးဘဲ execute မလုပ်ပါနှင့်။ Code display သာလိုပါက Highlight.js + WebView သည် ပိုလုံခြုံပါသည်။

## 4. Utilities နှင့် Networking

| Library | Version | Maven Dependency | အသုံးပြုရန် |
|---|---:|---|---|
| OkHttp | 5.5.0 | `implementation 'com.squareup.okhttp3:okhttp:5.5.0'` | HTTP/HTTPS, REST API, upload/download |
| Retrofit | 3.0.0 | `implementation 'com.squareup.retrofit2:retrofit:3.0.0'` | Typed REST API client |
| Retrofit Gson Converter | 3.0.0 | `implementation 'com.squareup.retrofit2:converter-gson:3.0.0'` | Retrofit JSON conversion |
| Glide | 5.0.3 | `implementation 'com.github.bumptech.glide:glide:5.0.3'` | Image loading, cache, resize, GIF |
| Glide Compiler | 5.0.3 | `annotationProcessor 'com.github.bumptech.glide:compiler:5.0.3'` | Custom Glide module/code generation |
| Picasso | 2.8 | `implementation 'com.squareup.picasso:picasso:2.8'` | ရိုးရှင်းသော image loading |
| Gson | 2.14.0 | `implementation 'com.google.code.gson:gson:2.14.0'` | Java Object ↔ JSON |
| Fastjson2 | 2.0.60 | `implementation 'com.alibaba.fastjson2:fastjson2:2.0.60'` | Alternative JSON parser |
| Lifecycle Runtime | 2.9.2 | `implementation 'androidx.lifecycle:lifecycle-runtime:2.9.2'` | Lifecycle-aware async work |

Official links:

- OkHttp — https://github.com/square/okhttp
- Retrofit — https://github.com/square/retrofit
- Glide — https://github.com/bumptech/glide
- Picasso — https://github.com/square/picasso
- Gson — https://github.com/google/gson
- Fastjson2 — https://github.com/alibaba/fastjson2

## Legacy Sketchware Compatibility Fallback

အထက်ပါ version အသစ်များသည် Sketchware ၏ဟောင်းသော Gradle/Java setup နှင့် မကိုက်ပါက အောက်ပါ fallback ကို စဉ်းစားနိုင်ပါသည်—

```gradle
implementation 'com.squareup.okhttp3:okhttp:4.12.0'
implementation 'com.squareup.retrofit2:retrofit:2.11.0'
implementation 'com.squareup.retrofit2:converter-gson:2.11.0'
implementation 'com.github.bumptech.glide:glide:4.16.0'
implementation 'com.google.code.gson:gson:2.10.1'
```

Fallback သုံးရမည်ဆိုလျှင် library တစ်ခုချင်းစီ၏ AndroidX/Java compatibility ကို build log ဖြင့်စစ်ပါ။

## Dependency ထည့်သွင်းပြီးနောက် စစ်ဆေးရန်

1. Project ကို backup/export လုပ်ပါ။
2. Library တစ်ခုချင်းစီကို သီးခြားထည့်ပြီး build လုပ်ပါ။
3. `Duplicate class`, `Manifest merger failed`, `minSdk`, `desugar` error များကို စစ်ပါ။
4. AndroidX နှင့် legacy `android.support.*` ကို ရောမသုံးပါနှင့်။
5. Network request များကို main/UI thread တွင် မလုပ်ပါနှင့်။
6. API key, Firebase config နှင့် Supabase key များကို public GitHub repository ထဲ မတင်ပါနှင့်။