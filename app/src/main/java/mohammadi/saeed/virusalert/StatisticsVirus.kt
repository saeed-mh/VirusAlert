package mohammadi.saeed.virusalert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import mohammadi.saeed.virusalert.databinding.FragmentStatisticsVirusBinding

class StatisticsVirus : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_statistics_virus, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitApplication()
    }

    private fun exitApplication() {
        val onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val dialog = AlertDialog.Builder(this@StatisticsVirus.requireContext())
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentStatisticsVirusBinding.bind(view)

        binding.mainBottomNavigationMenu.setOnItemSelectedListener {
            when (it.itemId) {
                // item 1 == this fragment
                R.id.item2 -> {
                    MainActivity().replaceFragment(view, R.id.action_statisticsVirus_to_mapFragment)
                }
                R.id.item3 -> {
                    MainActivity().replaceFragment(view, R.id.action_statisticsVirus_to_myAccountFragment)
                }
            }
            return@setOnItemSelectedListener true
        }
    }
}