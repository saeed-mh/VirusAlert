package mohammadi.saeed.virusalert

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.RadioGroup
import androidx.navigation.Navigation
import mohammadi.saeed.virusalert.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentSignupBinding.bind(view)

        binding.signupActivityCheckBoxVirus.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked)
                binding.signupActivityRadioGroupVirusRadioGroup.visibility = View.VISIBLE
            else
                binding.signupActivityRadioGroupVirusRadioGroup.visibility = View.INVISIBLE
        }

        binding.signupActivityBtnSignUp.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_statisticsCoronaFragment)
        }
    }
}