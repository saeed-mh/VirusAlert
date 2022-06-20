package mohammadi.saeed.virusalert

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import mohammadi.saeed.virusalert.model.EVirusType
import mohammadi.saeed.virusalert.model.SharedPrefData
import mohammadi.saeed.virusalert.model.Users
import org.json.JSONArray


class Requests {
    fun signupUser(context: Context, view: View, userName: Editable, password: Editable) {
        val createUserAPI =
            "http://192.168.43.121:5000/create_user?username=$userName&password=$password"
        val requestQueue = Volley.newRequestQueue(context)

        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, createUserAPI, null, {
            val jsonObject = it.getBoolean("result")
            if (jsonObject) {
                val sharedPrefData = SharedPrefData(context)
                sharedPrefData.setUserName(userName.toString())
                // for first signup the virus item equals 0
                updateVirus(context, sharedPrefData.getUserName(), 0)
                // for first signup latitude and longitude equals 0
                updateLatitudeLongitude(context, sharedPrefData.getUserName(), 0.0, 0.0)

                val nav = Navigation.findNavController(view)
                nav.navigate(SignupFragmentDirections.actionSignupFragmentToMapFragment())
                sharedPrefData.setLogged(true)
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
                val sharedPrefData = SharedPrefData(context)
                sharedPrefData.setUserName(userName.toString())

                val nav = Navigation.findNavController(view)
                nav.navigate(LoginFragmentDirections.actionLoginFragmentToMapFragment())
                sharedPrefData.setLogged(true)

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
                val sharedPrefData = SharedPrefData(context)
                sharedPrefData.setLogged(false)
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
                if (viruses[index].toInt() != 0) {
                    val user = Users(latitudes[index].toDouble(), longitudes[index].toDouble(), viruses[index].toInt())
                    when (user.virus) {
                        // CORONA
                        1 -> addUserOnMap(map, user.latitude, user.longitude, Color.parseColor(/* GREEN */"#6600ff00"))
                        // MEASLES
                        2 -> addUserOnMap(map, user.latitude, user.longitude, Color.parseColor(/* RED */"#66ff0000"))
                        // FLU
                        3 -> addUserOnMap(map, user.latitude, user.longitude, Color.parseColor(/* BLUE */"#660000ff"))
                        // OTHER
                        4 -> addUserOnMap(map, user.latitude, user.longitude, Color.parseColor(/* BLACK */"#66000000"))
                    }
                }
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

    fun updateLatitudeLongitude(
        context: Context,
        userName: String,
        latitude: Double,
        longitude: Double
    ) {
        val deleteUserAPI =
            "http://192.168.43.121:5000/update_latitude_longitude?username=$userName&latitude=$latitude&longitude=$longitude"
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, deleteUserAPI, null, {

        }) {
            Toast.makeText(context, "مشکل در اتصال به سرور", Toast.LENGTH_SHORT).show()
        }
        requestQueue.add((jsonObjectRequest))
    }

    fun getCountCorona(context: Context, virusType: EVirusType, txtViewCountCorona: TextView) {
        var getCountVirusAPI = ""
        getCountVirusAPI += when (virusType) {
            EVirusType.corona -> {
                "http://192.168.43.121:5000/count_corona"
            }
            EVirusType.measles -> {
                "http://192.168.43.121:5000/count_measles"
            }
            EVirusType.flu -> {
                "http://192.168.43.121:5000/count_flu"
            }
            EVirusType.otherViruses -> {
                "http://192.168.43.121:5000/count_other_viruses"
            }
        }
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, getCountVirusAPI, null, {
            val count = it.getInt("result")
            txtViewCountCorona.text = "تعداد $count"
            Log.d("getCountCoronaTAG", "getCountCorona: $count")
        }) {
            Toast.makeText(context, "مشکل در اتصال به سرور", Toast.LENGTH_SHORT).show()
        }
        requestQueue.add((jsonObjectRequest))
    }

    fun getVirusByUsername(context: Context, userName: String, radioGroup: RadioGroup, btnApplyChange: Button) {
        val getVirusByUsernameAPI = "http://192.168.43.121:5000/get_virus_by_name?username=${userName}"
        val requestQueue = Volley.newRequestQueue(context)
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, getVirusByUsernameAPI, null, {
            when (it.getInt("result")) {
                0 -> radioGroup.check(R.id.radioButton0)
                1 -> radioGroup.check(R.id.radioButton1)
                2 -> radioGroup.check(R.id.radioButton2)
                3 -> radioGroup.check(R.id.radioButton3)
                4 -> radioGroup.check(R.id.radioButton4)
            }
            btnApplyChange.isEnabled = false

        }, {
            Toast.makeText(context, "مشکل در اتصال به سرور", Toast.LENGTH_SHORT).show()
        })
        requestQueue.add((jsonObjectRequest))
    }
}