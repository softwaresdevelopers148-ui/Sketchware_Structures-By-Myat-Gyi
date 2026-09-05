# Sketchware ProgressDialog Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware ProgressDialog component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.progressdialog` selector ကို အသုံးပြုထားသောကြောင့် ProgressDialog component ID, Activity ID နှင့် View ID များကို fixed မလုပ်ထားပါ။ Project ထဲရှိ မည်သည့် ProgressDialog component မဆို dropdown မှ ရွေးချယ်အသုံးပြုနိုင်သည်။

ProgressDialog သည် လုပ်ငန်းစဉ်တစ်ခု လုပ်ဆောင်နေချိန်တွင် user ကို အခြေအနေပြရန် အသုံးပြုနိုင်သော modal progress dialog ဖြစ်သည်။ သို့သော် Android API 26 မှစ၍ ProgressDialog ကို deprecated လုပ်ထားပြီး ခေတ်မီ App များတွင် in-layout ProgressBar သို့မဟုတ် အခြား non-modal progress indicator ကို ဦးစားပေးသင့်သည်။ [1]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **36 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Show, dismiss, showing status, title, message, cancel behavior |
| Progress | Max, current progress, increment, percentage, get values |
| Mode | Indeterminate/determinate mode နှင့် horizontal/spinner style |
| Safe | Show-if-hidden, dismiss-if-showing, clamped progress, reset |
| Professional | Start operation, finish-and-dismiss, lifecycle dismiss, progress message update |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `progressdialog_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ ProgressDialog palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် ProgressDialog component တစ်ခုရှိရမည်။ Block တစ်ခုချင်းစီ၏ `%m.progressdialog` dropdown မှ မိမိအသုံးပြုလိုသော component ကို ရွေးပါ။

## အခြေခံ Show/Dismiss Workflow

လုပ်ငန်းစဉ်စတင်ချိန်တွင် dialog ကို ပြပြီး အလုပ်ပြီးဆုံးချိန် သို့မဟုတ် error ဖြစ်ချိန်တွင် ပိတ်ပါ။

```text
Before network/file operation
    ProgressDialog_Set_Message
        progressdialog = progress_dialog
        message = "လုပ်ဆောင်နေပါသည်..."
    ProgressDialog_Show_If_Hidden
        progressdialog = progress_dialog

On success
    ProgressDialog_Dismiss_If_Showing(progress_dialog)

On error
    ProgressDialog_Dismiss_If_Showing(progress_dialog)
    show error message
```

`Show If Hidden` နှင့် `Dismiss If Showing` ကို အသုံးပြုခြင်းဖြင့် dialog ကို ထပ်ခါတလဲလဲ show/dismiss မလုပ်မိစေရန် ကာကွယ်နိုင်သည်။

## Message နှင့် Title

```text
ProgressDialog_Set_Title
    progressdialog = progress_dialog
    title = "ဖိုင်တင်နေပါသည်"

ProgressDialog_Set_Message
    progressdialog = progress_dialog
    message = "ခဏစောင့်ပေးပါ..."
```

ProgressDialog ပြသနေစဉ် Message ပြောင်းလိုပါက `ProgressDialog_Set_Message_If_Visible` ကို သုံးပါ။ Download, upload, sync နှင့် API request များတွင် လက်ရှိအခြေအနေကို message အဖြစ် ပြသနိုင်သည်။

## Indeterminate Progress

အလုပ်၏ စုစုပေါင်းအရွယ်အစား သို့မဟုတ် ပြီးစီးမှုကို မသိသောအခါ indeterminate mode ကို သုံးပါ။ ဥပမာ server response စောင့်ခြင်း၊ login စစ်ဆေးခြင်း သို့မဟုတ် unknown-duration operation ဖြစ်သည်။

```text
ProgressDialog_Start_Indeterminate
    progressdialog = progress_dialog
```

ဤ block သည် Indeterminate mode ဖွင့်ပြီး dialog မပြသသေးပါက ပြသပေးသည်။ အလုပ်ပြီးဆုံးသောအခါ `ProgressDialog_Dismiss_If_Showing` ကို မဖြစ်မနေ ခေါ်ပါ။

## Determinate Progress

Download/upload အရွယ်အစားကို သိရှိပြီး completed value ကို တွက်နိုင်သောအခါ horizontal determinate progress ကို သုံးပါ။

```text
ProgressDialog_Start_Determinate
    progressdialog = progress_dialog
    max = total_bytes
    progress = downloaded_bytes
