package id.gits.gitsdesigncomponent

import android.app.NotificationChannel
import android.app.NotificationChannelGroup
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.os.Build

class GitsDesignApplication {

    companion object {

        private const val GROUP_1_ID = "group1"
        private const val GROUP_2_ID = "group2"
        private const val CHANNEL_1_ID = "channel1"
        private const val CHANNEL_2_ID = "channel2"
        private const val CHANNEL_3_ID = "channel3"
        private const val CHANNEL_4_ID = "channel4"

        @JvmStatic
        fun createNotificationChannels(context: Context) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                val group1 = NotificationChannelGroup(GROUP_1_ID, "Group 1")

                val group2 = NotificationChannelGroup(GROUP_2_ID, "Group 2")

                val channel1 = NotificationChannel(CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH)
                channel1.description = "This is Channel 1"
                channel1.group = GROUP_1_ID
                channel1.lightColor = Color.RED
                channel1.enableVibration(true)
                channel1.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

                val channel2 = NotificationChannel(CHANNEL_2_ID, "Channel 2", NotificationManager.IMPORTANCE_LOW)
                channel2.description = "This is Channel 2"
                channel2.group = GROUP_1_ID
                channel2.lightColor = Color.RED
                channel2.enableVibration(true)
                channel2.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

                val channel3 = NotificationChannel(CHANNEL_3_ID, "Channel 3", NotificationManager.IMPORTANCE_HIGH)
                channel3.description = "This is Channel 3"
                channel3.group = GROUP_2_ID
                channel3.lightColor = Color.RED
                channel3.enableVibration(true)
                channel3.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

                val channel4 = NotificationChannel(CHANNEL_4_ID, "Channel 4", NotificationManager.IMPORTANCE_LOW)
                channel4.description = "This is Channel 4"

                val manager = context.getSystemService(NotificationManager::class.java)
                manager.createNotificationChannelGroup(group1)
                manager.createNotificationChannelGroup(group2)
                manager.createNotificationChannel(channel1)
                manager.createNotificationChannel(channel2)
                manager.createNotificationChannel(channel3)
                manager.createNotificationChannel(channel4)
            }
        }

    }
}