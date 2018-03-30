package batman.alfred;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class ControlLightsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_lights);

    }

    public void turnLightsOn(View v){

        // animation
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        v.startAnimation(anim);
        this.lightsOn();

    }

    public void turnLightsOff(View v){

        // animation
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        v.startAnimation(anim);
        this.lightsOff();
    }


    public void lightsOn(){
        // the url for the server
        String url = MainActivity.serverBase + "lights_on";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);



        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // show toast message with Lights ON
                        Toast toast = Toast.makeText(getApplicationContext(),"Lights: ON", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 100);
                        toast.show();
                    }
                }
                ,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // show toast message with Error
                        Toast toast = Toast.makeText(getApplicationContext(),"Lights: Error, Check Server", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 100);
                        toast.show();
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public void lightsOff(){
        // the url for the server
        String url = MainActivity.serverBase + "lights_off";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // show toast message with Lights OFF
                        Toast toast = Toast.makeText(getApplicationContext(),"Lights: OFF", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 100);
                        toast.show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // show toast message with Error
                        Toast toast = Toast.makeText(getApplicationContext(),"Lights: Error, Check Server", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 100);
                        toast.show();
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}

