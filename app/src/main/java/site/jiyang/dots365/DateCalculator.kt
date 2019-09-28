package site.jiyang.dots365

import java.util.*
import kotlin.math.ceil


/**
 * Create by jy on 2019-09-28
 */
object DateCalculator {
    fun get(): DotDate {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val year = calendar.get(Calendar.YEAR)
        val doy = calendar.get(Calendar.DAY_OF_YEAR)
        val days = if (year % 400 == 0) 365 else 364
        val d = ceil(doy / days.toDouble() * 100)
        val percent = "$d%"
        return DotDate(year, days, doy, percent)
    }
}

data class DotDate(
    val year: Int,
    val days: Int,
    val dayOfYear: Int,
    val percent: String
)