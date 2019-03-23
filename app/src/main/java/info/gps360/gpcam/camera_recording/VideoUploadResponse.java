package info.gps360.gpcam.camera_recording;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class VideoUploadResponse extends AsyncTask<Void, Void, Void> {

    List<String> list;
    Context ctx;

    public VideoUploadResponse(Context context, List<String> list) {
        this.list = list;
        ctx = context;
    }

    protected void onPreExecute() {



    }


    @Override
    protected Void doInBackground(Void... params) {


//        Log.e("wifiiii",mContext.getSharedPreferences("sp", MODE_PRIVATE).getString("WiFi-Connected-To", ""));
//        String wifi_connected=mContext.getSharedPreferences("sp", MODE_PRIVATE).getString("WiFi-Connected-To", "");
//        String wifi_connect_to=mContext.getSharedPreferences("sp", MODE_PRIVATE).getString("wifi_connect_to", "TP-LINK_35B6");
//        mContext=null;
//        if (wifi_connected.equals(wifi_connect_to))
        {
            // Your upload Server SCRIPT
            String ip ="139.59.21.107";

            String urlString = "http://"+ip+"/api/video_add/";
            // The file
            Log.e("DO", "in Background");
            //        Calendar c = Calendar.getInstance();
            //        c.add(Calendar.HOUR, -1);
            //        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy") ;
            //        String date = df.format(c.getTime()).replaceAll("/","-");
            String PATH = String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/" + "video");
            // The selected path is the location of the file in your device.

//            Log.d("Files", "Path: " + PATH);
//            File directory = new File(PATH);
//            File[] files = directory.listFiles();
//            Log.d("Files", "Size: " + files.length);
            for (int i = 0; i < list.size(); i++) {
                Log.d("Files", "FileName:" + list.get(i));


                File file = new File( list.get(i));
                Log.e("sub files", String.valueOf(file));
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(urlString);

                //        MultipartEntity reqEntity = new MultipartEntity(
                //                HttpMultipartMode.BROWSER_COMPATIBLE);


                // There are more examples above
                FileBody fb = new FileBody(file);

                MultipartEntityBuilder builder = MultipartEntityBuilder.create()
                        .setMode(HttpMultipartMode.BROWSER_COMPATIBLE)
                        .addTextBody("vehicle", ctx.getSharedPreferences("busno", Context.MODE_PRIVATE).getString("busno","1000"))
                        .addTextBody("imei",ctx.getSharedPreferences("sp", MODE_PRIVATE).getString("IMEI-NO", "Imei-Number"))
                        .addPart("myfile", fb);

                Log.e("ASYNCTASKvehicle",ctx.getSharedPreferences("busno", Context.MODE_PRIVATE).getString("busno","1000"));
                Log.e("ASYNCTASKimei",ctx.getSharedPreferences("sp", MODE_PRIVATE).getString("IMEI-NO", "Imei-Number"));

                //        if (file.getName().endsWith(".xml")) {
                //            fb = new FileBody(file, "text/xml");
                //            reqEntity.addPart("xml_submission_file", fb);
                //            Log.v("Debug", "  file type,   adding file: " + file.getName());
                //        } else if (file.getName().endsWith(".jpg")) {
                //            fb = new FileBody(file, "image/jpeg");
                //            reqEntity.addPart(file.getName(), fb);
                //            Log.v("Debug", "  file type,   adding file: " + file.getName());
                //        } else if (file.getName().endsWith(".3gpp")) {
                //            fb = new FileBody(file, "audio/3gpp");
                //            reqEntity.addPart(file.getName(), fb);
                //            Log.v("Debug", "  file type,   adding file: " + file.getName());
                //        } else if (file.getName().endsWith(".3gp")) {
                //            fb = new FileBody(file, "video/3gpp");
                //            reqEntity.addPart(file.getName(), fb);
                //            Log.v("Debug", "  file type,   adding file: " + file.getName());
                //        } else if (file.getName().endsWith(".mp4")) {
                //            fb = new FileBody(file, "video/mp4");
                //            reqEntity.addPart(file.getName(), fb);
                //            Log.v("Debug", "  file type,   adding file: " + file.getName());
                //        } else {
                //            Log.w("Debug", "unsupported file type, not adding file: "
                //                    + file.getName());
                //        }


                //        FormBodyPart bodyPart = new FormBodyPart("uploadedfile", fb);
                //        reqEntity.addPart(bodyPart);
                httppost.setEntity(builder.build());

                try {
                    HttpResponse response = httpclient.execute(httppost);

                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(
                                    response.getEntity().getContent(), "UTF-8"));
                    String sResponse;
                    StringBuilder mUploadResponse = new StringBuilder();

                    while ((sResponse = reader.readLine()) != null) {
                        mUploadResponse = mUploadResponse.append(sResponse);
                    }
                    Log.d("response add video", String.valueOf(mUploadResponse));
                    JSONObject mUploadResponseObject = new JSONObject(String.valueOf(mUploadResponse));
                    if (mUploadResponseObject.get("status").equals("success")) {
                        Log.e("file deletion", String.valueOf(file));
                        file.delete();
                    }
                    //            mUploadResponseObject.getJSONArray("response");

                    //            try {
                    //                JSONArray jsonArray = mUploadResponseObject
                    //                        .getJSONArray("response");
                    ////                for (int i = 0; i < jsonArray.length(); i++) {
                    ////                    String uploadStatus = jsonArray.getJSONObject(i)
                    ////                            .getJSONObject("send").getString("message");
                    //////                    uploadPhotoID = jsonArray.getJSONObject(i)
                    //////                            .getJSONObject("send").getString("id");
                    ////                    Log.d("Res",uploadStatus);
                    ////
                    ////                }
                    //            } catch (Exception e) {
                    //                Log.d("DEBUG",
                    //                        "The Json response message : " + e.getMessage());
                    //            }

                } catch (ClientProtocolException e) {
                    Log.d("DEBUG",
                            "The server ClientProtocolException response message : "
                                    + e.getMessage());
                    e.printStackTrace();
                } catch (IOException e) {
                    Log.d("DEBUG", "The server  IOException response message : "
                            + e.getMessage());
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        System.gc();
            return null;


    }


    protected void onPostExecute(Void result) {


    }
}

