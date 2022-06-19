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

    fun setVirusItemSelected(item: Int) {
        sharedPreferences = context.getSharedPreferences("virusItem", Context.MODE_PRIVATE)
        sharedPreferences.edit().putInt("virusItem", item).apply()
    }

    fun getVirusItemSelected() : Int {
        sharedPreferences = context.getSharedPreferences("virusItem", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("virusItem", 0)
    }

    fun setLogged(isLogged : Boolean) {
        sharedPreferences = context.getSharedPreferences("isLogged", Context.MODE_PRIVATE)
        sharedPreferences.edit().putBoolean("isLogged", isLogged).apply()
    }

    fun isLogged() : Boolean {
        sharedPreferences = context.getSharedPreferences("isLogged", Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean("isLogged", false)
    }

    fun setLocationState(lat:Double, long: Double, zoom: Float) {
        sharedPreferences = context.getSharedPreferences("locationState", Context.MODE_PRIVATE)
        sharedPreferences.edit().putFloat("lat", lat.toFloat()).apply()
        sharedPreferences.edit().putFloat("long", long.toFloat()).apply()
        sharedPreferences.edit().putFloat("zoom", zoom).apply()
    }

    fun getLocationState() : ArrayList<Float> {
        sharedPreferences = context.getSharedPreferences("locationState", Context.MODE_PRIVATE)
        return arrayListOf(
            sharedPreferences.getFloat("lat", 0.0f),
            sharedPreferences.getFloat("long", 0.0f),
            sharedPreferences.getFloat("zoom", 0.0f))
    }


}