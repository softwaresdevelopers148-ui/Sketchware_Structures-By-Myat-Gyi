# Sketchware BluetoothConnect Universal Custom Blocks

ဤ library သည် user ပေးထားသော Master Rules ကို အခြေခံပြီး Sketchware BluetoothConnect component အတွက် ဖန်တီးထားသော Universal Custom Block set ဖြစ်သည်။ `%m.bluetoothconnect` နှင့် `%m.listmap` selector များကို အသုံးပြုထားသောကြောင့် BluetoothConnect component ID, Activity ID, View ID နှင့် app-specific variable များကို fixed မလုပ်ထားပါ။

ဤ component သည် paired Bluetooth device များကို ရယူခြင်း၊ Bluetooth ဖွင့်/ပိတ်အခြေအနေစစ်ခြင်း၊ device address/UUID ဖြင့် connection စတင်ခြင်း၊ text data ပို့ခြင်းနှင့် connection lifecycle event များကို ကိုင်တွယ်ခြင်းအတွက် အသုံးပြုသည်။ Android Bluetooth data transfer သည် connected socket ၏ input/output stream များကို အသုံးပြုပြီး blocking read/write operation များကို UI thread ပေါ်တွင် မလုပ်သင့်ပါ။ [1]

## ပါဝင်သည့် Block အဆင့်များ

JSON ဖိုင်တွင် Header များအပါအဝင် **43 entries** ပါဝင်သည်။

| အဆင့် | ပါဝင်သောလုပ်ဆောင်ချက်များ |
|---|---|
| Basic | Bluetooth စစ်ခြင်း၊ Activate၊ UUID ရယူခြင်း၊ Paired Device စာရင်း |
| Connection | Default UUID/custom UUID ဖြင့် prepare, connect, stop |
| Data | Text, newline, command, JSON line နှင့် heartbeat ပို့ခြင်း |
| Conversion | Received bytes ကို UTF-8 text ပြောင်းခြင်း၊ trim, split, byte count |
| Safe | Bluetooth/Address/Text စစ်ပြီး connect/send/stop |
| Professional | Address/UUID validation, error summary, message terminator, lifecycle |
| Events | `onConnected`, `onDataReceived`, `onDataSent`, `onConnectionErr`, `onConnectionStopped` workflow |

## Import လုပ်ခြင်း

Sketchware Pro တွင် Project menu → Developer Tools → Block Manager သို့ဝင်ပါ။ Custom Block JSON Import ကိုရွေးပြီး `bluetoothconnect_custom_blocks.json` ဖိုင်ကို ရွေးပါ။ Import ပြီးနောက် Logic Editor ထဲရှိ BluetoothConnect palette တွင် block များကို အသုံးပြုနိုင်ပါမည်။

Project ထဲတွင် BluetoothConnect component တစ်ခု ထည့်ထားပြီး `%m.bluetoothconnect` dropdown မှ မိမိအသုံးပြုလိုသော component ကို ရွေးပါ။ Paired device များအတွက် `%m.listmap` selector ကို အသုံးပြုနိုင်ရန် List Map variable တစ်ခု ကြိုတင်ဖန်တီးထားပါ။

## အခြေခံ Setup Workflow

```text
App start / screen open
    if Bluetooth is not available
        show unsupported message
    else if Bluetooth is not activated
        request Bluetooth activation
    else
        clear device list
        get paired devices into List Map
```

`BluetoothConnect_Is_Available` သည် Bluetooth adapter ရှိ/မရှိကို စစ်ပြီး `BluetoothConnect_Is_Activated` သည် Bluetooth ဖွင့်ထား/မထားကို စစ်သည်။ Bluetooth မဖွင့်ထားပါက `BluetoothConnect_Activate` ကို user consent ဖြင့် သုံးပါ။

```text
BluetoothConnect_Get_Paired_Devices
    bluetoothconnect = bt_connection
    listmap = paired_devices
```

Paired device List Map ထဲတွင် ပုံမှန်အားဖြင့် device `name` နှင့် `address` တို့ကို အသုံးပြုနိုင်သည်။ User ရွေးချယ်ထားသော device ၏ address ကိုယူပြီး connection block သို့ ပို့ပါ။

## Connection Workflow

Default SPP UUID သုံးမည့် project များတွင် အောက်ပါအတိုင်း လုပ်ဆောင်နိုင်သည်။

