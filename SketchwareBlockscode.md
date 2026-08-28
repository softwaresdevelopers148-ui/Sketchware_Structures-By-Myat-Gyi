
1.Selectable Text
textview1.setTextIsSelectable(true);

2.Scrolling Text
textview1.setEllipsize(TextUtils.TruncateAt.MARQUEE);
textview1.setMarqueeRepeatLimit(-1);
textview1.setSingleLine(true);
textview1.setSelected(true);

3.Unity Ads - Import
com.unity3d.ads.UnityAds
com.unity3d.ads.IUnityAdsListener

4.Unity Ads Mainifest
<activity
        android:name="com.unity3d.services.ads.adunit.AdUnitActivity"
        android:configChanges="fontScale|keyboard|keyboardHidden|locale|mnc|mcc|navigation|orientation|screenLayout|screenSize|smallestScreenSize|uiMode|touchscreen"
        android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
        android:hardwareAccelerated="true" />
    <activity
        android:name="com.unity3d.services.ads.adunit.AdUnitTransparentActivity"
        android:configChanges="fontScale|keyboard|keyboardHidden|locale|mnc|mcc|navigation|orientation|screenLayout|screenSize|smallestScreenSize|uiMode|touchscreen"
        android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen"
        android:hardwareAccelerated="true" />
    <activity
        android:name="com.unity3d.services.ads.adunit.AdUnitTransparentSoftwareActivity"
        android:configChanges="fontScale|keyboard|keyboardHidden|locale|mnc|mcc|navigation|orientation|screenLayout|screenSize|smallestScreenSize|uiMode|touchscreen"
        android:theme="@android:style/Theme.Translucent.NoTitleBar.Fullscreen"
        android:hardwareAccelerated="false" />
    <activity
        android:name="com.unity3d.services.ads.adunit.AdUnitSoftwareActivity"
        android:configChanges="fontScale|keyboard|keyboardHidden|locale|mnc|mcc|navigation|orientation|screenLayout|screenSize|smallestScreenSize|uiMode|touchscreen"
        android:theme="@android:style/Theme.NoTitleBar.Fullscreen"
        android:hardwareAccelerated="false" />

5.Unity Ads OnCreate
1)

// Declare a new listener:
        final UnityAdsListener myAdsListener = new UnityAdsListener ();
        // Add the listener to the SDK:
        UnityAds.addListener(myAdsListener);
        // Initialize the SDK:
        UnityAds.initialize (this, unityGameID, testMode);


2)//start

}





// Implement a function to display an ad if the Placement is ready:
    public void DisplayInterstitialAd () {
        if (UnityAds.isReady (placementId)) {
            UnityAds.show (this, placementId);
        }
    }




    // Implement the IUnityAdsListener interface methods:
    private class UnityAdsListener implements IUnityAdsListener {

        @Override
        public void onUnityAdsReady (String placementId) {
            // Implement functionality for an ad being ready to show.

3)//start

}

        @Override
        public void onUnityAdsStart (String placementId) {
            // Implement functionality for a user starting to watch an ad.

4)//start

}

        @Override
        public void onUnityAdsFinish (String placementId, UnityAds.FinishState finishState) {
            // Implement functionality for a user finishing an ad.

5)//start


        }

        @Override
        public void onUnityAdsError (UnityAds.UnityAdsError error, String message) {
            // Implement functionality for a Unity Ads service error occurring.
        
msg = message;

6)//start

}

//ended on Create code



*BUTTON : DisplayInterstitialAd ();
   



NOTE:ONLY FOR UNITY SDK 4.0 VERSION

Unity Ads Banner OnCreate
com.unity3d.services.banners.IUnityBannerListener

com.unity3d.services.banners.UnityBanners

android.view.ViewGroup

6.Web View Touch To Copy
webview1.setOnTouchListener(new View.OnTouchListener() {
Boolean tF;
@Override public boolean onTouch(View v, MotionEvent event) {
showMessage("Aan");
tF = true;
switch (event.getAction()) {
case MotionEvent.ACTION_DOWN: tF = false;
showMessage("0");
}
return tF;
}
});

7.Web View Title
textview1.setText(webview1.getTitle());

8.Web View Progress Bar
final android.widget.ProgressBar prog = new android.widget.ProgressBar(this,null, android.R.attr.progressBarStyleHorizontal);
prog.setPadding(0,0,0,0);
prog.setIndeterminate(false);
prog.setFitsSystemWindows(true);
prog.setProgress(0);
prog.setScrollBarStyle(android.widget.ProgressBar.SCROLLBARS_OUTSIDE_INSET);
prog.setMax(100);
ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
prog.setLayoutParams(vlp);
pl.addView(prog);
webview1.setWebChromeClient(new WebChromeClient() {
@Override public void onProgressChanged(WebView view, int newProgress) {
prog.setProgress(newProgress);
}
});

9.Web View Zoom
webview1.getSettings().setBuiltInZoomControls(true);webview1.getSettings().setDisplayZoomControls(false);

10.Web View Favicon(icon)
imageview1.setImageBitmap(webview1.getFavicon());

11.Volume Control
//add seekbar set progress to 12

audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE); seekbar1.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));

}

AudioManager audioManager;

private void nothing() {

//OnSeekBar Changed add:

textview1.setText("Music Volume : " + _progressValue);

audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, _progressValue, 0);

12.Text View Multicolors
textview1.setTextColor(Color.rgb(73,82,178));

13.Text View Color
textview1.setTextColor(Color.parseColor("#000000"));

14.Text View Zoom In
ScaleAnimation scaleAnimation = new ScaleAnimation(1f,4f,1f,4f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); 
scaleAnimation.setInterpolator(new LinearInterpolator()); 
scaleAnimation.setDuration(1800); 
text.startAnimation(scaleAnimation);

15.Text View Zoom Out
ScaleAnimation scaleAnimation = new ScaleAnimation(1f,0.5f,1f,.50f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); 
scaleAnimation.setInterpolator(new AccelerateDecelerateInterpolator()); 
scaleAnimation.setDuration(1800); 
text.startAnimation(scaleAnimation);

