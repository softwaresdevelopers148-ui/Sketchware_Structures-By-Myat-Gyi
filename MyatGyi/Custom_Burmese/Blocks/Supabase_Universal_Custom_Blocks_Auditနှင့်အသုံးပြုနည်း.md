# Supabase Universal Custom Blocks — Audit နှင့် အသုံးပြုနည်း

ဤ library သည် ပေးထားသော Supabase Sketchware blocks များကို Master Rules အတိုင်း ပြန်လည်စစ်ဆေးပြီး သက်ဆိုင်ရာအလိုက် စုစည်းထားသော Universal Custom Block library ဖြစ်သည်။ မူရင်းဖိုင်တွင် Database, Storage, Filtering, Delete နှင့် Auth blocks များ ရောနှောနေပြီး header များထပ်နေခြင်း၊ name များမရှင်းလင်းခြင်း၊ callback fragment များကို standalone block အဖြစ်ထည့်ထားခြင်းနှင့် parameter order မကိုက်ခြင်းများရှိသည်။

## ပြင်ဆင်ပြီးသော Library

`supabase_universal_custom_blocks.json` တွင် Header 7 ခုအပါအဝင် **58 entries** ပါဝင်ပြီး အသုံးပြုနိုင်သော blocks 51 ခုရှိသည်။ Block အမည်များကို `Supabase_...` prefix ဖြင့် တစ်သမတ်တည်းရေးထားပြီး palette value အားလုံးကို Master Rules အတိုင်း empty string သတ်မှတ်ထားသည်။

| Section | အဓိက Blocks |
|---|---|
| Core, Headers နှင့် Map | Header map စတင်ခြင်း၊ API key၊ Bearer token၊ Content-Type၊ Map put/remove/clear |
| URL နှင့် Query Builder | Base URL, REST table URL, filter, order, limit, offset, query join |
| Database REST CRUD | Select, Select with query, Insert, Update, Delete, Upsert, Prefer headers |
| Response နှင့် JSON | Empty/JSON object/array စစ်ခြင်း၊ raw response သိမ်းခြင်း၊ JSON key value ဖတ်ခြင်း |
| Storage | Public URL, object URL, path encode, bucket/file validation |
| Auth | Signup, password login, reset-email request, session cleanup note |
| Quality နှင့် Security | HTTPS/base URL, table-name validation, event separation, secret-key warning |

## မူရင်းဖိုင်တွင် ပြင်ဆင်ခဲ့သော အချက်များ

မူရင်း JSON သည် parse လုပ်နိုင်သော်လည်း entry 87 ခု၊ header 16 ခု၊ `type: c` callback blocks 10 ခုနှင့် `type: e` event fragments 5 ခု ရှိခဲ့သည်။ Palette value အားလုံးသည် `35` ဖြစ်ပြီး header ထပ်နေမှုများ၊ `responseBody` getter သုံးခု၊ Auth/Storage အပိုင်းများ၏ duplicate workflow များနှင့် `six` block တွင် malformed `2%$s` placeholder ပါရှိသည်။

| မူရင်းပြဿနာ | ပြင်ဆင်ထားပုံ |
|---|---|
| `palette: "35"` ပုံသေသတ်မှတ်ထားခြင်း | `palette: ""` ပြောင်းထားသည် |
| `made_by...` header များထပ်နေခြင်း | အသုံးဝင်သော category header 7 ခုအဖြစ် ပေါင်းစည်းထားသည် |
| `c/e` callback fragments | Standalone callback blocks အဖြစ် မထည့်တော့ဘဲ RequestNetwork ၏ project event ထဲ ထည့်ရန် guide လုပ်ထားသည် |
| `getdata`, `gtwl`, `gtwl_`, `gettdt` အမည်များမရှင်းလင်းခြင်း | `Supabase_DB_Select`, `Supabase_DB_Select_Query` စသည့် ရည်ရွယ်ချက်ရှင်းသော names ပြောင်းထားသည် |
| CRUD code နှင့် spec parameter order မကိုက်ခြင်း | `%m.RequestNetwork`, Table, Query/Data, Headers, Base URL အစဉ်အတိုင်း ပြန်ချထားသည် |
| `responseBody` ကို scope မသေချာဘဲ getter block အဖြစ် အသုံးပြုခြင်း | Event ထဲမှ response ကို variable ထဲ အရင်သိမ်းပြီး JSON helper သုံးရန် ပြောင်းထားသည် |
| `2%$s` malformed placeholder | အသစ်ပြန်ရေးထားသော JSON builder blocks ဖြင့် ဖယ်ရှားထားသည် |
| Auth code တွင် API key/success/failure placeholder မကိုက်ခြင်း | Spec နှင့် code position များကို တစ်သမတ်တည်း ပြန်ညှိထားသည် |
| `runOnUiThread` ပါသော event fragments | Auth request blocks တွင်သာ scope `{ ... }` နှင့် unique `_sp_` variables သုံးထားပြီး generic callback fragment များကို ဖယ်ထားသည် |
| Service role key သုံးရန် အားပေးသကဲ့သို့ ဖြစ်ခြင်း | Client APK ထဲ service role key မထည့်ရန် အထူးသတိပေးထားသည် |

