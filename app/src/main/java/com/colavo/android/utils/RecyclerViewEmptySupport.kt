package com.colavo.android.utils

import android.content.Context
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView.AdapterDataObserver
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.ProgressBar
import com.colavo.android.R
import com.colavo.android.common.MyTextView
import com.simplecityapps.recyclerview_fastscroll.views.FastScrollRecyclerView
import kotlinx.android.synthetic.main.base_empty.view.*



class RecyclerViewEmptySupport : FastScrollRecyclerView {
    private var emptyView: View? = null
//    val mProgressBar: ProgressBar? = findViewById<ProgressBar>(R.id.progressBar)

    init {

    }

    private val observer = object : AdapterDataObserver() {
        override fun onChanged() {
            checkIfEmpty()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
//            mProgressBar!!.visibility = View.GONE
            checkIfEmpty()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            checkIfEmpty()
        }
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    internal fun checkIfEmpty() {
        if (emptyView != null && adapter != null) {
            val emptyViewVisible = adapter.itemCount == 0
            emptyView!!.setVisibility(if (emptyViewVisible) View.VISIBLE else View.GONE)
            //empty_group?.setVisibility(View.VISIBLE)

            visibility = if (emptyViewVisible) View.GONE else View.VISIBLE
        }


    }

    override fun setAdapter(adapter: RecyclerView.Adapter<*>?) {
        val oldAdapter = getAdapter()
        oldAdapter?.unregisterAdapterDataObserver(observer)
        super.setAdapter(adapter)
        adapter?.registerAdapterDataObserver(observer)

        checkIfEmpty()
    }

    fun setEmptyView(emptyView: View) {
        this.emptyView = emptyView
        checkIfEmpty()

    }



}