package id.gits.gitslibrary.presentation

import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.View
import id.gits.gitsdesigncomponent.notificationhelper.GitsNotificationManager
import id.gits.gitsdesigncomponent.notificationhelper.NotificationModel
import id.gits.gitsdesigncomponent.view.gitscalendarview.CalendarModel
import id.gits.gitsdesigncomponent.view.gitscalendarview.GitsCalendarUserActionListener
import id.gits.gitsdesigncomponent.view.gitscalendarview.DateHelper
import id.gits.gitslibrary.R
import id.gits.gitslibrary.databinding.ActivityDemoBinding


class DemoActivity : AppCompatActivity() {

    private lateinit var mViewBinding: ActivityDemoBinding
    private lateinit var gitsNotificationManager: GitsNotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewBinding = DataBindingUtil.setContentView(this, R.layout.activity_demo)
        initComponent()
    }


    private fun initComponent() {
        gitsNotificationManager = GitsNotificationManager(this@DemoActivity,
            R.drawable.ic_launcher_foreground, "radhika@gits.co.id")

        mViewBinding.buttonTestNotif.setOnClickListener {
            val data = NotificationModel(System.currentTimeMillis().toInt(), mViewBinding.etTitle.text.toString(), mViewBinding.etMessage.text.toString(), R.drawable.ic_launcher_foreground, mViewBinding.checkboxmain.isChecked, if(mViewBinding.radioA.isChecked) "targetANotifActivityAction" else "targetBNotifActivityAction")
            mViewBinding.etTitle.text = Editable.Factory.getInstance().newEditable("")
            mViewBinding.etMessage.text = Editable.Factory.getInstance().newEditable("")
            mViewBinding.checkboxmain.isChecked = false
            gitsNotificationManager.addNotificationToGroup(data, Intent(this@DemoActivity, DemoActivity::class.java))

        }
        mViewBinding.calendarView.apply {
            initializeCalendar(object : GitsCalendarUserActionListener{
                override fun onClickNextMonth(month: Int, year: Int) {
                    Handler().postDelayed({
                        val list = ArrayList<CalendarModel>().apply {
                            add(CalendarModel(DateHelper.stringToTimeStamp("20/$month/$year", "dd/MM/yyyy"), "123", null, R.drawable.ic_test_mark, null))
                        }
                        setData(list, false)

                        this@apply.clickNextMonth()
                    }, 2000)
                }

                override fun onClickPreviousMonth(month: Int, year: Int) {
                   Handler().postDelayed({
                       val list = ArrayList<CalendarModel>().apply {
                           add(CalendarModel(DateHelper.stringToTimeStamp("14/$month/$year", "dd/MM/yyyy"), "", null, null, R.drawable.red_circle))
                           add(CalendarModel(DateHelper.stringToTimeStamp("03/$month/$year", "dd/MM/yyyy"), "123", null, R.drawable.ic_test_mark, null))
                           add(CalendarModel(DateHelper.stringToTimeStamp("15/$month/$year", "dd/MM/yyyy"), "", null, null, R.drawable.red_circle))
                           add(CalendarModel(DateHelper.stringToTimeStamp("16/$month/$year", "dd/MM/yyyy"), "", null, null, R.drawable.red_circle))

                       }

                       list.sortBy { it.date }
                       setData(list, false)

                       this@apply.clickPreviousMonth()
                   }, 3000)
                }

                override fun onCreateDateView(view: View, position: Int, time: Long, data: CalendarModel?) {

                }


            })


            val list = ArrayList<CalendarModel>().apply {
                add(CalendarModel(System.currentTimeMillis() - 86400000L, "123213213", null, R.drawable.ic_test_mark, null))
                add(CalendarModel(DateHelper.stringToTimeStamp("14/02/2019", "dd/MM/yyyy"), "", null, null, R.drawable.red_circle))
                add(CalendarModel(DateHelper.stringToTimeStamp("03/02/2019", "dd/MM/yyyy"), "123", null, R.drawable.ic_test_mark, null))
                add(CalendarModel(DateHelper.stringToTimeStamp("15/02/2019", "dd/MM/yyyy"), "", null, null, R.drawable.red_circle))
                add(CalendarModel(DateHelper.stringToTimeStamp("16/02/2019", "dd/MM/yyyy"), "", null, null, R.drawable.red_circle))

            }

            list.sortBy { it.date }
            setData(list, true)
        }


        mViewBinding.validatorView.initialComponent()

        mViewBinding.buttonValidation.setOnClickListener {
            val validateAllValue = mViewBinding.validatorView.validateAllValue()
            if (validateAllValue.isResult) {

            }
        }
    }


}

