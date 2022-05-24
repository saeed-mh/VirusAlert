package mohammadi.saeed.virusalert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
        var corona = 0
        var measles = 0
        var flu = 0

        val deleteUserAPI = "http://192.168.43.121:5000/select_virus_information"
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, deleteUserAPI, null, {
            val viruses = it.getJSONArray("viruses").toArrayList()
            viruses.forEach {
                when (it) {
                    "1" -> corona += 1
                    "2" -> measles += 1
                    "3" -> flu += 1
                }
            }

        }) {
            Toast.makeText(context, "مشکل در اتصال به سرور", Toast.LENGTH_SHORT).show()
        }
        requestQueue.add((jsonObjectRequest))


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