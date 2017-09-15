package com.colavo.android.view.main.presenter

import com.colavo.android.base.presenter.AbstractPresenter
import com.colavo.android.main.presenter.MainContract
import com.colavo.android.ui.adapter.SectionsPagerModel

/**
 * Created by macbookpro on 2017. 9. 14..
 */

class MainPresenter : AbstractPresenter<MainContract.View>(), MainContract.Presenter {

    private var sectionsPagerModel: SectionsPagerModel? = null

    override fun setSectionPagerModel(sectionPagerModel: SectionsPagerModel) {
        this.sectionsPagerModel = sectionPagerModel
    }

    override fun loadSectionPagerItem() { //WTF
        sectionsPagerModel!!.setListItem(1)
        sectionsPagerModel!!.setListItem(2)
        sectionsPagerModel!!.setListItem(3)
        sectionsPagerModel!!.setListItem(4)
        sectionsPagerModel!!.setListItem(5)

        //getView().updatePager()
        view!!.updatePager() //WTF
    }

}