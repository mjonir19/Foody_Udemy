package com.example.foody_udemy_training_2.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.foody_udemy_training_2.util.Constants.Companion.FAVORITES_RECIPES_TABLE

// create entity annotation
@Entity(tableName = FAVORITES_RECIPES_TABLE)
class FavoritesEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var result: com.example.foody_udemy_training_2.models.Result
) {



}