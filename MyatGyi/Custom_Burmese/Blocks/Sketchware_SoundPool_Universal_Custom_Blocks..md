# Sketchware SoundPool Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules နှင့် Sketchware SoundPool component ၏ အသံ effect workflow ကို အခြေခံထားသော Universal Custom Block set ဖြစ်သည်။ `%m.soundpool` component selector ကို အသုံးပြုထားသောကြောင့် SoundPool component ID ကို fixed မလုပ်ထားပါ။ `soundpool_main`, `game_sounds`, `button_effects` သို့မဟုတ် Project ထဲရှိ မည်သည့် SoundPool component မဆို dropdown မှ ရွေးချယ်အသုံးပြုနိုင်သည်။

SoundPool သည် short audio clip, button click, game effect နှင့် notification tone များကို latency နည်းနည်းဖြင့် ဖွင့်ရန် သင့်တော်သည်။ ရှည်လျားသော music သို့မဟုတ် podcast များအတွက် MediaPlayer ကို ပိုမိုသင့်တော်စွာ သုံးသင့်သည်။

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **33 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Max streams သတ်မှတ်ခြင်း၊ sound load၊ default play နှင့် loop play |
| Playback | Advanced play, pause, resume, stop, auto pause/resume |
| Control | Stream volume, mute, rate, loop နှင့် priority |
| Safe | Sound/stream ID စစ်ပြီး play/stop၊ loop count ကန့်သတ်ခြင်း |
| Professional | Unload, release, stop-and-release နှင့် clamped playback values |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `soundpool_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ SoundPool palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် SoundPool component တစ်ခု ထည့်ထားပြီးနောက် block တစ်ခုချင်းစီ၏ `%m.soundpool` dropdown မှ မိမိအသုံးပြုလိုသော SoundPool component ကို ရွေးပါ။

## SoundPool အခြေခံ Workflow

SoundPool တွင် အသံကို အရင် load လုပ်ပြီး `Sound ID` ကို Number variable ထဲတွင် သိမ်းထားရသည်။ ထို Sound ID ကို နောက်ပိုင်း play block များတွင် ပြန်အသုံးပြုရသည်။ Play method ၏ ပြန်ရလာသော `Stream ID` ကိုလည်း သိမ်းထားပါက ထိုတစ်ကြိမ်ဖွင့်နေသော stream ကို pause, resume, stop, volume နှင့် rate ထိန်းချုပ်နိုင်သည်။

```text
App start / screen open
    create SoundPool with max streams
    load sound resource
    save returned Sound ID

Button clicked
    play Sound ID
    save returned Stream ID if later control is needed

Screen exit / app lifecycle
    stop active streams if needed
    unload sounds or release SoundPool
```

## Beginner Level: Button Click Sound

```text
SoundPool_Create_Max_Streams
    soundpool = soundpool_main
    max_streams = 4

sound_id = SoundPool_Load_From_Resource
    soundpool = soundpool_main
    resource = button_click

When btn_click clicked
    SoundPool_Play_Default
        soundpool = soundpool_main
        sound_id = sound_id
```

Resource load block ၏ ပြန်တန်ဖိုးကို Number variable ထဲတွင် သိမ်းပါ။ Sound ID မရသေးမီ play မလုပ်ပါနှင့်။ Sound မဖွင့်ပါက resource ထည့်ထားမှု၊ load result နှင့် sound ID တန်ဖိုးကို စစ်ပါ။

## Advanced Play Parameters

`SoundPool_Play_Advanced` တွင် Sound ID, left/right volume, priority, loop count နှင့် playback rate ကို သတ်မှတ်နိုင်သည်။

| Parameter | အကြံပြုတန်ဖိုး/အဓိပ္ပာယ် |
|---|---|
| Sound ID | `load` မှ ပြန်ရသော ID |
| Left/Right Volume | 0.0 မှ 1.0 |
| Priority | နံပါတ်ကြီးလေလေ priority မြင့်လေလေ |
| Loop | 0 = တစ်ကြိမ်၊ -1 = အဆုံးမရှိ၊ positive = ထပ်ဖွင့်အကြိမ်သတ်မှတ်ချက် |
| Rate | 0.5 မှ 2.0; 1.0 သည် မူလနှုန်း |

SoundPool သည် max stream အရေအတွက် ပြည့်သွားလျှင် priority နိမ့်သော သို့မဟုတ် အဟောင်းဆုံး stream ကို ရပ်နိုင်သည်။ ထို့ကြောင့် game effect များတွင် အရေးကြီးသော sound ကို priority မြင့်စွာသတ်မှတ်ပြီး တစ်ပြိုင်နက် stream အရေအတွက်ကို လိုအပ်သလောက်သာ ထားပါ။

## Stream Control

Play block ၏ ပြန်တန်ဖိုးမှာ Stream ID ဖြစ်သည်။ Stream ID ကို `stream_id` Number variable ထဲတွင် သိမ်းထားပြီးနောက် အောက်ပါ block များကို အသုံးပြုနိုင်သည်။

```text
stream_id = SoundPool_Play_Advanced(...)

