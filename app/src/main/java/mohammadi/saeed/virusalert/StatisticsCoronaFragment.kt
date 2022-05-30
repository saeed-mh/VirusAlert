package mohammadi.saeed.virusalert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavDirections
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mohammadi.saeed.virusalert.databinding.FragmentStatisticsCoronaBinding
import org.json.JSONArray
import java.util.ArrayList

class StatisticsCoronaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_statistics_corona, container, false)
    }

    private fun JSONArray.toArrayList(): ArrayList<String> {
        val list = arrayListOf<String>()
        for (i in 0 until this.length()) {
            list.add(this.getString(i))
        }
        return list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentStatisticsCoronaBinding.bind(view)

        binding.mainBottomNavigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
                // items 1 == this fragment
                R.id.item2 -> {
                    MainActivity().replaceFragment(
                        view,
                        R.id.action_statisticsCoronaFragment_to_mapFragment
                    )
                }
                R.id.item3 -> {
                    MainActivity().replaceFragment(
                        view,
                        R.id.action_statisticsCoronaFragment_to_myAccountFragment
                    )
                }
            }
            return@setOnItemSelectedListener true
        }


    }
}