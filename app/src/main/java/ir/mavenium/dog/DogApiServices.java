package ir.mavenium.dog;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

public class DogApiServices {

    public void getRandomImage(final ResultCallBack resultCallBack) {
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, "https://dog.ceo/api/breeds/image/random", null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                resultCallBack.onRandomImageRecived();
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
        void onRandomImageRecived();
        void onRandomImageError();
        void OnRandomImageByBeedRecived();
        void OnRandomImageByBeedError();
    }
}
