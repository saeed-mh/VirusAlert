package mohammadi.saeed.virusalert

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.View
import android.widget.Toast
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import org.json.JSONArray
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule
import kotlin.jvm.internal.Intrinsics


class Requests {
    fun signupUser(context: Context, view: View, userName: Editable, password: Editable) {
        val createUserAPI =
            "http://192.168.43.121:5000/create_user?username=$userName&password=$password"
        val requestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, createUserAPI, null, {
            val jsonObject = it.getBoolean("result")
            if (jsonObject) {
//                val userNamePref = context.getSharedPreferences("userName", Context.MODE_PRIVATE)
//                userNamePref.edit().putString("userName", userName.toString()).apply()
                val sharedPrefData = SharedPrefData(context)
                sharedPrefData.setUserName(userName.toString())
                // for first signup the virus item equals 0
                updateVirus(context, sharedPrefData.getUserName(), 0)
                // for first signup latitude and longitude equals 0
                update_latitude_longitude(context, sharedPrefData.getUserName(), 0.0, 0.0)

                val nav = Navigation.findNavController(view)
                nav.navigate(SignupFragmentDirections.actionSignupFragmentToStatisticsCoronaFragment())
            } else
                Toast.makeText(context, "این نام کاربری وجود دارد", Toast.LENGTH_SHORT).show()
        }, {
            Toast.makeText(context, "خطا در اتصال به سرور", Toast.LENGTH_SHORT).show()
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
//                val userNamePref = context.getSharedPreferences("userName", Context.MODE_PRIVATE)
//                userNamePref.edit().putString("userName", userName.toString()).apply()
                val sharedPrefData = SharedPrefData(context)
                sharedPrefData.setUserName(userName.toString())

                val nav = Navigation.findNavController(view)
                nav.navigate(LoginFragmentDirections.actionLoginFragmentToStatisticsCoronaFragment())
            } else
                Toast.makeText(context, "نام کاربری و رمز اشتباه است", Toast.LENGTH_SHORT)
                    .show()

        }, {
            Toast.makeText(context, "خطا در اتصال به سرور", Toast.LENGTH_SHORT)
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

    fun updateVirus(context: Context, userName: String, virusItem: Int) {
        val deleteUserAPI =
            "http://192.168.43.121:5000/update_virus?username=${userName}&virus=${virusItem}"
        val requestQueue = Volley.newRequestQueue(context)
        val stringRequest = JsonObjectRequest(Request.Method.GET, deleteUserAPI, null, {
        }, {
            val sharedPrefData = SharedPrefData(context)
            sharedPrefData.setVirusItemSelected(virusItem)
            Toast.makeText(context, "اینترنت خود را بررسی کنید", Toast.LENGTH_SHORT).show()
        })
        requestQueue.add(stringRequest)
    }


    fun fetchUsersAndShowOnMap(context: Context, map: GoogleMap) {

        val deleteUserAPI = "http://192.168.43.121:5000/select_virus_information"
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, deleteUserAPI, null, {

            val latitudes = it.getJSONArray("latitudes").toArrayList()
            val longitudes = it.getJSONArray("longitudes").toArrayList()
            val viruses = it.getJSONArray("viruses").toArrayList()

            for (index in 0 until viruses.size) {
//                if (viruses[index].toIntOrNull() == null) {
//                    // do not anything
//                } else {
                if (viruses[index].toInt() != 0) {
                    val users = Users(
                        latitudes[index].toDouble(),
                        longitudes[index].toDouble(),
                        viruses[index].toInt()
                    )
                    when (users.virus) {
                        // CORONA
                        1 -> addUserOnMap(map, users.latitude, users.longitude, Color.GREEN)
                        // MEASLES
                        2 -> addUserOnMap(map, users.latitude, users.longitude, Color.RED)
                        // FLU
                        3 -> addUserOnMap(map, users.latitude, users.longitude, Color.BLUE)
                        // OTHER
                        4 -> addUserOnMap(map, users.latitude, users.longitude, Color.BLACK)
                    }
                }
//                }
            }

        }, {
            Toast.makeText(context, "مشکل در اتصال به سرور", Toast.LENGTH_SHORT).show()
        })
        requestQueue.add((jsonObjectRequest))
        map.clear()
    }

    private fun addUserOnMap(map: GoogleMap, latitude: Double, longitude: Double, colorUser: Int) {
        map.addCircle(
            CircleOptions()
                .center(LatLng(latitude, longitude))
                .radius(50.0)
                .fillColor(colorUser)
                .strokeColor(colorUser)
        )
    }

    private fun JSONArray.toArrayList(): ArrayList<String> {
        val list = arrayListOf<String>()
        for (i in 0 until this.length()) {
            list.add(this.getString(i))
        }
        return list
    }

    fun statisticsVirus(context: Context): ArrayList<String> {
        var viruses: ArrayList<String> = arrayListOf()
        val deleteUserAPI = "http://192.168.43.121:5000/select_virus_information"
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, deleteUserAPI, null, {
            viruses = it.getJSONArray("viruses").toArrayList()

        }) {
            Toast.makeText(context, "مشکل در اتصال به سرور", Toast.LENGTH_SHORT).show()
        }
        requestQueue.add((jsonObjectRequest))
        return viruses
    }

    fun update_latitude_longitude(
        context: Context,
        userName: String,
        latitude: Double,
        longitude: Double
    ) {
        val deleteUserAPI = "http://192.168.43.121:5000/update_latitude_longitude?username=$userName&latitude=$latitude&longitude=$longitude"
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, deleteUserAPI, null, {

        }) {
            Toast.makeText(context, "مشکل در اتصال به سرور", Toast.LENGTH_SHORT).show()
        }
        requestQueue.add((jsonObjectRequest))
    }


}