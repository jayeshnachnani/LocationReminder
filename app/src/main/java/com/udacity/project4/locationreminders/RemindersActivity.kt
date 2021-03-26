package com.udacity.project4.locationreminders

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.udacity.project4.R
import com.udacity.project4.authentication.AuthenticationActivity
import com.udacity.project4.authentication.LoginViewModel
import com.udacity.project4.databinding.ActivityRemindersBinding
import com.udacity.project4.locationreminders.reminderslist.RemindersListViewModel
import kotlinx.android.synthetic.main.activity_reminders.*
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * The RemindersActivity that holds the reminders fragments
 */
class RemindersActivity : AppCompatActivity() {

    private val viewModel: RemindersListViewModel by viewModel()

    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.Q


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_reminders)
        val binding: ActivityRemindersBinding = DataBindingUtil.setContentView(
                this,
                R.layout.activity_reminders
        )

        viewModel.State.observe(this, { authState ->
            val authenticationActivityIntent = Intent(this, AuthenticationActivity::class.java)
            when (authState) {
                LoginViewModel.AuthenticationState.AUTHENTICATED -> {
                    //Timber.i("Authenticated")
                }
                else -> startActivity(authenticationActivityIntent)
            }
        })

        checkPermissionsAndStartGeofencing()

    }


    private fun checkPermissionsAndStartGeofencing() {

        if (foregroundAndBackgroundLocationPermissionApproved()) {
            checkDeviceLocationSettingsAndStartGeofence()
        } else {
            requestForegroundAndBackgroundLocationPermissions()
        }
    }

    @TargetApi(29)
    fun requestForegroundAndBackgroundLocationPermissions() {
        if (foregroundAndBackgroundLocationPermissionApproved())
            return
        var permissionsArray = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        val resultCode = when {
            runningQOrLater -> {
                permissionsArray += Manifest.permission.ACCESS_BACKGROUND_LOCATION
                REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
            }
            else -> REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
        }
        //Log.d(com.udacity.project4.locationreminders.geofence.TAG, "Request foreground only location permission")
        ActivityCompat.requestPermissions(
                this@RemindersActivity,
                permissionsArray,
                resultCode
        )
    }

    @TargetApi(29)
    fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(this,
                                Manifest.permission.ACCESS_FINE_LOCATION))
        val backgroundPermissionApproved =
                if (runningQOrLater) {
                    PackageManager.PERMISSION_GRANTED ==
                            ActivityCompat.checkSelfPermission(
                                    this, Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            )
                } else {
                    true
                }
        return foregroundLocationApproved && backgroundPermissionApproved
    }

    fun checkDeviceLocationSettingsAndStartGeofence(resolve: Boolean = true) {
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_LOW_POWER
        }
        val builder = LocationSettingsRequest.Builder().addLocationRequest(locationRequest)
        val settingsClient = LocationServices.getSettingsClient(this)
        val locationSettingsResponseTask =
                settingsClient.checkLocationSettings(builder.build())
        locationSettingsResponseTask.addOnFailureListener { exception ->
            if (exception is ResolvableApiException && resolve) {
                try {
                    exception.startResolutionForResult(this@RemindersActivity,
                            com.udacity.project4.locationreminders.REQUEST_TURN_DEVICE_LOCATION_ON)
                } catch (sendEx: IntentSender.SendIntentException) {
                    //Log.d(com.udacity.project4.locationreminders.geofence.TAG, "Error getting location settings resolution: " + sendEx.message)
                }
            } else {
                /*Snackbar.make(
                        binding.activityMapsMain,
                        R.string.location_required_error, Snackbar.LENGTH_INDEFINITE
                ).setAction(android.R.string.ok) {
                    checkDeviceLocationSettingsAndStartGeofence()
                }.show()*/
            }
        }
        locationSettingsResponseTask.addOnCompleteListener {
            if (it.isSuccessful) {
                //addGeofenceForClue()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == com.udacity.project4.locationreminders.REQUEST_TURN_DEVICE_LOCATION_ON) {
            checkDeviceLocationSettingsAndStartGeofence(false)
        }
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<String>,
            grantResults: IntArray
    ) {
        Log.d(com.udacity.project4.locationreminders.TAG, "onRequestPermissionResult")

        if (
                grantResults.isEmpty() ||
                grantResults[com.udacity.project4.locationreminders.LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_DENIED ||
                (requestCode == com.udacity.project4.locationreminders.REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE &&
                        grantResults[com.udacity.project4.locationreminders.BACKGROUND_LOCATION_PERMISSION_INDEX] ==
                        PackageManager.PERMISSION_DENIED)) {
            /*Snackbar.make(
                    binding.activityMapsMain,
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
            )
                    .setAction(R.string.settings) {
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }.show()*/
        } else {
            checkDeviceLocationSettingsAndStartGeofence()
        }
    }

    companion object {
        internal const val ACTION_GEOFENCE_EVENT =
                "ReminderMainActivity.action.ACTION_GEOFENCE_EVENT"
    }


}

 const val REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE = 33
 const val REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE = 34
 const val REQUEST_TURN_DEVICE_LOCATION_ON = 29
 const val TAG = "RemindersMainActivity"
 const val LOCATION_PERMISSION_INDEX = 0
 const val BACKGROUND_LOCATION_PERMISSION_INDEX = 1
