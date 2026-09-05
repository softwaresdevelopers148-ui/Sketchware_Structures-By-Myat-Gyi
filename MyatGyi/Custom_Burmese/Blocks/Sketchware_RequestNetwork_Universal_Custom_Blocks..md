# Sketchware RequestNetwork Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware RequestNetwork component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.requestnetwork` နှင့် `%m.map` selector များကို အသုံးပြုထားသောကြောင့် RequestNetwork component ID, View ID, Activity ID သို့မဟုတ် app-specific variable များကို fixed မလုပ်ထားပါ။

RequestNetwork ၏ အဓိကအလုပ်စဉ်မှာ request data ပြင်ဆင်ခြင်း၊ request စတင်ခြင်း၊ `onResponse` event တွင် response ကို ကိုင်တွယ်ခြင်းနှင့် `onErrorResponse` event တွင် error ကို ကိုင်တွယ်ခြင်း ဖြစ်သည်။ ဤ Custom Blocks များသည် request setup/start နှင့် response/error utility များကို ပေးထားပြီး event အတွင်းရှိ app-specific logic ကို အသုံးပြုသူ၏ Project ထဲတွင် သီးခြားထည့်ရမည်။

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **36 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Header/Parameter Map ပြင်ဆင်ခြင်း၊ Bearer/JSON headers |
| Request | GET, POST, PUT, DELETE နှင့် header/parameter ပါသော request များ |
| Response | Status code class, empty response နှင့် tag စစ်ခြင်း |
| Safe | Empty URL စစ်ခြင်း၊ valid response/error handling |
| Professional | Retry, retryable error, status text, input sanitization |
| Event Guide | `onResponse` နှင့် `onErrorResponse` workflow |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `requestnetwork_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ RequestNetwork palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် RequestNetwork component တစ်ခုရှိရမည်။ Header နှင့် Parameter များအတွက် Map variable များ ဖန်တီးပြီး block များ၏ `%m.map` dropdown မှ သက်ဆိုင်ရာ Map ကို ရွေးပါ။

## အခြေခံ GET Request

```text
When btn_load clicked
    RequestNetwork_Start_Get
        requestnetwork = request_api
        url = "https://example.com/api/items"
        tag = "items"
```

Request ပြီးဆုံးသောအခါ `onResponse` event ထဲတွင် response body နှင့် response code ကို ကိုင်တွယ်ပါ။ GET request အတွက် request parameter မလိုပါက Map ထည့်စရာမလိုပါ။

## POST Request နှင့် Parameters

```text
RequestNetwork_Set_Param
    map = body_params
    key = "name"
    value = user_name

RequestNetwork_Set_Param
    map = body_params
    key = "email"
    value = user_email

RequestNetwork_Start_Post
    requestnetwork = request_api
    url = "https://example.com/api/users"
    tag = "create_user"
```

POST body data ကို Project ၏ RequestNetwork built-in parameter workflow နှင့် သင့် version ၏ `setParams` behavior အတိုင်း ချိတ်ပါ။ JSON API သုံးပါက `RequestNetwork_Set_Json_Content_Type` နှင့် သက်ဆိုင်သော header Map ကို request မစမီ သတ်မှတ်ပါ။

## Header နှင့် Authentication

Header Map ထဲတွင် Content-Type, Accept, Authorization နှင့် User-Agent စသည်တို့ကို ထည့်နိုင်သည်။

```text
RequestNetwork_Set_Json_Content_Type
    map = headers

RequestNetwork_Set_Header
    map = headers
    key = "Accept"
    value = "application/json"

RequestNetwork_Set_Bearer_Header
    map = headers
    token = access_token

RequestNetwork_Start_Get_With_Headers
    requestnetwork = request_api
    headers = headers
    url = api_url
    tag = "protected_data"
