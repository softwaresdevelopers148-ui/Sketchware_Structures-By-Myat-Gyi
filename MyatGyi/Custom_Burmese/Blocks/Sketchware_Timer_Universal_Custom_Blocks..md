# Sketchware Timer Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware Timer component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.timer` component selector ကို အသုံးပြုထားသောကြောင့် Timer component ID ကို fixed မလုပ်ထားပါ။ ထို့ကြောင့် `timer1`, `countdown_timer`, `auto_refresh_timer` သို့မဟုတ် အခြား Timer component များနှင့် ပြန်လည်အသုံးပြုနိုင်သည်။

## ပါဝင်သော Block များ

JSON ဖိုင်တွင် Header များအပါအဝင် **41 entries** ပါဝင်သည်။ အသုံးပြုနိုင်သော Timer blocks များကို အောက်ပါအတိုင်း ခွဲထားသည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Interval သတ်မှတ်ခြင်း၊ start, cancel, restart, status စစ်ခြင်း |
| Safe Control | မအလုပ်လုပ်သေးမှ start၊ အလုပ်လုပ်နေမှ cancel၊ valid interval စစ်ခြင်း |
| Time Conversion | milliseconds, seconds, minutes, hours ပြောင်းခြင်း |
| Duration Format | seconds/milliseconds ကို `HH:mm:ss` သို့မဟုတ် `MM:ss` ပြောင်းခြင်း |
| Countdown Variables | Number တိုး/လျှော့၊ reset၊ zero/positive စစ်ခြင်း |
| Professional | Clamp, minimum interval, lifecycle cancel, restart, elapsed/current time |

## Sketchware ထဲသို့ Import လုပ်ခြင်း

1. Sketchware Pro ကိုဖွင့်ပြီး Project တစ်ခုကို ဝင်ပါ။
2. Project menu မှ Developer Tools သို့မဟုတ် Block Manager ကိုဖွင့်ပါ။
3. Custom Block JSON Import ကိုရွေးပြီး `timer_custom_blocks.json` ဖိုင်ကို ရွေးပါ။
4. Import ပြီးနောက် Logic Editor ထဲတွင် Timer blocks palette ကိုရှာပါ။
5. Project ထဲတွင် Timer component တစ်ခုရှိကြောင်း စစ်ပါ။ မရှိသေးပါက View/Component အပိုင်းမှ Timer ထည့်ပါ။
6. Block ထဲရှိ `%m.timer` dropdown မှ မိမိအသုံးပြုမည့် Timer component ကို ရွေးပါ။

## Timer ၏ အခြေခံ Workflow

Timer component သည် ကိုယ်တိုင် logic ရေးသားပြီးနောက် သတ်မှတ်ထားသော interval အတိုင်း Timer event ကို ပြန်ခေါ်ပေးသည်။ ထို့ကြောင့် Timer blocks များသည် Timer ကို configure/start/stop လုပ်ရန် အသုံးပြုပြီး အမှန်တကယ် ပြန်လည်လုပ်ဆောင်မည့် UI logic ကို Timer component ၏ Timer event ထဲတွင် ထည့်ရမည်။

```text
App start / Button click
    set timer interval
    start timer

Timer event
    run repeated logic

Stop button / Activity lifecycle
    cancel timer
```

## Beginner Level အသုံးပြုပုံ

### 1. 3 seconds တစ်ကြိမ် Timer စတင်ခြင်း

```text
Timer_Set_Interval_Seconds
    timer = timer_main
    seconds = 3

Timer_Start
    timer = timer_main
```

ထို့နောက် Timer component ၏ timer event အတွင်း လုပ်ဆောင်လိုသည့် blocks များထည့်ပါ။ ဥပမာ TextView ကို update လုပ်ခြင်း၊ list refresh လုပ်ခြင်း သို့မဟုတ် animation position ပြောင်းခြင်းတို့ ဖြစ်နိုင်သည်။

### 2. Timer ရပ်တန့်ခြင်း

```text
Timer_Cancel
    timer = timer_main
```

Timer ကို ရပ်ပြီး ပြန်စချင်ပါက `Timer_Restart` ကို သုံးနိုင်သည်။ Timer တစ်ခုကို multiple event များထဲမှ မကြာခဏ start လုပ်ခြင်းသည် duplicate scheduling သို့မဟုတ် မမျှော်လင့်သော repeated behavior ဖြစ်စေနိုင်သောကြောင့် Safe blocks များကို ဦးစားပေးသုံးပါ။

