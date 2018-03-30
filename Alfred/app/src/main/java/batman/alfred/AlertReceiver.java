package batman.alfred;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        // start alarm ringing activity
        Intent i = new Intent();
        i.setClassName("batman.alfred", "batman.alfred.AlarmRingingActivity");
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);


    }


}
