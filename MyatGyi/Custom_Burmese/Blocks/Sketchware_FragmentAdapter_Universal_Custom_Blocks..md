# Sketchware FragmentAdapter Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware FragmentAdapter component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.fragmentadapter` selector ကို အသုံးပြုထားသောကြောင့် FragmentAdapter component ID, Activity ID, View ID နှင့် app-specific tab ID များကို fixed မလုပ်ထားပါ။

FragmentAdapter သည် tab position တစ်ခုနှင့် Fragment တစ်ခုကို ချိတ်ဆက်ပေးပြီး ViewPager/TabLayout စသည့် swipe-tab UI များတွင် page navigation ကို စီမံပေးသည်။ Android documentation အရ fixed page အနည်းငယ်အတွက် FragmentPagerAdapter ပုံစံနှင့် page အရေအတွက်များ/ပြောင်းလဲနိုင်သော data အတွက် FragmentStatePagerAdapter ပုံစံကို စဉ်းစားနိုင်သည်။ Legacy ViewPager သည် deprecated ဖြစ်ပြီး project အသစ်များတွင် ViewPager2 ကို ဦးစားပေးသင့်သော်လည်း Sketchware component version ပေါ်မူတည်၍ FragmentAdapter API ကွာနိုင်သည်။ [1]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **43 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Tab count, current position, notify data, swipe enabled |
| Event | `onTabAdded` title return နှင့် `onFragmentAdded` fragment return |
| Tab Data | Title, icon, badge, position validation |
| Safe | Valid position/title/icon/count နှင့် refresh workflow |
| Professional | Add/remove/move/clear tab, first/last select |
| Dynamic | List/Map အလိုက် tab title, rebuild နှင့် event synchronization |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `fragmentadapter_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ FragmentAdapter palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် FragmentAdapter component တစ်ခုရှိရမည်။ Block တစ်ခုချင်းစီ၏ `%m.fragmentadapter` dropdown မှ မိမိအသုံးပြုလိုသော component ကို ရွေးပါ။ FragmentAdapter ကို ViewPager/TabLayout configuration နှင့် ချိတ်ထားပါက adapter data ပြောင်းလဲပြီးနောက် notify block ကို အသုံးပြုပါ။

## `onTabAdded` Event

`onTabAdded` event ၏ အဓိကတာဝန်မှာ tab တစ်ခုချင်းစီအတွက် ပြသမည့် title ကို return ပြန်ပေးခြင်းဖြစ်သည်။ User ပေးထားသော title list သို့မဟုတ် position အလိုက် dynamic title ကို သုံးနိုင်သည်။

```text
FragmentAdapter → onTabAdded
    return "Home"
```

Title list သုံးသော workflow တွင် position range ကို စစ်ပြီး title ပြန်ပေးပါ။

```text
FragmentAdapter → onTabAdded
    title = titles[position]
    return title
```

`FragmentAdapter_Return_Tab_Title_Safe` သည် title လွတ်နေပါက fallback title ပြန်ပေးနိုင်သည်။ Event ထဲတွင် return type သည် String ဖြစ်ရမည်။

```text
FragmentAdapter_Return_Tab_Title_Safe
    title = title_from_list
    fallback = "Untitled"
```

> Sketchware Pro version အလိုက် `onTabAdded` event ၏ position parameter name သို့မဟုတ် parameter ရှိ/မရှိ ကွာနိုင်သည်။ Event ထဲတွင် position မရပါက static return block သို့မဟုတ် project variable မှ title ရယူသည့် workflow ကို သုံးပါ။

## `onFragmentAdded` Event

`onFragmentAdded` event ၏ အဓိကတာဝန်မှာ tab position သို့မဟုတ် adapter request အလိုက် Fragment object/class ကို return ပြန်ပေးခြင်းဖြစ်သည်။

```text
FragmentAdapter → onFragmentAdded
    return new HomeFragmentActivity()
```

Sketchware tutorial convention အချို့တွင် Fragment Activity class အသစ်ကို return ပြန်သည့်ပုံစံကို အသုံးပြုထားသည်။ ဥပမာ `return new AbcFragmentActivity();` ဖြစ်ပြီး `Abc` သည် project ထဲရှိ သက်ဆိုင်ရာ Fragment Activity အမည်ဖြင့် အစားထိုးရသည်။ [2]

```text
FragmentAdapter_Return_New_Fragment
    fragment_class = HomeFragmentActivity
