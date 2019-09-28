package site.jiyang.dots365

import android.appwidget.AppWidgetManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import site.jiyang.dots365.fragment.ConfigFragment
import site.jiyang.dots365.fragment.GuideFragment

/**
 * Create by jy on 2019-09-28
 */
class DotsWidgetActivity : AppCompatActivity() {

    private var appWidgetId: Int = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_dots365)

        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        Log.d(TAG, "[onCreate] appWidgetId: $appWidgetId")

        val fragment = if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            GuideFragment.instance()
        } else {
            ConfigFragment.instance(appWidgetId)
        }

        supportFragmentManager.beginTransaction().add(R.id.container, fragment).commit()
    }

    companion object {
        const val TAG = "DotsConfigActivity"
    }
}