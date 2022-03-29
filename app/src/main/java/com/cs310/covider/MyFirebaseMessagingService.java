package com.cs310.covider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.cs310.covider.model.Pair;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.jetbrains.annotations.NotNull;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(@NonNull @NotNull String token) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                @Override
                public void onSuccess(String s) {
                    FirebaseFirestore.getInstance().collection("DeviceTokens").document(FirebaseAuth.getInstance().getCurrentUser().getEmail()).set(new Pair("id", s));
                }
            });
        }
    }


    @Override
    public final void onMessageReceived(@NonNull final RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //In this example we only consider data messages being received and do not differentiate between the app being in the foreground or background.

        // Get the data from the message.
        final RemoteMessage.Notification notification = remoteMessage.getNotification();

        final Bundle notificationBundle = new Bundle();

        final String title = notification.getTitle();
        final String message = notification.getBody();

        sendNotification(notificationBundle, title != null ? title : "", message != null ? message : "");

    }


    private void sendNotification(@NonNull final Bundle bundle, @NonNull final String title, @NonNull final String message) {

        final Intent intentClick = new Intent(this, MainActivity.class);
        intentClick.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intentClick.putExtras(bundle);

        // Build the pending intent that contains our launch intent.
        final PendingIntent pendingIntentClick = PendingIntent.getActivity(this, 0, intentClick, PendingIntent.FLAG_CANCEL_CURRENT);

        // Build the notification.
        final String channelId = "push_01";
        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.adaptive_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntentClick)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message));

        // Post the push notification.
        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= 26) {
                // Add the push channel if we are targeting API 26+. You should use channel ids, names, and descriptions as appropriate to your usage.
                final NotificationChannel channel = new NotificationChannel(channelId, "apple Push", NotificationManager.IMPORTANCE_HIGH);
                channel.setDescription("apple Push Messages");
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}
