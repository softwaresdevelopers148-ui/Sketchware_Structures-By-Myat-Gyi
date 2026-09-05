# Sketchware Firebase Auth Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware Firebase Auth component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.firebaseauth` selector ကို အသုံးပြုထားသောကြောင့် FirebaseAuth component ID, Activity ID, fixed email, password, phone number, token နှင့် user ID များကို fixed မလုပ်ထားပါ။

Firebase Authentication သည် Email/Password, Google, Phone နှင့် အခြား provider များဖြင့် user identity ကို စီမံပေးသည်။ Firebase Console တွင် အသုံးပြုမည့် provider ကို အရင် Enable လုပ်ပြီး Android project ကို Firebase နှင့် မှန်ကန်စွာ ချိတ်ဆက်ထားရမည်။ [1]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **52 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Create account, sign in, anonymous sign in, sign out, session check |
| Email | Verification email, reset email, update email, update password |
| Profile | Display name, photo URL, provider ID, reload, delete account |
| Google/Phone | Google sign-in, Google token, phone code, phone verification sign-in |
| Validation | Email, password, confirmation, phone, empty input |
| Event | Complete success/error, provider result, signed-in state |
| Professional | Re-authentication, recent-login detection, verified email, ID token, secure cleanup |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `firebaseauth_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ FirebaseAuth palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် Firebase Auth component တစ်ခုရှိရမည်။ Block တစ်ခုချင်းစီ၏ `%m.firebaseauth` dropdown မှ မိမိအသုံးပြုလိုသော component ကို ရွေးပါ။

## Firebase Console Setup

Firebase Console → Authentication → Sign-in method သို့ဝင်ပြီး အသုံးပြုမည့် Email/Password, Google နှင့် Phone provider များကို Enable လုပ်ပါ။ Google sign-in အတွက် Android app ၏ SHA-1/SHA-256 configuration နှင့် OAuth client setup ကို စစ်ပါ။ Phone sign-in အတွက် SMS region policy, app verification, Play Integrity/reCAPTCHA နှင့် test phone number configuration များကို စီမံပါ။ [1] [4]

Password policy နှင့် email enumeration protection ကဲ့သို့ Firebase Console security options များကိုလည်း project risk အလိုက် စီမံပါ။ User ထံ error ပြရာတွင် account ရှိ/မရှိကို အလွန်တိကျစွာ ဖော်ပြခြင်းသည် email enumeration risk ဖြစ်နိုင်သောကြောင့် generic message သုံးရန် စဉ်းစားပါ။ [3]

## Create Account နှင့် Sign In

```text
Button clicked
    if FirebaseAuth_Is_Valid_Email(email)
       and FirebaseAuth_Is_Valid_Password(password, 8)
        FirebaseAuth_Create_User(firebaseauth, email, password)
```

Create user အောင်မြင်ပါက user သည် အလိုအလျောက် signed in ဖြစ်နိုင်ပြီး `onCreateUserComplete` event ထဲတွင် success UI, profile initialization နှင့် email verification flow ကို ဆက်လုပ်ပါ။ [1]

```text
Firebase Auth → onCreateUserComplete
    if event success
        FirebaseAuth_Send_Verification_Email(firebaseauth)
        open verification guidance
    else
        show safe error summary
```

Sign in အတွက် Email နှင့် Password ကို client-side validation ပြုလုပ်ပြီး `FirebaseAuth_Sign_In_User` ကို သုံးပါ။ Firebase Auth သည် credential မှန်/မမှန်ကို server-side စစ်ပေးသဖြင့် client validation သည် UX အတွက်သာဖြစ်ပြီး security boundary မဟုတ်ပါ။

## `onCreateUserComplete` Event

`onCreateUserComplete` သည် Create User block ပြီးဆုံးသောအခါ ဖြစ်သည်။ Event အတွင်း success ဖြစ်ပါက current user/session ကို ပြန်စစ်ပြီး database profile data ကို UID ဖြင့် စတင်တည်ဆောက်နိုင်သည်။ Error ဖြစ်ပါက raw exception သို့မဟုတ် credential ကို UI/log ထဲ မဖော်ပြပါနှင့်။

```text
onCreateUserComplete
    if success
        uid = FirebaseAuth_Get_UID(firebaseauth)
        create initial user profile under uid
    else
        show "Account creation failed"
```

## `onSigninUserComplete` Event

User sign-in ပြီးသောအခါ `onSigninUserComplete` event တွင် session ကို စစ်ပြီး authenticated screen သို့ navigate လုပ်ပါ။

```text
onSigninUserComplete
    if success and FirebaseAuth_Is_Logged_In(firebaseauth)
        load user profile
        open home screen
    else
        keep user on sign-in screen
        show safe error message
