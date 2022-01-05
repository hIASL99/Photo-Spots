package at.fhjoanneum.photo_spots

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.net.toUri
import com.bumptech.glide.Glide

class ViewLocation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_location)

        val id = intent.getStringExtra(MapsActivity.TAG_ID).toString()

        val location = LocationRepository.locationById(id)
        if (location != null) {
            findViewById<TextView>(R.id.viewloc_text_title).setText(location.title)
            findViewById<RatingBar>(R.id.viewloc_ratingbar_rating).rating = location.rating
            findViewById<TextView>(R.id.viewloc_textview_cat1).setText(location.Categories[0])
            findViewById<TextView>(R.id.viewloc_textview_cat2).setText(location.Categories[1])
            if (location.Categories[2] != "") {
                findViewById<TextView>(R.id.viewloc_textview_morecat).setText("and more...")
            }
            val imageUri: Uri = location.ImageUriString.toUri()
            Glide
                .with(this)
                .load(imageUri)
                .into(findViewById(R.id.viewloc_imageview))
        }



    }
}