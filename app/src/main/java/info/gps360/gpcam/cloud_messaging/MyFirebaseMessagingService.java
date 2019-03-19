package info.gps360.gpcam.cloud_messaging;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Environment;
import android.os.Process;
import android.util.Log;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import info.gps360.gpcam.utility.Utility;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    Utility util = new Utility();
    String videoDate,videoInterval;


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("message", String.valueOf(remoteMessage.getData()));

        if (remoteMessage.getData().containsKey("restart") && remoteMessage.getData().get("restart").equals("True")) {
            util.reBoot();
        }
        if (remoteMessage.getData().containsKey("shutdown") && remoteMessage.getData().get("shutdown").equals("True")) {
            util.shutDown();
        }
        if (remoteMessage.getData().containsKey("restart_app") && remoteMessage.getData().get("restart_app").equals("True")) {
            Intent i = getBaseContext().getPackageManager()
                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }
        if (remoteMessage.getData().containsKey("kill") &&  remoteMessage.getData().get("kill").equals("True")) {
            Process.killProcess(Process.myPid());

        }
        if (remoteMessage.getData().containsKey("Camera_ON") && remoteMessage.getData().get("Camera_ON").equals("True")) {


            Intent i = new Intent();
            i.setAction("startLive");
            sendBroadcast(i);
        } else if(remoteMessage.getData().containsKey("Camera_OFF") && remoteMessage.getData().get("Camera_OFF").equals("True")){
            Intent i = new Intent();
            i.setAction("stopLive");
            sendBroadcast(i);
// l.triggerStopRecording();
//            Intent i = new Intent();
//            i.setAction("stop");
//            sendBroadcast(i);
        }
        if (remoteMessage.getData().containsKey("obd") && remoteMessage.getData().get("obd").equals("True")) {
            ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);

            List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(Integer.MAX_VALUE);
            boolean contains = false;
            for (int i = 0; i < rs.size(); i++) {
                ActivityManager.RunningServiceInfo rsi = rs.get(i);
                if (rsi.process.contains(".myobdprocess")) {

                    contains = contains | true;
                }
            }
//                                    String command;
//                                    command = "rm -rf" + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) ;
//
//                                    try {
//                                        java.lang.Process proc = Runtime.getRuntime().exec(new String[] { "su", "-c", command });
//                                        proc.waitFor();
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }

            if (!contains) {
//                startService(new Intent(getApplicationContext(), ObdReaderService.class));
            }

        } else {
            ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Activity.ACTIVITY_SERVICE);

            List<ActivityManager.RunningServiceInfo> rs = am.getRunningServices(Integer.MAX_VALUE);
            boolean contains = false;
            for (int i = 0; i < rs.size(); i++) {
                ActivityManager.RunningServiceInfo rsi = rs.get(i);
                if (rsi.process.contains(".myobdprocess")) {
                    contains = contains | true;

                }
            }
            if (contains) {
//                stopService(new Intent(getApplicationContext(), ObdReaderService.class));
            }

        }


        if (remoteMessage.getData().containsKey("force_update") && remoteMessage.getData().get("force_update").equals("True")) {
//            startService(new Intent(getApplicationContext(), UpdateApk.class));
        } else {
//                                    Log.e("force_update is ", "false");
        }
        if (remoteMessage.getData().containsKey("date_video") && !remoteMessage.getData().get("date_video").toString().isEmpty()){

            videoDate = remoteMessage.getData().get("date_video").toString();

        }if (remoteMessage.getData().containsKey("interval_video") && !remoteMessage.getData().get("interval_video").toString().isEmpty()){
            // interval calculation

            String PATH = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + "video" + File.separator + videoDate);

            videoInterval  = remoteMessage.getData().get("interval_video").toString();
            String s[] = videoInterval.split("-");
            int a = Integer.parseInt(s[0]);
            int b = Integer.parseInt(s[1]);
            Log.e(String.valueOf(a),String.valueOf(b));
            List<String> send_list = new ArrayList<>();
            for(int i=a; i<=b-1;i++){
                send_list.add(PATH + File.separator + videoDate +" "+String.format("%02d",i)+".zip");
                Log.e("PATHOF",PATH + File.separator + videoDate +" "+String.valueOf(i)+".zip");

            }
//            new VideoUploadResponse(getApplicationContext(),send_list).execute();


        }


    }
}
