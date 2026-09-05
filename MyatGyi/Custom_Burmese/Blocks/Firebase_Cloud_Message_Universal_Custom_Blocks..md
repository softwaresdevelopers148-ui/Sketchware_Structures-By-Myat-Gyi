# Firebase Cloud Message Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Block Rules & Guide ကို အခြေခံပြီး Sketchware Firebase Cloud Message component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ အရေးကြီးဆုံး စည်းမျဉ်းမှာ **parameter spec/code ထဲရှိ component selector ကို `%m.cloudmessage` ဟုသာ အသုံးပြုရမည်** ဖြစ်သည်။ `%m.firebasecloudmessage` ကို ဤ library တွင် မသုံးထားပါ။

Firebase Cloud Messaging (FCM) သည် Android app များသို့ notification/data message ပို့ရန် အသုံးပြုနိုင်သည်။ Sketchware Cloud Message component ကို Component Manager မှ Add လုပ်ပြီး user သတ်မှတ်ထားသော component name ဖြင့် အသုံးပြုရသည်။ Component documentation တွင် `onCompleteRegister` event အတွက် `token` string, `success` boolean နှင့် `errorMessage` string ကို ဖော်ပြထားသည်။ [1] [2]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **42 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | FCM register, token ရယူ/စစ်/ရှင်း |
| Topic | Subscribe, unsubscribe, topic validation, safe topic flow |
| Payload | Title/body validation, data map key/value helpers |
| Event | `onCompleteRegister` token, success, error workflow |
| Android | Notification permission နှင့် channel safety notes |
| Professional | Token refresh, topic deduplication, backoff, masked logging, secure cleanup |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `cloudmessage_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ Firebase Cloud Message palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် Firebase Cloud Message component ကို Add လုပ်ပြီး component name ကို သတ်မှတ်ထားရမည်။ Block ထဲရှိ component slot တွင် သင့် project ၏ Cloud Message component ကို ရွေးပါ။

> **Master Rules အရေးကြီးမှတ်ချက်:** Parameter spec/code တွင် FCM component selector သည် **`%m.cloudmessage`** ဖြစ်သည်။ `%m.firebasecloudmessage` သို့မဟုတ် အခြား selector များကို မသုံးပါ။

## Firebase Project နှင့် Component Setup

Firebase project ကို Android app နှင့် ချိတ်ဆက်ပြီး Cloud Messaging ကို အသုံးပြုနိုင်သော Firebase dependency/configuration ရှိရမည်။ Sketchware Pro တွင် Component Manager → `+` → Cloud Message သို့သွားပြီး component name ထည့်ပါ။ Component ထည့်ပြီးနောက် `onCompleteRegister` event ကို အသုံးပြုနိုင်ပါမည်။ [2]

FCM မှ notification message ကို background တွင် ပြသရန် built-in behavior ရှိနိုင်သော်လည်း foreground message, data payload နှင့် custom handling လိုပါက `FirebaseMessagingService`/manifest integration နှင့် Sketchware version ၏ generated setup ကို စစ်ရမည်။ [1] [3]

## Basic Registration Flow

```text
When app starts
    if CloudMessage_Register State မပြီးသေး
        CloudMessage_Register(cloudmessage)
```

FCM registration token သည် installation, reinstall, app-data clear, device restore သို့မဟုတ် token refresh အခြေအနေများတွင် ပြောင်းနိုင်သည်။ Token ရလာတိုင်း နောက်ဆုံး token ကိုသာ အသုံးပြုပြီး backend တွင် သိမ်းမည်ဆိုပါက authenticated user နှင့် device record ကို မှန်ကန်စွာ ချိတ်ပါ။ [1]

```text
Firebase Cloud Message → onCompleteRegister
    if success
        save latest token only when required
        update backend device record through trusted server
    else
        show safe registration error
```

## `onCompleteRegister` Event

ဤ event သည် Cloud Message component ၏ registration ပြီးဆုံးသောအခါ ဖြစ်သည်။ Event parameter များကို အောက်ပါအတိုင်း အသုံးပြုပါ။

| Event parameter | Type | အသုံးပြုပုံ |
|---|---|---|
| `token` | String | Device/app instance ကို target လုပ်ရန် နောက်ဆုံး registration token |
| `success` | Boolean | Registration အောင်မြင်/မအောင်မြင် စစ်ရန် |
| `errorMessage` | String | success=false ဖြစ်သောအခါ diagnostic error |

```text
onCompleteRegister(token, success, errorMessage)
    hide loading
    if success and CloudMessage_Is_Token_Valid(token)
        token_variable = token
        continue token-sync flow
    else
        show generic registration failure
