package batman.alfred;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
public class AlarmRingingActivity extends AppCompatActivity {
    private MediaPlayer mMediaPlayer;
    private AudioManager mAudioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ringing);

        // set sound volume to 100%
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);


        // turn on the screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON | WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);

        // turn on lights lights if it was checked
        SharedPreferences prefs = getSharedPreferences("Alfred_prefs", MODE_PRIVATE);
        boolean restoredValue = prefs.getBoolean("lights_on", false);
        if(restoredValue == true){
            lightsOn();
        }

        mMediaPlayer = new MediaPlayer();

        // set the volume to maximum
        mMediaPlayer.setVolume(1,1);

        // play the alarm ringtone
        mMediaPlayer = MediaPlayer.create(this, R.raw.alarm_ringtone);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();

    }

    public void stopAlarm(View v){
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // reset the alarm text
        SharedPreferences.Editor editor = getSharedPreferences("Alfred_prefs", MODE_PRIVATE).edit();
        editor.putString("alarm_time", "Alarm isn't set");
        editor.apply();

        // stop the alarm playing ringtone
        mMediaPlayer.stop();


        // if music play option is checked start the music player and send play action to it
        SharedPreferences prefs = getSharedPreferences("Alfred_prefs", MODE_PRIVATE);
        Boolean restoredValue = prefs.getBoolean("music_on", false);

        if(restoredValue == true) {

            // open the music player
            String packageName = "com.sec.android.app.music";
            PackageManager packageManager = getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            startActivity(intent);


            try {
                // wait a while
                Thread.sleep(2000);

                // set the music volume to 60%
                // mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, (int)(mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)* 0.6), 0);


                // decrease the volume of the music
                // I used this instead of the commented one because this makes the user see the volume bar moving on the screen
                // which looks cool and more robotic :)
                for(int i = 0; i < 7; i++) {
                    mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_LOWER, AudioManager.FLAG_SHOW_UI);
                }


                // wait again
                Thread.sleep(2000);

                // play music
                KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY);
                mAudioManager.dispatchMediaKeyEvent(event);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }

    @Override
    public void onBackPressed() {
        // to disable using back key
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
}
