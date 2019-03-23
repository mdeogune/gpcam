
package info.gps360.gpcam.gps;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import info.gps360.gpcam.R;
import info.gps360.gpcam.camera_streaming.LiveVideoBroadcasterActivity;

public class MainActivity extends AppCompatActivity {


    public  static final int PERMISSIONS_MULTIPLE_REQUEST=2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        permissionCheck();
        super.onCreate(savedInstanceState);

        IntentFilter intentfilter = new IntentFilter();
        intentfilter.addAction("startLive");
        getApplicationContext().registerReceiver(cameraReceiver, intentfilter);

        if (savedInstanceState == null && permissionCheck()) {
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


    private boolean permissionCheck() {

        if (ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getApplicationContext(),Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.RECEIVE_SMS,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE,
                        Manifest.permission.SEND_SMS,
                        Manifest.permission.RECORD_AUDIO}, PERMISSIONS_MULTIPLE_REQUEST);

            }
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_MULTIPLE_REQUEST:
                if (grantResults.length > 0) {
                    boolean cameraPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternal = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if(cameraPermission && readExternal)
                    {
                        getFragmentManager().beginTransaction().replace(android.R.id.content, new MainFragment()).commit();

                    } else {

                    }
                }
                break;
        }
    }


}
