package mohammadi.saeed.virusalert

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.SharedElementCallback
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.*
import mohammadi.saeed.virusalert.databinding.FragmentMapBinding

class MapFragment() : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var sharedPrefData: SharedPrefData
    lateinit var update: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val dialog = AlertDialog.Builder(this@MapFragment.requireContext())
            dialog.setCancelable(true)
            dialog.setMessage("آیا میخواهید از برنامه خارج شوید؟")
            dialog.setPositiveButton("Yes") { _, _ ->
                requireActivity().finish()
            }
            dialog.setNegativeButton("No") { dialog, _ ->
                dialog.cancel()
            }
            val alertDialog = dialog.create()
            alertDialog.show()
        }
        onBackPressedCallback.isEnabled = true
    }


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
//         update map with coroutines

        update = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                delay(5000)
                Requests().fetchUsersAndShowOnMap(requireContext(), googleMap)
            }
        }
        update.start()
        // update map with thread
//        updateThread = Thread(kotlinx.coroutines.Runnable {
//            while (true) {
//                Thread.sleep(5000)
//                requireActivity().runOnUiThread {
//                    Requests().fetchUsersAndShowOnMap(requireContext(), googleMap)
//                }
//            }
//        })
//        updateThread.start()


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(32.4279, 53.6880), 15f))

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMapBinding.bind(view)
        binding.mainBottomNavigationMenu.selectedItemId = R.id.item2
        sharedPrefData = SharedPrefData(requireContext())

        updatingLocation()

        binding.mainBottomNavigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
                // item 1 == this fragment
                R.id.item3 -> {
                    MainActivity().replaceFragment(
                        view,
                        R.id.action_mapFragment_to_myAccountFragment
                    )
                }
            }
            return@setOnItemSelectedListener true
        }

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun updatingLocation() {
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for (location in p0.locations) {
                    Requests().update_latitude_longitude(
                        requireContext(),
                        sharedPrefData.getUserName(),
                        location.latitude,
                        location.longitude
                    )
                    Log.d("TAG_UPDATE", "onLocationResult: updating........")
                }
            }
        }

        getLocationUpdates()
    }

    private fun getLocationUpdates() {
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 5000
        locationRequest.smallestDisplacement = 0f
        locationRequest.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function

    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        update.cancel()

    }

//    override fun onStop() {
//        super.onStop()
//        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
////        Toast.makeText(requireContext(), "pause", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
////        Toast.makeText(requireContext(), "pause", Toast.LENGTH_SHORT).show()
//    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}