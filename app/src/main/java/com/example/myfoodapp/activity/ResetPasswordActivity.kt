package com.example.myfoodapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myfoodapp.R
import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myfoodapp.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject


class ResetPasswordActivity : AppCompatActivity() {


    lateinit var etOTP: EditText
    lateinit var etEnterPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnSubmit: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)



        etOTP = findViewById(R.id.etOTP)
        etEnterPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmNewPassword)
        btnSubmit = findViewById(R.id.btnSubmit)


        btnSubmit.setOnClickListener {


            val otp = etOTP.text.toString()
            val password = etEnterPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()
            val mobile_number:String? = intent.getStringExtra("mobile_number")


            if (otp != "" && password != "" && confirmPassword != "") {
                if (password == confirmPassword) {
                    val queue = Volley.newRequestQueue(this@ResetPasswordActivity)
                    val url = "http://13.235.250.119/v2/reset_password/fetch_result"


                    val jsonObject = JSONObject()


                    jsonObject.put("mobile_number", mobile_number)
                    jsonObject.put("password", password)
                    jsonObject.put("otp", otp)


                    if (ConnectionManager().checkConnectivity(this@ResetPasswordActivity)) {
                        val jsonObjectRequest = object : JsonObjectRequest(
                            Method.POST, url, jsonObject,
                            Response.Listener {
                                try {
                                    val data = it.getJSONObject("data")
                                    val success = data.getBoolean("success")
                                    if (success) {
                                        val intent = Intent(
                                            this@ResetPasswordActivity,
                                            LoginActivity::class.java
                                        )
                                        Toast.makeText(
                                            this@ResetPasswordActivity,
                                            data.getString("successMessage"),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        startActivity(intent)
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this@ResetPasswordActivity,
                                            data.getString("errorMessage"),
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                } catch (e: JSONException) {
                                    Toast.makeText(
                                        this@ResetPasswordActivity,
                                        "Some Unexpected Error Occur",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            Response.ErrorListener {
                                if (this@ResetPasswordActivity != null) {
                                    Toast.makeText(
                                        this@ResetPasswordActivity,
                                        "Volley error occurred!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }) {
                            override fun getHeaders(): MutableMap<String, String> {
                                val headers = HashMap<String, String>()
                                headers["Content-type"] = "application/json"
                                headers["token"] = "16befab9b3c901"
                                return headers
                            }
                        }


                        queue.add(jsonObjectRequest)
                    } else {
                        val dialog = AlertDialog.Builder(this@ResetPasswordActivity)
                        dialog.setTitle("Error")
                        dialog.setMessage("Internet Connection Not Found")
                        dialog.setPositiveButton("open setting")
                        { text, listner ->
                            val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                            startActivity(settingIntent)
                            finish()
                        }
                        dialog.setNegativeButton("exit")
                        { text, listner ->
                            ActivityCompat.finishAffinity(this@ResetPasswordActivity)
                        }


                        dialog.create()
                        dialog.show()
                    }


                } else {
                    Toast.makeText(
                        this@ResetPasswordActivity,
                        "Password and confirm password is not same",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(this@ResetPasswordActivity, "Enter Details", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
        startActivity(intent)
        finish()


    }
}
