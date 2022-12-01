package com.example.foody_udemy_training_2.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody_udemy_training_2.R
import com.example.foody_udemy_training_2.adapters.IngredientsAdapter
import com.example.foody_udemy_training_2.util.Constants.Companion.RECIPE_RESULT_KEY
import kotlinx.android.synthetic.main.fragment_ingredients.view.*

class IngredientsFragment : Fragment() {

    //created new variable
    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_ingredients, container, false)

        //get the arguments in pager adapter
        val args = arguments
        val myBundle: com.example.foody_udemy_training_2.models.Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        setUpRecyclerView(view)
        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }

        return view
    }

    //create function for setting up recyclerview
    private fun setUpRecyclerView(view: View) {
        view.ingredients_recyclerView.adapter = mAdapter
        view.ingredients_recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}