# Sketchware FirebaseDB Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware Firebase DB component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.firebasedb` selector ကို အသုံးပြုထားသောကြောင့် FirebaseDB component ID, Activity ID, View ID, Firebase project path နှင့် app-specific key များကို fixed မလုပ်ထားပါ။

Firebase Realtime Database သည် JSON tree ပုံစံဖြင့် data သိမ်းပြီး Android client မှ reference/tag/path တစ်ခုကို အသုံးပြုကာ data ဖတ်၊ ရေး၊ update နှင့် delete လုပ်နိုင်သည်။ List data များအတွက် child listener သည် child တစ်ခုချင်းစီ ပြောင်းလဲမှုကို စီမံရန် သင့်တော်သည်။ [1] [2]

## Component ထည့်သွင်းခြင်းနှင့် Configuration

Sketchware Pro တွင် FirebaseDB component ကို Add လုပ်သောအခါ အနည်းဆုံး အောက်ပါအချက် ၂ ခုကို သတ်မှတ်ရသည်။

| Setting | အဓိပ္ပာယ် | ဥပမာ |
|---|---|---|
| Name | Project ထဲတွင် component ကို ခေါ်မည့် local name | `firebase_db` |
| Data Location | Firebase root အောက်ရှိ default path/tag | `users`, `posts`, `chat/rooms` |

`Name` သည် Sketchware project အတွင်း component variable အမည်ဖြစ်ပြီး `Data Location` သည် Firebase data tree အတွင်းရှိ location ဖြစ်သည်။ Universal blocks များသည် component dropdown အတွင်းမှ `%m.firebasedb` ကို လက်ခံသဖြင့် component name ကို JSON ထဲ hard-code မလုပ်ထားပါ။

> Data Location ကို user ID သို့မဟုတ် room ID အလိုက် ပြောင်းလဲမည်ဆိုပါက root location ကို တည်ငြိမ်စွာ သတ်မှတ်ပြီး child path ကို tag input အဖြစ် ချိတ်ပါ။ Secret key, service-account JSON သို့မဟုတ် admin credential ကို Android client ထဲ မထည့်ပါနှင့်။ Firebase Security Rules သည် access control ၏ အဓိကနေရာဖြစ်သည်။

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **50 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Get, set, push, delete, update နှင့် string/number/map write |
| Data | Child key, Map value, type conversion, key check |
| Query | Order by child/key, limit first/last, start/end range |
| Events | `onChildAdded`, `onChildChanged`, `onChildRemoved`, `onChildCancelled` |
| Safe | Valid tag, valid data, valid child key, permission/network error |
| Professional | Push key, child path, list synchronization, listener cleanup |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `firebasedb_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ FirebaseDB palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

## Basic Read နှင့် Write

```text
FirebaseDB_Set_Data
    firebasedb = firebase_db
    tag = "users/user_001/name"
    data = "Aung Aung"

FirebaseDB_Get_Data
    firebasedb = firebase_db
    tag = "users/user_001"
```

`setData` ပုံစံသည် သတ်မှတ်ထားသော path ၏ data ကို အစားထိုးနိုင်သောကြောင့် parent map တစ်ခုလုံးကို မလိုအပ်ဘဲ overwrite မဖြစ်စေရန် သတိထားပါ။ Child field အနည်းငယ်ကိုသာ ပြောင်းလိုပါက `Update Data` ကို သုံးပါ။ Firebase official guidance အရ `updateChildren` သည် အခြား sibling field များ မဖျက်ဘဲ သတ်မှတ်ထားသော child paths ကိုသာ update လုပ်ရန် သင့်တော်သည်။ [2]

```text
FirebaseDB_Update_Data
    firebasedb = firebase_db
    tag = "users/user_001"
    data = profile_update_map
```

Data type အနေဖြင့် String, Number, Boolean, Map နှင့် List များကို အသုံးပြုနိုင်သည်။ [2]

## Push Key နှင့် List Data

List node အတွင်း item အသစ်ထည့်ရာတွင် `Push Data` ကို သုံးပါ။ Firebase `push()` သည် unique child key တစ်ခု ထုတ်ပေးပြီး multi-user write conflict လျှော့ချပေးနိုင်သည်။ ထို key သည် timestamp-based ဖြစ်သောကြောင့် list item များကို အချိန်စဉ်အတိုင်း စီမံရာတွင် အသုံးဝင်သည်။ [1]

```text
FirebaseDB_Push_Data
    firebasedb = firebase_db
    tag = "posts"
    data = post_map