```

`max` သည် 1 ထက် မနည်းအောင် ပြင်ဆင်ထားပြီး progress သည် 0 မှ max အတွင်း clamp လုပ်ထားသည်။

```text
ProgressDialog_Set_Progress_Safely
    progressdialog = progress_dialog
    progress = current_value

ProgressDialog_Set_Percent
    progressdialog = progress_dialog
    percent = 65
```

Percentage block သည် input ကို 0 မှ 100 အတွင်း ကန့်သတ်ပြီး dialog ၏ max value နှင့် တွက်ချက်ပေးသည်။ User input သို့မဟုတ် network data မှ progress ရရှိပါက safe block ကို ဦးစားပေးသုံးပါ။

## Increment Progress

တစ်ဆင့်ချင်း လုပ်ဆောင်မှုများတွင် increment blocks ကို သုံးနိုင်သည်။

```text
ProgressDialog_Increment_Safely
    progressdialog = progress_dialog
    amount = 1
```

Progress သည် max ထက် မကျော်စေရန် block အတွင်း clamp လုပ်ထားသည်။ Negative increment သုံးပါက progress ပြန်လျော့နိုင်သော်လည်း 0 အောက် မဆင်းပါ။

## Style နှင့် Mode

| Block | အသုံးပြုမှု |
|---|---|
| Set Horizontal | Progress value ကို bar အဖြစ် ပြသရန် |
| Set Spinner | လုပ်ဆောင်နေကြောင်းသာ ပြရန် |
| Set Indeterminate true | ပြီးစီးမှုမသိသော operation |
| Set Indeterminate false | Current/max progress အသုံးပြုသော operation |
| Is Indeterminate | လက်ရှိ mode စစ်ရန် |
| Get Progress/Get Max | UI သို့မဟုတ် logic အတွက် value ပြန်ယူရန် |

Spinner style သည် အများအားဖြင့် indeterminate ဖြစ်ပြီး progress value ကို မပြသနိုင်သော implementation ရှိနိုင်သည်။ Determinate percentage ပြလိုပါက Horizontal style ကို သုံးပါ။

## Operation Workflow

API request, file upload နှင့် download များအတွက် dialog state ကို success, error နှင့် cancel path အားလုံးတွင် ပိတ်ရမည်။

```text
Button clicked
    disable button
    ProgressDialog_Start_Indeterminate(progress_dialog)
    start async operation

Operation success
    ProgressDialog_Finish_And_Dismiss(progress_dialog)
    enable button

Operation error
    ProgressDialog_Dismiss_If_Showing(progress_dialog)
    enable button
    show error

Screen leaving
    ProgressDialog_Dismiss_On_Lifecycle(progress_dialog)
```

`ProgressDialog_Finish_And_Dismiss` သည် progress ကို max သို့ သတ်မှတ်ပြီး dialog ပြသနေပါက ပိတ်ပေးသည်။ အလုပ်မပြီးသေးမီ finish block ကို မခေါ်ပါနှင့်။

## Cancelable နှင့် User Experience

`Set Cancelable false` သည် user က Back ခလုတ်နှိပ်ရုံဖြင့် dialog မပိတ်နိုင်စေရန် အသုံးဝင်သော်လည်း user ကို ပိတ်မရသော modal ထဲတွင် အကြာကြီး မထားပါနှင့်။ Network timeout, cancellation action သို့မဟုတ် error fallback ထည့်ပါ။

```text
ProgressDialog_Set_Cancelable
    progressdialog = progress_dialog
    cancelable = false
