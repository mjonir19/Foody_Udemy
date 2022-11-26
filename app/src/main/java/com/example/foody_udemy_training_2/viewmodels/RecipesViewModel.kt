package com.example.foody_udemy_training_2.viewmodels

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.foody_udemy_training_2.data.DataStoreRepository
import com.example.foody_udemy_training_2.util.Constants.Companion.API_KEY
import com.example.foody_udemy_training_2.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foody_udemy_training_2.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foody_udemy_training_2.util.Constants.Companion.DEFAULT_RECIPES_NUMBER
import com.example.foody_udemy_training_2.util.Constants.Companion.QUERY_ADD_RECIPE_INFORMATION
import com.example.foody_udemy_training_2.util.Constants.Companion.QUERY_API_KEY
import com.example.foody_udemy_training_2.util.Constants.Companion.QUERY_DIET
import com.example.foody_udemy_training_2.util.Constants.Companion.QUERY_FILL_INGREDIENTS
import com.example.foody_udemy_training_2.util.Constants.Companion.QUERY_NUMBER
import com.example.foody_udemy_training_2.util.Constants.Companion.QUERY_SEARCH
import com.example.foody_udemy_training_2.util.Constants.Companion.QUERY_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

//added @ViewModelInject and dataStoreRepository
class RecipesViewModel @ViewModelInject constructor(
    application: Application,
    private val dataStoreRepository: DataStoreRepository
) : AndroidViewModel(application) {

    private var mealtype = DEFAULT_MEAL_TYPE
    private var dietType = DEFAULT_DIET_TYPE

    var networkStatus = false

    var backOnline = false

    val readMealAndDietType = dataStoreRepository.readMealAndDietType

    // called readBackOnline in dataStoreRepository as live data
    val readBackOnline = dataStoreRepository.readBackOnline.asLiveData()

    fun saveMealAndDietType(mealType: String, mealTypeId: Int, dietType: String, dietTypeId: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            // basically we're inserting the exact values from this function parameters
            dataStoreRepository.saveMealAndDietType(mealType, mealTypeId, dietType, dietTypeId)
        }

    // create a function for saving that back online and for reading it
    fun saveBackOnline(backOnline: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveBackOnline(backOnline)
        }
    }

    fun applyQueries(): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        viewModelScope.launch {
            // collect: collect the values from that flow (readMealAndDietType)
            readMealAndDietType.collect { value ->
                mealtype = value.selectedMealType
                dietType = value.selectedDietType
            }
        }

        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_TYPE] = mealtype
        queries[QUERY_DIET] = dietType
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        return queries
    }

    // created new function for search recipes api
    fun applySearchQuery(searchQuery: String): HashMap<String, String> {
        val queries: HashMap<String, String> = HashMap()

        // we don't need to specify any diet type or meal type, because we are searching all our API database to find a specific
        // recipe for the specified search query
        // adding queries for calling api
        queries[QUERY_SEARCH] = searchQuery
        queries[QUERY_NUMBER] = DEFAULT_RECIPES_NUMBER
        queries[QUERY_API_KEY] = API_KEY
        queries[QUERY_ADD_RECIPE_INFORMATION] = "true"
        queries[QUERY_FILL_INGREDIENTS] = "true"

        Log.e("applySearchQuery", queries.toList().toString())

        return queries
    }

    // checking if the value of our network status is false
    fun showNetworkStatus() {
        // when we lose our internet connection, we're going to display toast message and saying no internet
        // and we're going to persist this back online of value to true
        if(!networkStatus) {
            // displays if the network status is false
            Toast.makeText(getApplication(), "No Internet Connection", Toast.LENGTH_SHORT).show()

            // when we lose our internet connection and we want to set the value of this back online to true,
            // we're going to add also else if block so else if a network is true
            saveBackOnline(true)

            // then when we get back this internet connection, this function will be called once more
        } else if(networkStatus) {
            if(backOnline) {
                Toast.makeText(getApplication(), "We're back online", Toast.LENGTH_SHORT).show()
                saveBackOnline(false)
            }
        }
    }

}