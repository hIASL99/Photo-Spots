package at.fhjoanneum.photo_spots

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.MutableLiveData

class LogoutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_logout2)



    }
}

public fun DeleteLoginData(context: Context) {
    val applicationContext = context.applicationContext
    val db = LoginModelDatabase.getDatabase(applicationContext)
    db.logoutModelDao.deleteAll()
}

