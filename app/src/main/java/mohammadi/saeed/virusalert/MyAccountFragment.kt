package mohammadi.saeed.virusalert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import mohammadi.saeed.virusalert.databinding.FragmentMyAccountBinding
import mohammadi.saeed.virusalert.model.SharedPrefData

class MyAccountFragment : Fragment() {
    lateinit var binding: FragmentMyAccountBinding
    lateinit var sharedPrefData: SharedPrefData
    var virusItem = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitApplication()
    }

    private fun exitApplication() {
        val onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val dialog = AlertDialog.Builder(this@MyAccountFragment.requireContext())
            dialog.setCancelable(true)
            dialog.setMessage("آیا میخواهید از برنامه خارج شوید؟")
            dialog.setPositiveButton("بله") { _, _ ->
                requireActivity().finish()
            }
            dialog.setNegativeButton("خیر") { alertDialog, _ ->
                alertDialog.cancel()
            }
            val alertDialog = dialog.create()
            alertDialog.show()
        }
        onBackPressedCallback.isEnabled = true
    }

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

        binding.mainBottomNavigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
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
    }
}