```text
BluetoothConnect_Prepare_Default_Connection
    bluetoothconnect = bt_connection
    tag = "server"

BluetoothConnect_Start_Default
    bluetoothconnect = bt_connection
    address = selected_device_address
    tag = "server"
```

Custom UUID သုံးမည့် device များတွင် `BluetoothConnect_Prepare_UUID_Connection` နှင့် `BluetoothConnect_Start_UUID` ကို သုံးပါ။ UUID နှင့် Bluetooth address ကို user input အဖြစ် တိုက်ရိုက်ယုံကြည်မထားဘဲ `BluetoothConnect_Is_UUID_Format` နှင့် `BluetoothConnect_Is_Address_Format` ဖြင့် အခြေခံ format စစ်ပါ။

> Source implementation တွင် `readyConnection(listener, tag)`, `readyConnection(listener, uuid, tag)`, `startConnection(listener, address, tag)`, `startConnection(listener, uuid, address, tag)` နှင့် `stopConnection(listener, tag)` method များကို အသုံးပြုထားသည်။ Custom block code တွင် generated listener variable ကို component convention အတိုင်း ချိတ်ထားသည်။ Sketchware Pro version အလိုက် listener variable name ကွာနိုင်သောကြောင့် compile error ဖြစ်ပါက built-in BluetoothConnect connect block ၏ generated source code နှင့် တိုက်စစ်ပါ။

## `onConnected` Event

Connection အောင်မြင်သောအခါ `onConnected` event ကို သုံးပါ။ Event ထဲတွင် tag နှင့် remote device data Map ရနိုင်သည်။ UI တွင် connected status ပြခြင်း၊ Connect button ကို disable လုပ်ခြင်း၊ Send controls ကို enable လုပ်ခြင်းနှင့် device name/address သိမ်းခြင်းတို့ကို ဒီ event အတွင်း ပြုလုပ်ပါ။

```text
BluetoothConnect → onConnected
    set status_text to "ချိတ်ဆက်ပြီးပါပြီ"
    enable send controls
    disable connect button
    save device name/address from device data
```

Connection မအောင်မြင်သေးမီ send block မခေါ်ပါနှင့်။ Connect button ကို အကြိမ်ကြိမ်နှိပ်ခြင်းကြောင့် duplicate connection မဖြစ်စေရန် connection state variable တစ်ခု ထားပါ။

## `onDataReceived` Event

Remote device မှ bytes ရောက်လာသောအခါ `onDataReceived` event ကို သုံးပါ။ Event parameter များတွင် tag, byte array နှင့် bytes အရေအတွက် ပါနိုင်သည်။ Received bytes ကို `BluetoothConnect_Bytes_To_Text` ဖြင့် UTF-8 text ပြောင်းပြီး message protocol အတိုင်း parse လုပ်ပါ။

```text
BluetoothConnect → onDataReceived
    received_text = BluetoothConnect_Bytes_To_Text(received_data)
    received_text = BluetoothConnect_Text_Trim(received_text)
    if BluetoothConnect_Text_Is_Not_Empty(received_text)
        if BluetoothConnect_Received_Has_Terminator(received_text, "\\n")
            process complete message
        else
            append to receive buffer
```

Bluetooth stream သည် message boundary ကို အလိုအလျောက် မထိန်းပေးနိုင်သောကြောင့် message သည် တစ်ကြိမ်တည်းမရောက်ဘဲ အပိုင်းပိုင်းရောက်နိုင်သည်။ Newline, `END`, fixed length သို့မဟုတ် JSON framing စသည့် protocol တစ်ခု သတ်မှတ်ပြီး buffer ဖြင့် ပြည့်စုံသော message ကိုသာ parse လုပ်ပါ။ [1]

## `onDataSent` Event

Data ပို့ပြီးသောအခါ `onDataSent` event ကို သုံးပါ။ Send status ပြောင်းခြင်း၊ pending message ရှင်းခြင်း၊ sent counter တိုးခြင်း သို့မဟုတ် audit/debug log ထည့်ခြင်းတို့ကို လုပ်နိုင်သည်။

```text
BluetoothConnect → onDataSent
    set send_status to "ပို့ပြီးပါပြီ"
    clear send_text input
    increment sent_count
```

Sensitive data ကို log သို့မဟုတ် UI တွင် အပြည့်အစုံ မပြပါနှင့်။ လိုအပ်ပါက message type, length သို့မဟုတ် masked identifier သာ မှတ်တမ်းတင်ပါ။

