package com.inducesmile.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.inducesmile.androidecommerceshop.R;
import com.inducesmile.androidecommerceshop.ShopActivity;
import com.inducesmile.androidecommerceshop.utils.CustomApplication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class CustomsFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = CustomsFirebaseMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

        List<NotifyObject> allNotifications = new ArrayList<>();

        String storedNotification = ((CustomApplication)getApplication()).getShared().getStoredNotifications();
        Gson gson = ((CustomApplication)getApplication()).getGsonObject();

        String notificationTitle = "";
        String notificationMessage = "";

        if(remoteMessage.getData().size() > 0){
            Map<String, String> dataLoad = remoteMessage.getData();
            for(Map.Entry<String, String> entries : dataLoad.entrySet()){
                if(entries.getKey().equals("title")){
                    notificationTitle = entries.getValue();
                }
                if(entries.getKey().equals("message")){
                    notificationMessage = entries.getValue();
                }
            }
        }
        String message = remoteMessage.getNotification().getBody();
        //get current date
        String dateNow = com.inducesmile.androidecommerceshop.utils.Helper.convertDateToString(new Date());

        if(TextUtils.isEmpty(storedNotification)){
            allNotifications.add(new NotifyObject(notificationTitle, message, dateNow));
            String listNotification = gson.toJson(allNotifications);
            ((CustomApplication)getApplication()).getShared().storeNotification(listNotification);

        }else{
            List<NotifyObject> updateNotification = gson.fromJson(storedNotification, new TypeToken<List<NotifyObject>>(){}.getType());
            updateNotification.add(new NotifyObject(notificationTitle, message, dateNow));
            ((CustomApplication)getApplication()).getShared().storeNotification(gson.toJson(updateNotification));
        }

        sendNotification(notificationTitle, message);
    }

    private void sendNotification(String title, String messages) {
        Intent intent = new Intent(this, ShopActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.notificationicon)
                .setContentTitle(title)
                .setContentText(messages)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }
}
