package at.fhjoanneum.photo_spots

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES

class SettingsActivity : AppCompatActivity() {

    companion object{
        val DARKMODE = "DARKMODE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //username
        oldUsername()


        //darkmode
        val darkModeView = findViewById<Switch>(R.id.settings_swi_darkmode)
        val sharedPreferences = getSharedPreferences(packageName, Context.MODE_PRIVATE)

        val darkModeStored =  sharedPreferences.getBoolean(DARKMODE, false)

        if (darkModeStored !=darkModeView.isChecked ) {
            darkModeView.isChecked = darkModeStored
        }
        findViewById<Button>(R.id.settings_btn_save).setOnClickListener {

            //change username
            val newUsername = findViewById<EditText>(R.id.settings_txt_newUsername).text.toString()
            if (newUsername.isNotEmpty()) {
               savePressed(newUsername)
                findViewById<TextView>(R.id.settings_txt_oldusername).text = newUsername
            }
           //darkmode
            val darkMode = darkModeView.isChecked
            sharedPreferences.edit().putBoolean(DARKMODE, darkMode).apply()
                if (darkMode) AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)
                else AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

            }


    }
    private fun savePressed(username :String){
        val usernameModel= ChangeUserName(username = username)
        PutUserName(this, usernameModel,
            success = {
                // handle success
                Toast.makeText(applicationContext, "Username successfully changed", Toast.LENGTH_SHORT).show()
              //  val intent = Intent(this, LoginActivity::class.java)
               // startActivity(intent)
            },
            error = {
                // handle error
                Log.e("API",it)
                val toast = Toast.makeText(applicationContext, it, Toast.LENGTH_LONG)
                toast.show()
            }
        )
    }

    private fun oldUsername(){
          GetUserName(this,
            success = {
                // handle success
            findViewById<TextView>(R.id.settings_txt_oldusername).text = it

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