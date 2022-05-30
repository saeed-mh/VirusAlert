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
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var sharedPrefData: SharedPrefData
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
        sharedPrefData = SharedPrefData(requireContext())
        binding.myAccountFragmentTxtUserNameTextView.text = sharedPrefData.getUserName()
        binding.myAccountFragmentBtnApplyChanges.isEnabled = false
        binding.mainBottomNavigationMenu.selectedItemId = R.id.item3
        virusItem = sharedPrefData.getVirusItemSelected()
        when (virusItem) {
            0 -> binding.radioButton0.isChecked = true
            1 -> binding.radioButton1.isChecked = true
            2 -> binding.radioButton2.isChecked = true
            3 -> binding.radioButton3.isChecked = true
            4 -> binding.radioButton4.isChecked = true
            else -> {
                binding.radioButton0.isChecked = true
            }
        }

        updatingLocation()


        binding.mainBottomNavigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item1 -> {
                    MainActivity().replaceFragment(
                        view,
                        R.id.action_myAccountFragment_to_statisticsCoronaFragment
                    )
                }
                R.id.item2 -> {
                    MainActivity().replaceFragment(
                        view,
                        R.id.action_myAccountFragment_to_mapFragment
                    )
                }
                // items 3 == this fragment
            }
            return@setOnItemSelectedListener true
        }

        binding.myAccountFragmentRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            binding.myAccountFragmentBtnApplyChanges.isEnabled = true
            virusItem = when (checkedId) {
                R.id.radioButton0 -> 0
                R.id.radioButton1 -> 1
                R.id.radioButton2 -> 2
                R.id.radioButton3 -> 3
                R.id.radioButton4 -> 4
                else -> {
                    0
                }
            }
            sharedPrefData.setVirusItemSelected(virusItem)
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
        Requests().updateVirus(requireContext(), sharedPrefData.getUserName(), virusItem)
    }

    private fun deleteUser() {
        Requests().deleteUser(
            requireContext(),
            requireActivity(),
            binding.myAccountFragmentTxtUserNameTextView.text.toString()
        )
        sharedPrefData.setVirusItemSelected(0)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
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
                        binding.myAccountFragmentTxtUserNameTextView.text.toString(),
                        location.latitude,
                        location.longitude
                    )
                    Toast.makeText(
                        requireContext(),
                        "${location.latitude} : ${location.longitude}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        getLocationUpdates()
    }

    private fun getLocationUpdates() {
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 10000
        locationRequest.smallestDisplacement = 0f
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY //set according to your app function

    }

    override fun onResume() {
        super.onResume()
        startLocationUpdates()
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