## `onConnectionErr` Event

User ဖော်ပြထားသော `onConnectionErr` event သည် source implementation အချို့တွင် `onConnectionError` ဟု အမည်ရနိုင်သည်။ Event parameter များတွင် tag, connection state နှင့် error message ပါနိုင်သည်။

```text
BluetoothConnect → onConnectionErr / onConnectionError
    error_text = BluetoothConnect_Connection_Error_Text(state, message)
    set status_text to error_text
    disable send controls
    if connection error can recover
        show Retry button
```

Error ဖြစ်တိုင်း ချက်ချင်း retry မလုပ်ပါနှင့်။ Address, Bluetooth permission, pairing, UUID, remote device availability နှင့် connection state ကို စစ်ပြီးမှ retry လုပ်ပါ။ Retry count နှင့် delay ထားခြင်းက loop နှင့် battery drain ကို ကာကွယ်ပေးသည်။

## `onConnectionStopped` Event

Connection ကို user action, lifecycle event သို့မဟုတ် remote disconnect ကြောင့် ရပ်သောအခါ `onConnectionStopped` event ကို သုံးပါ။ UI state ကို disconnected သို့ ပြောင်းပြီး send controls ကို ပိတ်ပါ။

```text
BluetoothConnect → onConnectionStopped
    set is_connected to false
    disable send controls
    enable connect button
    set status_text to "ချိတ်ဆက်မှု ရပ်သွားပါပြီ"
```

Screen ပိတ်ခြင်း သို့မဟုတ် Bluetooth session မလိုတော့ခြင်းအချိန်တွင် `BluetoothConnect_Stop_Safely` သို့မဟုတ် `BluetoothConnect_Stop` ကို သုံးပါ။ Android guidance အရ Bluetooth connection အသုံးမလိုတော့သောအခါ socket/connection ကို ပိတ်သင့်သည်။ [1]

## Data ပို့ခြင်း

Connection အောင်မြင်ပြီးနောက် `BluetoothConnect_Send_Text` ကို သုံးနိုင်သည်။ Message boundary လိုအပ်ပါက `BluetoothConnect_Send_Text_Newline` သို့မဟုတ် JSON line block ကို သုံးပါ။

```text
BluetoothConnect_Send_Text_Newline
    bluetoothconnect = bt_connection
    text = "LED_ON"
    tag = "command"
```

Command/value protocol သုံးပါက `BluetoothConnect_Send_Command` ကို သုံးနိုင်သည်။

```text
BluetoothConnect_Send_Command
    bluetoothconnect = bt_connection
    command = "SET_SPEED"
    value = speed_value
    tag = "command"
```

JSON line protocol သုံးပါက JSON string ကို အရင်တည်ဆောက်ပြီး newline ဖြင့် ပို့ပါ။ JSON string ထဲတွင် device က လက်ခံနိုင်သော key/value schema နှင့် escaping rule ကို တစ်ဖက်နှင့်တစ်ဖက် တူညီအောင် သတ်မှတ်ပါ။

## Safe နှင့် Professional Blocks

`BluetoothConnect_Start_Default_Safely` နှင့် `BluetoothConnect_Start_UUID_Safely` သည် Bluetooth adapter အသုံးပြုနိုင်ခြင်း၊ address/UUID လွတ်မနေခြင်းတို့ကို အရင်စစ်ပြီးမှ connection စတင်သည်။ `BluetoothConnect_Send_Command_Safely` သည် empty command မပို့စေရန် ကာကွယ်သည်။

`BluetoothConnect_Is_Address_Format` သည် ပုံမှန် MAC address ပုံစံကို စစ်ပြီး `BluetoothConnect_Is_UUID_Format` သည် UUID ပုံစံကို စစ်သည်။ ဤစစ်ဆေးမှုများသည် format validation သာဖြစ်ပြီး device အမှန်တကယ် paired/available ဖြစ်ကြောင်း မအာမခံပါ။

`BluetoothConnect_Received_Has_Terminator` ကို message framing အတွက် သုံးပါ။ `BluetoothConnect_Text_Split_Lines` သည် newline-based protocol များအတွက် အထောက်အကူပြုသော်လည်း partial message များကို မစဉ်းစားဘဲ တိုက်ရိုက် split မလုပ်သင့်ပါ။

## Android Permission နှင့် Security

