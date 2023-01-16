package com.example.foody_udemy_training_2.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.preferencesKey
import com.example.foody_udemy_training_2.util.Constants.Companion.DEFAULT_DIET_TYPE
import com.example.foody_udemy_training_2.util.Constants.Companion.DEFAULT_MEAL_TYPE
import com.example.foody_udemy_training_2.util.Constants.Companion.PREFERENCES_BACK_ONLINE
import com.example.foody_udemy_training_2.util.Constants.Companion.PREFERENCES_DIET_TYPE
import com.example.foody_udemy_training_2.util.Constants.Companion.PREFERENCES_DIET_TYPE_ID
import com.example.foody_udemy_training_2.util.Constants.Companion.PREFERENCES_MEAL_TYPE
import com.example.foody_udemy_training_2.util.Constants.Companion.PREFERENCES_MEAL_TYPE_ID
import com.example.foody_udemy_training_2.util.Constants.Companion.PREFERENCES_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

// we're going to need this context for our data store
// the main difference between data store and share preferences is that data store is running on
// background thread and no on main thread

// annotated activityRetainedScoped is because this datastore repository will be used inside our recipesViewModel
@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        // define all our keys, which we're going to use for our data store preferences
        val selectedMealType = preferencesKey<String>(PREFERENCES_MEAL_TYPE)
        val selectedMealTypeId = preferencesKey<Int>(PREFERENCES_MEAL_TYPE_ID)
        val selectedDietType = preferencesKey<String>(PREFERENCES_DIET_TYPE)
        val selectedDietTypeId = preferencesKey<Int>(PREFERENCES_DIET_TYPE_ID)

        // this key will be for our back online variable
        val backOnline = preferencesKey<Boolean>(PREFERENCES_BACK_ONLINE)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        name = PREFERENCES_NAME
    )

    // function for saving values
    suspend fun saveMealAndDietType(
        mealType: String,
        mealTypeId: Int,
        dietType: String,
        dietTypeId: Int
    ) {
        dataStore.edit { preferences ->
            // specifying actual key name (selectedMealType) and value(mealType)
            // the value will be passed through this function
            preferences[PreferenceKeys.selectedMealType] = mealType
            preferences[PreferenceKeys.selectedMealTypeId] = mealTypeId
            preferences[PreferenceKeys.selectedDietType] = dietType
            preferences[PreferenceKeys.selectedDietTypeId] = dietTypeId
        }
    }

    // creating function for basically saving this backOnline value
    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit { preferences ->
            // store the value from our function parameters inside this key
            preferences[PreferenceKeys.backOnline] = backOnline
        }
    }

    // when we are reading those values from our bottom sheet, we're going to flow to pass this class MealAndDietType
    val readMealAndDietType: Flow<MealAndDietType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            // basically storing some value insinde this variable, we are selecting the value of this specific key
            val selectedMealType = preferences[PreferenceKeys.selectedMealType] ?: DEFAULT_MEAL_TYPE
            val selectedMealTypeId = preferences[PreferenceKeys.selectedMealTypeId] ?: 0
            val selectedDietType = preferences[PreferenceKeys.selectedDietType] ?: DEFAULT_DIET_TYPE
            val selectedDietTypeId = preferences[PreferenceKeys.selectedDietTypeId] ?: 0

            MealAndDietType(
                selectedMealType,
                selectedMealTypeId,
                selectedDietType,
                selectedDietTypeId
            )
        }

    // provide a way for reading the value from the data store (preferences[PreferenceKeys.backOnline] = backOnline)
    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val backOnline = preferences[PreferenceKeys.backOnline] ?: false // if there is no return the default value will be false
            backOnline
        }

}

data class MealAndDietType(
    val selectedMealType: String,
    val selectedMealTypeId: Int,
    val selectedDietType: String,
    val selectedDietTypeId: Int
)