package batman.alfred;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.util.Calendar;

public class WakeMeActivity extends AppCompatActivity {
    private TextView mTextView ;
    private TimePicker timePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wake_me);

        mTextView = (TextView) findViewById(R.id.alarm_text);

        timePicker = (TimePicker) findViewById(R.id.alarm);

        SharedPreferences prefs = getSharedPreferences("Alfred_prefs", MODE_PRIVATE);
        String restoredText = prefs.getString("alarm_time", "Alarm isn't set");
        mTextView.setText(restoredText);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void setAlarm(View v){
        // get the time from time picker and update the text view
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, hour);
        c.set(Calendar.MINUTE, minute);
        c.set(Calendar.SECOND, 0);
        updateTimeText(c);

        // get information from check boxes
        CheckBox b1 = (CheckBox)findViewById(R.id.check_lights_off);
        CheckBox b2 = (CheckBox)findViewById(R.id.check_lights_on);
        CheckBox b3 = (CheckBox)findViewById(R.id.check_play_music);


        // save them in the SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("Alfred_prefs", MODE_PRIVATE).edit();

        if(b1.isChecked()){
            lightsOff();
        }

        if(b2.isChecked()){
            editor.putBoolean("lights_on", true);
        }
        else{
            editor.putBoolean("lights_on", false);
        }

        if(b3.isChecked()){
            editor.putBoolean("music_on", true);
        }
        else{
            editor.putBoolean("music_on", false);
        }

        editor.apply();


        // if the time set is before the current time add one day
        if(c.before(Calendar.getInstance())){
            c.add(Calendar.DATE, 1);
        }

        startAlarm(c);

    }

    private void updateTimeText(Calendar c){

        // update the text under the alarm with the time set
        String timeText = "Alarm is set for: ";
        timeText += DateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());
        mTextView.setText(timeText);

        SharedPreferences.Editor editor = getSharedPreferences("Alfred_prefs", MODE_PRIVATE).edit();
        editor.putString("alarm_time", timeText);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c){

        // set the alarm
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);

        // show toast message with Alarm Set
        Toast toast = Toast.makeText(getApplicationContext(),"Alarm is set", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 100);
        toast.show();
    }

    public void cancelAlarm(View v){
        // cancel the alarm
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);

        // change the text under the alarm to no alarm set
        mTextView.setText("Alarm isn't set");

        SharedPreferences.Editor editor = getSharedPreferences("Alfred_prefs", MODE_PRIVATE).edit();
        editor.putString("alarm_time", "Alarm isn't set");
        editor.apply();

        // show toast message with Alarm Canceled
        Toast toast = Toast.makeText(getApplicationContext(),"Alarm is canceled", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 100);
        toast.show();

    }

    public void lightsOff() {
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
                        Toast toast = Toast.makeText(getApplicationContext(), "Lights: OFF", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 100);
                        toast.show();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // show toast message with Error
                        Toast toast = Toast.makeText(getApplicationContext(), "Lights: Error, Check Server", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER, 0, 100);
                        toast.show();
                    }
                }
        );

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
