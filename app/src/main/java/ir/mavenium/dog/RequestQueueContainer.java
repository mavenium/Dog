package ir.mavenium.dog;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class RequestQueueContainer {
    private static RequestQueue requestQueue;

    private RequestQueueContainer() {}

    /**
     * @param context
     * @return request queue
     */
    public static RequestQueue getInstance (Context context) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }

        return requestQueue;
    }
}
