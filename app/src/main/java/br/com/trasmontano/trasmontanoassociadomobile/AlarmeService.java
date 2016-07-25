package br.com.trasmontano.trasmontanoassociadomobile;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.drm.DrmStore;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
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
        super(AlarmeService.class.getName());


    }

    @Override
    protected void onHandleIntent(Intent intent) {
/*        Intent notifIntent = new Intent(this,MainActivity.class);

        NotificationUtil.create(this, 1, notifIntent, R.mipmap.ic_launcher,"Alarme de Medicamentos!!!","Apirina");

        Vibrator vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
       // long[] pattern = {0, 100, 1000};
        vibrator.vibrate(3000);*/
        Log.d("TESTE", "service.");
       // Toast.makeText(this, "service", Toast.LENGTH_LONG).show();


        Bundle b = intent.getExtras();

        int id = 0;
        String paciente = "";
        String medicamento = "";


        if(b != null)
        {
            id = b.getInt("id");
            paciente = b.getString("paciente");
            medicamento = b.getString("medicamento");
        }


       //funcionando
       /* Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(4000);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(this).setContentTitle(paciente).setContentText(medicamento).setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent);

        Notification notification = builder.build();

        // Hide the notification after it's selected
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);*/


        gerarNotificacao(paciente, medicamento, id);


    }
    public void gerarNotificacao(String paciente, String medicacao, int id){

        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent i = new Intent(this, MensagemActivity.class);
        i.putExtra("id", id);
        i.putExtra("paciente", paciente);
        i.putExtra("medicamento", medicacao);

        PendingIntent p = PendingIntent.getActivity(this, id, i, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setTicker("Ticker Texto");
        builder.setContentTitle(paciente);
        builder.setContentText(medicacao);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentIntent(p);

        Notification n = builder.build();

        n.vibrate = new long[]{150, 300, 150, 600};
        n.flags = Notification.FLAG_NO_CLEAR;
        nm.notify(id, n);

        try{
            Uri som = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone toque = RingtoneManager.getRingtone(this, som);
            toque.play();
        }
        catch(Exception e){}
    }

   /* @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }*/
}
