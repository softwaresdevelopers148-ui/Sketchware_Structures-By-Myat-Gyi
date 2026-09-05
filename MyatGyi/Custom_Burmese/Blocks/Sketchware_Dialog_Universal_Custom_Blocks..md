# Sketchware Dialog Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules နှင့် Sketchware Dialog component ၏ အခြေခံလုပ်ဆောင်ချက်များကို အခြေခံထားသော Universal Custom Block set ဖြစ်သည်။ `%m.dialog` component selector ကို အသုံးပြုထားသောကြောင့် Dialog component ID ကို fixed မလုပ်ထားပါ။ `dialog_main`, `confirm_dialog` သို့မဟုတ် Project ထဲရှိ မည်သည့် Dialog component မဆို dropdown မှ ရွေးချယ်အသုံးပြုနိုင်သည်။

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **27 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Title, message, show, title/message setup |
| Button | Positive, negative, neutral button များ |
| Presets | Info, confirmation, choice dialog များ |
| Safe | Empty message မဖြစ်စေရန် validation ပါသော dialog များ |
| Professional | All-button dialog, reset/setup workflow နှင့် error dialog |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `dialog_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ Dialog palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် Dialog component တစ်ခု ထည့်ထားပြီးနောက် block တစ်ခုချင်းစီ၏ `%m.dialog` dropdown မှ မိမိအသုံးပြုလိုသော Dialog component ကို ရွေးပါ။

## အခြေခံအသုံးပြုပုံ

### ရိုးရိုး Info Dialog

```text
Dialog_Set_Title_And_Message
    dialog = dialog_main
    title = "အကြောင်းကြားချက်"
    message = "သင့်အချက်အလက်ကို သိမ်းဆည်းပြီးပါပြီ။"

Dialog_Set_Positive_Button
    dialog = dialog_main
    text = "OK"

Dialog_Show
    dialog = dialog_main
```

ပိုမိုတိုတောင်းစွာရေးလိုပါက `Dialog_Prepare_Info` သို့မဟုတ် `Dialog_Prepare_And_Show` ကို အသုံးပြုနိုင်သည်။

```text
Dialog_Prepare_And_Show
    dialog = dialog_main
    title = "အကြောင်းကြားချက်"
    message = "လုပ်ဆောင်ချက်ပြီးမြောက်ပါပြီ။"
```

## Confirmation Dialog

အသုံးပြုသူထံမှ အတည်ပြုချက်ရယူလိုသော Delete, Logout, Submit သို့မဟုတ် Reset action များအတွက် Confirm Dialog ကို သုံးပါ။

```text
Dialog_Confirm_And_Show
    dialog = dialog_confirm
    title = "ဖျက်ရန် အတည်ပြုပါ"
    message = "ဤအချက်အလက်ကို အမှန်တကယ် ဖျက်လိုပါသလား။"
    positive = "ဖျက်မည်"
    negative = "မဖျက်ပါ"
```

Positive နှင့် Negative button ကို သတ်မှတ်ခြင်းသည် button စာသားနှင့် Dialog ပြသမှုကိုသာ ပြင်ဆင်ပေးသည်။ Button နှိပ်ပြီးနောက် လုပ်ဆောင်ချက်ကို Dialog component ၏ သက်ဆိုင်ရာ event ထဲတွင် ထည့်ရမည်။

```text
Dialog component → OK/Positive Button Clicked event
    delete record
    show success message

Dialog component → Cancel/Negative Button Clicked event
    close current action
```

## Button သုံးမျိုးပါ Choice Dialog

Positive, Negative နှင့် Neutral button သုံးမျိုးလိုသောအခါ `Dialog_Prepare_Choice` သို့မဟုတ် `Dialog_Prepare_Three_Choice_And_Show` ကို အသုံးပြုနိုင်သည်။ Button တစ်ခုချင်းစီ၏ event အတွင်း သီးခြား logic ရေးပါ။

```text
Dialog_Prepare_Three_Choice_And_Show
    dialog = dialog_choice
    title = "ရွေးချယ်ပါ"
    message = "မည်သည့်လုပ်ဆောင်ချက်ကို ရွေးမလဲ။"
    positive = "သိမ်းမည်"
    negative = "ပယ်ဖျက်မည်"
    neutral = "နောက်မှလုပ်မည်"
```

## Safe Validation Blocks

User input, API response သို့မဟုတ် variable ထဲမှ message ကို Dialog တွင် ပြမည့်အခါ empty value ကို စစ်ပါ။ `Dialog_Show_If_Message_Not_Empty` သည် message မလွတ်လပ်မှသာ Dialog ကို ပြသည်။ `Dialog_Set_Confirm_And_Show_If_Valid` သည် message မလွတ်လပ်မှသာ title, message, buttons နှင့် Dialog ပြသမှုကို လုပ်ဆောင်သည်။

```text
get response_message

Dialog_Show_If_Message_Not_Empty
    message = response_message
    dialog = dialog_info
