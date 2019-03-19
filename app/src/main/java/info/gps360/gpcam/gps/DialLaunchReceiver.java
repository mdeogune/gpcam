/*
 * Copyright 2015 Anton Tananaev (anton@gps360.org)
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

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class DialLaunchReceiver extends BroadcastReceiver {

    private static final String LAUNCHER_NUMBER = "8722227"; // TRACCAR

    @Override
    public void onReceive(Context context, Intent intent) {
        String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        if (phoneNumber.equals(LAUNCHER_NUMBER)) {
            setResultData(null);
            Intent appIntent = new Intent(context, MainActivity.class);
            appIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(appIntent);
        }
    }

}
