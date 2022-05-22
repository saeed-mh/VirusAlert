package mohammadi.saeed.virusalert

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import mohammadi.saeed.virusalert.databinding.FragmentMapBinding
import org.json.JSONArray

class MapFragment : Fragment() {
    private fun JSONArray.toArrayList(): ArrayList<String> {
        val list = arrayListOf<String>()
        for (i in 0 until this.length()) {
            list.add(this.getString(i))
        }

        return list
    }

    private val callback = OnMapReadyCallback { googleMap ->
        val deleteUserAPI = "http://192.168.43.121:5000/select_virus_information"
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, deleteUserAPI, null, {

            val latitudes = it.getJSONArray("latitudes")
            val longitudes = it.getJSONArray("longitudes")
            val viruses = it.getJSONArray("viruses")

            val listOfLatitudes = latitudes.toArrayList()
            val listOfLongitudes = longitudes.toArrayList()
            val listOfViruses = viruses.toArrayList()

            Toast.makeText(
                requireContext(),
                "${listOfLatitudes[0].toDouble()}, ${listOfLongitudes[0].toDouble()}",
                Toast.LENGTH_SHORT
            ).show()

            for (index in 0 until latitudes.length()) {
                val users = Users(
                    listOfLatitudes[index].toDouble(), listOfLongitudes[index].toDouble(),
                    listOfViruses[index].toInt()
                )

                googleMap.addCircle(CircleOptions()
                    .center(LatLng(users.latitude, users.longitude))
                    .radius(30.0)
                    .fillColor(Color.RED)
                    .strokeColor(Color.GREEN))

            }

        }, {
            Toast.makeText(context, "اینترنت خود را بررسی کنید", Toast.LENGTH_SHORT).show()
        })
        requestQueue.add((jsonObjectRequest))

        val tehranLocation = LatLng(37.274609, 49.381942)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(tehranLocation, 18f))
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

        binding.mainBottomNavigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item1 -> {
                    MainActivity().replaceFragment(
                        view,
                        R.id.action_mapFragment_to_statisticsCoronaFragment
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

    private fun getAllUsers(context: Context) {
        Requests().getAllUsers(context)
    }
}