```

ပြီးသား Fragment object ကို ပြန်ပေးလိုပါက `FragmentAdapter_Return_Fragment` ကို သုံးပါ။ Null ဖြစ်နိုင်သော object များတွင် `FragmentAdapter_Return_Fragment_Safe` ကို သုံးပြီး default Fragment fallback ထားပါ။

> `onFragmentAdded` event တွင် အသုံးပြုရမည့် class name သည် project-specific ဖြစ်သောကြောင့် fixed class name ကို library အတွင်း မထည့်ထားပါ။ Block input အဖြစ် သင့် Project ထဲမှ Fragment Activity/class ကို ရွေးပါ။

## Tab Count နှင့် Current Position

Dynamic tab data အတွက် အရင်ဆုံး tab count သတ်မှတ်ပြီး adapter data change ကို အသိပေးပါ။

```text
FragmentAdapter_Set_Tab_Count
    fragmentadapter = fragment_adapter
    count = titles.size

FragmentAdapter_Notify_Data_Changed
    fragmentadapter = fragment_adapter
```

လက်ရှိရွေးထားသော page ကို `Get Current Position` ဖြင့် ရယူပြီး position ပြောင်းရန် `Set Current Position` ကို သုံးပါ။ Position သည် 0-based ဖြစ်သဖြင့် ပထမ tab = 0 ဖြစ်သည်။

```text
FragmentAdapter_Set_Position_Safely
    fragmentadapter = fragment_adapter
    position = 1
```

Position ကို တိုက်ရိုက်သတ်မှတ်မည့်အစား `Is Valid Position` သို့မဟုတ် safe set block ကို သုံးပါ။ Tab မရှိသေးဘဲ position သတ်မှတ်ပါက crash သို့မဟုတ် invalid page ဖြစ်နိုင်သည်။

## Tab Title, Icon နှင့် Badge

Tab metadata ကို adapter အတွင်း သတ်မှတ်ထားနိုင်သည့် version များတွင် အောက်ပါ blocks ကို သုံးနိုင်သည်။

```text
FragmentAdapter_Set_Tab_Title
    fragmentadapter = fragment_adapter
    position = 0
    title = "Home"

FragmentAdapter_Set_Tab_Icon
    fragmentadapter = fragment_adapter
    position = 0
    icon_resource = icon_home
```

Badge လိုအပ်ပါက `Set Tab Badge` ကို သုံးပြီး မလိုတော့သောအခါ `Clear Tab Badge` ကို သုံးပါ။ Position range နှင့် title empty state ကို စစ်လိုပါက safe variants ကို ဦးစားပေးပါ။

## Add, Remove, Move နှင့် Clear

Project ၏ FragmentAdapter implementation သည် runtime tab mutation ကို ထောက်ပံ့ပါက Professional blocks များဖြင့် tab data ကို ပြောင်းလဲနိုင်သည်။

```text
FragmentAdapter_Add_Tab
    fragmentadapter = fragment_adapter
    title = "Settings"
    fragment = new SettingsFragmentActivity()

FragmentAdapter_Move_Tab
    fragmentadapter = fragment_adapter
    from_position = 2
    to_position = 0

FragmentAdapter_Remove_Tab
    fragmentadapter = fragment_adapter
    position = 3

FragmentAdapter_Notify_After_Tab_Change
    fragmentadapter = fragment_adapter
```

အချို့ Sketchware Pro version များတွင် `addTab`, `removeTab`, `moveTab` သို့မဟုတ် `clearTabs` method မပါနိုင်ပါ။ ထိုအခြေအနေတွင် source List/Map data ကို ပြောင်းပြီး `Set Tab Count` နှင့် `Notify Data Changed` ကို အသုံးပြုပါ။

## List/Map ဖြင့် Dynamic Tabs

Tab titles များကို List String သို့မဟုတ် Map မှ ရယူနိုင်သည်။ `onTabAdded` event တွင် position နှင့် list/map data ကို ပြန်ချိတ်ပါ။

```text
FragmentAdapter → onTabAdded
    return FragmentAdapter_Return_Tab_From_List(titles, position)
```

Map key အဖြစ် position string သုံးသော project များတွင် `FragmentAdapter_Tab_Title_From_Map` ကို သုံးနိုင်သည်။ List/Map အရွယ်အစား၊ null နှင့် position range ကို စစ်ပြီးမှ အသုံးပြုပါ။

## Swipe Control

User က swipe လုပ်ပြီး page ပြောင်းနိုင်/မနိုင်ကို `Set Swipe Enabled` ဖြင့် သတ်မှတ်နိုင်သည်။ Form wizard သို့မဟုတ် sequential flow တွင် swipe ပိတ်ပြီး Next/Back button ဖြင့်သာ navigation လုပ်စေနိုင်သည်။

```text
FragmentAdapter_Set_Swipe_Enabled
    fragmentadapter = fragment_adapter
    enabled = false
