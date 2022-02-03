package at.fhjoanneum.photo_spots

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import at.fhjoanneum.photo_spots.ui.dashboard.DashboardFragment
import at.fhjoanneum.photo_spots.ui.map.MapFragment
import com.bumptech.glide.Glide

class EditPostActivity : AppCompatActivity() {
    lateinit var edit_post: PostModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_post)

        val idFromMap = intent.getStringExtra(MapFragment.TAG_ID)?.toInt()
        val idFromDashboard = intent.getStringExtra(DashboardFragment.TAG_ID)?.toInt()

        if (idFromMap != null){
            getPostToEdit(idFromMap)
        }else if (idFromDashboard != null){
            getPostToEdit(idFromDashboard)
        }
    }

    fun getPostToEdit (id: Int){
        PostRepository.getPhotoByID(this,id,
            success = {setupUi(it)
                //location = it.filter {it.Id == idFromMap}[0]
                edit_post = it


            },
            error = {
                Toast.makeText(this, "There was an Error!", Toast.LENGTH_LONG).show()
                val mapIntent = Intent(this, MapFragment::class.java)
                startActivity(mapIntent)
            }
        )

    }

    fun setupUi(location:PostModel){
        val image = location.Photo
        Glide
            .with(this)
            .load(image)
            .into(findViewById(R.id.edit_viewpic_imageview))

        findViewById<TextView>(R.id.edit_viewpic_textview_address).setText(location.getGPSData().Address)

        var catText = ""
        for (cat in location.Categories){
            catText = catText + "\n" + cat
        }
        findViewById<TextView>(R.id.edit_viewpic_textview_cat1).text = catText

        findViewById<EditText>(R.id.edit_viewpic_edittext_title).text = location.Title.toEditable()
        findViewById<EditText>(R.id.edit_viewpic_edittext_description).text = location.Description.toEditable()

        findViewById<Button>(R.id.edit_viewpic_button_save).setOnClickListener(){
            edit_post.Title = findViewById<EditText>(R.id.edit_viewpic_edittext_title).text.toString()
            edit_post.Description = findViewById<EditText>(R.id.edit_viewpic_edittext_description).text.toString()

            PostRepository.changePost(this,edit_post,
                success = {//finish()

                },
                error = {
                    Toast.makeText(this, "There was an Error!", Toast.LENGTH_LONG).show()
                    val mapIntent = Intent(this, MapFragment::class.java)
                    startActivity(mapIntent)
                }
            )
        }
        findViewById<Button>(R.id.edit_delete_post).setOnClickListener(){
            PostRepository.deletePost(this,edit_post.Id,
                success = {//finish()


                },
                error = {
                    Toast.makeText(this, "There was an Error!", Toast.LENGTH_LONG).show()
                    val mapIntent = Intent(this, MapFragment::class.java)
                    startActivity(mapIntent)
                }
            )
        }

    }
}
fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)