SoundPool_Pause_Stream
    soundpool = soundpool_main
    stream_id = stream_id

SoundPool_Resume_Stream
    soundpool = soundpool_main
    stream_id = stream_id

SoundPool_Stop_Stream
    soundpool = soundpool_main
    stream_id = stream_id
```

Stream ID ကို မသိမ်းထားလိုသော ရိုးရှင်းသည့် effect များအတွက် `SoundPool_Play_Default` ကို သုံးနိုင်သည်။ ဖွင့်နေသော stream အားလုံးကို lifecycle သို့မဟုတ် app state အလိုက် ခဏရပ်လိုပါက `SoundPool_Auto_Pause` နှင့် `SoundPool_Auto_Resume` ကို သုံးပါ။

## Volume, Rate နှင့် Loop

Volume သည် 0 မှ 1 အတွင်း ဖြစ်သင့်သည်။ `SoundPool_Set_Stream_Stereo_Volume` သည် channel နှစ်ခုကို တူညီသောအသံဖြင့် သတ်မှတ်ပြီး `SoundPool_Set_Stream_Volume` သည် left/right channel ကို သီးခြားသတ်မှတ်နိုင်သည်။

```text
SoundPool_Set_Stream_Stereo_Volume
    soundpool = soundpool_main
    stream_id = stream_id
    volume = 0.65

SoundPool_Set_Stream_Rate
    soundpool = soundpool_main
    stream_id = stream_id
    rate = 1.25

SoundPool_Set_Stream_Loop
    soundpool = soundpool_main
    stream_id = stream_id
    loop_count = 2
```

Rate သည် 0.5 ထက်မနည်း၊ 2.0 ထက်မကျော်သင့်သောကြောင့် Professional blocks များတွင် clamp လုပ်ထားသည်။ Infinite loop သုံးပါက loop count ကို -1 ထားပြီး နောက်ပိုင်း `SoundPool_Stop_Stream` ဖြင့် ကိုယ်တိုင်ရပ်ပါ။

## Safe Blocks

Sound ID သို့မဟုတ် Stream ID သည် 0 သို့မဟုတ် negative ဖြစ်နိုင်သောအခြေအနေတွင် `SoundPool_Play_If_Sound_Valid` နှင့် `SoundPool_Stop_If_Stream_Valid` ကို သုံးပါ။ SoundPool load/play မအောင်မြင်လျှင် ID 0 ဖြစ်နိုင်သောကြောင့် safe path ထည့်ထားခြင်းက app crash နှင့် မလိုအပ်သော call များကို လျှော့ချပေးသည်။

User setting သို့မဟုတ် API မှ loop count/rate/volume ရရှိပါက အရင် clamp လုပ်ပါ။ `SoundPool_Clamp_Loop_Count` သည် -1 နှင့် သတ်မှတ်ထားသော maximum ကြားတွင် loop count ကို ထိန်းပေးသည်။

## Professional Resource Management

SoundPool တွင် loaded sounds များကို memory ထဲတွင် ပြင်ဆင်ထားနိုင်သောကြောင့် short sound effect များအတွက်သာ အသုံးပြုသင့်သည်။ အသုံးမလိုတော့သော sound တစ်ခုကို `SoundPool_Unload_Sound` ဖြင့် ဖယ်ရှားနိုင်သည်။ Project သို့မဟုတ် level တစ်ခု ပြီးဆုံးသောအခါ `SoundPool_Release` ဖြင့် native resource များကို လွှတ်ပေးပါ။

```text
onDestroy / level finished
    stop active stream if necessary
    unload unused sound IDs
    SoundPool_Release(soundpool_main)