```

API key, access token, password သို့မဟုတ် private secret များကို Custom Block JSON, screenshot, public project သို့မဟုတ် hard-coded source code ထဲ မထည့်ရ။ Runtime configuration သို့မဟုတ် လုံခြုံသော backend ကို သုံးပြီး token ကို ထိန်းသိမ်းပါ။

## `onResponse` Event Workflow

Request အောင်မြင်စွာ response ပြန်လာသောအခါ RequestNetwork component ၏ `onResponse` event သို့ ဝင်ပါ။ Event ထဲတွင် ပုံမှန်အားဖြင့် response tag, response code နှင့် response body တို့ကို အသုံးပြုနိုင်သည်။ သင့် Sketchware version တွင် parameter အမည်ကွာနိုင်သောကြောင့် event ထဲရှိ အမှန်တကယ် variable name ကို အခြေခံပါ။

```text
RequestNetwork → onResponse
    if RequestNetwork_Is_Tag_Match(response_tag, "items")
        if RequestNetwork_Is_Success_Code(response_code)
            if RequestNetwork_Is_Response_Not_Empty(response_body)
                parse response_body as JSON
                convert JSON to Map/List Map
                update ListView or UI
            else
                show empty-result message
        else
            show RequestNetwork_Status_Text(response_code)
```

HTTP status code အုပ်စုများကို 2xx success, 3xx redirection, 4xx client error နှင့် 5xx server error အဖြစ် ခွဲနိုင်သည်။ Status code တစ်ခုတည်းကို အောင်မြင်သည်ဟု မယူဆဘဲ response body နှင့် API-specific error field များကိုပါ စစ်ပါ။

## `onErrorResponse` Event Workflow

Network မရခြင်း၊ timeout၊ DNS failure၊ invalid URL သို့မဟုတ် request layer error ဖြစ်သောအခါ `onErrorResponse` event ကို သုံးပါ။ Error code နှင့် error message ကို log/လူနားလည်လွယ်သော message အဖြစ် ပြောင်းပြီး UI တွင် ပြပါ။

```text
RequestNetwork → onErrorResponse
    hide loading state
    error_text = RequestNetwork_Get_Error_Summary(error_code, error_message)
    show error_text to user
    if RequestNetwork_Is_Retryable_Error(error_code)
        show Retry button
    else
        show manual-help message
```

`onErrorResponse` သည် server က 4xx/5xx response ပြန်ခြင်းနှင့် မတူနိုင်ပါ။ Server response code ပါလာသော်လည်း HTTP response ရရှိခဲ့ပါက `onResponse` ထဲတွင် status code စစ်ရနိုင်ပြီး transport/network failure ဖြစ်ပါက `onErrorResponse` သို့ ရောက်နိုင်သည်။ သင့် component version ၏ event behavior ကို test လုပ်ပါ။

## Loading နှင့် UI State

Request စတင်မီ loading indicator ပြပြီး response/error event နှစ်ခုစလုံးတွင် loading state ကို ပိတ်ပါ။ မည်သည့် path ဖြင့်မဆို loading state ပြန်ပိတ်နိုင်ရန် duplicate-safe flow သုံးပါ။

```text
Before request
    show progress indicator
    disable submit button

onResponse
    hide progress indicator
    enable submit button
    process response

onErrorResponse
    hide progress indicator
    enable submit button
    show error message
```

Request မပြီးမီ user က ခလုတ်ကို ထပ်နှိပ်နိုင်လျှင် duplicate request ဖြစ်နိုင်သောကြောင့် button disable, request tag စစ်ခြင်း သို့မဟုတ် in-flight Boolean variable သုံးပါ။

## Retry Workflow

`RequestNetwork_Is_Retryable_Error` သည် timeout 408, rate limit 429 နှင့် server-side 5xx error များကို retry ပြုလုပ်နိုင်သောအုပ်စုအဖြစ် ခွဲပေးသည်။ 400, 401, 403, 404 စသည့် client/authentication error များကို တိုက်ရိုက် retry မလုပ်ဘဲ URL, credentials, permission နှင့် request data ကို ပြင်ဆင်ပါ။

```text
onErrorResponse
    if RequestNetwork_Is_Retryable_Error(error_code)
        wait using Timer/backoff
        RequestNetwork_Retry_Get(request_api, api_url, request_tag)
    else
        show permanent error guidance
