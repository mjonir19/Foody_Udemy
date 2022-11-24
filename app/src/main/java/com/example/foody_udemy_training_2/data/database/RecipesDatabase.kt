package com.example.foody_udemy_training_2.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [RecipesEntity::class],
    // whenever you change your database schema, you need to increase this version number
    version = 1,
    // specify export schema to false so you can set the annotation process or arguments to tell room to export the database schema into a folder
    exportSchema = false
)
@TypeConverters(RecipesTypeConverter::class)
abstract class RecipesDatabase: RoomDatabase() {

    abstract fun recipesDao(): RecipesDao

}