```

Key ကို မိမိ map ထဲတွင် သိမ်းရန် သို့မဟုတ် multi-location write လုပ်ရန် `FirebaseDB_Push_Key_From_Path` ကို သုံးပါ။ Array index ကို database key အဖြစ် မသုံးဘဲ push key သို့မဟုတ် stable business ID ကို သုံးခြင်းက concurrent client များအတွက် ပိုမိုလုံခြုံသည်။

## Delete နှင့် Child Path

Data ဖျက်ရန် `Delete Data` သို့မဟုတ် Professional child path block ကို သုံးပါ။

```text
FirebaseDB_Delete_Child_Path
    firebasedb = firebase_db
    parent_tag = "users/user_001"
    child_path = "profile/photoUrl"
```

ဖျက်မည့် path ကို user input မှ တိုက်ရိုက်ဆောက်ပါက `/`, `.`, `#`, `$`, `[`, `]` နှင့် empty segment များကို validation လုပ်ပါ။ `FirebaseDB_Child_Key_Is_Valid` နှင့် `FirebaseDB_Tag_Path_Is_Valid` ကို အရင်သုံးပါ။

## `onChildAdded` Event

`onChildAdded` သည် listener စတင်ချိန်တွင် ရှိပြီးသား child တစ်ခုချင်းစီအတွက် တစ်ကြိမ်ခေါ်ပြီး နောက်ပိုင်း child အသစ်ထည့်တိုင်း ထပ်ခေါ်သည်။ [1] [3]

```text
FirebaseDB → onChildAdded
    child_key = event child key
    child_map = event data map
    add child_map to local list
    notify RecyclerView/ListView adapter
```

Existing data load နှင့် new child insert နှစ်မျိုးလုံးကို တစ်နေရာတည်း စီမံနိုင်သော်လည်း UI list ထဲ duplicate မဖြစ်စေရန် child key ဖြင့် deduplicate လုပ်ပါ။ Previous child key ရှိပါက ordered list ထဲတွင် item ကို နေရာမှန်တွင် ထည့်နိုင်သည်။

## `onChildChanged` Event

`onChildChanged` သည် child data သို့မဟုတ် descendant တစ်ခုခု ပြောင်းသည့်အခါ ခေါ်သည်။ [1] [3]

```text
FirebaseDB → onChildChanged
    child_key = event child key
    updated_map = event data map
    find local row by child_key
    replace the row
    notify only the changed item when possible
```

Local list တွင် child key ကို hidden field အဖြစ် သိမ်းထားခြင်းက changed row ကို မြန်မြန်ရှာနိုင်ပြီး entire list rebuild လုပ်စရာ လျှော့ချပေးသည်။ `FirebaseDB_Update_By_Key` block သည် key field အဖြစ် သင်သတ်မှတ်ပေးသော field ကို အသုံးပြုသည်။

## `onChildRemoved` Event

`onChildRemoved` သည် child တစ်ခု ဖျက်သည့်အခါ ခေါ်ပြီး ဖျက်သွားသော child ၏ data snapshot ကို ပြန်ရနိုင်သည်။ [1] [3]

```text
FirebaseDB → onChildRemoved
    child_key = event child key
    remove local row by child_key
    notify adapter
```

ဖျက်ပြီးနောက် data မရနိုင်တော့သဖြင့် event ထဲက child key ကို local list row ID နှင့် တိုက်စစ်ရန် အရေးကြီးသည်။

## `onChildCancelled` Event

`onChildCancelled` သည် listener read operation သည် server error သို့မဟုတ် Firebase Security Rules ကြောင့် fail/cancel ဖြစ်သောအခါ ခေါ်သည်။ [3]

```text
FirebaseDB → onChildCancelled
    error_message = event error
    hide loading state
    show a safe error message
    do not clear existing cached data unnecessarily
```

