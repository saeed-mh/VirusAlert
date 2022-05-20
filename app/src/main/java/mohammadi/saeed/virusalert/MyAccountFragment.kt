package mohammadi.saeed.virusalert

import android.content.Context
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mohammadi.saeed.virusalert.databinding.FragmentMyAccountBinding

class MyAccountFragment : Fragment() {
    lateinit var binding: FragmentMyAccountBinding
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
        binding.myAccountFragmentBtnApplyChanges.isEnabled = false
        binding.mainBottomNavigationMenu.selectedItemId = R.id.item3
        val userNamePref = requireContext().getSharedPreferences("userName", Context.MODE_PRIVATE)


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
}