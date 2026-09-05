# Sketchware ObjectAnimator Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules နှင့် Sketchware ObjectAnimator component ၏ View animation workflow ကို အခြေခံထားသော Universal Custom Block set ဖြစ်သည်။ `%m.objectanimator` နှင့် `%m.view` selector များကို အသုံးပြုထားသောကြောင့် ObjectAnimator ID သို့မဟုတ် View ID ကို fixed မလုပ်ထားပါ။ မည်သည့် ObjectAnimator component နှင့် မည်သည့် View/Widget ကိုမဆို dropdown မှ ရွေးချယ်ပြီး ပြန်လည်အသုံးပြုနိုင်သည်။

ObjectAnimator သည် target object ၏ property တစ်ခုကို သတ်မှတ်ထားသော start/end value ကြားတွင် အချိန်အလိုက် ပြောင်းလဲပေးသော property animation system ဖြစ်သည်။ Sketchware တွင် ObjectAnimator component ၏ `onAnimationStart`, `onAnimationEnd` နှင့် `onAnimationCancel` event များမှတစ်ဆင့် Animation lifecycle ကို ကိုင်တွယ်နိုင်သည်။

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **52 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Target, property, value, from/to value, duration, start, cancel, running status |
| View Presets | Rotation, RotationX/Y, TranslationX/Y, Alpha, ScaleX/Y |
| Timing | Start delay, repeat count, infinite repeat, RESTART, REVERSE, auto-cancel |
| Interpolator | Linear, Accelerate, Decelerate, Accelerate-Decelerate, Bounce, Overshoot, Anticipate |
| Runtime Control | Reverse, end, current play time, fraction, total duration |
| Safe | Start/cancel only when required, reset to beginning, safe View animation |
| Professional | Minimum duration, repeat limit, lifecycle-safe reset, pause/resume |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `objectanimator_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ ObjectAnimator palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် ObjectAnimator component နှင့် animation ပြုလုပ်မည့် View တစ်ခုရှိရမည်။ Block ထဲရှိ `%m.objectanimator` dropdown မှ Animator ကိုရွေးပြီး `%m.view` dropdown မှ Target View ကိုရွေးပါ။

## အခြေခံ Animation Workflow

Animation တစ်ခု၏ အခြေခံအစီအစဉ်မှာ Target သတ်မှတ်ခြင်း၊ Property သတ်မှတ်ခြင်း၊ Value/From-To သတ်မှတ်ခြင်း၊ Duration သတ်မှတ်ခြင်းနှင့် Start လုပ်ခြင်း ဖြစ်သည်။

```text
ObjectAnimator_Set_Target
    objectanimator = animator_main
    view = target_view

ObjectAnimator_Set_Property
    objectanimator = animator_main
    property = "alpha"

ObjectAnimator_Set_Values_From_To
    objectanimator = animator_main
    from = 0
    to = 1

ObjectAnimator_Set_Duration
    objectanimator = animator_main
    milliseconds = 500

ObjectAnimator_Start
    objectanimator = animator_main
```

Property value များသည် property အလိုက် အဓိပ္ပာယ်ကွာနိုင်သည်။ `alpha` သည် 0 မှ 1 အတွင်း၊ `scaleX/scaleY` တွင် 1 သည် မူလအရွယ်၊ `rotation` တွင် degree နှင့် `translationX/translationY` တွင် pixel-based position value ကို သုံးရမည်။

## View Property Presets

Property name ကို ကိုယ်တိုင်ရိုက်ထည့်ရန် မလိုပါက preset blocks များကို သုံးနိုင်သည်။

| Block | အသုံးပြုမှု |
|---|---|
| Set Rotation Property | View ကို degree ဖြင့် လှည့်ခြင်း |
| Set TranslationX Property | ဘယ်/ညာ ရွှေ့ခြင်း |
| Set TranslationY Property | အပေါ်/အောက် ရွှေ့ခြင်း |
| Set Alpha Property | ဖျော့ခြင်း/ပေါ်လာခြင်း |
| Set ScaleX/ScaleY Property | အလျားလိုက်/ဒေါင်လိုက် ချဲ့ခြင်း |
| Set RotationX/RotationY Property | 3D ဝင်ရိုးလှည့်ခြင်း |

တစ်ကြောင်းတည်းဖြင့် Animation စတင်လိုပါက `ObjectAnimator_Animate_View_From_To` ကို သုံးပါ။ ဤ block သည် Target, Property, From, To နှင့် Start ကို တစ်ခါတည်း လုပ်ဆောင်သည်။

```text
ObjectAnimator_Animate_View_From_To
    objectanimator = animator_main
    view = image_icon
    property = "rotation"
    from = 0
    to = 360
