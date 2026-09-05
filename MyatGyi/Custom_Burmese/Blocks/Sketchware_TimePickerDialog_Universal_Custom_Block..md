# Sketchware TimePickerDialog Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware Time Picker Dialog component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.timepickerdialog` selector ကို အသုံးပြုထားသောကြောင့် TimePickerDialog component ID, Activity ID နှင့် View ID များကို fixed မလုပ်ထားပါ။

TimePickerDialog သည် user ကို တစ်နေ့တာအချိန် ရွေးချယ်စေပြီး `onTimeSet` event တွင် hour နှင့် minute ကို ပြန်ပေးသည်။ Android API တွင် hour သည် 24-hour format အရ 0–23 ဖြစ်ပြီး minute သည် 0–59 ဖြစ်သည်။ Dialog ၏ UI ကို 12-hour AM/PM သို့မဟုတ် 24-hour view အဖြစ် component configuration အလိုက် ပြသနိုင်သည်။ [1] [2]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **39 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Show, dismiss, showing status, update time, title, cancel behavior |
| Values | Hour/minute ကို total minutes/seconds အဖြစ် ပြောင်းခြင်း |
| Format | 24-hour `HH:mm` နှင့် 12-hour `hh:mm AM/PM` |
| Safe | Valid time, safe update, show-if-hidden, dismiss-if-showing |
| Compare | Before/after, range, duration နှင့် overnight range |
| Professional | `onTimeSet` normalization, epoch conversion, current time, lifecycle |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `timepickerdialog_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ TimePickerDialog palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် Time Picker Dialog component တစ်ခုရှိရမည်။ Block တစ်ခုချင်းစီ၏ `%m.timepickerdialog` dropdown မှ မိမိအသုံးပြုလိုသော component ကို ရွေးပါ။

## အခြေခံ Show နှင့် Update Workflow

```text
Button clicked
    TimePickerDialog_Update_Time
        timepickerdialog = time_picker_dialog
        hour = 9
        minute = 30
    TimePickerDialog_Show_If_Hidden
        timepickerdialog = time_picker_dialog

TimePickerDialog → onTimeSet
    use hour and minute from the event
    display or save the selected time
```

`TimePickerDialog_Update_Time` တွင် hour ကို 0–23 နှင့် minute ကို 0–59 အဖြစ် ထည့်ပါ။ ဤ block သည် မမှန်ကန်သော input ကို အခြေခံအကန့်အသတ်အတွင်း clamp လုပ်ပေးသည်။ Strict validation လိုအပ်ပါက `TimePickerDialog_Update_Time_Safely` ကို သုံးပါ။

## `onTimeSet` Event Workflow

User က time ရွေးပြီး Confirm လုပ်သောအခါ TimePickerDialog component ၏ `onTimeSet` event ကို သုံးပါ။ Event ထဲရှိ `hourOfDay`/hour နှင့် minute parameter များကို component version အလိုက် အမည်ကွာနိုင်သော်လည်း hour သည် ပုံမှန်အားဖြင့် 24-hour value ဖြစ်သည်။ [1]

```text
TimePickerDialog → onTimeSet
    selected_text = TimePickerDialog_OnTimeSet_Format_24(hour, minute)
    selected_minutes = TimePickerDialog_Get_Total_Minutes(hour, minute)
    update time label with selected_text
    save selected_minutes
