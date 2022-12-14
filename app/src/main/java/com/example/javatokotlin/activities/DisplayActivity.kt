package com.example.javatokotlin.activities

import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.navigation.NavigationView
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.javatokotlin.adapters.DisplayAdapter
import com.example.javatokotlin.retrofit.GithubAPIService
import android.os.Bundle
import com.example.javatokotlin.R
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.javatokotlin.retrofit.RetrofitClient
import androidx.appcompat.app.ActionBarDrawerToggle
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import com.example.javatokotlin.models.SearchResponse
import androidx.core.view.GravityCompat
import com.example.javatokotlin.app.Constants
import com.example.javatokotlin.extentions.showErrorMessage
import com.example.javatokotlin.extentions.toast
import com.example.javatokotlin.databinding.ActivityDisplayBinding
import com.example.javatokotlin.models.Repository
import io.realm.Realm
import io.realm.RealmConfiguration
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class DisplayActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var displayAdapter: DisplayAdapter
    private var realm: Realm? = null
    private lateinit var binding: ActivityDisplayBinding
    private lateinit var navigationView: NavigationView

    private var browsedRepositories: List<Repository> = mutableListOf()

    // this variable will be instantiated only in the first call
    private val githubAPIService: GithubAPIService by lazy {
        RetrofitClient.githubAPIService
    }

    companion object {
        private val TAG = DisplayActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View Binding
        binding = ActivityDisplayBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar!!.title = "Showing Browsed Results"
        recyclerView = binding.recyclerView


        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView.layoutManager = layoutManager

        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .allowQueriesOnUiThread(true)
            .allowWritesOnUiThread(true)
            .build()
//        Realm Db
        realm = Realm.getInstance(config)

        navigationView = binding.navView
        navigationView.setNavigationItemSelectedListener(this)
        setAppUsername()

        drawerLayout = binding.drawerLayout
        val drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        val intent = intent
        if (intent.getIntExtra(Constants.KEY_QUERY_TYPE, -1) == Constants.SEARCH_BY_REPO) {
            val queryRepo = intent.getStringExtra(Constants.KEY_REPO_SEARCH) ?: ""
            val repoLanguage = intent.getStringExtra(Constants.KEY_LANGUAGE) ?: ""
            Log.d(TAG, "onCreate: $queryRepo$repoLanguage")
            fetchRepositories(queryRepo, repoLanguage)
        } else {
            val githubUser = intent.getStringExtra(Constants.KEY_GITHUB_USER) ?: ""
            fetchUserRepositories(githubUser)
        }
    }

    private fun setAppUsername() {
        val preferences = getSharedPreferences(Constants.APP_SHARED_PREFERENCES, MODE_PRIVATE)
        val name = preferences.getString(Constants.KEY_PERSON_NAME, "User")
        val header = navigationView.getHeaderView(0).findViewById<TextView>(R.id.txvName)
        header.text = name

    }

    private fun fetchUserRepositories(githubUser: String) {
        githubAPIService.searchRepositoriesByUser(githubUser)
            .enqueue(object : Callback<List<Repository>> {
                override fun onResponse(
                    call: Call<List<Repository>>,
                    response: Response<List<Repository>>
                ) {
                    handleResponse({
                        response.body()?.let {
                            browsedRepositories = it
                        }
                    }, response)
                }

                override fun onFailure(call: Call<List<Repository>>, error: Throwable) {
                    toast(error.message ?: "Error Fetching Data")
                }
            })
    }

    private fun fetchRepositories(queryRepo: String, repoLanguage: String) {
        var repo = queryRepo
        val query: MutableMap<String, String> = HashMap()
        if (repoLanguage.isNotEmpty()) repo += " language:$repoLanguage"
        query["q"] = repo
        githubAPIService.searchRepositories(query).enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                handleResponse({
                    response.body()?.items?.let {
                        browsedRepositories = it
                    }
                }, response)
            }

            override fun onFailure(call: Call<SearchResponse>, error: Throwable) {
                toast(error.toString())
            }
        })
    }

    private fun <T : Any> handleResponse(
        setBrowsedRepositories: () -> Unit,
        response: Response<T>
    ) {
        if (response.isSuccessful) {
            Log.d(TAG, "posts loaded from API: $response")
            setBrowsedRepositories()
            if (browsedRepositories.isNotEmpty())
                setupRecyclerView(browsedRepositories)
            else toast("No Item Found")
        } else {
            Log.i(TAG, "error $response")
            showErrorMessage(response.errorBody()!!)
        }
    }

    private fun setupRecyclerView(items: List<Repository>) {
        displayAdapter = DisplayAdapter(this, items)
        recyclerView.adapter = displayAdapter
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        menuItem.isChecked = true
        closeDrawer()
        when (menuItem.itemId) {
            R.id.item_bookmark -> {
                consumeMenuEvent({ showBookmarks() }, "Showing Bookmarks")
            }
            R.id.item_browsed_results -> {
                consumeMenuEvent({ showBrowsedResults() }, "Showing Browsed Results")
            }

        }
        return true
    }

    /* This type corresponds to the void type in Java.
    // HOF Disadvantages
    // The passed functions are stored as objects
    // Inline Functions Disadvantages
    // Too Much of its Usage Increases Byte code(for short functions)
    */
    private inline fun consumeMenuEvent(myFun: () -> Unit, msg: String) {
        myFun()
        supportActionBar!!.title = msg
    }

    private fun showBrowsedResults() {
        displayAdapter.swap(browsedRepositories)
    }

    private fun showBookmarks() {
        realm!!.executeTransaction { realm ->
            val repositories = realm.where(
                Repository::class.java
            ).findAll()
            displayAdapter.swap(repositories)
        }
    }

    private fun closeDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) closeDrawer() else {
            super.onBackPressed()
//            realm!!.close()
        }
    }

}