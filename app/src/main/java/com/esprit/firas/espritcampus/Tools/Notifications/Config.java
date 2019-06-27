package com.esprit.firas.espritcampus.Tools.Notifications;

import java.util.ArrayList;

/**
 * Created by Ravi Tamada on 28/09/16.
 * www.androidhive.info
 */

public class Config {

    // global topic to receive app wide push notifications
    public static final String TOPIC_GLOBAL = "global";

    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";

    // id to handle the notification in the notification tray
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;

    public static final String SHARED_PREF = "ah_firebase";


    public static int messages_num = 0;

    public static ArrayList<Integer> list_notif_mess;

    public static int likes_num = 0;
    public static int follow_num = 0;
}
