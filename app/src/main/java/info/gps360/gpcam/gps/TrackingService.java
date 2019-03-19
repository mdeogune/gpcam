/*
 * Copyright 2012 - 2017 Anton Tananaev (anton@gps360.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.gps360.gpcam.gps;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import info.gps360.gpcam.BuildConfig;
import info.gps360.gpcam.R;


import android.util.Log;




public class TrackingService extends Service {

    private static final String TAG = TrackingService.class.getSimpleName();
    private static final int NOTIFICATION_ID = 1;

    private TrackingController trackingController;

    private static Notification createNotification(Context context) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, MainApplication.PRIMARY_CHANNEL)
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(Notification.CATEGORY_SERVICE);
        Intent intent;
        if (!BuildConfig.HIDDEN_APP) {
            intent = new Intent(context, MainActivity.class);
            builder
                .setContentTitle(context.getString(R.string.settings_status_on_summary))
                .setTicker(context.getString(R.string.settings_status_on_summary))
                .setColor(ContextCompat.getColor(context, R.color.primary_dark));
        } else {
            intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
        }
        builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
        return builder.build();
    }

    public static class HideNotificationService extends Service {
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            startForeground(NOTIFICATION_ID, createNotification(this));
            stopForeground(true);
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            stopSelfResult(startId);
            return START_NOT_STICKY;
        }
    }

    @Override
    public void onCreate() {
        Log.i(TAG, "service create");
        StatusActivity.addMessage(getString(R.string.status_service_create));

        startForeground(NOTIFICATION_ID, createNotification(this));

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            trackingController = new TrackingController(this);
            trackingController.start();
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this, new Intent(this, HideNotificationService.class));
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            AutostartReceiver.completeWakefulIntent(intent);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "service destroy");
        StatusActivity.addMessage(getString(R.string.status_service_destroy));

        stopForeground(true);

        if (trackingController != null) {
            trackingController.stop();
        }
    }

}
