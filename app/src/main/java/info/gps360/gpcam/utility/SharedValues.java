package info.gps360.gpcam.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedValues {

    private static final String SHARED_PREFS = "gps_app_shared_values";

    public static String getValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_MULTI_PROCESS);
        String value = sharedPreferences.getString(key, null);
        return value;
    }



    public static String getSafeValue(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_MULTI_PROCESS);
        String value = sharedPreferences.getString(key, "0");
        return value;
    }
    public static void saveValue(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }



    public static void saveValue_async(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }




}