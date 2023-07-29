package com.alpertunademirbas.nakile;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


    public class NotificationHelper extends ContextWrapper {
    public static final String CHANNEL_ID = "TransferReminderChannel";
    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);

            createChannel();

    }

    public boolean areNotificationsEnabled() {
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        return notificationManager.areNotificationsEnabled();
    }

    private void createChannel() {

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Transfer Reminder", NotificationManager.IMPORTANCE_LOW);
            channel.setDescription("This channel is used by Transfer App");
            getManager().createNotificationChannel(channel);
            System.out.println("Kanal oluştu");

    }

    public NotificationManager getManager() {
        if (mManager == null) {
            System.out.println("değer null geldi");
            mManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title, String message) {
        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_date_range_24);
    }
}


