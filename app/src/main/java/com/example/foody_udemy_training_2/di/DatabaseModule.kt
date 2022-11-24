package com.example.foody_udemy_training_2.di

import android.content.Context
import androidx.room.Room
import com.example.foody_udemy_training_2.data.database.RecipesDatabase
import com.example.foody_udemy_training_2.util.Constants.Companion.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object DatabaseModule {

    // this function will provide our room database builder and we need to annotate this function with singleton and with provides
    // because the room library is not our class, it is a third party
    @Singleton
    @Provides
    fun providerDatabase(
        // we're going to use this annotation from our hilt library so we can inject this application context immediately
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        RecipesDatabase::class.java,
        DATABASE_NAME
    ).build()

    // we basically told our hilt library how to provide our room database builder and our recipes dao through this database
    @Singleton
    @Provides
    // database: RecipesDatabase - we need to satisfy this dependency in order to provide our dao
    fun provideDao(database: RecipesDatabase) = database.recipesDao()

}