package com.colavo.android.ui.salons

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.InputType
import android.view.Menu
import android.view.MenuItem
import com.afollestad.materialdialogs.MaterialDialog
import com.colavo.android.App

import com.colavo.android.R
import com.colavo.android.base.BaseActivity
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.presenters.salons.SalonsPresenterImpl
import com.colavo.android.ui.SalonMainActivity
import com.colavo.android.ui.adapter.SalonsAdapter
import com.colavo.android.ui.login.LoginActivity
import com.colavo.android.utils.Logger
import com.colavo.android.utils.showSnackBar
import com.colavo.android.utils.toast
import kotlinx.android.synthetic.main.activity_salons.*
import kotlinx.android.synthetic.main.content_salons.*
import javax.inject.Inject
import com.google.firebase.auth.FirebaseAuth


class SalonListActivity : BaseActivity()
        , SalonlistView, SalonsAdapter.OnItemClickListener {

    @Inject
    lateinit var salonsPresenter: SalonsPresenterImpl
    lateinit var salonsAdapter: SalonsAdapter
    private val progressDialog: MaterialDialog by lazy {
        MaterialDialog.Builder(this)
                .title(R.string.conversations_loading)
                .content(R.string.wait)
                .progress(true, 0)
                .build() }
    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_salons)

//        setSupportActionBar(toolBar)
        if(getSupportActionBar() != null)
        {
            getSupportActionBar()?.setElevation(0F);
        }

        (application as App).addSalonsComponent().inject(this)
        salonsAdapter = SalonsAdapter(this, mutableListOf<SalonModel>())
        salons_recyclerView.adapter = salonsAdapter

        salons_recyclerView.layoutManager = LinearLayoutManager(this)

        add_salon.setOnClickListener { salonsPresenter.onCreateSalonButtonClicked() }

        salonsPresenter.attachView(this)
        salonsPresenter.initialize(firebaseAuth.currentUser!!.uid)

    }

    override fun showCreateSalonlistFragment() {
        MaterialDialog.Builder(this)
                .title(R.string.create_conversation)
                .content(R.string.input_salon_name)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .positiveText(R.string.create_conversation)
                .input("", "", false) { dialog, input -> salonsPresenter.createSalon(input.toString()
                        ,"made from Android outer space", firebaseAuth.currentUser!!.uid) }.show() //TODO replace this code
    }

    override fun setSalonlist(salonEntities: List<SalonModel>?) {
        salons_recyclerView.adapter = SalonsAdapter(this, salonEntities!!.toMutableList())
    }

    override fun addSalon(salonEntity: SalonModel) {
        salonsAdapter.items.add(salonEntity)
        salonsAdapter.notifyItemInserted(salonsAdapter.itemCount - 1)
    }

    override fun changeSalon(salonEntity: SalonModel) {
        val index = (salons_recyclerView.adapter as SalonsAdapter).items.indexOfFirst { it.id.equals(salonEntity.id) }
        (salons_recyclerView.adapter as SalonsAdapter).items[index] = salonEntity
        salons_recyclerView.adapter.notifyDataSetChanged()
    }

    override fun removeSalon(salonEntity: SalonModel) {
        Logger.log("removed")
        (salons_recyclerView.adapter as SalonsAdapter).items.removeAll { it.id.equals(salonEntity.id) }
        salons_recyclerView.adapter.notifyDataSetChanged()
    }

    override fun showProgress() {
        progressDialog.show()
    }

    override fun hideProgress() {
        progressDialog.hide()
    }

    override fun onItemClicked(item: SalonModel) {
        openEventActivity(item)
    }

    override fun onLongItemClicked(item: SalonModel) {
        removeSalon(item) //todo
    }

    override fun openEventActivity(salonModel: SalonModel) {
        //val intent = Intent(this, eventActivity::class.java)
        //TODO WTF
        val intent = Intent(this, SalonMainActivity::class.java)
        //val intent = Intent(this, CustomerListActivity::class.java)
        intent.putExtra(EXTRA_CONVERSATION, salonModel)
        startActivity(intent)
    }

    override fun onError(throwable: Throwable) {
        Logger.log("${throwable.javaClass.name} ${throwable.message}")
        salons_recyclerView.showSnackBar(throwable.message)
    }

    override fun showSnackbar(event: String) {
        salons_recyclerView.showSnackBar(event)
    }

    override fun showToast(event: String) {
        toast(event)
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra(EXTRA_SIGN_OUT, true)
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as App).clearSalonsComponent()
        salonsPresenter.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_conversations, menu)
        return true
    }

/*    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_sign_out -> openLoginActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_conversations, menu);
    }*/

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.action_sign_out -> {
                openLoginActivity()
                return true
            }
            R.id.action_settings -> {
                showToast("Hello world")
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }
    companion object {
        val EXTRA_SIGN_OUT: String = "SIGN_OUT"
        val EXTRA_CONVERSATION: String = "CONVERSATION"
    }
}
