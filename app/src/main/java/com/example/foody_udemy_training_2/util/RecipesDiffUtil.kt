package com.example.foody_udemy_training_2.util

import androidx.recyclerview.widget.DiffUtil
import com.example.foody_udemy_training_2.models.Result

// added <T> for generics
class RecipesDiffUtil<T>(
    // removed Result and used generics
    private val oldList: List<T>,
    private val newList: List<T>
): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}