16.Tab Layout
viewPager = new androidx.viewpager.widget.ViewPager(this);

viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
MyPagerAdapter adapter = new MyPagerAdapter();
viewPager.setAdapter(adapter);
viewPager.setCurrentItem(0);
base.addView(viewPager);

tabLayout = new com.google.android.material.tabs.TabLayout(this);
tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#000000"));
///You can change tab underline(indicator) color 
from here


tabLayout.setTabTextColors(Color.parseColor("#00ffffff"),///Tab color when not selected Color.parseColor("#000000"));///Tab color when selected
///You can change tab text color from here


final int[ ] tabIcons = {
R.drawable.ic_1,
R.drawable.ic_2,
R.drawable.ic_3,
R.drawable.ic_4
};
///here ic_1, ic_2, etc. are the names of icons which you added in image manager


tabLayout.setupWithViewPager(viewPager);
tabLayout.getTabAt(0).setIcon(tabIcons[0]);
tabLayout.getTabAt(1).setIcon(tabIcons[1]);
tabLayout.getTabAt(2).setIcon(tabIcons[2]);
tabLayout.getTabAt(3).setIcon(tabIcons[3]);

cod.addView(tabLayout);
}

private class MyPagerAdapter extends androidx.viewpager.widget.PagerAdapter {
public int getCount() {
return 4;
}

@Override public Object instantiateItem(ViewGroup collection, int position) {

LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
View v = inflater.inflate(R.layout.empty, null);

LinearLayout container = (LinearLayout) v.findViewById(R.id.linear1);

if (position == 0) {
ViewGroup parent = (ViewGroup) layout1.getParent();
if (parent != null) {
parent.removeView(layout1);
}container.addView(layout1);

} else if (position == 1) {
ViewGroup parent = (ViewGroup) layout2.getParent();
if (parent != null) {
parent.removeView(layout2);
}
container.addView(layout2);

} else if (position == 2) {
ViewGroup parent = (ViewGroup) layout3.getParent();
if (parent != null) {
parent.removeView(layout3);
}
container.addView(layout3);

} else if (position == 3) {
ViewGroup parent = (ViewGroup) layout4.getParent();
if (parent != null) {
parent.removeView(layout4);
}
container.addView(layout4);
}
collection.addView(v, 0);
return v;
}
@Override public void destroyItem(ViewGroup collection, int position, Object view) {
collection.removeView((View) view);
trash.addView((View) view);
}
@Override public CharSequence getPageTitle(int position) {
switch (position) {
case 0:
return "HOME";
case 1:
return "SETTINGS";
case 2:
return "PROFILE";
case 3:
return "MUSIC";
default:
return null;
}
///you can change the name of tabs from HOME, SETTINGS, PROFILE, etc you want to


}
@Override public boolean isViewFromObject(View arg0, Object arg1) {
return arg0 == ((View) arg1);}
@Override public Parcelable saveState() {
return null;
}
}
androidx.viewpager.widget.ViewPager viewPager;
com.google.android.material.tabs.TabLayout tabLayout;
private void foo() {


///add 4 linearV with height=WRAP_CONTENT and width = MATCH_PARENT and name that linears id (layout1, layout2, layout3, layout4)
///add 2 linearV or H with height and width = WRAP_CONTENT and name their id (base and trash)
///add one more linearH on the topp of all linear and name its id (cod), it will show yout tabs
///base and trash should be set in between cod and that 4 linears
///add anything to that 4 linear will show in the screen as different when particular tab will be clicked
///now add a custom layout (empty) and add a linear V or H with height and width = MATCH_PARENT

17.Tap Target
//To Use
//_view
//_title
//_msg
//_bgcolor

TapTargetView.showFor(MainActivity.this,
TapTarget.forView(_view, _title, _msg)
.outerCircleColorInt(Color.parseColor(_bgcolor))
.outerCircleAlpha(0.96f)
.targetCircleColorInt(Color.parseColor("#FFFFFF"))
.titleTextSize(25)
.titleTextColorInt(Color.parseColor("#FFFFFF"))
.descriptionTextSize(18)
.descriptionTextColor(android.R.color.white)
.textColorInt(Color.parseColor("#FFFFFF"))
.textTypeface(Typeface.SANS_SERIF)
.dimColor(android.R.color.black)
.drawShadow(true)
.cancelable(true)
.tintTarget(true)
.transparentTarget(true)
//.icon(Drawable)
.targetRadius(60),

new TapTargetView.Listener() {
@Override
public void onTargetClick(TapTargetView view) {
super.onTargetClick(view);
}
});

18.Status Bar Color
getWindow().setStatusBarColor(0xFFFFFFFF);

19.Password (Hide/Show)
//text into password style - Hide password
edittext1.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());


//password into text - Show password
edittext1.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());

20.Share Any View
Bitmap image = Bitmap.createBitmap(_view.getWidth(), _view.getHeight(), Bitmap.Config.ARGB_8888);
Canvas canvas = new Canvas(image);
android.graphics.drawable.Drawable bgDrawable = _view.getBackground();
if (bgDrawable!=null) {
bgDrawable.draw(canvas);
} else{
canvas.drawColor(Color.WHITE);
}
_view.draw(canvas);

java.io.File pictureFile = new java.io.File(getExternalCacheDir() + "/image.jpg");
if (pictureFile == null) {
Log.d("MainActivity", "Error creating media file, check storage permissions: ");
return;
}
try {
java.io.FileOutputStream fos = new java.io.FileOutputStream(pictureFile); 
image.compress(Bitmap.CompressFormat.PNG, 90, fos);
fos.close();
} catch (java.io.FileNotFoundException e) {
Log.d("MainActivity", "File not found: " + e.getMessage());
} catch (java.io.IOException e) {
Log.d("MainActivity", "Error accessing file: " + e.getMessage());
}
Intent iten = new Intent(android.content.Intent.ACTION_SEND);
iten.setType("*/*");
iten.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(getExternalCacheDir() + "/image.jpg")));
startActivity(Intent.createChooser(iten, "Send image"));

21.Elevation
this is code for moreblock

