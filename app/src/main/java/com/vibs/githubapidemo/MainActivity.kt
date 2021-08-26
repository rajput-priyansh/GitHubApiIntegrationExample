package com.vibs.githubapidemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.vibs.githubapidemo.api.ResponseManager
import com.vibs.githubapidemo.databinding.ActivityMainBinding
import com.vibs.githubapidemo.viewmodels.MainViewModel
import java.util.HashMap

class MainActivity : AppCompatActivity(), GitHubNavigation {

    companion object{
        const val PER_PAGE_SIZE = 10
    }

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private lateinit var binding: ActivityMainBinding

    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private var currentPageCount = 1;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentNavHost) as NavHostFragment

        navController = navHostFragment.navController

        setContentView(binding.root)

        initData()

        initUi()

        getRepository("rajput")
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

    /**
     * update the UI and title
     */
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

    /**
     * Make API call to get repository
     */
    private fun getRepository(query: String) {
        if (currentPageCount == 1) {
            showProgressDialog()
        }
        viewModel.apiGetGitHubRepository(query, PER_PAGE_SIZE, currentPageCount).observe(this, {
            hideProgressDialog()
            when (it) {
                is ResponseManager.Success -> {
                    viewModel.setResponseRepositories(it.data)

                    if (!it.data.message.isNullOrEmpty()) {
                        Toast.makeText(this, it.data.message, Toast.LENGTH_SHORT).show()
                    }
                }
                is ResponseManager.Error -> {
                    AppUtil.displaySimpleMessageDialog(this, it.message)
                }
                else -> {

                }
            }
        })
    }

    /**
     * Hide progress bar
     */
    private fun hideProgressDialog() {
        binding.rvProgress.visibility = View.GONE
    }

    /**
     * Show Progress bar for first page data only
     */
    private fun showProgressDialog() {
        binding.rvProgress.visibility = View.VISIBLE
    }

    override fun onNavigation(pageId: Int) {
        findNavController(R.id.fragmentNavHost).navigate(pageId)
    }

    override fun getCurrentPAgeCount(): Int {
        return currentPageCount
    }
}