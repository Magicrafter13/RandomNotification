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

    public void sendNotification(String title, String body) {
        // TODO: get name of user who pinged from database
        // This gets the device name
        // Builder object for a notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), "RandomNotification")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // default android icon
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)                             // Focuses (opens) app when notification is clicked
            .setPriority(3);
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

        data = new ViewModelProvider(activity).get(ServerData.class);
        data.setNotificationListener(this::sendNotification);

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceStage) {
        super.onViewCreated(view, savedInstanceStage);

        data.sendName(Settings.Global.getString(activity.getContentResolver(), "device_name"));
        // Set onClick method for button.
        view.findViewById(R.id.funnyButton).setOnClickListener((buttonView) -> data.sendRequest());
    }
}
