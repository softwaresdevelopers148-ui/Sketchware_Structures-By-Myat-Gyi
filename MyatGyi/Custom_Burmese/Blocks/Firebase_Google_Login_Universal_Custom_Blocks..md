# Firebase Google Login Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Block Rules & Guide ကို အခြေခံပြီး Firebase Google Login component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ အရေးကြီးဆုံး စည်းမျဉ်းမှာ **parameter spec/code ထဲရှိ component selector ကို `%m.googlelogin` ဟုသာ အသုံးပြုရမည်** ဖြစ်သည်။ `%m.firebasegooglelogin` ကို ဤ library တွင် မသုံးထားပါ။

Firebase Google Sign-In သည် Google Account picker မှ account ရွေးပြီး Google credential/ID token ကို Firebase Authentication နှင့် ချိတ်ဆက်ကာ sign-in ဝင်သည့် workflow ဖြစ်သည်။ Modern Android implementation များတွင် Credential Manager နှင့် Google ID token ကို အသုံးပြုနိုင်ပြီး Firebase Console တွင် Google provider, OAuth client configuration နှင့် Android SHA fingerprint များ မှန်ကန်ရမည်။ [1] [2]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **42 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Account Picker စတင်ခြင်း၊ token/credential ဖြင့် sign-in၊ sign-out |
| Session | Login state, email, UID, provider ID နှင့် current profile |
| Profile | Display name, photo URL, provider check, reload |
| `onAccountPicker` | Account email/token သိမ်းခြင်း၊ success flow၊ sign-in ဆက်လုပ်ခြင်း |
| `onAccountPickerCancelled` | Cancel စစ်ခြင်း၊ retry၊ state cleanup နှင့် cancel action |
| Validation | Email, token, OAuth client ID နှင့် session validation |
| Professional | Credential state cleanup, token masking, provider configuration နှင့် secure lifecycle |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `googlelogin_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ Firebase Google Login palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် Firebase Google Login component ကို Add လုပ်ပြီး component name ကို သတ်မှတ်ထားရမည်။ Block ထဲရှိ component slot တွင် သင့် project ၏ Google Login component ကို ရွေးပါ။

> **Master Rules အရေးကြီးမှတ်ချက်:** Block spec/code parameter တွင် component selector သည် **`%m.googlelogin`** ဖြစ်သည်။ `%m.firebasegooglelogin` သို့မဟုတ် အခြား selector များကို မသုံးပါ။

## Firebase Project နှင့် OAuth Setup

Firebase Console → Authentication → Sign-in method သို့ဝင်ပြီး Google provider ကို Enable လုပ်ပါ။ Firebase project ၏ Android app တွင် SHA-1/SHA-256 fingerprint များ၊ OAuth client configuration နှင့် နောက်ဆုံး `google-services.json` configuration ကို စစ်ပါ။ Modern Credential Manager flow တွင် server/client ID configuration သည် သင့် backend/provider setup နှင့် ကိုက်ညီရမည်။ [1]

Sketchware version အလိုက် Google Login component သည် legacy Google Sign-In API သို့မဟုတ် Credential Manager wrapper ကို အသုံးပြုနိုင်သောကြောင့် component ၏ built-in block နှင့် generated source code ကို အခြေခံပါ။

## Basic Account Picker Flow

```text
When btn_google_login clicked
    if GoogleLogin_Is_Logged_In(googlelogin) is false
        show loading
        GoogleLogin_Start_Picker(googlelogin)
```

Account picker သည် user က ပိတ်နိုင်သော UI ဖြစ်သည်။ User က picker ကို dismiss/cancel လုပ်ပြီးနောက် login button ဖြင့် flow ကို ပြန်စနိုင်ရန် persistent Google login button တစ်ခု ထားသင့်သည်။ Device တွင် Google Account မရှိသောအခါ picker မပေါ်နိုင်သဖြင့် account ထည့်ရန် သို့မဟုတ် alternative login method သုံးရန် user ကို လမ်းညွှန်ပါ။ [2]

## `onAccountPicker` Event

`onAccountPicker` event သည် account ရွေးချယ်မှုအောင်မြင်ပြီး account information သို့မဟုတ် credential/token ရရှိသောအခါ ဖြစ်သည်။ Event parameter name/order သည် Sketchware version အလိုက် ကွာနိုင်သဖြင့် event တွင် အမှန်တကယ်ရရှိသော parameter ကို block input အဖြစ် ချိတ်ပါ။

```text
Firebase Google Login → onAccountPicker
    save account email if available
    save Google ID token only temporarily
    if token is valid
        GoogleLogin_OnPicker_Sign_In(googlelogin, token)
