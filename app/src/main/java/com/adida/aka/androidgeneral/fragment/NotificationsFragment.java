package com.adida.aka.androidgeneral.fragment;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.adida.aka.androidgeneral.activity.UpdateActivity;
import com.adida.aka.androidgeneral.R;
import com.adida.aka.androidgeneral.widget.Constans;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends android.app.Fragment {
    Button btnCreate, btnClose;
    int notificationId;
    private View mView;

    private int mVersion = 0;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_notifications, container, false);
        btnCreate = (Button) mView.findViewById(R.id.buttonCreateNotification);
        btnClose  = (Button) mView.findViewById(R.id.buttonCloseNotificattion);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNotification();
            }

        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeNotification();
            }
        });
        return mView;
    }

    private void closeNotification() {
        NotificationManager mNotifyMgr =
                (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(notificationId);
    }

    private void createNotification() {

        mVersion++;

        android.support.v4.app.NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getActivity())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Có thông báo")
                        .setContentText("Mời bạn nhấn để cập nhật version");

        Intent resultIntent = new Intent(getActivity(), UpdateActivity.class);
            resultIntent.putExtra(Constans.EXTRA_VERSION, mVersion);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        getActivity(),
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(uri);

//        Uri newSound= Uri.parse("android.resource://"
//                + getActivity().getPackageName() + "/" + R.raw.adidaphat);
//        mBuilder.setSound(newSound);


        notificationId = 113;

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
        // Builds the notification and issues it.
        mNotifyMgr.notify(notificationId, mBuilder.build());


    }

}
