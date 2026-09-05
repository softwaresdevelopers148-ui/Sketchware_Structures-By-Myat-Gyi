# Sketchware MediaPlayer Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules နှင့် Sketchware MediaPlayer component ၏ playback workflow ကို အခြေခံထားသော Universal Custom Block set ဖြစ်သည်။ `%m.mediaplayer` component selector ကို အသုံးပြုထားသောကြောင့် MediaPlayer component ID ကို fixed မလုပ်ထားပါ။ `player_main`, `music_player`, `audio_player` သို့မဟုတ် Project ထဲရှိ မည်သည့် MediaPlayer component မဆို dropdown မှ ရွေးချယ်အသုံးပြုနိုင်သည်။

Android MediaPlayer သည် audio/video file နှင့် stream playback ကို ထိန်းချုပ်ရန် အသုံးပြုသော state-based component ဖြစ်သည်။ Player ကို prepare မလုပ်မီ `start`, `pause`, `stop`, `seekTo`, `getDuration` စသည့် method များကို ခေါ်ပါက error ဖြစ်နိုင်သောကြောင့် Sketchware ၏ prepared/completion/error event workflow ကို အမြဲလိုက်နာရမည်။

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **43 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Start, pause, stop, reset, release, playing status |
| Safe Control | Conditional start/pause, toggle, stop/reset, resource release |
| Position | Current position, duration, seek milliseconds, seek percentage |
| Seek | Forward/backward milliseconds နှင့် seconds |
| Volume/Loop | Stereo volume, mute/unmute, looping နှင့် loop status |
| Format | Milliseconds ကို `MM:ss` သို့မဟုတ် `HH:mm:ss` ပြောင်းခြင်း |
| Professional | Safe progress, start at percentage, audio session, end detection, lifecycle release |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `mediaplayer_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ MediaPlayer palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် MediaPlayer component တစ်ခု ထည့်ထားပြီးနောက် block တစ်ခုချင်းစီ၏ `%m.mediaplayer` dropdown မှ မိမိအသုံးပြုလိုသော MediaPlayer component ကို ရွေးပါ။

## အခြေခံ Playback Workflow

MediaPlayer ကို အသုံးပြုရာတွင် media source သတ်မှတ်ခြင်းနှင့် prepare လုပ်ခြင်းသည် Project ၏ built-in MediaPlayer blocks/event များအတိုင်း အရင်လုပ်ထားရမည်။ ပြီးမှ ဤ library ထဲရှိ Start/Pause/Seek blocks များကို သုံးပါ။

```text
Media source prepared / onPrepared event
    MediaPlayer_Start

Play button clicked
    MediaPlayer_Start_If_Paused

Pause button clicked
    MediaPlayer_Pause_If_Playing

Stop button clicked
    MediaPlayer_Stop

Screen lifecycle ends
    MediaPlayer_Stop_Release
```

`MediaPlayer_Start_If_Paused` သည် player မဖွင့်နေမှသာ start လုပ်သည်။ သို့သော် player သည် မပြင်ဆင်ရသေးသော state ဖြစ်နိုင်ပါက component ၏ prepared event အတွင်းမှသာ start လုပ်ပါ။

## Play/Pause Toggle

Play နှင့် Pause button တစ်ခုတည်းဖြင့် ထိန်းချုပ်လိုပါက `MediaPlayer_Pause_Or_Start` ကို သုံးနိုင်သည်။

```text
When btn_play_pause clicked
    MediaPlayer_Pause_Or_Start
        mediaplayer = player_main
```

Professional UI တွင် `MediaPlayer_Is_Playing` ဖြင့် icon နှင့် button text ကိုလည်း update လုပ်ပါ။ Player state မသေချာပါက error event ကို ထည့်ပြီး user ကို ပြန်လည်အကြောင်းကြားပါ။

## Position နှင့် Seek Bar Workflow

`MediaPlayer_Get_Current_Position` သည် လက်ရှိ playback position ကို milliseconds ဖြင့် ပြန်ပေးပြီး `MediaPlayer_Get_Duration` သည် စုစုပေါင်းကြာချိန်ကို ပြန်ပေးသည်။ SeekBar နှင့် ချိတ်ရာတွင် duration သည် 0 ထက်ကြီးကြောင်း အရင်စစ်ပါ။

```text
When media is prepared
    duration = MediaPlayer_Get_Duration(player_main)
    set SeekBar max to duration

