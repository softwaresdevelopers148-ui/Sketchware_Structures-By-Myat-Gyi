# Sketchware Async Task Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware Async Task component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.asynctask` selector ကို အသုံးပြုထားသောကြောင့် fixed Activity ID, View ID, task ID နှင့် app-specific variable များကို hard-code မလုပ်ထားပါ။

Android AsyncTask ၏ အခြေခံ lifecycle သည် `onPreExecute`၊ `doInBackground`၊ `onProgressUpdate` နှင့် `onPostExecute` ဟူသော အဆင့် ၄ ဆင့်ဖြစ်သည်။ `onPreExecute`, `onProgressUpdate` နှင့် `onPostExecute` သည် UI thread ပေါ်တွင် အလုပ်လုပ်ပြီး `doInBackground` သည် background thread ပေါ်တွင် အလုပ်လုပ်သည်။ [1]

> **Compatibility သတိပေးချက်:** AsyncTask သည် Android API 30 မှ deprecated ဖြစ်ပြီး context leak, missed callback, configuration-change crash နှင့် inconsistent behavior များ ဖြစ်နိုင်သည်။ Project အသစ်များတွင် `Executor`, `java.util.concurrent` သို့မဟုတ် Kotlin coroutines ကို ဦးစားပေးပါ။ Legacy Sketchware project တွင် Async Task component သုံးရပါက ဤ guide ထဲရှိ cancellation နှင့် lifecycle စည်းမျဉ်းများကို လိုက်နာပါ။ [1]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **45 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Execute, input, cancel, status စစ်ခြင်း |
| `onPreExecute` | Loading, progress reset, result reset, pre-action |
| `doInBackground` | Input validation, result return, cancellation checkpoint, progress publish |
| `onProgressUpdate` | Progress value, percentage text, valid range, update action |
| `onPostExecute` | Loading ပိတ်ခြင်း, result save, success/cancel action |
| Safe | Empty result, error summary, progress/delay clamp |
| Professional | Duplicate execution ကာကွယ်ခြင်း, lifecycle cancellation, UI-thread safety, migration note |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `asynctask_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ Async Task palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် Async Task component တစ်ခုရှိရမည်။ Block တစ်ခုချင်းစီ၏ `%m.asynctask` dropdown မှ မိမိအသုံးပြုလိုသော component ကို ရွေးပါ။

## Lifecycle Structure

AsyncTask ကို အောက်ပါအစီအစဉ်ဖြင့် နားလည်ပါ။

```text
onPreExecute
    prepare UI/loading state

doInBackground
    perform heavy work
    publish progress when needed
    periodically check cancellation
    return result

onProgressUpdate
    update ProgressBar/text on UI thread

onPostExecute
    hide loading
    consume result
    update UI on UI thread
```

Event lifecycle method များကို ကိုယ်တိုင် manually ခေါ်ခြင်းမပြုပါနှင့်။ AsyncTask instance ကို UI thread ပေါ်တွင် ဖန်တီးပြီး `execute` ကို UI thread မှ ခေါ်ရမည်။ Task instance တစ်ခုကို တစ်ကြိမ်သာ execute လုပ်နိုင်သည်။ [1]

## Basic Execute နှင့် Input

```text
When btn_start clicked
    if AsyncTask_Prevent_Duplicate(asynctask)
        AsyncTask_Execute_With_Input(asynctask, input_value)
```

Input မလိုသော task အတွက် `AsyncTask_Execute` ကို သုံးနိုင်သည်။ Input ပါသော task များတွင် `doInBackground` event ထဲရှိ parameter သို့မဟုတ် project variable မှ input ကို ရယူပြီး background processing ပြုလုပ်ပါ။

> Task တစ်ခုကို ပြီးဆုံးပြီးနောက် ထပ်မံအသုံးပြုလိုပါက component/version က ခွင့်ပြုသည့် new task instance သို့မဟုတ် new execution workflow ကို အသုံးပြုပါ။ တစ်ခုတည်းသော AsyncTask instance ကို ထပ်ခါ execute မလုပ်ပါနှင့်။ [1]

## `onPreExecute` Event

`onPreExecute` သည် background work မစမီ UI thread ပေါ်တွင် ခေါ်သည်။ Loading indicator ပြခြင်း၊ button disable ပြုလုပ်ခြင်း၊ progress reset ပြုလုပ်ခြင်းနှင့် old result ရှင်းခြင်းတို့အတွက် သုံးပါ။

```text
Async Task → onPreExecute
    loading = true
    progress = 0
    result = ""
    disable start button
