# Sketchware DatePicker Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware Date Picker component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.datepicker` နှင့် `%m.location` မဟုတ်သော DatePicker-specific input များကို အသုံးပြုထားသောကြောင့် DatePicker component ID, Activity ID နှင့် View ID များကို fixed မလုပ်ထားပါ။

DatePicker component သည် user ကို year, month, day ရွေးချယ်စေပြီး `onDateSet` event တွင် ရွေးချယ်ထားသော date ကို ပြန်ပေးသည်။ Android ၏ `onDateSet`/`updateDate` API တွင် month သည် Java Calendar compatibility အတွက် **0 မှ 11** ဖြစ်ပြီး day သည် **1 မှ 31** ဖြစ်သည်။ ဤ library ၏ user-facing block များတွင် Month ကို 1 မှ 12 အဖြစ် လက်ခံပြီး code အတွင်း Java month သို့ အလိုအလျောက် ပြောင်းပေးထားသည်။ [1] [2]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **45 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Show, dismiss, showing status, selected date update |
| Limits | Minimum/maximum date, today minimum, range restriction |
| Values | Selected year, month, day နှင့် Java month |
| Conversion | Epoch milliseconds, today, date format, YYYY-MM-DD |
| Compare | Days between, before/after, same day, range, leap year |
| Safe | Valid date, null/showing checks, safe update, epoch selection |
| Professional | `onDateSet` conversion/formatting နှင့် lifecycle workflow |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `datepicker_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ DatePicker palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် Date Picker component တစ်ခုရှိရမည်။ Block တစ်ခုချင်းစီ၏ `%m.datepicker` dropdown မှ မိမိအသုံးပြုလိုသော component ကို ရွေးပါ။

## အခြေခံ Date Picker Workflow

```text
Button clicked
    DatePicker_Update_Date
        datepicker = date_picker
        year = 2026
        month = 8
        day = 28
    DatePicker_Show_If_Hidden
        datepicker = date_picker

DatePicker → onDateSet
    use year, month, day from the event
    display or save the selected date
```

`DatePicker_Update_Date` တွင် month ကို 1 မှ 12 အဖြစ် ထည့်ပါ။ January သည် 1၊ December သည် 12 ဖြစ်သည်။ Java month ကို တိုက်ရိုက်အသုံးပြုလိုပါက `DatePicker_Update_Date_Java_Month` ကို သုံးပြီး January = 0၊ December = 11 အဖြစ် ထည့်ပါ။

## `onDateSet` Event Workflow

User က date ရွေးပြီး Confirm လုပ်သောအခါ DatePicker component ၏ `onDateSet` event ကို သုံးပါ။ Event ထဲရှိ `year`, `month`, `day` parameter များသည် သင့် Sketchware version အတိုင်း အမည်ကွာနိုင်သော်လည်း month သည် ပုံမှန်အားဖြင့် Java 0-based ဖြစ်သည်။

```text
DatePicker → onDateSet
    selected_month = DatePicker_OnDateSet_Month_1_Based(month)
    selected_text = DatePicker_OnDateSet_Format(
        year,
        month,
        day,
        "dd/MM/yyyy"
    )
    set selected_text into txt_date
```

`DatePicker_OnDateSet_Format` သို့ event ထဲမှ Java month ကို တိုက်ရိုက်ပေးပါ။ ထို block သည် Java month 0–11 ကို `GregorianCalendar` ထဲသို့ ထည့်ပြီး format ပြောင်းပေးသည်။ Event month ကို 1 ဖြင့် ကြိုတင်မပေါင်းပါနှင့်။

ရွေးချယ်ထားသော date ကို database/API တွင် သိမ်းလိုပါက epoch milliseconds အဖြစ် ပြောင်းနိုင်သည်။

```text
selected_epoch = DatePicker_OnDateSet_To_Epoch(year, month, day)
save selected_epoch
```

## Show နှင့် Dismiss

```text
DatePicker_Show
    datepicker = date_picker

DatePicker_Dismiss_If_Showing
    datepicker = date_picker