### 3. Timer အလုပ်လုပ်/မလုပ် စစ်ခြင်း

```text
if Timer_Is_Enabled(timer_main)
    // Timer အလုပ်လုပ်နေသည်
else
    // Timer ရပ်နေသည်
```

## Intermediate Level: Countdown Timer

Countdown တစ်ခုအတွက် Number variable တစ်ခု ဖန်တီးပြီး Timer interval ကို 1000 milliseconds သတ်မှတ်ပါ။ Timer event တိုင်းတွင် Number ကို 1 လျှော့ပြီး output ပြပါ။ သုညရောက်လျှင် Timer ကို cancel လုပ်ပါ။

```text
Start button clicked
    Timer_Set_Interval
        timer = timer_countdown
        milliseconds = 1000
    Timer_Reset_Number
        variable = remaining_seconds
        number = 60
    Timer_Start_If_Stopped
        timer = timer_countdown

Timer event
    Timer_Decrement_Number
        variable = remaining_seconds
    Timer_Format_Seconds
        seconds = remaining_seconds
    set formatted result into TextView
    if Timer_Number_Is_Zero_Or_Less(remaining_seconds)
        Timer_Cancel_If_Running(timer_countdown)
```

Reset button တွင် Timer ကို cancel လုပ်ပြီး Number variable ကို မူလတန်ဖိုးသို့ ပြန်သတ်မှတ်ရမည်။ UI တွင် `00:59` ကဲ့သို့ ပြရန် `Timer_Format_Seconds` သည် သင့်တော်သည်။

## Time Unit ပြောင်းလဲခြင်း

Timer interval များသည် milliseconds အခြေခံဖြစ်သောကြောင့် user interface တွင် seconds/minutes ဖြင့် တန်ဖိုးလက်ခံပြီး block အတွင်း milliseconds သို့ ပြောင်းလဲနိုင်သည်။

| Block | ရလဒ် |
|---|---|
| Seconds to milliseconds | seconds × 1000 |
| Minutes to milliseconds | minutes × 60000 |
| Hours to milliseconds | hours × 3600000 |
| Milliseconds to seconds | milliseconds ÷ 1000 |
| Current time millis | လက်ရှိ system time |
| Elapsed millis | စတင်တန်ဖိုးမှ ယခုအချိန်အထိ ကြာချိန် |

အလွန်ကြီးသော Number ကို milliseconds သို့ ပြောင်းရာတွင် overflow မဖြစ်စေရန် Number type နှင့် calculation range ကို စစ်ဆေးပါ။

## Duration Format Blocks

`Timer_Format_Seconds` သည် seconds ကို `HH:mm:ss` ပုံစံဖြင့် ပြန်ပေးသည်။ `Timer_Format_Minutes_Seconds` သည် countdown နှင့် stopwatch များအတွက် အသုံးဝင်သော `MM:ss` ပုံစံကို ပြန်ပေးသည်။

```text
Timer_Format_Minutes_Seconds
    seconds = remaining_seconds

// Result example: 04:07
```

ကြာချိန်သည် 60 minutes ကျော်နိုင်ပါက `HH:mm:ss` ကို သုံးပါ။ Negative duration များကို မပြမီ သုညသို့ clamp လုပ်ထားသင့်သည်။

## Professional Safe Control

`Timer_Start_If_Stopped` သည် Timer အလုပ်မလုပ်သေးမှသာ start လုပ်ပြီး duplicate start လုပ်ခြင်းကို လျှော့ချပေးသည်။ `Timer_Cancel_If_Running` သည် Timer အလုပ်လုပ်နေမှသာ cancel လုပ်သည်။ User input သို့မဟုတ် API မှ ရရှိလာသော interval ကို အသုံးပြုပါက `Timer_Set_And_Start_If_Valid` ကို သုံးပြီး interval သည် 0 ထက်ကြီးကြောင်း စစ်ပါ။

`Timer_Clamp_Number` သည် Number variable ကို minimum နှင့် maximum အကြားတွင် ထိန်းပေးသည်။ ဥပမာ animation interval ကို 16 ms ထက် မနည်းစေလိုပါက minimum 16 ဟု သတ်မှတ်နိုင်သည်။ သို့သော် အလွန်တိုသော interval သည် CPU/battery အသုံးပြုမှုကို တိုးစေနိုင်သောကြောင့် feature လိုအပ်ချက်နှင့်ကိုက်ညီသည့် interval ကိုသာ ရွေးပါ။