Permission denied, unavailable database, network timeout နှင့် invalid query အခြေအနေများကို user-friendly message ဖြင့် ကိုင်တွယ်ပါ။ Firebase error text ကို user ထံ အပြည့်အစုံ ပြမည့်အစား အတွင်း log နှင့် user-facing summary ကို ခွဲထားပါ။

## Listener နှင့် Duplicate Callback ကာကွယ်ခြင်း

Child listener ကို screen တစ်ခုဖွင့်တိုင်း ထပ်မံ attach လုပ်ပြီး detach မလုပ်ပါက callback သည် တစ်ကြိမ်ထက်ပို၍ ခေါ်နိုင်သည်။ Firebase official guidance အရ listener ကို ထပ်မံ attach လုပ်ထားသလောက် အကြိမ်ရေတူတူ remove လုပ်ရပြီး parent listener ကို ဖယ်ခြင်းသည် child listener များကို အလိုအလျောက် မဖယ်ပေးနိုင်ပါ။ [1]

Screen destroy, logout, room change သို့မဟုတ် query change ဖြစ်သောအခါ listener cleanup workflow ထည့်ပါ။ `FirebaseDB_Listener_Detach` block ၏ generated method signature သည် Sketchware version အလိုက် listener reference လိုအပ်နိုင်သောကြောင့် သင့် version ၏ built-in remove-listener block/source နှင့် တိုက်စစ်ပါ။

## Query နှင့် Pagination

List data ကို child field သို့မဟုတ် key အလိုက် စီပြီး result အရေအတွက်ကို limit လုပ်နိုင်သည်။ Query တစ်ခုတွင် order-by method တစ်မျိုးသာ သုံးသင့်ပြီး limit/range filter များကို ထပ်မံချိတ်နိုင်သည်။ [1]

```text
FirebaseDB_Order_By_Child
    firebasedb = firebase_db
    tag = "posts"
    child_field = "createdAt"

FirebaseDB_Limit_To_Last
    firebasedb = firebase_db
    tag = "posts"
    count = 20
```

Pagination အတွက် stable ordering field, last key/time cursor နှင့် duplicate prevention ကို ထည့်သွင်းစဉ်းစားပါ။ `limitToFirst` သို့မဟုတ် `limitToLast` ကို များစွာကြီးသော value ဖြင့် မသုံးဘဲ UI လိုအပ်ချက်နှင့် ကိုက်ညီသော page size သတ်မှတ်ပါ။

## Local List Synchronization Pattern

```text
onChildAdded
    if child_key is not already in local list
        add row

onChildChanged
    replace row by child_key

onChildRemoved
    remove row by child_key

onChildCancelled
    show error state
```

`onChildAdded` သည် initial children အားလုံးအတွက် callback ပြန်လာနိုင်သောကြောင့် data list ကို တစ်ခါတည်း clear လုပ်ပြီး ပြန်ဖြည့်ခြင်း သို့မဟုတ် key-based merge strategy တစ်ခုကို သတ်မှတ်ပါ။ Data set ကြီးပါက callback တစ်ကြိမ်ချင်းစီတွင် UI အပြည့် refresh မလုပ်ဘဲ changed row ကိုသာ update လုပ်ပါ။

## Safe Data Workflow

```text
if FirebaseDB_Tag_Path_Is_Valid(tag)
    if data is valid
        FirebaseDB_Set_Data(firebase_db, tag, data)
    else
        show validation message
else
    show invalid-path message
```

User-provided path ကို တိုက်ရိုက်သုံးမည့်အစား allow-list root path အတွင်းတွင်သာ ခွင့်ပြုပါ။ Firebase Security Rules သည် client-side validation ကို အစားမထိုးနိုင်သောကြောင့် database rules တွင် authentication, ownership နှင့် field-level validation ထည့်ပါ။

## Professional Multi-Location Update

Related node များကို တစ်ပြိုင်နက် update လုပ်ရန် Map path များကို ပြင်ဆင်ပြီး Firebase update workflow ကို သုံးနိုင်သည်။ ဥပမာ post တစ်ခုကို `/posts/{id}` နှင့် `/user-posts/{uid}/{id}` နှစ်နေရာတွင် တစ်ပြိုင်နက် update လုပ်ခြင်းဖြစ်သည်။ [2]