```

User က dialog အပြင်ဘက်နှိပ်ခြင်းဖြင့် ပိတ်နိုင်/မနိုင်ကို `Set Canceled On Touch Outside` ဖြင့် သီးခြားသတ်မှတ်နိုင်သည်။ UI သည် ပိတ်သွားသော်လည်း underlying operation ဆက်လုပ်နေပါက state mismatch ဖြစ်နိုင်သောကြောင့် operation cancellation logic နှင့် dialog cancellation behavior ကို တစ်ပြိုင်တည်း ဒီဇိုင်းဆွဲပါ။

## Lifecycle Safety

Activity ပိတ်ခြင်း၊ orientation ပြောင်းခြင်း သို့မဟုတ် screen ပြောင်းခြင်းအချိန်တွင် window မရှိတော့သော dialog ကို update/show လုပ်ပါက crash သို့မဟုတ် window leak ဖြစ်နိုင်သည်။ Screen lifecycle ပြောင်းသောအခါ `ProgressDialog_Dismiss_On_Lifecycle` ကို သုံးပြီး asynchronous callback ပြန်လာချိန်တွင် dialog မရှိတော့ကြောင်း စစ်ပါ။

Dialog သည် operation ကို ကိုင်တွယ်သည့် တစ်ခုတည်းသော state မဖြစ်သင့်ပါ။ `is_loading` Boolean သို့မဟုတ် request state variable ဖြင့် operation state ကို သီးခြားထိန်းသိမ်းပြီး dialog သည် ထို state ၏ UI representation အဖြစ်သာ အသုံးပြုပါ။

## Deprecated API နှင့် Modern Alternative

Android official documentation အရ ProgressDialog သည် API 26 မှစ၍ deprecated ဖြစ်ပြီး progress indicator ကို App layout ထဲတွင် ထည့်သွင်းအသုံးပြုရန် အကြံပြုထားသည်။ [1] Existing Sketchware component သို့မဟုတ် legacy project ကို ထိန်းသိမ်းရသောအခါ ဤ library သည် အသုံးဝင်နိုင်သော်လည်း project အသစ်များတွင် ProgressBar, custom loading layout သို့မဟုတ် notification progress ကို စဉ်းစားပါ။

`ProgressDialog` ကို ဆက်သုံးရမည်ဆိုပါက operation အချိန်တိုတို၊ message ရှင်းလင်းမှု၊ error dismissal နှင့် lifecycle safety ကို အထူးဂရုစိုက်ပါ။ Long-running background work ကို dialog ဖွင့်ထားခြင်းတစ်ခုတည်းဖြင့် မစီမံပါနှင့်။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.progressdialog` selector သုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, View ID နှင့် component ID မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$b` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | Message, max, progress, mode နှင့် cancel behavior များကို input အဖြစ် လက်ခံထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Show If Hidden | Dialog တစ်ခုပဲ ပြသသည် |
| Dismiss If Showing | ပြသနေမှသာ error မရှိဘဲ ပိတ်သည် |
| Indeterminate operation | Spinner/loading indicator ပြသည် |
| Determinate operation | Progress သည် 0 မှ max အတွင်း update သည် |
| Progress > max | Max ထက် မကျော်ပါ |
| Progress < 0 | 0 အောက် မဆင်းပါ |
| Success path | Progress finish ပြီး dialog ပိတ်သည် |
| Error path | Dialog ပိတ်ပြီး error ပြသည် |
| Back/outside cancel | သတ်မှတ်ထားသော cancel behavior အတိုင်း အလုပ်လုပ်သည် |
| Screen leaving | Window leak မဖြစ်စေရန် dialog ပိတ်သည် |
| API 26+ | Deprecated API သုံးနေကြောင်း စီမံပြီး modern alternative စဉ်းစားထားသည် |

## Compatibility Note

Sketchware Pro version အလိုက် ProgressDialog component ၏ generated method name၊ default style နှင့် block label ကွာနိုင်ပါသည်။ ဤ library သည် `show`, `dismiss`, `isShowing`, `setMessage`, `setTitle`, `setCancelable`, `setCanceledOnTouchOutside`, `setMax`, `setProgress`, `incrementProgressBy`, `getProgress`, `getMax`, `setIndeterminate`, `isIndeterminate` နှင့် `setProgressStyle` API များကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in ProgressDialog block နှင့် generated source code ကို တိုက်စစ်ပါ။

## References

[1] Android Developers, “ProgressDialog API reference,” https://developer.android.com/reference/android/app/ProgressDialog  
[2] Android Developers, “Dialogs,” https://developer.android.com/develop/ui/views/components/dialogs  
[3] Sketchware Pro, “Components,” https://docs.sketchware.pro/docs/course/basics/component/
