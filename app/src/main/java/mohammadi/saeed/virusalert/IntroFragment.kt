package mohammadi.saeed.virusalert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

        introActivityBtnLogIn.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_loginFragment)
        }

        introActivityBtnSignUp.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_signupFragment)
        }

    }
}