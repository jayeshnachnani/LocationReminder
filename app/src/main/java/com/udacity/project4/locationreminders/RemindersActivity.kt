package com.udacity.project4.locationreminders

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.udacity.project4.R
import com.udacity.project4.databinding.ActivityRemindersBinding
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import kotlinx.android.synthetic.main.activity_reminders.*

import org.koin.androidx.viewmodel.ext.android.viewModel
/**
 * The RemindersActivity that holds the reminders fragments
 */
class RemindersActivity : AppCompatActivity() {

    private val viewModel: RemindersListViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_reminders)
        val binding: ActivityRemindersBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_reminders
        )

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                (nav_host_fragment as NavHostFragment).navController.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