```

`Show If Hidden` နှင့် `Dismiss If Showing` ကို အသုံးပြုခြင်းဖြင့် dialog ကို ထပ်ခါတလဲလဲ show/dismiss မလုပ်မိစေရန် ကာကွယ်နိုင်သည်။ Date ရွေးပြီးနောက် dialog ကို ပိတ်လိုပါက `DatePicker_Dismiss_After_Set` ကို `onDateSet` event အတွင်းတွင် သုံးပါ။

## Minimum နှင့် Maximum Date

Date ရွေးချယ်မှုကို သတ်မှတ်ထားသော date range အတွင်းတွင်သာ ခွင့်ပြုလိုပါက min/max date block များကို သုံးပါ။ ဤ blocks များသည် epoch milliseconds ကို လက်ခံသည်။

```text
min_epoch = DatePicker_To_Epoch_Millis(2026, 1, 1)
max_epoch = DatePicker_To_Epoch_Millis(2026, 12, 31)

DatePicker_Set_Min_Max_Range
    datepicker = date_picker
    min_epoch = min_epoch
    max_epoch = max_epoch
```

ယနေ့မတိုင်မီ date များကို မရွေးစေလိုပါက `DatePicker_Set_Min_Today` ကို သုံးနိုင်သည်။ အနာဂတ် date များအတွက် maximum date ကို သီးခြားသတ်မှတ်ပြီး current date, expiry date, booking window စသည့် business rule များနှင့် တွဲသုံးပါ။

## Selected Date တန်ဖိုးများ

DatePicker ပြသနေစဉ် လက်ရှိရွေးထားသော တန်ဖိုးများကို အောက်ပါ blocks များဖြင့် ရယူနိုင်သည်။

| Block | ပြန်ပေးသည့်တန်ဖိုး |
|---|---|
| Get Selected Year | ဥပမာ 2026 |
| Get Selected Month | User-facing 1–12 |
| Get Selected Java Month | Java/Calendar 0–11 |
| Get Selected Day | 1–31 |

Java month နှင့် user-facing month ကို မရောထွေးပါနှင့်။ Calendar component နှင့် DatePicker event ကြား data ကူးပြောင်းရာတွင် `month + 1` သို့မဟုတ် `month - 1` လိုအပ်နိုင်သောကြောင့် သက်ဆိုင်ရာ block ကိုသာ သုံးပါ။

## Date Format နှင့် Epoch

`DatePicker_Format_Epoch` သည် epoch milliseconds ကို format string ဖြင့် date စာသားပြောင်းသည်။ ဥပမာ `dd/MM/yyyy`, `yyyy-MM-dd` သို့မဟုတ် `EEE, dd MMM yyyy` ကို အသုံးပြုနိုင်သည်။

```text
formatted = DatePicker_Format_Epoch(epoch_value, "dd/MM/yyyy")
```

`DatePicker_Default_Format` သည် year, user month 1–12 နှင့် day ကို `yyyy-MM-dd` အဖြစ် ပြောင်းသည်။ API/database တွင် date သိမ်းရန် timezone နှင့် date-only semantics ကို သေချာသတ်မှတ်ပါ။ Midnight epoch ကို timezone မတူသော server သို့ ပို့လျှင် ရက်ပြောင်းသွားနိုင်သည်။

## Date Validation

User input သို့မဟုတ် imported data မှ year/month/day ရရှိပါက `DatePicker_Is_Valid_Date` ဖြင့် အရင်စစ်ပါ။ February 30 သို့မဟုတ် month 13 ကဲ့သို့ မမှန်ကန်သော date များကို မသိမ်းပါနှင့်။

```text
if DatePicker_Is_Valid_Date(year, month, day)
    DatePicker_Update_Date(date_picker, year, month, day)
else
    show invalid-date message
```

`DatePicker_Is_Leap_Year` ကို February date calculation သို့မဟုတ် year-based business rule များတွင် သုံးနိုင်သည်။

## Date Comparison နှင့် Range

Epoch milliseconds နှစ်ခုကို `DatePicker_Is_Before`, `DatePicker_Is_After`, `DatePicker_Is_Same_Day` နှင့် `DatePicker_Is_In_Range` ဖြင့် နှိုင်းယှဉ်နိုင်သည်။ `DatePicker_Days_Between` သည် milliseconds အကွာအဝေးကို 24-hour day အဖြစ် တွက်သည်။ Daylight saving/timezone ပါသော business calendar များတွင် local-date calculation ကို သီးခြားစီမံပါ။

```text
start_epoch = DatePicker_To_Epoch_Millis(2026, 8, 1)
end_epoch = DatePicker_To_Epoch_Millis(2026, 8, 31)
selected_epoch = DatePicker_OnDateSet_To_Epoch(year, month, day)