```

## `onAnimationStart` Event

Animation စတင်ချိန်တွင် UI state ပြောင်းရန် `onAnimationStart` event ကို သုံးပါ။ ဥပမာ loading indicator ကို ပြသခြင်း၊ Start button ကို disable လုပ်ခြင်း သို့မဟုတ် status text ပြောင်းခြင်း ဖြစ်သည်။

```text
ObjectAnimator component → onAnimationStart
    set loading_text to "လုပ်ဆောင်နေပါသည်..."
    set btn_start enabled to false
```

Custom Block library သည် event ကို အလိုအလျောက်ဖန်တီးပေးခြင်းမဟုတ်ပါ။ Sketchware ObjectAnimator component ၏ event အတွင်းတွင် အထက်ပါ logic ကို သီးခြားထည့်ရမည်။

## `onAnimationEnd` Event

Animation သတ်မှတ်ထားသော duration နှင့် repeat cycle များ ပြီးဆုံးသောအခါ `onAnimationEnd` event ဖြစ်ပေါ်သည်။ Final state သတ်မှတ်ခြင်း၊ Next screen သို့သွားခြင်း၊ Button ပြန်ဖွင့်ခြင်း သို့မဟုတ် animation ပြီးဆုံးကြောင်း ပြသခြင်းအတွက် သုံးပါ။

```text
ObjectAnimator component → onAnimationEnd
    set btn_start enabled to true
    set status_text to "ပြီးဆုံးပါပြီ"
    run next action
```

REVERSE mode နှင့် repeat count သုံးထားပါက end event သည် သတ်မှတ်ထားသော repeat cycles အားလုံးပြီးသောအခါ ဖြစ်ပေါ်သည်။

## `onAnimationCancel` Event

Animation ကို `ObjectAnimator_Cancel` သို့မဟုတ် `ObjectAnimator_Cancel_If_Running` ဖြင့် ပယ်ဖျက်သောအခါ `onAnimationCancel` event ဖြစ်ပေါ်သည်။ Cancel ဖြစ်ချိန်တွင် UI ကို safe state သို့ ပြန်ထားခြင်း၊ Button ပြန်ဖွင့်ခြင်းနှင့် temporary state ရှင်းခြင်းတို့ကို လုပ်ပါ။

```text
ObjectAnimator component → onAnimationCancel
    set btn_start enabled to true
    set status_text to "ပယ်ဖျက်လိုက်ပါပြီ"
    restore required view state
```

Android animation lifecycle တွင် cancel ဖြစ်သော animation သည် end callback ကိုလည်း ဖြစ်စေနိုင်သော implementation behavior ရှိနိုင်သောကြောင့် `onAnimationEnd` နှင့် `onAnimationCancel` နှစ်ခုစလုံးတွင် duplicate action မဖြစ်စေရန် Boolean state variable တစ်ခု သို့မဟုတ် guard logic သုံးပါ။

## Timing နှင့် Repeat

`ObjectAnimator_Set_Duration` သည် animation ကြာချိန်ကို milliseconds ဖြင့် သတ်မှတ်သည်။ `ObjectAnimator_Set_Start_Delay` သည် animation စတင်မီ စောင့်ရမည့်အချိန်ကို သတ်မှတ်သည်။

`RESTART` သည် animation ကို အစမှပြန်စပြီး `REVERSE` သည် အရှေ့သို့သွားပြီး နောက်ပြန်လာသည့်ပုံစံဖြင့် ထပ်လုပ်သည်။ `ObjectAnimator_Set_Repeat_Infinite` သုံးပါက animation သည် အဆုံးမရှိ ထပ်လုပ်မည်ဖြစ်သောကြောင့် lifecycle event သို့မဟုတ် user action တွင် cancel လုပ်ရန် မမေ့ပါနှင့်။

```text
ObjectAnimator_Set_Duration
    objectanimator = animator_pulse
    milliseconds = 800

ObjectAnimator_Set_Repeat_Count
    objectanimator = animator_pulse
    count = 2

ObjectAnimator_Set_Repeat_Reverse(animator_pulse)
```

## Interpolator ရွေးချယ်ခြင်း

Interpolator သည် Animation တစ်လျှောက် value ပြောင်းသည့် အမြန်နှုန်းပုံစံကို သတ်မှတ်သည်။ Linear သည် အမြန်နှုန်းတူ၊ Accelerate သည် နှေးရာမှမြန်၊ Decelerate သည် မြန်ရာမှနှေး၊ Bounce သည် ခုန်သကဲ့သို့ effect ရစေသည်။

```text
ObjectAnimator_Set_Decelerate_Interpolator
    objectanimator = animator_main