_view.setElevation((int)_number);


Ex-  linear1.setElevation(10f);

22.ScreenShot
}
private void openScreenshot(java.io.File imageFile) {
Intent intent = new Intent(); 
intent.setAction(Intent.ACTION_VIEW);
Uri uri = Uri.fromFile(imageFile); 
intent.setDataAndType(uri, "image/*");
startActivity(intent);
}

private void takeScreenshot() {
Date now = new Date(); 
android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
try {
String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
View v1 = getWindow().getDecorView().getRootView();
v1.setDrawingCacheEnabled(true);
Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache()); 
v1.setDrawingCacheEnabled(false);
java.io.File imageFile = new java.io.File(mPath);
java.io.FileOutputStream outputStream = new java.io.FileOutputStream(imageFile);
int quality = 100; 
bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
outputStream.flush();
outputStream.close();
openScreenshot(imageFile);
} catch (Throwable e) {
e.printStackTrace();
}

<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />

/*
If you want to use this on fragment view then use:
View v1 = getActivity().getWindow().getDecorView().getRootView();
//getActivity are Your activity example: (this).getWindow()
instead of
View v1 = getWindow().getDecorView().getRootView();
*/

23.Save Image
Bitmap bm = ((android.graphics.drawable.BitmapDrawable) imageview1.getDrawable()).getBitmap();

try {
java.io.File file = new java.io.File(getExternalCacheDir() + "/image.jpg");

java.io.OutputStream out = new java.io.FileOutputStream(file);

bm.compress(Bitmap.CompressFormat.JPEG, 100, out);

out.flush();
out.close();

} catch (Exception e) { showMessage(e.toString()); }
Intent iten = new Intent(android.content.Intent.ACTION_SEND);

iten.setType("*/*");

iten.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(getExternalCacheDir() + "/image.jpg")));

startActivity(Intent.createChooser(iten, "Save Or Share"));

24.Request Focus
youranywidget.requestFocus();

25.Replace All
String str = edit.getText().toString(); 
				str = str.replaceAll("value", "text for replace"); 
				edit.setText(str);
				
26.Progress Bar Color
progressBar.getProgressDrawable().setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);



//with drawable
android.graphics.drawable.Drawable progressDrawable = progressBar.getProgressDrawable().mutate(); progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN); progressBar.setProgressDrawable(progressDrawable);


27.Poup View
View popupView = getLayoutInflater().inflate(R.layout.myview, null);
final PopupWindow popup = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
t1.setOnClickListener(new OnClickListener() { public void onClick(View view) {
//YOUR CODE/BLOCKS HERE
popup.dismiss();
} });

t2.setOnClickListener(new OnClickListener() { public void onClick(View view) {
//YOUR CODES/BLOCKS HERE
popup.dismiss();
} });
popup.showAtLocation(popupView, Gravity.CENTER, 0, 0);

28.Open Url
startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("your url")));

29.Open Keyboard
android.view.inputmethod.InputMethodManager imm = (android.view.inputmethod.InputMethodManager) getSystemService(android.content.Context.INPUT_METHOD_SERVICE);
 imm.showSoftInput(yourEditText, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT);
 
30.On/Off Mobile Data
private final static String COMMAND_L_ON = "svc data enable
 ";
private final static String COMMAND_L_OFF = "svc data disable
 ";
private final static String COMMAND_SU = "su";

public static void setConnection(boolean enable,Context context){
 String command;
 if(enable)
 command = COMMAND_L_ON;    
 else
 command = COMMAND_L_OFF;
 try{
 java.lang.Process su = Runtime.getRuntime().exec(COMMAND_SU);
 java.io.DataOutputStream outputStream = new java.io.DataOutputStream(su.getOutputStream());
 outputStream.writeBytes(command);
 outputStream.flush();
 outputStream.writeBytes("exit
");
 outputStream.flush();
 try {
 su.waitFor();
 } catch (InterruptedException e) {
 e.printStackTrace();
 }
 outputStream.close();
 }catch(java.io.IOException e){
 e.printStackTrace();
 }
}

31.Nagivation Bar
//Show

getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

//Hide

getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

32.List View Speed Srcoll
//by Orexot

listview.setFriction(2f);

//instead «2f» set yor value. Ex. «1,5f»

33.List View Text Color
final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, yourlistname){ @Override public View getView(int position, View convertView, ViewGroup parent){ View view = super.getView(position, convertView, parent);TextView tv = (TextView) view.findViewById(android.R.id.text1); tv.setTextColor(Color.WHITE); return view; } }; listview1.setAdapter(arrayAdapter)

34.List View Swipe Refresh
final androidx.swiperefreshlayout.widget sl = new androidx.swiperefreshlayout.widget(MainActivity.this);
sl.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.MATCH_PARENT));
linear1.addView(sl);
linear1.removeView(listview1);
sl.addView(listview1);
sl.setOnRefreshListener( new androidx.swiperefreshlayout.widget.OnRefreshListener() { @Override public void onRefresh() { _refresh(); } } );
// Make moreblock refresh for onRefresh action

35.List View Divider
listview1.setDivider(null); 
listview1.setDividerHeight(0);

36.List View Animation
Animation animation; animation = AnimationUtils.loadAnimation( getApplicationContext(), android.R.anim.slide_in_left ); animation.setDuration(300);

linear1.startAnimation(animation); animation = null;

37.List View Select Item Animation
android.graphics.drawable.ColorDrawable[] color = {
new android.graphics.drawable.ColorDrawable(Color.parseColor("#b6c75c")), new android.graphics.drawable.ColorDrawable(Color.parseColor("#FF0000"))
};
android.graphics.drawable.TransitionDrawable trans = new android.graphics.drawable.TransitionDrawable(color);
_param2.setBackground(trans);
trans.startTransition(2000); // duration 2 seconds

// Go back to the default background color of Item
android.graphics.drawable.ColorDrawable[] color2 = {
new android.graphics.drawable.ColorDrawable(Color.parseColor("#FF0000")), new android.graphics.drawable.ColorDrawable(Color.parseColor("#FFFFFF"))
};
android.graphics.drawable.TransitionDrawable trans2 = new android.graphics.drawable.TransitionDrawable(color2);
_param2.setBackground(trans2);
 trans2.startTransition(2000); // duration 2 seconds
 
