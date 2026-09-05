# Sketchware Firebase Storage Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware Firebase Storage component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.firebasestorage` selector ကို အသုံးပြုထားသောကြောင့် Firebase Storage component ID, Activity ID, View ID, bucket path, local file path နှင့် user-specific file name များကို fixed မလုပ်ထားပါ။

Firebase Cloud Storage သည် file များကို bucket အတွင်း path တစ်ခုဖြင့် သိမ်းပြီး upload, download, metadata နှင့် delete လုပ်ဆောင်ချက်များကို စီမံနိုင်သည်။ Upload/download task များမှ progress, success နှင့် failure state များကို နားထောင်နိုင်သည်။ [1] [2] [3]

## Component ထည့်သွင်းခြင်းနှင့် Configuration

Sketchware Pro တွင် Firebase Storage component ကို Add လုပ်သောအခါ အနည်းဆုံး အောက်ပါအချက် ၂ ခုကို သတ်မှတ်ရသည်။

| Setting | အဓိပ္ပာယ် | ဥပမာ |
|---|---|---|
| Name | Project ထဲတွင် component ကို ခေါ်မည့် local name | `firebase_storage` |
| Data Location | Storage bucket အောက်ရှိ default folder/path | `uploads`, `users/profile` |

`Name` သည် Sketchware project အတွင်း component variable အမည်ဖြစ်ပြီး `Data Location` သည် Cloud Storage object path အတွက် default location ဖြစ်သည်။ Universal blocks များသည် `%m.firebasestorage` dropdown မှ component ကို ရွေးနိုင်သဖြင့် component name နှင့် path ကို JSON ထဲ hard-code မလုပ်ထားပါ။

> Upload လုပ်မည့် Storage path သည် bucket root `/` တစ်ခုတည်း မဖြစ်ရပါ။ File name ပါသော child path တစ်ခု ဖြစ်ရမည်။ User ID, record ID နှင့် file name ကို input အဖြစ် တည်ဆောက်ပြီး allow-list path အတွင်းတွင်သာ အသုံးပြုပါ။ [1]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **41 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Upload file, upload with MIME, download, download URL, delete, metadata |
| Progress | Percentage, transferred/total MB, pause, resume, cancel |
| Validation | Storage path, local file, MIME type, size, safe upload/download |
| Events | Upload/download progress, upload/download success, delete success, failure |
| Professional | Content type, cache-control, file size/type, retryable error, cleanup |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `firebasestorage_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ Firebase Storage palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် Firebase Storage component တစ်ခုရှိရမည်။ Block တစ်ခုချင်းစီ၏ `%m.firebasestorage` dropdown မှ မိမိအသုံးပြုလိုသော component ကို ရွေးပါ။

## Upload Workflow

```text
File picker / camera result
    validate local file path
    validate storage path
    validate MIME type and size
    upload file
```

`Upload File` သည် local file path သို့မဟုတ် version-supported URI input ကို လက်ခံသည်။ Upload လုပ်မည့် path သည် file name ပါသော child path ဖြစ်ရမည်။

```text
FirebaseStorage_Safe_Upload_File
    firebasestorage = firebase_storage
    path = "users/user_id/profile/avatar.jpg"
    local_file = selected_file_path
```

အရွယ်အစားကြီးသော file များကို memory ထဲ တစ်ခုလုံးတင်ပြီး byte array အဖြစ် upload မလုပ်ဘဲ `putFile`/stream-based component workflow ကို ဦးစားပေးပါ။ Official Storage API တွင် `putFile`, `putBytes` နှင့် `putStream` ပုံစံများရှိပြီး `putBytes` သည် file တစ်ခုလုံးကို memory ထဲ ထိန်းထားနိုင်သည်။ [1]

## `onUploadProgress` Event

Upload task လုပ်နေစဉ် bytes transferred နှင့် total bytes ကို event မှ ရယူပြီး progress indicator ပြပါ။

```text
Firebase Storage → onUploadProgress
    percent = FirebaseStorage_Event_Progress_Percent(transferred, total)
    update ProgressBar with percent
    update label with transferred/total MB