```

Release ပြီးသော SoundPool ကို play/load/stop မလုပ်ရ။ နောက်ထပ် level သို့မဟုတ် screen တွင် ပြန်သုံးလိုပါက component ၏ standard setup workflow ဖြင့် SoundPool ကို ပြန်ဖန်တီးပြီး sound များကို ပြန် load လုပ်ပါ။

## SoundPool နှင့် MediaPlayer ရွေးချယ်ခြင်း

| လိုအပ်ချက် | သင့်တော်သော component |
|---|---|
| Button click, game effect, short notification | SoundPool |
| အသံ effect များကို တစ်ပြိုင်နက်ဖွင့်ခြင်း | SoundPool |
| ရှည်လျားသော music/song | MediaPlayer |
| Streaming audio နှင့် progress control | MediaPlayer |
| အမြန်ပြန်တုံ့ပြန်ရမည့် short clip | SoundPool |

SoundPool သည် short preloaded clip များအတွက် ဖြစ်ပြီး ရှည်လျားသော audio ကို အတင်းသုံးပါက memory နှင့် loading ပြဿနာ ဖြစ်နိုင်သည်။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.soundpool` selector အသုံးပြုထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, fixed View ID နှင့် app-specific component ID မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ အသုံးပြုထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$d` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | Sound ID/Stream ID ကို input အဖြစ်လက်ခံပြီး fixed variable မချည်ထားပါ |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| SoundPool create + load | Sound ID တစ်ခု ရရှိသည် |
| Valid Sound ID play | Short sound ဖွင့်သည် |
| Invalid Sound ID | Safe block ကြောင့် မလိုအပ်သော play မဖြစ်ပါ |
| Stream pause/resume | လက်ရှိ stream ကို ထိန်းချုပ်နိုင်သည် |
| Stream stop | သတ်မှတ်ထားသော stream ရပ်သည် |
| Volume 0/1 | Mute နှင့် full volume မှန်သည် |
| Rate 0.5/1/2 | Playback rate ကန့်သတ်ချက်အတွင်း အလုပ်လုပ်သည် |
| Loop -1 | Stop block ခေါ်သည့်အထိ ထပ်ခါဖွင့်သည် |
| Max streams exceeded | Priority နှင့် stream limit behavior ကို စမ်းပါ |
| Release | Screen/level ပြီးသောအခါ resource လွှတ်သည် |

## Compatibility Note

Sketchware Pro version အလိုက် SoundPool component ၏ built-in block label သို့မဟုတ် generated method name အနည်းငယ်ကွာနိုင်ပါသည်။ ဤ library သည် `create`, `load`, `play`, `pause`, `resume`, `stop`, `autoPause`, `autoResume`, `setVolume`, `setRate`, `setLoop`, `setPriority`, `unload` နှင့် `release` API များကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in SoundPool blocks နှင့် generated source code ကို တိုက်စစ်ပြီး method name/state workflow ကို version-compatible ပြင်ဆင်ပါ။

ဤ Custom Blocks များသည် SoundPool configuration နှင့် stream control ကို ပေးထားခြင်းဖြစ်ပြီး sound resource ထည့်သွင်းခြင်း၊ loading ပြီးဆုံးမှုကို စောင့်ခြင်းနှင့် error recovery ကို သင့် Project ၏ built-in component workflow အတွင်းတွင် ဆက်လက်စီမံရမည်။

## References

[1] Android Developers, “SoundPool API reference,” https://developer.android.com/reference/android/media/SoundPool  
[2] Sketchware Docs, “SoundPool,” https://sketchware-docs.vercel.app/docs/component-soundpool.html  
[3] Sketchyas, “Learn Sketchware SoundPool Component,” https://sketchyas.com/sketchware/components/learn-soundpool.php
