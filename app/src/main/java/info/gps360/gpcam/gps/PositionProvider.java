
package info.gps360.gpcam.gps;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.BatteryManager;
import android.preference.PreferenceManager;
import android.util.Log;

public abstract class PositionProvider {

    private static final String TAG = PositionProvider.class.getSimpleName();

    protected static final int MINIMUM_INTERVAL = 1000;

    public interface PositionListener {
        void onPositionUpdate(Position position);
        void onPositionError(Throwable error);
    }

    protected final PositionListener listener;

    protected final Context context;
    protected SharedPreferences preferences;

    protected String deviceId;
    protected long interval;
    protected double distance;
    protected double angle;

    protected Location lastLocation;

    public PositionProvider(Context context, PositionListener listener) {
        this.context = context;
        this.listener = listener;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        deviceId = preferences.getString(MainFragment.KEY_DEVICE, "undefined");
        interval = Long.parseLong(preferences.getString(MainFragment.KEY_INTERVAL, "600")) * 1000;
        distance = Integer.parseInt(preferences.getString(MainFragment.KEY_DISTANCE, "0"));
        angle = Integer.parseInt(preferences.getString(MainFragment.KEY_ANGLE, "0"));
    }

    public abstract void startUpdates();

    public abstract void stopUpdates();

    public abstract void requestSingleLocation();

    protected void processLocation(Location location) {
        if (location != null && (lastLocation == null
                || location.getTime() - lastLocation.getTime() >= interval
                || distance > 0 && location.distanceTo(lastLocation) >= distance
                || angle > 0 && Math.abs(location.getBearing() - lastLocation.getBearing()) >= angle)) {
            Log.i(TAG, "location new");
            lastLocation = location;
            listener.onPositionUpdate(new Position(deviceId, location, getBatteryLevel(context)));
        } else {
            Log.i(TAG, location != null ? "location ignored" : "location nil");
        }
    }

    protected static double getBatteryLevel(Context context) {
        Intent batteryIntent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        if (batteryIntent != null) {
            int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, 1);
            return (level * 100.0) / scale;
        }
        return 0;
    }

}