Android version နှင့် target SDK အလိုက် Bluetooth scan/connect permission များ လိုအပ်နိုင်သည်။ Android 12 နှင့်အထက်တွင် `BLUETOOTH_SCAN` နှင့် `BLUETOOTH_CONNECT` runtime permission များကို သင့် Sketchware project ၏ permission workflow အတွင်း ထည့်သွင်းစစ်ဆေးပါ။ သင့် component version က permission request ကို ကိုယ်တိုင်စီမံပါက ထို built-in flow ကို ဦးစားပေးပါ။

Bluetooth address, UUID နှင့် received data များကို မလိုအပ်ဘဲ public log, Toast သို့မဟုတ် cloud service သို့ မပို့ပါနှင့်။ Trusted device မဟုတ်သော connection မှ command များကို တိုက်ရိုက် execute မလုပ်ဘဲ authentication token, message checksum သို့မဟုတ် command allow-list ထည့်သွင်းစဉ်းစားပါ။

## Master Rules Compliance

| Master Rule | ဤ library တွင် လိုက်နာထားပုံ |
|---|---|
| Universal ဖြစ်ရမည် | `%m.bluetoothconnect` နှင့် `%m.listmap` selector သုံးထားသည် |
| Fixed View/Activity မဖြစ်ရ | Fixed Activity ID, View ID နှင့် app-specific device ID မသုံးထားပါ |
| Add source directly မသုံးရ | JSON custom block `code` နှင့် `imports` field ကိုသာ သုံးထားသည် |
| Palette rule | Block အားလုံးတွင် `palette: ""` ထားထားသည် |
| Header rule | Section Header များတွင် `type: "h"` နှင့် black color သုံးထားသည် |
| Burmese spec | Block spec များကို မြန်မာလို သဘာဝကျစွာ ရေးထားသည် |
| Parameter order | `%1$s`, `%2$s`, `%3$s` စသည့် position symbols များကို အစဉ်လိုက်သုံးထားသည် |
| Reusable | Address, UUID, tag, text, Map နှင့် List Map များကို input အဖြစ် လက်ခံထားသည် |

## စမ်းသပ်ရန် Checklist

| စမ်းသပ်ချက် | မျှော်မှန်းရလဒ် |
|---|---|
| Bluetooth မရှိခြင်း | Unsupported state ကို ပြသည် |
| Bluetooth ပိတ်ထားခြင်း | Activation request ပြသည် |
| Paired device list | Name/address များ List Map ထဲရောက်သည် |
| Valid connection | `onConnected` event run သည် |
| Invalid address/UUID | Safe validation ဖြင့် မချိတ်ဆက်ပါ |
| Send after connected | `onDataSent` event run သည် |
| Incoming partial bytes | Buffer/framing အတိုင်း ပြည့်စုံသော message ပြန်တည်ဆောက်သည် |
| Connection error | `onConnectionErr` တွင် friendly error ပြသည် |
| Stop/disconnect | `onConnectionStopped` တွင် UI state ပြန်ပြင်သည် |
| Screen exit | Connection ရပ်ပြီး resource မကျန်ပါ |
| Permission denied | App မ crash ဘဲ permission guidance ပြသည် |
| Unauthorized command | Allow-list/authentication ဖြင့် ကာကွယ်သည် |

## Compatibility Note

Sketchware Pro version နှင့် BluetoothConnect implementation အလိုက် component selector name၊ generated listener variable name နှင့် error event label ကွာနိုင်ပါသည်။ ဤ library သည် `isBluetoothEnabled`, `isBluetoothActivated`, `activateBluetooth`, `getRandomUUID`, `getPairedDevices`, `readyConnection`, `startConnection`, `stopConnection` နှင့် `sendData` method များကို အခြေခံထားသည်။ Source implementation အချို့တွင် event callback ကို `onConnectionError` ဟု ခေါ်ပြီး Sketchware UI တွင် `onConnectionErr` ဟု ပြနိုင်သည်။ Import ပြီး compile error ဖြစ်ပါက built-in component block တစ်ခု၏ generated source code နှင့် listener variable ကို တိုက်စစ်ပါ။

## References

[1] Android Developers, “Transfer Bluetooth data,” https://developer.android.com/develop/connectivity/bluetooth/transfer-data  
[2] Android Developers, “Bluetooth permissions,” https://developer.android.com/develop/connectivity/bluetooth/permissions  
[3] Sketchware Pro, “Components,” https://docs.sketchware.pro/docs/course/basics/component/