## Lifecycle နှင့် Memory Safety

Activity ပိတ်ခြင်း၊ screen ပြောင်းခြင်း သို့မဟုတ် app background သို့ ဝင်ခြင်းမတိုင်မီ Timer ကို cancel လုပ်သင့်သည်။ ဤ package ထဲရှိ `Timer_Cancel_On_Lifecycle` သည် Timer အလုပ်လုပ်နေမှသာ cancel လုပ်သော safe block ဖြစ်သည်။ သင့် Project ၏ `onPause`, `onStop` သို့မဟုတ် `onDestroy` event အတွင်း app design နှင့်ကိုက်ညီသော lifecycle location တွင် ထည့်ပါ။

Timer event အတွင်း heavy file operation, network request, large list processing သို့မဟုတ် အချိန်ကြာသော loop များကို interval တိုင်း ထပ်ခါထပ်ခါ မလုပ်သင့်ပါ။ Timer သည် UI update လုပ်ရန်သာဖြစ်ပါက လိုအပ်သည့် data ကို အရင် cache လုပ်ပြီး UI ကို အနည်းဆုံးပြောင်းလဲမှုဖြင့် update လုပ်ပါ။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal block | `%m.timer` selector သုံးထားသည် |
| Fixed View/Activity မသုံးရ | Fixed view ID, activity ID, `this` မချည်ထားပါ |
| Add source directly မသုံးရ | Custom block `code` နှင့် `imports` ကိုသာ အသုံးပြုထားသည် |
| Palette | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Section separation | Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Natural spec | မြန်မာဘာသာဖြင့် သဘာဝကျသော block spec ရေးထားသည် |
| Parameter positions | `%1$s`, `%2$d` စသည့် position symbols များကို မှန်ကန်စွာသုံးထားသည် |
| Reusable | Component ID သို့မဟုတ် App-specific variable ကို code ထဲ hard-code မလုပ်ထားပါ |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| 1000 ms interval + start | Timer event သည် တစ်စက္ကန့်ခြား ဖြစ်ပေါ်သည် |
| Cancel | Timer event ရပ်သွားသည် |
| Restart | Timer သည် interval အသစ်ဖြင့် ပြန်စသည် |
| Start If Stopped | ထပ်ခါတလဲလဲ start မဖြစ်စေပါ |
| Interval 0 သို့မဟုတ် negative | Safe block သည် start မလုပ်ပါ |
| Countdown reaches zero | Timer cancel ဖြစ်ပြီး zero state ပြသည် |
| Activity exit | Lifecycle cancel ပြီးနောက် event မဆက်ဖြစ်သင့်ပါ |
| Multiple Timer components | Component dropdown ဖြင့် သီးခြားထိန်းချုပ်နိုင်သည် |

## အရေးကြီးသော သတိပြုရန်

Timer component ၏ actual method name သည် Sketchware Pro version အလိုက် ကွာနိုင်ပါသည်။ ဤ library သည် `start()`, `cancel()`, `setInterval(...)` နှင့် `isEnabled()` API များကို အခြေခံထားသည်။ Import ပြီးနောက် compile error ဖြစ်ပါက သင့် Sketchware version တွင် Timer component ၏ generated source code သို့မဟုတ် built-in Timer blocks မှ method name ကို စစ်ပြီး version-compatible ပြင်ဆင်ရန်လိုသည်။

Timer block သည် event အတွင်း ထည့်ရမည့် UI logic ကို အစားထိုးခြင်းမဟုတ်ပါ။ Timer ကို စတင်/ရပ်တန့်ရန် ဤ library ကို သုံးပြီး Timer component event ထဲတွင် countdown, refresh, animation သို့မဟုတ် polling logic ကို သီးခြားရေးပါ။

## References

[1] Sketchyas, “Learn Sketchware Pro Timer Component,” https://sketchyas.com/sketchware/components/learn-timer  
[2] Sketchware Docs Unofficial, “Timer,” https://sketchwaredocs.gitbook.io/home/components/timer  
[3] Sketchware Pro Docs, “Creating a Custom Block,” https://docs.sketchware.pro/docs/blocks/custom%20block/creating-block/
