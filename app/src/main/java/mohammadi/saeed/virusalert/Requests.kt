package mohammadi.saeed.virusalert

import android.app.Activity
import android.content.Context
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray

class Requests {
    var latitudes: JSONArray? = null
    var longitudes: JSONArray? = null
    var viruses: JSONArray? = null

    fun signupUser(context: Context, view: View, userName: Editable, password: Editable) {
        val createUserAPI =
            "http://192.168.43.121:5000/create_user?username=$userName&password=$password"
        val requestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, createUserAPI, null, {
            val jsonObject = it.getBoolean("result")
            if (jsonObject) {
                val userNamePref = context.getSharedPreferences("userName", Context.MODE_PRIVATE)
                userNamePref.edit().putString("userName", userName.toString()).apply()

                Navigation.findNavController(view)
                    .navigate(R.id.action_signupFragment_to_statisticsCoronaFragment)
            } else
                Toast.makeText(context, "این نام کاربری وجود دارد", Toast.LENGTH_SHORT).show()
        }, {
            Toast.makeText(context, "مشکل اتصال اینترنت!", Toast.LENGTH_SHORT).show()
            return@JsonObjectRequest
        })
        requestQueue.add(jsonObjectRequest)
    }

    fun loginUser(context: Context, view: View, userName: Editable, password: Editable) {
        val loginUserAPI =
            "http://192.168.43.121:5000/login_user?username=$userName&password=$password"
        val requestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, loginUserAPI, null, {
            val jsonObject = it.getBoolean("result")
            if (jsonObject) {
                val userNamePref = context.getSharedPreferences("userName", Context.MODE_PRIVATE)
                userNamePref.edit().putString("userName", userName.toString()).apply()

                Navigation.findNavController(view)
                    .navigate(R.id.action_loginFragment_to_statisticsCoronaFragment)
            } else
                Toast.makeText(context, "نام کاربری و رمز اشتباه است", Toast.LENGTH_SHORT)
                    .show()

        }, {
            Toast.makeText(context, "اینترنت خود را بررسی کنید", Toast.LENGTH_SHORT)
                .show()
        })

        requestQueue.add(jsonObjectRequest)
    }

    fun deleteUser(context: Context, activity: Activity, userName: String) {
        val deleteUserAPI = "http://192.168.43.121:5000/delete_user?username=$userName"
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, deleteUserAPI, null, {
            val jsonObject = it.getBoolean("result")
            if (jsonObject) {
                activity.moveTaskToBack(true)
                activity.finish()
            }
        }, {
            Toast.makeText(context, "اینترنت خود را بررسی کنید", Toast.LENGTH_SHORT)
                .show()
        })
        requestQueue.add((jsonObjectRequest))
    }

    fun updateVirus(context: Context, virusItem: Int) {
        val deleteUserAPI =
            "http://192.168.43.121:5000/update_virus?username=admin&virus=${virusItem}"
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, deleteUserAPI, null, {
        }, {
            Toast.makeText(context, "اینترنت خود را بررسی کنید", Toast.LENGTH_SHORT)
                .show()
        })
        requestQueue.add((jsonObjectRequest))
    }

    fun getAllUsers(context: Context) {
        val deleteUserAPI = "http://192.168.43.121:5000/select_virus_information"
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, deleteUserAPI, null, {

            latitudes = it.getJSONArray("latitudes")
            longitudes = it.getJSONArray("longitudes")
            viruses = it.getJSONArray("viruses")

        }, {
            Toast.makeText(context, "اینترنت خود را بررسی کنید", Toast.LENGTH_SHORT).show()
        })
        requestQueue.add((jsonObjectRequest))
    }
}