```

Google ID token ရရှိပါက Firebase credential သို့ exchange လုပ်ပြီး Firebase Auth sign-in ကို ဆက်လုပ်နိုင်သည်။ Official Firebase flow တွင် Google credential ကို `GoogleAuthProvider.getCredential(idToken, null)` ဖြင့် ဖန်တီးပြီး `signInWithCredential` ဖြင့် authenticate လုပ်သည်။ [1]

```text
onAccountPicker(account_email, id_token)
    if GoogleLogin_Is_Valid_Token(id_token)
        GoogleLogin_Sign_In_With_Token(googlelogin, id_token)
```

Component က sign-in ကို အလိုအလျောက် ဆက်လုပ်ပြီးသားဖြစ်ပါက token ကို ထပ်မံ sign-in မလုပ်ဘဲ `isLoggedIn` နှင့် current user state ကိုသာ စစ်ပါ။

## `onAccountPickerCancelled` Event

`onAccountPickerCancelled` event သည် user က account picker ပိတ်လိုက်သောအခါ သို့မဟုတ် picker result မရရှိဘဲ flow ပယ်ဖျက်သောအခါ ဖြစ်သည်။ Cancel သည် authentication failure အမြဲမဟုတ်ဘဲ user action ဖြစ်နိုင်သောကြောင့် error အဖြစ် ပြင်းပြင်းထန်ထန် မပြပါနှင့်။

```text
Firebase Google Login → onAccountPickerCancelled
    hide loading
    clear temporary account/token state
    keep user on login screen
    optionally allow manual retry
```

Retry ကို အလိုအလျောက် အကြိမ်ကြိမ် မလုပ်ဘဲ user နှိပ်သောအခါသာ `GoogleLogin_Cancel_Retry` ကို သုံးပါ။ Cancel error parameter မရှိသော version တွင် empty error ကို user cancellation အဖြစ် သတ်မှတ်နိုင်သည်။

## Token နှင့် Credential Security

Google ID token နှင့် credential များသည် sensitive data ဖြစ်သောကြောင့် source code, Logcat, public database, URL query string, screenshot သို့မဟုတ် SharedPreferences ထဲတွင် မလိုအပ်ဘဲ မသိမ်းပါနှင့်။ Backend သို့ authentication ပြုလုပ်ရန်လိုပါက token ကို HTTPS ဖြင့် trusted server သို့ ပို့ပြီး server-side verify လုပ်ပါ။

```text
onAccountPicker
    if token is valid
        sign in immediately
        clear temporary token variable after use
```

Debug လုပ်ရန်လိုပါက `GoogleLogin_Safe_Token_For_Log` ဖြင့် token ကို mask လုပ်ပါ။ Production Logcat တွင် token အပြည့်အစုံ မပေါ်ကြောင်း စစ်ပါ။

## Current User နှင့် Profile

Sign-in အောင်မြင်ပြီးနောက် `GoogleLogin_Get_Email`, `GoogleLogin_Get_UID`, `GoogleLogin_Get_Display_Name` နှင့် `GoogleLogin_Get_Photo_URL` ကို အသုံးပြု၍ current user data ရယူနိုင်သည်။ UID သည် Firebase project အတွင်း user ခွဲခြားရန်ဖြစ်ပြီး authentication secret မဟုတ်ပါ။ Backend authorization လိုပါက verified Firebase ID token ကို အသုံးပြုပါ။ [1]

```text
onAccountPicker success
    if GoogleLogin_Is_Logged_In(googlelogin)
        uid = GoogleLogin_Get_UID(googlelogin)
        display_name = GoogleLogin_Get_Display_Name(googlelogin)
        open authenticated screen
```

Photo URL သည် user profile data ဖြစ်သောကြောင့် trusted image loader နှင့် safe HTTPS handling ကို သုံးပါ။ User sign-in provider သည် Google ဖြစ်/မဖြစ် စစ်ရန် `GoogleLogin_Is_Google_Provider` ကို သုံးပါ။

## Sign Out နှင့် Credential State Cleanup

Sign out လုပ်သောအခါ Firebase auth state ကို ရှင်းရုံသာမက modern Credential Manager support ရှိသော implementation တွင် provider credential state ကိုပါ clear လုပ်သင့်သည်။ ထို့နောက် Google account picker ကို ပြန်ဖွင့်ရန် login button ကို အသုံးပြုနိုင်သည်။ [1]

```text
When btn_logout clicked
    GoogleLogin_Safe_Sign_Out(googlelogin, temporary_token)
    GoogleLogin_Clear_Credential_State(googlelogin)
    clear local user state
    return to login screen