```

Input မမှန်သည့်အခါ Error Dialog ပြလိုပါက `Dialog_Show_Error_If_Invalid` ကို သုံးနိုင်သည်။

```text
Dialog_Show_Error_If_Invalid
    message_to_check = user_input
    dialog = dialog_error
    title = "အချက်အလက် မပြည့်စုံပါ"
    error_message = "လိုအပ်သောအချက်အလက်ကို ဖြည့်ပေးပါ။"
```

## Dialog Lifecycle Workflow

Dialog ကို ပြသခြင်း၊ button event ကို ကိုင်တွယ်ခြင်းနှင့် screen ပြောင်းခြင်းတို့ကို အဆင့်ခွဲရေးပါ။ Dialog ပြသမည့် event ထဲတွင် setup block ပြီးမှ show block ကို ခေါ်ပါ။ Button event ထဲတွင် action ပြီးဆုံးသောအခါ နောက်ထပ် Dialog ပြသမည်ဆိုလျှင် မိမိ Project ၏ Dialog event flow ကို စနစ်တကျ ထိန်းပါ။

```text
Open screen / Button clicked
    prepare title and message
    configure required buttons
    show dialog

Positive button clicked
    validate user decision
    perform action
    update UI

Negative button clicked
    cancel action
    return to previous state
```

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.dialog` selector အသုံးပြုထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, fixed View ID, `this` မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် `#FF000000` သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$s` စသည်ဖြင့် position symbol များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | Dialog component ID နှင့် app-specific view များကို hard-code မလုပ်ထားပါ |

## Professional Design Recommendations

Dialog ကို သတိပေးချက်တိုင်းအတွက် မသုံးဘဲ အသုံးပြုသူ၏ ဆုံးဖြတ်ချက် လိုအပ်သောအခြေအနေတွင်သာ သုံးပါ။ Message ကိုတိုတောင်းရှင်းလင်းစွာ ရေးပြီး Positive button ကို အဓိကလုပ်ဆောင်ချက်၊ Negative button ကို ပယ်ဖျက်ခြင်းအဖြစ် သတ်မှတ်ပါ။ Destructive action များအတွက် message ထဲတွင် အကျိုးဆက်ကို ရှင်းပြပြီး default action ကို မမြန်မြန်ဆန်ဆန် ရွေးစေသင့်ပါ။

API response သို့မဟုတ် user input ကို Dialog message အဖြစ် ပြရာတွင် null/empty value ကို စစ်ပါ။ Sensitive data, password, API key သို့မဟုတ် token များကို Dialog ထဲ မပြပါနှင့်။ Long text များအတွက် Dialog အတွင်းအတင်းထည့်မည့်အစား သီးခြား screen သို့မဟုတ် scrollable layout ကို စဉ်းစားပါ။

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Title နှင့် message သတ်မှတ်ပြီး Show | Dialog မှန်ကန်စွာ ပေါ်သည် |
| Positive button click | Positive event logic တစ်ကြိမ်သာ run သည် |
| Negative button click | Cancel event logic အလုပ်လုပ်သည် |
| Neutral button click | Neutral event logic အလုပ်လုပ်သည် |
| Empty message | Safe block သည် မလိုအပ်ဘဲ Dialog မပြပါ |
| Null message | App crash မဖြစ်ဘဲ safe path သို့သွားသည် |
| Multiple Dialog components | Dropdown ဖြင့် သီးခြားထိန်းချုပ်နိုင်သည် |
| Screen exit/reopen | Old state ကြောင့် duplicate behavior မဖြစ်ပါ |

## Compatibility Note

Sketchware Pro version အလိုက် Dialog component ၏ built-in method name သို့မဟုတ် button event label ကွာနိုင်ပါသည်။ ဤ library သည် `setTitle`, `setMessage`, `setPositiveButton`, `setNegativeButton`, `setNeutralButton` နှင့် `show` ပုံစံကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် Sketchware version ၏ built-in Dialog blocks နှင့် generated source code ကို တိုက်စစ်ပြီး method name ကို version-compatible ပြင်ဆင်ပါ။

ဤ Custom Blocks များသည် Dialog ကို configure နှင့် show လုပ်ရန် ဖြစ်ပြီး Positive/Negative/Neutral button နှိပ်ပြီးနောက် လုပ်ဆောင်ချက်ကို Dialog component event ထဲတွင် သီးခြားထည့်ရမည်။ ထိုပုံစံကြောင့် block များသည် App-specific callback code မပါဘဲ Universal အဖြစ် ပြန်လည်အသုံးပြုနိုင်သည်။

## References

[1] Sketchware Docs Unofficial, “Dialog,” https://sketchwaredocs.gitbook.io/home/components/dialog  
[2] Sketchware Docs, “Dialog,” https://sketchware-docs.vercel.app/docs/component-dialog.html  
[3] Sketchware Pro Docs, “Import custom blocks,” https://docs.sketchware.pro/docs/blocks/Custom%20block/import-blocks/