```

User အကောင့်ကို စစ်ရန် `isLoggedIn` ကို သုံးပါ။ App restart, Activity start နှင့် protected screen ဝင်ချိန်တို့တွင် current auth state ကို ပြန်စစ်သင့်သည်။ [1]

## `onResetPasswordEmailSent` Event

Password မေ့သွားသော user အတွက် `FirebaseAuth_Send_Reset_Email` ကို သုံးပြီး `onResetPasswordEmailSent` event တွင် request အောင်မြင်/မအောင်မြင်ကို ပြပါ။

```text
if FirebaseAuth_Is_Valid_Email(email)
    FirebaseAuth_Send_Reset_Email(firebaseauth, email)

Firebase Auth → onResetPasswordEmailSent
    hide loading
    show generic reset-email message
```

Email enumeration protection ကို ထည့်ထားသော project များတွင် email ရှိ/မရှိကို မဖော်ပြဘဲ generic response ထားရနိုင်သည်။ [3]

## `onEmailVerificationSent` Event

Create user သို့မဟုတ် signed-in user ထံ verification email ပို့ပြီး `onEmailVerificationSent` event ကို အသုံးပြုပါ။

```text
FirebaseAuth_Send_Verification_Email(firebaseauth)

Firebase Auth → onEmailVerificationSent
    show "Check your email" message
```

Email verification ပို့ခြင်းနှင့် email verified ဖြစ်ပြီးသား/မဖြစ်သေးကို ခွဲထားပါ။ `FirebaseAuth_Is_Email_Verified` သည် local user state ကို စစ်ရန် အသုံးပြုပြီး state update လိုပါက reload user block ကို သုံးပြီး ပြန်စစ်ပါ။

## `onUpdateEmailComplete` Event

Email ပြောင်းခြင်းသည် user identity နှင့် ဆက်စပ်သော sensitive operation ဖြစ်သည်။ User signed in ဖြစ်ကြောင်းစစ်ပြီး လိုအပ်ပါက recent credential ဖြင့် re-authenticate လုပ်ပြီးမှ update email ကို လုပ်ပါ။ Firebase docs အရ sensitive operations များသည် recent sign-in လိုအပ်နိုင်သည်။ [2]

```text
if FirebaseAuth_Is_Valid_Email(new_email)
    if FirebaseAuth_Is_Recent_Login_Error(previous_error)
        ask user to sign in again
    else
        FirebaseAuth_Update_Email(firebaseauth, new_email)

Firebase Auth → onUpdateEmailComplete
    if success
        show email-updated message
    else
        show safe error summary
```

## `onUpdatePasswordComplete` Event

Password အသစ်သည် project policy နှင့် ကိုက်ညီရမည်။ Confirm password တူညီမှုကို client-side စစ်ပြီး sensitive operation ဖြစ်သောကြောင့် recent-login error path ထည့်ပါ။ Password အား UI သို့မဟုတ် log တွင် မသိမ်းပါနှင့်။

```text
if FirebaseAuth_Is_Valid_Password(new_password, 8)
   and FirebaseAuth_Passwords_Match(new_password, confirm_password)
    FirebaseAuth_Update_Password(firebaseauth, new_password)
```

ပြီးဆုံးပြီးနောက် password variables/input fields များကို `FirebaseAuth_Clear_Sensitive_Input` ဖြင့် ရှင်းပါ။

## `onUpdateProfileComplete` Event

Display name နှင့် photo URL ကို ပြောင်းရန် `FirebaseAuth_Update_Profile` ကို သုံးပါ။ Photo URL ကို trusted HTTPS URL အဖြစ် validation ပြုလုပ်ပြီး user-provided URL ကို မယုံကြည်ဘဲ image loading library တွင် safe configuration သုံးပါ။

```text
FirebaseAuth_Update_Profile(firebaseauth, display_name, photo_url)

Firebase Auth → onUpdateProfileComplete
    reload user profile data
    update local UI
```

## `onDeleteUserComplete` Event

Account deletion သည် ပြန်လည်မရနိုင်သော high-impact operation ဖြစ်သောကြောင့် confirmation dialog နှင့် recent re-authentication လိုအပ်သည်။ Firebase docs အရ delete operation သည် recent sign-in မရှိပါက fail ဖြစ်နိုင်သည်။ [2]

```text
show confirmation dialog
if user confirms
    if FirebaseAuth_Is_Logged_In(firebaseauth)
        FirebaseAuth_Delete_User(firebaseauth)