38.Linkify
textview1.setClickable(true);
android.text.util.Linkify.addLinks(textview1, android.text.util.Linkify.ALL);
textview1.setLinkTextColor(Color.parseColor("#009688"));
textview1.setLinksClickable(true);

39.List View Top Scroll
button1.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View _view) {
	    vscroll1.fullScroll(ScrollView.FOCUS_UP);
	    	}
});

40.List View Top Scroll 2
button1.setOnClickListener(new View.OnClickListener() {
	@Override
	public void onClick(View _view) {
	    listview1.setSelectionAfterHeaderView();
	    	}
});

41.Layout Height/Width
LinearLayout layout = findViewById(R.id.linear1);
LayoutParams params = layout.getLayoutParams();
params.height = 100;
params.width = 100;
layout.setLayoutParams(params);

42.Layout To Image
//onCreate
}
private void storeImage(Bitmap image) { java.io.File pictureFile = new java.io.File(getExternalCacheDir() + "/image.jpg");
if (pictureFile == null) { Log.d("MainActivity", "Error creating media file, check storage permissions: ");
return; } try {
java.io.FileOutputStream fos = new java.io.FileOutputStream(pictureFile); image.compress(Bitmap.CompressFormat.PNG, 90, fos);
fos.close(); } catch (java.io.FileNotFoundException e) { Log.d("MainActivity", "File not found: " + e.getMessage()); } catch (java.io.IOException e) { Log.d("MainActivity", "Error accessing file: " + e.getMessage());
}

Intent iten = new Intent(android.content.Intent.ACTION_SEND);
iten.setType("*/*");
iten.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new java.io.File(getExternalCacheDir() + "/image.jpg")));
startActivity(Intent.createChooser(iten, "Ў掭펭ꎭ??/??Ꭽꎭ?"));
}

private Bitmap getBitmapFromView(View view) { Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
Canvas canvas = new Canvas(returnedBitmap);
android.graphics.drawable.Drawable bgDrawable =view.getBackground();
if (bgDrawable!=null) { bgDrawable.draw(canvas); } else{ canvas.drawColor(Color.WHITE); }
view.draw(canvas);
return returnedBitmap;


//button onClick

storeImage(getBitmapFromView(linear2));

43.Layout Gradient
android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM, new int[]{
0xFF616261, //Top Color
0xFF131313 //Bottom Color
});
gd.setCornerRadius(0f);
linear1.setBackgroundDrawable(gd);

44.Layout Fit Device
Display display = getWindowManager().getDefaultDisplay();
Point size = new Point();
try {
display.getRealSize(size);
} catch (NoSuchMethodError err) {
display.getSize(size);
}
int width = size.x;
int height = size.y;
LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams((int)(width), LinearLayout.LayoutParams.MATCH_PARENT);
layout1.setLayoutParams(lp);

45.Intent Uninstall Apk
Uri packageURI = Uri.parse("package:".concat("com.package.name")); Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI); startActivity(uninstallIntent);

46.Intent Send Email
String to = "youremail@gmail.com"; String
//Your email id here
 subject= "Your Subject"; 
//your subject here
String body="Your Body"; 
//your body here
String mailTo = "mailto:" + to + "?&subject=" + Uri.encode(subject) + "&body=" + Uri.encode(body); Intent emailIntent = new Intent(Intent.ACTION_VIEW); emailIntent.setData(Uri.parse(mailTo)); startActivity(emailIntent);

//create 4 string variables : to , subject , body , mailTo

47.Intent Open App Settings
//by ezzex (check author)
//works on API 1 or high
Intent open_settings = new Intent(android.provider.Settings.ACTION_SETTINGS);
startActivity(open_settings);

48.Intent Open Download
startActivity(new android.content.Intent(android.app.DownloadManager.ACTION_VIEW_DOWNLOADS));

49.Intent Open App
Intent launch = getPackageManager().getLaunchIntentForPackage("com.my.project"); 

if (launch != null) { startActivity(launchIntent); }

50.Intent Close App
finishAffinity();
int pid = android.os.Process.myPid();
android.os.Process.killProcess(pid); 
Intent intent = new Intent(Intent.ACTION_MAIN);
intent.addCategory(Intent.CATEGORY_HOME);
startActivity(intent);

51.Image Send From Url
Glide.with(getApplicationContext()).load(Uri.parse(url)).into(imageview1);

52.Image Zoom Out
float x=preview.getScaleX(), y=preview.getScaleY(),yenix=x-1,yeniy=y-1; preview.setScaleX(yenix);preview.setScaleY(yeniy);

53.Image Zoom In

float x=preview.getScaleX(), y=preview.getScaleY(),yenix=x+1,yeniy=y+1; preview.setScaleX(yenix);preview.setScaleY(yeniy);

54.Image Radius
Bitmap bm = ((android.graphics.drawable.BitmapDrawable)imageview1.getDrawable()).getBitmap();

imageview1.setImageBitmap(getRoundedCornerBitmap(bm, 360));

// onCreate
}
public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
Canvas canvas = new Canvas(output);
final int color = 0xff424242;
final Paint paint = new Paint();
final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
final RectF rectF = new RectF(rect);
final float roundPx = pixels;
paint.setAntiAlias(true);
canvas.drawARGB(0, 0, 0, 0);
paint.setColor(color);
canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); 
canvas.drawBitmap(bitmap, rect, rect, paint);
return output;

