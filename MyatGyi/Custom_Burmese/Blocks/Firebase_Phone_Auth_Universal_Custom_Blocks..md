# Firebase Phone Auth Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Block Rules & Guide ကို အခြေခံပြီး Firebase Phone Auth component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ အရေးကြီးဆုံး စည်းမျဉ်းမှာ **parameter spec ထဲရှိ Firebase Phone Auth component selector ကို `%m.phoneauth` ဟုသာ အသုံးပြုရမည်** ဖြစ်သည်။ `%m.firebasephoneauth` ကို ဤ library တွင် မသုံးထားပါ။

Firebase Phone Auth သည် user ၏ phone number သို့ SMS one-time code ပို့ပြီး verification ID နှင့် code ကို အသုံးပြုကာ sign-in ပြုလုပ်သည့် flow ဖြစ်သည်။ Firebase Console တွင် Phone provider ကို enable လုပ်ခြင်း၊ SMS region policy သတ်မှတ်ခြင်း၊ Android SHA-1/SHA-256၊ Play Integrity/reCAPTCHA နှင့် app verification configuration များ လိုအပ်နိုင်သည်။ [1]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **44 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | SMS code ပို့ခြင်း၊ verification code စစ်ခြင်း၊ phone auth sign-in |
| State | Verification ID သိမ်းခြင်း၊ process active စစ်ခြင်း၊ state ရှင်းခြင်း |
| Validation | E.164 phone, 6-digit code, empty input နှင့် normalize blocks |
| `onCodeSent` | Verification ID save, resend state, cooldown workflow |
| `onVerificationComplete` | Auto/manual verification success နှင့် sign-in workflow |
| `onVerificationFailed` | Invalid number/code, quota, network နှင့် app-verification error |
| Professional | Safe resend, cooldown, sensitive-state cleanup, language နှင့် authenticated flow |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `phoneauth_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ Firebase Phone Auth palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် Firebase Phone Auth component ကို Add လုပ်ပြီး component name ကို သတ်မှတ်ထားရမည်။ Block ထဲရှိ component slot တွင် သင့် project ၏ Phone Auth component ကို ရွေးပါ။

> **Master Rules အရေးကြီးမှတ်ချက်:** Block spec/code parameter တွင် component slot သည် `%m.phoneauth` ဖြစ်သည်။ Event name သည် component ၏ UI တွင် `onVerificationComplete`, `onVerificationFailed`, `onCodeSent` ဟု တွဲပါလာသည့် Firebase Phone Auth event ဖြစ်သည်။

## Firebase Console နှင့် Project ပြင်ဆင်ခြင်း

Firebase Console → Authentication → Sign-in method သို့ဝင်ပြီး Phone provider ကို Enable လုပ်ပါ။ SMS region policy ကို သင့်အသုံးပြုမည့်နိုင်ငံ/ဒေသအတိုင်း သတ်မှတ်ပါ။ Android app configuration တွင် SHA-1 နှင့် SHA-256 fingerprint လိုအပ်နိုင်ပြီး Play Integrity သို့မဟုတ် reCAPTCHA app verification ကို Firebase Phone Auth က အသုံးပြုနိုင်သည်။ [1]

Development အတွက် Firebase Console တွင် fictional test phone number နှင့် code ထည့်သွင်းသုံးနိုင်ပါက production phone number ကို မဖြုန်းဘဲ စမ်းသပ်ပါ။ Production တွင် SMS quota, abuse protection နှင့် region policy များကို စောင့်ကြည့်ပါ။

## Basic Phone Verification Flow

```text
When btn_send_code clicked
    phone = get text from edt_phone
    phone = PhoneAuth_Normalize_Phone(phone)
    if PhoneAuth_Is_Valid_Phone(phone)
        show loading
        PhoneAuth_Send_Code(phoneauth, phone)
    else
        show valid phone-number error