Firebase Auth → onDeleteUserComplete
    if success
        clear local sensitive state
        return to signed-out screen
    else if FirebaseAuth_Is_Recent_Login_Error(error)
        request re-authentication
    else
        show safe error message
```

User account ဖျက်ခြင်းနှင့် Realtime Database/Storage profile data ဖျက်ခြင်းသည် သီးခြားလုပ်ဆောင်ချက်များ ဖြစ်နိုင်သောကြောင့် backend cleanup strategy ကို ကြိုတင်သတ်မှတ်ပါ။ Client မှ arbitrary user data ကို ဖျက်ခွင့်မပေးဘဲ Firebase Security Rules/Cloud Functions ကို အသုံးပြုပါ။

## `onGoogleSignin` Event

Google sign-in အတွက် Firebase Console provider enablement၊ Android SHA configuration နှင့် Google credential setup မှန်ကန်ရမည်။ Google ID token သို့မဟုတ် credential ကို block input အဖြစ် လက်ခံနိုင်သော်လည်း token ကို log/UI ထဲ မဖော်ပြပါနှင့်။

```text
Button clicked
    FirebaseAuth_Google_Sign_In(firebaseauth)

Firebase Auth → onGoogleSignin
    if success
        check FirebaseAuth_Is_Logged_In(firebaseauth)
        continue to authenticated screen
    else
        show generic Google sign-in failure
```

Google provider configuration အမှား၊ user cancel နှင့် network failure ကို သီးခြား UI state များဖြင့် ကိုင်တွယ်ပါ။

## `signInWithPhoneAuthComplete` Event

Phone auth သည် SMS one-time code နှင့် app verification ကို သုံးသည်။ Phone number သည် E.164 ပုံစံနှင့် ကိုက်ညီကြောင်း စစ်ပြီး user ထံ SMS rates/verification အကြောင်း ရှင်းပြပါ။ Phone number တစ်ခုတည်းကို အခြေခံသော auth သည် အခြား provider များထက် security tradeoff ရှိသောကြောင့် multi-factor သို့မဟုတ် email/Google option ကို စဉ်းစားပါ။ [4]

```text
if FirebaseAuth_Is_Valid_Phone(phone_number)
    FirebaseAuth_Send_Phone_Code(firebaseauth, phone_number)

user enters sms_code
    FirebaseAuth_Sign_In_Phone(firebaseauth, verification_id, sms_code)

Firebase Auth → signInWithPhoneAuthComplete
    if success
        open authenticated screen
    else
        show safe verification failure
```

User ပေးထားသော event name `sigInWithPhoneAuthComplete` တွင် spelling ကွာနိုင်သော်လည်း Sketchware version တွင် အမှန်တကယ် ပြသသော event label ကို အသုံးပြုပါ။ Code, verification ID နှင့် phone number ကို log မလုပ်ပါနှင့်။ Phone provider အတွက် region policy နှင့် app verification ကို Firebase Console တွင် သတ်မှတ်ပါ။ [4]

## `onUpdateEmailComplete`, `onUpdatePasswordComplete`, `onUpdateProfileComplete` Result Pattern

Complete event အားလုံးတွင် loading state ကို အရင်ပိတ်ပြီး success/error path ကို ခွဲပါ။ Event signature သည် Sketchware version အလိုက် `success`, `errorMessage` သို့မဟုတ် အခြား parameter name ဖြင့် ရောက်လာနိုင်သောကြောင့် callback parameter ကို သင့် event UI တွင် အမှန်တကယ် ရရှိသည့်အတိုင်း ချိတ်ပါ။

```text
complete event
    hide loading
    if success
        reload current user if required
        update UI
    else
        show safe error summary
```

## Session, UID နှင့် ID Token

`FirebaseAuth_Get_UID` သည် Firebase project အတွင်း user record ခွဲခြားရန် အသုံးဝင်သော်လည်း UID တစ်ခုတည်းကို backend authentication credential အဖြစ် မသုံးပါနှင့်။ Backend သို့ authenticated request ပို့ရန် Firebase ID token ကို server-side verify လုပ်ပါ။ [1]

```text
if FirebaseAuth_Is_Logged_In(firebaseauth)
    uid = FirebaseAuth_Get_UID(firebaseauth)
    FirebaseAuth_Get_ID_Token(firebaseauth, true)