55.Image Crop
private void performCrop(Uri picUri) {
 try {
 Intent cropIntent = new Intent("com.android.camera.action.CROP");
 // indicate image type and Uri
 cropIntent.setDataAndType(picUri, "image/*");
 // set crop properties here
 cropIntent.putExtra("crop", true);
 // indicate aspect of desired crop
 cropIntent.putExtra("aspectX", 1);
 cropIntent.putExtra("aspectY", 1);
 // indicate output X and Y
 cropIntent.putExtra("outputX", 128);
 cropIntent.putExtra("outputY", 128);
 // retrieve data on return
 cropIntent.putExtra("return-data", true);
 // start the activity - we handle returning in onActivityResult
 startActivityForResult(cropIntent, PIC_CROP);
 }
 // respond to users whose devices do not support the crop action
 catch (ActivityNotFoundException anfe) {
 // display an error message
 String errorMessage = "Whoops - your device doesn't support the crop action!";
 Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
 toast.show();
 }
}

final int PIC_CROP = 1;
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 super.onActivityResult(requestCode, resultCode, data);

 if (requestCode == PIC_CROP) {
 if (data != null) {
 // get the returned data
 Bundle extras = data.getExtras();
 // get the cropped bitmap
 Bitmap selectedBitmap = extras.getParcelable("data");

 imageview1.setImageBitmap(selectedBitmap);
 }
 }
}

56.Hightlight Text View
//First Make MoreBlock with textview as view
//Add TextView with name regex1 - regex10 set to zero width and height and add regex compile to text


//Config Color:
final String secondaryColor = "#5c6bc0";
final String primaryColor = "#42a5f5";
final String numbersColor = "#26a69a";
final String quotesColor = "#ff1744";
final String commentsColor = "#9e9e9e";