Timer event / progress update
    position = MediaPlayer_Get_Current_Position(player_main)
    set SeekBar progress to position

SeekBar changed
    MediaPlayer_Seek_To_Millis
        mediaplayer = player_main
        milliseconds = seekbar_progress
```

ရာခိုင်နှုန်းဖြင့် ရွှေ့လိုပါက `MediaPlayer_Set_Progress_Safely` သို့မဟုတ် `MediaPlayer_Seek_To_Percent` ကို သုံးပါ။ Percent input ကို 0 မှ 100 အတွင်း clamp လုပ်ထားသော professional block ကို ဦးစားပေးသုံးပါ။

## Forward နှင့် Backward Seek

```text
Forward button
    MediaPlayer_Seek_Forward_Seconds
        mediaplayer = player_main
        seconds = 10

Rewind button
    MediaPlayer_Seek_Backward_Seconds
        mediaplayer = player_main
        seconds = 10
```

Seek position သည် 0 ထက်မနည်းစေရန်နှင့် duration ထက်မကျော်စေရန် block code အတွင်း ကန့်သတ်ထားသည်။

## Volume နှင့် Mute

Volume block များ၏ တန်ဖိုးသည် ပုံမှန်အားဖြင့် 0 မှ 1 အတွင်း ဖြစ်သင့်သည်။ `MediaPlayer_Set_Stereo_Volume` သည် ဘယ်/ညာ channel နှစ်ခုလုံးကို တူညီသော volume ဖြင့် သတ်မှတ်သည်။ `MediaPlayer_Set_Volume` သည် left/right volume ကို သီးခြားသတ်မှတ်နိုင်သည်။

```text
MediaPlayer_Set_Stereo_Volume
    mediaplayer = player_main
    volume = 0.75

Mute button
    MediaPlayer_Mute(player_main)

Unmute button
    MediaPlayer_Unmute(player_main)
```

Mute/Unmute အတွက် မူလ volume ကို ပြန်သုံးလိုပါက volume value ကို Number variable တစ်ခုတွင် သီးခြားသိမ်းပြီး `Set Volume` block ဖြင့် ပြန်သတ်မှတ်ပါ။

## Loop Playback

```text
MediaPlayer_Set_Looping
    mediaplayer = player_main
    enabled = true
```

Loop status ကို `MediaPlayer_Is_Looping` ဖြင့် စစ်နိုင်သည်။ Single audio နှင့် music player workflow များတွင် loop setting ကို media prepared event သို့မဟုတ် play မစမီ သတ်မှတ်ပါ။

## Playback Time Format

`MediaPlayer_Format_Millis` သည် milliseconds ကို `MM:ss` အဖြစ် ပြောင်းပြီး `MediaPlayer_Format_Millis_Hours` သည် `HH:mm:ss` အဖြစ် ပြောင်းသည်။

```text
position = MediaPlayer_Get_Current_Position(player_main)
current_text = MediaPlayer_Format_Millis(position)
set current_text into txt_current_time

