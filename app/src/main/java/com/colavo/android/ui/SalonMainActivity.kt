package com.colavo.android.ui

import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.MenuItem
import com.colavo.android.R
import kotlinx.android.synthetic.main.activity_salon_main.*
import android.support.v4.view.ViewPager
import android.view.Menu
import android.view.View
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.ui.adapter.SectionsPagerAdapter
import com.colavo.android.ui.login.LoginActivity
import com.colavo.android.ui.salons.SalonListActivity
import kotlinx.android.synthetic.main.toolbar.*
import com.colavo.android.base.view.BasePresenterActivity
import com.colavo.android.main.presenter.MainContract
import com.colavo.android.ui.adapter.SectionsPagerModel
import com.colavo.android.utils.toast
import com.colavo.android.view.main.presenter.MainPresenter
import com.simmorsal.recolor_project.ReColor
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import com.colavo.android.weekview.WeekView
import com.roughike.bottombar.BottomBarTab
import kotlinx.android.synthetic.main.activity_salon_main.view.*
import kotlinx.android.synthetic.main.fragment_01.*


class SalonMainActivity : BasePresenterActivity<MainContract.View
        , MainContract.Presenter>(), MainContract.View, ViewPager.OnPageChangeListener {

    override fun onError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showToast(event: String) {
        toast(event)
    }

    override fun showSnackbar(event: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    // Set up the ViewPager with the sections adapter.

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    /**
     * The [ViewPager] that will host the section contents.
     */

    override fun onCreatePresenter(): MainContract.Presenter? {
        return MainPresenter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salon_main)

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.setAdapter(mSectionsPagerAdapter)
        container.addOnPageChangeListener(this)
        container.setCurrentItem(0)

        // prevBottomNavigation = bottomBar.getItem(0)
        container.setOffscreenPageLimit(5)
        // for status bar color change
        val decor = window.decorView
        val tab1 : BottomBarTab = bottomBar.getTabAtPosition(0)
        tab1.setBarColorWhenSelected(Color.WHITE)
        val tab2 : BottomBarTab = bottomBar.getTabAtPosition(1)
        tab2.setBarColorWhenSelected(Color.WHITE)
        val tab3 : BottomBarTab = bottomBar.getTabAtPosition(2)
        tab3.setBarColorWhenSelected(Color.parseColor("#0B0F28"))
        tab3.setActiveColor(Color.WHITE)
        val tab4 : BottomBarTab = bottomBar.getTabAtPosition(3)
        tab4.setBarColorWhenSelected(Color.WHITE)
        val tab5 : BottomBarTab = bottomBar.getTabAtPosition(4)
        tab5.setBarColorWhenSelected(Color.WHITE)

        // Listener for bottomBar
        bottomBar.setOnTabSelectListener {  tabId ->
            when (tabId){
                R.id.action_calendar -> {
                    container.setCurrentItem(0)
                    container.setPagingEnabled(false)
                    setLightUI(true)
                }
                R.id.action_checkouts -> {
                    container.setCurrentItem(1)
                    container.setPagingEnabled(true)
                    setLightUI(true)
                }
                R.id.action_stats -> {
                    container.setCurrentItem(2)
                    container.setPagingEnabled(true)
                    setLightUI(false)
                }
                R.id.action_customers -> {
                    container.setCurrentItem(3)
                    container.setPagingEnabled(true)
                    setLightUI(true)
                }
                R.id.action_settings -> {
                    container.setCurrentItem(4)
                    container.setPagingEnabled(true)
                    setLightUI(true)
                }
            }
        }


        presenter?.setSectionPagerModel(mSectionsPagerAdapter)
        presenter?.loadSectionPagerItem() //WTF

  //      loadCustomerFragment(savedInstanceState)

    }

    private fun setLightUI(light: Boolean) {
        if (light == true) {
            ReColor(this).setStatusBarColor(null, "FEFEFE", 500)
            ReColor(this).setNavigationBarColor(null, "FEFEFE", 500)
            bottomBar.setBackgroundColor(Color.parseColor("#FEFEFE"))
        }
        else
        {
            ReColor(this).setStatusBarColor(null, "#0B0F28", 500)
            ReColor(this).setNavigationBarColor(null, "#0B0F28", 500)
            bottomBar.setBackgroundColor(Color.parseColor("#0B0F28"))
            //bottomBar.setBackgroundColor(Color.parseColor("#0C102B"))
        }
        setLightStatusBar(light)
        setLightNavigationBar(light)

    }

    fun setLightStatusBar(lightStatusBar: Boolean) {
        setLightBar(lightStatusBar, View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
    }

    fun setLightNavigationBar(lightNavigationBar: Boolean) {
        setLightBar(lightNavigationBar, View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)
    }

    private fun setLightBar(light: Boolean, systemUiFlag: Int) {
        var vis = window.decorView.systemUiVisibility
        if (light) {
            vis = vis or systemUiFlag
        } else {
            vis = vis and systemUiFlag.inv()
        }
        window.decorView.systemUiVisibility = vis
    }


/*    private fun loadCustomerFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .add(R.layout.fragment_04, PlaceholderFragment04(), PlaceholderFragment04::class.simpleName)
                    .commit()
        }
    }*/

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(SalonListActivity.EXTRA_SIGN_OUT, true)
        startActivity(intent)
        finish()
    }

    override fun updatePager() { //WTF
        val pagerAdapter : PagerAdapter = container?.getAdapter()!!
        if (pagerAdapter != null) pagerAdapter.notifyDataSetChanged()
        val nearby = bottomBar.getTabWithId(R.id.action_stats)
        nearby.setBadgeCount(5)
        //mSectionsPagerAdapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        container.removeOnPageChangeListener(this)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }

    /**
     * BottomNavigation Prev
     */
    private var prevBottomNavigation: MenuItem? = null

    override fun onPageSelected(position: Int) {
/*        if (prevBottomNavigation != null) {
            prevBottomNavigation!!.isChecked = false
        }
        prevBottomNavigation = bottomBar.getTabAtPosition(position)
        prevBottomNavigation!!.isChecked = true*/
        bottomBar.selectTabAtPosition(position)
    }

    override fun onPageScrollStateChanged(state: Int) {

    }



}
