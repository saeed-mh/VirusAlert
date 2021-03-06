package mohammadi.saeed.virusalert.model

import android.content.Context
import android.content.SharedPreferences

class SharedPrefData (val context: Context) {
    private lateinit var sharedPreferences: SharedPreferences

    fun setUserName(name: String) {
        sharedPreferences = context.getSharedPreferences("userName", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("userName", name).apply()
    }

    fun getUserName() : String {
        sharedPreferences = context.getSharedPreferences("userName", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userName", "null")!!.toString()
    }

    fun setLogged(isLogged : Boolean) {
        sharedPreferences = context.getSharedPreferences("isLogged", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLogged", isLogged).apply()
    }

    fun isLogged() : Boolean {
        sharedPreferences = context.getSharedPreferences("isLogged", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLogged", false)
    }

}