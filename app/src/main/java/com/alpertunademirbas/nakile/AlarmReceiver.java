package com.alpertunademirbas.nakile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("Transfer Hatırlatıcı", "Bugün için bir transfer var.");
        notificationHelper.getManager().notify(1, nb.build());
        System.out.println("Alarm eklendi");
    }
}


