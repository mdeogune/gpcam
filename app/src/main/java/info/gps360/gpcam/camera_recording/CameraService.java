package info.gps360.gpcam.camera_recording;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.androidhiddencamera.HiddenCameraService;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraFocus;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import info.gps360.gpcam.utility.Constants;

public class CameraService extends HiddenCameraService {

    private Handler mHandler;

    public long DEFAULT_SYNC_INTERVAL = 1* 1000;
    public File image_file;
    public String TAG="CameraService";
    public int width = 500;
    public int height = 300;

    public boolean checkFile(String file, boolean subfile){
        String path;
        if (subfile) {
             path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + file + File.separator + dateProvider());
        }else {
             path= String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + file);
        }
        Log.d(TAG, path);
        File mFile = new File(path);
        if (!mFile.exists()) {
            mFile.mkdirs();
            return false;
        }
        return true;
    }

    private String dateProvider(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String date = df.format(c.getTime()).replaceAll("/", "-");
        return date;
    }

    private void deleteExtraFolders(String folder){
        String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + folder);

        File videoPathFile = new File(path);


            while (videoPathFile.listFiles().length>7){
                videoPathFile = new File(path);
                Log.e("PATHFIKESI",String.valueOf(videoPathFile.listFiles().length));
                File folders[] = videoPathFile.listFiles();
                if(folders.length>7){
                    Arrays.sort(folders, new Comparator<File>(){
                        public int compare(File f1, File f2) {
                            return Long.compare(f1.lastModified(),(f2.lastModified()));
                        }
                    });
                File fx[] =  folders[0].listFiles();
                for(File x : fx){
                    if(x.isDirectory()){
                        File y[] = x.listFiles();
                        for(File z : y){
                            z.delete();
                        }
                    }
                    x.delete();
                }
                Log.e("fff",folders[0].getName());
                folders[0].delete();
            }
            Log.e("PATHFIKESI",String.valueOf(videoPathFile.listFiles().length));

        }
    }

    public Bitmap getResizedBitmap (Bitmap bm, int newWidth, int newHeight) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(
                bm, 0, 0, width, height, matrix, false);
        bm.recycle();
        matrix = null;
        System.gc();
        return resizedBitmap;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)  {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {

            if (HiddenCameraUtils.canOverDrawOtherApps(this)) {

                CameraConfig cameraConfig = new CameraConfig()
                        .getBuilder(this)
                        .setCameraFacing(CameraFacing.FRONT_FACING_CAMERA)
                        .setCameraResolution(CameraResolution.MEDIUM_RESOLUTION)
                        .setImageFormat(CameraImageFormat.FORMAT_JPEG)
                        .setCameraFocus(CameraFocus.CONTINUOUS_PICTURE)
                        .build();

                startCamera(cameraConfig);

                mHandler = new Handler();
                mHandler.post(runnableService);
            } else {
                //Open settings to grant permission for "Draw other apps".
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
            }
        } else {
            //TODO Ask your parent activity for providing runtime permission
            Toast.makeText(this, "Camera permission not available", Toast.LENGTH_SHORT).show();
        }
        return START_STICKY;
    }

    @Override
    public void onImageCapture(@NonNull Bitmap bitmap) {
        Log.e(TAG,"Capturing Image");
        checkFile(Constants.IMAGE_FILE,false);
        checkFile(Constants.VIDEO_FILE,false);
        Calendar c = Calendar.getInstance();
        String PATH = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + Constants.IMAGE_FILE+ File.separator + dateProvider());

            try {

                deleteExtraFolders(Constants.IMAGE_FILE);
                deleteExtraFolders(Constants.VIDEO_FILE);

                checkFile(Constants.IMAGE_FILE,true);
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy kk");
                String date2 = df2.format(c.getTime()).replaceAll("/", "-");
                String PATH2 = String.valueOf(PATH + File.separator + date2);
                File file2 = new File(PATH2);
                if(!file2.exists()){
                    boolean x = file2.mkdirs();
                    startService(new Intent(getApplicationContext(),ImagesToVideo.class));
                }


                String time = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(c.getTime()).replaceAll("/", "-").replaceAll(":", "-");
                image_file = new File(PATH2, File.separator + time + ".jpeg");

                Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                Canvas cs = new Canvas(mutableBitmap);
                Paint tPaint = new Paint();
                tPaint.setTextSize(50);
                tPaint.setColor(Color.WHITE);
                tPaint.setStyle(Paint.Style.FILL);
                float height_ = tPaint.measureText("yY");
                cs.drawText(time, 20f, height_+15f, tPaint);

                if (!image_file.exists()) {
                    boolean y = image_file.createNewFile();
               }
                OutputStream os = new BufferedOutputStream(new FileOutputStream(image_file));
                mutableBitmap=(getResizedBitmap(mutableBitmap, width, height));
                mutableBitmap.compress(Bitmap.CompressFormat.JPEG, 80, os);
                os.flush();
                os.close();
            } catch (Exception e) {
                Log.e("Excepton", e.toString());
            }



    }

    @Override
    public void onCameraError(int errorCode) {
        switch (errorCode) {
            case CameraError.ERROR_CAMERA_OPEN_FAILED:
                //Camera open failed. Probably because another application
                //is using the camera
                Log.e("Error","Cannot Open Camera");
                break;
            case CameraError.ERROR_IMAGE_WRITE_FAILED:
                //Image write failed. Please check if you have provided WRITE_EXTERNAL_STORAGE permission
                Log.e("Error","Cannot write");
                break;
            case CameraError.ERROR_CAMERA_PERMISSION_NOT_AVAILABLE:
                //camera permission is not available
                //Ask for the camera permission before initializing it.
                Log.e("Error","Camera permission not available");
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_OVERDRAW_PERMISSION:
                //Display information dialog to the user with steps to grant "Draw over other app"
                //permission for the app.
                HiddenCameraUtils.openDrawOverPermissionSetting(this);
                break;
            case CameraError.ERROR_DOES_NOT_HAVE_FRONT_CAMERA:
                Log.e("Error","Not having  Camera");
                break;
        }
    }

    private Runnable runnableService = new Runnable() {
        @Override
        public void run() {
            syncData();
            // Repeat this runnable code block again every ... min
            mHandler.postDelayed(runnableService, DEFAULT_SYNC_INTERVAL);
        }
    };

    private synchronized void syncData() {

        takePicture();
    }

}
