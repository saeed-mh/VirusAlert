package mohammadi.saeed.virusalert

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
import mohammadi.saeed.virusalert.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    lateinit var binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentLoginBinding.bind(view)

        binding.loginActivityBtnLogIn.setOnClickListener {
            loginUser(view)
        }
    }

    private fun loginUser(view: View) {
        val loginUserAPI =
            "http://192.168.43.121:5000/login_user?username=${binding.loginActivityEmailTextEmail.text}&password=${binding.loginActivityEditTextPassword.text}"
        val requestQueue = Volley.newRequestQueue(this.context)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, loginUserAPI, null, {
            val jsonObject = it.getBoolean("result")
            if (jsonObject) {
                Navigation.findNavController(view)
                    .navigate(R.id.action_loginFragment_to_statisticsCoronaFragment)
            } else
                Toast.makeText(this.context, "نام کاربری و رمز اشتباه است", Toast.LENGTH_SHORT)
                    .show()

        }, {
            Toast.makeText(this.context, "اینترنت خود را بررسی کنید", Toast.LENGTH_SHORT)
                .show()
        })

        requestQueue.add(jsonObjectRequest)
    }
}