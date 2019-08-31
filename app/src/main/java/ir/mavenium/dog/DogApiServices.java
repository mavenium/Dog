package ir.mavenium.dog;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DogApiServices {

    private static final String TAG = "ApiServices";
    private Context context;

    public DogApiServices(Context context) {
        this.context = context;
    }

    public void getRandomImage(final RandomResultCallBack resultCallBack) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "https://dog.ceo/api/breeds/image/random", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if ("success".equals(response.getString("status"))) {
                        Log.e(TAG, "onResponse: " + response.getString("message"), null);
                        resultCallBack.onRandomImageRecived(response.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: " + error.toString(), null);
                resultCallBack.onRandomImageError("Errrror");
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueContainer.getInstance(context).add(request);
    }

    public void getRandomImageByBeed() {

    }

    public interface RandomResultCallBack {
        void onRandomImageRecived(String message);
        void onRandomImageError(String error);
    }

    public interface RandomByBeedResultCallBack {
        void OnRandomImageByBeedRecived();
        void OnRandomImageByBeedError();
    }
}
