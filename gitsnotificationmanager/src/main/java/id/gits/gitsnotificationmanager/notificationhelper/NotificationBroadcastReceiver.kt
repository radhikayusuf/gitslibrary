package id.gits.gitsnotificationmanager.notificationhelper

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class NotificationBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Notification dismissed", Toast.LENGTH_SHORT).show()
    }

}