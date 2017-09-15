package com.colavo.android.main.presenter

import com.colavo.android.base.BaseView
import com.colavo.android.base.presenter.BasePresenter
import com.colavo.android.ui.adapter.SectionsPagerModel

/**
 * Created by macbookpro on 2017. 9. 14..
 */
interface MainContract {

    interface View : BaseView {

        fun updatePager()
    }

    interface Presenter : BasePresenter<View> {

        fun setSectionPagerModel(sectionPagerModel: SectionsPagerModel)

        fun loadSectionPagerItem()
    }
}