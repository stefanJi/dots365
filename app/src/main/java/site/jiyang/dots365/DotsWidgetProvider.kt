package site.jiyang.dots365

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService

/**
 * Create by jy on 2019-09-28
 */
class DotsWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        Log.d(TAG, "[onUpdate]")

        appWidgetIds.forEach { appWidgetId ->
            updateWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
    }

    override fun onAppWidgetOptionsChanged(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetId: Int,
        newOptions: Bundle?
    ) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions)
        Log.d(TAG, "[onAppWidgetOptionsChanged]")
    }

    companion object {
        const val TAG = "DotsWidgetProvider"

        fun updateWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {

            val intent = Intent(context, DotsWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }

            // Create an Intent to launch Activity
            val pendingIntent: PendingIntent =
                Intent(context, DotsWidgetActivity::class.java).let {
                    it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    PendingIntent.getActivity(context, 0, it, 0)
                }

            val dotDate = DateCalculator.get()
            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.widget_dots
            ).apply {
                setRemoteAdapter(R.id.lv_days, intent)
                setEmptyView(R.id.lv_days, R.id.loading)
                setTextViewText(R.id.tvYear, "${dotDate.year}")
                setTextViewText(R.id.tvPercent, dotDate.percent)
                setTextViewText(R.id.tvSpend, "${dotDate.dayOfYear}/${dotDate.days}")
                setOnClickPendingIntent(R.id.dot_content, pendingIntent)
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.lv_days)
        }
    }
}

class DotsWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return DotsWidgetRemoteViewFactory(this.applicationContext, intent)
    }
}

class DotsWidgetRemoteViewFactory(private val context: Context, intent: Intent) :
    RemoteViewsService.RemoteViewsFactory {

    private lateinit var dotDate: DotDate

    override fun onCreate() {
        dotDate = DateCalculator.get()
        Log.d(TAG, "days: $dotDate")
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onDataSetChanged() {
        dotDate = DateCalculator.get()
    }

    override fun hasStableIds(): Boolean = true

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.dot_row).apply {
            val month = dotDate.months[position]
            val gone = View.GONE
            when {
                month.total == 31 -> {
                    setViewVisibility(R.id.day1, View.VISIBLE)
                    setViewVisibility(R.id.day2, View.VISIBLE)
                    setViewVisibility(R.id.day3, View.VISIBLE)
                }
                month.total == 30 -> setViewVisibility(R.id.day1, gone)
                month.total == 29 -> {
                    setViewVisibility(R.id.day1, gone)
                    setViewVisibility(R.id.day2, gone)
                }
                month.total == 28 -> {
                    setViewVisibility(R.id.day1, gone)
                    setViewVisibility(R.id.day2, gone)
                    setViewVisibility(R.id.day3, gone)
                }
            }

            val delta = 31 - month.total
            repeat(month.total) { i ->
                val drawable = if (i < month.spend) {
                    R.drawable.dot_highlight
                } else {
                    R.drawable.dot_default
                }
                setImageViewResource(DOTS[i + delta], drawable)
            }
        }
    }

    override fun getCount(): Int = dotDate.months.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
    }

    companion object {
        const val TAG = "WidgetService"

        val DOTS = intArrayOf(
            R.id.day1,
            R.id.day2,
            R.id.day3,
            R.id.day4,
            R.id.day5,
            R.id.day6,
            R.id.day7,
            R.id.day8,
            R.id.day9,
            R.id.day10,
            R.id.day11,
            R.id.day12,
            R.id.day13,
            R.id.day14,
            R.id.day15,
            R.id.day16,
            R.id.day17,
            R.id.day18,
            R.id.day19,
            R.id.day20,
            R.id.day21,
            R.id.day22,
            R.id.day23,
            R.id.day24,
            R.id.day25,
            R.id.day26,
            R.id.day27,
            R.id.day28,
            R.id.day29,
            R.id.day30,
            R.id.day31
        )
    }
}