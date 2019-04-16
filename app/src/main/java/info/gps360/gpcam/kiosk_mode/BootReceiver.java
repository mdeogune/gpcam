package info.gps360.gpcam.kiosk_mode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import info.gps360.gpcam.gps.MainActivity;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MainActivity.class);
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(myIntent);
    }
}