remaining_text = MediaPlayer_Get_Remaining_Text(player_main)
set remaining_text into txt_remaining_time
```

Long audio များတွင် `HH:mm:ss` ကို သုံးပါ။ Duration သို့မဟုတ် position မရသေးသောအခြေအနေတွင် 0 value ကို မျှော်မှန်းပြီး UI ကို မပြိုကွဲအောင် စီမံပါ။

## Professional Resource Lifecycle

MediaPlayer object သည် memory နှင့် codec resource များကို အသုံးပြုသောကြောင့် အသုံးပြုပြီးနောက် release လုပ်ရန်လိုသည်။ Activity ပိတ်ခြင်း၊ screen ပြောင်းခြင်း၊ playback ပြီးဆုံးခြင်း သို့မဟုတ် player အသစ်ပြန်ဖန်တီးခြင်းအချိန်တွင် Project ၏ lifecycle design နှင့်ကိုက်ညီသောနေရာတွင် `MediaPlayer_Stop_Release` သို့မဟုတ် `MediaPlayer_Release_If_Ready` ကို သုံးပါ။

Release ပြီးသွားသော player ကို ပြန်၍ start, pause, seek သို့မဟုတ် duration ရယူခြင်း မလုပ်ရ။ Player အသစ်တစ်ခုလိုပါက Project ၏ standard prepare/create workflow ဖြင့် ပြန်လည်တည်ဆောက်ရမည်။

## Error နှင့် State Safety

MediaPlayer သည် state machine ဖြစ်သောကြောင့် prepare မပြီးမီ playback operation ခေါ်ခြင်း၊ stop ပြီးနောက် prepare မလုပ်ဘဲ start ခေါ်ခြင်း၊ release ပြီးနောက် method ခေါ်ခြင်းတို့သည် error ဖြစ်စေနိုင်သည်။ Error event ကို အသုံးပြုပြီး user-friendly message ပြပါ။

```text
onPrepared
    enable play controls

onCompletion
    update UI to completed state
    if loop is disabled
        reset progress display

onError
    show playback error
    reset or release player according to the recovery plan
```

Network stream များတွင် buffering, timeout, unsupported format နှင့် server failure များကို ထည့်သွင်းစဉ်းစားပါ။ Timer ဖြင့် progress update လုပ်ပါက player မဖွင့်နေချိန်တွင် မလိုအပ်ဘဲ အမြဲ run မနေစေရန် status စစ်ပါ။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.mediaplayer` selector အသုံးပြုထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, fixed View ID နှင့် `this` မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$d` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | MediaPlayer component ID, View ID နှင့် App-specific variable များကို hard-code မလုပ်ထားပါ |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Prepared ပြီး Start | Audio/video စတင်ဖွင့်သည် |
| Pause → Start | Playback position ဆက်လက်မှန်ကန်သည် |
| Stop → Reset workflow | Player ကို သင့် Project workflow အတိုင်း ပြန်ပြင်ဆင်နိုင်သည် |
| Seek 0%/100% | အစနှင့်အဆုံးသို့ မကျော်ဘဲ ရွှေ့သည် |
| Forward/Backward | Position သည် duration ထက်မကျော်၊ 0 အောက်မဆင်း |
| Volume 0/1 | Mute နှင့် full volume မှန်ကန်သည် |
| Loop enabled | Playback ပြီးနောက် ထပ်ဖွင့်သည် |
| Completion event | UI state မှန်ကန်စွာ ပြောင်းသည် |
| Error event | App မ crash ဘဲ user-friendly error ပြသည် |
| Activity exit | Resource release ဖြစ်ပြီး playback မဆက်လက်လုပ်ပါ |

## Compatibility Note

Sketchware Pro version နှင့် MediaPlayer component implementation အလိုက် method name သို့မဟုတ် built-in event label ကွာနိုင်ပါသည်။ ဤ library သည် `start()`, `pause()`, `stop()`, `reset()`, `release()`, `isPlaying()`, `seekTo()`, `getCurrentPosition()`, `getDuration()`, `setVolume()`, `setLooping()` နှင့် `isLooping()` API များကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် Sketchware version ၏ built-in MediaPlayer blocks နှင့် generated source code ကို တိုက်စစ်ပြီး method name နှင့် state workflow ကို version-compatible ပြင်ဆင်ပါ။

ဤ Custom Blocks များသည် playback control နှင့် utility logic ကို ပေးထားခြင်းဖြစ်ပြီး media source loading/prepare နှင့် completion/error callbacks များကို သင့် Project ၏ built-in MediaPlayer event workflow အတွင်းတွင် ဆက်လက်စီမံရမည်။

## References

[1] Android Developers, “MediaPlayer API reference,” https://developer.android.com/reference/android/media/MediaPlayer  
[2] Sketchware Pro, “Components,” https://docs.sketchware.pro/docs/course/basics/component/  
[3] Sketchyas, “Sketchware Pro Course — MediaPlayer Component,” https://sketchyas.com/sketchware-course
