package mohammadi.saeed.virusalert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import mohammadi.saeed.virusalert.databinding.FragmentStatisticsCoronaBinding

class StatisticsCoronaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics_corona, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentStatisticsCoronaBinding.bind(view)

        binding.mainBottomNavigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.item3 -> {
                    MainActivity().replaceFragment(
                        view,
                        R.id.action_statisticsCoronaFragment_to_aboutUsFragment
                    )
                }
            }
            return@setOnItemSelectedListener true
        }
    }
}