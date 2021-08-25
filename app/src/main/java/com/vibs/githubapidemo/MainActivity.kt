package com.vibs.githubapidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.vibs.githubapidemo.databinding.ActivityMainBinding
import com.vibs.githubapidemo.viewmodels.MainViewModel

class MainActivity : AppCompatActivity(), GitHubNavigation {

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentNavHost) as NavHostFragment

        navController = navHostFragment.navController

        setContentView(binding.root)

        initData()

        initUi()
    }

    private fun initData() {

    }

    private fun initUi() {
        setSupportActionBar(binding.toolBar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Set the title
        supportActionBar?.title = "Repository"

    }

    private val navigationListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            updateUi(destination.id)
        }


    override fun onResume() {
        super.onResume()
        navController.addOnDestinationChangedListener(navigationListener)
    }

    override fun onPause() {
        super.onPause()
        navController.removeOnDestinationChangedListener(navigationListener)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun updateUi(id: Int) {
        supportActionBar?.title = when (id) {
            R.id.HomeFragment -> {
                "Repositories"
            }
            R.id.RepositoryDetailsFragment -> {
                "Repository Details"
            }
            R.id.WebViewFragment -> {
                "Project"
            }
            else -> {
                "Repositories"
            }
        }

    }

    override fun onNavigation(pageId: Int) {
        findNavController(R.id.fragmentNavHost).navigate(pageId)
    }
}