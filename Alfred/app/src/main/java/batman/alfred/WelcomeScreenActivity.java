package batman.alfred;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class WelcomeScreenActivity extends AppCompatActivity {
    private int delayTime = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        Thread thread = new Thread(){
            @Override
            public void run(){

                try {
                    sleep(delayTime);
                    startActivity(new Intent(WelcomeScreenActivity.this, MainActivity.class));
                    finish();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }
}