# FilePicker Universal Custom Blocks

ဒီ JSON သည် Sketchware Pro ၏ FilePicker component (`%m.filepicker`) အတွက် Master Rules အတိုင်းရေးထားသော universal custom blocks ဖြစ်သည်။

## Component ထည့်သွင်းခြင်း

FilePicker component ကို ထည့်ရာတွင် မူလအတိုင်း—

- Component Name သတ်မှတ်ပါ
- Select MIME Type တွင် All files, Image files, Audio files သို့မဟုတ် Text files ကို ရွေးပါ
- Component ထည့်ပြီးနောက် `onFilesPicked` နှင့် `onFilesPickedCancel` event များကို အသုံးပြုပါ

## အရေးကြီးသော Launch Rule

FilePicker ကို ဖွင့်ရန် JSON ထဲတွင် launch code ကို ထပ်မဖန်တီးထားပါ။ Sketchware Pro ၏ built-in `filepickerstartpickfiles` block ကို အသုံးပြုရပါမည်။

အကြောင်းရင်းမှာ FilePicker component တစ်ခုချင်းစီအတွက် Sketchware Pro က `REQ_CD_<COMPONENT_NAME>` request-code constant ကို အလိုအလျောက်ထုတ်ပေးပြီး `onFilesPicked` / `onFilesPickedCancel` event များနှင့် ချိတ်ဆက်ပေးခြင်းဖြစ်သည်။ Custom JSON block မှ component name ကို compile-time request-code constant အဖြစ် ပြောင်းလဲမပေးနိုင်သောကြောင့် launch block ကို အတုယူခြင်းသည် universal မဖြစ်နိုင်ပါ။

## onFilesPicked Event

`onFilesPicked` event အတွင်း Sketchware Pro က `ArrayList<String> _filePath` ကို အလိုအလျောက် ဖန်တီးပေးသည်။ JSON ထဲရှိ `picked...` blocks များသည် ထို event အတွင်းမှာသာ အသုံးပြုရန်ဖြစ်သည်။

- `FilePicker_pickedFileCount` — ရွေးထားသော File အရေအတွက်
- `FilePicker_firstPickedFile` — ပထမဆုံး Path
- `FilePicker_pickedFileAt` — Index အလိုက် Path
- `FilePicker_copyPickedPathsToList` — ကိုယ်ပိုင် String List ထဲသို့ ကူးယူခြင်း
- `FilePicker_joinPickedPaths` — Path များကို စာသားတစ်ကြောင်းအဖြစ် ပေါင်းခြင်း

`onFilesPickedCancel` event တွင် `_filePath` မရှိသောကြောင့် `picked...` blocks များကို ထို event ထဲတွင် မသုံးပါနှင့်။

## Permission နှင့် Compatibility

File Picker က ပြန်ပေးသော Path များကို `File` API ဖြင့် စစ်ဆေးရန်အတွက် `exists`, `isFile`, `canRead`, size နှင့် MIME helper များ ပါဝင်သည်။ URI ကို အခြား App သို့ မျှဝေမည်ဆိုပါက Read Permission သို့မဟုတ် Persistable Permission block ကို သင့်အသုံးပြုမည့် Intent Action နှင့် ကိုက်ညီအောင် ထည့်ပါ။

`removeDuplicatePaths` သည် မူလအစဉ်ကို ထိန်းထားပြီး နောက်မှ ထပ်နေသော Path များကို ဖယ်ရှားသည်။ `filterPathsByExtension` သည် List ကို in-place ပြင်ဆင်သောကြောင့် မူလ List data ကို ပြောင်းလဲသွားမည်ဖြစ်သည်။