//Text Highlight.
_view.addTextChangedListener(new TextWatcher() {
ColorScheme keywords1 = new ColorScheme(java.util.regex.Pattern.compile(regex1.getText().toString().concat(regex2.getText().toString())), Color.parseColor(secondaryColor));
ColorScheme keywords2 = new ColorScheme(java.util.regex.Pattern.compile(regex3.getText().toString().concat(regex4.getText().toString().concat(regex5.getText().toString()))), Color.parseColor(primaryColor));
ColorScheme keywords3 = new ColorScheme(java.util.regex.Pattern.compile(regex6.getText().toString()), Color.parseColor(numbersColor));
ColorScheme keywords4 = new ColorScheme(java.util.regex.Pattern.compile(regex7.getText().toString()), Color.parseColor(secondaryColor));
ColorScheme keywords5 = new ColorScheme(java.util.regex.Pattern.compile(regex9.getText().toString()), Color.parseColor(quotesColor));
ColorScheme keywords6 = new ColorScheme(java.util.regex.Pattern.compile(regex10.getText().toString()), Color.parseColor(commentsColor));
ColorScheme keywords7 = new ColorScheme(java.util.regex.Pattern.compile(regex8.getText().toString()), Color.parseColor(numbersColor));
final ColorScheme[] schemes = {keywords1, keywords2, keywords3, keywords4, keywords5, keywords6, keywords7};
@Override
public void beforeTextChanged(CharSequence s, int start, int count, int after) {
}
@Override
public void onTextChanged(CharSequence s, int start, int before, int count) {
}
@Override
public void afterTextChanged(Editable s) {
removeSpans(s, android.text.style.ForegroundColorSpan.class);
for(ColorScheme scheme : schemes) {
for(java.util.regex.Matcher m = scheme.pattern.matcher(s);
m.find();) {
if (scheme == keywords4) {
s.setSpan(new android.text.style.ForegroundColorSpan(scheme.color), m.start(), m.end()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
} else {
s.setSpan(new android.text.style.ForegroundColorSpan(scheme.color), m.start(), m.end(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
}
}
}
}
void removeSpans(Editable e, Class<? extends android.text.style.CharacterStyle> type) {
android.text.style.CharacterStyle[] spans = e.getSpans(0, e.length(), type);
for (android.text.style.CharacterStyle span : spans) {
e.removeSpan(span);
}
}
class ColorScheme {
final java.util.regex.Pattern pattern;
final int color;
ColorScheme(java.util.regex.Pattern pattern, int color) {
this.pattern = pattern;
this.color = color;
}
}
});


//For REGEX Example:
regex1 = b(out|print|println|valueOf|toString|concat|equals|for|while|switch|getText
regex2 = |println|printf|print|out|parseInt|round|sqrt|charAt|compareTo|compareToIgnoreCase|concat|contains|contentEquals|equals|length|toLowerCase|trim|toUpperCase|toString|valueOf|substring|startsWith|split|replace|replaceAll|lastIndexOf|size)b
regex3 = b(public|private|protected|void|switch|case|class|import|package|extends|Activity|TextView|EditText|LinearLayout|CharSequence|String|int|onCreate|ArrayList|float|if|else|static|Intent|Button|SharedPreferences
regex4 = |abstract|assert|boolean|break|byte|case|catch|char|class|const|continue|default|do|double|else|enum|extends|final|finally|float|for|goto|if|implements|import|instanceof|interface|long|native|new|package|private|protected|
regex5 = public|return|short|static|strictfp|super|switch|synchronized|this|throw|throws|transient|try|void|volatile|while|true|false|null)b
regex6 = b([0-9]+)b
regex7 = (w+)(()+
regex8 = @s*(w+)
regex9 = "(.*?)"|'(.*?)'
regex10 = /*(?:.|[
nr])*?*/|//.*

//Note: add to TextView String.

57.Hightlight Text
textview1.addTextChangedListener(new TextWatcher() {
	
	  ColorScheme keywords = new ColorScheme(
	      Pattern.compile(
	          "\\b(transient|Activity|View|Log|MotionEvent|void|const|static|volatile|interface|native|protected|final|abstract|synchronized|enum|instanceof|assert|break|goto|return|new|throw|throws|super|extends|implements|import)\\b"),
	      Color.parseColor("#FFD84315") //brown
	  );
	
	ColorScheme keywordskeywords = new ColorScheme(
	      Pattern.compile(
	          "\\b(class|private|public|this|0|arg|args|out)\\b"),
	      Color.parseColor("#FFFF1744") //rose
	  );
	
	ColorScheme keywords2 = new ColorScheme(
	      Pattern.compile(
	          "\\b(package|strictfp|char|short|int|long|double|String|float|byte|boolean|default|do|continue)\\b"),
	      Color.parseColor("#FF4DB6AC") 
	  );
	
	ColorScheme keywords3 = new ColorScheme(
	      Pattern.compile(
	          "\\b(if|else|switch|case|for|while)\\b"),
	      Color.parseColor("#FFD500F9") //mauve
	  );
	
	ColorScheme keywords4 = new ColorScheme(
	      Pattern.compile(
	          "\\b(try|catch|finally|true|false|null|IOException|java.io.IOException)\\b"),
	      Color.parseColor("#FFFFC400") // jaune foncé
	  );
	
	ColorScheme keywords5 = new ColorScheme(
	      Pattern.compile(
	          "\\b(BroadcastReceiver|Intent|Integer|CharSequence|Dialog|OnFocusChangeListener|OnCreateContextMenuListener|OnKeyListener|WifiManager|StringBuffer|StringBuilder|OnClickListener|OnTouchListener|OnLongClickListener|OnCheckedChanged|addTextChangedListener|Color.parseColor|color|colors|parseColor|Color|ColorScheme|ProcessBuilder|Process|PackageManager|)\\b"),
	      Color.parseColor("#FF2196F3") //Blue
	  );
	
	ColorScheme keywords6 = new ColorScheme(
	      Pattern.compile(
	          "\\b(LinearLayout|LinearParams|CheckboxGroup|CheckBox|Button|Switch|Spannable|EditText|TextView|ImageView|CircleImageView|RadioButton|TabLayout|SwipRefreshLayout)\\b"),
	      Color.parseColor("#FFC0CA33") //Lime foncé
	  );
	
	ColorScheme newtypes = new ColorScheme(
	      Pattern.compile(
	          "\\b(onTextChanged|isSelected|isChecked|CheckBox|Button|Switch|Spannable|EditText|TextView|ImageView|CircleImageView|RadioButton|TabLayout|SwipRefreshLayout)\\b"),
	      Color.parseColor("#76ff03") //green claire
	  );
	
	ColorScheme keywords7 = new ColorScheme(    
	      Pattern.compile(
	          "\\b(java.io.|BufferedReader|InputStream|BufferedWriter|InputStreamReader|File|FileReader|java.io.BufferedReader|setElevation|setBackground|setStroke|setCornerRadii|java.io.InputStream|android.graphics.drawable.GradientDrawable|android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM|java.io.BufferedWriter|java.io.InputStreamReader|java.io.File|java.io.FileReader)\\b"),
	      Color.parseColor("#FFBA68C8") 
	  );
	
	
	  final ColorScheme[] schemes = {newtypes, keywordskeywords , keywords4, keywords3, keywords2, keywords, keywords5, keywords6, keywords7 };
	
	  @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		
		  }
	
	  @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
		
		  }
	
	  @Override public void afterTextChanged(Editable s) {
		    removeSpans(s, ForegroundColorSpan.class);
		    for (ColorScheme scheme : schemes) {
			      for(Matcher m = scheme.pattern.matcher(s); m.find();) {
				        s.setSpan(new ForegroundColorSpan(scheme.color),
				            m.start(),
				            m.end(),
				            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				      }
			    }
		  }
	
	  void removeSpans(Editable e, Class<? extends CharacterStyle> type) {
		    CharacterStyle[] spans = e.getSpans(0, e.length(), type);
		    for (CharacterStyle span : spans) {
			      e.removeSpan(span);
			    }
		  }
	
	  class ColorScheme {
		    final Pattern pattern;
		    final int color;
		
		     ColorScheme(Pattern pattern, int color) {
			      this.pattern = pattern;
			      this.color = color;
			    }
		  }
	
});

58.Horizontal List View (OnCreate)
}
	
	private GridView grid; 
	
	
	public class Gridview1Adapter extends BaseAdapter {
				ArrayList<HashMap<String, Object>> _data;
				public Gridview1Adapter(ArrayList<HashMap<String, Object>> _arr) {
						_data = _arr;
				}
				
				@Override
				public int getCount() {
						return _data.size();
				}
				
				@Override
				public HashMap<String, Object> getItem(int _index) {
						return _data.get(_index);
				}
				
				@Override
				public long getItemId(int _index) {
						return _index;
				}
				
				@Override
				public View getView(final int _position, View _view, ViewGroup _viewGroup) {
						LayoutInflater _inflater = (LayoutInflater)getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						View _v = _view;
						if (_v == null) {
								_v = _inflater.inflate(R.layout.list_custom, null);
						}
						
						final ImageView icon = (ImageView) _v.findViewById(R.id.icon);



/* Bitmap bm = ((android.graphics.drawable.BitmapDrawable)icon.getDrawable()).getBitmap();

icon.setImageBitmap(getRoundedCornerBitmap(bm, 360));
*/

						final TextView text = (TextView) _v.findViewById(R.id.text);

LinearLayout lin2 = (LinearLayout)
_v.findViewById(R.id.linear1);


	
						return _v;
				}
				
59.Grid View (ASD Code)
grid = new GridView(MainActivity.this);
		
		grid.setLayoutParams(new GridView.LayoutParams(listmap.size()*(int)getDip(100), GridLayout.LayoutParams.WRAP_CONTENT));
		
		grid.setNumColumns(listmap.size());
		
		grid.setBackgroundColor(Color.WHITE);
		
		grid.setVerticalSpacing(5);
		
		grid.setHorizontalSpacing(5);
		
		grid.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		
		grid.setAdapter(new Listview1Adapter(listmap));
		
		linear1.addView(grid);
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView parent, View view, int _pos, long id) {
				showMessage(String.valueOf(_pos));
			}
		});
linear1.removeAllViews();

linear1.addView(grid);

60.Get App Version
try {
android.content.pm.PackageInfo pinfo = getPackageManager().getPackageInfo( package_name, android.content.pm.PackageManager.GET_ACTIVITIES);
ver = pinfo.versionName; }
catch (Exception e){ showMessage(e.toString()); }

61.Gradient Color
int[] colors = {Color.rgb(138,41,81),Color.rgb(41,53,158)};
android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.BR_TL, colors);
gd.setCornerRadius(0f); 
gd.setStroke(0,Color.WHITE);
if(android.os.Build.VERSION.SDK_INT >= 16) {linear1.setBackground(gd); } else {linear1.setBackgroundDrawable(gd);}

