package com.esprit.firas.espritcampus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.esprit.firas.espritcampus.Messages.MainConversationsActivity;
import com.esprit.firas.espritcampus.Tools.Events.MainSingleEventActivity;
import com.esprit.firas.espritcampus.Tools.Notifications.Config;
import com.esprit.firas.espritcampus.Tools.Notifications.NotificationUtils;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.esprit.firas.espritcampus.Tools.Notifications.Config.list_notif_mess;
import static com.esprit.firas.espritcampus.Tools.Notifications.Config.messages_num;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";

    private NotificationUtils notificationUtils;

    public static String BROADCAST_ACTION = "com.esprit.firas.espritcampus.Messages.MainMessagesActivity";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // TODO: Handle FCM messages here.
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated.

        SharedPreferences pref = getSharedPreferences("MyPREFERENCES", MODE_PRIVATE);
        String token = pref.getString("token", null);

        if (token != null)
        {

            Log.d("Response token", token + " ");

            String host = getString(R.string.aphost);

            Map<String, String> data = remoteMessage.getData();

            String type = data.get("type");

            if (type.equals("message")) {


                int sender = Integer.valueOf(data.get("sender"));

                Log.d("Response1", list_notif_mess + " ");

                if (!list_notif_mess.contains(sender)) {
                    list_notif_mess.add(sender);
                    messages_num++;
                    Log.d("Response2", list_notif_mess + " " + messages_num);

                }


                String title = data.get("name");

                int mess_id = Integer.valueOf(data.get("messid"));
                String message = data.get("mass");

                String imageUrl = host + "/" + data.get("image");
                String timestamp = data.get("timestamp");
                int convo_id = Integer.valueOf(data.get("convo_id"));


                Log.d(TAG, "From: " + title + " " + message + " " + imageUrl + " " + timestamp); //remoteMessage.getFrom());

                //   Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotificationMessage(title, message, imageUrl, timestamp, mess_id, sender,convo_id);

                Intent intent = new Intent(getApplicationContext(), MainConversationsActivity.class);
                intent.setAction(Long.toString(System.currentTimeMillis()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        intent, PendingIntent.FLAG_CANCEL_CURRENT);

                if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Notification notification = new NotificationCompat.Builder(this.getApplicationContext(), "notify_001")
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSmallIcon(R.drawable.message_icon)
                            .setStyle(new NotificationCompat.BigTextStyle())
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build();

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("notify_001",
                                "Channel human readable title",
                                NotificationManager.IMPORTANCE_DEFAULT);
                        if (notificationManager != null) {
                            notificationManager.createNotificationChannel(channel);
                        }
                    }
                    notificationManager.notify(Config.NOTIFICATION_ID, notification);
                }


                //   messages_num++;

            }


            if (type.equals("other")) {
                String title = data.get("name");

                int post_id = Integer.valueOf(data.get("post_id"));
//            int sender = Integer.valueOf(data.get("user_id"));

                String message = data.get("mass");

                String imageUrl = host + "/" + data.get("image");
                String timestamp = data.get("timestamp");


                Log.d(TAG, "From: " + title + " " + message + " " + imageUrl + " " + timestamp); //remoteMessage.getFrom());

                //   Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotificationOther(title, message, imageUrl, timestamp, post_id);

                if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Notification notification = new NotificationCompat.Builder(this.getApplicationContext(), "notify_001")
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSmallIcon(R.drawable.message_icon)
                            .setStyle(new NotificationCompat.BigTextStyle())
                            .build();

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("notify_001",
                                "Channel human readable title",
                                NotificationManager.IMPORTANCE_DEFAULT);
                        if (notificationManager != null) {
                            notificationManager.createNotificationChannel(channel);
                        }
                    }

                    notificationManager.notify(Config.NOTIFICATION_ID, notification);
                }
            }


            if (type.equals("event")) {
                String title = data.get("name");

                int post_id = Integer.valueOf(data.get("post_id"));
//            int sender = Integer.valueOf(data.get("user_id"));

                String message = data.get("mass");

                String imageUrl = host + "/" + data.get("image");
                String timestamp = data.get("timestamp");


                Log.d(TAG, "From: " + title + " " + message + " " + imageUrl + " " + timestamp); //remoteMessage.getFrom());

                //   Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotificationOther(title, message, imageUrl, timestamp, post_id);

                Intent intent = new Intent(getApplicationContext(), MainSingleEventActivity.class);
                intent.putExtra("id",post_id);
                intent.setAction(Long.toString(System.currentTimeMillis()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                        intent, PendingIntent.FLAG_CANCEL_CURRENT);


                if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Notification notification = new NotificationCompat.Builder(this.getApplicationContext(), "notify_001")
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSmallIcon(R.drawable.message_icon)
                            .setStyle(new NotificationCompat.BigTextStyle())
                            .setContentIntent(pendingIntent)
                            .setAutoCancel(true)
                            .build();

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("notify_001",
                                "Channel human readable title",
                                NotificationManager.IMPORTANCE_DEFAULT);
                        if (notificationManager != null) {
                            notificationManager.createNotificationChannel(channel);
                        }
                    }

                    notificationManager.notify(Config.NOTIFICATION_ID, notification);
                }
            }


            if (type.equals("follow")) {
                String title = data.get("name");

                int user_id = Integer.valueOf(data.get("user_id"));
                String message = data.get("mass");

                String imageUrl = host + "/" + data.get("image");
                String timestamp = data.get("timestamp");


                Log.d(TAG, "From: " + title + " " + message + " " + imageUrl + " " + timestamp); //remoteMessage.getFrom());

                //   Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
                handleNotificationFollow(title, message, imageUrl, timestamp, user_id);

                if (NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                    Notification notification = new NotificationCompat.Builder(this.getApplicationContext(), "notify_001")
                            .setContentTitle(title)
                            .setContentText(message)
                            .setSmallIcon(R.drawable.message_icon)
                            .setStyle(new NotificationCompat.BigTextStyle())
                            .build();

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        NotificationChannel channel = new NotificationChannel("notify_001",
                                "Channel human readable title",
                                NotificationManager.IMPORTANCE_DEFAULT);
                        if (notificationManager != null) {
                            notificationManager.createNotificationChannel(channel);
                        }
                    }

                    notificationManager.notify(Config.NOTIFICATION_ID, notification);
                }
            }
        }

    }





    private void handleNotificationMessage(String title, String message, String imageUrl, String timestamp,
                                           int mess_id, int sender,int convo_id) {
        // app is in foreground, broadcast the push message
        Intent pushNotification = new Intent("update-message");
        pushNotification.putExtra("title", title);
        pushNotification.putExtra("message", message);
        pushNotification.putExtra("image", imageUrl);
        pushNotification.putExtra("timestamp", timestamp);
        pushNotification.putExtra("mess_id", mess_id);
        pushNotification.putExtra("sender", sender);
        pushNotification.putExtra("convo_id", convo_id);

        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


    }


    private void handleNotificationOther(String title, String message, String imageUrl, String timestamp,
                                         int post_id) {
        // app is in foreground, broadcast the push message
        Intent pushNotification = new Intent("other-message");
        pushNotification.putExtra("title", title);
        pushNotification.putExtra("message", message);
        pushNotification.putExtra("image", imageUrl);
        pushNotification.putExtra("time", timestamp);
        pushNotification.putExtra("post_id", post_id);

        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


    }



    private void handleNotificationFollow(String title, String message, String imageUrl, String timestamp,
                                           int user_id) {
        // app is in foreground, broadcast the push message
        Intent pushNotification = new Intent("follow-message");
        pushNotification.putExtra("title", title);
        pushNotification.putExtra("message", message);
        pushNotification.putExtra("image", imageUrl);
        pushNotification.putExtra("time", timestamp);
        pushNotification.putExtra("user_id", user_id);

        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
    }



    private void handleDataMessage(String title,String message,String imageUrl,String timestamp) {


        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);


        } else {
            // app is in background, show the notification in notification tray
            Intent resultIntent = new Intent(getApplicationContext(), MainActivity.class);
            resultIntent.putExtra("message", message);

            // check for image attachment
            if (TextUtils.isEmpty(imageUrl)) {
                showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
            } else {
                // image is present, show notification with image
                showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
            }
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        Toast.makeText(getApplicationContext(),message+" big",Toast.LENGTH_LONG).show();
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {

        Toast.makeText(getApplicationContext(),message+" small",Toast.LENGTH_LONG).show();
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }



}