package com.example.foody_udemy_training_2.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.foody_udemy_training_2.data.Repository
import com.example.foody_udemy_training_2.data.database.entities.FavoritesEntity
import com.example.foody_udemy_training_2.data.database.entities.RecipesEntity
import com.example.foody_udemy_training_2.models.FoodRecipe
import com.example.foody_udemy_training_2.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import java.lang.Exception

class MainViewModel @ViewModelInject constructor(
    private val repository: Repository,
    application: Application
) : AndroidViewModel(application) {

    /** ROOM DATABASE */

    // if you get a warning and a variable type is a live data and Flow is required, just add .assLiveData()
    // as extension function to convert our flow to a live data
    val readRecipes: LiveData<List<RecipesEntity>> = repository.local.readRecipes().asLiveData()

    // initialize favorite recipes LiveData
    val readFavoriteRecipes: LiveData<List<FavoritesEntity>> = repository.local.readFavoriteRecipes().asLiveData()

    private fun insertRecipes(recipesEntity: RecipesEntity) =
        // added Dispatchers.IO because we are running a database work
        viewModelScope.launch(Dispatchers.IO) {
            // call repository.local.insertRecipes(recipesEntity) passing recipesEntity from the parameters of this function
            repository.local.insertRecipes(recipesEntity)
        }

    // added functions for favorite recipe
    fun insertFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertFavoriteRecipes(favoritesEntity)
        }

    fun deleteFavoriteRecipe(favoritesEntity: FavoritesEntity) =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteFavoriteRecipe(favoritesEntity)
        }

    fun deleteAllFavoriteRecipe() =
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.deleteAllFavoriteRecipes()
        }

    /** RETROFIT */
    var recipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()

    fun getRecipes(queries: Map<String, String>) = viewModelScope.launch {
        getRecipesSafeCall(queries)
    }

    // created new variable and function (searchRecipes) for searching recipes
    var searchedRecipesResponse: MutableLiveData<NetworkResult<FoodRecipe>> = MutableLiveData()
    
    fun searchRecipes(searchQuery: Map<String, String>) = viewModelScope.launch { 
        searchRecipesSafeCall(searchQuery)
    }

    private suspend fun getRecipesSafeCall(queries: Map<String, String>) {
        recipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {

                val response = repository.remote.getRecipes(queries)
                recipesResponse.value = handleFoodRecipesResponse(response)

                // Room database handling offline caching of our recipes from API
                // so here in this to get the recipe save for call and we want to
                // cache our data immediately after we receive it
                val foodRecipe = recipesResponse.value!!.data

                if(foodRecipe != null) {
                    offlineCacheRecipes(foodRecipe)
                }


            } catch (e: Exception) {
                recipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            recipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    // created function for safe call in searchRecipes
    private suspend fun searchRecipesSafeCall(searchQuery: Map<String, String>) {
        searchedRecipesResponse.value = NetworkResult.Loading()
        if (hasInternetConnection()) {
            try {

                val response = repository.remote.searchRecipes(searchQuery)
                searchedRecipesResponse.value = handleFoodRecipesResponse(response)

            } catch (e: Exception) {
                searchedRecipesResponse.value = NetworkResult.Error("Recipes not found.")
            }
        } else {
            searchedRecipesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    // created one parameter and it is food recipe
    private fun offlineCacheRecipes(foodRecipe: FoodRecipe) {
        val recipesEntity = RecipesEntity(foodRecipe)

        insertRecipes(recipesEntity)
    }

    private fun handleFoodRecipesResponse(response: Response<FoodRecipe>): NetworkResult<FoodRecipe>? {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.results.isNullOrEmpty() -> {
                return NetworkResult.Error("Recipes not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}