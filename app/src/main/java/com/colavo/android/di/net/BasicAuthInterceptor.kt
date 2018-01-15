package com.colavo.android.di.net

import com.colavo.android.utils.Logger
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.GetTokenResult
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser



/**
 * Created by macbookpro on 2018. 1. 9..
 */
class FirebaseUserIdTokenInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        try {
            val user = FirebaseAuth.getInstance().currentUser
            Logger.log(user.toString())
            if (user == null) {
                Logger.log("FirebaseUserIdTokenInterceptor : Not logged in.")
                throw Exception("FirebaseUserIdTokenInterceptor : User is not logged in.")
            } else {
                val task = user.getToken(true)
                val tokenResult = Tasks.await(task)
                val idToken = tokenResult.token

                if (idToken == null) {
                    Logger.log("FirebaseUserIdTokenInterceptor : idToken is null")
                    throw Exception("FirebaseUserIdTokenInterceptor : idToken is null")
                } else {
                    Logger.log("FirebaseUserIdTokenInterceptor : TOKEN : ${idToken}")
                    val modifiedRequest = request.newBuilder()
                            .addHeader(X_FIREBASE_ID_TOKEN, idToken)
                            .build()
               //     Logger.log("FirebaseUserIdTokenInterceptor : modifiedRequest : ${modifiedRequest.toString()}")
                    return chain.proceed(modifiedRequest)
                }
            }
        } catch (e: Exception) {
            throw IOException(e.message)
        }

    }

    companion object {

        // Custom header for passing ID token in request.
        private val X_FIREBASE_ID_TOKEN = "Authorization"
    }
}