package com.colavo.android.ui

import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.afollestad.materialdialogs.MaterialDialog
import com.colavo.android.App
import com.colavo.android.R
import com.colavo.android.R.menu.menu_customer
import com.colavo.android.base.BaseFragment
import com.colavo.android.entity.customer.CustomerModel
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.presenters.customer.CustomerPresenterImpl
import com.colavo.android.ui.adapter.CustomerAdapter
import com.colavo.android.ui.customerdetail.CustomerDetailFragment
import com.colavo.android.ui.customer.CustomerlistView
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_04.*
import javax.inject.Inject

import com.colavo.android.ui.animations.DetailsTransition
import com.colavo.android.ui.customerdetail.CustomerCreateFragment
import com.colavo.android.utils.toast
import kotlinx.android.synthetic.main.base_empty.*
import kotlinx.android.synthetic.main.customer_item.view.*
import java.io.ByteArrayOutputStream
import android.support.v4.view.MenuItemCompat
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection
import java.util.*


class PlaceholderFragment04 : BaseFragment(), CustomerlistView
        , CustomerAdapter.OnItemClickListener, SearchView.OnQueryTextListener {

    /* Transition */
    private val MOVE_DEFAULT_TIME: Long = 1000
    private val FADE_DEFAULT_TIME: Long = 300
    private val mDelayedTransactionHandler = Handler()
    private val mRunnable = Runnable { this.performTransition() }

    @Inject
    lateinit var customerPresenter: CustomerPresenterImpl
    lateinit var customerAdapter: CustomerAdapter

    override fun getLayout() = R.layout.fragment_04

    private var sectionAdapter: SectionedRecyclerViewAdapter? = null

    companion object {
        fun newInstance() = PlaceholderFragment04()
        val EXTRA_SALON: String = "CUSTOMER"
    }

    private val progressDialog: MaterialDialog by lazy {
        MaterialDialog.Builder(this.context!!)
                .title(R.string.customers_loading)
                .content(R.string.wait)
                .progress(true, 0)
                .build()
    }
    @Inject
    lateinit var firebaseAuth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (context!!.applicationContext as App).addCustomerComponent().inject(this)
   // this works     (context.applicationContext as App).addCustomerComponent().inject(this)
        //   (context.applicationContext as App).addCustomerComponent().inject(this)
        //    (getActivity().getApplication() as App).addCustomerComponent().inject(this)


    }


    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).setSupportActionBar(toolBar_customer)

        val title = R.string.bottom_navi_4
        toolBar_customer.setTitle (title)

        //TODO update number of customers
    }

    override fun updateNumberofCustomer(){
        val title : String = getString(R.string.customer) + " (${customerAdapter.itemCount})"
        toolBar_customer.setTitle(title)

        Logger.log("TOOLBAR UPDATED : ${customerAdapter.itemCount}")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgress()

        toolBar_customer.inflateMenu(menu_customer)

        if (empty_customer.visibility == View.VISIBLE) ripplebg.startRippleAnimation()
        else ripplebg.stopRippleAnimation()

        val salon = (activity as AppCompatActivity).intent.extras.getSerializable(SalonListActivity.EXTRA_SALONMODDEL) as SalonModel


//        sectionAdapter = SectionedRecyclerViewAdapter()

/*
        var alphabet = 'A'
        while (alphabet <= 'Z') {
            val contacts = getContactsWithLetter(alphabet)

            if (contacts.size > 0) {
                val contactsSection = ContactsSection(alphabet.toString(), contacts)
                sectionAdapter!!.addSection(contactsSection)
            }
            alphabet++
        }
*/

        customers_recyclerView.layoutManager = LinearLayoutManager(this.context)

        customerAdapter = CustomerAdapter(this, mutableListOf<CustomerModel>())
        customers_recyclerView.adapter = customerAdapter
        customers_recyclerView.setItemViewCacheSize(20)
        customers_recyclerView.isDrawingCacheEnabled = true
        customers_recyclerView.setHasFixedSize(true)

        customerPresenter.attachView(this)
        customerPresenter.initialize(salon.id)




        // Fab button
        fab_customer.setOnClickListener(View.OnClickListener { view ->
            showCreateCustomerFragment()
        })
        // Pull to refresh
        swipe_layout_customer.setOnRefreshListener{
            fun onRefresh(){
                showSnackbar("Refresh start : onRefresh Customer")
                customerAdapter.notifyDataSetChanged()
                updateNumberofCustomer()

                val ft = fragmentManager?.beginTransaction()
                ft?.detach(this)?.attach(this)?.commit()

                showSnackbar("Refresh start : onRefresh done")
            }
            swipe_layout_customer.setRefreshing(false)
            Logger.log("Refresh Done")
        }

    }

    override fun refresh(salonId: String, customerId: String)  {
        val transaction: android.support.v4.app.FragmentTransaction? = fragmentManager?.beginTransaction()
        transaction?.detach(this)?.attach(this)?.commit()
        showSnackbar("refresh()")
    }

    override fun setCustomerlist(customerEntities: List<CustomerModel>?) {
        //customers_recyclerView.adapter = CustomerAdapter(this, customerEntities!!.toMutableList()) //todo
    }

    override fun showCreateCustomerFragment() {
        //todo
        val newFragment = CustomerCreateFragment()

        val bundle = Bundle(2)
        bundle.putString("SENDER","create")
        newFragment.setArguments(bundle)

        val transaction = fragmentManager!!.beginTransaction()
        transaction.setCustomAnimations(R.animator.fade_in, R.animator.fade_out, R.animator.parent_enter, R.animator.parent_exit)
        transaction.replace(R.id.containerLayout, newFragment) //container
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun openCustomerFragment(customerModel: CustomerModel) {
        //todo
/*
        val intent = Intent(this, SalonMainActivity::class.java)
        intent.putExtra(SalonListActivity.EXTRA_SALONMODDEL, salonModel)
        startActivity(intent)

        */
    }


    override fun addCustomer(customerEntity: CustomerModel) {
        hideProgress()
        customerAdapter.items.add(customerEntity)
        customerAdapter.notifyItemInserted(customerAdapter.itemCount)
        updateNumberofCustomer()
        Logger.log("Customer added : ${customerEntity.name} (${customerAdapter.itemCount})")
    }

    override fun changeCustomer(customerEntity: CustomerModel) {
        Logger.log("Customer changed")

 /*       val position = (customers_recyclerView.adapter as CustomerAdapter).items.indexOfFirst { it.uid.equals(customerEntity.uid) }
        (customers_recyclerView.adapter as CustomerAdapter).items[position] = customerEntity
        customers_recyclerView.adapter.notifyItemChanged(position)*/


        val position = customerAdapter.items.indexOfFirst { it.key.equals(customerEntity.key) }
        customerAdapter.items[position] = customerEntity
        customerAdapter.notifyItemChanged(position)
        updateNumberofCustomer()
    }

    override fun removeCustomer(customerEntity: CustomerModel) {
        Logger.log("Customer removed")

/*        val position = customerAdapter.items.indexOfFirst { it.uid.equals(customerEntity) }
        customerAdapter.items.removeAt(position)
        customerAdapter.notifyItemRemoved(position)*/
        customerAdapter.items.removeAll{it.key.equals(customerEntity.key)}
        customerAdapter.notifyDataSetChanged()
        updateNumberofCustomer()
    }


    override fun showProgress() {
        empty_progress.visibility = View.VISIBLE
        val handler = Handler()
        handler.postDelayed({
            empty_group?.visibility = View.VISIBLE
            empty_progress.visibility = View.GONE
        }, 700)

        //empty_group.visibility = View.GONE
        //progressDialog.show()
    }

    override fun hideProgress() {
        empty_progress.visibility = View.GONE
        empty_group.visibility = View.VISIBLE
        //progressDialog.hide()
    }

    override fun onItemClicked(item: CustomerModel, position: Int, v: View) {
        Toast.makeText(context, "Clicked ${item.name} : ${position}" , Toast.LENGTH_LONG).show()

  //      CustomerDetailFragment().show(activity.getSupportFragmentManager(), R.id.bottomsheet)

        v.customer_image.buildDrawingCache()
        val bitmap = v.customer_image.getDrawingCache()
        val bs = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, bs)
        val byteArray = bs.toByteArray()



        val newFragment = CustomerDetailFragment()

        val bundle = Bundle(3)
        bundle.putSerializable(PlaceholderFragment02.EXTRA_CHECKOUT, item)
        bundle.putString("SENDER","customer")
        bundle.putByteArray("BYTE", byteArray)
        newFragment.setArguments(bundle)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            newFragment.setSharedElementEnterTransition(DetailsTransition())
            newFragment.setEnterTransition(android.transition.Fade())
            exitTransition = android.transition.Fade()
            newFragment.setSharedElementReturnTransition(DetailsTransition())
        }

        val transaction = fragmentManager!!.beginTransaction()
