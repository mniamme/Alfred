package batman.alfred;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    public static String serverBase = "http://192.168.1.3:1995/";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void goToControlLights(View v){
        startActivity(new Intent(MainActivity.this, ControlLightsActivity.class));
    }

    public void goToWakeMe(View v){
        startActivity(new Intent(MainActivity.this, WakeMeActivity.class));
    }
}
