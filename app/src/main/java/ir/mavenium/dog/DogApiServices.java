package ir.mavenium.dog;

import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;

public class DogApiServices {

    private static final String TAG = "ApiServices";

    public void getRandomImage(final ResultCallBack resultCallBack) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "https://dog.ceo/api/breeds/image/random", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if (response.getString(1) == "success") {
                        resultCallBack.onRandomImageRecived(response.getString(0));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: Response Error!", null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                resultCallBack.onRandomImageError();
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(2000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    public void getRandomImageByBeed() {

    }

    public interface ResultCallBack {
        void onRandomImageRecived(String message);
        void onRandomImageError();
        void OnRandomImageByBeedRecived();
        void OnRandomImageByBeedError();
    }
}
