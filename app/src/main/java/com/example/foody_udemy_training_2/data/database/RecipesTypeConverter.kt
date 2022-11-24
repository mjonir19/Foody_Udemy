package com.example.foody_udemy_training_2.data.database

import androidx.room.TypeConverter
import com.example.foody_udemy_training_2.models.FoodRecipe
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class RecipesTypeConverter {

    var gson = Gson()

    /**we will create two functions
    1. will convert food recipe to string
    2. will convert string back to food recipe*/

    @TypeConverter
    fun foodRecipeToString(foodRecipe: FoodRecipe): String {
        // using JSON library to actually convert this object to a string
        return gson.toJson(foodRecipe)
    }

    @TypeConverter
    fun stringToFoodRecipe(data: String): FoodRecipe {
        val listType = object : TypeToken<FoodRecipe>() {}.type
        return gson.fromJson(data, listType)
    }

}