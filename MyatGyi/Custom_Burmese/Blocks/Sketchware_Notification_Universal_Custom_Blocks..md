# Sketchware Notification Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware Notification component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.notification` selector ကို အသုံးပြုထားသောကြောင့် Notification component ID, Activity ID, View ID နှင့် app-specific notification ID များကို fixed မလုပ်ထားပါ။

Notification သည် App ကို မဖွင့်ထားချိန်တွင် user ထံ timely information ပေးရန် အသုံးပြုသည်။ Android 8.0/API 26 နှင့်အထက်တွင် notification အားလုံးကို channel တစ်ခုနှင့် ချိတ်ဆက်ရပြီး Android 13/API 33 နှင့်အထက်တွင် `POST_NOTIFICATIONS` runtime permission လိုအပ်နိုင်သည်။ [1] [2] [3]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **43 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Title, text, subtext, small icon, channel ID, auto-cancel, ongoing |
| Post | Notify, cancel, cancel all, priority, timestamp |
| Channel | Default/high/low importance channel create/delete |
| Progress | Determinate, indeterminate, clear, update, finish |
| Group | Group key, group summary, only-alert-once |
| Safe | Valid channel/content/ID စစ်ပြီး post/update |
| Professional | Big text, tap intent, visibility, silent mode, lifecycle cancellation |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `notification_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ Notification palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် Notification component တစ်ခုရှိရမည်။ Block တစ်ခုချင်းစီ၏ `%m.notification` dropdown မှ မိမိအသုံးပြုလိုသော component ကို ရွေးပါ။

## အခြေခံ Notification Workflow

```text
App start / first notification use
    create a channel

Before posting
    set title
    set content text
    set small icon
    set channel ID
    post with a unique notification ID
```

Android 8.0/API 26 နှင့်အထက်တွင် channel မဖန်တီးဘဲ notification post လုပ်ပါက notification မပေါ်နိုင်သောကြောင့် App စတင်ချိန် သို့မဟုတ် ပထမဆုံး notification မပို့မီ channel create လုပ်ပါ။ Existing channel ကို ထပ်မံ create လုပ်ခြင်းသည် safe ဖြစ်ပြီး channel ၏ importance/behavior ကို user က သတ်မှတ်ပြီးပါက code ဖြင့် ပြန်မပြောင်းနိုင်ပါ။ [2]

```text
Notification_Create_Default_Channel
    notification = notification_component
    channel_id = "general"
    name = "General"
    description = "General app updates"
```

## Content သတ်မှတ်ခြင်း

Notification ၏ small icon သည် အရေးကြီးသော required visual element တစ်ခုဖြစ်ပြီး title နှင့် text သည် user ဖတ်ရန် အဓိက content ဖြစ်သည်။ [4]

```text
Notification_Set_Title(notification_component, "Download complete")
Notification_Set_Text(notification_component, "Your file is ready")
Notification_Set_Small_Icon(notification_component, icon_resource)
Notification_Set_Channel_Id(notification_component, "general")
Notification_Notify(notification_component, 1001)
```

Notification ID `1001` သည် ဥပမာသာဖြစ်ပြီး App logic ထဲတွင် မိမိသတ်မှတ်ထားသော variable သို့မဟုတ် constant ကို ထည့်ပါ။ Notification ID တူညီစွာ ပြန် post လုပ်ပါက notification အသစ်တစ်ခု ထပ်မပေါ်ဘဲ ရှိပြီးသား notification ကို update လုပ်နိုင်သည်။

## Android 13 Notification Permission

Android 13/API 33 နှင့်အထက်တွင် normal notification များအတွက် `POST_NOTIFICATIONS` runtime permission လိုအပ်နိုင်သည်။ Manifest တွင် permission ကြေညာပြီး user ကို runtime တွင် တောင်းခံပါ။ User က deny လုပ်ပါက normal notification များ မပေါ်နိုင်သောကြောင့် App သည် permission မရှိသည့်အခြေအနေကို မျှော်မှန်းထားရမည်။ [3]

```text
if notification permission is granted
    create channel
    post notification
else
    show an in-app explanation or settings guidance
