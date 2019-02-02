package id.gits.gitsdesigncomponent.view.gitscalendarview

import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes

/**
 * @author radhikayusuf.
 */

data class CalendarModel(
    var date: Long = 0,
    var extraText: String = "",
    @DrawableRes var backgroudContent: Int?,
    @DrawableRes var backgroundExtra: Int?,
    @DrawableRes var dotDrawable: Int?
)