```

```text
AsyncTask_Pre_Show_Loading(loading)
AsyncTask_Pre_Reset_Progress(progress)
AsyncTask_Pre_Clear_Result(result)
```

ဒီ event ထဲတွင် network/database/file task အမှန်တကယ် မလုပ်ပါနှင့်။ Heavy operation ကို `doInBackground` ထဲသို့ ရွှေ့ပါ။ UI view များကို ဒီ event တွင် update လုပ်နိုင်သည်။

## `doInBackground` Event

`doInBackground` သည် background thread ပေါ်တွင် ခေါ်ပြီး file read, computation, network request နှင့် data conversion ကဲ့သို့ UI ကို မပိတ်စေသည့် heavy work များ ပြုလုပ်ရမည့်နေရာ ဖြစ်သည်။ Android View များကို ဒီ event ထဲတွင် တိုက်ရိုက် update မလုပ်ပါနှင့်။ [1]

```text
Async Task → doInBackground
    if AsyncTask_Background_Is_Cancelled(asynctask)
        return empty_result

    perform heavy work
    publish progress
    return final_result
```

`doInBackground` အတွင်း loop ရှိပါက iteration တစ်ကြိမ်စီ သို့မဟုတ် အချိန်ကာလတစ်ခုစီတွင် `isCancelled` ကို စစ်ပါ။ Cancellation သည် cooperative ဖြစ်သောကြောင့် `cancel(true)` ခေါ်ရုံဖြင့် blocking I/O သို့မဟုတ် arbitrary code သည် အမြဲချက်ချင်း ရပ်မည်ဟု မယူဆပါနှင့်။ [1]

```text
repeat for each item
    if task is cancelled
        stop loop and return
    process item
    publish progress
```

## `onProgressUpdate` Event

`doInBackground` မှ `publishProgress` ခေါ်သောအခါ `onProgressUpdate` သည် UI thread ပေါ်တွင် progress value ကို လက်ခံသည်။ ProgressBar, percentage label နှင့် status text update များကို ဒီ event ထဲတွင်သာ ပြုလုပ်ပါ။ [1]

```text
Async Task → onProgressUpdate(progress_value)
    progress = AsyncTask_Clamp_Progress(progress_value)
    set ProgressBar to progress
    set txt_status to progress + "%"
```

Progress value ကို 0–100 အတွင်း clamp ပြုလုပ်ပါ။ Progress callback timing သည် exact real-time guarantee မဟုတ်သောကြောင့် UI ကို callback တိုင်းတွင် အလွန်အကျွံ redraw မလုပ်ဘဲ လိုအပ်ပါက throttle/debounce လုပ်ပါ။

## `onPostExecute` Event

`onPostExecute` သည် background result ရရှိပြီးနောက် UI thread ပေါ်တွင် ခေါ်သည်။ Loading ပိတ်ခြင်း၊ button ပြန်ဖွင့်ခြင်း၊ result ပြခြင်း၊ list refresh နှင့် success UI ကို ဒီ event ထဲတွင် ပြုလုပ်ပါ။ [1]

```text
Async Task → onPostExecute(result)
    loading = false
    save result
    if result is valid
        show result
    else
        show empty-result message
    enable start button
