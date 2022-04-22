package mohammadi.saeed.virusalert

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun replaceFragment(view: View, action: Int) {
        Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_aboutUsFragment)
    }
}