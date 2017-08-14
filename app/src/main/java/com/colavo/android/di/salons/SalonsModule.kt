package com.colavo.android.di.salons

import com.google.firebase.database.FirebaseDatabase
import com.colavo.android.interactors.salons.CreateSalon
import com.colavo.android.interactors.salons.GetSalons
import com.colavo.android.repositories.salons.SalonsRepository
import com.colavo.android.repositories.salons.datasource.SalonsDataSourceImpl
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

/**
 * Created by RUS on 20.07.2016.
 */
@Module
class SalonsModule() {

    @Provides
    @SalonsScope
    fun getGetSalons(salonsRepository: SalonsRepository): GetSalons = GetSalons(salonsRepository)

    @Provides
    @SalonsScope
    fun getCreateSalon(salonsRepository: SalonsRepository): CreateSalon = CreateSalon(salonsRepository)

    @Provides
    @SalonsScope
    fun getSalonRepository(salonsDataSourceImpl: SalonsDataSourceImpl): SalonsRepository = SalonsRepository(salonsDataSourceImpl)

    @Provides
    @SalonsScope
    fun getSalonDataSourceImplementation(retrofit: Retrofit, firebaseDatabase: FirebaseDatabase): SalonsDataSourceImpl = SalonsDataSourceImpl(retrofit, firebaseDatabase)

}