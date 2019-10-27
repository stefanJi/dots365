package site.jiyang.dots365

import androidx.annotation.VisibleForTesting
import java.text.DecimalFormat
import java.util.*


/**
 * Create by jy on 2019-09-28
 */
object DateCalculator {

    private val MONTH_31 = intArrayOf(1, 3, 5, 7, 8, 10, 12)
    private val MONTH_30 = intArrayOf(4, 6, 9, 11)

    fun get(): DotDate {
        val calendar = Calendar.getInstance(Locale.getDefault())
        return calculate(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.DAY_OF_YEAR)
        )
    }

    @VisibleForTesting
    fun calculate(year: Int, month: Int, dom: Int, doy: Int): DotDate {
        val leapYear = year % 400 == 0
        val days = if (leapYear) 366 else 365

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
                i == month -> dom - 1 // 因为 DAY_OF_MOUTH 返回的是从 1 开始的
                else -> 0
            }
            Month(i, total, spend, i == month)
        }

        val percent = DecimalFormat("00.0").format(doy / days.toDouble() * 100) + "%"
        return DotDate(
            year,
            month,
            days,
            doy,
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
    val months: Array<Month>,
    val percent: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as DotDate

        if (year != other.year) return false
        if (month != other.month) return false
        if (days != other.days) return false
        if (dayOfYear != other.dayOfYear) return false
        if (!months.contentEquals(other.months)) return false
        if (percent != other.percent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = year
        result = 31 * result + month
        result = 31 * result + days
        result = 31 * result + dayOfYear
        result = 31 * result + months.contentHashCode()
        result = 31 * result + percent.hashCode()
        return result
    }
}

/**
 * @param month 当前月份 [0, 12)
 * @param total 当前月份有多少天 28, 29, 30, 31
 * @param spend 当前月已经过了多少天
 */
data class Month(
    val month: Int,
    val total: Int,
    val spend: Int,
    val isDone: Boolean
)