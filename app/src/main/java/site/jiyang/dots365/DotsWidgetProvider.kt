package site.jiyang.dots365

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import java.util.*

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
            updateWidget(context, appWidgetManager, appWidgetId, DotsWidgetConfig())
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d(TAG, "[onReceive]")
    }

    override fun onRestored(context: Context?, oldWidgetIds: IntArray?, newWidgetIds: IntArray?) {
        super.onRestored(context, oldWidgetIds, newWidgetIds)
        Log.d(TAG, "[onRestored]")
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        Log.d(TAG, "[onEnabled]")
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        Log.d(TAG, "[onDisabled]")
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        super.onDeleted(context, appWidgetIds)
        Log.d(TAG, "[onDeleted]")
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
            appWidgetId: Int,
            dotsWidgetConfig: DotsWidgetConfig
        ) {

            val intent = Intent(context, StackWidgetService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }

            // Create an Intent to launch ExampleActivity
            val pendingIntent: PendingIntent =
                Intent(context, DotsWidgetActivity::class.java).let {
                    it.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    PendingIntent.getActivity(context, 0, it, 0)
                }

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            val dotDate = DateCalculator.get()
            val views: RemoteViews = RemoteViews(
                context.packageName,
                R.layout.widget_dots
            ).apply {
                setOnClickPendingIntent(R.id.button, pendingIntent)
                setRemoteAdapter(R.id.grid_view, intent)
                setEmptyView(R.id.grid_view, R.id.button)
                setTextViewText(R.id.tvYear, "${dotDate.year}")
                setTextViewText(R.id.tvPercent, dotDate.percent)
                setTextViewText(R.id.tvSpend, "${dotDate.dayOfYear}/${dotDate.days}")
            }

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

class StackWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext, intent)
    }
}

class StackRemoteViewsFactory(
    private val context: Context,
    intent: Intent
) : RemoteViewsService.RemoteViewsFactory {

    private lateinit var widgetItems: MutableList<WidgetItem>

    override fun onCreate() {
        widgetItems = mutableListOf()

        val dotDate = DateCalculator.get()
        Log.d(TAG, "days: ${dotDate.days} dayOfYear: ${dotDate.dayOfYear}")
        repeat(dotDate.days) {
            widgetItems.add(WidgetItem(it < dotDate.dayOfYear))
        }
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = position.toLong()

    override fun onDataSetChanged() {
    }

    override fun hasStableIds(): Boolean = true

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_item).apply {
            val item = widgetItems[position]
            val icon = if (item.spend) {
                R.drawable.dot_highlight
            } else {
                R.drawable.dot_default
            }
            setImageViewResource(R.id.widget_item, icon)
        }
    }

    override fun getCount(): Int = widgetItems.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {
        widgetItems.clear()
    }

    data class WidgetItem(val spend: Boolean)


    companion object {
        const val TAG = "WidgetService"
    }
}