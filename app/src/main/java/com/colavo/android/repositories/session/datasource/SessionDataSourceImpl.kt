package com.colavo.android.repositories.session.datasource

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.colavo.android.entity.session.User
import com.colavo.android.entity.query.session.SessionQuery
import com.colavo.android.entity.session.UserMapper
import retrofit2.Retrofit
import rx.Observable

/**
 * Created by RUS on 11.07.2016.
 */
class SessionDataSourceImpl(val retrofit: Retrofit, val firebaseAuth: FirebaseAuth) : SessionDataSource {

    override fun register(query: SessionQuery.Register): Observable<User> = Observable.create<FirebaseUser> { subscriber ->
        firebaseAuth.createUserWithEmailAndPassword(query.email, query.password)
                .addOnSuccessListener { task ->
                    subscriber.onNext(task.user)
                    subscriber.onCompleted() }
                .addOnFailureListener { exception -> subscriber.onError(exception) }
    }
            .flatMap { firebaseUser -> addUserNameToDb(firebaseUser.uid, query.name) }

    override fun signIn(query: SessionQuery.SignIn): Observable<User> = Observable.create<FirebaseUser> { subscriber ->
        firebaseAuth.signInWithEmailAndPassword(query.email, query.password)
                .addOnSuccessListener { task ->
                    subscriber.onNext(task.user)
                    subscriber.onCompleted() }
                .addOnFailureListener { exception -> subscriber.onError(exception) }
    }
            .map { firebaseUser -> UserMapper.transformFromFirebaseUser(firebaseUser) }

    override fun signOut(query: SessionQuery.SignOut): Observable<User> = Observable.create { subscriber ->
        firebaseAuth.signOut()
        subscriber.onCompleted()
    }

    private fun addUserNameToDb(uid: String, name: String): Observable<User> = Observable.create { subscriber ->
        val user = User(uid, name)
        FirebaseDatabase.getInstance()
                .reference
                .child("users")
                .child(uid)
                .setValue(user)
                .addOnSuccessListener { subscriber.onNext(user)
                                        subscriber.onCompleted() }
                .addOnFailureListener { exception -> subscriber.onError(exception) }
    }

}