```

Total byte count သည် 0 သို့မဟုတ် မသိရသောအခါ percentage ကို 0 အဖြစ် လုံခြုံစွာ ပြန်ပေးထားသည်။ UI ကို callback တိုင်းတွင် အလွန်အကျွံ refresh မလုပ်ဘဲ throttle/debounce လုပ်ပါ။

## `onUploadSuccess` Event

Upload ပြီးဆုံးသောအခါ `onUploadSuccess` event တွင် download URL, metadata နှင့် database record update workflow ကို ဆက်လုပ်နိုင်သည်။ Upload ပြီးနောက် Storage reference ၏ `getDownloadUrl` ဖြင့် URL ရယူနိုင်သည်။ [1]

```text
Firebase Storage → onUploadSuccess
    FirebaseStorage_Get_Download_URL(firebase_storage, uploaded_path)
    save URL only if the app's sharing policy permits
    mark upload as complete
```

Download URL ကို public secret အဖြစ် မယူဆပါနှင့်။ URL ကို လိုအပ်သူထံသာ ပေးပြီး Storage Security Rules နှင့် authentication ဖြင့် file access ကို server-side ကာကွယ်ပါ။

## Download Workflow နှင့် `onDownloadProgress`

```text
FirebaseStorage_Safe_Download_File
    firebasestorage = firebase_storage
    path = "users/user_id/profile/avatar.jpg"
    local_file = local_cache_path
```

`getFile` သည် file ကို local device သို့ download ပြုလုပ်ရန် သင့်တော်သည်။ Memory ထဲသို့ byte array အဖြစ် download လုပ်မည်ဆိုပါက explicit maximum size သတ်မှတ်ရပြီး file ကြီးလွန်းလျှင် app crash ဖြစ်နိုင်သည်။ [2]

`onDownloadProgress` event တွင် transferred/total bytes ဖြင့် progress ပြပါ။ Download task သည် Activity lifecycle ပြောင်းလဲပြီးနောက် ဆက်လက်လုပ်ဆောင်နိုင်သဖြင့် stale callback မဖြစ်စေရန် lifecycle-aware listener သို့မဟုတ် active task restoration workflow ကို စဉ်းစားပါ။ [2]

```text
Firebase Storage → onDownloadProgress
    percent = FirebaseStorage_Event_Progress_Percent(transferred, total)
    show download progress
```

## `onDownloadSuccess` Event

Download အောင်မြင်သောအခါ local file path ကို စစ်ပြီး image/audio/document UI သို့ တင်ပါ။ File type ကို metadata သို့မဟုတ် trusted extension အပေါ် အခြေခံပြီး သင့်လျော်သော renderer ကိုသာ အသုံးပြုပါ။

```text
Firebase Storage → onDownloadSuccess
    verify local file exists
    hide loading
    display or share the local file
```

Download URL ဖြင့် external infrastructure သုံးပါက URL ကို URL query string ထဲ မလိုအပ်ဘဲ မပို့ပါနှင့်။ URL access policy နှင့် expiry/share rules ကို project အလိုက် သတ်မှတ်ပါ။

## `onDeleteSuccess` Event

ဖိုင်ဖျက်ရန် Storage path ကို အတည်ပြုစစ်ဆေးပြီး `Delete File` ကို သုံးပါ။ Delete operation မအောင်မြင်နိုင်သည့်အကြောင်းများထဲတွင် file မရှိခြင်းနှင့် permission မရှိခြင်း ပါဝင်သည်။ [3]

```text
confirm delete
    FirebaseStorage_Delete_If_Path_Valid(firebase_storage, path)

Firebase Storage → onDeleteSuccess
    remove matching metadata/database record
    refresh UI
