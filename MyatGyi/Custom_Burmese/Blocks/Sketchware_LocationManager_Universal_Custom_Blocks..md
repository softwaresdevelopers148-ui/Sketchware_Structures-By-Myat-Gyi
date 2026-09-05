# Sketchware LocationManager Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware Location Manager component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.locationmanager`, `%m.location` နှင့် `%m.listmap` selector များကို အသုံးပြုထားသောကြောင့် LocationManager component ID, Activity ID, View ID နှင့် app-specific variable များကို fixed မလုပ်ထားပါ။

LocationManager သည် GPS, Network သို့မဟုတ် အခြား provider များမှ device location update များနှင့် last-known location ကို ရယူနိုင်သည်။ Location API ကို အသုံးပြုရန် `ACCESS_COARSE_LOCATION` သို့မဟုတ် `ACCESS_FINE_LOCATION` permission လိုအပ်ပြီး user ခွင့်ပြုထားသော accuracy အဆင့်အပေါ် မူတည်၍ ရရှိသောတန်ဖိုးကွာနိုင်သည်။ [1] [2]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **37 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Provider state, last-known location, update request, update stop |
| Provider | GPS နှင့် Network provider အတွက် သီးခြား request blocks |
| Values | Latitude, longitude, accuracy, altitude, bearing, speed, time |
| Safe | Provider enabled, non-null, accuracy threshold နှင့် lifecycle stop |
| Distance | Coordinate distance, coordinate format, lat/lon range validation |
| Professional | Freshness, provider name, interval/distance clamp နှင့် lifecycle handling |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `locationmanager_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ LocationManager palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် Location Manager component တစ်ခုရှိရမည်။ `%m.locationmanager` dropdown မှ မိမိအသုံးပြုလိုသော component ကို ရွေးပါ။ `onLocationChanged` event ထဲမှ ရရှိသော Location object ကို `%m.location` selector ပါသော value blocks များထံ ပေးပါ။

## Permission အရင်စီမံခြင်း

Location update မတောင်းမီ Project ၏ permission workflow ဖြင့် foreground location permission တောင်းပါ။ Android 12 နှင့်အထက်တွင် user သည် approximate သို့မဟုတ် precise location ကို ရွေးချယ်နိုင်သောကြောင့် app သည် approximate location ခွင့်ပြုထားသည့်အခြေအနေတွင်လည်း သင့်လျော်စွာ အလုပ်လုပ်နိုင်အောင် ဒီဇိုင်းဆွဲပါ။ [2]

```text
Before requesting updates
    check ACCESS_COARSE_LOCATION / ACCESS_FINE_LOCATION
    if permission is not granted
        request permission
    else
        check provider state
        request location updates
```

Permission မရသေးဘဲ update block ကို ခေါ်ပါက SecurityException သို့မဟုတ် runtime error ဖြစ်နိုင်သည်။ Permission denied ဖြစ်လျှင် app မ crash စေရန် UI message နှင့် Settings guidance ပြပါ။

## အခြေခံ Location Update Workflow

`LocationManager_Request_Updates` တွင် provider, interval milliseconds နှင့် minimum distance meters ကို သတ်မှတ်သည်။ Interval ကို 0 ထားခြင်းသည် provider update behavior အပေါ် မူတည်ပြီး မကြာခဏ update ရနိုင်သောကြောင့် battery သုံးစွဲမှုကို စဉ်းစားပြီး တန်ဖိုးထားပါ။

```text
LocationManager_Request_Updates
    locationmanager = location_manager
    provider = GPS_PROVIDER
    interval_ms = 5000
    distance_m = 10
```

GPS နှင့် Network အတွက် အသင့်သုံး blocks များဖြစ်သော `LocationManager_Request_GPS` နှင့် `LocationManager_Request_Network` ကို သုံးနိုင်သည်။ Provider တစ်ခု ပိတ်ထားနိုင်သောကြောင့် professional workflow တွင် provider enabled စစ်ပြီးမှ request လုပ်ပါ။

```text
LocationManager_Request_GPS_If_Enabled
    locationmanager = location_manager
    interval_ms = 5000
    distance_m = 10
```

## `onLocationChanged` Event Workflow

Location အသစ်ရရှိသောအခါ LocationManager component ၏ `onLocationChanged` event ကို သုံးပါ။ Event parameter အဖြစ်ရရှိသော Location object ကို `%m.location` ပါသော blocks များတွင် သုံးပြီး latitude, longitude နှင့် accuracy ကို ယူပါ။

```text
LocationManager → onLocationChanged
    if LocationManager_Is_Not_Null(location)
        latitude = LocationManager_Get_Latitude(location)
        longitude = LocationManager_Get_Longitude(location)
        accuracy = LocationManager_Get_Accuracy(location)
        coordinate_text = LocationManager_Format_Coordinate(latitude, longitude)
        update location UI
