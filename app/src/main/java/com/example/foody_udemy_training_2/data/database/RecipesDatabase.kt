package com.example.foody_udemy_training_2.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.foody_udemy_training_2.data.database.entities.FavoritesEntity
import com.example.foody_udemy_training_2.data.database.entities.RecipesEntity

@Database(
    // added favoritesEntity class and it made 2 database
    entities = [RecipesEntity::class, FavoritesEntity::class],
    // whenever you change your database schema, you need to increase this version number
    // made version = 2, because we added a new database
    version = 1,
    // specify export schema to false so you can set the annotation process or arguments to tell room to export the database schema into a folder
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

}