```

ID token သည် sensitive data ဖြစ်သောကြောင့် log, URL query string, public database field သို့မဟုတ် screenshot ထဲ မထည့်ပါနှင့်။ Token refresh နှင့် expiry ကို backend/client SDK workflow အတိုင်း ကိုင်တွယ်ပါ။

## Safe Validation နှင့် Error Handling

| Input/အခြေအနေ | သုံးရန် block/လုပ်ဆောင်ချက် |
|---|---|
| Email လွတ်/မှား | `Is Valid Email` |
| Password တို | `Is Valid Password` |
| Password မတူ | `Passwords Match` |
| Phone ပုံစံမှား | `Is Valid Phone` |
| User မဝင်ထား | `Require Signed In` |
| Email မverified | `Require Verified Email` |
| Recent login လို | `Is Recent Login Error` ဖြင့် re-auth |
| User cancel | Generic cancel message, retry only by action |
| Network/server error | Loading ပိတ်ပြီး safe retry path |

Firebase exception raw message သည် internal details သို့မဟုတ် account enumeration information ပါနိုင်သောကြောင့် `Event Error Summary` ဖြင့် user-facing message ကို safe ပြောင်းပါ။ Debug build တွင်သာ controlled logging ပြုလုပ်ပြီး production တွင် credential/token မပါကြောင်း စစ်ပါ။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.firebaseauth` selector သုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, View ID, email, phone, UID, password, token မသုံးထားပါ |
| Event-aware | User ဖော်ပြထားသော Auth events အားလုံးကို guide workflow ဖြင့် ဖော်ပြထားသည် |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$b` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Security | Credential, password, code, token hard-code/log မလုပ်ရန် လမ်းညွှန်ထားသည် |
| Reusable | Provider, credential, input, validation နှင့် result state များကို input အဖြစ် လက်ခံထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Create user | `onCreateUserComplete` success/error path မှန်သည် |
| Sign in | `onSigninUserComplete` တွင် session state မှန်သည် |
| Google | Provider/SHA configuration ပြီးမှ sign-in အလုပ်လုပ်သည် |
| Phone | SMS code, verification ID နှင့် app verification မှန်သည် |
| Verification | `onEmailVerificationSent` success message ပေါ်သည် |
| Reset | `onResetPasswordEmailSent` တွင် generic safe response ရသည် |
| Email update | Recent-login error တွင် re-auth path ရှိသည် |
| Password update | Policy, confirm match နှင့် sensitive input cleanup ရှိသည် |
| Profile update | Name/photo URL update ပြီး UI reload ဖြစ်သည် |
| Delete | Confirmation + recent authentication + cleanup ရှိသည် |
| Sign out | Local sensitive state ရှင်းပြီး signed-out screen ပြန်ရောက်သည် |
| Error | Raw credential/error detail မပေါ်ပါ |
| Offline/network | Loading state stuck မဖြစ်ဘဲ retry/generic error path ရှိသည် |

## Compatibility Note

Sketchware Pro version အလိုက် Firebase Auth component ၏ generated method name၊ event capitalization၊ event parameter order နှင့် Google/Phone auth support ကွာနိုင်ပါသည်။ ဤ library သည် `createUserWithEmailAndPassword`, `signInWithEmailAndPassword`, `signInAnonymously`, `signOut`, `isLoggedIn`, `getCurrentUser`, `sendEmailVerification`, `sendPasswordResetEmail`, `updateEmail`, `updatePassword`, `updateProfile`, `deleteUser`, `reloadUser`, Google sign-in နှင့် Phone auth workflow များကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in Firebase Auth blocks နှင့် generated source code ကို တိုက်စစ်ပြီး unsupported provider block ကို built-in equivalent ဖြင့် အစားထိုးပါ။

`onUpdateEmailComplete`, `onUpdatePasswordComplete`, `onEmailVerificationSent`, `onDeleteUserComplete`, `signInWithPhoneAuthComplete`, `onUpdateProfileComplete`, `onGoogleSignin`, `onCreateUserComplete`, `onSigninUserComplete` နှင့် `onResetPasswordEmailSent` event များကို Custom Block JSON က အလိုအလျောက် ဖန်တီးပေးခြင်းမဟုတ်ပါ။ Firebase Auth component ကို Add လုပ်ပြီးနောက် သက်ဆိုင်ရာ event အတွင်းတွင် success/error parameters နှင့် UI/business logic ကို သီးခြားရေးရမည်။

## References

[1] Firebase, “Get Started with Firebase Authentication on Android,” https://firebase.google.com/docs/auth/android/start  
[2] Firebase, “Manage Users in Firebase,” https://firebase.google.com/docs/auth/android/manage-users  
[3] Firebase, “Password Authentication on Android,” https://firebase.google.com/docs/auth/android/password-auth  
[4] Firebase, “Phone Number Authentication on Android,” https://firebase.google.com/docs/auth/android/phone-auth  
[5] Sketchware Docs, “Firebase Auth Component,” https://sketchware-docs.vercel.app/docs/component-firebase-auth.html
