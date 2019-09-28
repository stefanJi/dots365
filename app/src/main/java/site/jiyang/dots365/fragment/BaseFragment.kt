package site.jiyang.dots365.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Create by jy on 2019-09-28
 */
abstract class BaseFragment : Fragment() {

    @LayoutRes
    abstract fun layoutId(): Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        require(layoutId() != -1) { "layoutId illegal" }
        return inflater.inflate(layoutId(), container, false)
    }

}