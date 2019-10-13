package site.jiyang.dots365

import java.util.*
import kotlin.math.ceil


/**
 * Create by jy on 2019-09-28
 */
object DateCalculator {

    private val MONTH_31 = intArrayOf(1, 3, 5, 7, 8, 10, 12)
    private val MONTH_30 = intArrayOf(4, 6, 9, 11)

    fun get(): DotDate {
        val calendar = Calendar.getInstance(Locale.getDefault())
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val doy = calendar.get(Calendar.DAY_OF_YEAR)
        val dom = calendar.get(Calendar.DAY_OF_MONTH)

        val leapYear = year % 400 == 0
        val days = if (leapYear) 365 else 364

        val months = Array(12) { i ->
            val total = when (i + 1) {
                in MONTH_30 -> 30
                in MONTH_31 -> 31
                else -> {
                    if (leapYear) {
                        29
                    } else {
                        28
                    }
                }
            }

            val spend = when {
                i < month -> total
                i == month -> dom - 1
                else -> 0
            }
            Month(i, total, spend)
        }

        val d = ceil(doy / days.toDouble() * 100).toInt()
        val percent = "$d%"
        return DotDate(
            year,
            month,
            days,
            doy,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND),
            months,
            percent
        )
    }
}

data class DotDate(
    val year: Int,
    val month: Int,
    val days: Int,
    val dayOfYear: Int,
    val hour: Int,
    val minute: Int,
    val second: Int,
    val months: Array<Month>,
    val percent: String
)

data class Month(
    val month: Int,
    val total: Int,
    val spend: Int
)