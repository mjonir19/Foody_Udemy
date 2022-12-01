package com.example.foody_udemy_training_2.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foody_udemy_training_2.data.database.entities.FavoritesEntity
import com.example.foody_udemy_training_2.data.database.entities.RecipesEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipesDao {
  /*creating 2 queries:
    1. for inserting our data to recipes entity
    2. for reading the data from our recipes entity class*/

    // adding suspend because later we're going to use kotlin coroutines to run this query and we need to annotate
    // our function with insert annotation

    // onConflict = OnConflictStrategy.REPLACE: means that whenever we fetch a new data from our API, we want to replace the old one
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipesEntity: RecipesEntity)

    /*instead of using a live data, were using the flow.
    so were using the flow in these recipes dao and inside our repository but when we reach our view model,
    then were going to convert flow to a live data
    basically this flow is kind of similar to two live data and the data inside this flow is basically circulating
    whenever we receive some new value*/

    // creating SELECT query from recipes table ordered by ID and ascending
    @Query("SELECT * FROM recipes_table ORDER BY id ASC")
    fun readRecipes(): Flow<List<RecipesEntity>>

    // create a function to insert favorites in our database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity)

    // function that read the favorites recipe
    @Query("SELECT * FROM favorites_recipes_table ORDER BY id ASC")
    fun readFavoriteRecipes(): Flow<List<FavoritesEntity>>

    // function that deletes certain favorite
    @Delete
    suspend fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity)

    // function that deletes the table
    @Query("DELETE FROM favorites_recipes_table")
    suspend fun deleteAllFavoriteRecipes()

}