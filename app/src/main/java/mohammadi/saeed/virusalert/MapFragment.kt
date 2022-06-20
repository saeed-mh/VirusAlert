package mohammadi.saeed.virusalert

import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.*
import mohammadi.saeed.virusalert.databinding.FragmentMapBinding
import mohammadi.saeed.virusalert.model.SharedPrefData

class MapFragment() : Fragment() {
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var update: Job
    lateinit var sharedPrefData: SharedPrefData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val dialog = AlertDialog.Builder(this@MapFragment.requireContext())
            dialog.setCancelable(true)
            dialog.setMessage("آیا میخواهید از برنامه خارج شوید؟")
            dialog.setPositiveButton("Yes") { _, _ ->
                requireActivity().finish()
            }
            dialog.setNegativeButton("No") { alertDialog, _ ->
                alertDialog.cancel()
            }
            val alertDialog = dialog.create()
            alertDialog.show()
        }
        onBackPressedCallback.isEnabled = true
    }


    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->

        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
            if (location != null) {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener {
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 16f))
                }
            } else {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(32.4279, 53.6880), 6f))
            }
        }

        //  update map with coroutines
        update = GlobalScope.launch(Dispatchers.Main) {
            while (true) {
                delay(5000)
                Requests().fetchUsersAndShowOnMap(requireContext(), googleMap)
            }
        }
        update.start()


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

        val locationManager = activity!!.getSystemService(LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            binding.txtShowErrorDisableGPS.visibility = View.VISIBLE
        } else {
            binding.txtShowErrorDisableGPS.visibility = View.INVISIBLE
        }

        updatingLocation()

        binding.mainBottomNavigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item1 -> {
                    MainActivity().replaceFragment(
                        view,
                        R.id.action_mapFragment_to_statisticsVirus
                    )
                }
                // item 2 == this fragment
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                for (location in p0.locations) {
                    Requests().updateLatitudeLongitude(requireContext(), sharedPrefData.getUserName(), location.latitude, location.longitude)
                }
            }
        }

        getLocationUpdates()
    }



    private fun getLocationUpdates() {
        locationRequest.interval = 5000
        locationRequest.fastestInterval = 5000
        locationRequest.smallestDisplacement = 0f
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function

    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    override fun onDestroy() {
        super.onDestroy()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        update.cancel()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
}