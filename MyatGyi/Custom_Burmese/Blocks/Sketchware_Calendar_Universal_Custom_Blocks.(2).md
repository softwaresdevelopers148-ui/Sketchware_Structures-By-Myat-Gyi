# Sketchware Calendar Universal Custom Blocks

ဤ package သည် user ပေးထားသော **Sketchware Pro Custom Block & JSON Specification / Master Rules** ကို အခြေခံပြီး Calendar component အတွက် ဖန်တီးထားသော Universal Custom Block library ဖြစ်သည်။ App တစ်ခု၊ Activity တစ်ခု သို့မဟုတ် View တစ်ခုအတွက် ပုံသေချည်ထားခြင်း မရှိဘဲ `%m.calendar` ဖြင့် Calendar component selector ကို အသုံးပြုထားသောကြောင့် Calendar component အမျိုးမျိုးနှင့် ပြန်လည်အသုံးပြုနိုင်သည်။

## ထည့်သွင်းထားသော Block အရေအတွက်

JSON ဖိုင်တွင် Header များအပါအဝင် **46 entries** ပါဝင်ပြီး Header မဟုတ်သော usable blocks များကို အောက်ပါအဆင့်များအဖြစ် ခွဲထားသည်။

| အဆင့် | အဓိကလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Clear, current time, milliseconds, timezone |
| Date Setup | Date/time သတ်မှတ်ခြင်း၊ နေ့စတင်/ဆုံးချိန် |
| Read | Year, month, day, weekday, hour, minute, second, week, days-in-month |
| Arithmetic | Years, months, days, hours, minutes, seconds ပေါင်းခြင်း |
| Compare | Before, after, same day, date difference, leap year |
| Format/Parse | Format string, ISO date, string မှ Calendar ထဲ parse လုပ်ခြင်း |
| Professional | First day of week, minimal days, UTC offset, sync, weekend, day-of-year |

## Import လုပ်ပြီးနောက် သုံးရမည့် Component

Sketchware Project ထဲတွင် Calendar component တစ်ခု ထည့်ပြီး component ID တစ်ခု သတ်မှတ်ပါ။ ဥပမာ `calendar_main` ဖြစ်နိုင်သော်လည်း JSON block များသည် ထို ID ကို hard-code မလုပ်ထားပါ။ Logic editor ထဲတွင် `%m.calendar` dropdown မှ လက်ရှိ Project ရှိ Calendar component ကို ရွေးချယ်ရမည်။

ဤ library သည် `Add source directly` block ကို အသုံးမပြုပါ။ Java standard library class များကို code ထဲတွင် fully-qualified name ဖြင့် ရေးထားပြီး လိုအပ်သော imports များကို JSON `imports` field တွင် ထည့်ထားသည်။

## အသုံးပြုပုံ နမူနာများ

### 1. လက်ရှိအချိန်ရယူခြင်း

```text
Calendar_Set_Now
Calendar_Format_Iso → txt_date.setText(...)
```

ရလဒ်မှာ `yyyy-MM-dd` ပုံစံဖြင့် လက်ရှိရက်စွဲ ဖြစ်သည်။

### 2. ရက်စွဲတစ်ခု သတ်မှတ်ခြင်း

```text
Calendar_Set_Date
    calendar = calendar_main
    year = 2026
    month = 8
    day = 28
```

ဤ block ၏ month input သည် အသုံးပြုသူအတွက် **1 မှ 12** ဖြစ်ပြီး code အတွင်း Java Calendar ၏ 0-based month စနစ်အတွက် `month - 1` ပြောင်းပေးထားသည်။

### 3. ရက်ပေါင်းထည့်ခြင်း

```text
Calendar_Add_Days
    calendar = calendar_main
    days = 7
```

လက်ရှိ Calendar တန်ဖိုးထဲသို့ 7 ရက် ပေါင်းပေးမည်။ Negative number သုံးပါက ရက်နုတ်နိုင်သည်။

### 4. လကုန်ရက်ကို ရယူခြင်း

```text
Calendar_Get_Days_In_Month(calendar_main)
```

ဤ block သည် ထို Calendar ၏ လအတွင်း ရက်အရေအတွက်ကို Number block အဖြစ် ပြန်ပေးသည်။ Leap year ဖြစ်သော February တွင် 29 ရက်ကို အလိုအလျောက်တွက်ပေးနိုင်သည်။

### 5. Date နှစ်ခုကို နှိုင်းယှဉ်ခြင်း

```text
if Calendar_Is_Before(calendar_start, calendar_end)
    // start date သည် end date ထက် စောသည်
```

`Calendar_Is_Same_Day` သည် hour/minute မတူသော်လည်း year နှင့် day-of-year တူလျှင် true ပြန်ပေးသည်။ `Calendar_Difference_Days` သည် milliseconds အပေါ်အခြေခံ၍ double number ပြန်ပေးသောကြောင့် အချိန်ပိုင်းပါသော date နှစ်ခုတွင် fraction ပါနိုင်သည်။

### 6. Custom format ဖြင့် စာသားပြောင်းခြင်း

```text
Calendar_Format
    calendar = calendar_main
    format = "dd/MM/yyyy HH:mm"
```

