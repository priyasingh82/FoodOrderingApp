package com.example.myfoodapp.activity

/*import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myfoodapp.R

class LoginActivity: AppCompatActivity() {
    lateinit var etMobileNumber: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgotPassword: TextView
    lateinit var txtRegisterYourself: TextView

    val validMobileNumber = "8837772754"
    val validPassword = "Priya"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.login_page)



        title = "Log In"

        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgotPassword = findViewById(R.id.txtForgotPassword)
        txtRegisterYourself = findViewById(R.id.txtRegister)

        btnLogin.setOnClickListener {

            val mobileNumber = etMobileNumber.text.toString()

            val password = etPassword.text.toString()



            val intent = Intent(this@LoginActivity, MainActivity::class.java)

            if ((mobileNumber == validMobileNumber && password == validPassword)) {

               startActivity(intent)
                finish()

            } else {

                Toast.makeText(this@LoginActivity, "Incorrect Mobile Number or Password", Toast.LENGTH_LONG).show()

            }
        }


        txtForgotPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(intent)
        }

        txtRegisterYourself.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

    }




}*/


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.myfoodapp.R
import com.example.myfoodapp.util.ConnectionManager
import org.json.JSONObject
import java.lang.Exception


class LoginActivity : AppCompatActivity() {
    lateinit var etMobile:EditText
    lateinit var etPassword:EditText
    lateinit var btnLogin:Button
    lateinit var txtForgot:TextView
    lateinit var txtSign:TextView
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)
        etMobile=findViewById(R.id.etMobileNumber)
        etPassword=findViewById(R.id.etPassword)
        btnLogin=findViewById(R.id.btnLogin)
        txtForgot=findViewById(R.id.txtForgotPassword)
        txtSign=findViewById(R.id.txtRegister)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)


        btnLogin.setOnClickListener {
            if (ConnectionManager().checkConnectivity(this)) {
                val queue=Volley.newRequestQueue(this@LoginActivity)
                val url="http://13.235.250.119/v2/login/fetch_result"
                val jsonParams=JSONObject()
                jsonParams.put("mobile_number",etMobile.text.toString())
                jsonParams.put("password",etPassword.text.toString())
                val jsonObjectRequest = object :
                    JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                        try {
                            val data = it.getJSONObject("data")
                            val success=data.getBoolean("success")
                            println("Response is $it")

                            if (success) {

                                Toast.makeText(
                                    this@LoginActivity,
                                    "Login Successfully",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val user_data = data.getJSONObject("data")
                                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                                setSharedPreferences(user_data)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "Incorrect username or password",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } catch (e: Exception) {
                            println("error is $it")
                            Toast.makeText(
                                this@LoginActivity,

                                "Volley error occured",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }, Response.ErrorListener {
                        Toast.makeText(this@LoginActivity, "Some error occured", Toast.LENGTH_SHORT)
                            .show()
                    }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val header=HashMap<String,String>()
                        header["Content-type"]="application/json"
                        header["token"]="16befab9b3c901"
                        return header
                    }
                }
                queue.add(jsonObjectRequest)
            }
            else
            {
                Toast.makeText(this@LoginActivity,"some error occured",Toast.LENGTH_SHORT).show()
            }
        }
        txtForgot.setOnClickListener {
            val intent=Intent(this@LoginActivity,ForgotPasswordActivity::class.java)
            startActivity(intent)
        }
        txtSign.setOnClickListener {
            val intent=Intent(this@LoginActivity,RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    fun setSharedPreferences(user_data: JSONObject) {


        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()


        sharedPreferences.edit()
            .putString("u_id", user_data.getString("user_id"))
            .apply()


        sharedPreferences.edit()
            .putString("u_name", user_data.getString("name"))
            .apply()


        sharedPreferences.edit()
            .putString("u_email", user_data.getString("email"))
            .apply()


        sharedPreferences.edit()
            .putString("u_mobile_number", user_data.getString("mobile_number"))
            .apply()


        sharedPreferences.edit()
            .putString("u_address", user_data.getString("address"))
            .apply()
    }
}