```

Background exception များကို task ၏ result/error model ထဲတွင် စီမံပြီး `onPostExecute` တွင် safe error state ပြပါ။ Raw exception ကို user ထံ တိုက်ရိုက်မပြဘဲ `AsyncTask_Error_Summary` ကို အသုံးပြုပါ။

## Cancellation Workflow

Task cancel ခလုတ်အတွက် `AsyncTask_Cancel_If_Running` ကို သုံးပါ။ `doInBackground` ထဲတွင် cancellation checkpoint များထားပြီး `onPostExecute` ကို normal success path အဖြစ် မယူဆပါနှင့်။ AsyncTask cancellation သည် `onCancelled` path သို့ ရောက်နိုင်ပြီး result success နှင့် cancel state ကို ခွဲစစ်ရမည်။ [1]

```text
When btn_cancel clicked
    if AsyncTask_Cancel_If_Running(asynctask)
        loading = false
        show "Cancelled"
```

Network call, file operation သို့မဟုတ် blocking method များသည် cancellation request ကို ချက်ချင်းမတုံ့ပြန်နိုင်ပါ။ ထို operation များကို timeout သတ်မှတ်ပြီး cancellation flag ကို အခါအားလျော်စွာ စစ်ပါ။

## Thread Safety Rule

| Event | Thread | ခွင့်ပြုသင့်သောလုပ်ဆောင်ချက် |
|---|---|---|
| `onPreExecute` | UI thread | Loading, button, view state |
| `doInBackground` | Background thread | Network, file, computation, database |
| `onProgressUpdate` | UI thread | ProgressBar နှင့် status UI |
| `onPostExecute` | UI thread | Result UI, list refresh, navigation |

`doInBackground` ထဲမှ TextView, ImageView, ProgressBar သို့ တိုက်ရိုက် setText/setImage/setProgress မလုပ်ပါနှင့်။ `publishProgress` ဖြင့် value ပို့ပြီး `onProgressUpdate` တွင် UI update လုပ်ပါ။ Result ကို return ပြန်ပြီး `onPostExecute` တွင် UI သို့ ပြပါ။

## Loading နှင့် Duplicate Execution

```text
When btn_start clicked
    if task status is PENDING
        show loading
        execute task
    else
        ignore duplicate click
```

Start button ကို `onPreExecute` တွင် disabled ပြုလုပ်ပြီး `onPostExecute`/cancel/error path တွင် ပြန်ဖွင့်ပါ။ App state များတွင် loading indicator stuck မဖြစ်စေရန် success, empty result, cancellation နှင့် failure လမ်းကြောင်းအားလုံးတွင် loading ကို ပိတ်ပါ။

## Result နှင့် Error Design

Result type ကို project ရည်ရွယ်ချက်နှင့် ကိုက်ညီအောင် သတ်မှတ်ပါ။ Simple text result, number result, boolean result သို့မဟုတ် Map/List result ဖြစ်နိုင်သည်။ Empty/null result အတွက် default state ထားပြီး `onPostExecute` တွင် branch ခွဲပါ။

```text
onPostExecute(result)
    if AsyncTask_Post_Result_Is_Valid(result)
        update result view
    else
        show "No result"
```

Exception ကို error result အဖြစ်ပြန်ပေးခြင်း၊ error variable ထဲသိမ်းခြင်း သို့မဟုတ် component/version ၏ error path ကို အသုံးပြုခြင်းတို့ကို project တစ်ခုလုံးတွင် တစ်သမတ်တည်းထားပါ။ Production UI တွင် stack trace, file path, token နှင့် private data များ မဖော်ပြပါနှင့်။

## Lifecycle နှင့် Screen Change

AsyncTask callback သည် Activity destroy/rotate ပြီးနောက် UI reference အဟောင်းသို့ ပြန်လာနိုင်သည်။ Screen ပိတ်ခြင်း၊ configuration change နှင့် navigation ဖြစ်ချိန်တွင် task cancel/detach strategy သုံးပါ။ Callback ပြန်လာချိန်တွင် လက်ရှိ screen အသက်ရှင်နေသေးသလား၊ task သည် လက်ရှိ request နှင့် ကိုက်ညီသေးသလား စစ်ပါ။

```text
onDestroy/onStop
    if task is running
        cancel task or detach result delivery
