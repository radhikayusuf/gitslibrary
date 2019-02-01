package id.gits.gitslibrary.presentation

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import id.gits.gitslibrary.R
import id.gits.gitslibrary.databinding.ActivityDemoBinding
import id.gits.gitsnotificationmanager.notificationhelper.GitsNotificationManager
import id.gits.gitsnotificationmanager.notificationhelper.NotificationModel


class DemoActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityDemoBinding
    private lateinit var gitsNotificationManager: GitsNotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_demo)
        gitsNotificationManager = GitsNotificationManager(this@DemoActivity,
            R.drawable.ic_launcher_foreground, "radhika@gits.co.id")

        mViewBinding.buttonTestNotif.setOnClickListener {
            val data = NotificationModel(System.currentTimeMillis().toInt(), mViewBinding.etTitle.text.toString(), mViewBinding.etMessage.text.toString(),
                R.drawable.ic_launcher_foreground, mViewBinding.checkboxmain.isChecked, if(mViewBinding.radioA.isChecked) "targetANotifActivityAction" else "targetBNotifActivityAction")
            mViewBinding.etTitle.text = Editable.Factory.getInstance().newEditable("")
            mViewBinding.etMessage.text = Editable.Factory.getInstance().newEditable("")
            mViewBinding.checkboxmain.isChecked = false
            gitsNotificationManager.addNotificationToGroup(data, Intent(this@DemoActivity, DemoActivity::class.java))

        }
    }



}

