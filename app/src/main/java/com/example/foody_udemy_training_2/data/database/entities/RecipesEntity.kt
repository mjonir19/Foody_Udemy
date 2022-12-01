package com.example.foody_udemy_training_2.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody_udemy_training_2.models.FoodRecipe
import com.example.foody_udemy_training_2.util.Constants.Companion.RECIPES_TABLE

// entity created table name
@Entity(tableName = RECIPES_TABLE)
class RecipesEntity(
    // our entity or our table will contain only one row of the food recipe
    var foodRecipe: FoodRecipe
)  {
    // create value integer value for ID and the second food recipe
    // autoGenerate = false means whenever we fetch a new data from our API, we're going to replace all the data from our database table with the new data
    @PrimaryKey(autoGenerate = false)
    var id: Int = 0
}