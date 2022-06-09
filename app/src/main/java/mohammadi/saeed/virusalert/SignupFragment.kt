package mohammadi.saeed.virusalert

import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import mohammadi.saeed.virusalert.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    private lateinit var binding: FragmentSignupBinding

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
            if (checkInputEditText())
                signUpUser(view)
        }

        binding.signUpActivityShowPassword.setOnCheckedChangeListener { _, isChecked ->
            val txtPass = view.findViewById<EditText>(R.id.signupActivityEditTextPassword)
            if (!isChecked) {
                binding.signupActivityEditTextPassword.transformationMethod = PasswordTransformationMethod()
                txtPass.setSelection(txtPass.length())
            } else {
                binding.signupActivityEditTextPassword.transformationMethod = HideReturnsTransformationMethod()
                txtPass.setSelection(txtPass.length())
            }
        }
    }

    private fun checkInputEditText() : Boolean {
        return if (binding.signupActivityEmailTextEmail.text.trim().isEmpty()) {
            binding.signupActivityEmailTextEmail.error = "وارد کردن نام کاربری ضروری است"
            false
        } else if (binding.signupActivityEditTextPassword.text.trim().isEmpty()) {
            binding.signupActivityEditTextPassword.error = "وارد کردن رمز ضروری است"
            false
        } else {
            true
        }
    }

    private fun signUpUser(view: View) {
        Requests().signupUser(
            requireContext(),
            view,
            binding.signupActivityEmailTextEmail.text,
            binding.signupActivityEditTextPassword.text
        )
    }
}