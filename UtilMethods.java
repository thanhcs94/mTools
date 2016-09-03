import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * @ githug.com/thanhcs94.
 */

public class UtilMethods {

    //! set this value to false when publishing to google play
    private static final boolean APP_TEST_MODE = true;
    private static AlertDialog dialog = null;
    private SimpleDateFormat appViewFormat;

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void l(Context context, String tag, String msg) {
        if (APP_TEST_MODE) {
            if (TextUtils.isEmpty(tag)) {
                tag = "logCheck";
            }
            Log.v(context.getClass().getSimpleName() + ": " + tag + " --> ", msg);
        }
    }


    /**
     * @param context
     * @return true or false mentioning the device is connected or not
     * @brief checking the internet connection on run time
     */
    public static boolean isConnectedToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
            }
        }
        return false;
    }

    /**
     * @param context the application context
     * @param url     the specified url to which the browser will be redirected
     * @brief methods for calling browser's intent with specified url
     */
    public static void browseUrl(Context context, String url) {
        if (!url.startsWith("http") && !url.startsWith("https"))
            url = "http://" + url;
        Intent openBrowserIntent = new Intent(Intent.ACTION_VIEW);
        openBrowserIntent.setData(Uri.parse(url));
        context.startActivity(openBrowserIntent);
    }

    /**
     * @param context the application context
     * @param number  the specified phone number
     * @brief methods for doing a phone call with specified phone number
     */
    public static void phoneCall(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + number));
        context.startActivity(intent);
    }

    /**
     * @param context the application context
     * @return true or false
     * @brief methods for identifying the device is supported for calling feature or not
     */
    public static boolean isDeviceCallSupported(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * @param context the application context
     * @param address the specified email address
     * @brief methods for sending a mail via mail intent
     */
    public static void mailTo(Context context, String address) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", address, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Contact with Audacity IT Solutions via company profile app");
        context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
    }

    /**
     * @param context application context
     * @param subject
     * @param body
     * @brief method for sharing plain text via default share intent
     */
    public static void shareText(Context context, String subject, String body) {
        Intent txtIntent = new Intent(android.content.Intent.ACTION_SEND);
        txtIntent.setType("text/plain");
        txtIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        txtIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(txtIntent, "Share " +
                context.getString(R.string.app_name)));
    }

    /**
     * @param context the application context
     * @param name    the subject of the mail to be sent
     * @param address the specified email address
     * @brief methods for sending a mail via mail intent
     */
    public static void sendMail(Context context, String name, String address) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", address, null));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello " + name);
        context.startActivity(Intent.createChooser(emailIntent, "Contact with Us!"));
    }

    /**
     * @param activity the context of the activity
     * @brief methods for showing the soft keyboard by forced
     */
    public static void showSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(activity.getCurrentFocus(), 0);
        }
    }

    /**
     * @param activity the context of the activity
     * @brief methods for hiding the soft keyboard by forced
     */
    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity
                    .getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * @param fragment the fragment from where the keyboard will be removed
     * @brief methods for hiding the soft keyboard by forced
     */
    public static void hideSoftKeyboard(Fragment fragment) {
        if (fragment.getActivity().getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) fragment.getActivity()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(fragment.getActivity()
                    .getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * @param context the application context
     * @return Point containing the width and height
     * @brief methods for getting device window height and width via Point object
     */
    public static Point getWindowSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    public static int getActionBarSize(Context context) {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
             actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                    context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }


    /**
     * @param context the application context
     * @param dp      the value in density pixel to be converted into pixel
     * @return pixel in int
     * @brief convert density pixel to standard pixel
     */
    public static int dpToPx(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    /**
     * @param context the application context
     * @return true or false
     * @brief methods for checking the device's gps is enable or not
     */
    public static boolean isGpsEnable(Context context) {
        LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    /**
     * @param context  the application context
     * @param fileName name of the file from which the text will be loaded
     * @return json text in String
     * @brief methods for loading dummy JSON String from asset folder
     */
    public static String loadJSONFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("json/" + fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    /**
     * @param context   the application context
     * @param imageName the name of the image file
     * @return \c Uri object
     * @brief methods for getting \c Uri of a drawable from file name
     */
    public static String getDrawableFromFileName(Context context, String imageName) {
        return Uri.parse("android.resource://" + context.getPackageName() + "/drawable/" + imageName).toString();
    }

    public static int getDrawableIdFromFileName(Context context, String nameOfDrawable) {
        return context.getResources().getIdentifier(nameOfDrawable, "drawable", context.getPackageName());
    }


    /**
     * @brief check device version lollipop or higher
     */
    public static boolean isLollipopOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    /**
     * @brief check device version kitkat or higher
     */
    public static boolean isKitkatOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    /**
     * @brief check device version kitkat or higher
     */
    public static boolean isJellyBeanOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * @param context is the context to get the resources
     * @return the screen width in pixels
     * @brief Returns the screen width in pixels
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.widthPixels;
    }

    /**
     * @param context the context to get the resources from
     * @param attr    is the attribute dimension we want to know the size from
     * @return the size in pixels of an attribute dimension
     * @brief Returns the size in pixels of an attribute dimension
     */
    public static int getThemeAttributeDimensionSize(Context context, int attr) {
        TypedArray a = null;
        try {
            a = context.getTheme().obtainStyledAttributes(new int[]{attr});
            return a.getDimensionPixelSize(0, 0);
        } finally {
            if (a != null) {
                a.recycle();
            }
        }
    }

    /**
     * @param context application context
     * @param volume  volume level
     * @brief method to change system volume level
     */
    public static void changeSystemVolume(Context context, int volume) {
        if (context != null) {
            AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
    }

    /**
     * @param context
     * @return int max volume level
     * @brief method to get maximum system volume
     */
    public static int getSystemMaxVolume(Context context) {
        if (context != null) {
            AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            return audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        } else {
            return 0;
        }
    }

    /**
     * @param context
     * @return int current volume level
     * @brief method to get system current level
     */
    public static int getSystemVolume(Context context) {
        AudioManager audioManager = (AudioManager) context
                .getSystemService(Context.AUDIO_SERVICE);
        return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    /**
     * @param date date in String
     * @return date in user readable format
     * @brief method for converting server date format into user readable format
     */
    public static String appDateFormatter(String date) {
        try {
            return new SimpleDateFormat("dd MMM, yyyy").
                    format(new SimpleDateFormat("yyyy-MM-dd").parse(date));
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * @param date date in String
     * @return date in server format
     * @brief method for converting date in server format
     */
    public static String serverDateFormatter(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").
                    format(new SimpleDateFormat("dd MMM, yyyy").parse(date));
        } catch (ParseException e) {
            return date;
        }
    }

    /**
     * @param dateInString string eg 2015-09-15
     * @return Date object
     * @brief convert string to date format
     */
    public static Date stringToDateConverter(String dateInString) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateInString);
        } catch (ParseException e) {
            return new Date();
        }
    }

    /**
     * @param context   application context
     * @param imageView view where the downloaded image will be shown
     * @param url       from where image will be downloaded
     * @brief methods for downloading image to imageview using picasso
     */
//    public static void downloadImage(Context context, ImageView imageView, String url) {
//        if (!TextUtils.isEmpty(url) && !url.equalsIgnoreCase(" ")) {
//            Picasso.with(context).load(url)
//                    .placeholder(R.drawable.img_placeholder)
//                    .into(imageView);
//        }
//    }

    /**
     * @param context      app context
     * @param serviceClass
     * @return boolean true/false
     * @brief method for checking service is running or not
     */
    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        if (context != null) {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param context application context
     * @return String url
     * @brief method for getting google play browser url
     */
    public static String getGooglePlayUrl(Context context) {
        return "https://play.google.com/store/apps/details?id=" + context.getPackageName();
    }


    /**
     * @param context
     * @param subject
     * @param text
     * @param imgUrl
     * @param shareTitle
     */
    public static void socialShare(Context context, String subject, String text,
                                   String imgUrl, String shareTitle) {
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, subject);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, subject + "\n" + imgUrl);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, imgUrl);
        context.startActivity(Intent.createChooser(shareIntent, shareTitle));
    }

    /**
     * @param packageName package name of the app
     * @param context     application context
     * @return
     * @brief method for checking app is installed on device or not
     */
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private static void addShortcut() {

    }

    /**
     * @param context
     * @brief methods for creating app shortcut icon on home screen
     */
    public static void createShortcut(Context context) {
        Intent shortcutIntent = new Intent(context, context.getClass());
        shortcutIntent.setAction(Intent.ACTION_MAIN);
        Intent addIntent = new Intent();
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, context.getString(R.string.app_name));
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
                Intent.ShortcutIconResource.fromContext(context, R.mipmap.ic_launcher));
        addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
        context.sendBroadcast(addIntent);
        savePreference(context, "SHORTCUT_CREATED", 1);
    }


    /**
     * @param context
     * @param bitmap
     * @return
     * @brief methods for getting file uri from bitmap
     */
    public static Uri getUriFromBitmap(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(),
                bitmap, "image_share", null);
        return Uri.parse(path);
    }

    /**
     * @brief methods for getting user country code
     * @param context app context
     * @return country code in String
     */
    public static String getUserCountryCode(Context context) {
        TelephonyManager tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        String countryCode = tm.getSimCountryIso();
        if(!TextUtils.isEmpty(countryCode)) {
            return countryCode;
        } else {
            return "country code not found";
        }
    }

    public static String getUserOperatorName(Context context) {
        TelephonyManager telephonyManager =((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE));
        String operatorName = telephonyManager.getNetworkOperatorName();
        if(!TextUtils.isEmpty(operatorName)) {
            Log.v("logCheck",operatorName);
            return operatorName;
        } else {
            return "n/a";
        }
    }

    /**
     * @brief method for getting device IMEI
     * @param context
     * @return IMEI in String
     */
    public static String getUserIMEI(Context context){
        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();
        if(!TextUtils.isEmpty(imei)) {
            Log.v("logCheck",imei);
            return imei;
        } else {
            return "n/a";
        }
    }

    public static String getUserEmail(Context context) {
        Pattern emailPattern = Patterns.EMAIL_ADDRESS;
        Account[] accounts = AccountManager.get(context).getAccounts();
        StringBuilder accountInfo = new StringBuilder();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches() && account.name.contains("gmail")) {
                accountInfo.append(account.name);
                break;
            }
        }
        if(!TextUtils.isEmpty(accountInfo.toString())) {
            return accountInfo.toString();

        } else {
            return "user email not found";
        }
    }


    /**
     * @brief interface used by showNoInternetDialog() methods
     */
    public interface InternetConnectionListener {

        void onConnectionEstablished(int code);

        void onUserCanceled(int code);
    }


}
