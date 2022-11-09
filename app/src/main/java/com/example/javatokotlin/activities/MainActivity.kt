package com.example.javatokotlin.activities

import androidx.appcompat.app.AppCompatActivity
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import android.os.Bundle
//import kotlinx.android.extensions.*
import android.content.Intent
import android.view.View
import com.example.javatokotlin.app.Constants
import com.example.javatokotlin.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private var etName: EditText? = null
    private var etRepoName: EditText? = null
    private var etLanguage: EditText? = null
    private var etGithubUser: EditText? = null
    private var inputLayoutName: TextInputLayout? = null
    private var inputLayoutRepoName: TextInputLayout? = null
    private var inputLayoutGithubUser: TextInputLayout? = null
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // View binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        // New View Binding Style
        setSupportActionBar(binding.toolbar)
        etName = binding.etName
        etRepoName = binding.etRepoName
        etLanguage = binding.etLanguage
        etGithubUser = binding.etGithubUser
        inputLayoutName = binding.inputLayoutName
        inputLayoutRepoName = binding.inputLayoutRepoName
        inputLayoutGithubUser = binding.inputLayoutGithubUser
    }

    /** Save app username in SharedPreferences  */
    fun saveName(view: View?) {
        if (isNotEmpty(etName, inputLayoutName)) {
            val personName = etName?.text.toString()
            val preferences = getSharedPreferences(Constants.APP_SHARED_PREFERENCES, MODE_PRIVATE)
            val editor = preferences.edit()
            editor.putString(Constants.KEY_PERSON_NAME, personName)
            editor.apply()
        }

    }

    /** Search repositories on github  */
    fun listRepositories(view: View?) {
        if (isNotEmpty(etRepoName, inputLayoutRepoName)) {
            val queryRepo = etRepoName?.text.toString()
            val repoLanguage = etLanguage?.text.toString()

            val intent = Intent(this@MainActivity, DisplayActivity::class.java)
            intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_REPO)
            intent.putExtra(Constants.KEY_REPO_SEARCH, queryRepo)
            intent.putExtra(Constants.KEY_LANGUAGE, repoLanguage)
            startActivity(intent)
        }
    }

    /** Search repositories of a particular github user  */
    fun listUserRepositories(view: View?) {
        if (isNotEmpty(etGithubUser, inputLayoutGithubUser)) {
            val githubUser = etGithubUser?.text.toString()
            val intent = Intent(this@MainActivity, DisplayActivity::class.java)
            intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_USER)
            intent.putExtra(Constants.KEY_GITHUB_USER, githubUser)
            startActivity(intent)
        }

    }

    /** Validation  */
    private fun isNotEmpty(editText: EditText?, textInputLayout: TextInputLayout?): Boolean {
        return if (editText?.text.toString().isEmpty()) {
            textInputLayout?.error = "Cannot be blank"
            false
        } else {
            textInputLayout?.isErrorEnabled = false
            true
        }
    }


}