```

`onLocationChanged` သည် အကြိမ်ကြိမ်ခေါ်နိုင်သောကြောင့် UI update ကို လိုအပ်သလောက်သာ ပြုလုပ်ပါ။ Map marker သို့မဟုတ် network upload ကို update တိုင်း ပြုလုပ်ပါက debounce, minimum distance သို့မဟုတ် accuracy threshold သုံးပါ။

## Location တန်ဖိုးများ

| Block | ပြန်ပေးသည့်တန်ဖိုး |
|---|---|
| Get Latitude | မြောက်/တောင် အနေအထား degree, -90 မှ 90 |
| Get Longitude | အရှေ့/အနောက် အနေအထား degree, -180 မှ 180 |
| Get Accuracy | ခန့်မှန်းမှုအမှား radius meters |
| Get Altitude | ပင်လယ်ရေမျက်နှာပြင်မှ အမြင့် meters |
| Get Bearing | ရွေ့လျားနေသည့် ဦးတည်ချက် degree |
| Get Speed | meters/second |
| Get Time | Location timestamp milliseconds |
| Get Provider Name | GPS/Network စသည့် provider အမည် |

Accuracy ရှိ/မရှိကို `LocationManager_Has_Accuracy` ဖြင့် စစ်ပြီး `LocationManager_Is_Accurate_Enough` ဖြင့် သတ်မှတ်ထားသော threshold အတွင်း ရှိ/မရှိ စစ်နိုင်သည်။ Accuracy တန်ဖိုးနည်းလေလေ location ပိုတိကျလေလေ ဖြစ်သည်။

```text
onLocationChanged(location)
    if LocationManager_Is_Accurate_Enough(location, 50)
        update primary location
    else
        keep previous value or show low-accuracy status
```

## Last-Known Location

`LocationManager_Get_Last_Known` သည် provider တစ်ခု၏ နောက်ဆုံးသိမ်းထားသော location ကို ရယူသည်။ ထိုတန်ဖိုးသည် null ဖြစ်နိုင်ပြီး အသစ်မဟုတ်နိုင်သောကြောင့် `LocationManager_Is_Not_Null` နှင့် `LocationManager_Is_Fresh` ဖြင့် စစ်ပြီးမှ အသုံးပြုပါ။

```text
last_location = LocationManager_Get_Last_Known(location_manager, GPS_PROVIDER)
if LocationManager_Is_Not_Null(last_location)
    if LocationManager_Is_Fresh(last_location, 120)
        show last location
    else
        request fresh updates
else
    request fresh updates
```

Last-known location ကို current location အဖြစ် တိုက်ရိုက်မယူဆပါနှင့်။ Navigation, delivery နှင့် safety-critical feature များတွင် update အသစ်နှင့် accuracy ကို အမြဲစစ်ပါ။

## Distance နှင့် Coordinate Validation

`LocationManager_Distance_Between` သို့မဟုတ် `LocationManager_Distance_Meters` တွင် latitude/longitude နှစ်စုံကို ထည့်ပြီး coordinate နှစ်ခုကြား distance meters ကို တွက်နိုင်သည်။

```text
meters = LocationManager_Distance_Meters(
    current_latitude,
    current_longitude,
    destination_latitude,
    destination_longitude
)
```

Input coordinate များကို `LocationManager_Is_Valid_Latitude` နှင့် `LocationManager_Is_Valid_Longitude` ဖြင့် စစ်ပါ။ Latitude သည် -90 မှ 90၊ longitude သည် -180 မှ 180 အတွင်း ဖြစ်ရမည်။ Invalid coordinate များကို map, distance calculation သို့မဟုတ် API သို့ မပို့ပါနှင့်။

## Update Stop နှင့် Lifecycle

Location update မလိုတော့သောအခါ `LocationManager_Remove_Updates` ကို ခေါ်ပါ။ Screen ပိတ်ခြင်း၊ Activity `onPause`/`onStop` သို့မဟုတ် user က tracking ပိတ်သောအခါ update များကို ရပ်ခြင်းသည် battery သက်သာစေသည်။ [3]

```text
onPause / screen leaving
    LocationManager_Stop_On_Lifecycle(location_manager)

onResume / tracking enabled
    check permission
    check provider
    request updates again
