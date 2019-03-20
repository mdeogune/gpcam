package info.gps360.gpcam.utility;


import android.content.Context;
import android.nfc.Tag;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyHelper  {
    String TAG="VolleyHelper";

    Context context;
    String baseIP;
    RequestQueue requestQueue;


/*  Example:----------------
    public void searchUser(final VolleyCallback callback){
        baseIP=context.getResources().getString(R.string.base_ip);
        requestQueue = Volley.newRequestQueue(context);
        String url="http://"+baseIP+"/api/get_already_reg_client/";

        HashMap<String, String> data = new HashMap();

        data.put("key","bd0e7468203f76439a9d4cb3d29a2403cfe49e41e781813e0cdec392cf054dc9");
        data.put("phone_number",mFirebaseUser.getPhoneNumber().replace("+",""));
        JSONObject jsonObject = new JSONObject(data);
        volleyFunction(jsonObject,url,callback);

    }
*/

    public void volleyFunction(JSONObject jsonObject,String url,final VolleyCallback callback){
        JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.POST, url,jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                        callback.onSuccess(response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse (VolleyError error){
                        if (error instanceof NetworkError) {

                        } else if (error instanceof ServerError) {
                        } else if (error instanceof AuthFailureError) {
                        } else if (error instanceof ParseError) {
                        } else if (error instanceof NoConnectionError) {
                        } else if (error instanceof TimeoutError) {
                        }
                        callback.onError(error.getMessage());
                        Log.e("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                return params;
            }
            @Override
            public Priority getPriority() {
                return Priority.IMMEDIATE;
            }
        };
        putRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        requestQueue.add(putRequest);

        return;
    }

}