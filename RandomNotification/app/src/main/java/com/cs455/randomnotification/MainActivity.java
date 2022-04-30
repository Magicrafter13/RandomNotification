package com.cs455.randomnotification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    //private ServerData data;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //data = new ViewModelProvider(this).get(ServerData.class);

        NotificationChannel channel = new NotificationChannel(
            "RandomNotification",
            "Funny Notifications Haha XD",
            NotificationManager.IMPORTANCE_HIGH
        );
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);

        setContentView(R.layout.activity_main);
    }
}