```

Swipe ပိတ်ထားခြင်းသည် programmatic `Set Current Position` ကို ပိတ်ခြင်းမဟုတ်ပါ။ App flow အလိုက် navigation button နှင့် state validation ကို သီးခြားစီမံပါ။

## Professional Event Synchronization

`onTabAdded` နှင့် `onFragmentAdded` event နှစ်ခုသည် တူညီသော position/data source ကို အခြေခံရမည်။ Title list နှင့် Fragment list အရွယ်အစား မတူပါက invalid index ဖြစ်နိုင်သောကြောင့် count ကို သေးငယ်သော list length အတိုင်း သတ်မှတ်ပါ သို့မဟုတ် safe fallback Fragment/title သုံးပါ။

```text
tab_count = min(titles.size, fragments.size)
FragmentAdapter_Set_Count_Safely(fragment_adapter, tab_count)
FragmentAdapter_Notify_Data_Changed(fragment_adapter)
```

စာမျက်နှာအရေအတွက်များ သို့မဟုတ် dynamic collection ဖြစ်ပါက fragment state ကို မလိုအပ်ဘဲ ထပ်တည်ဆောက်ခြင်း မပြုပါနှင့်။ Android guidance အရ dynamic/များပြားသော pages များတွင် state-saving adapter pattern ကို စဉ်းစားပြီး lifecycle နှင့် state restoration ကို စမ်းသပ်ပါ။ [1]

## Lifecycle နှင့် State

Activity rotation, screen change သို့မဟုတ် fragment recreation ဖြစ်သောအခါ current position, selected tab နှင့် data source ကို ပြန်တည်ဆောက်နိုင်ရမည်။ Adapter ကို view lifecycle နှင့် ချိတ်ထားသော project များတွင် old adapter reference မသုံးဘဲ current lifecycle အတွင်းရှိ adapter ကိုသာ အသုံးပြုပါ။

Android documentation သည် Fragment/ViewPager pattern တွင် Fragment lifecycle နှင့် state restoration ကို ထည့်သွင်းစဉ်းစားရန် ပြသထားသည်။ Legacy ViewPager အသုံးပြုနေပါက modern project အသစ်များအတွက် ViewPager2 migration ကို စဉ်းစားပါ။ [1]

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.fragmentadapter` selector သုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, View ID, Fragment class နှင့် Tab ID မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ အသုံးပြုထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$b` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | Position, title, fragment, icon, badge, count နှင့် data collections များကို input အဖြစ် လက်ခံထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| `onTabAdded` | Title string မှန်ကန်စွာ return ပြန်သည် |
| `onFragmentAdded` | Fragment class/object မှန်ကန်စွာ return ပြန်သည် |
| Empty title | Fallback title သို့မဟုတ် safe path သွားသည် |
| Invalid position | Page crash မဖြစ်ဘဲ safe block က တားဆီးသည် |
| Tab count | Title/Fragment data အရွယ်အစားနှင့် ကိုက်ညီသည် |
| Dynamic update | Data change ပြီးနောက် adapter refresh ဖြစ်သည် |
| Add/remove/move | Version support ရှိပါက tab order မှန်ကန်သည် |
| Swipe disabled | User swipe မလုပ်နိုင်ဘဲ programmatic navigation အလုပ်လုပ်သည် |
| Rotation | Selected position/data ပြန်ရသည် |
| Many pages | Memory/state behavior ကို စမ်းသပ်ထားသည် |
| Back navigation | Fragment/Activity back flow မပျက်ပါ |

## Compatibility Note

Sketchware Pro version အလိုက် FragmentAdapter method name၊ `onTabAdded`/`onFragmentAdded` event parameter၊ tab mutation API နှင့် generated adapter type ကွာနိုင်ပါသည်။ ဤ library သည် `setTabCount`, `getTabCount`, `notifyDataSetChanged`, `setCurrentItem`, `getCurrentItem`, `setSwipeEnabled`, `isSwipeEnabled`, `setTabTitle`, `getTabTitle`, `setTabIcon`, `setTabBadge`, `addTab`, `removeTab`, `moveTab` နှင့် `clearTabs` ပုံစံများကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in FragmentAdapter blocks နှင့် generated source code ကို တိုက်စစ်ပြီး unsupported mutation blocks များကို data-list + notify workflow ဖြင့် အစားထိုးပါ။

`onTabAdded` နှင့် `onFragmentAdded` event များကို Custom Block JSON က အလိုအလျောက် ဖန်တီးပေးခြင်းမဟုတ်ပါ။ Event များသည် Component ထည့်ပြီးသောအခါ ရရှိလာသည့် Sketchware event extension point များဖြစ်သောကြောင့် title/fragment return logic ကို သက်ဆိုင်ရာ event အတွင်းတွင် သီးခြားထည့်ရမည်။

## References

[1] Android Developers, “Create swipe views with tabs using ViewPager,” https://developer.android.com/guide/navigation/navigation-swipe-view  
[2] RUTUBE, “Fragment in ViewPager with TabLayout in Sketchware Pro,” https://rutube.ru/video/023fba0e2e4df90df11ea11a52448032/  
[3] Sketchware Pro, “Components,” https://docs.sketchware.pro/docs/course/basics/component/