```text
updates["/posts/post_id"] = post_map
updates["/user-posts/user_id/post_id"] = post_map
FirebaseDB_Update_Data(firebase_db, "", updates)
```

သင့် Sketchware FirebaseDB version သည် multi-location map update ကို တိုက်ရိုက် မထောက်ပံ့ပါက built-in update block သို့မဟုတ် supported child update workflow ကို အသုံးပြုပါ။ တစ်နေရာရေးပြီး တစ်နေရာမရေးနိုင်သည့် partial-write ပြဿနာ မဖြစ်စေရန် server-side data design ကို ကြိုတင်စဉ်းစားပါ။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.firebasedb` selector သုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, View ID, Firebase path, project name နှင့် key မသုံးထားပါ |
| Component setup ကိုလေးစားရမည် | Name နှင့် Data Location ကို guide တွင် သီးခြားရှင်းပြထားသည် |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$s` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Event-aware | Child events အားလုံးကို event-specific workflow အဖြစ် မှတ်တမ်းတင်ထားသည် |
| Secure by design | Credential hard-code မလုပ်ဘဲ Firebase Rules နှင့် validation ကို အလေးထားထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Component Name | Project တွင် unique local name ရှိသည် |
| Data Location | Default root/tag မှန်ကန်သည် |
| Set data | သတ်မှတ် path ၏ overwrite behavior ကို သိရှိထားသည် |
| Update data | Sibling data မပျက်ဘဲ selected child fields ပြောင်းသည် |
| Push data | Unique key ရပြီး duplicate မဖြစ်ပါ |
| `onChildAdded` | Existing/new children နှစ်မျိုးလုံးကို ကိုင်တွယ်သည် |
| `onChildChanged` | Key ဖြင့် row update ဖြစ်သည် |
| `onChildRemoved` | Key ဖြင့် row remove ဖြစ်သည် |
| `onChildCancelled` | Error state ပြပြီး app မ crash ပါ |
| Query | Order-by တစ်မျိုးသာ သုံးထားသည် |
| Listener | Screen/room change အချိန် duplicate callback မဖြစ်ပါ |
| Security | Firebase Rules က auth/ownership ကို စစ်သည် |
| Privacy | Token/password/private data ကို client/log ထဲ မဖော်ပြပါ |

## Compatibility Note

Sketchware Pro version အလိုက် FirebaseDB component ၏ generated method name၊ tag parameter အဓိပ္ပာယ်၊ child-event parameter order နှင့် query/update support ကွာနိုင်ပါသည်။ ဤ library သည် `getData`, `setData`, `pushData`, `deleteData`, `updateData`, `orderByChild`, `orderByKey`, `limitToFirst`, `limitToLast`, `startAt`, `endAt` နှင့် child-event data workflow များကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in FirebaseDB blocks နှင့် generated source code ကို တိုက်စစ်ပြီး unsupported query/mutation block ကို built-in equivalent ဖြင့် အစားထိုးပါ။

`onChildAdded`, `onChildChanged`, `onChildRemoved` နှင့် `onChildCancelled` event များကို Custom Block JSON က အလိုအလျောက် ဖန်တီးပေးခြင်းမဟုတ်ပါ။ FirebaseDB component ကို Add လုပ်ပြီးနောက် component ၏ သက်ဆိုင်ရာ event အတွင်းတွင် callback parameters ကို အသုံးပြုကာ local list, UI နှင့် error logic ကို သီးခြားရေးရမည်။

## References

[1] Firebase, “Work with Lists of Data on Android,” https://firebase.google.com/docs/database/android/lists-of-data  
[2] Firebase, “Read and Write Data on Android,” https://firebase.google.com/docs/database/android/read-and-write  
[3] Firebase, “ChildEventListener Reference,” https://firebase.google.com/docs/reference/android/com/google/firebase/database/ChildEventListener  
[4] Sketchware Pro, “Connecting to Firebase,” https://docs.sketchware.pro/docs/components/google/firebase/connect-to-firebase/
