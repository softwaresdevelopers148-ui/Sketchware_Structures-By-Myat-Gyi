# Universal Custom Dialog Blocks — Audit နှင့် အသုံးပြုနည်း

ဤဖိုင်သည် ပေးထားသော `pasted_content.txt` ထဲရှိ Custom Dialog နှင့် Security blocks များကို Master Rules အတိုင်း ပြန်လည်စစ်ဆေးပြီး ပြင်ဆင်ထားသော `universal_dialog_blocks_fixed.json` အတွက် audit report ဖြစ်သည်။ ရည်ရွယ်ချက်မှာ Dialog block တစ်ခုကို ထည့်ပြီးနောက် နောက်ထပ် block ထည့်သောအခါ variable collision, fixed event coupling, Activity/View ID coupling နှင့် Java compile error များ မဖြစ်စေရန် ဖြစ်သည်။

## မူရင်းဖိုင်တွင် တွေ့ရှိခဲ့သော ပြဿနာများ

မူရင်း JSON သည် parse လုပ်နိုင်ပြီး entry 25 ခုရှိသော်လည်း Master Rules နှင့် Universal Block စံနှုန်းအတွက် အောက်ပါအချက်များကို ပြင်ရန်လိုအပ်သည်။

| ပြဿနာ | အကြောင်းရင်း | ပြင်ဆင်ထားပုံ |
|---|---|---|
| `palette: "33"` | Master Rules ၏ palette convention နှင့် မကိုက်ညီနိုင်ပြီး palette 33 သို့ ပုံသေချိတ်ထားသည် | Block အားလုံးကို `palette: ""` ပြောင်းထားသည် |
| `OnDismiss` သည် `type: "c"` | Event/listener ကို block တစ်ခုထဲ ပုံသေချိတ်ထားပြီး event တစ်ခုသာ အသုံးပြုနိုင်သည့် collision ဖြစ်နိုင်သည် | Event block ကို ဖယ်ပြီး action input/သီးခြား event workflow အဖြစ် ပြောင်းထားသည် |
| `%3$s.this` | Activity parameter order မရှင်းလင်းဘဲ Activity တစ်ခုနှင့် fixed ဖြစ်နိုင်သည် | Activity ကို explicit `%m.activity` input အဖြစ် သတ်မှတ်ထားသည် |
| `%m.dialog` နှင့် `%m.customViews` သာ spec တွင်ရှိသော်လည်း code မှ `%1$s`, `%2$s`, `%3$s` အသုံးပြုထားသည် | Spec placeholder နှင့် code placeholder မကိုက်၍ generated code မှားနိုင်သည် | Parameter order ကို spec နှင့် code တွင် တစ်သမတ်တည်း ပြန်ချထားသည် |
| `Activity.class` နှင့် `v` | Activity class, undefined `v` နှင့် application context ကို ပေါင်းသုံးထားသည် | Navigation/exit action များကို universal Dialog action မထည့်တော့ဘဲ explicit Action Code သို့ ပြောင်းထားသည် |
| `getApplicationContext()` ဖြင့် Window Dialog ပြခြင်း | Application context သည် Window UI အတွက် သင့်တော်သော Activity context မဟုတ်နိုင် | Dialog ဖန်တီး/show ကို `%m.activity.this` ဖြင့် လုပ်ထားပြီး app-info helper များတွင်သာ application context သုံးထားသည် |
| `layout`, `dialog`, `builder`, `title`, `btn` အမည်များ ထပ်နေခြင်း | Block နှစ်ခုကို source တစ်နေရာတည်းတွင် ထည့်လျှင် local variable redeclaration ဖြစ်နိုင်သည် | Reusable block များသည် existing Dialog/Action input ကို အသုံးပြုပြီး temporary variable များကို `_ud_` prefix သုံးထားသည် |
| Progress block ထဲတွင် Thread/Handler/Activity navigation | Block တစ်ခုက နောက်ထပ် event နှင့် navigation ကို ပုံသေဆုံးဖြတ်ထားပြီး lifecycle leak ဖြစ်နိုင်သည် | Progress UI configuration သီးခြား၊ task/navigation logic သီးခြား ဖြစ်အောင် ပြန်ခွဲထားသည် |
| `killProcess()`/`System.exit(1)` | User app ကို အတင်းပိတ်ခြင်း၊ Android lifecycle နှင့် မကိုက်ညီခြင်း | Security warning ကို ပြသပြီး user action ကို caller ထံထားသည် |
| Client-side anti-tamper check | Package/version/signature check သည် tamper-proof မဟုတ်ဘဲ false positive ဖြစ်နိုင်သည် | Safe diagnostic helpers နှင့် server-side authorization မှတ်ချက် ထည့်ထားသည် |
| Inline `try { value; } catch { fallback; }` expression | Java expression block အဖြစ် compile မဖြစ်နိုင်နိုင်သည် | Version values ကို output variable ထဲ assignment action အဖြစ် ပြောင်းထားသည် |

## ပြန်လည်တည်ဆောက်ထားသော Library

