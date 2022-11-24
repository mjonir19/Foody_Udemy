package com.example.foody_udemy_training_2.data

import com.example.foody_udemy_training_2.data.network.FoodRecipesApi
import com.example.foody_udemy_training_2.models.FoodRecipe
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val foodRecipesApi: FoodRecipesApi
) {

    suspend fun getRecipes(queries: Map<String, String>): Response<FoodRecipe> {
        return foodRecipesApi.getRecipes(queries)
    }

}