```

အလှဆင် effect များအတွက် Bounce/Overshoot သုံးနိုင်သော်လည်း form input, accessibility feedback နှင့် critical status transition များတွင် မျက်စိမူးစေနိုင်သော animation များကို မလွန်ကဲစွာ သုံးပါ။

## Seek-like Runtime Control

`ObjectAnimator_Get_Animated_Fraction` သည် animation ပြီးစီးမှုကို percentage အဖြစ် ရယူနိုင်သည်။ `ObjectAnimator_Get_Current_Play_Time` သည် လက်ရှိ play time ကို ပြန်ပေးသည်။ Timer event ဖြင့် progress label သို့မဟုတ် custom progress display ပြလိုပါက ၎င်းတို့ကို အသုံးပြုနိုင်သည်။

`ObjectAnimator_Set_Current_Play_Time` သည် animation ကို သတ်မှတ်ထားသော millisecond position သို့ ရွှေ့နိုင်သည်။ `ObjectAnimator_Reverse` သည် လက်ရှိ direction ကို ပြောင်းပြန်လုပ်ပြီး `ObjectAnimator_End` သည် animation ကို ချက်ချင်းအဆုံးသတ်သည်။

## Safe နှင့် Professional Workflow

Animation တစ်ခုကို မကြာခဏ start လုပ်ပါက overlapping animation ဖြစ်နိုင်သည်။ `ObjectAnimator_Start_If_Stopped` သည် မအလုပ်လုပ်သေးမှသာ start လုပ်ပြီး `ObjectAnimator_Cancel_If_Running` သည် အလုပ်လုပ်နေမှသာ cancel လုပ်သည်။ Animation ပြန်စလိုပါက `ObjectAnimator_Start_From_Beginning` သို့မဟုတ် `ObjectAnimator_Cancel_And_Reset` ကို သုံးပါ။

Duration ကို user input ဖြင့် သတ်မှတ်ပါက `ObjectAnimator_Set_Safe_Duration` ကို သုံးပြီး အနည်းဆုံး 1 millisecond ရှိစေပါ။ Repeat count ကိုလည်း `ObjectAnimator_Set_Safe_Repeat_Count` ဖြင့် မလိုအပ်ဘဲ အလွန်ကြီးမားသောတန်ဖိုး မဖြစ်စေရန် စစ်ဆေးပါ။

Screen ပိတ်ခြင်း၊ Activity ပြောင်းခြင်း သို့မဟုတ် View ဖယ်ရှားခြင်းမတိုင်မီ animation ကို cancel လုပ်ပါ။ `onAnimationCancel` နှင့် lifecycle event များတွင် UI state ကို တစ်သမတ်တည်း ပြန်ထားပါ။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.objectanimator` နှင့် `%m.view` selector များသုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed View ID, fixed Activity ID နှင့် `this` မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ အသုံးပြုထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$d` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | Animator/View ID နှင့် app-specific state များကို hard-code မလုပ်ထားပါ |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Target + Property + From/To + Start | View သည် မှန်ကန်သော property ဖြင့် လှုပ်ရှားသည် |
| onAnimationStart | Start state logic တစ်ကြိမ် run သည် |
| onAnimationEnd | Final state logic ပြီးဆုံးချိန်တွင် run သည် |
| Cancel button | onAnimationCancel event ဖြစ်ပြီး safe state ပြန်ရသည် |
| Infinite repeat | User/lifecycle action ဖြင့် cancel လုပ်နိုင်သည် |
| RESTART/REVERSE | Repeat behavior မှန်ကန်သည် |
| Duration 0/negative | Safe duration block ကြောင့် မမှန်ကန်သောတန်ဖိုး မဝင်ပါ |
| Overlapping start | Start If Stopped ဖြင့် duplicate animation လျှော့နိုင်သည် |
| Screen exit | Animation cancel/reset ပြီး state မကျန်ပါ |
| Multiple Animator/View | Selector dropdown ဖြင့် သီးခြားထိန်းချုပ်နိုင်သည် |

## Compatibility Note

Sketchware Pro version အလိုက် ObjectAnimator block label သို့မဟုတ် generated method name ကွာနိုင်ပါသည်။ ဤ library သည် `setTarget`, `setPropertyName`, `setFloatValues`, `setDuration`, `setStartDelay`, `setRepeatCount`, `setRepeatMode`, `setInterpolator`, `start`, `cancel`, `isRunning`, `reverse`, `end`, `setCurrentPlayTime`, `getAnimatedFraction`, `getCurrentPlayTime`, `getTotalDuration`, `setAutoCancel`, `pause` နှင့် `resume` API များကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in ObjectAnimator blocks နှင့် generated source code ကို တိုက်စစ်ပြီး method name/API compatibility ကို ပြင်ဆင်ပါ။

ဤ Custom Blocks များသည် ObjectAnimator ကို configure/control လုပ်ရန် ဖြစ်ပြီး `onAnimationStart`, `onAnimationEnd`, `onAnimationCancel` event များ၏ အတွင်း logic ကို Sketchware component event အတွင်းတွင် သီးခြားရေးရမည်။

## References

[1] Sketchware Docs, “ObjectAnimator,” https://sketchware-docs.vercel.app/docs/component-object-animator.html  
[2] Android Developers, “ObjectAnimator API reference,” https://developer.android.com/reference/android/animation/ObjectAnimator  
[3] Android Developers, “Property Animation Overview,” https://developer.android.com/develop/ui/views/animations/prop-animation
