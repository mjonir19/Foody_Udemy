package com.example.foody_udemy_training_2.data

import com.example.foody_udemy_training_2.data.database.RecipesDao
import com.example.foody_udemy_training_2.data.database.RecipesEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val recipesDao: RecipesDao
) {

    // our local data source will contain all the queries from our recipes now, because we have injected that
    // recipes down and for now for reading the database we're using a flow
    // but in the main view modulator, we should be able to change that to a live data
    fun readDatabase(): Flow<List<RecipesEntity>> {
        return recipesDao.readRecipes()
    }

    // create one function for inserting the recipes into the database
    suspend fun insertRecipes(recipesEntity: RecipesEntity) {
        // we are calling this insert recipes from these recipes dao
        recipesDao.insertRecipes(recipesEntity)
    }

}