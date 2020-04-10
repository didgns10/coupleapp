package com.example.coupleapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.coupleapp.Activity.Calender.CalenderDiaryActivity;
import com.example.coupleapp.Activity.Calender.DiaryDetailActivity;
import com.example.coupleapp.Activity.ChatActivity;
import com.example.coupleapp.Activity.VideoChatActivity;
import com.google.firebase.messaging.RemoteMessage;


public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    private static final String TAG = "MyFirebase";

    private String type;
    private String date,date1;

    private String diary_title,diary_idx,diary_content,diary_date,diary_time,diary_focus,name;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {  //data payload로 보내면 실행

        //로그인 저장 정보
        SharedPreferences sf = getSharedPreferences("CHAT_ON",MODE_PRIVATE);
        boolean chat = sf.getBoolean("chat_on",false);

        SharedPreferences sf1 = getSharedPreferences("VIDEO_ON",MODE_PRIVATE);
        boolean video = sf1.getBoolean("video_on",false);


         type = remoteMessage.getData().get("type");

         if(type.equals("alarm")){
             //캘린더 일정부분
             String title = remoteMessage.getData().get("title");
             String body = remoteMessage.getData().get("body");
              date = remoteMessage.getData().get("date");
             sendNotification2(title, body);

             Log.d("messageService", "Broadcasting message");
             Intent intent = new Intent("broad");
             intent.putExtra("shot_day", date);
             LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
         }
        if(type.equals("diary")){
            //캘린더 일정부분
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            date = remoteMessage.getData().get("date");
            sendNotification2(title, body);

            Log.d("messageService", "Broadcasting message");
            Intent intent = new Intent("broad");
            intent.putExtra("shot_day", date);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }
        if(type.equals("diary_comment")){
            //캘린더 일정부분
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
             diary_title = remoteMessage.getData().get("diary_title");
             diary_idx = remoteMessage.getData().get("diary_idx");
             name = remoteMessage.getData().get("name");
             diary_content = remoteMessage.getData().get("diary_content");
             diary_date = remoteMessage.getData().get("diary_date");
             diary_time = remoteMessage.getData().get("diary_time");
             diary_focus = remoteMessage.getData().get("diary_focus");
            sendNotification3(title, body);

        }
        if(type.equals("reject")){

            //전화거절부분
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");
            // sendNotification1(title, body);

            Log.d("messageService", "Broadcasting message");
            Intent intent = new Intent("custom-event-name");
            intent.putExtra("start", "2");
            intent.putExtra("reject", "1");
            intent.putExtra("end", "2");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }else if(type.equals("end")){
            Log.d("messageService", "Broadcasting message");
            Intent intent = new Intent("custom-event-name");
            intent.putExtra("start", "2");
            intent.putExtra("end", "1");
            intent.putExtra("reject", "2");
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        }

        if(chat){

        }else{

            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());

                if (type.equals("message")) {
                    String title = remoteMessage.getData().get("title");
                    String body = remoteMessage.getData().get("body");
                    sendNotification(title, body);
                }
            }
        }
        if(video){

        }else{

            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());


                if (type.equals("video")) {
                    Intent intent = new Intent("custom-event-name");
                    intent.putExtra("start", "1");
                    intent.putExtra("end", "2");
                    intent.putExtra("reject", "2");
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

                    String title = remoteMessage.getData().get("title");
                    String body = remoteMessage.getData().get("body");
                    sendNotification1(title, body);
                }
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());


        }
    }

    private void sendNotification(String title, String body) {
        Intent intent = new Intent(this, ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        String channelId = "default_channel_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = "default_channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendNotification1(String title, String body) {
        Intent intent = new Intent(this, VideoChatActivity.class);
        intent.putExtra("start","2");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        String channelId = "default_channel_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setSound(defaultSoundUri)
                        .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                        .setTimeoutAfter(10000)
                        .setFullScreenIntent(pendingIntent, true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = "default_channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    private void sendNotification2(String title, String body) {
        Intent intent = new Intent(this, CalenderDiaryActivity.class);
        intent.putExtra("start",date);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        String channelId = "default_channel_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = "default_channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
    private void sendNotification3(String title, String body) {
        Intent intent = new Intent(this, DiaryDetailActivity.class);
        intent.putExtra("title",diary_title);
        intent.putExtra("idx",diary_idx);
        intent.putExtra("content",diary_content);
        intent.putExtra("date",diary_date);
        intent.putExtra("time",diary_time);
        intent.putExtra("focus",diary_focus);
        intent.putExtra("name",name);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT
                        | PendingIntent.FLAG_ONE_SHOT);

        String channelId = "default_channel_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelName = "default_channel_id";
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
