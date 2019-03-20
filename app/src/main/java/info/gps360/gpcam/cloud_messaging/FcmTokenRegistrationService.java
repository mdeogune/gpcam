
package info.gps360.gpcam.cloud_messaging;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Service for sending this device's registrationToken to your server to remember it.
 *
 * @author Tom Misawa (riversun.org@gmail.com)
 */
public class FcmTokenRegistrationService extends IntentService {

    public FcmTokenRegistrationService() {
        super("FcmTokenRegistrationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String regToken = FirebaseInstanceId.getInstance().getToken();

        Log.e("Firebase Token=" , regToken);

    }

}
