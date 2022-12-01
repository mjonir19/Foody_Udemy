package com.example.foody_udemy_training_2.bindingadapters

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.foody_udemy_training_2.adapters.FavoriteRecipesAdapter
import com.example.foody_udemy_training_2.data.database.entities.FavoritesEntity

class FavoriteRecipesBinding {

    companion object {

        /**
         * CHECK fragment_favorite_recipes.xml for this code call
         *
         * viewVisibility="@{mainViewModel.readFavoriteRecipes}" refers to this function
         * mainViewModel.readFavoriteRecipes = favoritesEntity from this function paramters
         *
         * setData="@{mAdapter}" calls the second parameter for this function
         * */

        // viewVisibility refers to favorites entity and setData to mAdapter, be careful and follow the order for the parameters of this function
        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoritesEntity: List<FavoritesEntity>?,
            mAdapter: FavoriteRecipesAdapter?
        ) {
            if (favoritesEntity.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            } else {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        mAdapter?.setData(favoritesEntity)
                    }
                }
            }
        }
    }

}