```

Retry ကို အဆုံးမရှိ ထပ်မလုပ်ပါနှင့်။ Retry count variable သတ်မှတ်ပြီး exponential backoff သို့မဟုတ် minimum delay သုံးပါ။ POST/PUT request များတွင် server က request ကို လက်ခံပြီး response မရနိုင်သောအခြေအနေရှိသောကြောင့် idempotency ကို စဉ်းစားပြီးမှ retry လုပ်ပါ။

## JSON နှင့် Response Parsing

Response body ကို အရင် empty/null စစ်ပြီးမှ JSON parser သို့ ပို့ပါ။ JSON key မရှိခြင်း၊ data type မကိုက်ခြင်းနှင့် server error object ပြန်လာခြင်းတို့အတွက် fallback path ထည့်ပါ။

```text
onResponse
    if success code
        if body is not empty
            parse JSON
            if required key exists
                read value
                update UI
            else
                show invalid-data message
        else
            show no-content message
```

Response body ထဲမှ user-controlled HTML, script သို့မဟုတ် untrusted text ကို WebView တွင် တိုက်ရိုက်မပြပါနှင့်။ Output rendering ကို escape/sanitize လုပ်ပြီး API data ကို လိုအပ်သလောက်သာ UI ထဲသို့ ထည့်ပါ။

## Request Data Map စီမံခြင်း

Request အသစ်မစမီ ယခင် request ၏ parameter/header Map ကို အလိုအလျောက် ပြန်အသုံးမပြုမိစေရန် `RequestNetwork_Clear_Map` သို့မဟုတ် `RequestNetwork_Clear_Request_Data` ကို သုံးပါ။ Public header နှင့် private/auth header Map ကို သီးခြားထားခြင်းက accidental credential leakage ကို လျှော့ချပေးသည်။

```text
RequestNetwork_Clear_Map(headers)
RequestNetwork_Set_Json_Content_Type(headers)
RequestNetwork_Set_Bearer_Header(headers, access_token)
RequestNetwork_Start_Get_With_Headers(...)
```

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.requestnetwork` နှင့် `%m.map` selector သုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed View ID, Activity ID နှင့် app-specific component ID မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$s`, `%3$d` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | URL, tag, Map, token နှင့် component များကို input အဖြစ် လက်ခံထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| GET 200 response | `onResponse` တွင် success path run သည် |
| 204 empty response | Empty-result path ကို ကိုင်တွယ်သည် |
| 400/401/403/404 | Client/auth error ကို retry မလုပ်ဘဲ ပြသသည် |
| 408/429/5xx | Retry policy အတိုင်း လုပ်ဆောင်သည် |
| Network offline | `onErrorResponse` တွင် friendly message ပြသည် |
| Invalid JSON | App crash မဖြစ်ဘဲ parse-error path သို့သွားသည် |
| Duplicate click | Duplicate request ကို ကာကွယ်သည် |
| Secret header | Token ကို UI/log/public source ထဲ မပေါ်စေပါ |
| Multiple tags | မှန်ကန်သော tag သာ process လုပ်သည် |
| Loading state | Response နှင့် error နှစ်ခုစလုံးတွင် loading ပိတ်သည် |

## Compatibility Note

Sketchware Pro version အလိုက် RequestNetwork component ၏ generated method name၊ request listener variable name နှင့် event parameter names ကွာနိုင်ပါသည်။ ဤ library သည် `setHeaders`, `setParams`, `startRequestNetwork`, `RequestNetworkController.GET`, `POST`, `PUT`, `DELETE` နှင့် `_requestNetwork_request_listener` generated listener convention ကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in RequestNetwork block နှင့် generated source code ကို တိုက်စစ်ပြီး listener/method name ကို version-compatible ပြင်ဆင်ပါ။

`onResponse` နှင့် `onErrorResponse` event များကို Custom Block JSON က အလိုအလျောက်ဖန်တီးပေးခြင်းမဟုတ်ပါ။ Request စတင်ရန် block များကို ဤ library မှ သုံးပြီး response/error logic ကို RequestNetwork component ၏ သက်ဆိုင်ရာ event အတွင်းတွင် သီးခြားရေးရမည်။

## References

[1] Sketchware Pro, “Components,” https://docs.sketchware.pro/docs/course/basics/component/  
[2] MDN Web Docs, “HTTP response status codes,” https://developer.mozilla.org/en-US/docs/Web/HTTP/Reference/Status  
[3] Sketchware Pro, “RequestNetwork component search guidance,” https://docs.sketchub.in/for-sketchware
