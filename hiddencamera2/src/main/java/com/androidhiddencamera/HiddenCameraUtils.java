/*
 * Copyright 2017 Keval Patel.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.androidhiddencamera;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;

import android.util.Base64;
import android.util.Log;
import android.widget.Toast;import android.util.Log;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.toolbox.Volley;
import com.androidhiddencamera.CameraConfig;
import com.androidhiddencamera.CameraError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.androidhiddencamera.HiddenCameraService;
import com.androidhiddencamera.HiddenCameraUtils;
import com.androidhiddencamera.config.CameraFacing;
import com.androidhiddencamera.config.CameraFocus;
import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraResolution;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import org.json.JSONObject;
import com.android.volley.VolleyError;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.androidhiddencamera.config.CameraImageFormat;
import com.androidhiddencamera.config.CameraRotation;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

/**
 * Created by Keval on 11-Nov-16.
 * This class holds common camera utils.
 *
 * @author {@link 'https://github.com/kevalpatel2106'}
 */

public final class HiddenCameraUtils {
    RequestQueue requestQueue;
    public static List<HashMap<String, String>> urlList = new ArrayList<>();
    public static String Urlf="http://35.200.136.84/api/update_image/";
    public static String busno = "10000";
    /**
     * Check if the application has "Draw over other app" permission? This permission is available to all
     * the application below Android M (<API 23). But for the API 23 and above user has to enable it mannually
     * if the permission is not available by opening Settings -> Apps -> Gear icon on top-right corner ->
     * Draw Over other apps.
     *
     * @return true if the permission is available.
     * @see 'http://www.androidpolice.com/2015/09/07/android-m-begins-locking-down-floating-apps-requires-users-to-grant-special-permission-to-draw-on-other-apps/'
     */


    private class Myservice extends Service{

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }

    Myservice ms = new Myservice();

    @SuppressLint("NewApi")
    public static boolean canOverDrawOtherApps(Context context) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M || Settings.canDrawOverlays(context);
    }

    /**
     * This will open settings screen to allow the "Draw over other apps" permission to the application.
     *
     * @param context instance of caller.
     */
    public static void openDrawOverPermissionSetting(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;

        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * Get the cache directory.
     *
     * @param context instance of the caller
     * @return cache directory file.
     */
    @NonNull
    static File getCacheDir(Context context) {
        return context.getExternalCacheDir() == null ? context.getCacheDir() : context.getExternalCacheDir();
    }

    /**
     * Check if the device has front camera or not?
     *
     * @param context context
     * @return true if the device has front camera.
     */
    @SuppressWarnings("deprecation")
    public static boolean isFrontCameraAvailable(@NonNull Context context) {
        int numCameras = Camera.getNumberOfCameras();
        return numCameras > 0 && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
    }


    /**
     * Rotate the bitmap by 90 degree.
     *
     * @param bitmap original bitmap
     * @return rotated bitmap
     */
    @WorkerThread
    static Bitmap rotateBitmap(@NonNull Bitmap bitmap, @CameraRotation.SupportedRotation int rotation) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * Save image to the file.
     *
     * @param bitmap     bitmap to store.
     * @param fileToSave file where bitmap should stored
     */
     boolean saveImageFromFile(@NonNull Bitmap bitmap,
                                     @NonNull File fileToSave,
                                     @CameraImageFormat.SupportedImageFormat int imageFormat) {
//        FileOutputStream out = null;
        boolean isSuccess=true ;

//                 Bitmap.CompressFormat compressFormat;
//        switch (imageFormat) {
//            case CameraImageFormat.FORMAT_JPEG:
//                compressFormat = Bitmap.CompressFormat.JPEG;
//                break;
//            case CameraImageFormat.FORMAT_WEBP:
//                compressFormat = Bitmap.CompressFormat.WEBP;
//                break;
//            case CameraImageFormat.FORMAT_PNG:
//            default:
//                compressFormat = Bitmap.CompressFormat.PNG;
//        }

//        try {
//            if (!fileToSave.exists()) //noinspection ResultOfMethodCallIgnored
//                fileToSave.createNewFile();
//
//            out = new FileOutputStream(fileToSave);
//            bitmap.compress(compressFormat, 100, out); // bmp is your Bitmap instance
//            isSuccess = true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            isSuccess = false;
//        } finally {
//            try {
//                if (out != null) out.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        //Decide the image format
        return isSuccess;
    }



    public String encodeTobase64(Bitmap image)
    {
        String byteImage = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 80, baos);
        byte[] b = baos.toByteArray();
        try
        {
            System.gc();
            byteImage = Base64.encodeToString(b, Base64.DEFAULT);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        catch (OutOfMemoryError e)
        {
            baos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b = baos.toByteArray();
            byteImage = Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("Bitmap", "Out of memory error catched");
        }
        return byteImage;
    }


    public void volleyRequest() {

         Log.e("Volley mehtod","volley");
        try {
            if (urlList.size() >= 1) {

                Log.e("urllist", String.valueOf(urlList));

                Log.e("size at request update", urlList.get(0) + "");


                // Tag used to cancel the request
                //////
                JSONObject urlf = new JSONObject(urlList.get(0));


                Log.e("url", urlf.toString());
                String tag_string_req = "string_req";
                JsonObjectRequest putRequest1 = new JsonObjectRequest(Request.Method.POST, Urlf, urlf,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // response

                                Log.d("Response", response.toString());



                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                if (error instanceof NetworkError) {
                                } else if (error instanceof ServerError) {
                                } else if (error instanceof AuthFailureError) {
                                } else if (error instanceof ParseError) {
                                } else if (error instanceof NoConnectionError) {
                                } else if (error instanceof TimeoutError) {


                                }
                                Log.d("Error.Response", error.toString());

                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        return params;
                    }


                };




                putRequest1.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 50000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 50000;
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });

//                putRequest1.setRetryPolicy(new DefaultRetryPolicy(
//                        20000,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

//

                requestQueue.add(putRequest1);

            } else {
                Log.e("ERror", "URL list  null");


            }

        } catch (Exception e) {
            Log.e("volley ereor", String.valueOf(e));
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
        return resizedBitmap;
    }
}
