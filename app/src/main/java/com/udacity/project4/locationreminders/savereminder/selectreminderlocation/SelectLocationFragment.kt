package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.Manifest
import android.annotation.TargetApi
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.REQUEST_FOREGROUND_AND_BACKGROUND_PERMISSION_RESULT_CODE
import com.udacity.project4.locationreminders.REQUEST_FOREGROUND_ONLY_PERMISSIONS_REQUEST_CODE
import com.udacity.project4.locationreminders.RemindersActivity
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject

//Referred to raywenderlich.com to get steps for Google Maps API
class SelectLocationFragment : BaseFragment(), OnMapReadyCallback {

    //Use Koin to get the view model of the SaveReminder
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var reminderDataItem: ReminderDataItem
    private val runningQOrLater = android.os.Build.VERSION.SDK_INT >=
            android.os.Build.VERSION_CODES.Q

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }
    //Referred to raywenderlich.com to get steps for Google Maps API
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_select_location, container, false)

        binding.viewModel = _viewModel
        binding.lifecycleOwner = this

        setHasOptionsMenu(true)
        setDisplayHomeAsUpEnabled(true)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = this.context?.let { LocationServices.getFusedLocationProviderClient(it) }!!


//        TODO: add the map setup implementation
//        TODO: zoom to the user location after taking his permission
//        TODO: add style to the map
//        TODO: put a marker to location that the user selected


//        TODO: call this function after the user confirms on the selected location
        //onLocationSelected()

        return binding.root
    }

    private fun onLocationSelected() {
        //        TODO: When the user confirms on the selected location,
        //         send back the selected location details to the view model
        //         and navigate back to the previous fragment to save the reminder and add the geofence
        //NavigationCommand.BackTo(R.id.saveReminderFragment)
        //_viewModel.navigationCommand.value = NavigationCommand.BackTo(R.id.saveReminderFragment)
        _viewModel.navigationCommand.postValue(NavigationCommand.To(SelectLocationFragmentDirections.actionSelectLocationFragmentToSaveReminderFragment()))
        //NavigationCommand.To(R.id.saveReminderFragment)
        //SelectLocationFragmentDirections.toSaveReminder()
        // )
        //)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.map_options, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // TODO: Change the map type based on the user's selection.
        R.id.normal_map -> {
            true
        }
        R.id.hybrid_map -> {
            true
        }
        R.id.satellite_map -> {
            true
        }
        R.id.terrain_map -> {
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Add a marker in Sydney and move the camera
        val kondhwa = LatLng(18.28, 73.53)
        val zoomLevel = 10f
        //map.addMarker(MarkerOptions().position(kondhwa).title("Marker in Kondhwa"))
        //map.moveCamera(CameraUpdateFactory.newLatLngZoom((kondhwa),zoomLevel))
        //map.moveCamera(CameraUpdateFactory.newLatLngBounds(La))
        //TODO: zoom to the user location after taking his permission
        askPermissionAndMovetoCurrentLocation()
        setPoiClick(map)

    }

    private fun setPoiClick(map: GoogleMap) {
        map.setOnPoiClickListener { poi ->
            val poiMarker = map.addMarker(
                    MarkerOptions()
                            .position(poi.latLng)
                            .title(poi.name)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))


            )
            //Save to DB
            //_viewModel.saveReminder(reminderData = ReminderDataItem(test))
            poiMarker.showInfoWindow()
            //reminderDataItem = ReminderDataItem("placeholder","tets","test",poi.latLng.latitude,poi.latLng.longitude)
            _viewModel.latitude.value = poi.latLng.latitude
            _viewModel.longitude.value = poi.latLng.longitude
            _viewModel.reminderSelectedLocationStr.value = poi.name
            onLocationSelected()
            //_viewModel.saveReminder(reminderDataItem)
            /*reminderDataItem.title = "test"
            reminderDataItem.location = "test"
            reminderDataItem.description = "test"
            reminderDataItem.location = "test2"
            reminderDataItem.latitude = poi.latLng.latitude
            reminderDataItem.latitude = poi.latLng.longitude*/

            //_viewModel.saveReminder(reminderDataItem)
        }
    }

    //Referred to https://www.raywenderlich.com/230-introduction-to-google-maps-api-for-android-with-kotlin
    // to get steps for Google Maps API
    private fun askPermissionAndMovetoCurrentLocation() {
        if (foregroundAndBackgroundLocationPermissionApproved()) {
            moveToCurrentLocation()
        } else {
            requestForegroundAndBackgroundLocationPermissions()
        }
        return

    }

    private fun moveToCurrentLocation(){
            if (ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestForegroundAndBackgroundLocationPermissions()
            } else {
                map.isMyLocationEnabled = true
                fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->
                    // Got last known location. In some rare situations this can be null.
                    // 3
                    if (location != null) {
                        lastLocation = location
                        val currentLatLng = LatLng(location.latitude, location.longitude)
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                    }
                }
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
                this.requireActivity().parent,
                permissionsArray,
                resultCode
        )
    }

    @TargetApi(29)
    fun foregroundAndBackgroundLocationPermissionApproved(): Boolean {
        val foregroundLocationApproved = (
                PackageManager.PERMISSION_GRANTED ==
                        ActivityCompat.checkSelfPermission(this.requireContext(),
                                Manifest.permission.ACCESS_FINE_LOCATION))
        val backgroundPermissionApproved =
                if (runningQOrLater) {
                    PackageManager.PERMISSION_GRANTED ==
                            ActivityCompat.checkSelfPermission(
                                    this.requireContext(), Manifest.permission.ACCESS_BACKGROUND_LOCATION
                            )
                } else {
                    true
                }
        return foregroundLocationApproved && backgroundPermissionApproved
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
            Snackbar.make(
                    this.requireView(),
                    R.string.permission_denied_explanation,
                    Snackbar.LENGTH_INDEFINITE
            )
                    /*.setAction(R.string.settings) {
                        startActivity(Intent().apply {
                            action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                            data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        })
                    }.show()*/
        } else {
            moveToCurrentLocation()
        }
    }


}
