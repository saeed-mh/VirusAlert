package mohammadi.saeed.virusalert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
            if (checkInputEditText())
                loginUser(view)
        }
    }

    private fun checkInputEditText() : Boolean {
        return if (binding.loginActivityEmailTextEmail.text.trim().isEmpty()) {
            binding.loginActivityEmailTextEmail.error = "وارد کردن نام کاربری ضروری است"
            false
        } else if (binding.loginActivityEditTextPassword.text.trim().isEmpty()) {
            binding.loginActivityEditTextPassword.error = "وارد کردن رمز ضروری است"
            false
        } else {
            true
        }
    }

    private fun loginUser(view: View) {
        Requests().loginUser(requireContext(), view, binding.loginActivityEmailTextEmail.text, binding.loginActivityEditTextPassword.text)
    }
}