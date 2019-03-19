package info.gps360.gpcam.utility;

import org.json.JSONObject;

public interface VolleyCallback {

    void onSuccess(JSONObject result);

    void onError(String s);
}