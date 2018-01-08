package com.colavo.android.ui

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.MenuItem
import com.colavo.android.R
import kotlinx.android.synthetic.main.activity_salon_main.*
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.ui.adapter.SectionsPagerAdapter
import com.colavo.android.ui.login.LoginActivity
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.base.view.BasePresenterActivity
import com.colavo.android.main.presenter.MainContract
import com.colavo.android.utils.toast
import com.colavo.android.view.main.presenter.MainPresenter
import com.simmorsal.recolor_project.ReColor
import com.colavo.android.utils.Logger
import com.google.firebase.database.FirebaseDatabase
import com.roughike.bottombar.BottomBarTab
import com.google.gson.Gson




class   SalonMainActivity : BasePresenterActivity<MainContract.View
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
        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            Logger.log(FirebaseDatabase.getInstance().toString())
        }catch (e : Exception){
            Logger.log("SetPresistenceEnabled:Fail"+FirebaseDatabase.getInstance().toString())
            e.printStackTrace()
        }

        salonModel = intent.extras.getSerializable(SalonListActivity.EXTRA_SALONMODDEL) as SalonModel

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        val mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)

        // Set up the ViewPager with the sections adapter.
        container.setAdapter(mSectionsPagerAdapter)
        container.addOnPageChangeListener(this)
        container.setCurrentItem(0)

        // prevBottomNavigation = bottomBar.getItem(0)
        container.setOffscreenPageLimit(4)
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

    public fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(SalonListActivity.EXTRA_SIGN_OUT, true)
        startActivity(intent)
        finish()
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

    override fun updatePager() { //WTF
        val pagerAdapter : PagerAdapter = container?.getAdapter()!!

        if (pagerAdapter != null) {
            pagerAdapter!!.notifyDataSetChanged()
            Logger.log("SalonMainActivity : updatePager : notifyDataSetChanged !")
        }

        val nearby = bottomBar.getTabWithId(R.id.action_stats)
        nearby.setBadgeCount(5)
    }
    /*
      override fun onSaveInstanceState(saveBundle: Bundle?) {
        val bundle = Bundle()
          val salonModel = intent.extras.getSerializable(SalonListActivity.EXTRA_SALONMODDEL) as SalonModel
          bundle.putSerializable(SAVED_SALON_STATE, salonModel)
  *//*
        bundle.putExtra("데이터키값", salon)
        //저장할 데이터를 번들객체에 저장해서, 다시 복구시 넘어갈 번들안에 계층적으로 저장했다가..
        //복구시에 번들 안에서 다시 이 저장된 번들객체를 추출해서 처리하게 됩니다.
*//*
        saveBundle?.putParcelable(BUNDLE_KEY, bundle)

        Logger.log("onSaveInstanceState : ${salonModel.name} ")
        super.onSaveInstanceState(saveBundle)
    }
*/

    override fun onDestroy() {
        super.onDestroy()
        container.removeOnPageChangeListener(this)

    }

    override fun onPause() {
        super.onPause()
/*        val pref : SharedPreferences  = getSharedPreferences(SAVED_PREFS, 0)
        val edit : SharedPreferences.Editor = pref.edit()
        val mSalonModel = intent.extras.getSerializable(SalonListActivity.EXTRA_SALONMODDEL) as SalonModel
        val gson = Gson()
        val json = gson.toJson(mSalonModel)
        Logger.log("onPause : ${mSalonModel.name} ${mSalonModel.id} ")
       // val serializedSalonModel = salonModel.toString()
        edit.putString(SAVED_SALON_ID, json)

        edit.apply()*/
    }

    companion object {
        val SAVED_PREFS: String = "SAVED_PREFS"
        val SAVED_SALON_ID: String = "SALON_ID"
        val SAVED_SALON_STATE: String = "SALON_STATE"
        val BUNDLE_KEY: String = "BUNDLE_KEY"
        var salonModel = SalonModel()
    }

}
