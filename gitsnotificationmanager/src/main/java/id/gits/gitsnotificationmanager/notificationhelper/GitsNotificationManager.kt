package id.gits.gitsnotificationmanager.notificationhelper

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.SystemClock
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat


/**
 * Class ini dipakai untuk membuat notification
 * untuk beberapa setting notification bisa di lihat di GitsNotificationApplication
 */
class GitsNotificationManager(private val context: Context, @DrawableRes val icon: Int, private val summaryText: String) {

    private val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)

    fun addNotificationToGroup(
        mData: NotificationModel,
        homeIntent: Intent
    ) {

        val inboxStyle = NotificationCompat.InboxStyle()
            .setSummaryText(summaryText)


        val intent = Intent(context, NotificationBroadcastReceiver::class.java)
        val deleteIntent = PendingIntent.getBroadcast(context, 0, intent, 0)


        val pendingIntent = PendingIntent.getActivities(
            context,
            0,
            if (mData.isBackToHome) arrayOf(
                homeIntent,
                Intent(mData.targetIntentAction)
            ) else arrayOf(Intent(mData.targetIntentAction)),
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder = NotificationCompat.Builder(context, GitsNotificationApplication.CHANNEL_1_ID)
            .setSmallIcon(mData.icon)
            .setContentTitle(mData.title)
            .setContentText(mData.message)
            .setGroup("example_group")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDeleteIntent(deleteIntent)

        if (mData.sound != null) {
            notificationBuilder.setSound(mData.sound)
        }

        val notification = notificationBuilder.build()

        notificationManager.notify(mData.id, notification)


        val summaryNotification = NotificationCompat.Builder(context, GitsNotificationApplication.CHANNEL_1_ID)
            .setSmallIcon(icon)
            .setStyle(inboxStyle)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setGroup("example_group")
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
            .setGroupSummary(true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(801, summaryNotification)
    }

    fun createGroupNotification(
        mData: List<NotificationModel>,
        homeIntent: Intent
    ) {
        val mListNotification = ArrayList<Notification>()
        val inboxStyle = NotificationCompat.InboxStyle()
            .setSummaryText(summaryText)

        for (model in mData) {
            val pendingIntent = PendingIntent.getActivities(
                context,
                0,
                if (model.isBackToHome) arrayOf(
                    homeIntent,
                    Intent(model.targetIntentAction).apply { putExtra(NOTIF_ID_KEY, model.id) }) else arrayOf(
                    Intent(
                        model.targetIntentAction
                    ).apply { putExtra(NOTIF_ID_KEY, model.id) }),
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val intent = Intent(context, NotificationBroadcastReceiver::class.java).apply {
                putExtra(NOTIF_ID_KEY, model.id)
                action = model.id.toString()
            }

            val deleteIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            val notificationBuilder = NotificationCompat.Builder(context, GitsNotificationApplication.CHANNEL_1_ID)
                .setSmallIcon(model.icon)
                .setContentTitle(model.title)
                .setContentText(model.message)
                .setGroup("example_group")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDeleteIntent(deleteIntent)


            if (model.sound != null) {
                notificationBuilder.setSound(model.sound)
            }

            mListNotification.add(notificationBuilder.build())
        }

        for (i in 0 until mListNotification.size) {
            SystemClock.sleep(500)
            notificationManager.notify(mData[i].id, mListNotification[i])
        }

        val summaryNotification = NotificationCompat.Builder(context, GitsNotificationApplication.CHANNEL_1_ID)
            .setSmallIcon(icon)
            .setStyle(inboxStyle)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setGroup("example_group")
            .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_CHILDREN)
            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            .setGroupSummary(true)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(801, summaryNotification)
    }


    companion object {
        const val NOTIF_ID_KEY = "NOTIF_ID$"
    }
}