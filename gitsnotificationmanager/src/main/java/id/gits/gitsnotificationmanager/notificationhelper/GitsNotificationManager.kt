package id.gits.gitsnotificationmanager.notificationhelper

import android.annotation.SuppressLint
import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.SystemClock
import android.support.annotation.DrawableRes
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat


/**
 * Class ini dipakai untuk membuat notification
 * untuk beberapa setting notification bisa di lihat di GitsNotificationApplication
 */
class GitsNotificationManager(private val context: Context, @DrawableRes val icon: Int, private val summaryText: String) {


//    private val mPref = context.getSharedPreferences("NOTIFICATION_NAME", Context.MODE_PRIVATE)
//    private val mGson = Gson()
    private val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)


    fun addNotificationToGroup(
        mData: NotificationModel,
        homeIntent: Intent
    ) {


        val mListNotification = ArrayList<Notification>()
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

        val notification = NotificationCompat.Builder(context, GitsNotificationApplication.CHANNEL_1_ID)
            .setSmallIcon(mData.icon)
            .setContentTitle(mData.title)
            .setContentText(mData.message)
            .setGroup("example_group")
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setDeleteIntent(deleteIntent)
            .build()

        mListNotification.add(notification)


        for (i in 0 until mListNotification.size) {
            SystemClock.sleep(500)
            notificationManager.notify(mData.id, mListNotification[i])
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

            val notification = NotificationCompat.Builder(context, GitsNotificationApplication.CHANNEL_1_ID)
                .setSmallIcon(model.icon)
                .setContentTitle(model.title)
                .setContentText(model.message)
                .setGroup("example_group")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setDeleteIntent(deleteIntent)
                .build()

            mListNotification.add(notification)
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



    //    fun sendOnChannel1(context: Context) {
//        sendChannel1Notification(context)
//    }

//    private fun sendChannel1Notification(context: Context) {
//        val activityIntent = Intent(context, DemoActivity::class.java)
//        val contentIntent = PendingIntent.getActivity(
//            context,
//            0, activityIntent, 0
//        )
//
//        val remoteInput = RemoteInput.Builder("key_text_reply")
//            .setLabel("Your answer...")
//            .build()
//
//        val replyIntent: Intent
//        var replyPendingIntent: PendingIntent? = null
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            replyIntent = Intent(context, DemoActivity::class.java)
//            replyPendingIntent = PendingIntent.getBroadcast(
//                context,
//                0, replyIntent, 0
//            )
//        } else {
//            //start chat activity instead (PendingIntent.getActivity)
//            //cancel notification with notificationManagerCompat.cancel(id)
//        }
//
//        val replyAction = NotificationCompat.Action.Builder(
//            R.drawable.ic_launcher_foreground,
//            "Reply",
//            replyPendingIntent
//        ).addRemoteInput(remoteInput).build()
//
//        val messagingStyle = NotificationCompat.MessagingStyle(Person.Builder().apply {
//            setName("Me")
//        }.build())
//        messagingStyle.conversationTitle = "Group Chat"
//
//        for (chatMessage in 0 until 3) {
//            val notificationMessage = NotificationCompat.MessagingStyle.Message(
//                "Test",
//                System.currentTimeMillis(),
//                "Radhika"
//            )
//            messagingStyle.addMessage(notificationMessage)
//        }
//
//        val notification = NotificationCompat.Builder(context, GitsNotificationApplication.CHANNEL_1_ID)
//            .setSmallIcon(R.drawable.ic_launcher_foreground)
//            .setStyle(messagingStyle)
//            .addAction(replyAction)
//            .setColor(Color.BLUE)
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
//            .setContentIntent(contentIntent)
//            .setAutoCancel(true)
//            .setOnlyAlertOnce(true)
//            .build()
//
//        val notificationManager = NotificationManagerCompat.from(context)
//        notificationManager.notify(1, notification)
//    }

//    private fun getData(): List<NotificationModel> {
//        val contentString = mPref.getString(DATA_KEY, "")
//        return if (contentString.isNullOrEmpty()) {
//            emptyList()
//        } else {
//            val typeModel = object : TypeToken<MutableList<NotificationModel>>() {}.type
//            mGson.fromJson<MutableList<NotificationModel>>(contentString, typeModel)
//        }
//    }
//
//    private fun saveData(content: List<NotificationModel>) {
//        val contentString = mPref.getString(DATA_KEY, "")
//
//        if (contentString.isNullOrEmpty()) {
//            mPref.edit().putString(DATA_KEY, mGson.toJson(content)).apply()
//        } else {
//            val typeModel = object : TypeToken<MutableList<NotificationModel>>() {}.type
//            val list = mGson.fromJson<MutableList<NotificationModel>>(contentString, typeModel)
//
//            content.forEach {
//                list.add(it)
//            }
//            mPref.edit().putString(DATA_KEY, mGson.toJson(list)).apply()
//        }
//    }
//
//    private fun addData(content: NotificationModel) {
//        val contentString = mPref.getString(DATA_KEY, "")
//
//        if (contentString.isNullOrEmpty()) {
//            mPref.edit().putString(DATA_KEY, mGson.toJson(ArrayList<NotificationModel>().apply { add(content) }))
//                .apply()
//        } else {
//            val typeModel = object : TypeToken<MutableList<NotificationModel>>() {}.type
//            val list = mGson.fromJson<MutableList<NotificationModel>>(contentString, typeModel)
//            list.add(content)
//            mPref.edit().putString(DATA_KEY, mGson.toJson(list)).apply()
//        }
//    }
//
//    private fun delete(id: Int) {
//        val contentString = mPref.getString(DATA_KEY, "")
//        if (!contentString.isNullOrEmpty()) {
//            val typeModel = object : TypeToken<MutableList<NotificationModel>>() {}.type
//            val list = mGson.fromJson<MutableList<NotificationModel>>(contentString, typeModel)
//            val model = list.find { it.id == id }
//
//            if (model != null) {
//                list.remove(model)
//            }
//
//
//            mPref.edit().putString(DATA_KEY, mGson.toJson(list)).apply()
//        }
//    }

//    private fun deleteAll() {
//        mPref.edit().putString(DATA_KEY, mGson.toJson(ArrayList<NotificationModel>())).apply()
//    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var notifInstance: GitsNotificationManager? = null

        const val DATA_KEY = "DATA_KEY$"
        const val NOTIF_ID_KEY = "NOTIF_ID$"
    }
}