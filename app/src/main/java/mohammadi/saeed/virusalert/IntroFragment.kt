package mohammadi.saeed.virusalert

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import mohammadi.saeed.virusalert.model.SharedPrefData

class IntroFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onBackPressedCallback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            val dialog = AlertDialog.Builder(this@IntroFragment.requireContext())
            dialog.setCancelable(true)
            dialog.setMessage("آیا میخواهید از برنامه خارج شوید؟")
            dialog.setPositiveButton("Yes") { _, _ ->
                requireActivity().finish()
            }
            dialog.setNegativeButton("No") { alertDialog, _ ->
                alertDialog.cancel()
            }
            val alertDialog = dialog.create()
            alertDialog.show()
        }
        onBackPressedCallback.isEnabled = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val introActivityBtnLogIn = view.findViewById<Button>(R.id.introActivity_btn_LogIn)
        val introActivityBtnSignUp = view.findViewById<Button>(R.id.introActivity_btn_SignUp)
        val sharedPrefData = SharedPrefData(requireContext())

        // if the user has logged in before, it starts form MapFragment
        if (sharedPrefData.isLogged())
            MainActivity().replaceFragment(requireView(), R.id.action_introFragment_to_mapFragment)

        checkPermissions()

        introActivityBtnLogIn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_loginFragment)
        }

        introActivityBtnSignUp.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_signupFragment)
        }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 222)
        }
    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            222 -> if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(requireContext(), "شما مجوز لوکیشن را دارید", Toast.LENGTH_SHORT).show()
//            } else {
//                Toast.makeText(requireContext(), "شما مجوز لوکیشن را ندارید", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }}

}