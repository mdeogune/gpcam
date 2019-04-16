package info.gps360.gpcam.kiosk_mode;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import static com.android.volley.VolleyLog.TAG;

public class PrefUtils {
    private static final String PREF_KIOSK_MODE = "pref_kiosk_mode";
    private static String TAG="Hello";

    public static boolean isKioskModeActive(final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d(TAG, "isKioskModeActive: "+sp.getBoolean(PREF_KIOSK_MODE,false));
        return sp.getBoolean(PREF_KIOSK_MODE, false);
    }

    public static void setKioskModeActive(final boolean active, final Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(PREF_KIOSK_MODE, active).apply();
        Log.d(TAG, "isKioskModeActive: "+active);

    }
}