```

Permission request ကို App ဖွင့်သည်နှင့် အလိုအလျောက်မတောင်းဘဲ user က notification feature ကို အသုံးပြုလိုသည့် context တွင် ရှင်းပြပြီး တောင်းရန် Android guidance က အကြံပြုထားသည်။ [3]

## Priority နှင့် Channel Importance

Android 8.0/API 26+ တွင် notification interruption behavior ကို channel importance က အဓိကဆုံးဖြတ်သည်။ Android 7.1 နှင့်အောက်တွင် priority ကို သုံးရနိုင်သည်။ Channel ကို system သို့ register ပြီးနောက် importance ကို code ဖြင့် ပြောင်း၍မရဘဲ user setting ကသာ နောက်ဆုံးဆုံးဖြတ်သည်။ [2]

| အသုံးပြုမှု | Channel အကြံပြုချက် |
|---|---|
| General update | Default |
| Urgent user action | High, မလိုအပ်ဘဲ မသုံးရန် |
| Background sync | Low |
| Silent status | Low နှင့် Silent notification |

High importance notification ကို စာတိုင်းအတွက် မသုံးပါနှင့်။ User attention ကို လိုအပ်သည့် အခြေအနေများတွင်သာ အသုံးပြုပါ။

## Auto-Cancel နှင့် Ongoing

`Notification_Set_Auto_Cancel` ကို true ထားပါက user က notification ကိုနှိပ်ပြီးနောက် notification ပျောက်စေရန် အသုံးပြုနိုင်သည်။ `Ongoing` ကို download, upload, foreground operation စသည့် အလုပ်လုပ်နေစဉ် status notification များတွင် သုံးနိုင်သော်လည်း အလုပ်ပြီးဆုံးချိန်တွင် false ပြန်ထားခြင်း သို့မဟုတ် cancel လုပ်ခြင်း ပြုလုပ်ပါ။

```text
Notification_Set_Auto_Cancel(notification_component, true)
Notification_Set_Ongoing(notification_component, false)
```

## Tap Action နှင့် Action Button

Notification ကို user နှိပ်သောအခါ သက်ဆိုင်ရာ screen ကို ဖွင့်ပေးရန် PendingIntent သတ်မှတ်ပါ။ `Notification_Set_Content_Intent` သည် PendingIntent input ကို လက်ခံသည်။ PendingIntent ကို App ထဲရှိ target Activity နှင့် expected back-stack behavior အတိုင်း ကြိုတင်တည်ဆောက်ထားရမည်။ [4]

Notification action button များသည် quick response အတွက် ဖြစ်ပြီး main notification tap action နှင့် တူညီသော action ကို ထပ်မလုပ်သင့်ပါ။ Component version တွင် action-specific blocks ပါပါက unique action request code နှင့် immutable/mutable flag ကို Android target အလိုက် မှန်ကန်စွာ သတ်မှတ်ပါ။

## Progress Notification

Download/upload ကဲ့သို့ total size သိသော operation အတွက် determinate progress ကို သုံးပါ။ Total size မသိသောအခါ indeterminate progress ကို သုံးပါ။

```text
Notification_Set_Progress
    notification = notification_component
    max = total_items
    current = completed_items

Notification_Update_Progress
    notification = notification_component
    notification_id = 2001
    max = total_items
    current = completed_items
```

Same notification ID ဖြင့် update လုပ်ပါက notification တစ်ခုတည်း၏ progress ပြောင်းသည်။ Current value သည် 0 မှ max အတွင်း safe clamp လုပ်ထားသည်။ Operation ပြီးဆုံးသောအခါ `Notification_Finish_Progress` သို့မဟုတ် `Notification_Stop_And_Cancel` ကို သုံးပြီး stale notification မကျန်စေရန် စီမံပါ။

```text
operation success
    Notification_Finish_Progress(notification_component, 2001, "Completed")

operation error/cancel
    Notification_Stop_And_Cancel(notification_component, 2001)
```

## Notification Grouping

Related notifications များကို group key တစ်ခုဖြင့် စုစည်းနိုင်သည်။ Group summary notification ကို သီးခြား post လုပ်ပြီး `Set Group Summary` ကို true ထားပါက system notification shade တွင် group အဖြစ် ပြသနိုင်သည်။ [5]

```text
Notification_Set_Group(notification_component, "downloads")
Notification_Set_Group_Summary(notification_component, false)
```

User-visible group meaning ရှင်းလင်းစေရန် group key ကို stable string အဖြစ် သတ်မှတ်ပါ။ Account တစ်ခုချင်းစီ သို့မဟုတ် feature တစ်ခုချင်းစီအလိုက် group key ခွဲနိုင်သည်။

## Big Text, Silent နှင့် Visibility

Content text သည် တစ်ကြောင်းထက်ရှည်ပါက component တွင် Big Text support ရှိလျှင် `Notification_Set_Big_Text` ကို သုံးပါ။ Sensitive content များကို lock screen တွင် မပြလိုပါက visibility ကို project/API version အလိုက် သတ်မှတ်ပြီး `Silent` ကို user preference နှင့် ကိုက်ညီအောင် သုံးပါ။ Notification သည် user privacy ကို မချိုးဖောက်စေရန် message ထဲတွင် password, token, private chat content သို့မဟုတ် full personal data မထည့်ပါနှင့်။

## Safe နှင့် Professional Workflow

```text
prepare notification
    if channel_id is not empty
        create channel if required
    set title/text only if not empty
    set small icon
    set channel ID

