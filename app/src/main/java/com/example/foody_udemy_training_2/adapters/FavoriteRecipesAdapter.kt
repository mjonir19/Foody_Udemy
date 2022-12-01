package com.example.foody_udemy_training_2.adapters

import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.foody_udemy_training_2.R
import com.example.foody_udemy_training_2.data.database.entities.FavoritesEntity
import com.example.foody_udemy_training_2.databinding.FavoriteRecipesRowLayoutBinding
import com.example.foody_udemy_training_2.ui.fragments.favorites.FavoriteRecipesFragmentDirections
import com.example.foody_udemy_training_2.util.RecipesDiffUtil
import com.example.foody_udemy_training_2.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.favorite_recipes_row_layout.view.*

// inherited ActionMode.Callback for delete menu and implement all methods
// we need to add the fragmentActivity as a parameter of our favoriteRecipesAdapter
class FavoriteRecipesAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteRecipesAdapter.MyViewHolder>(), ActionMode.Callback {

    // created variable for multi selection
    private var multiSelection = false
    private lateinit var rootView: View

    // created this as global variable to destroy the contextual action mode
    private lateinit var mActionMode: ActionMode

    // to add or remove items from this arrayList
    private var selectedRecipes = arrayListOf<FavoritesEntity>()

    private var myViewHolders = arrayListOf<MyViewHolder>()

    private var favoriteRecipes = emptyList<FavoritesEntity>()

    class MyViewHolder(private val binding: FavoriteRecipesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoritesEntity) {
            // binding the variable in favorites_recipes_row_layout with this favoritesEntity
            // from our parameters of this bind function (favoritesEntity)
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoriteRecipesRowLayoutBinding.inflate(layoutInflater, parent, false)

                return MyViewHolder(binding)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        myViewHolders.add(holder)
        // store this rootView inside this root view global variable
        rootView = holder.itemView.rootView

        // on this bindViewHolder, we're getting each and every row here from our recyclerview
        // and we're storing that row dynamically inside selecting the recipe variable
        val currentRecipe = favoriteRecipes[position]

        holder.bind(currentRecipe)

        /**
         * Single Click Listener
         * */

        // create a listener for our saved recipe
        holder.itemView.favoriteRecipesRowLayout.setOnClickListener {

            // apply the selection or that style for our recipe only if a multiSelection is true
            if (multiSelection) {
                applySelection(holder, currentRecipe)
            } else {
                val gotoDetailsActivity =
                    // this is to navigate FavoriteRecipesFragment to DetailsActivity
                    FavoriteRecipesFragmentDirections.actionFavoriteRecipesFragmentToDetailsActivity(
                        currentRecipe.result
                    )

                holder.itemView.findNavController().navigate(gotoDetailsActivity)
            }
        }

        /**
         * Long click listener
         * */

        // add click listener for our row here for our favorite_recipes_row_layout
        holder.itemView.favoriteRecipesRowLayout.setOnLongClickListener {

            // multi selection
            if (!multiSelection) {
                multiSelection = true
                // to start our action mode and to actually start our contextual action mode,
                // we will need to use one more parameter inside our favoriteRecipesAdapter.
                requireActivity.startActionMode(this)
                applySelection(holder, currentRecipe)
                true
            } else {
                multiSelection = false
                false
            }

        }

    }

    // changing color of selected recipes for deletion
    private fun applySelection(holder: MyViewHolder, currentRecipe: FavoritesEntity) {
        if (selectedRecipes.contains(currentRecipe)) {
            selectedRecipes.remove(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)

        } else {
            selectedRecipes.add(currentRecipe)
            changeRecipeStyle(holder, R.color.cardBackgroundLightColor, R.color.colorPrimary)
        }

        applyActionModeTitle()
    }

    private fun changeRecipeStyle(holder: MyViewHolder, backgroundColor: Int, strokeColor: Int) {
        holder.itemView.favoriteRecipesRowLayout.setBackgroundColor(
            ContextCompat.getColor(requireActivity, backgroundColor)
        )
        holder.itemView.favorite_row_cardView.strokeColor =
            ContextCompat.getColor(requireActivity, strokeColor)
    }

    private fun applyActionModeTitle() {
        when(selectedRecipes.size) {
            // set to 0 if selected item is empty
            0 -> {
                mActionMode.finish()
            }
            1 -> {
                mActionMode.title = "${selectedRecipes.size} item selected"
            }
            // else if more that 1 item is selected
            else -> {
                mActionMode.title = "${selectedRecipes.size} items selected"
            }
        }
    }

    override fun getItemCount(): Int {
        return favoriteRecipes.size
    }

    // whenever we show a new contextual action mode
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        // inflate our new menu
        mode?.menuInflater?.inflate(R.menu.favorites_contextual_menu, menu)

        // to set the title of our contextual action mode
        mActionMode = mode!!

        // when we show our contextual action mode, this will set our status bar color to darkerColor (onCreateActionMode)
        //  or ContextualStatusBarColor (onDestroyActionMode)
        applyStatusBarColor(R.color.contextualStatusBarColor)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        // if you clicked the trash button, your selected item/s will be deleted in this condition
        if(item?.itemId == R.id.delete_favorite_recipe_menu) {
            selectedRecipes.forEach {
                // "it" is considered as selectedRecipes. it also refers to our selectedRecipes
                // basically our selectedRecipes are list contains those favoritesEntity
                mainViewModel.deleteFavoriteRecipe(it)
            }
            showSnackBar("${selectedRecipes.size} Recipe/s removed.")

            multiSelection = false
            selectedRecipes.clear()
            mode?.finish()
        }

        return true
    }

    // when we get out of our contextual action mode
    override fun onDestroyActionMode(actionMode: ActionMode?) {
        myViewHolders.forEach { holder ->
            changeRecipeStyle(holder, R.color.cardBackgroundColor, R.color.strokeColor)
        }
        multiSelection = false
        selectedRecipes.clear()
        applyStatusBarColor(R.color.statusBarColor)
    }

    // create function to create a new color for status bar if an item is long clicked
    private fun applyStatusBarColor(color: Int) {
        requireActivity.window.statusBarColor = ContextCompat.getColor(requireActivity, color)
    }

    fun setData(newFavoriteRecipes: List<FavoritesEntity>) {
        val favoriteRecipesDiffUtil =
            RecipesDiffUtil(favoriteRecipes, newFavoriteRecipes)
        val diffUtilResult = DiffUtil.calculateDiff(favoriteRecipesDiffUtil)

        favoriteRecipes = newFavoriteRecipes
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}
            .show()
    }

    // this is to close contextualActionMode if we navigate to other fragment or activities
    fun clearContextualActionMode() {
        if(this::mActionMode.isInitialized) {
            mActionMode.finish()
        }
    }

}