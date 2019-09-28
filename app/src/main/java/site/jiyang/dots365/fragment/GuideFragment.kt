package site.jiyang.dots365.fragment

import android.os.Bundle
import android.view.View
import site.jiyang.dots365.R

/**
 * Create by jy on 2019-09-28
 */
class GuideFragment private constructor() : BaseFragment() {
    override fun layoutId(): Int = R.layout.fragment_guide

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

    companion object {
        fun instance(): GuideFragment {
            return GuideFragment()
        }
    }
}