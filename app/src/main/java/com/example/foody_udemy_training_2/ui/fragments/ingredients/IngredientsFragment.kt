package com.example.foody_udemy_training_2.ui.fragments.ingredients

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody_udemy_training_2.R
import com.example.foody_udemy_training_2.adapters.IngredientsAdapter
import com.example.foody_udemy_training_2.databinding.FragmentIngredientsBinding
import com.example.foody_udemy_training_2.util.Constants.Companion.RECIPE_RESULT_KEY

class IngredientsFragment : Fragment() {

    //created new variable
    private val mAdapter: IngredientsAdapter by lazy { IngredientsAdapter() }

    // enable viewBinding
    private var _binding: FragmentIngredientsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentIngredientsBinding.inflate(inflater, container, false)

        //get the arguments in pager adapter
        val args = arguments
        val myBundle: com.example.foody_udemy_training_2.models.Result? = args?.getParcelable(RECIPE_RESULT_KEY)

        setUpRecyclerView()
        myBundle?.extendedIngredients?.let { mAdapter.setData(it) }

        return binding.root
    }

    //create function for setting up recyclerview
    private fun setUpRecyclerView() {
        binding.ingredientsRecyclerView.adapter = mAdapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}