`universal_dialog_blocks_fixed.json` တွင် Header များအပါအဝင် **44 entries** ပါဝင်သည်။ Master Rules အရ Header များသည် `type: "h"` ဖြစ်ပြီး block entry များတွင် `palette: ""` ပါဝင်သည်။ Block အမည်များအားလုံး unique ဖြစ်အောင် ရေးထားပြီး custom event block မပါဝင်ပါ။

| Section | အသုံးပြုရန် |
|---|---|
| ဖန်တီးခြင်းနှင့် အခြေခံထိန်းချုပ်မှု | Activity context ဖြင့် Dialog ဖန်တီး၊ show, dismiss, cancelable, background |
| Title/Message/Custom View | Content သတ်မှတ်ခြင်းနှင့် Dialog ထဲမှ View ရှာခြင်း |
| Buttons | Positive, Negative, Neutral button များနှင့် optional action code |
| Safe Workflow | Empty message စစ်ခြင်း၊ safe show/dismiss၊ confirm dialog |
| App Information | Package, app name, version နှင့် debug state ရယူခြင်း |
| Security | Warning dialog နှင့် non-authoritative package/version check |
| Notes | Fixed event မချိတ်ရန်၊ Activity context သုံးရန်၊ server-side security မှတ်ချက် |

## Universal အသုံးပြုပုံ

### 1. Custom Dialog ဖန်တီးခြင်း

ပထမဆုံး `UniversalDialog_Create` ကို ထည့်ပါ။ Output Dialog variable အမည်၊ Activity component နှင့် custom layout resource name ကို ထည့်ပါ။ Layout resource name သည် Resource Manager ထဲတွင် တကယ်ရှိရမည်။

```text
UniversalDialog_Create(dialog_main, MainActivity, dialog_profile)
```

Generated code သည် Activity context ဖြင့် Dialog ပြုလုပ်ပြီး layout ကို inflate လုပ်သည်။ ထို့နောက် `UniversalDialog_Set_Title`, `UniversalDialog_Set_Message`, button blocks နှင့် `UniversalDialog_Show` ကို ဆက်ထည့်နိုင်သည်။

### 2. Block များကို တစ်ခုထက်ပို၍ ပေါင်းစပ်ခြင်း

ယခု library ၏ အဓိကပြင်ဆင်ချက်မှာ block တစ်ခုချင်းစီသည် single monolithic action မဖြစ်ဘဲ composeable ဖြစ်ခြင်း ဖြစ်သည်။ အောက်ပါပုံစံဖြင့် block များစွာကို တစ်နေရာတည်းတွင် ဆက်တိုက်ထည့်နိုင်သည်။

```text
UniversalDialog_Create(dialog_main, MainActivity, dialog_profile)
UniversalDialog_Set_Cancelable(dialog_main, true)
UniversalDialog_Set_Title(dialog_main, "Profile")
UniversalDialog_Set_Message(dialog_main, "သင့် Profile ကို ပြင်မည်လား")
UniversalDialog_Set_Positive(dialog_main, "ပြင်မည်")
UniversalDialog_Set_Negative(dialog_main, "မပြင်သေးပါ")
UniversalDialog_Show(dialog_main)
```

Block တစ်ခုချင်းစီတွင် local variable များ မထပ်စေရန် `_ud_` prefix ကို အသုံးပြုထားသည်။ Button action လိုပါက `UniversalDialog_Set_Positive_Action` ကို သုံးပြီး Action Code ကို caller က သတ်မှတ်ပါ။ ထိုနည်းဖြင့် Exit Activity, database save, API request စသည့် app-specific logic ကို library ထဲ မပုံသေချိတ်ထားပါ။

### 3. Button event နှင့် Action Code

မူရင်း `OnDismiss` သည် custom listener block ဖြစ်ပြီး Dialog event ကို ပုံသေချိတ်ထားသည်။ ပြန်လည်တည်ဆောက်ထားသော library တွင် button action ကို explicit input အဖြစ်ပေးနိုင်သည်။

```text
UniversalDialog_Set_Positive_Action(
    dialog_main,
    "OK",
    set result variable to "confirmed"
)
```

Action Code ထဲတွင် Sketchware block မှ ထုတ်ပေးနိုင်သော code သို့မဟုတ် သင့် version တွင် ခွင့်ပြုထားသော action ကိုသာ ထည့်ပါ။ External Activity သို့ navigation လိုပါက Activity picker သို့မဟုတ် built-in Intent workflow ကို သီးခြားသုံးပါ။ Universal Dialog block တစ်ခုထဲတွင် Activity name ကို ပုံသေမထည့်ပါနှင့်။

### 4. Custom View အသုံးပြုခြင်း

`UniversalDialog_Find_View_By_ID` သည် Dialog ၏ custom content view ထဲမှ View ကိုရှာရန် ဖြစ်သည်။ Output variable သည် သီးခြား local variable name ဖြစ်ရမည်။

```text
UniversalDialog_Find_View_By_ID(dialog_main, txt_name, name_view)
```

