package com.example.foody_udemy_training_2.bindingadapters

import android.net.Network
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.foody_udemy_training_2.data.database.entities.FoodJokeEntity
import com.example.foody_udemy_training_2.models.FoodJoke
import com.example.foody_udemy_training_2.util.NetworkResult
import com.google.android.material.card.MaterialCardView

class FoodJokeBinding {

    companion object {

        // requireAll = false, because we are not going to use those 2 attributes when every view
        // instead we might just add the one attribute on one view
        // readApiResponse3 is for ProgressBar and both is for materialCardView
        @BindingAdapter("readApiResponse3", "readDatabase3", requireAll = false)
        @JvmStatic
        fun setCardAndProgressVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {
            when(apiResponse) {
                is NetworkResult.Loading -> {
                    when(view) {
                        is ProgressBar -> {
                            view.visibility = View.VISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.INVISIBLE
                        }
                    }
                }
                is NetworkResult.Error -> {
                    when(view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                            if(database != null) {
                                if(database.isEmpty()) {
                                    view.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Success -> {
                    when(view) {
                        is ProgressBar -> {
                            view.visibility = View.INVISIBLE
                        }
                        is MaterialCardView -> {
                            view.visibility = View.VISIBLE
                        }
                    }
                }
                else -> {}
            }
        }

        @BindingAdapter("readApiResponse4", "readDatabase4", requireAll = true)
        @JvmStatic
        fun setErrorViewVisibility(
            view: View,
            apiResponse: NetworkResult<FoodJoke>?,
            database: List<FoodJokeEntity>?
        ) {

            Log.e("database1", database.toString())
            if(database != null) {
                Log.e("database2", database.toString())
                if(database.isEmpty()) {
                    view.visibility = View.VISIBLE

                    if(view is TextView) {
                        if(apiResponse != null) {
                            view.text = apiResponse.message.toString()
                        }
                    }
                }
            }

            if(apiResponse is NetworkResult.Success) {
                view.visibility = View.INVISIBLE
            }

        }

    }

}