if DatePicker_Is_In_Range(selected_epoch, start_epoch, end_epoch)
    accept selected date
else
    show out-of-range message
```

## Professional `onDateSet` Pattern

Date ရွေးပြီးနောက် UI, local storage နှင့် backend request သုံးခုစလုံးကို တစ်နေရာတည်းတွင် စီမံပါက duplicate update ဖြစ်နိုင်သောကြောင့် selected date ကို variable တစ်ခုအဖြစ် အရင် normalize လုပ်ပါ။

```text
DatePicker → onDateSet
    selected_month_1_based = month + 1
    selected_epoch = DatePicker_OnDateSet_To_Epoch(year, month, day)
    selected_text = DatePicker_OnDateSet_Format(year, month, day, "yyyy-MM-dd")
    save selected_epoch
    update date label with selected_text
    send normalized date to API if needed
```

Server သို့ ပို့သည့် date format ကို `yyyy-MM-dd` ကဲ့သို့ တိကျသော contract တစ်ခု သတ်မှတ်ပြီး device locale-dependent string ကို မပို့ပါနှင့်။ Display အတွက် locale-aware format နှင့် storage/API အတွက် fixed format ကို သီးခြားထားပါ။

## Lifecycle နှင့် Modern Design

Android official guidance သည် date picker ကို lifecycle-managed dialog container ဖြင့် စီမံရန် အကြံပြုသည်။ Legacy Sketchware DatePicker component ကို သုံးသောအခါ Activity ပိတ်ခြင်း သို့မဟုတ် screen ပြောင်းခြင်းမတိုင်မီ dialog ကို dismiss လုပ်ပြီး asynchronous callback များတွင် Activity state ကို စစ်ပါ။ [3]

Date Picker သည် user interaction အတွက်သာ ဖြစ်ပြီး background date tracking သို့မဟုတ် long-running operation ကို ကိုယ်စားမပြုပါ။ User ရွေးချယ်မှုမရမီ default date သတ်မှတ်ရန် `DatePicker_Update_Date` ကို သုံးပါ။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.datepicker` selector သုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, View ID နှင့် component ID မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ အသုံးပြုထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$d` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | Year, month, day, epoch, format, min/max နှင့် datepicker များကို input အဖြစ် လက်ခံထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Show/Dismiss | Dialog သည် တစ်ကြိမ်သာ ပေါ်ပြီး မှန်ကန်စွာ ပိတ်သည် |
| `onDateSet` month | Java month 0–11 ကို မှန်ကန်စွာ handle လုပ်သည် |
| January/December | User month 1/12 နှင့် Java month 0/11 မရောထွေးပါ |
| February 29 | Leap year အလိုက်သာ valid ဖြစ်သည် |
| Min/Max | Range အပြင် date မရွေးနိုင်ပါ |
| Empty/invalid input | Invalid date path သို့သွားသည် |
| Epoch format | Display နှင့် API format တိကျသည် |
| Same-day compare | Time difference မကြောင့် false မဖြစ်စေပါ |
| Lifecycle | Screen ထွက်ချိန် dialog leak မဖြစ်ပါ |
| Timezone | Server/device timezone contract မှန်ကန်သည် |

## Compatibility Note

Sketchware Pro version အလိုက် DatePicker component ၏ generated method name, component selector type နှင့် `onDateSet` event parameter name ကွာနိုင်ပါသည်။ ဤ library သည် `show`, `dismiss`, `isShowing`, `updateDate`, `getDatePicker`, `setMinDate`, `setMaxDate`, `getYear`, `getMonth`, `getDayOfMonth` နှင့် Java Calendar/Date formatting API များကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in DatePicker blocks နှင့် generated source code ကို တိုက်စစ်ပြီး method name သို့မဟုတ် selector ကို version-compatible ပြင်ဆင်ပါ။

## References

[1] Android Developers, “DatePickerDialog API reference,” https://developer.android.com/reference/android/app/DatePickerDialog  
[2] Android Developers, “DatePicker API reference,” https://developer.android.com/reference/android/widget/DatePicker  
[3] Android Developers, “Add pickers to your app,” https://developer.android.com/develop/ui/views/components/pickers
