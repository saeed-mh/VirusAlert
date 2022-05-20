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
    private lateinit var userNamePref: SharedPreferences
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
        userNamePref = requireContext().getSharedPreferences("userName", Context.MODE_PRIVATE)

        binding.signupActivityBtnSignUp.setOnClickListener {
            signUpUser(view)
        }
    }

    private fun signUpUser(view: View) {
        val createUserAPI =
            "http://192.168.43.121:5000/create_user?username=${binding.signupActivityEmailTextEmail.text}&password=${binding.signupActivityEditTextPassword.text}"
        val requestQueue = Volley.newRequestQueue(this.context)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, createUserAPI, null, {
            val jsonObject = it.getBoolean("result")
            if (jsonObject) {
                val editor = userNamePref.edit()
                editor.putString("userName", binding.signupActivityEmailTextEmail.text.toString()).apply()

                Navigation.findNavController(view)
                    .navigate(R.id.action_signupFragment_to_statisticsCoronaFragment)
            }
            else
                Toast.makeText(this.context, "این نام کاربری وجود دارد", Toast.LENGTH_SHORT).show()
        }, {
            Toast.makeText(this.context,"مشکل اتصال اینترنت!", Toast.LENGTH_SHORT).show()
            return@JsonObjectRequest
        })

        requestQueue.add(jsonObjectRequest)
    }
}