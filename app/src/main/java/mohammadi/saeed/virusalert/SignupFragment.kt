package mohammadi.saeed.virusalert

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import mohammadi.saeed.virusalert.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    private lateinit var binding : FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignupBinding.bind(view)

        binding.signupActivityBtnSignUp.setOnClickListener {
            signUpUser(view)
        }
    }

    private fun signUpUser(view: View) {
        Requests().signupUser(requireContext(), view, binding.signupActivityEmailTextEmail.text, binding.signupActivityEditTextPassword.text)
    }
}