package site.jiyang.dots365

import androidx.annotation.DrawableRes

/**
 * Create by jy on 2019-10-27
 */
object DrawableGetter {

    @DrawableRes
    fun get(i: Int, month: Month): Int {
        return when {
            i < month.spend -> R.drawable.dot_highlight
            i == month.spend && month.isDone -> R.drawable.dot_current
            else -> R.drawable.dot_default
        }
    }
}