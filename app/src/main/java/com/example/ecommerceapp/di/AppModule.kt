package com.example.ecommerceapp.di

import android.app.Application
import android.content.Context
import com.example.ecommerceapp.data.datasource.local.AppPreferencesDataSource
import com.example.ecommerceapp.data.reposotiry.auth.FirebaseAuthRepository
import com.example.ecommerceapp.data.reposotiry.auth.FirebaseAuthRepositoryImpl
import com.example.ecommerceapp.data.reposotiry.common.AppDataStoreRepositoryImpl
import com.example.ecommerceapp.data.reposotiry.common.AppPreferenceRepository
import com.example.ecommerceapp.data.reposotiry.user.UserFirestoreRepository
import com.example.ecommerceapp.data.reposotiry.user.UserFirestoreRepositoryImpl
import com.example.ecommerceapp.data.reposotiry.user.UserPreferenceRepository
import com.example.ecommerceapp.data.reposotiry.user.UserPreferenceRepositoryImpl


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesFirebaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAppPreferencesDataSource(@ApplicationContext context: Context): AppPreferencesDataSource {
        return AppPreferencesDataSource(context)
    }

    @Provides
    @Singleton
    fun provideUserPreferenceRepository(@ApplicationContext context: Context): UserPreferenceRepository =
        UserPreferenceRepositoryImpl(context)

    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): FirebaseAuthRepository = FirebaseAuthRepositoryImpl(auth, firestore)

    @Provides
    @Singleton
    fun provideAppPreferenceRepository(
        appPreferencesDataSource: AppPreferencesDataSource
    ): AppPreferenceRepository = AppDataStoreRepositoryImpl(appPreferencesDataSource)

    @Provides
    @Singleton
    fun provideUserFirestoreRepository(
        firebaseFirestore: FirebaseFirestore
    ): UserFirestoreRepository = UserFirestoreRepositoryImpl(firebaseFirestore)


}