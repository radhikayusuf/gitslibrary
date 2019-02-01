package id.gits.gitslibrary;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import id.gits.gitsnotificationmanager.notificationhelper.GitsNotificationApplication;

public class GitsLibraryApplication extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        GitsNotificationApplication.createNotificationChannels(getApplicationContext());
    }

}