//        transaction.addSharedElement(customer_name,"customer_name" )//logoTransitionName.toString()
        transaction.replace(R.id.customer_list_holder, newFragment) //container
        transaction.addToBackStack(null)
        transaction.commit()
     //   mDelayedTransactionHandler.postDelayed(mRunnable, 1000);
    }

    private fun performTransition() {

/*        val previousFragment = fragmentManager.findFragmentById(R.id.customer_list_holder)
        val nextFragment = CustomerDetailFragment.newInstance()

        val fragmentTransaction = fragmentManager.beginTransaction()

        // 1. Exit for Previous Fragment
        val exitFade = android.support.transition.Fade()
        exitFade.setDuration(FADE_DEFAULT_TIME)
        previousFragment.exitTransition = exitFade

        // 2. Shared Elements Transition
        val enterTransitionSet = android.support.transition.TransitionSet()
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move))
        enterTransitionSet.setDuration(MOVE_DEFAULT_TIME)
        enterTransitionSet.setStartDelay(FADE_DEFAULT_TIME)
        nextFragment.setSharedElementEnterTransition(enterTransitionSet)

        // 3. Enter Transition for New Fragment
        val enterFade = android.support.transition.Fade()
        enterFade.setStartDelay(MOVE_DEFAULT_TIME + FADE_DEFAULT_TIME)
        enterFade.setDuration(FADE_DEFAULT_TIME)
        nextFragment.setEnterTransition(enterFade)

        val logo = ButterKnife.findById(this, R.id.fragment1_logo)
        fragmentTransaction.addSharedElement(logo, logo.getTransitionName())
        fragmentTransaction.replace(R.id.fragment_container, nextFragment)
        fragmentTransaction.commitAllowingStateLoss()*/
    }


    override fun onLongItemClicked(item: CustomerModel) {

    }

    override fun onError(throwable: Throwable) {
        showToast(throwable.toString())
    }

    override fun showToast(event: String) {
        context!!.toast(event)
    }

    override fun showSnackbar(event: String) {
//        customers_recyclerView.showSnackBar(event)
    }

    override fun onDestroy() {
        super.onDestroy()
        //clearCustomerComponent()
        mDelayedTransactionHandler.removeCallbacks(mRunnable);
        customerPresenter.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_customer, menu)

        val item = menu.findItem(R.id.action_search)
        val searchView = MenuItemCompat.getActionView(item) as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextChange(query: String): Boolean {

        // getSectionsMap requires library version 1.0.4+
        for (section in sectionAdapter?.getSectionsMap()!!.values) {
            if (section is FilterableSection) {
                (section as FilterableSection).filter(query)
            }
        }
        sectionAdapter?.notifyDataSetChanged()

        return true
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    private fun getContactsWithLetter(letter: Char): List<String> {
        val contacts = ArrayList<String>()

        for (contact in resources.getStringArray(R.array.names)) {
            if (contact[0] == letter) {
                contacts.add(contact)
            }
        }

        return contacts
    }


    private inner class ContactsSection internal constructor(internal var title: String, internal var list: List<String>)
        : StatelessSection(SectionParameters.Builder(R.layout.section_ex7_item)
            .headerResourceId(R.layout.section_ex7_header)
            .build()), FilterableSection {
        internal var filteredList: MutableList<String>

        init {
            this.filteredList = ArrayList(list)
        }


        override fun getContentItemsTotal(): Int {
            return filteredList.size
        }

        override fun getItemViewHolder(view: View): RecyclerView.ViewHolder {
            return ItemViewHolder(view)
        }

        override fun onBindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val itemHolder = holder as ItemViewHolder

            val name = filteredList[position]

            itemHolder.tvItem.setText(name)
            //itemHolder.imgItem.setImageResource(if (name.hashCode() % 2 == 0) R.drawable.ic_face_black_48dp else R.drawable.ic_tag_faces_black_48dp)

            itemHolder.rootView.setOnClickListener(View.OnClickListener { Toast.makeText(context, String.format("Clicked on position #%s of Section %s", sectionAdapter!!.getPositionInSection(itemHolder.getAdapterPosition()), title), Toast.LENGTH_SHORT).show() })
        }

        override fun getHeaderViewHolder(view: View): RecyclerView.ViewHolder {
            return HeaderViewHolder(view)
        }

        override fun onBindHeaderViewHolder(holder: RecyclerView.ViewHolder?) {
            val headerHolder = holder as HeaderViewHolder?

            headerHolder!!.tvTitle.setText(title)
        }

        override fun filter(query: String) {
            if (TextUtils.isEmpty(query)) {
                filteredList = ArrayList(list)
                this.isVisible = true
            } else {
                filteredList.clear()
                for (value in list) {
                    if (value.toLowerCase().contains(query.toLowerCase())) {
                        filteredList.add(value)
                    }
                }

                this.isVisible = !filteredList.isEmpty()
            }
        }
    }

    private inner class HeaderViewHolder internal constructor(view: View) : RecyclerView.ViewHolder(view) {

        public val tvTitle: TextView

        init {

            tvTitle = view.findViewById(R.id.tvTitle) as TextView
        }
    }

    private inner class ItemViewHolder internal constructor(val rootView: View) : RecyclerView.ViewHolder(rootView) {
        private val imgItem: ImageView
        val tvItem: TextView

        init {
            imgItem = rootView.findViewById(R.id.imgItem) as ImageView
            tvItem = rootView.findViewById(R.id.tvItem) as TextView
        }
    }

    internal interface FilterableSection {
        fun filter(query: String)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_create_customer -> {
                showCreateCustomerFragment()
                return true
            }
      /*      R.id.action_create_event ->{
                //               val dialogFrag = CreateFabFragment.newInstance()
//                dialogFrag.setParentFab(fab_calendar)
//                dialogFrag.show((activity as AppCompatActivity).getSupportFragmentManager(), dialogFrag.getTag())
                return true
            }
            R.id.action_day_view -> {
                if (mWeekViewType != TYPE_DAY_VIEW) {
                    item.isChecked = !item.isChecked
                    mWeekViewType = TYPE_DAY_VIEW
                    weekView.numberOfVisibleDays = 1

                    // Lets change some dimensions to best fit the view.
                    weekView.columnGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt()
                    weekView.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt()
                    weekView.eventTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt()
                }
                return true
            }
            R.id.action_three_day_view -> {
                if (mWeekViewType != TYPE_THREE_DAY_VIEW) {
                    item.isChecked = !item.isChecked
                    mWeekViewType = TYPE_THREE_DAY_VIEW
                    weekView.numberOfVisibleDays = 3

                    // Lets change some dimensions to best fit the view.
                    weekView.columnGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8f, resources.displayMetrics).toInt()
                    weekView.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt()
                    weekView.eventTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12f, resources.displayMetrics).toInt()
                }
                return true
            }
            R.id.action_week_view -> {
                if (mWeekViewType != TYPE_WEEK_VIEW) {
                    item.isChecked = !item.isChecked
                    mWeekViewType = TYPE_WEEK_VIEW
                    weekView.numberOfVisibleDays = 7

                    // Lets change some dimensions to best fit the view.
                    weekView.columnGap = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2f, resources.displayMetrics).toInt()
                    weekView.textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt()
                    weekView.eventTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10f, resources.displayMetrics).toInt()
                }
                return true
            }

            R.id.action_sign_out -> {
                openLoginActivity()
                return true
            }*/

        }

        return super.onOptionsItemSelected(item)
    }
}