သင့် Sketchware version က typed variable declaration လိုအပ်ပါက built-in View variable အဖြစ် `name_view` ကို အရင်ဖန်တီးထားပါ။ ID သည် Resource/XML ထဲတွင် တကယ်ရှိရမည်။ Missing ID ဖြစ်လျှင် null ရနိုင်သဖြင့် အသုံးမပြုမီ null check လုပ်ပါ။

## Safe Dialog Rules

Dialog ကို show မလုပ်မီ title/message input များကို null/empty စစ်ပါ။ `UniversalDialog_Show_If_Hidden` နှင့် `UniversalDialog_Dismiss_If_Showing` ကို အသုံးပြုခြင်းဖြင့် repeated click နှင့် repeated dismiss ကြောင့် ဖြစ်နိုင်သော state error များကို လျော့ချနိုင်သည်။ Dialog ကို Activity ပိတ်ပြီးနောက် show မလုပ်ရပါ။

```text
if message is not empty
    UniversalDialog_Set_Message(dialog_main, message)
    UniversalDialog_Show_If_Hidden(dialog_main)
```

Progress dialog, network loading နှင့် async task loading အတွက် Dialog block သည် UI shell အဖြစ်သာ အသုံးပြုပါ။ Actual worker, retry, completion နှင့် error logic ကို Request Network/Async Task event များထဲတွင် သီးခြားထားပါ။

## App Information နှင့် Security Helpers

`AppInfo_Get_Package` နှင့် `AppInfo_Get_App_Name` သည် diagnostic UI အတွက် သုံးနိုင်သည်။ Version name/code သည် assignment block ဖြင့် variable ထဲသို့ သိမ်းရမည်။ Client-side package/version/debug check သည် reverse engineering ကို မတားနိုင်သောကြောင့် premium access, authorization, anti-fraud နှင့် sensitive decision များကို server-side တွင် အတည်ပြုပါ။

`Security_Show_Warning` သည် warning UI အတွက်သာ ဖြစ်သည်။ မူရင်းဖိုင်ကဲ့သို့ `killProcess()` သို့မဟုတ် `System.exit(1)` မသုံးပါ။ User ကို ပြဿနာရှင်းရန်၊ update လုပ်ရန် သို့မဟုတ် retry လုပ်ရန် ရွေးချယ်ခွင့်ပေးခြင်းသည် Android lifecycle နှင့် ပိုမိုကိုက်ညီသည်။

## Import နှင့် Build Checklist

| စစ်ဆေးချက် | လုပ်ဆောင်ရန် |
|---|---|
| JSON import | `universal_dialog_blocks_fixed.json` ကို Block Manager ထဲ import လုပ်ပါ |
| Palette | Imported blocks တွင် palette value သည် empty ဖြစ်ကြောင်း စစ်ပါ |
| Component selector | Dialog/Activity/Layout/View input များကို dropdown မှ သင့် project item ဖြင့် ရွေးပါ |
| Resource ID | Custom layout နှင့် View ID များ တကယ်ရှိကြောင်း စစ်ပါ |
| Multiple blocks | Block နှစ်ခုထက်ပို ဆက်ထည့်ပြီး duplicate local variable error မရှိကြောင်း build စမ်းပါ |
| Event | Dialog button/dismiss logic ကို project event/action ထဲတွင်ထားပါ |
| Context | Window UI အတွက် live Activity context သုံးပါ |
| Null safety | Dialog, message, custom view နှင့် View lookup များကို null/empty စစ်ပါ |
| Security | Client-side check ကို authorization အဖြစ် မယုံကြည်ပါနှင့် |
| Clean build | Import ပြီးနောက် clean/rebuild လုပ်ပြီး Logcat ၏ ပထမဆုံး meaningful error line ကို စစ်ပါ |

## Compatibility Note

Sketchware Pro version အလိုက် Custom Block JSON schema, `%m.activity` selector rendering, output variable type, Dialog method signature နှင့် custom layout parameter mapping ကွာနိုင်သည်။ ဤပြန်လည်တည်ဆောက်မှုသည် JSON structure, naming, composition နှင့် source-level safety ကို ပြင်ဆင်ထားခြင်းဖြစ်ပြီး installed Sketchware version အားလုံးတွင် compile အာမခံချက် မဟုတ်ပါ။ Compile error ဖြစ်ပါက ပထမဆုံး built-in Dialog block တစ်ခု၏ generated source code နှင့် တိုက်စစ်ပြီး parameter order နှင့် supported method ကို version အလိုက် ပြန်ညှိပါ။

## Final Result

မူရင်းဖိုင်ကို မပြင်ဘဲ backup အဖြစ် ထားရှိပြီး `universal_dialog_blocks_fixed.json` ကိုသာ import စမ်းသပ်ပါ။ ပြန်လည်တည်ဆောက်ထားသော library သည် Dialog တစ်ခုထဲတွင် block တစ်ခုသာ ထည့်နိုင်သော limitation ကို မသတ်မှတ်ထားဘဲ block များကို အဆင့်လိုက်ပေါင်းစပ်အသုံးပြုနိုင်ရန် ရည်ရွယ်ထားသည်။