```

Custom Block JSON သည် event ကို အလိုအလျောက် ဖန်တီးပေးခြင်းမဟုတ်ပါ။ Cloud Message component ကို Add လုပ်ပြီးနောက် `onCompleteRegister` event အတွင်းတွင် component ပေးထားသော `token`, `success`, `errorMessage` parameters ကို သင့် Sketchware version တွင် အမှန်တကယ်ရရှိသည့်အတိုင်း ချိတ်ပါ။

## Token Management

`CloudMessage_Get_Token` ကို current registration token ရယူရန် အသုံးပြုနိုင်ပြီး `CloudMessage_Is_Token_Valid` ဖြင့် empty value မဟုတ်ကြောင်း စစ်ပါ။ Token ကို public Realtime Database, URL, screenshot, Logcat သို့မဟုတ် user profile field ထဲ မလိုအပ်ဘဲ မထည့်ပါနှင့်။

```text
onCompleteRegister
    if success
        token = event token
        if token is valid
            send token to trusted backend over HTTPS
```

FCM server key, service-account JSON နှင့် server credential များကို Android client ထဲ ထည့်မထားပါနှင့်။ Device-specific message ပို့ရန် client မှ token ကို ရယူပြီး backend သို့ လုံခြုံစွာပို့ကာ backend/approved sending service မှ ပို့ပါ။

Token ကို debug လုပ်ရန် လိုအပ်လျှင် `CloudMessage_Safe_Token_For_Log` ကို သုံးပြီး token တစ်ခုလုံးမဖော်ပြပါနှင့်။ Production Logcat တွင် token မပေါ်စေရန် ထပ်မံစစ်ဆေးပါ။

## Topic Subscribe နှင့် Unsubscribe

Topic messaging သည် user များ opt-in ပြုလုပ်ထားသော broad information အတွက် သင့်တော်သည်။ Weather update, public announcement နှင့် category-based information တို့အတွက် သုံးနိုင်သော်လည်း sensitive single-device message များအတွက် current registration token ဖြင့် device targeting ကို ဦးစားပေးပါ။ [4]

```text
if CloudMessage_Is_Valid_Topic(topic)
    CloudMessage_Subscribe_Topic(cloudmessage, topic)
```

Topic name တွင် empty value, slash နှင့် မသင့်လျော်သော character များ မပါစေရန် validation ပြုလုပ်ပါ။ Subscribe/unsubscribe ကို app start တိုင်း blind ထပ်ခေါ်ခြင်း မလုပ်ဘဲ local preference ဖြင့် deduplicate လုပ်ပါ။ Topic subscription operation များကို အလွန်မြန်မြန် ထပ်ခေါ်လျှင် quota/throttling ဖြစ်နိုင်သောကြောင့် retry တွင် exponential backoff သုံးပါ။ [4]

```text
user enables topic_notifications
    if topic is not already subscribed
        CloudMessage_Safe_Subscribe(cloudmessage, topic)

user disables topic_notifications
    CloudMessage_Safe_Unsubscribe(cloudmessage, topic)
```

## Notification Permission နှင့် Channel

Android 13/API 33 နှင့်အထက်တွင် `POST_NOTIFICATIONS` runtime permission ကို user ထံ တောင်းရန် လိုအပ်နိုင်သည်။ Permission မရပါက notification ကို ပြသ၍ မရနိုင်သဖြင့် permission မတောင်းမီ user ကို feature အကြောင်း ရှင်းပြပါ။ [1] [3]

Android 8/API 26 နှင့်အထက်တွင် notification channel ကို အသုံးပြုသည်။ FCM notification ရောက်မလာမီ သင့် app သုံးမည့် channel ID နှင့် channel configuration ကို စနစ်တကျ ပြင်ဆင်ပါ။ Channel ID သည် တည်ငြိမ်ပြီး valid ဖြစ်ရမည်။ [1]

```text
if Android version >= 33
    request POST_NOTIFICATIONS

if permission granted
    create/configure notification channel
    allow FCM notifications
else
    show notification-disabled state
```

Cloud Message component သည် message registration နှင့် token workflow အတွက် ဖြစ်ပြီး local notification UI, channel styling, click action နှင့် custom foreground handling ကို Sketchware version အလိုက် Notification component သို့မဟုတ် service workflow နှင့် သီးခြားချိတ်ရနိုင်သည်။

## Notification/Data Payload

FCM payload ကို notification message နှင့် data message ဟူ၍ အကြမ်းဖျင်း ခွဲစဉ်းစားပါ။ Notification message တွင် title/body ကဲ့သို့ user-visible content ပါနိုင်ပြီး data payload တွင် app business logic အတွက် key/value များ ပါနိုင်သည်။ Foreground/background state နှင့် payload type အလိုက် callback/display behavior ကွာနိုင်သောကြောင့် app state အားလုံးတွင် စမ်းသပ်ပါ။ [1] [3]

```text
payload map
    CloudMessage_Build_Data_Pair("route", "detail", payload)
    CloudMessage_Build_Data_Pair("item_id", item_id, payload)