```

Long-running work, reliable background sync, deferrable work နှင့် process death ပြီးနောက် ဆက်လုပ်ရမည့်အလုပ်များအတွက် AsyncTask မသုံးဘဲ WorkManager, Executor, foreground service သို့မဟုတ် Kotlin coroutines ကဲ့သို့ modern alternative ကို စဉ်းစားပါ။

## Migration Guidance

AsyncTask သည် API 30 မှ deprecated ဖြစ်သောကြောင့် Project အသစ်များတွင် အောက်ပါရွေးချယ်မှုများကို အသုံးပြုပါ။

| လိုအပ်ချက် | သင့်တော်သော alternative |
|---|---|
| Short background computation | `Executor`/`java.util.concurrent` |
| Kotlin project နှင့် structured concurrency | Kotlin coroutines |
| Guaranteed deferrable background work | WorkManager |
| Long-running user-visible operation | Foreground service နှင့် notification |
| Legacy Sketchware component | AsyncTask ကို cancellation/lifecycle စည်းမျဉ်းနှင့်သာ သုံးပါ |

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.asynctask` selector သုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, View ID နှင့် task ID မသုံးထားပါ |
| Event-aware | `onPreExecute`, `doInBackground`, `onProgressUpdate`, `onPostExecute` workflow အားလုံးကို guide တွင် ထည့်ထားသည် |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$b` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Thread-safe guidance | UI update နှင့် background work ကို event အလိုက် ခွဲခြားထားသည် |
| Security/lifecycle | Cancellation, destroyed Activity, exception နှင့် sensitive data guidance ပါဝင်သည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Execute | Task သည် တစ်ကြိမ်မှန်ကန်စွာ စတင်သည် |
| Duplicate click | Running task ကို ထပ်မံ execute မလုပ်ပါ |
| Pre-execute | Loading ပြပြီး progress reset ဖြစ်သည် |
| Background | Heavy work ကြောင့် UI မခဲပါ |
| Progress | `onProgressUpdate` တွင် 0–100 အတွင်း ပြသည် |
| Result | `onPostExecute` တွင် result UI ပြသည် |
| Empty result | Safe empty-state message ပြသည် |
| Cancel | Loop သည် cancellation checkpoint တွင် ရပ်သည် |
| Error | Raw stack trace မပေါ်ဘဲ safe error ပြသည် |
| Rotation/navigation | Destroyed Activity သို့ stale update မလုပ်ပါ |
| Thread rule | `doInBackground` မှ View များကို တိုက်ရိုက်မပြင်ပါ |
| Build | AsyncTask deprecation warning ကို သိရှိထားသည် |
| Migration | Project အသစ်တွင် modern alternative စဉ်းစားထားသည် |

## Compatibility Note

Sketchware Pro version အလိုက် Async Task component ၏ generated method name၊ event parameter type/order၊ input/result type နှင့် cancellation event support ကွာနိုင်ပါသည်။ ဤ library သည် `%m.asynctask` selector၊ `execute`, `cancel`, `isCancelled`, `getStatus`, `publishProgress` နှင့် AsyncTask lifecycle ပုံစံကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in Async Task blocks နှင့် generated source code ကို တိုက်စစ်ပြီး unsupported method ကို built-in equivalent ဖြင့် အစားထိုးပါ။

AsyncTask component event များကို Custom Block JSON က အလိုအလျောက် ဖန်တီးပေးခြင်းမဟုတ်ပါ။ Async Task component ကို Add လုပ်ပြီးနောက် သက်ဆိုင်ရာ `onPreExecute`, `doInBackground`, `onProgressUpdate` နှင့် `onPostExecute` event အတွင်းတွင် event parameters နှင့် UI/business logic ကို သီးခြားရေးရမည်။

## References

[1] Android Developers, “AsyncTask API Reference,” https://developer.android.com/reference/android/os/AsyncTask
[2] Android Developers, “Kotlin coroutines on Android,” https://developer.android.com/topic/libraries/architecture/coroutines
