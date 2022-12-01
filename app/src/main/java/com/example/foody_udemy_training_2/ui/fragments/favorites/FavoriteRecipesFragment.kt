package com.example.foody_udemy_training_2.ui.fragments.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foody_udemy_training_2.R
import com.example.foody_udemy_training_2.adapters.FavoriteRecipesAdapter
import com.example.foody_udemy_training_2.databinding.FragmentFavoriteRecipesBinding
import com.example.foody_udemy_training_2.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_favorite_recipes.view.*

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {
    // initialize mainViewModel first before the mAdapter to initialize this model first
    private val mainViewModel: MainViewModel by viewModels()

    // initialize lazily favoritesAdapter
    // for delete favorites, FavoriteRecipesAdapter requires 1 parameter
    private val mAdapter: FavoriteRecipesAdapter by lazy { FavoriteRecipesAdapter(requireActivity(), mainViewModel) }


    // created 2 variables for data binding
    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteRecipesBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.mAdapter = mAdapter

        // set menu in favoritesFragment
        setHasOptionsMenu(true)

        setupRecyclerView(binding.favoriteRecipesRecyclerView)


        return binding.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = mAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mAdapter.clearContextualActionMode()
    }

    // inflating favorite_recipes_menu.xml
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // if delete all favorite recipes menu is clicked, all favorites will be deleted
        if(item.itemId == R.id.deleteAll_favorite_recipes_menu) {
            mainViewModel.deleteAllFavoriteRecipe()
            showSnackBar()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showSnackBar() {
        Snackbar.make(
            binding.root,
            "All Recipes removed.",
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }
}