## Component နှင့် Event Workflow

Project တွင် `RequestNetwork` component ကို အရင် Add လုပ်ပါ။ Custom Block တွင် အသုံးပြုသည့် `%m.RequestNetwork` သည် project အတွင်းရှိ RequestNetwork component ကို dropdown မှ ရွေးရန်ဖြစ်သည်။ Block library သည် `onResponse` သို့မဟုတ် `onErrorResponse` event ကို ပုံသေမချိတ်ထားပါ။

```text
When btn_load clicked
    Supabase_Init_Maps(headers_map, data_map)
    Supabase_Set_Default_Headers(headers_map, supabase_anon_key)
    Supabase_DB_Select(request_network, "profiles", headers_map, supabase_url)

RequestNetwork → onResponse
    Response → response_text
    if Supabase_Response_Is_JSON_Array(response_text)
        parse response_text into the required List Map
    else
        show a friendly error

RequestNetwork → onErrorResponse
    show retry/error state
```

ဤပုံစံကြောင့် CRUD block များနှင့် event logic များသည် သီးခြားဖြစ်နေပြီး block တစ်ခုထည့်ပြီး နောက်တစ်ခုထည့်ရာတွင် callback fragment ပိတ်မပိတ်၊ `else` မကိုက်ခြင်း၊ event နှစ်ခုထပ်ချိတ်ခြင်းစသည့် error များ လျော့နည်းသည်။

## Database အသုံးပြုနည်း

### Select

`Supabase_DB_Select` သည် Table တစ်ခုလုံးကို GET ဖြင့်ဖတ်သည်။ Query လိုပါက `Supabase_DB_Select_Query` တွင် Query string ကို `select=*,limit=20` သို့မဟုတ် query builder မှ ထုတ်ပေးသော string အဖြစ်ပေးပါ။

### Insert/Update/Upsert

Insert, Update နှင့် Upsert အတွက် Data Map ကို `RequestNetworkController.REQUEST_PARAM` ဖြင့် RequestNetwork component ထဲ ထည့်ထားသည်။ သင့် Sketchware/RequestNetwork version တွင် JSON body mode သီးခြားရှိပါက built-in RequestNetwork block ၏ mode ကို တိုက်စစ်ပါ။ Complex nested JSON အတွက် map-to-JSON conversion နှင့် request body support ကို version အလိုက် စစ်ရန်လိုသည်။

Update နှင့် Delete တို့တွင် Filter string ကို `id=eq.123` ကဲ့သို့ပေးပါ။ Query value များတွင် reserved URL characters ရှိပါက filter builder သို့မဟုတ် URL encoding ကို သုံးပါ။

### Query Builder

```text
Supabase_Filter_Equals("status", "active") → status=eq.active
Supabase_Query_Order("created_at", "desc") → order=created_at.desc
Supabase_Query_Limit(20) → limit=20
Supabase_Query_Join(filter_query, order_query) → filter_query&order_query
```

Query string ကို user input နှင့် တိုက်ရိုက်ပေါင်းစပ်ရာတွင် injection နှင့် invalid URL ဖြစ်နိုင်သောကြောင့် column name ကို whitelist လုပ်ပြီး value ကို encode လုပ်ပါ။

## Response နှင့် JSON

RequestNetwork ၏ `onResponse` event ထဲတွင် response body ကို String variable ထဲ အရင်သိမ်းပါ။ ထို့နောက် `Supabase_Response_Not_Empty`, `Supabase_Response_Is_JSON_Array` သို့မဟုတ် `Supabase_Response_Is_JSON_Object` ကို အသုံးပြု၍ စစ်ပါ။ JSON key value ဖတ်ရာတွင် `Supabase_JSON_Object_Get_String` သို့မဟုတ် `Supabase_JSON_Object_Get_Number` သုံးပြီး missing key အတွက် empty string/zero fallback ရှိကြောင်း သတိပြုပါ။

