package br.com.trasmontano.trasmontanoassociadomobile;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import livroandroid.lib.utils.NotificationUtil;

/**
 * Created by rbarbosa on 20/07/2016.
 */
public class AlarmeBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION = "br.com.trasmontano.trasmontanoassociadomobile.ALARME";

    @Override
    public void onReceive(Context context, Intent intent) {


     /*   final int id = 1;
        final Intent intent2 = new Intent(context, MensagemActivity.class);
        intent.putExtra("msg","Olá Leitor, como vai?");
        final String contentTitle = "Nova mensagem privada";
        final String contentText = "Você possui uma nova mensagem privada";

        br.com.trasmontano.trasmontanoassociadomobile.Util.NotificationUtil.createPrivateNotification(context, intent2, contentTitle, contentText, id);*/





        //funciona
        /*Intent notifIntent = new Intent(context,MainActivity.class);

        NotificationUtil.create(context, 1, notifIntent, R.mipmap.ic_launcher,"Alarme de Medicamentos!!!","Apirina");
        Log.d("TESTE", "BroadCast.");
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(4000);*/




        Bundle b = intent.getExtras();

        Log.d("TESTE", "BroadCast.");
        Intent newIntent = new Intent(context, AlarmeService.class);
        if(b != null)
        {
            newIntent.putExtra("id", b.getInt("id"));
            newIntent.putExtra("paciente", b.getString("paciente"));
            newIntent.putExtra("medicamento", b.getString("medicamento"));
            newIntent.putExtra("mp3", b.getString("mp3"));
        }

        context.startService(newIntent);



    }
}
