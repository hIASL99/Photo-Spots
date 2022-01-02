package at.fhjoanneum.photo_spots

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        findViewById<Button>(R.id.login_btn_login).setOnClickListener {

            val username = findViewById<EditText>(R.id.login_input_email).text.toString()
            val password = findViewById<EditText>(R.id.login_input_password).text.toString()
            if(username.isNotEmpty() && password.isNotEmpty()){
                loginPressed(username, password)
            }else{
                val toast = Toast.makeText(applicationContext, "Please enter your E-Mail and Password", Toast.LENGTH_SHORT)
                toast.show()
            }



        }
        findViewById<Button>(R.id.login_btn_register).setOnClickListener {
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }



    }

    private fun loginPressed(username:String, password:String){
        GetLoginToken(username,password,
            success = {
                // handle success

                // Token needs to be stored locally
                storeLoginData(this, it)

                // Go to Main Activity
                //Log.e("API",it.access_token.toString())
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            },
            error = {
                // handle error
                Log.e("API","ERROR")
                val toast = Toast.makeText(applicationContext, "Username or Password are invalid, please try again", Toast.LENGTH_LONG)
                toast.show()
            }
        )
    }

    private fun storeLoginData(context: Context, loginData: LoginModel) {
        val applicationContext = context.applicationContext
        val db = LoginModelDatabase.getDatabase(applicationContext)
        db.loginModelDao.insert(loginData)
    }


    override fun onStart() {
        super.onStart()
        Log.w("MyActivity", "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.w("MyActivity", "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.w("MyActivity", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.w("MyActivity", "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("MyActivity", "onDestroy")
    }
}