```

12-hour display လိုပါက `TimePickerDialog_OnTimeSet_Format_12` ကို သုံးပါ။ Event hour သည် 13–23 ဖြစ်နေချိန်တွင် ကိုယ်တိုင် AM/PM ပြောင်းရန် မလိုပါ။ Block သည် 0–11 ကို AM နှင့် 12–23 ကို PM အဖြစ် မှန်ကန်စွာ ပြောင်းပေးသည်။

## 12-Hour နှင့် 24-Hour Format

| Input hour | 24-hour format | 12-hour format |
|---:|---:|---:|
| 0 | 00:00 | 12:00 AM |
| 9 | 09:00 | 09:00 AM |
| 12 | 12:00 | 12:00 PM |
| 18 | 18:00 | 06:00 PM |
| 23 | 23:00 | 11:00 PM |

`TimePickerDialog_Format_24_Hour` သည် `HH:mm` သို့ format ပြောင်းပြီး `TimePickerDialog_Format_12_Hour` သည် `hh:mm AM/PM` သို့ ပြောင်းသည်။ API/database အတွက် 24-hour format သို့မဟုတ် total minutes ကို သီးခြားသိမ်းပြီး UI display အတွက် locale/12-hour format ကို သီးခြားထုတ်ပါ။

## Total Minutes နှင့် Total Seconds

Time ကို နှိုင်းယှဉ်ခြင်းနှင့် duration တွက်ခြင်းအတွက် hour/minute ကို တစ်နေ့တာ offset အဖြစ် ပြောင်းထားခြင်းက ပိုလွယ်ကူသည်။

```text
start_minutes = TimePickerDialog_Get_Total_Minutes(9, 30)
end_minutes = TimePickerDialog_Get_Total_Minutes(17, 45)
duration = TimePickerDialog_Duration_Minutes(start_minutes, end_minutes)
```

Total minutes range သည် 0–1439 ဖြစ်ပြီး midnight = 0 ဖြစ်သည်။ Total seconds လိုအပ်ပါက hour, minute, second သုံးပြီး `TimePickerDialog_Get_Total_Seconds` ကို သုံးပါ။

Total minutes မှ hour/minute ပြန်ယူလိုပါက `Get Hour From Minutes` နှင့် `Get Minute From Minutes` blocks ကို သုံးပါ။

## Time Validation

User input, server data သို့မဟုတ် Text input မှ hour/minute ရရှိပါက `TimePickerDialog_Is_Valid_Time` ဖြင့် အရင်စစ်ပါ။ Hour သည် 0–23၊ minute သည် 0–59 အတွင်း ဖြစ်ရမည်။

```text
if TimePickerDialog_Is_Valid_Time(hour, minute)
    TimePickerDialog_Update_Time_Safely(time_picker_dialog, hour, minute)
else
    show invalid-time message
```

Invalid input များကို TimePickerDialog သို့ မပေးပါနှင့်။ `Update Time` block သည် input ကို clamp လုပ်သော်လည်း business validation အတွက် strict safe block ကို ဦးစားပေးပါ။

## Time Compare နှင့် Duration

`TimePickerDialog_Is_Before` နှင့် `TimePickerDialog_Is_After` သည် total minutes တန်ဖိုးများကို နှိုင်းယှဉ်သည်။ `TimePickerDialog_Duration_Minutes` သည် midnight ဖြတ်သွားသော schedule ကိုလည်း ကိုင်တွယ်ပြီး result ကို 0–1439 အတွင်း ပြန်ပေးသည်။

```text
start = TimePickerDialog_Get_Total_Minutes(22, 0)
end = TimePickerDialog_Get_Total_Minutes(2, 0)
duration = TimePickerDialog_Duration_Minutes(start, end)
```

အထက်ပါဥပမာတွင် duration သည် နောက်နေ့ 02:00 အထိ 4 နာရီအဖြစ် ရရှိသည်။ Overnight range စစ်ရန် `TimePickerDialog_Is_In_Range` ကို သုံးပါ။ Start သည် End ထက်ကြီးလျှင် block သည် midnight ကို ဖြတ်သည့် range အဖြစ် သတ်မှတ်ပေးသည်။

```text
if TimePickerDialog_Is_In_Range(current_minutes, start_minutes, end_minutes)
    allow operation
else
    show outside-schedule message
```

## Current Time နှင့် Epoch

`TimePickerDialog_Current_Hour` နှင့် `TimePickerDialog_Current_Minute` သည် device ၏ local time ကို ပြန်ပေးသည်။ ယနေ့ရက်စွဲနှင့် time ကို တစ်စုတစ်စည်းတည်း သိမ်းလိုပါက `TimePickerDialog_OnTimeSet_To_Epoch` ကို သုံးနိုင်သည်။ ဤ block သည် ယနေ့ date ၏ hour/minute ကို milliseconds အဖြစ် ပြောင်းသည်။

> Time-only data အတွက် total minutes သည် ပိုရှင်းလင်းနိုင်ပြီး timestamp/appointment အတွက် epoch milliseconds သည် သင့်တော်နိုင်သည်။ Device timezone နှင့် server timezone ကို ကြိုတင်သတ်မှတ်ပါ။

## Safe Dialog Control

Dialog ကို ထပ်ခါတလဲလဲ ပြသခြင်း သို့မဟုတ် မပြသဘဲ dismiss လုပ်ခြင်းကြောင့် ဖြစ်နိုင်သော UI issue များကို ရှောင်ရန် အောက်ပါ blocks ကို သုံးပါ။

```text
TimePickerDialog_Show_If_Hidden(time_picker_dialog)
TimePickerDialog_Dismiss_If_Showing(time_picker_dialog)
```

User က Back ခလုတ် သို့မဟုတ် dialog အပြင်ဘက်နှိပ်သောအခါ ပိတ်နိုင်/မနိုင်ကို `Set Cancelable` နှင့် `Set Outside Cancel` ဖြင့် သတ်မှတ်ပါ။ Scheduling သို့မဟုတ် required time input တွင် cancel path ထည့်ပြီး cancel သွားပါက default value မသိမ်းပါနှင့်။

## Professional Scheduling Workflow

```text
Button clicked
    start_minutes = stored_start_time
    end_minutes = stored_end_time
    TimePickerDialog_Show_If_Hidden(time_picker_dialog)

