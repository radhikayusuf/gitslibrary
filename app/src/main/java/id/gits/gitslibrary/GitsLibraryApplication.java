package id.gits.gitslibrary;

import android.app.Application;
import id.gits.gitsdesigncomponent.GitsDesignApplication;

public class GitsLibraryApplication extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
        GitsDesignApplication.createNotificationChannels(getApplicationContext());
    }

}
