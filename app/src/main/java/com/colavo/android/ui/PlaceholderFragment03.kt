package com.colavo.android.ui

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.colavo.android.R
import com.colavo.android.R.menu.menu_stat
import com.colavo.android.base.BaseFragment
import kotlinx.android.synthetic.main.toolbar.*
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView
import lecho.lib.hellocharts.util.ChartUtils
import lecho.lib.hellocharts.view.PieChartView
import java.util.*


/**
 * Created by macbookpro on 2017. 9. 13..
 */
class PlaceholderFragment03 : BaseFragment() {

    private var chart: PieChartView? = null
    private var data: PieChartData? = null

    private var hasLabels = false
    private var hasLabelsOutside = false
    private var hasCenterCircle = false
    private var hasCenterText1 = false
    private var hasCenterText2 = false
    private var isExploded = false
    private var hasLabelForSelected = false
/*    override fun getLayout(position: Int) = when (position) {
        1 -> R.layout.fragment_01
        2 -> R.layout.fragment_02
        3 -> R.layout.fragment_03
        4 -> R.layout.fragment_04
        else -> R.layout.fragment_05
    }*/
    override fun getLayout() = R.layout.fragment_03

    companion object {
        fun newInstance() = PlaceholderFragment03()
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar?.setTitle (R.string.bottom_navi_3)
        toolBar?.inflateMenu(menu_stat)

        val values = ArrayList<PointValue>()
        values.add(PointValue(0f, 2f))
        values.add(PointValue(1f, 4f))
        values.add(PointValue(2f, 3f))
        values.add(PointValue(3f, 4f))

        //In most cased you can call data model methods in builder-pattern-like manner.
        val line = Line(values).setColor(Color.BLUE).setCubic(true)
        val lines = ArrayList<Line>()
        lines.add(line)

        val data = LineChartData()
        data.lines = lines



    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_menu_stat -> {
                //TODO monthly stat
                return true
            }

        }

        return super.onOptionsItemSelected(item)
    }


}