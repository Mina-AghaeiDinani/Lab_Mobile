package com.example.lab2firebase;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static int NOTIFICATION_ID = 1;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("DEVICE TOKEN",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("MSGRECEIVED", "remote: "+ remoteMessage);

        if (remoteMessage.getNotification() != null) {
            String orderId = remoteMessage.getNotification().getTag(); //The ID of the order is stored in the tag when the notification is sent
            Log.d("NOTIFICATION", "Order: " + orderId);
            generateNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), orderId);

        }


    }

    private void generateNotification(String body, String title, String orderId) {
        Intent intent = new Intent(this, CurrentOrders.class);
        intent.putExtra("order_id", orderId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,intent
        , PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (NOTIFICATION_ID > 1073741824){
            NOTIFICATION_ID = 0;
        }

        notificationManager.notify(NOTIFICATION_ID++,notificationBuilder.build());

    }


}