61.Get Clipart(Paste)
((android.content.ClipboardManager) getSystemService(getApplicationContext().CLIPBOARD_SERVICE)).setPrimaryClip(android.content.ClipData.newPlainText("clipboard", %2$s));

62.Glide Set Image From Url
Glide.with(mContext) .load(imageUrl) .animate(R.anim.abc_fade_in) .signature(new StringSignature(String.valueOf(System.currentTimeMillis()))) .centerCrop() .into(mImageView_photo) ;

63.Gif To Image View
Glide.with(context).load(url).into(new GlideDrawableImageViewTarget(img2));

64.Get Image From File
//onCreate

}

private static final String STATE_IMAGE_URI = "STATE_IMAGE_URI";

private Uri imageUri;

public void onSaveInstanceState(Bundle state) {

super.onSaveInstanceState(state);
if (imageUri != null ) {
state.putParcelable(STATE_IMAGE_URI, imageUri);
}

}

public void onRestoreInstanceState(Bundle state) {
super.onRestoreInstanceState(state);
if(state == null || !state.containsKey(STATE_IMAGE_URI)) return;

setImage((Uri) state.getParcelable(STATE_IMAGE_URI));

}

private static final int IMAGE_REQUEST_CODE = 9;

private void chooseImage() {

Intent intent = new Intent();
intent.setType("image/*");
intent.setAction(Intent.ACTION_GET_CONTENT);
startActivityForResult(Intent.createChooser(intent, "Select picture"), IMAGE_REQUEST_CODE);


}

public void onActivityResult(int requestCode, int resultCode, Intent data) {

super.onActivityResult(requestCode, resultCode, data);
if (requestCode != IMAGE_REQUEST_CODE) {
return;
}

if (resultCode != Activity.RESULT_OK) {
return;
}

setImage(data.getData());

}

private void setImage(Uri uri) {
imageUri = uri;
imageview1.setImageURI(uri);
}



private void nothing() {



//image choose
chooseImage();

65.Get Image From Audio
MediaMetadataRetriever retriever = new MediaMetadataRetriever(); 
retriever.setDataSource(songPath); 
byte[] art = retriever.getEmbeddedPicture(); 
if( art != null ){ 
Bitmap bitmap = BitmapFactory.decodeByteArray(art,0,art.length); 
androidx.core.graphics.drawable.RoundedBitmapDrawable rbd = androidx.core.graphics.drawable.RoundedBitmapDrawableFactory.create(getResources(), bitmap); 
rbd.setCircular(true); 
imageview1.setImageDrawable(rbd); 
} else { 
imageview1.setImageResource(R.drawable.default_image); 
}

66.Get Language From Device
/* Create variable language */
language = Locale.getDefault().getDisplayLanguage();

67.Get Device Info
java.lang.System.getProperty("os.version"); // OS version

String api_level = android.os.Build.VERSION.SDK // API Level
String device = android.os.Build.DEVICE // Device name
String model = android.os.Build.MODEL // Model 
String board_name = android.os.Build.BOARD // Board name
String bootloader_version = android.os.Build.BOOTLOADER // Bootloader version number
String display = android.os.Build.DISPLAY // Display
String manufacturer = android.os.Build.MANUFACTURER // Manufacturer
String build_timestamp = android.os.TIME // The time the build was produced

68.Get Current Time
String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

69.Firebase Email Verification
FirebaseAuth auth = FirebaseAuth.getInstance();
com.google.firebase.auth.FirebaseUser user = 
auth.getCurrentUser();

user.sendEmailVerification().addOnCompleteListener
(new 
OnCompleteListener<Void>()
{ @Override
public void onComplete(Task task)
{ if (task.isSuccessful()) {


} else {

}
}});

70.FileUtil Write File
FileUtil.writeFile(FileUtil.getExternalStorageDir().concat("/file.txt"), "some value");

71.FileUtil Read File
FileUtil.readFile(path);

72.FileUtil Move File
FileUtil.moveFile(path, movePath);

73.FileUtil File Length
String legth = FileUtil.getFileLength(path);
textview1.setText(legth);

74.FileUtil Delete File
FileUtil.deleteFile(path);

75.FileUtil Copy File
FileUtil.copyFile(path);

76.FileUtil File Picker
//Add This on Build
dependencies {
	compile 'com.github.angads25:filepicker:1.1.1'
}

//Add Import file
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.github.angads25.filepicker.controller.DialogSelectionListener;

//Add on File Chooser
DialogProperties properties = new DialogProperties();
properties.selection_mode = DialogConfigs.SINGLE_MODE;
properties.selection_type = DialogConfigs.FILE_SELECT;
properties.root = new java.io.File(DialogConfigs.DEFAULT_DIR);
properties.error_dir = new java.io.File(DialogConfigs.DEFAULT_DIR);
properties.offset = new java.io.File(DialogConfigs.DEFAULT_DIR);
properties.extensions = null;
final FilePickerDialog dialog = new FilePickerDialog(MainActivity.this,properties);
dialog.setTitle("Select a File");
dialog.setDialogSelectionListener(new DialogSelectionListener() {
	@Override
	public void onSelectedFilePaths(String[] files) {
	//files is the array of the paths of files selected by the Application User.
		showMessage(files[0]);
	}
});
dialog.show();

//Done and test

//Multi Choice: MULTI_MODE
//Path Select: DIR_SELECT
//Default Root: "/sdcard"


//use extention
private int countCommas(String fextension) {
 int count = 0;
 for(char ch:fextension.toCharArray())
 { if(ch==',') {
 count++;
 }
 }
 return count;
}
String fextension = "txt,pdf";
if(fextension.length()>0) {
int commas = countCommas(fextension);
String[] exts = new String[commas + 1];
StringBuffer buff = new StringBuffer();
int i = 0;
for (int j = 0; j < fextension.length(); j++) {
	if (fextension.charAt(j) == ',') {
		exts[i] = buff.toString();
		buff = new StringBuffer();
		i++;
	} else {
		buff.append(fextension.charAt(j));
	}
}
exts[i] = buff.toString();
properties.extensions=exts;
} else {
	properties.extensions=null;
}

77.Fab Hide
_fab.hide();

78.Fab Show
_fab.show();

79.Fab Size
//Use AUTO,NORMAL,MINI to Change Size
_fab.setSize(FloatingActionButton.SIZE_AUTO);

80.Exit App
finishAffinity();
int pid = android.os.Process.myPid();
android.os.Process.killProcess(pid);
Intent intent = new Intent(Intent.ACTION_MAIN);
intent.addCategory(Intent.CATEGORY_HOME);
startActivity(intent);

81.Edit Text Set Err
edittext1.setError("Your Error");

82.Edit Text Upper Case
edittext1.setFilters(new InputFilter[]{new InputFilter.AllCaps()});

83.Edit Text Lower Case
edittext1.setFilters(new InputFilter[] {
 new InputFilter.AllCaps() {
 @Override
 public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
 return String.valueOf(source).toLowerCase();
 }
 }
});