```

Component version တွင် `clearCredentialState` method မရှိပါက built-in sign-out block ကို သုံးပြီး generated source code အတိုင်း provider state ကို စီမံပါ။

## Error Handling နှင့် Retry

Google Login failure သည် user cancel, no account, network, invalid OAuth client ID, SHA mismatch, provider disabled, credential type မကိုက်ညီခြင်း သို့မဟုတ် Firebase configuration error ကြောင့် ဖြစ်နိုင်သည်။ Raw exception ကို user ထံ တိုက်ရိုက်မပြဘဲ `GoogleLogin_Event_Error_Summary` ကို အသုံးပြုပါ။

| အခြေအနေ | အသုံးပြုရန် workflow | User-facing action |
|---|---|---|
| User cancel | `onAccountPickerCancelled` | Login screen တွင်ထားပြီး manual retry ပေးပါ |
| No account | Picker retry | Device Google Account ထည့်ရန် ပြောပါ |
| Network | Error + manual retry | Network စစ်ရန် ပြောပါ |
| SHA/OAuth error | Provider configuration note | Firebase/Google Console ပြန်စစ်ပါ |
| Provider disabled | Configuration note | Google provider Enable လုပ်ပါ |
| Invalid token | Token validation | Token ကို မသိမ်းဘဲ flow ပြန်စပါ |
| Unknown | Safe error summary | Generic sign-in failure ပြပါ |

Retry ကို user action သို့မဟုတ် bounded retry အဖြစ်သာ သုံးပါ။ OAuth configuration error ကို အလိုအလျောက် ထပ်ခါထပ်ခါ retry လုပ်ခြင်းသည် ပြဿနာကို မဖြေရှင်းနိုင်ပါ။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Correct component selector | Parameter spec/code တွင် **`%m.googlelogin`** ကိုသာ အသုံးပြုထားသည် |
| Wrong selector exclusion | `%m.firebasegooglelogin` occurrence မရှိပါ |
| Universal ဖြစ်ရမည် | Google Login component ကို dropdown input အဖြစ် လက်ခံထားသည် |
| Fixed ID မဖြစ်ရ | Fixed Activity ID, View ID, email, account, token, client ID နှင့် user ID မသုံးထားပါ |
| Event-aware | `onAccountPicker` နှင့် `onAccountPickerCancelled` workflows အားလုံးကို guide တွင် ထည့်ထားသည် |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$b` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Security | Google ID token/credential များကို hard-code/log မလုပ်ရန် လမ်းညွှန်ထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Component selector | `%m.googlelogin` သာ သုံးထားသည် |
| Firebase provider | Google provider enabled ဖြစ်သည် |
| SHA/OAuth | Android SHA နှင့် OAuth client ID မှန်သည် |
| Account picker | Account ရွေးပြီး `onAccountPicker` event ရောက်သည် |
| Token | Token ရှိပါက credential/sign-in flow ဆက်သည် |
| Cancel | `onAccountPickerCancelled` တွင် app မ crash ပါ |
| Retry | Cancel ပြီး user action ဖြင့် ပြန်စနိုင်သည် |
| Profile | Email/name/photo/UID ကို current user မှ ရသည် |
| Sign out | Auth state နှင့် credential state cleanup ရှိသည် |
| Network | Loading stuck မဖြစ်ဘဲ retry path ရှိသည် |
| No account | User ကို account ထည့်ရန် သို့မဟုတ် alternative login ပြောသည် |
| Privacy | Token/credential/raw error မဖော်ပြပါ |
| Lifecycle | Rotation/background ပြီးနောက် stale picker result မဖြစ်ပါ |

## Compatibility Note

Sketchware Pro version အလိုက် Firebase Google Login component ၏ generated method name၊ account-picker API၊ token/credential parameter order၊ cancellation error text နှင့် sign-out behavior ကွာနိုင်ပါသည်။ ဤ library သည် `startAccountPicker`, `signInWithGoogleToken`, `signInWithCredential`, `isLoggedIn`, current-user/profile helpers နှင့် credential cleanup workflow များကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in Google Login blocks နှင့် generated source code ကို တိုက်စစ်ပြီး unsupported method ကို built-in equivalent ဖြင့် အစားထိုးပါ။

`onAccountPicker` နှင့် `onAccountPickerCancelled` event များကို Custom Block JSON က အလိုအလျောက် ဖန်တီးပေးခြင်းမဟုတ်ပါ။ Firebase Google Login component ကို Add လုပ်ပြီးနောက် သက်ဆိုင်ရာ event အတွင်းတွင် event parameters နှင့် UI/business logic ကို သီးခြားရေးရမည်။

## References

[1] Firebase, “Authenticate with Firebase Using Google on Android,” https://firebase.google.com/docs/auth/android/google-signin
[2] Android Developers, “Sign in with Google,” https://developer.android.com/identity/sign-in/credential-manager-siwg
[3] Firebase, “Manage Users in Firebase,” https://firebase.google.com/docs/auth/android/manage-users