```

Data key/value များကို type-safe အဖြစ် စစ်ပြီး missing key အတွက် default value သတ်မှတ်ပါ။ Password, access token, private message နှင့် sensitive personal data များကို notification title/body ထဲ မထည့်ပါနှင့်။

## Retry နှင့် Failure Handling

`onCompleteRegister` ၏ `success=false` အခြေအနေတွင် `errorMessage` ကို user ထံ raw အပြည့်မပြဘဲ safe summary ပြပါ။ Network/unavailable/quota-related error များတွင်သာ manual retry သို့မဟုတ် bounded exponential backoff ထည့်ပါ။ Invalid configuration သို့မဟုတ် permission error ကို အလိုအလျောက် အကြိမ်ကြိမ် retry မလုပ်ပါနှင့်။

```text
onCompleteRegister(token, success, errorMessage)
    if not success
        if CloudMessage_Is_Retryable_Error(errorMessage)
            wait CloudMessage_Exponential_Backoff(attempt)
            register again within retry limit
        else
            show configuration/permission guidance
```

Retry count ကို အကန့်အသတ်ထားပြီး app launch တိုင်း endless retry မဖြစ်စေရန် state variable သုံးပါ။ User ကို retry button ထည့်ပေးခြင်းသည် quota နှင့် network failure များအတွက် ပိုမိုသင့်တော်နိုင်သည်။

## Professional Event Workflow

```text
App start
    check notification permission on Android 13+
    configure notification channel on Android 8+
    register Cloud Message component

onCompleteRegister(token, success, errorMessage)
    if success and token is valid
        save latest token securely if backend sync is required
        subscribe to opted-in topics only
    else
        hide loading
        show safe failure state

on token refresh
    replace old token on backend
    do not keep stale token as the only target
```

Registration နှင့် topic subscription ကို မလိုအပ်ဘဲ အကြိမ်ကြိမ် မခေါ်ပါနှင့်။ Logout ဖြစ်သောအခါ user-specific topic များမှ unsubscribe လုပ်ရန်၊ backend token mapping ကို disable/remove လုပ်ရန်နှင့် local sensitive state ကို ရှင်းရန် project policy အတိုင်း စီမံပါ။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Correct component selector | Parameter spec/code တွင် **`%m.cloudmessage`** ကိုသာ အသုံးပြုထားသည် |
| Wrong selector exclusion | `%m.firebasecloudmessage` occurrence မရှိပါ |
| Universal ဖြစ်ရမည် | Cloud Message component ကို dropdown input အဖြစ် လက်ခံထားသည် |
| Fixed ID မဖြစ်ရ | Fixed Activity ID, View ID, token, topic, title, body, channel ID နှင့် notification ID မသုံးထားပါ |
| Event-aware | `onCompleteRegister` ၏ token/success/errorMessage workflow ကို guide တွင် ထည့်ထားသည် |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$b` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Security | Server key, service account, token နှင့် private payload ကို client ထဲ မထည့်ရန် လမ်းညွှန်ထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Component selector | `%m.cloudmessage` သာ သုံးထားသည် |
| Registration | `onCompleteRegister` event ရောက်သည် |
| Success | token ရှိပြီး success=true ဖြစ်သည် |
| Failure | errorMessage ကို safe summary ဖြင့် ပြသည် |
| Token rotation | latest token ကို backend mapping တွင် update လုပ်သည် |
| Topic | opt-in topic သာ subscribe လုပ်သည် |
| Duplicate | Topic/register blind duplicate မဖြစ်ပါ |
| Android 13+ | POST_NOTIFICATIONS permission flow ရှိသည် |
| Android 8+ | Notification channel ကြိုတင်ပြင်ဆင်ထားသည် |
| Foreground | data/notification behavior ကို စမ်းသပ်ထားသည် |
| Background | notification display/click behavior ကို စမ်းသပ်ထားသည် |
| Retry | bounded exponential backoff ရှိသည် |
| Security | FCM server key/service account မပါပါ |
| Privacy | token/private payload ကို log/UI မဖော်ပြပါ |

## Compatibility Note

Sketchware Pro version အလိုက် Cloud Message component ၏ generated method name၊ registration method, token refresh behavior၊ topic block support နှင့် event parameter order ကွာနိုင်ပါသည်။ ဤ library သည် `registerDevice`, `getToken`, `subscribeToTopic`, `unsubscribeFromTopic` နှင့် `%m.cloudmessage` selector ကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in Cloud Message blocks နှင့် generated source code ကို တိုက်စစ်ပြီး unsupported method ကို built-in equivalent ဖြင့် အစားထိုးပါ။

`onCompleteRegister` event သည် Custom Block JSON ဖြင့် အလိုအလျောက် ဖန်တီးခြင်းမဟုတ်ပါ။ Component Manager မှ Cloud Message component ကို Add လုပ်ပြီးနောက် Event tab တွင် `onCompleteRegister` ကို ရွေးကာ `token`, `success`, `errorMessage` parameters များကို သင့် version တွင် ပြသသည့်အတိုင်း အသုံးပြုပါ။

## References

[1] Firebase, “Get started with Firebase Cloud Messaging in Android apps,” https://firebase.google.com/docs/cloud-messaging/android/get-started
[2] Sketchware Pro, “Firebase Cloud Messaging,” https://docs.sketchware.pro/docs/components/google/firebase/cloud-messaging/
[3] Firebase, “Set notification message handling in Android apps,” https://firebase.google.com/docs/cloud-messaging/android/receive
[4] Firebase, “Topic messaging,” https://firebase.google.com/docs/cloud-messaging/topic-messaging
