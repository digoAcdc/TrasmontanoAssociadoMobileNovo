package br.com.trasmontano.trasmontanoassociadomobile;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import livroandroid.lib.utils.NotificationUtil;

/**
 * Created by rbarbosa on 22/07/2016.
 */
public class AlarmeService extends IntentService {

    public AlarmeService() {
        super("AlarmeService");


    }

    @Override
    protected void onHandleIntent(Intent intent) {
/*        Intent notifIntent = new Intent(this,MainActivity.class);

        NotificationUtil.create(this, 1, notifIntent, R.mipmap.ic_launcher,"Alarme de Medicamentos!!!","Apirina");

        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
       // long[] pattern = {0, 100, 1000};
        vibrator.vibrate(3000);*/
        Log.d("TESTE", "service.");
        Toast.makeText(this, "service", Toast.LENGTH_LONG).show();


/*
        Intent notificationIntent = new Intent(this, MainActivity.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this).setContentTitle("IntentServiceRefresh").setContentText("TEste").setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);
        Notification notification = builder.build();

        // Hide the notification after it's selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