```text
onResponse(responseBody)
    if response is not empty
        if response is JSON array
            convert to List Map
        else if response is JSON object
            read required key safely
        else
            show invalid-response message
```

မူရင်း `type: e` blocks များက success/failed callback fragment ကို အပြင်မှ ဆက်ပေးရန်လိုပြီး block တစ်ခုတည်းအဖြစ် import လုပ်ရာတွင် braces မကိုက်နိုင်သည်။ အသစ် library တွင် ထို fragments မပါဝင်တော့ဘဲ Event Editor ထဲတွင် success/error flow ကို သီးခြားဆောက်ရန် ရည်ရွယ်ထားသည်။

## Storage URL Helpers

Storage helper များသည် upload/download task ကို အလိုအလျောက်မလုပ်ပါ။ Public bucket အတွက် Public URL၊ private bucket အတွက် authenticated object URL သို့မဟုတ် signed URL workflow ကို အသုံးပြုပါ။ File path နှင့် bucket name ကို empty မဖြစ်စေရန် validate လုပ်ပြီး user-provided path ကို sanitize လုပ်ပါ။ Private file များအတွက် public URL ကို မဖန်တီးပါနှင့်။

## Auth Workflow

Auth blocks များသည် Supabase Auth REST endpoint သို့ asynchronous request ပြုလုပ်ပြီး success/failure action code ကို block parameter အဖြစ် လက်ခံသည်။ ဤ action code ထဲတွင် response body ကို variable ထဲသိမ်းခြင်း၊ access token ကို secure storage ထဲ ထည့်ခြင်းနှင့် UI ပြောင်းခြင်းတို့ကို ထည့်နိုင်သည်။

Password, access token, refresh token နှင့် API key များကို Logcat၊ Toast၊ public database သို့မဟုတ် screenshot ထဲ မဖော်ပြပါနှင့်။ Supabase `service_role` key သည် client APK ထဲ မထည့်ရသော privileged secret ဖြစ်ပြီး privileged operation များအတွက် trusted backend သုံးပါ။ Supabase API security သည် Row Level Security နှင့် database policy များကို မှန်ကန်စွာသတ်မှတ်ထားခြင်းအပေါ် အခြေခံရသည်။

## Import နှင့် Test Checklist

| အဆင့် | စစ်ဆေးရန် |
|---:|---|
| 1 | မူရင်း block file ကို backup လုပ်ထားပါ |
| 2 | `supabase_universal_custom_blocks.json` ကို Custom Blocks Manager ထဲ import လုပ်ပါ |
| 3 | Palette field များ empty ဖြစ်ကြောင်း စစ်ပါ |
| 4 | RequestNetwork component ကို Add လုပ်ပြီး `%m.RequestNetwork` dropdown တွင် ရွေးနိုင်ကြောင်း စစ်ပါ |
| 5 | `headers_map`, `data_map`, `response_text` variable type များကို သတ်မှတ်ပါ |
| 6 | HTTPS Base URL၊ table name နှင့် anon key ကို စစ်ပါ |
| 7 | onResponse နှင့် onErrorResponse ကို Project Event ထဲတွင် သီးခြားရေးပါ |
| 8 | Empty response, 401/403, 404, 409, 429 နှင့် offline network test လုပ်ပါ |
| 9 | Logcat တွင် secret မပါကြောင်း စစ်ပါ |
| 10 | Clean build လုပ်ပြီး actual Sketchware version တွင် compile စမ်းပါ |

## Compatibility Note

Sketchware Pro version, RequestNetwork implementation နှင့် enabled libraries အလိုက် `REQUEST_PARAM`, `PATCH`, OkHttp class package, `Gson` support နှင့် custom block placeholder behavior ကွာနိုင်သည်။ ဤ library သည် JSON structure, naming, header organization, parameter mapping နှင့် event separation ကို စနစ်တကျပြင်ထားသော်လည်း installed environment အားလုံးတွင် build အာမခံချက်မဟုတ်ပါ။ Compile error ဖြစ်ပါက ပထမဆုံး built-in RequestNetwork block တစ်ခု၏ generated source code နှင့် တိုက်စစ်ပြီး method name/import/package ကို version အလိုက် ပြန်ညှိပါ။

## References

[1]: https://supabase.com/docs/guides/api "Supabase API documentation"
[2]: https://supabase.com/docs/guides/database/api "Supabase Database REST API documentation"
[3]: https://supabase.com/docs/guides/auth "Supabase Auth documentation"
[4]: https://supabase.com/docs/guides/storage "Supabase Storage documentation"
[5]: https://firebase.google.com/docs/database/security "Firebase security rules reference used as a general client/server security reminder"