```

Storage file နှင့် Realtime Database metadata row ကို တစ်ပြိုင်နက်ထိန်းသိမ်းပါ။ Storage delete အောင်မြင်သော်လည်း database record မဖျက်နိုင်ပါက orphan metadata မကျန်စေရန် cleanup/retry strategy ထားပါ။

## `onFailure` Event

Upload, download, URL retrieval, metadata နှင့် delete operation အားလုံးတွင် failure path ထည့်ပါ။ Raw exception ကို user UI တွင် တိုက်ရိုက်မပြဘဲ safe summary နှင့် developer log ကို ခွဲထားပါ။

```text
Firebase Storage → onFailure
    hide loading/progress
    error_summary = FirebaseStorage_Event_Error_Summary(error)
    if FirebaseStorage_Is_Retryable_Error(error)
        show retry action
    else
        show safe failure message
```

Permission error, file-not-found error, network timeout နှင့် cancelled task ကို ခွဲခြားနိုင်ပါက user ကို သင့်လျော်သော next action ပြပါ။ Security Rules ကြောင့် permission denied ဖြစ်ပါက client မှ အခွင့်အရေးကို အတင်းမတောင်းဘဲ authenticated state/path ownership ကို ပြန်စစ်ပါ။

## MIME Type နှင့် Metadata

Upload မတင်မီ MIME type ကို စစ်ပြီး server-side Storage Rules တွင်လည်း content type နှင့် size ကို validate လုပ်ပါ။ Storage metadata တွင် file name, size နှင့် content type ကဲ့သို့ information များ ပါဝင်နိုင်သည်။ [1]

```text
if FirebaseStorage_Is_Allowed_Mime(mime, allowed_mime_list)
    FirebaseStorage_Upload_File_With_Mime(storage, path, file, mime)
else
    show unsupported-file-type message
