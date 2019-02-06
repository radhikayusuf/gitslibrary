package id.gits.gitsdesigncomponent.notificationhelper

import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle


data class NotificationModel(
    val id: Int, /* id of the notification*/
    val title: String, /* Title of the notification*/
    val message: String, /* Message of the notification*/
    val icon: Int, /* icon of the notification*/
    val isBackToHome: Boolean, /* If true, when user close targetIntent, application will open home activity */
    val targetIntentAction: String, /* Target page when click notification */
    val sound: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), /* Notification sound */
    val data: Bundle = Bundle() /* data for intent */
)