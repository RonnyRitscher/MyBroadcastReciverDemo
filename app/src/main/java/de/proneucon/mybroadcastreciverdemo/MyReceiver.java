package de.proneucon.mybroadcastreciverdemo;
/* Receiver -> zb Nachrichten versenden */

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import java.text.DateFormat;
import java.util.Date;

public class MyReceiver extends BroadcastReceiver {

    private static final int ID = 123;
    private final String CHANNEL_ID = "channel_1";
    CharSequence name = "MyChanel";
    int importance = NotificationManager.IMPORTANCE_HIGH;


    @Override
    public void onReceive(Context context, Intent intent) {

        String msg = DateFormat.getDateTimeInstance().format(new Date());

        //NOTIFICATION-MANAEGR
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = null;

        // OREO-PROBLEM: um den NotificationChanel angeben zu können muss (wegen OREO) die Version geprüft werden
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, name, importance);
            //builder.setChannelId(CHANNEL_ID);  //Builder erhällt die Channel_ID
            notificationManager.createNotificationChannel(channel);
        }


        //Receiver kann nicht nur auf eine Nachricht reagieren:
        switch (intent.getAction()) {                        //auf welche Action soll reagiert werden?

            case Intent.ACTION_AIRPLANE_MODE_CHANGED:       //beim Flugzeugmodus
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID); //Context-bezogen
                builder.setSmallIcon(R.mipmap.ic_launcher);  //mitgeben eines Icons
                builder.setContentTitle(context.getString((R.string.app_name)))  //Title der Notification
                        .setContentText("Flugmodus geändert!" + msg) //Message
                        .setWhen(System.currentTimeMillis());

                //OREO-PROBLEM:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    builder.setChannelId(CHANNEL_ID); //Builder erhällt die Channel_ID
                    //notificationManager.createNotificationChannel(CHANNEL_ID);
                }

                /* -> Wir möchten nun, dass wenn man auf die nachricht klickt, diese zu einer Activity geleitet wird:*/
                //INTENT
                Intent myIntent = new Intent(context, MainActivity.class);
                //Intent myIntent = new Intent(context , MainActivityUebersicht.class);

                //PENDING-INTENT
                PendingIntent pendingIntent =
                        PendingIntent.getActivity(context, ID, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent); //übergebe den builder den PadingIntent
                /* nun sollte auch auf klick der Nachricht die Activity starten*/

                //NOTIFICATION erstellen
                Notification notification = builder.build(); // Notification muss gebaut werden

                //SENDEN der NOTIFICATION
                notificationManager.notify(ID, notification);  //und abgeschickt werden ->

                /*  --------INFO-----------
                Wir erstellen eine Datumsinstanz und lassen diesen beim aktivieren des FlugModus ausgeben
                Nun muss auch noch die Manifest angepasst werden
                Hier können Hintergrundprozesse gestartet werden oder andere Aktionen ausgelöst werden

                mögliche erweiterungen:
                - Gruppieren von allen Notifications
                - Öffnen bei klick eine spezielle Activity
                  --------INFO-----------*/
        }

    }
}