```

Phone number ကို E.164 ပုံစံ `+countrycode + national number` အဖြစ် အသုံးပြုရန် ဦးစားပေးပါ။ ဥပမာ `+959xxxxxxxxx` ကဲ့သို့ နိုင်ငံကုဒ်ပါသော input ဖြစ်ရမည်။ Local phone format ကို E.164 သို့ ပြောင်းရာတွင် နိုင်ငံကုဒ်နှင့် normalization rule ကို project အလိုက် သတ်မှတ်ပါ။

## `onCodeSent` Event

`onCodeSent` သည် SMS code ပို့ပြီး verification ID ရရှိသည့်အခါ ဖြစ်သည်။ Event ထဲရှိ verification ID ကို variable တစ်ခုထဲ သိမ်းထားပြီး နောက်တစ်ဆင့် code verification တွင် အသုံးပြုရမည်။

```text
Firebase Phone Auth → onCodeSent
    verification_id = event verification ID
    save verification_id
    hide send-code loading
    show SMS input
    start resend cooldown
```

သင့် Sketchware version တွင် event parameter name/position သည် ကွာနိုင်သဖြင့် event တွင် အမှန်တကယ်ရရှိသော verification ID parameter ကို `PhoneAuth_OnCodeSent_Save_ID` block ၏ input အဖြစ် ချိတ်ပါ။ Verification ID ကို log, public database, URL သို့မဟုတ် screenshot ထဲ မထည့်ပါနှင့်။

Resend ကို ချက်ချင်း အကြိမ်ကြိမ်မပို့ဘဲ cooldown သတ်မှတ်ပါ။ Firebase `verifyPhoneNumber` flow သည် reentrant behavior ရှိနိုင်သော်လည်း app state ကို သီးခြားသိမ်းထားပြီး Activity restart/rotation အခြေအနေတွင် verification process ကို ပြန်ချိတ်စဉ်းစားရမည်။ [1]

## Verify SMS Code

```text
When btn_verify clicked
    code = get text from edt_code
    code = PhoneAuth_Normalize_Code(code)
    if verification_id is not empty and PhoneAuth_Is_Valid_Code(code)
        show loading
        PhoneAuth_Sign_In_With_Code(phoneauth, verification_id, code)
    else
        show code/verification-state error
```

ဤနေရာတွင် component selector သည် **`%m.phoneauth`** ဖြစ်ရမည်။ Verification ID မရှိဘဲ code တစ်ခုတည်းဖြင့် verify မလုပ်နိုင်ပါ။ Code input သည် ပုံမှန်အားဖြင့် 6 digit ဖြစ်သော်လည်း provider/API response ကို project version အလိုက် စစ်ဆေးပါ။

## `onVerificationComplete` Event

`onVerificationComplete` သည် instant verification သို့မဟုတ် SMS auto-retrieval ဖြစ်သောအခါလည်း ခေါ်နိုင်သည်။ Firebase Phone Auth Android flow တွင် verification complete callback မှ ရရှိသော credential ဖြင့် user ကို sign in ဝင်စေနိုင်သည်။ [1]

```text
Firebase Phone Auth → onVerificationComplete
    hide loading
    if event has valid credential/result
        continue sign-in success flow
        clear phone/code/verification state
        open authenticated screen
    else
        show safe verification message
```

အချို့ component implementation များတွင် event က sign-in ကို component အတွင်း အလိုအလျောက် ဆက်လုပ်ပြီး success parameter သို့မဟုတ် user data ကို ပြန်ပေးနိုင်သည်။ အချို့ version များတွင် verification ID/code သို့မဟုတ် credential ဖြင့် `PhoneAuth_OnVerificationComplete_Success` block ကို ထပ်မံချိတ်ရန် လိုနိုင်သည်။ Built-in event block ၏ generated parameters ကို အခြေခံပါ။

Auto verification ဖြစ်ပါက user ကို code ထပ်မံမတောင်းဘဲ success screen သို့ သွားနိုင်သည်။ သို့သော် authenticated state ကို component/session ဖြင့် ပြန်စစ်ပြီးမှ protected data ကို ဖတ်ပါ။

## `onVerificationFailed` Event

`onVerificationFailed` သည် invalid phone, invalid request, network failure, quota, app verification/reCAPTCHA failure သို့မဟုတ် permission/configuration ပြဿနာများကြောင့် ဖြစ်နိုင်သည်။ [1]

```text
Firebase Phone Auth → onVerificationFailed
    hide loading
    reset only the invalid state
    if invalid phone
        ask user to correct phone format
    else if quota/too-many-requests
        disable resend temporarily
    else if network
        show retry action
    else
        show safe generic error
