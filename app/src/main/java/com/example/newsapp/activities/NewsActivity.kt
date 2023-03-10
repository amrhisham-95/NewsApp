package com.example.newsapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import com.example.newsapp.R
import com.example.newsapp.databinding.ActivityNewsBinding
import com.example.newsapp.ui.DrawerLocker
import com.example.newsapp.ui.MainActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class NewsActivity : AppCompatActivity() , DrawerLocker {
    private lateinit var binding: ActivityNewsBinding
    private lateinit var toggle: ActionBarDrawerToggle


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,binding.toolbar,
            R.string.open,
            R.string.close
        )

        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)


        binding.navigationDrawerView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.Live -> {
                    Navigation.findNavController(this, R.id.fragment_host2).navigate(R.id.action_newsFragment_to_detailLiveFragment)
                }

                R.id.Sports -> {
                    Navigation.findNavController(this, R.id.fragment_host2).navigate(R.id.action_newsFragment_to_detailSportsFragment)
                }

                R.id.Sciences -> {
                    Navigation.findNavController(this, R.id.fragment_host2).navigate(R.id.action_newsFragment_to_detailSciencesFragment)

                }

                R.id.OtherTopics -> {
                    Navigation.findNavController(this, R.id.fragment_host2).navigate(R.id.action_newsFragment_to_otherTopicFragment)
                }

                R.id.signOut -> {
                    Toast.makeText(
                        this,
                        "Sign In Again, If You Want To Get A Service",
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    Firebase.auth.signOut()
                }

            }
            true
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onResume() {
        super.onResume()

        //To Add ToolBar(in ActionBar) for all fragments with appBarConfiguration
        setSupportActionBar(binding.toolbar)
        binding.toolbar.title= "News"
        binding.toolbar.setTitleTextColor(Color.WHITE)


    }

    override fun setDrawerLocked(shouldLock: Boolean) {
        if (shouldLock) {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        } else {
            binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }
}