if notification permission is available
    notify with a stable ID
else
    handle permission-denied path
```

`Notification_Post_With_Channel` သည် channel ID မလွတ်လပ်မှသာ channel ကို set လုပ်ပြီး post လုပ်သည်။ `Notification_Notify_If_Valid_Id` သည် negative ID များကို တားဆီးသည်။ Content မလွတ်လပ်စေရန် Safe title/text blocks ကို အသုံးပြုနိုင်သည်။

## Lifecycle နှင့် Cancellation

Notification သည် App UI နှင့်မတူဘဲ App မဖွင့်ထားချိန်တွင်လည်း ကျန်နေနိုင်သောကြောင့် operation ပြီးဆုံးခြင်း၊ error ဖြစ်ခြင်း၊ user cancel လုပ်ခြင်းနှင့် retry မဖြစ်နိုင်ခြင်း path အားလုံးတွင် update သို့မဟုတ် cancel ပြုလုပ်ပါ။ Foreground operation အတွက် ongoing notification သုံးပါက operation အဆုံးတွင် ongoing state ကို ဖယ်ပြီး notification ကို cancel လုပ်ပါ။

Channel ကို App တစ်ခုလုံးအတွက် stable ID ဖြင့် စီမံပြီး notification ID ကို feature/record အလိုက် တည်ငြိမ်စွာ သတ်မှတ်ပါ။ Random ID များကို မထိန်းချုပ်ဘဲ သုံးပါက cancel/update မလုပ်နိုင်ဘဲ notification အဟောင်းများ စုပုံနိုင်သည်။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.notification` selector သုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, View ID, package name နှင့် notification ID မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$b` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | Title, text, channel, ID, progress, group နှင့် behavior များကို input အဖြစ် လက်ခံထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Android 8+ | Channel မရှိဘဲ post မဖြစ်ပါ |
| Android 13+ | POST_NOTIFICATIONS permission path မှန်ကန်သည် |
| Stable ID | Same ID ဖြင့် update/cancel လုပ်နိုင်သည် |
| Small icon | Notification မပျက်ဘဲ ပေါ်သည် |
| Tap action | PendingIntent ဖြင့် သတ်မှတ်ထားသော screen ဖွင့်သည် |
| Progress | Current သည် 0–max အတွင်း update သည် |
| Completion | Finish/cancel ပြီး stale notification မကျန်ပါ |
| Grouping | Related notification များ group အဖြစ် စုစည်းသည် |
| Privacy | Lock screen/log တွင် sensitive content မဖော်ပြပါ |
| Permission denied | App မ crash ဘဲ user guidance ပြသည် |
| Channel setting | User-controlled importance ကို code ဖြင့် မအတင်းပြောင်းပါ |

## Compatibility Note

Sketchware Pro version အလိုက် Notification component ၏ generated method name၊ Notification Builder field၊ channel support နှင့် PendingIntent/action blocks ကွာနိုင်ပါသည်။ ဤ library သည် `setContentTitle`, `setContentText`, `setSubText`, `setSmallIcon`, `setChannelId`, `setAutoCancel`, `setOngoing`, `setPriority`, `setWhen`, `setShowWhen`, `setProgress`, `setGroup`, `setGroupSummary`, `setOnlyAlertOnce`, `setContentIntent`, `setVisibility`, `setSilent`, `notify`, `cancel`, `cancelAll` နှင့် channel create/delete ပုံစံများကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in Notification blocks နှင့် generated source code ကို တိုက်စစ်ပါ။

Android 8.0/API 26 နှင့်အထက်တွင် channel ကို အရင် create လုပ်ပြီး Android 13/API 33 နှင့်အထက်တွင် notification permission ကို handle လုပ်ပါ။ Modern Android project အသစ်များတွင် AndroidX `NotificationCompat` နှင့် project-supported notification APIs ကို ဦးစားပေးစဉ်းစားပါ။

## References

[1] Android Developers, “About notifications in Views,” https://developer.android.com/develop/ui/views/notifications  
[2] Android Developers, “Create and manage notification channels,” https://developer.android.com/develop/ui/views/notifications/channels  
[3] Android Developers, “Notification runtime permission,” https://developer.android.com/develop/ui/views/notifications/notification-permission  
[4] Android Developers, “Create a notification,” https://developer.android.com/develop/ui/views/notifications/build-notification  
[5] Android Developers, “Create a group of notifications,” https://developer.android.com/develop/ui/views/notifications/group
