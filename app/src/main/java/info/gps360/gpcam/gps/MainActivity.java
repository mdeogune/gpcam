/*
 * Copyright 2017 Anton Tananaev (anton@gps360.org)
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
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;

import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import info.gps360.gpcam.R;
import info.gps360.gpcam.camera_streaming.LiveVideoBroadcasterActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("startLive");
        getApplicationContext().registerReceiver(cameraReceiver, intentfilter);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();
        }


    }


    private BroadcastReceiver cameraReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() == "startLive"){
                Intent i =new Intent(context,LiveVideoBroadcasterActivity.class);
                startActivity(i);
            }
        }
    };


}
