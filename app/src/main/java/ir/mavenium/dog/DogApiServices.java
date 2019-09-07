package ir.mavenium.dog;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
                        Log.e(TAG, "getRandomImage onResponse: " + response.getString("message"), null);
                        resultCallBack.onRandomImageRecived(response.getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "getRandomImage onResponse: Error in fetch result!!", null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getRandomImage onErrorResponse: " + error.toString(), null);
                resultCallBack.onRandomImageError(Resources.getSystem().getString(R.string.dog_api_services_error_listener));
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueContainer.getInstance(context).add(request);
    }

    public void getRandomImageByBeed() {

    }

    public void getListAllBreeds(final ListAllBreedsCallBack resultCallBack) {
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, " https://dog.ceo/api/breeds/list/all", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if ("success".equals(response.getString("status"))) {
                        JSONObject message = response.getJSONObject("message");
                        Iterator keys = message.keys();
                        List<String> allBreedsList = new ArrayList();
                        while (keys.hasNext()) {
                            String breeds = (String) keys.next();
                            allBreedsList.add(breeds);
                        }
                        resultCallBack.onListAllBreedsRecived(allBreedsList);
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "getListAllBreeds onResponse: Error in fetch result!!", null);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "getListAllBreeds onErrorResponse: " + error.toString(), null);
                resultCallBack.onListAllBreedsError(Resources.getSystem().getString(R.string.dog_api_services_error_listener));
            }
        });

        request.setRetryPolicy(new DefaultRetryPolicy(3000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueueContainer.getInstance(context).add(request);
    }

    public interface RandomResultCallBack {
        void onRandomImageRecived(String message);
        void onRandomImageError(String error);
    }

    public interface RandomByBreedResultCallBack {
        void OnRandomImageByBreedRecived();
        void OnRandomImageByBreedError();
    }

    public interface ListAllBreedsCallBack {
        void onListAllBreedsRecived(List<String> Breeds);
        void onListAllBreedsError(String Error);
    }
}
