package site.jiyang.dots365

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_dots365.*
import kotlinx.android.synthetic.main.dots_content.*

/**
 * Create by jy on 2019-09-28
 */
class DotsWidgetActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_dots365)
        tip.text = Html.fromHtml(getString(R.string.tip))

        val dotDate = DateCalculator.get()

        tvYear.text = "${dotDate.year}"
        tvPercent.text = dotDate.percent
        tvSpend.text = "${dotDate.dayOfYear}/${dotDate.days}"

        val adapter = Adapter(dotDate)
        lv_days.adapter = adapter

        btn_home_page.setOnClickListener {
            Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/stefanJi/dots365")).apply {
                startActivity(this)
            }
        }
    }

    companion object {
        const val TAG = "DotsConfigActivity"
    }
}

class Adapter(private val dotDate: DotDate) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var holder = convertView?.tag as? Holder
        var v = convertView
        if (holder == null) {
            v = LayoutInflater.from(parent.context).inflate(R.layout.dot_row, parent, false)
            holder = Holder(DotsWidgetRemoteViewFactory.DOTS.map {
                v.findViewById<ImageView>(it)
            })
            v.tag = holder
        }

        val month = dotDate.months[position]
        when {
            month.total == 31 -> {
                holder.days[0].visibility = View.VISIBLE
                holder.days[1].visibility = View.VISIBLE
                holder.days[2].visibility = View.VISIBLE
            }
            month.total == 30 -> {
                holder.days[0].visibility = View.GONE
            }
            month.total == 29 -> {
                holder.days[0].visibility = View.GONE
                holder.days[1].visibility = View.GONE
            }
            month.total == 28 -> {
                holder.days[0].visibility = View.GONE
                holder.days[1].visibility = View.GONE
                holder.days[2].visibility = View.GONE
            }
        }

        val delta = 31 - month.total
        repeat(month.total) { i ->
            holder.days[i + delta].setImageResource(DrawableGetter.get(i, month))
        }
        return v!!
    }

    override fun getItem(position: Int): Any = dotDate.months[position]

    override fun getItemId(position: Int): Long = 0

    override fun getCount(): Int = dotDate.months.size

    data class Holder(val days: List<ImageView>)
}