package Helper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.phibasis.www.phiotclient.LoginActivity;
import com.phibasis.www.phiotclient.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public final class ApiHelper {

    public static void Call(final Context context,String endpoint,String data,final VolleyCallback callback)
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = ProjectConfig.Base_Url+endpoint+data;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        callback.onSuccessResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(error.networkResponse.statusCode==400)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                        Iterator<String> keys = jsonObject.keys();
                        while (keys.hasNext())
                        {
                            String key = keys.next();
                            JSONArray returnArr = jsonObject.getJSONArray(key);
                            for(int i=0;i<returnArr.length();i++)
                            {
                                String message = returnArr.getString(i);
                                Toast.makeText(context,message,Toast.LENGTH_SHORT).show();

                            }
                        }

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(context,"Something went wrong!",Toast.LENGTH_SHORT).show();
                        ProjectConfig.StaticLog(new String(error.networkResponse.data));
                    }

                }
                else if(error.networkResponse.statusCode==403)
                {
                    try
                    {
                        JSONObject jsonObject = new JSONObject(new String(error.networkResponse.data));
                        Toast.makeText(context,jsonObject.getString("statusMessage"),Toast.LENGTH_SHORT).show();

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(context,"Something went wrong!",Toast.LENGTH_SHORT).show();
                        ProjectConfig.StaticLog(new String(error.networkResponse.data));
                    }

                }
                else if(error.networkResponse.statusCode==401)
                {
                    try
                    {
                        ProjectConfig.StaticToast(context,"You are unauthorized to make this request,Please log in agian.");
                        Intent i = new Intent(context,LoginActivity.class);
                        context.startActivity(i);

                    }
                    catch (Exception e)
                    {
                        Toast.makeText(context,"Something went wrong!",Toast.LENGTH_SHORT).show();
                        ProjectConfig.StaticLog(new String(error.networkResponse.data));
                    }
                }
                else
                {
                    try
                    {
                        Toast.makeText(context,"Something went wrong!",Toast.LENGTH_SHORT).show();
                        ProjectConfig.StaticLog(new String(error.networkResponse.data));
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(context,"Something went wrong!",Toast.LENGTH_SHORT).show();
                        ProjectConfig.StaticLog(new String(error.networkResponse.data));
                    }

                }

            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                SharedPreferences prefs = context.getSharedPreferences(ProjectConfig.SharedPreferenceName, MODE_PRIVATE);
                String token = prefs.getString("token", "");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+token);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
