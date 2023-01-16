package com.example.foody_udemy_training_2.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.navArgs
import com.example.foody_udemy_training_2.R
import com.example.foody_udemy_training_2.data.database.entities.FavoritesEntity
import com.example.foody_udemy_training_2.databinding.ActivityDetailsBinding
import com.example.foody_udemy_training_2.ui.fragments.ingredients.IngredientsFragment
import com.example.foody_udemy_training_2.ui.fragments.instructions.InstructionsFragment
import com.example.foody_udemy_training_2.ui.fragments.overview.OverviewFragment
import com.example.foody_udemy_training_2.util.Constants.Companion.RECIPE_RESULT_KEY
import com.example.foody_udemy_training_2.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

//added annotation for dependency injection to create an instance in MainViewModel, because we are using our MainViewModel inside this class
@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    // before we initialize the pagerAdapter, we need to create a bundle object,
    // but in order to create a bundle object, we need to get the values or the data from our safe args
    // which we have passed from our recipes fragment to our details activity
    private val args by navArgs<DetailsActivityArgs>()

    //initialize MainViewModel
    private val mainViewModel: MainViewModel by viewModels()

    // added two global variables to remove item from favorites
    private var recipeSaved = false
    private var savedRecipeId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // setup actionbar
        setSupportActionBar(binding.toolbar)
        binding.toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // create arraylist of fragments
        val fragments = ArrayList<Fragment>()
        fragments.add(OverviewFragment())
        fragments.add(IngredientsFragment())
        fragments.add(InstructionsFragment())

        // create arrayList of title strings
        val titles = ArrayList<String>()
        titles.add("Overview")
        titles.add("Ingredients")
        titles.add("Instructions")

        // create a bundle object
        val resultBundle = Bundle()
        // calling putParcelable because our result variable from our args is a type of result
        // which is actually a possible, that's how we are able to basically use this function to pass that variable
        resultBundle.putParcelable(RECIPE_RESULT_KEY, args.result)

        // initialize pagerAdapter, be careful for inputting the parameters it should be in order
        val pagerAdapter = com.example.foody_udemy_training_2.adapters.PagerAdapter(
            resultBundle,
            fragments,
            this
        )


        // call view pager
        binding.viewPager2.apply {
            adapter = pagerAdapter
        }

        // set the titles properly
        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }

    // added for favorites icon
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.details_menu, menu)

        // to check if the favorites is saved
        val menuItem = menu?.findItem(R.id.save_to_favorites_menu)
        checkSavedRecipes(menuItem!!)
        
        return true
    }

    // click item finish() if you clicked the back button in DetailsActivity
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        } else if(item.itemId == R.id.save_to_favorites_menu && !recipeSaved) {
            // created else if for favorites icon
            saveToFavorites(item)
        } else if (item.itemId == R.id.save_to_favorites_menu && recipeSaved) {
            // this function is to remove our selected recipe from our favorites entity
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    // created new function to check if the recipe is saved in database
    private fun checkSavedRecipes(menuItem: MenuItem) {
        mainViewModel.readFavoriteRecipes.observe(this) { favoritesEntity ->
            try {
                for (savedRecipe in favoritesEntity) {
                    // check if the Result.id and args.result.id (selected favorite recipe) is true
                    if(savedRecipe.result.id == args.result.id) {
                        changeMenuItemColor(menuItem, R.color.yellow)
                        savedRecipeId = savedRecipe.id
                        recipeSaved = true
                    } else {
                        // change the icon to white if the recipe is not saved in favorites db
                        changeMenuItemColor(menuItem, R.color.white)
                    }
                }
            } catch (e: java.lang.Exception) {
                Log.d("DetailsActivity", e.message.toString())
            }
        }
    }

    // created function to saveFavorites
    private fun saveToFavorites(item: MenuItem) {
        // we created a new favoritesEntity object, with this argument which we have received,
        // when we have navigated from our recipes fragment to our details activity
        val favoritesEntity =
            FavoritesEntity(
                0,
                // pass the actual result of our selected recipe and our recipe can be obtained through our args
                args.result
            )
        // to insert item in favorites entity
        mainViewModel.insertFavoriteRecipe(favoritesEntity)
        changeMenuItemColor(item, R.color.yellow)

        showSnackBar("Recipe saved.")
        // check if recipe is saved in database
        recipeSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity = FavoritesEntity(
            // get the id in checkSavedRecipes
            savedRecipeId,
            args.result
        )

        // called mainViewModel.deleteFavoriteRecipe to delete certain favorites recipe in database
        mainViewModel.deleteFavoriteRecipe(favoritesEntity)

        changeMenuItemColor(item, R.color.white)
        showSnackBar("Removed From Favorites")
        // set if recipe db is false
        recipeSaved = false
    }

    // show snack bar if you add the recipe to favorites
    private fun showSnackBar(message: String) {
        Snackbar.make(
            binding.detailsLayout,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay"){}
            .show()
    }

    // created function to change the color of the star icon
    private fun changeMenuItemColor(item: MenuItem, color: Int) {
        item.icon.setTint(ContextCompat.getColor(this, color))
    }
}