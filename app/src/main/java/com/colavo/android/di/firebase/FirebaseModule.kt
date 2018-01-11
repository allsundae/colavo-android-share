package com.colavo.android.di.firebase

import com.colavo.android.utils.Logger
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class FirebaseModule {

    @Provides
    @Singleton
    fun getFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun getFirebaseDatabase(): FirebaseDatabase {
/*
        val mDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()

        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            Logger.log(FirebaseDatabase.getInstance().toString())
        }catch (e : Exception){
            Logger.log("SetPresistenceEnabled:Fail"+ FirebaseDatabase.getInstance().toString())
            e.printStackTrace()
        }
*/
        var mDatabase: FirebaseDatabase

        mDatabase = FirebaseDatabase.getInstance()

        if (mDatabase == null){
            mDatabase = FirebaseDatabase.getInstance()
            mDatabase.setPersistenceEnabled(true)
            Logger.log("FirebaseModule : FirebaseDatabase setPersistenceEnabled : TRUE")
        }
            return mDatabase;

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true)
//        // return mDatabase
//        return FirebaseDatabase.getInstance()
    }
}

