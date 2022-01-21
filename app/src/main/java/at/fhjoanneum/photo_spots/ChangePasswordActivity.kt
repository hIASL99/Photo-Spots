package at.fhjoanneum.photo_spots

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class ChangePasswordActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)

        findViewById<Button>(R.id.changepw_btn_savepw).setOnClickListener {

            val oldPassword = findViewById<EditText>(R.id.changpw_txt_oldpw).text.toString()
            val newPassword = findViewById<EditText>(R.id.changpw_txt_newpw).text.toString()
            val confirmPassword = findViewById<EditText>(R.id.changpw_txt_confpw).text.toString()

            if (oldPassword == newPassword){
                Toast.makeText(applicationContext, "New password is same as the old password", Toast.LENGTH_SHORT).show()
            }else if(newPassword != confirmPassword){
                Toast.makeText(applicationContext, "New password and Confirm Password need to be the same", Toast.LENGTH_SHORT).show()
            }else {

                val pwdata = ChangePasswordModel(oldPassword, newPassword, confirmPassword)
                savePressed(pwdata)
            }
        }
    }

    private fun savePressed(pwdata: ChangePasswordModel){
        ChangePassword(pwdata,this,
            success = {
                // handle success
                Toast.makeText(applicationContext, "Password successfully changed", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            },
            error = {
                // handle error
                Log.e("API", "ERROR")
                val toast = Toast.makeText(applicationContext, it, Toast.LENGTH_LONG)
                toast.show()
            }
        )
    }
}