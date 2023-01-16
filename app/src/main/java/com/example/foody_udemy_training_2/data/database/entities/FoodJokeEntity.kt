package com.example.foody_udemy_training_2.data.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody_udemy_training_2.models.FoodJoke
import com.example.foody_udemy_training_2.util.Constants.Companion.FOOD_JOKE_TABLE

@Entity(tableName = FOOD_JOKE_TABLE)
class FoodJokeEntity(
    // this embedded annotation will inspect our foodJoke model class and it will take it's
    // field and store that inside our new table
    @Embedded
    var foodJoke: FoodJoke
) {

    @PrimaryKey(autoGenerate = false)
    var id: Int = 0

}