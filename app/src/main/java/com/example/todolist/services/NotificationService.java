package com.example.todolist.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import androidx.core.app.NotificationCompat;

import com.devmobile.todolistBertaudLeroi.R;

public class NotificationService{

   public static void SendNotification(String Titre, String Description, Context context){
       Thread thread = new Thread() {
           @Override
           public void run() {
               try {
                   String channelId = "alarm_channel";
                   NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

                   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                       NotificationChannel channel = new NotificationChannel(channelId, "Alarm Notifications", NotificationManager.IMPORTANCE_HIGH);
                       notificationManager.createNotificationChannel(channel);
                   }

                   NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channelId)
                           .setSmallIcon(R.drawable.icon_white)
                           .setContentTitle(Titre)
                           .setContentText(Description)
                           .setPriority(NotificationCompat.PRIORITY_HIGH);

                   notificationManager.notify(1, builder.build());
               } catch (Exception ignored) {

               }
           }
       };

       thread.start();


   }
}
