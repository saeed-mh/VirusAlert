package mohammadi.saeed.virusalert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mohammadi.saeed.virusalert.databinding.FragmentAboutUsBinding

class AboutUsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAboutUsBinding.bind(view)

        binding.mainBottomNavigationMenu.selectedItemId = R.id.item3
        binding.mainBottomNavigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item1 -> {
                    MainActivity().replaceFragment(
                        view,
                        R.id.action_aboutUsFragment_to_statisticsCoronaFragment
                    )
                }
            }
            return@setOnItemSelectedListener true
        }

    }
}