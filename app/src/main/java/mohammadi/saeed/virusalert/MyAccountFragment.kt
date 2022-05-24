package mohammadi.saeed.virusalert

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.*
import mohammadi.saeed.virusalert.databinding.FragmentMyAccountBinding

class MyAccountFragment : Fragment() {
    lateinit var binding: FragmentMyAccountBinding
    lateinit var fusedLocationProviderClient:FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    var virusItem = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMyAccountBinding.bind(view)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationRequest = LocationRequest.create()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0 ?: return
                for (location in p0.locations) {
                    Requests().update_latitude_longitude(requireContext(), binding.myAccountFragmentTxtUserNameTextView.text.toString(), location.latitude, location.longitude)
                    Toast.makeText(requireContext(), "${location.latitude} : ${location.longitude}", Toast.LENGTH_LONG).show()
                    //Log.d("ChangeLoc", "${location.latitude} : ${location.longitude}")
                }
            }
        }

        binding.myAccountFragmentBtnApplyChanges.isEnabled = false
        binding.mainBottomNavigationMenu.selectedItemId = R.id.item3
        val userNamePref = requireContext().getSharedPreferences("userName", Context.MODE_PRIVATE)

        getLocationUpdates()

        binding.myAccountFragmentTxtUserNameTextView.text = userNamePref.getString("userName", " null ")

        binding.mainBottomNavigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item1 -> {
                    MainActivity().replaceFragment(view, R.id.action_myAccountFragment_to_statisticsCoronaFragment)
                }
                R.id.item2 -> {
                    MainActivity().replaceFragment(view, R.id.action_myAccountFragment_to_mapFragment)
                }
                // items 3 == this fragment
            }
            return@setOnItemSelectedListener true
        }

        binding.myAccountFragmentRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.myAccountFragmentBtnApplyChanges.isEnabled = true
            virusItem = when(checkedId) {
                R.id.radioButton0 -> 0
                R.id.radioButton1 -> 1
                R.id.radioButton2 -> 2
                R.id.radioButton3 -> 3
                R.id.radioButton4 -> 4
                else -> {
                    0
                }
            }
        }

        binding.myAccountFragmentBtnApplyChanges.setOnClickListener {
            updateVirus()
            binding.myAccountFragmentBtnApplyChanges.isEnabled = false
        }

        binding.myAccountFragmentBtnDeleteAccount.setOnClickListener {
            deleteUser()
        }
    }

    private fun updateVirus() {
        Requests().updateVirus(requireContext(), virusItem)
    }

    private fun deleteUser() {
        Requests().deleteUser(requireContext(), requireActivity(), binding.myAccountFragmentTxtUserNameTextView.text.toString())
    }

    private fun getLocationUpdates() {
        locationRequest.interval = 20000
        locationRequest.fastestInterval = 20000
        locationRequest.smallestDisplacement = 0f // 170 m = 0.1 mile
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function
        locationCallback

    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }
}