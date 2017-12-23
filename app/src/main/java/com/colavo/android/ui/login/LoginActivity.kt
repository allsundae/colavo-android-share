package com.colavo.android.ui.login

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.colavo.android.App
import com.colavo.android.R
import com.colavo.android.presenters.login.LoginPresenterImpl
import com.colavo.android.ui.salons.SalonListActivity
import com.colavo.android.utils.toast
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject
import android.text.Html
import android.databinding.adapters.TextViewBindingAdapter.setText



class LoginActivity : AppCompatActivity(), LoginView {

    @Inject
    lateinit var loginPresenter: LoginPresenterImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        (application as App).addSessionComponent().inject(this)

        loginPresenter.attachView(this)

        signInButton.setOnClickListener { loginPresenter.signIn(name.text.toString(), password.text.toString()) }
        registerButton.setOnClickListener { loginPresenter.register(name.text.toString(), password.text.toString()) }

        if(intent.extras?.getBoolean(SalonListActivity.EXTRA_SIGN_OUT) ?: false) loginPresenter.signOut()

        val string = resources.getString(R.string.tnc)
        guideline_text.setText(Html.fromHtml(string))

    }

    override fun openSalonsActivity(uid: String) {
        val intent = Intent(this, SalonListActivity::class.java)
        intent.putExtra(KEY_UID, uid)
        startActivity(intent)
        finish()
    }

 /*   override fun onError(throwable: Throwable) = toast("${throwable.javaClass.name}: ${throwable.message}")*/
     override fun onError(throwable: Throwable) = toast("${throwable.message}")

    override fun showToast(event: String) = toast(event)

    override fun showSnackbar(event: String) {}

    override fun showSignInProgress() {
        signInButton.visibility = View.GONE
        signInProgress.visibility = View.VISIBLE
    }

    override fun hideSignInProgress() {
        signInButton.visibility = View.VISIBLE
        signInProgress.visibility = View.GONE
    }

    override fun showRegisterProgress() {
        registerButton.visibility = View.GONE
        registerProgress.visibility = View.VISIBLE
    }

    override fun hideRegisterProgress() {
        registerButton.visibility = View.VISIBLE
        registerProgress.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        (application as App).clearSessionComponent()
        this.loginPresenter.onDestroy()
    }

    companion object {
        val KEY_UID: String = "UID"
    }
}