```

Raw exception ကို user ထံ တိုက်ရိုက်မပြဘဲ `PhoneAuth_Event_Error_Summary` ကို အသုံးပြုပါ။ SMS quota နှင့် abuse-related error ဖြစ်ပါက retry ကို အလိုအလျောက် အကြိမ်ကြိမ်မလုပ်ပါနှင့်။

## Resend Cooldown

```text
onCodeSent
    cooldown_end = current time + 60 seconds

When btn_resend clicked
    if PhoneAuth_Can_Resend(cooldown_end)
        PhoneAuth_Resend_With_Cooldown(phoneauth, cooldown_end, phone)
    else
        show remaining seconds
```

`PhoneAuth_OnCodeSent_Start_Cooldown` block ၏ generated local variable သည် Sketchware version အလိုက် scope ကွာနိုင်သောကြောင့် production project တွင် cooldown end time ကို project Number variable တစ်ခုထဲ သိမ်းပြီး `PhoneAuth_Can_Resend` ကို အသုံးပြုပါ။ Resend button ကို cooldown အတွင်း disabled ထားခြင်းက UX နှင့် SMS abuse နှစ်ခုစလုံးအတွက် သင့်တော်သည်။

## Error အမျိုးအစား ခွဲခြားခြင်း

| Error အမျိုးအစား | သုံးရန် block | User-facing action |
|---|---|---|
| Invalid phone | `Error Is Invalid Number` | Phone format ပြန်ပြင်ရန် ပြောပါ |
| Invalid SMS code | `Error Is Invalid Code` | Code ပြန်စစ်၍ ထပ်ထည့်ရန် ပြောပါ |
| Quota/too many requests | `Error Is Quota` | Resend ရပ်ပြီး နောက်မှ retry လုပ်ပါ |
| Network/timeout | `Error Is Network` | Network စစ်ပြီး manual retry ပေးပါ |
| App verification | `Error Is Captcha` | SHA/app verification configuration စစ်ပါ |
| Unknown | `Event Error Summary` | Generic safe message ပြပါ |

Error text ၏ exact content သည် Firebase SDK version အလိုက် ကွာနိုင်သောကြောင့် substring matching blocks များကို diagnostic helper အဖြစ်သာ သုံးပါ။ Final business logic တွင် component ၏ official error code ရရှိပါက code-based branching ကို ဦးစားပေးပါ။

## Security နှင့် Privacy

Phone number authentication သည် လွယ်ကူသော်လည်း phone number ပိုင်ဆိုင်မှုသည် email/password သို့မဟုတ် multi-factor protection ထက် အားနည်းနိုင်သောကြောင့် security tradeoff ရှိသည်။ Sensitive app များတွင် email/Google login သို့မဟုတ် MFA ကို ထည့်သွင်းစဉ်းစားပါ။ [1]

SMS code, verification ID, phone number, Firebase ID token နှင့် credential များကို source code, logcat, public database, URL query string သို့မဟုတ် screenshot ထဲ မထည့်ပါနှင့်။ Firebase Auth result ကို backend သို့ ပို့ရမည်ဆိုပါက ID token ကို HTTPS ဖြင့် ပို့ပြီး backend တွင် verify လုပ်ပါ။ UID သည် user identifier ဖြစ်သော်လည်း authentication secret မဟုတ်ပါ။

Phone verification မစတင်မီ user ကို SMS ပို့မည်ဖြစ်ကြောင်းနှင့် standard messaging charges ရှိနိုင်ကြောင်း ရှင်းပြပါ။ [1]

## Lifecycle နှင့် State Recovery

Activity rotation, app background/foreground, SMS app ဖွင့်ခြင်းနှင့် screen navigation ဖြစ်နိုင်သောကြောင့် phone, verification ID, cooldown end time နှင့် verification-in-progress state ကို သင့်လျော်စွာ သိမ်းထားပါ။ Screen ပိတ်သွားသောအခါ UI reference များ stale မဖြစ်စေရန် loading state ကို reset လုပ်ပြီး event ပြန်လာချိန်တွင် current screen state ကို စစ်ပါ။

```text
onStart/onResume
    if verification_in_progress
        restore verification_id and cooldown state

