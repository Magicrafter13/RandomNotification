package com.cs455.randomnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    Button notifyButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        notifyButton=findViewById(R.id.notifyButton);
        //Initialize notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            NotificationChannel channel = new NotificationChannel("notify channel ID",
                    "server notify", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void notify(View view){
        //TODO: get name of user who pinged from database
        //This gets the device name
        String user= Settings.System.getString(getContentResolver(),"device_name");
        String context="hahahaha you just got randomly notified";
        //Builder object for a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "notify channel ID")
                .setSmallIcon(R.drawable.ic_launcher_foreground)    //default android icon
                .setContentTitle(user+" notified you!")
                .setContentText(context)
                .setAutoCancel(true)    //Allows for removing the notification
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MainActivity.this);
        //Sends a notification from the manager based on the notification ID
        notificationManager.notify(1, builder.build());
    }
}