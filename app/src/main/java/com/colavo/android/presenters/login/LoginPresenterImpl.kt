package com.colavo.android.presenters.login

import com.colavo.android.entity.session.User
import com.colavo.android.interactors.session.*
import com.colavo.android.ui.login.LoginView
import rx.Subscriber
import javax.inject.Inject

/**
 * Created by RUS on 10.07.2016.
 */

class LoginPresenterImpl @Inject constructor(val signIn: SignIn,
                                             val signOut: SignOut,
                                             val register: Register) : LoginPresenter {

    var loginView: LoginView? = null

    override fun attachView(loginView: LoginView) {
        this.loginView = loginView
    }

    override fun signIn(name: String, password: String) {
        loginView?.showSignInProgress()
 /*       val email = name + "@gmail.com"*/
        val email = name
        signIn.execute(email, password, SignInSubscriber())
    }

    override fun register(name: String, password: String) {
        loginView?.showRegisterProgress()
        val email = name
        register.execute(email, name, password, RegisterSubscriber())
    }

    override fun signOut() {
        signOut.execute(SignOutSubscriber())
    }

    override fun onDestroy() {
        this.loginView = null
//        this.useCase.unsubscribe()
    }

    private inner class SignInSubscriber : Subscriber<User>() {

        override fun onNext(user: User) {
            loginView?.openSalonsActivity(user.uid)
        }

        override fun onError(e: Throwable) {
            loginView?.hideSignInProgress()
            loginView?.onError(e)
        }

        override fun onCompleted() {
            loginView?.hideSignInProgress()
        }
    }

    private inner class RegisterSubscriber : Subscriber<User>() {

        override fun onNext(user: User) {
            loginView?.openSalonsActivity(user.uid)
        }

        override fun onError(e: Throwable) {
            loginView?.hideRegisterProgress()
            loginView?.onError(e)
        }

        override fun onCompleted() {
            loginView?.hideRegisterProgress()
        }
    }

    private inner class SignOutSubscriber : Subscriber<User>() {
        override fun onNext(user: User?) {}

        override fun onError(throwable: Throwable) {
            loginView?.onError(throwable)
        }

        override fun onCompleted() {}

    }

    companion object {
        val COMPLETED_REGISTRATION: String = "Registration has been completed"
    }
}