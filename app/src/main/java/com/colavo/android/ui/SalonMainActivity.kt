package com.colavo.android.ui

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.view.MenuItem
import com.colavo.android.R
import kotlinx.android.synthetic.main.activity_salon_main.*
import android.support.v4.view.ViewPager
import android.view.Menu
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

        // Listener for bottomBar
        bottomBar.setOnTabSelectListener {  tabId ->
            when (tabId){
                R.id.action_calendar -> {
                    container.setCurrentItem(0)
                    container.setPagingEnabled(false)
                }
                R.id.action_checkouts -> {
                    container.setCurrentItem(1)
                    container.setPagingEnabled(true)
                }
                R.id.action_stats -> {
                    container.setCurrentItem(2)
                    container.setPagingEnabled(true)
                }
                R.id.action_customers -> {
                    container.setCurrentItem(3)
                    container.setPagingEnabled(true)
                }
                R.id.action_settings -> {
                    container.setCurrentItem(4)
                    container.setPagingEnabled(true)
                }
            }
        }


        presenter?.setSectionPagerModel(mSectionsPagerAdapter)
        presenter?.loadSectionPagerItem() //WTF

  //      loadCustomerFragment(savedInstanceState)

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
