package at.fhjoanneum.photo_spots

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Register : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        findViewById<Button>(R.id.register_btn_register).setOnClickListener{
            val email = findViewById<EditText>(R.id.register_txt_email).text.toString()
            val username = findViewById<EditText>(R.id.register_txt_username).text.toString()
            val password = findViewById<EditText>(R.id.register_txt_pw).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.register_txt_pw_confirm).text.toString()

            val registerData = RegisterModel(email, username, password, confirmPassword)
            registerPressed(registerData)
        }

    }
    private fun registerPressed(registerData :RegisterModel){
        RegisterNewUser(registerData,
            success = {
                // handle success




                val intent = Intent(this, Login::class.java)
                startActivity(intent)
            },
            error = {
                // handle error
                Log.e("API","ERROR")
                val toast = Toast.makeText(applicationContext, it, Toast.LENGTH_LONG)
                toast.show()
            }
        )
    }
}

