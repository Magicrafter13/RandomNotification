package com.cs455.randomnotification;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class serverConnect extends Fragment {
    private FragmentActivity activity;
    private ServerData data;

    public void notify(View view){
        // TODO: get name of user who pinged from database
        // This gets the device name
        String user= Settings.System.getString(activity.getContentResolver(), "device_name");
        String context="hahahaha you just got randomly notified";
        // Builder object for a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "notify channel ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // default android icon
            .setContentTitle(user+" notified you!")
            .setContentText(context)
            .setAutoCancel(true)                             // Allows for removing the notification
            .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(activity);
        // Sends a notification from the manager based on the notification ID
        notificationManager.notify(data.nextId(), builder.build());
    }

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_blank, container, false);

        activity = requireActivity();

        // Set onClick method for button.
        view.findViewById(R.id.funnyButton).setOnClickListener((buttonView) -> data.sendRequest());

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceStage) {
        super.onViewCreated(view, savedInstanceStage);
        data = new ViewModelProvider(requireActivity()).get(ServerData.class);
        data.setNotificationListener(() -> serverConnect.this.notify(view));
    }
}
