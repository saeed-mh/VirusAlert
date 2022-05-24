package mohammadi.saeed.virusalert

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import mohammadi.saeed.virusalert.databinding.FragmentLoginBinding

class IntroFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intro, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val introActivityBtnLogIn = view.findViewById<Button>(R.id.introActivity_btn_LogIn)
        val introActivityBtnSignUp = view.findViewById<Button>(R.id.introActivity_btn_SignUp)

        checkPermissions()

        introActivityBtnLogIn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_loginFragment)
        }

        introActivityBtnSignUp.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_signupFragment)
        }
    }

    fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 222)
        } else {
            Toast.makeText(requireContext(), "شما مجوز لوکیشن را دارید", Toast.LENGTH_SHORT).show()
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