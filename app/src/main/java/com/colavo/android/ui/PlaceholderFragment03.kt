package com.colavo.android.ui

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.AnimationDrawable
import android.hardware.SensorManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import com.colavo.android.R
import com.colavo.android.R.menu.menu_stat
import com.colavo.android.base.BaseFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.toolbar.*
import java.util.*
import android.widget.LinearLayout
import com.colavo.android.R.string.loading
import butterknife.BindView
import android.widget.TextView
import android.widget.FrameLayout
import android.widget.ImageView
import com.colavo.android.meteor.MeteorView
import com.colavo.android.ui.checkout.ColavoAnimatedTranslationUpdater
import com.schibsted.spain.parallaxlayerlayout.AnimatedTranslationUpdater
import com.schibsted.spain.parallaxlayerlayout.SensorTranslationUpdater
import kotlinx.android.synthetic.main.fragment_03.*


/**
 * Created by macbookpro on 2017. 9. 13..
 */
class PlaceholderFragment03 : BaseFragment() {
    override fun refresh(salonId: String, customerId: String)  {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

/*    override fun getLayout(position: Int) = when (position) {
        1 -> R.layout.fragment_01
        2 -> R.layout.fragment_02
        3 -> R.layout.fragment_03
        4 -> R.layout.fragment_04
        else -> R.layout.fragment_05
    }*/
    /*@BindView(R.id.img_background_star)
    var imgBackgroundStar: ImageView? = null
    @BindView(R.id.img_meteor0)
    var imgMeteor0: MeteorView? = null
    @BindView(R.id.img_meteor1)
    var imgMeteor1: MeteorView? = null
    @BindView(R.id.img_meteor2)
    var imgMeteor2: MeteorView? = null
    @BindView(R.id.img_meteor3)
    var imgMeteor3: MeteorView? = null
    @BindView(R.id.img_background_tree)
    var imgBackgroundTree: ImageView? = null
    @BindView(R.id.img_background_moon)
    var imgBackgroundMoon: ImageView? = null
    @BindView(R.id.img_background_fire)
    var imgBackgroundFire: ImageView? = null
    @BindView(R.id.button)
    var button: FrameLayout? = null*/


    override fun getLayout() = R.layout.fragment_03

    companion object {
        fun newInstance() = PlaceholderFragment03()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar?.setTitle (R.string.bottom_navi_3)
        toolBar?.inflateMenu(menu_stat)

  /*      val values = ArrayList<PointValue>()
        values.add(PointValue(0f, 2f))
        values.add(PointValue(1f, 4f))
        values.add(PointValue(2f, 3f))
        values.add(PointValue(3f, 4f))

        //In most cased you can call data model methods in builder-pattern-like manner.
        val line = Line(values).setColor(Color.BLUE).setCubic(true)
        val lines = ArrayList<Line>()
        lines.add(line)

        val data = LineChartData()
        data.lines = lines*/

        val sensorTranslationUpdater = SensorTranslationUpdater(context)
        parallax.setTranslationUpdater(sensorTranslationUpdater)
         //parallax.setTranslationUpdater(ColavoAnimatedTranslationUpdater(0.1f))

        initView()
    }

    private fun initView() {
        Picasso.with(this.context).load(R.drawable.meteor_star).resize(900, 1600).centerCrop().into(img_background_star, object : Callback {
            override fun onSuccess() {
                val frameAnimation = img_background_fire.getDrawable() as AnimationDrawable
                frameAnimation.start()
            }

            override fun onError() {

            }
        })
        Picasso.with(this.context).load(R.drawable.meteor_icon_light_moon).resize(100, 100).centerCrop().into(img_background_moon)
        Picasso.with(this.context).load(R.drawable.meteor_background_main_back).resize(500, 322).centerCrop().into(img_background_tree)
        Picasso.with(this.context).load(R.drawable.meteor_icon_meteor).into(img_meteor0)
        Picasso.with(this.context).load(R.drawable.meteor_icon_meteor).into(img_meteor1)
        Picasso.with(this.context).load(R.drawable.meteor_icon_meteor).into(img_meteor2)
        Picasso.with(this.context).load(R.drawable.meteor_icon_meteor).into(img_meteor3)

    }

    override fun onResume() {
        super.onResume()
        val sensorTranslationUpdater = SensorTranslationUpdater(activity)
        sensorTranslationUpdater.registerSensorManager()
    }
    override fun onPause() {
        super.onPause()
        val sensorTranslationUpdater = SensorTranslationUpdater(activity)
        sensorTranslationUpdater.unregisterSensorManager()
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