```

Background tracking လိုအပ်ပါက foreground service, persistent notification နှင့် background location policy/permission များကို သင့် use case အတိုင်း စီမံရမည်။ မလိုအပ်ဘဲ background location မတောင်းပါနှင့်။ [2]

## Professional Interval နှင့် Distance

Update interval အလွန်တိုလျှင် battery နှင့် CPU သုံးစွဲမှုတိုးနိုင်သည်။ `LocationManager_Request_Balanced` သည် interval ကို seconds ဖြင့် လက်ခံပြီး `LocationManager_Clamp_Interval` နှင့် `LocationManager_Clamp_Distance` ဖြင့် အလွန်သေးသော input မဖြစ်စေရန် ကန့်သတ်နိုင်သည်။

| Use case | စတင်စမ်းသပ်ရန် အကြံပြုချက် |
|---|---|
| Map viewing | 2–5 seconds, 5–20 meters |
| Walking tracking | 3–10 seconds, 5–15 meters |
| Vehicle tracking | 1–5 seconds, 10–50 meters |
| Occasional location | Last-known first, then one fresh update |

ဤတန်ဖိုးများသည် universal requirement မဟုတ်ဘဲ စတင်စမ်းသပ်ရန်အတွက်သာ ဖြစ်သည်။ Device, provider, battery state, permission accuracy နှင့် app use case အလိုက် ပြန်ညှိပါ။

## Mock Location နှင့် Data Trust

`LocationManager_Is_Mock` သည် location ကို mock provider မှ ရရှိခြင်း ရှိ/မရှိ စစ်နိုင်သည်။ ဤစစ်ဆေးမှုသည် location ကို အမှန်တကယ် လူနေရာဟုတ်ကြောင်း အာမခံခြင်းမဟုတ်ပါ။ Location ကို authentication, business rules, server-side validation နှင့် permission context များနှင့် တွဲဖက်စစ်ဆေးပါ။

Location data သည် sensitive personal data ဖြစ်နိုင်သောကြောင့် အနည်းဆုံးလိုအပ်သည့် data ကိုသာ သိမ်းပါ၊ user ကို ရည်ရွယ်ချက်နှင့် retention ကို ရှင်းပြပါ၊ public log/Toast တွင် တိကျသော coordinate များ မပြပါနှင့်။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.locationmanager`, `%m.location`, `%m.listmap` selector များသုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, View ID နှင့် app-specific variable မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ အသုံးပြုထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$d`, `%3$d` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | Provider, interval, distance, Location နှင့် coordinate များကို input အဖြစ် လက်ခံထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Permission denied | App မ crash ဘဲ permission guidance ပြသည် |
| GPS disabled | Provider-disabled path ကို ကိုင်တွယ်သည် |
| onLocationChanged | Latitude/longitude/accuracy မှန်ကန်စွာ update သည် |
| Null last-known location | Fresh update request သို့ fallback လုပ်သည် |
| Low accuracy | Previous value ထားခြင်း သို့မဟုတ် warning ပြသည် |
| Invalid coordinates | Map/API သို့ မပို့ပါ |
| Lifecycle pause | Updates ရပ်ပြီး battery သက်သာသည် |
| Resume tracking | Permission/provider စစ်ပြီး updates ပြန်စသည် |
| Frequent updates | Battery/CPU usage ကို စောင့်ကြည့်သည် |
| Mock location | App policy အတိုင်း handle လုပ်သည် |
| Background tracking | Foreground service/permission policy နှင့်ကိုက်ညီသည် |

## Compatibility Note

Sketchware Pro version အလိုက် Location Manager block label, event parameter name, generated listener variable name နှင့် provider constant အသုံးပြုပုံ ကွာနိုင်ပါသည်။ ဤ library သည် `isProviderEnabled`, `getLastKnownLocation`, `requestLocationUpdates`, `removeUpdates`, `getLatitude`, `getLongitude`, `getAccuracy`, `getAltitude`, `getBearing`, `getSpeed`, `getTime`, `getProvider`, `hasAccuracy` နှင့် `isMock` API များကို အခြေခံထားသည်။ Import ပြီး compile error ဖြစ်ပါက သင့် version ၏ built-in Location Manager blocks နှင့် generated source code ကို တိုက်စစ်ပြီး method/listener name ကို version-compatible ပြင်ဆင်ပါ။

`onLocationChanged` event ကို Custom Block JSON က အလိုအလျောက် ဖန်တီးပေးခြင်းမဟုတ်ပါ။ Location update စတင်/ရပ်ရန် block များကို ဤ library မှ သုံးပြီး location value processing နှင့် UI logic ကို Location Manager component ၏ `onLocationChanged` event အတွင်းတွင် သီးခြားရေးရမည်။

## References

[1] Android Developers, “LocationManager API reference,” https://developer.android.com/reference/android/location/LocationManager  
[2] Android Developers, “Request location permissions,” https://developer.android.com/develop/sensors-and-location/location/permissions  
[3] Android Developers, “Request location updates,” https://developer.android.com/develop/sensors-and-location/location/request-updates
