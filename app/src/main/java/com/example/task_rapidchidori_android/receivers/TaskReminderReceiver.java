package com.example.task_rapidchidori_android.receivers;

import static com.example.task_rapidchidori_android.helper.Constants.CHANNEL_ID;
import static com.example.task_rapidchidori_android.helper.Constants.TASK_DESC;
import static com.example.task_rapidchidori_android.helper.Constants.TASK_ID;
import static com.example.task_rapidchidori_android.helper.Constants.TASK_TITLE;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.task_rapidchidori_android.R;
import com.example.task_rapidchidori_android.ui.activities.TaskActivity;

public class TaskReminderReceiver extends BroadcastReceiver {

    @SuppressLint("UnspecifiedImmutableFlag")
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent i = new Intent(context, TaskActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getActivity(context
                    .getApplicationContext(), 0, i, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(context
                    .getApplicationContext(), 0, i, 0);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,
                CHANNEL_ID)
                .setSmallIcon(R.drawable.app_icon)
                .setContentTitle(context.getString(R.string.notification_head))
                .setContentText(String.format(context.getString(R.string.notification_body_text),
                        intent.getExtras().getString(TASK_TITLE)))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(intent.getExtras().getString(TASK_DESC)))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify((int) intent.getExtras().getLong(TASK_ID), builder.build());
    }
}