TimePickerDialog → onTimeSet
    selected_minutes = TimePickerDialog_Get_Total_Minutes(hour, minute)
    if TimePickerDialog_Is_In_Range(selected_minutes, start_minutes, end_minutes)
        selected_text = TimePickerDialog_OnTimeSet_Format_24(hour, minute)
        save selected_minutes
        update UI
        TimePickerDialog_Dismiss_After_Set(time_picker_dialog)
    else
        show schedule warning
```

UI display string, internal total minutes နှင့် API payload တို့ကို တစ်မျိုးတည်းသော data အဖြစ် မရောထွေးပါနှင့်။ Normalized total minutes သို့မဟုတ် ISO-compatible representation ကို source of truth အဖြစ် သိမ်းပါ။

## Lifecycle နှင့် Modern Design

Android official guidance သည် time picker ကို lifecycle-managed dialog container ဖြင့် စီမံရန် အကြံပြုသည်။ Legacy Sketchware TimePickerDialog component ကို သုံးသောအခါ Activity ပိတ်ခြင်း၊ screen ပြောင်းခြင်း သို့မဟုတ် configuration change မတိုင်မီ `TimePickerDialog_Lifecycle_Dismiss` ကို သုံးပြီး callback ပြန်လာချိန်တွင် UI state ကို စစ်ပါ။ [3]

Time picker dialog သည် user input အတွက်သာ ဖြစ်ပြီး long-running background timer မဟုတ်ပါ။ User က time ရွေးပြီးသည်နှင့် dialog ကို ပိတ်ပြီး timer/scheduler logic ကို သီးခြား component သို့မဟုတ် event workflow တွင် စီမံပါ။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.timepickerdialog` selector သုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, View ID နှင့် component ID မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ အသုံးပြုထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$d` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | Hour, minute, duration, format, range နှင့် dialog component များကို input အဖြစ် လက်ခံထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Show/Dismiss | Dialog တစ်ကြိမ်သာ ပေါ်ပြီး မှန်ကန်စွာ ပိတ်သည် |
| `onTimeSet` hour | 0–23 24-hour value ကို မှန်ကန်စွာ သိမ်းသည် |
| Minute | 0–59 အတွင်းသာ လက်ခံသည် |
| Midnight | 00:00 ကို 12:00 AM အဖြစ် format ပြောင်းသည် |
| Noon | 12:00 ကို 12:00 PM အဖြစ် format ပြောင်းသည် |
| Overnight range | 22:00–02:00 range ကို မှန်ကန်စွာ စစ်သည် |
| Invalid input | Strict validation path သို့သွားသည် |
| Epoch | Local date/time နှင့် timezone contract ကို ထိန်းထားသည် |
| Cancel | Cancel path တွင် default value မမှားယွင်းစွာ သိမ်းပါ |
| Lifecycle | Screen ထွက်ချိန် dialog leak မဖြစ်ပါ |

## Compatibility Note

Sketchware Pro version အလိုက် TimePickerDialog component ၏ generated method name, component selector type နှင့် `onTimeSet` event parameter name ကွာနိုင်ပါသည်။ ဤ library သည် `show`, `dismiss`, `isShowing`, `updateTime` နှင့် Java Calendar/Locale formatting API များကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in TimePickerDialog blocks နှင့် generated source code ကို တိုက်စစ်ပြီး method name သို့မဟုတ် selector ကို version-compatible ပြင်ဆင်ပါ။

`onTimeSet` event ကို Custom Block JSON က အလိုအလျောက် ဖန်တီးပေးခြင်းမဟုတ်ပါ။ Dialog ကို ပြရန်နှင့် default time သတ်မှတ်ရန် ဤ library မှ blocks ကို သုံးပြီး selected hour/minute processing နှင့် UI/business logic ကို TimePickerDialog component ၏ `onTimeSet` event အတွင်းတွင် သီးခြားရေးရမည်။

## References

[1] Android Developers, “TimePickerDialog API reference,” https://developer.android.com/reference/android/app/TimePickerDialog  
[2] Android Developers, “TimePicker API reference,” https://developer.android.com/reference/android/widget/TimePicker  
[3] Android Developers, “Add pickers to your app,” https://developer.android.com/develop/ui/views/components/pickers
