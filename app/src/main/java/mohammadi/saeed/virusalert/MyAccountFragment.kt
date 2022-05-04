package mohammadi.saeed.virusalert

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mohammadi.saeed.virusalert.databinding.FragmentMyAccountBinding

class MyAccountFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentMyAccountBinding.bind(view)

        binding.mainBottomNavigationMenu.selectedItemId = R.id.item3
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
    }
}