အသုံးများသော format များမှာ `yyyy-MM-dd`, `dd/MM/yyyy`, `HH:mm:ss` နှင့် `EEE, dd MMM yyyy` တို့ ဖြစ်သည်။

### 7. String မှ Calendar ထဲသို့ Parse လုပ်ခြင်း

```text
Calendar_Parse
    calendar = calendar_main
    text = "28/08/2026"
    format = "dd/MM/yyyy"
```

Input စာသားနှင့် format မကိုက်ညီပါက parse error ဖြစ်နိုင်သောကြောင့် user input ကို အရင် validate လုပ်ပြီးမှ parse လုပ်သင့်သည်။

## အရေးကြီးသော Java Calendar စည်းမျဉ်းများ

| တန်ဖိုး | အဓိပ္ပာယ် |
|---|---|
| Month input | ဤ library တွင် 1–12; code အတွင်း 0–11 သို့ ပြောင်းသည် |
| DAY_OF_WEEK | Sunday = 1, Monday = 2 … Saturday = 7 |
| HOUR_OF_DAY | 24-hour format |
| DAY_OF_YEAR | တစ်နှစ်အတွင်း ရက်အမှတ် |
| WEEK_OF_YEAR | Calendar ၏ first-day/minimal-days setting အပေါ် မူတည်နိုင်သည် |
| Timezone | Calendar object ကိုယ်တိုင်၏ timezone ကို အသုံးပြုသည် |

## Professional အသုံးပြုမှု

Professional workflow တွင် `Calendar_Set_Timezone` ဖြင့် timezone ကို အရင်သတ်မှတ်ပြီးနောက် format သို့မဟုတ် compare block များကို သုံးပါ။ Timezone မတူသော Calendar နှစ်ခုကို နှိုင်းယှဉ်ရာတွင် absolute milliseconds နှင့် local display time ကွာနိုင်သည်။ Server/API date များကို သုံးပါက timezone နှင့် format ကို တစ်သမတ်တည်းသတ်မှတ်ပါ။

Calendar object နှစ်ခု၏ တန်ဖိုးကို တစ်ခုနှင့်တစ်ခု တူညီစေရန် `Calendar_Sync` ကို သုံးနိုင်သည်။ Week-based application များတွင် `Calendar_Set_First_Day_Of_Week` နှင့် `Calendar_Set_Minimal_Days` ကို project requirement နှင့်ကိုက်ညီအောင် သတ်မှတ်ပါ။

## Block Design Rules နှင့် ကိုက်ညီမှု

ဤ library သည် Master Rules အတိုင်း အောက်ပါစည်းမျဉ်းများကို လိုက်နာထားသည်။

| Rule | Implementation |
|---|---|
| Universal ဖြစ်ရမည် | `%m.calendar` component selector သုံးထားသည် |
| Activity/View တစ်ခုတည်းကို မချည်ရ | `this`, fixed Activity ID, fixed View ID မသုံးထားပါ |
| Add source directly မသုံးရ | Custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette routing | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Group separation | Header block များကို `type: "h"`, black color ဖြင့် ထည့်ထားသည် |
| Natural Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter positioning | `%1$s`, `%2$d` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable code | Generic Calendar component selector နှင့် standard Java Calendar API ကို သုံးထားသည် |

## စမ်းသပ်ရန် Checklist

1. Calendar component တစ်ခုရှိပြီး selector dropdown ထဲတွင် ပေါ်လာကြောင်း စစ်ပါ။
2. `Calendar_Set_Now` ပြီးနောက် `Calendar_Format_Iso` ဖြင့် TextView ထဲပြပါ။
3. February နှင့် leap year ကို စမ်းသပ်ပါ။
4. Month input တွင် 1 နှင့် 12 နှစ်ခုလုံးကို စမ်းသပ်ပါ။
5. Date နှစ်ခု၏ before/after/same-day result ကို စမ်းသပ်ပါ။
6. Timezone ပြောင်းပြီး format output ပြောင်းလဲမှုကို စစ်ပါ။
7. Parse input မှားသောအခါ app crash မဖြစ်ကြောင်း စစ်ပါ။
8. Professional blocks မသုံးမီ standard blocks များဖြင့် expected result ကို အရင်အတည်ပြုပါ။

## သတိပြုရန်

Sketchware Pro version အလိုက် custom block JSON field အချို့၊ component selector name အချို့ သို့မဟုတ် import behavior အနည်းငယ် ကွာနိုင်သည်။ Import ပြီးနောက် block များ မပေါ်လာပါက JSON ကို ပြန်မပြင်မီ Sketchware ၏ Custom Block Manager တွင် Calendar component selector သည် `%m.calendar` အဖြစ် support လုပ်မလုပ် စစ်ဆေးပါ။

Keystore password, API key သို့မဟုတ် private data များကို block code ထဲ မထည့်ရ။ Calendar block များသည် date/time logic ကိုသာ ကိုင်တွယ်ပြီး UI output ကို fixed မလုပ်ထားသောကြောင့် ရလဒ်ကို TextView, List, Dialog, API request သို့မဟုတ် အခြား component တစ်ခုခုသို့ အသုံးပြုသူကိုယ်တိုင် ချိတ်ဆက်နိုင်သည်။