84.Edit Text IME Option
edittext1.setOnEditorActionListener(new EditText.OnEditorActionListener() { public boolean onEditorAction(TextView v, int actionId, KeyEvent event) { if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_GO) { button1.performClick(); return true; } return false; } });

85.Edit Text Show/Hide Keyboard
//Enable
edittext1.setShowSoftInputOnFocus(true);

//Disable
edittext1.setShowSoftInputOnFocus(false);

86.Edit Text Suggestion (Disable)
edittext1.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

87.Edit Text Suggestion (Enable)
edittext1.setRawInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

88.Edit Text Background Color
edittext1.getBackground().setColorFilter(Color.parseColor("#000000"), PorterDuff.Mode.SRC_ATOP);

89.Edit Text Border
android.graphics.drawable.ShapeDrawable shape = new android.graphics.drawable.ShapeDrawable(new android.graphics.drawable.shapes.RectShape());
shape.getPaint().setColor(Color.RED);
shape.getPaint().setStyle(Paint.Style.STROKE);
shape.getPaint().setStrokeWidth(3);
edittext1.setBackground(shape);

90.Edit Text Limit
_Edittext_Set_Limit(edittext1, 5);

91.Edit Text Dialog Show
final EditText 


edittext


= new EditText(


MainActivity


.this);
LinearLayout.LayoutParams lpar = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);



edittext.setLayoutParams(lpar);



d.setView(edittext);

92.Duplicate Image
Drawable copy = imageview1.getDrawable(); imageview2.setImageDrawable(copy);

93.Dialog Set Icon
your_dialog.setIcon(R.drawable.your_icon_name);

94.Debug Code
String madeErrMsg = "";
String[] exceptionType = {"StringIndexOutOfBoundsException","IndexOutOfBoundsException","ArithmeticException","NumberFormatException","ActivityNotFoundException"};
String[] errMessage= {"Invalid string operation\n","Invalid list operation\n","Invalid arithmetical operation\n","Invalid toNumber block operation\n","Invalid intent operation"};
Intent intent = getIntent();
		String errMsg = "";
		if(intent != null){
				errMsg = intent.getStringExtra("error");
		String[] spilt = errMsg.split("\n");
				//errMsg = spilt[0];
				try {
						for (int j = 0; j < exceptionType.length; j++) {
								if (spilt[0].contains(exceptionType[j])) {
										madeErrMsg = errMessage[j];
				
										int addIndex = spilt[0].indexOf(exceptionType[j]) + exceptionType[j].length();
				
										madeErrMsg += spilt[0].substring(addIndex, spilt[0].length());
										break;
				
								}
						}
		
						if(madeErrMsg.isEmpty()) madeErrMsg = errMsg;
				}catch(Exception e){}
	
		}


95.Dialog List View Menu
{
list1.add("Google");
list1.add("Yandex");
list1.add("Yahoo");
list1.add("Bingo");
}

// button1 onClick()

dialog.setAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, list1), new DialogInterface.OnClickListener() {
@Override public void onClick(DialogInterface dia, int _pos) {

textview1.setText(list1.get(_pos)); 
// txt = list1.get(_pos);

}
});

dialog.show();

96.Dialog Set Cancelable
dialog_name.setCancelable(false);

97.Dialog Rounder
final AlertDialog dialog = new AlertDialog.Builder(VerificationActivity.this).create();
LayoutInflater inflater = getLayoutInflater();

View convertView = (View) inflater.inflate(R.layout.mylayout, null);
dialog.setView(convertView);



dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); dialog.getWindow().setBackgroundDrawable(new android.graphics.drawable.ColorDrawable(Color.TRANSPARENT));



dailog.show();
// TODO

98.Dialog Date Picker
DatePicker dp=new DatePicker(MainActivity.this);
linear1.addView(dp);
Calendar calendar = Calendar.getInstance();
dp.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),new DatePicker.OnDateChangedListener(){
@Override public void onDateChanged(DatePicker datePicker, int i, int i1, int i2) {
Toast.makeText(getApplicationContext(),datePicker.getDayOfMonth() + "-" +datePicker.getMonth() + "-"+datePicker.getYear(),Toast.LENGTH_SHORT).show();
} });


99.Dialog Custom
final AlertDialog dialog1 = new AlertDialog.Builder(MainActivity.this).create();
View inflate = getLayoutInflater().inflate(R.layout.custom,null); dialog1.setView(inflate);


dialog1.setTitle ("title");


Button but1 = (Button)inflate.findViewById(R.id.button1);


but1.setOnClickListener(new OnClickListener() { public void onClick(View view) { _Test(); } });


dialog1.show();



dialog1.setCancelable(false);

100.Detected Keyboard
View rootView = (View)anylinear.getRootView();
rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
	@Override
	public void onGlobalLayout() {
		int heightDiff = rootView.getRootView().getHeight() - rootView.getHeight();

		if (heightDiff > 100) { 
			//keyboard is opened
		} else { 
			//keyboard is closed
		}
	}
});

