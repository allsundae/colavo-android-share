package com.colavo.android.ui

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.colavo.android.App
import com.colavo.android.R
import com.colavo.android.base.BaseActivity
import com.colavo.android.entity.salon.SalonModel
import com.colavo.android.ui.SalonMainActivity.Companion.SAVED_PREFS
import com.colavo.android.ui.SalonMainActivity.Companion.SAVED_SALON_ID
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.ui.login.LoginActivity
import com.colavo.android.utils.Logger
import javax.inject.Inject
import com.google.gson.Gson





class MainActivity : BaseActivity(){

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

/*    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Logger.log("onRestoreInstanceState : START ")

        (application as App).appComponent.inject(this)

        val bundle  = savedInstanceState?.getParcelable(SalonMainActivity.BUNDLE_KEY) as Bundle

        if(firebaseAuth.currentUser != null) {
                val salonModel = bundle.getSerializable(SalonMainActivity.SAVED_SALON_STATE) as SalonModel
                Logger.log("onRestoreInstanceState : ${salonModel.name} ")
                val intent = Intent(this, SalonMainActivity::class.java)
                intent.putExtra(SalonListActivity.EXTRA_SALONMODDEL, salonModel)
                startActivity(intent)
                finish()
        }
        else openLoginActivity()

        finish()
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        (application as App).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        Logger.log("MainActivity : onCreate")

        if (savedInstanceState != null){
            val bundle  = savedInstanceState.getParcelable(SalonMainActivity.BUNDLE_KEY) as Bundle
            Logger.log("MainActivity : onCreate : ${bundle.toString()} ")

            if(firebaseAuth.currentUser != null) {
                    val salonModel = bundle.getSerializable(SalonMainActivity.SAVED_SALON_STATE) as SalonModel
                    Logger.log("MainActivity : onCreate : ${salonModel.name} ")
                    val intent = Intent(this, SalonMainActivity::class.java)
                    intent.putExtra(SalonListActivity.EXTRA_SALONMODDEL, salonModel)
                    startActivity(intent)
                    finish()
            }
            else openLoginActivity()
            finish()

        }
        else {
            if(firebaseAuth.currentUser != null) {
                val savedPref = getSharedPreferences(SAVED_PREFS, 0)
              //  val serializedSavedSalonModel = savedPref.getString(SAVED_SALON_ID, "")
              //  val salonModel = serializedSavedSalonModel.to(SalonModel::class.java) as SalonModel
                if (savedPref != null )    {
                    val gson = Gson()
                    val json = savedPref.getString(SAVED_SALON_ID, "")
                    val salonModel = gson.fromJson<SalonModel>(json, SalonModel::class.java)

                    if (salonModel != null){
                        Logger.log("MainActivity : onCreate : ${salonModel.name} : ${salonModel.id} ")
                        val intent = Intent(this, SalonMainActivity::class.java)
                        intent.putExtra(SalonListActivity.EXTRA_SALONMODDEL, salonModel)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        openLoginActivity()
                        finish()
                    }

                }
                else {
                    openLoginActivity()
                    finish()
                }
            }
            else
            {
                initialize()
            }
        }
    }

 /*   override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("savedId", savedId)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        savedId = savedInstanceState.getString("savedId")
    }*/
    private fun initialize() {
        if(firebaseAuth.currentUser != null)
            openSalonlistActivity(firebaseAuth.currentUser!!.uid)
        else openLoginActivity()
        finish()
    }

    private fun openLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    private fun openSalonlistActivity(uid: String) {
        val intent = Intent(this, SalonListActivity::class.java)
        intent.putExtra(KEY_UID, uid)
        startActivity(intent)
    }

    companion object {
        val KEY_UID: String = "UID"
    }
}