onVerificationComplete/onVerificationFailed
    verification_in_progress = false
    clear code input
```

အောင်မြင်ပြီးနောက် `PhoneAuth_Clear_After_Success` သို့မဟုတ် clear-state block များကို သုံးပါ။ Failure ဖြစ်သောအခါ verification ID ကို ချက်ချင်းဖျက်မည်/မဖျက်မည်ကို error အမျိုးအစားအလိုက် ဆုံးဖြတ်ပါ။ Invalid code တွင် resend မလိုဘဲ code ပြန်ထည့်ခွင့်ပေးနိုင်သည်။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Correct component selector | Parameter spec/code တွင် **`%m.phoneauth`** ကိုသာ အသုံးပြုထားသည် |
| Wrong selector exclusion | `%m.firebasephoneauth` occurrence မရှိပါ |
| Universal ဖြစ်ရမည် | Phone Auth component ကို dropdown input အဖြစ် လက်ခံထားသည် |
| Fixed ID မဖြစ်ရ | Fixed Activity ID, View ID, phone number, code, verification ID မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$b` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Event-aware | `onCodeSent`, `onVerificationComplete`, `onVerificationFailed` workflows အားလုံးကို guide တွင် ထည့်ထားသည် |
| Security | Code, verification ID, token နှင့် phone data ကို hard-code/log မလုပ်ရန် လမ်းညွှန်ထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Component selector | `%m.phoneauth` သာ သုံးထားသည် |
| Provider | Firebase Console တွင် Phone provider enabled ဖြစ်သည် |
| Phone format | E.164 phone number ကို လက်ခံသည် |
| Code sent | `onCodeSent` တွင် verification ID သိမ်းသည် |
| Resend | Cooldown အတွင်း SMS ထပ်မပို့ပါ |
| Correct code | `onVerificationComplete` success flow ဖြစ်သည် |
| Wrong code | `onVerificationFailed` မှ safe error ပြသည် |
| Auto verification | Code input မလိုဘဲ session state စစ်သည် |
| Quota error | Retry ကို rate-limit လုပ်သည် |
| Network error | Manual retry action ရှိသည် |
| Rotation | Verification state မပျောက်ပါ |
| Logout/cleanup | Phone, code, verification ID ကို ရှင်းသည် |
| Privacy | SMS code/token/raw credential မဖော်ပြပါ |
| Release | SHA, app verification နှင့် SMS region policy မှန်သည် |

## Compatibility Note

Sketchware Pro version နှင့် Firebase Auth library version အလိုက် method name၊ event capitalization၊ callback parameter order၊ code length၊ Google Play Services behavior နှင့် Phone Auth component generated code ကွာနိုင်ပါသည်။ ဤ library သည် `sendVerificationCode`, `verifyCode`, `signInWithPhoneAuth`, cooldown/state helpers နှင့် event workflow များကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in Firebase Phone Auth blocks နှင့် generated source code ကို တိုက်စစ်ပြီး unsupported method ကို built-in equivalent ဖြင့် အစားထိုးပါ။

`onVerificationComplete`, `onVerificationFailed` နှင့် `onCodeSent` event များကို Custom Block JSON က အလိုအလျောက် ဖန်တီးပေးခြင်းမဟုတ်ပါ။ Firebase Phone Auth component ကို Add လုပ်ပြီးနောက် သက်ဆိုင်ရာ event အတွင်းတွင် event parameters နှင့် UI/business logic ကို သီးခြားရေးရမည်။

## References

[1] Firebase, “Phone Number Authentication on Android,” https://firebase.google.com/docs/auth/android/phone-auth
[2] Firebase, “Get Started with Firebase Authentication on Android,” https://firebase.google.com/docs/auth/android/start
[3] Firebase, “Manage Users in Firebase,” https://firebase.google.com/docs/auth/android/manage-users
[4] Sketchware Docs, “Firebase Auth Component,” https://sketchware-docs.vercel.app/docs/component-firebase-auth.html
