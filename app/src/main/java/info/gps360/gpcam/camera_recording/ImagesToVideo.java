package info.gps360.gpcam.camera_recording;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import info.gps360.gpcam.utility.Constants;

public class ImagesToVideo extends Service  {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.HOUR, -1);
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy kk");
        String date = df.format(c.getTime()).replaceAll("/", "-");

        Calendar c2 = Calendar.getInstance();
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String date2 = df2.format(c2.getTime()).replaceAll("/", "-");


        String path = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+ File.separator + Constants.VIDEO_FILE + File.separator + date2 );
        File f = new File(path);
        if(!f.exists())
        {
            f.mkdirs();
        }

        zipFolder(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + Constants.IMAGE_FILE + File.separator + date2 + File.separator + date ,path+ File.separator + date + ".zip");

        return START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void zipFolder(String inputFolderPath, String outZipPath)
    {
        Log.d("Location",inputFolderPath);
        try {
            FileOutputStream fos = new FileOutputStream(outZipPath);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File srcFile = new File(inputFolderPath);
            File[] files = srcFile.listFiles();
            Log.d("Hello", "Zip directory: " + srcFile.getName());
            for (int i = 0; i < files.length; i++) {
                Log.d("Yes", "Adding file: " + files[i].getName());
                byte[] buffer = new byte[4096];
                FileInputStream fis = new FileInputStream(files[i]);
                zos.putNextEntry(new ZipEntry(files[i].getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();

                fis.close();

            }
            zos.flush();
            zos.close();
            fos.flush();
            fos.close();

        } catch (IOException ioe) {
            Log.e("", ioe.getMessage());
        }finally {
            File f = new File(inputFolderPath);
            File a[] =  f.listFiles();
            for(File x : a){
                x.delete();
            }
            if(f.canWrite()){
                Log.e("Write",String.valueOf(f.canWrite()));

            }
            boolean x = f.delete();
            Log.e("Delete",String.valueOf(x));
            this.stopSelf();
            stopService(new Intent(this,ImagesToVideo.class));
        }

        System.gc();

    }

    List<Bitmap> list=new ArrayList<>();
    String filePath;
    int count = 0;



}
