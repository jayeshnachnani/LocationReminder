package com.udacity.project4.locationreminders.savereminder.selectreminderlocation


import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.udacity.project4.R
import com.udacity.project4.base.BaseFragment
import com.udacity.project4.base.NavigationCommand
import com.udacity.project4.databinding.FragmentSelectLocationBinding
import com.udacity.project4.locationreminders.reminderslist.ReminderDataItem
import com.udacity.project4.locationreminders.savereminder.SaveReminderViewModel
import com.udacity.project4.utils.setDisplayHomeAsUpEnabled
import org.koin.android.ext.android.inject

class SelectLocationFragment : BaseFragment(), OnMapReadyCallback {

    //Use Koin to get the view model of the SaveReminder
    override val _viewModel: SaveReminderViewModel by inject()
    private lateinit var binding: FragmentSelectLocationBinding
    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private  lateinit var reminderDataItem: ReminderDataItem

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

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
        setPoiClick(map)
        //TODO: zoom to the user location after taking his permission
        askPermissionAndMovetoCurrentLocation()

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

    private fun askPermissionAndMovetoCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this.requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)

        }
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

        return

    }


}
