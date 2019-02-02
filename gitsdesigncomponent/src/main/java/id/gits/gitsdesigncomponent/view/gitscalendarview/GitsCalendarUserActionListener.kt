package id.gits.gitsdesigncomponent.view.gitscalendarview

import android.view.View

interface GitsCalendarUserActionListener {

    fun onClickNextMonth(month: Int, year: Int)

    fun onClickPreviousMonth(month: Int, year: Int)

    fun onCreateDateView(view: View, position: Int, time: Long, data: CalendarModel?)


}
