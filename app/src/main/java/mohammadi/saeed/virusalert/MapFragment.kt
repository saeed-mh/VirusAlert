package mohammadi.saeed.virusalert

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mohammadi.saeed.virusalert.databinding.FragmentMapBinding
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class MapFragment : Fragment() {
    private val callback = OnMapReadyCallback { googleMap ->
        GlobalScope.launch (Dispatchers.Main) {
            while (true) {
                delay(5000)
                Requests().fetchUsersAndShowOnMap(requireContext(), googleMap)
            }
        }

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
}