```

Client-side MIME/size validation သည် UX အတွက်ဖြစ်ပြီး security boundary မဟုတ်ပါ။ Firebase Storage Security Rules တွင် `request.resource.size` နှင့် `request.resource.contentType` ကို အကန့်အသတ်ထားပါ။ [4]

## Pause, Resume နှင့် Cancel

Upload task ကို user က ခဏရပ်ရန် `Pause Upload`၊ ဆက်ရန် `Resume Upload` နှင့် ဖျက်သိမ်းရန် `Cancel Task` ကို သုံးပါ။ Cancel လုပ်ခြင်းသည် failure callback ဖြစ်စေနိုင်သောကြောင့် `onFailure` event တွင် user-cancel နှင့် unexpected error ကို ခွဲခြားစဉ်းစားပါ။ [1]

```text
Pause button → FirebaseStorage_Pause_Upload(firebase_storage)
Resume button → FirebaseStorage_Resume_Upload(firebase_storage)
Cancel button → FirebaseStorage_Cancel_Task(firebase_storage)
```

## Professional User Path Design

```text
path = "users/" + authenticated_uid + "/files/" + generated_file_name
```

Path ကို user-controlled `../`, empty segment, root path နှင့် illegal character များမှ ကာကွယ်ပါ။ Firebase Security Rules တွင် `request.auth.uid` နှင့် path user ID ကို တိုက်စစ်ပြီး user ကိုယ်ပိုင် folder အတွင်းသာ read/write ခွင့်ပြုပါ။ Cloud Storage Rules သည် path-based authorization နှင့် file metadata validation ကို ထောက်ပံ့သည်။ [4]

ဥပမာ rule concept သည် authenticated user ကို သူ့ folder အတွင်းသာ ခွင့်ပြုခြင်းဖြစ်သည်။ Production project တွင် မိမိ data model, authentication provider နှင့် admin workflow အတိုင်း rule ကို သီးခြားရေးပြီး default public rule မထားပါနှင့်။

## Lifecycle နှင့် Active Task

Activity rotation, screen navigation သို့မဟုတ် app background သွားချိန်တွင် upload/download task သည် ဆက်လက်လုပ်ဆောင်နိုင်သည်။ Old Activity ကို ရည်ညွှန်းနေသော listener များကြောင့် memory leak သို့မဟုတ် stale UI update ဖြစ်နိုင်သောကြောင့် lifecycle-aware listener သုံးပါ။ Download ပြန်ဝင်လာသောအခါ active task များကို ရှာပြီး current UI နှင့် ပြန်ချိတ်ပါ။ [2]

Operation ပြီးဆုံးခြင်း၊ failure ဖြစ်ခြင်း၊ cancel ဖြစ်ခြင်းနှင့် logout ဖြစ်ခြင်း path အားလုံးတွင် progress state ကို reset လုပ်ပြီး task cleanup ပြုလုပ်ပါ။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.firebasestorage` selector သုံးထားသည် |
| Component setup ကိုလေးစားရမည် | Name နှင့် Data Location ကို guide တွင် သီးခြားရှင်းပြထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, View ID, bucket path, URL, file name နှင့် user ID မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$s` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Event-aware | Progress, success, delete success, download success နှင့် failure workflows အားလုံးကို မှတ်တမ်းတင်ထားသည် |
| Secure by design | Path, MIME, size နှင့် server-side Rules ကို အလေးထားထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Component Name | Project တွင် unique local name ရှိသည် |
| Data Location | Default Storage path မှန်ကန်သည် |
| Root upload | `/` root သို့ တိုက်ရိုက် upload မလုပ်ပါ |
| File validation | Local file ရှိပြီး size/MIME မှန်သည် |
| Upload progress | Percentage 0–100 အတွင်း update ဖြစ်သည် |
| Upload success | URL/metadata ရရှိပြီး success UI ပြသည် |
| Download progress | Large file ကို memory မပြည့်ဘဲ download သည် |
| Download success | Local file ရှိပြီး renderer မှန်သည် |
| Delete success | UI နှင့် metadata record synchronize ဖြစ်သည် |
| Failure | Loading ပိတ်ပြီး retry/generic error path ရှိသည် |
| Permission | Security Rules denied path ကို မှန်ကန်စွာ ကိုင်တွယ်သည် |
| Lifecycle | Rotation/navigation ပြီးနောက် stale callback မဖြစ်ပါ |
| Cancel | User cancel နှင့် unexpected failure ခွဲခြားနိုင်သည် |
| Privacy | Sensitive URL/path/token များ မဖော်ပြပါ |

## Compatibility Note

Sketchware Pro version အလိုက် Firebase Storage component ၏ generated method name၊ local file/URI input type၊ progress event parameter order၊ success URL parameter နှင့် metadata support ကွာနိုင်ပါသည်။ ဤ library သည် `uploadFile`, `downloadFile`, `getDownloadUrl`, `deleteFile`, `getMetadata`, `setMetadata`, `setContentType`, `setCacheControl`, progress control နှင့် task lifecycle ပုံစံများကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in Firebase Storage blocks နှင့် generated source code ကို တိုက်စစ်ပြီး unsupported block ကို built-in equivalent ဖြင့် အစားထိုးပါ။

`onUploadProgress`, `onDownloadProgress`, `onUploadSuccess`, `onDownloadSuccess`, `onDeleteSuccess` နှင့် `onFailure` event များကို Custom Block JSON က အလိုအလျောက် ဖန်တီးပေးခြင်းမဟုတ်ပါ။ Firebase Storage component ကို Add လုပ်ပြီးနောက် သက်ဆိုင်ရာ event အတွင်းတွင် event parameters နှင့် UI/business logic ကို သီးခြားရေးရမည်။

## References

[1] Firebase, “Upload files with Cloud Storage on Android,” https://firebase.google.com/docs/storage/android/upload-files  
[2] Firebase, “Download files with Cloud Storage on Android,” https://firebase.google.com/docs/storage/android/download-files  
[3] Firebase, “Delete files with Cloud Storage on Android,” https://firebase.google.com/docs/storage/android/delete-files  
[4] Firebase, “Cloud Storage for Firebase Security Rules,” https://firebase.google.com/docs/storage/security  
[5] Sketchware Pro, “Connecting to Firebase,” https://docs.sketchware.pro/docs/components/google/firebase/connect-to-firebase/
