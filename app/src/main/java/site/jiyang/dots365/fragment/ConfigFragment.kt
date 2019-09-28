package site.jiyang.dots365.fragment

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.fragment_config.*
import site.jiyang.dots365.DotsWidgetConfig
import site.jiyang.dots365.DotsWidgetProvider
import site.jiyang.dots365.R

/**
 * Create by jy on 2019-09-28
 */
class ConfigFragment private constructor() : BaseFragment() {
    override fun layoutId(): Int = R.layout.fragment_config

    private var appWidgetId = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        appWidgetId = arguments?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID)
            ?: AppWidgetManager.INVALID_APPWIDGET_ID
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            return
        }
        configWidget()
    }

    private fun configWidget() {
        val activity = requireActivity()
        val appWidgetManager: AppWidgetManager = AppWidgetManager.getInstance(activity)
        DotsWidgetProvider.updateWidget(
            requireContext(),
            appWidgetManager,
            appWidgetId,
            DotsWidgetConfig()
        )

        finishConfig.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                val resultValue =
                    Intent().apply {
                        putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    }
                activity.setResult(Activity.RESULT_OK, resultValue)
                activity.finish()
            }
        }
    }

    companion object {
        fun instance(appWidgetId: Int): ConfigFragment {
            val f = ConfigFragment()
            f.arguments = Bundle().apply {
                putInt(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            }
            return f
        }
    }
}