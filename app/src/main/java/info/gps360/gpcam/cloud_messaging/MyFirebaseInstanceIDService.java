package info.gps360.gpcam.cloud_messaging;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceIdService;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        startService(new Intent(this, FcmTokenRegistrationService.class));

    }

}