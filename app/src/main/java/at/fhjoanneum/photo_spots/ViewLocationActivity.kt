package at.fhjoanneum.photo_spots

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.net.toUri
import com.bumptech.glide.Glide

/*

Todo: Get current username for
 Rating and Commenting Posts
 Checking if user has rated post already (colour of rating)
 Lines 42, 53, 63

 */

class ViewLocationActivity : AppCompatActivity() {
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
            val imageUri: Uri = location.Image.toUri()
            Glide
                .with(this)
                .load(imageUri)
                .into(findViewById(R.id.viewloc_imageview))

            findViewById<Button>(R.id.viewloc_button_back).setOnClickListener(){
                val mapIntent = Intent(this, MapsActivity::class.java)
                startActivity(mapIntent)
            }

            findViewById<Button>(R.id.viewloc_button_like).setOnClickListener() {
                if (location.addPostRating("username", true)) {
                    findViewById<TextView>(R.id.viewloc_textview_postrating).text = location.getPostRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_postrating).setTextColor(getResources().getColor(R.color.design_default_color_secondary))

                } else {
                    findViewById<TextView>(R.id.viewloc_textview_postrating).text = location.getPostRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_postrating).setTextColor(getResources().getColor(R.color.design_default_color_primary))
                }

            }
            findViewById<Button>(R.id.viewloc_button_dislike).setOnClickListener() {
                if (location.addPostRating("username", false)) {
                    findViewById<TextView>(R.id.viewloc_textview_postrating).text = location.getPostRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_postrating).setTextColor(getResources().getColor(R.color.design_default_color_secondary))

                } else {
                    findViewById<TextView>(R.id.viewloc_textview_postrating).text = location.getPostRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_postrating).setTextColor(getResources().getColor(R.color.design_default_color_primary))
                }
            }
            findViewById<TextView>(R.id.viewloc_textview_postrating).text = location.getPostRating().toString()
            if (location.postRating.filter { it.first == "username" }.isNotEmpty()) {
                findViewById<TextView>(R.id.viewloc_textview_postrating).setTextColor(getResources().getColor(R.color.design_default_color_secondary))
            }

            findViewById<Button>(R.id.viewloc_button_commentadd).setOnClickListener() {
                val commentText: String = findViewById<EditText>(R.id.viewloc_edittext_comment).text.toString()
                location.addComment("username", commentText)
                val topComment1 = location.getTopComments()[1]
                val topComment2 = location.getTopComments()[0]
                findViewById<TextView>(R.id.viewloc_textview_topcomment_username1).text = topComment1.username
                findViewById<TextView>(R.id.viewloc_textview_topcomment_username2).text = topComment2.username

                findViewById<TextView>(R.id.viewloc_textview_topcomment_comment1).text = topComment1.content
                findViewById<TextView>(R.id.viewloc_textview_topcomment_comment2).text = topComment2.content

                findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).text = topComment1.getCommentRating().toString()
                findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).text = topComment2.getCommentRating().toString()
                if (topComment2.username != "") {
                    findViewById<Button>(R.id.viewloc_button_topcomment_plus1).visibility = View.VISIBLE
                    findViewById<Button>(R.id.viewloc_button_topcomment_minus1).visibility = View.VISIBLE
                    findViewById<Button>(R.id.viewloc_button_topcomment_plus2).visibility = View.VISIBLE
                    findViewById<Button>(R.id.viewloc_button_topcomment_minus2).visibility = View.VISIBLE

                } else {
                    findViewById<Button>(R.id.viewloc_button_topcomment_plus1).visibility = View.VISIBLE
                    findViewById<Button>(R.id.viewloc_button_topcomment_minus1).visibility = View.VISIBLE
                }
                findViewById<EditText>(R.id.viewloc_edittext_comment).setText("")
            }

            val topComment1 = location.getTopComments()[1]
            val topComment2 = location.getTopComments()[0]

            if (topComment2.username != "") {
                findViewById<TextView>(R.id.viewloc_textview_topcomment_username1).text = topComment1.username
                findViewById<TextView>(R.id.viewloc_textview_topcomment_username2).text = topComment2.username

                findViewById<TextView>(R.id.viewloc_textview_topcomment_comment1).text = topComment1.content
                findViewById<TextView>(R.id.viewloc_textview_topcomment_comment2).text = topComment2.content

                findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).text = topComment1.getCommentRating().toString()
                findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).text = topComment2.getCommentRating().toString()
            } else if (topComment1.username != "") {
                findViewById<TextView>(R.id.viewloc_textview_topcomment_username1).text = topComment1.username
                findViewById<TextView>(R.id.viewloc_textview_topcomment_username2).text = topComment2.username

                findViewById<TextView>(R.id.viewloc_textview_topcomment_comment1).text = topComment1.content
                findViewById<TextView>(R.id.viewloc_textview_topcomment_comment2).text = topComment2.content

                findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).text = topComment1.getCommentRating().toString()
                findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).text = ""

                findViewById<Button>(R.id.viewloc_button_topcomment_plus2).visibility = View.GONE
                findViewById<Button>(R.id.viewloc_button_topcomment_minus2).visibility = View.GONE
            } else {
                findViewById<TextView>(R.id.viewloc_textview_topcomment_username1).text = topComment1.username
                findViewById<TextView>(R.id.viewloc_textview_topcomment_username2).text = topComment2.username

                findViewById<TextView>(R.id.viewloc_textview_topcomment_comment1).text = topComment1.content
                findViewById<TextView>(R.id.viewloc_textview_topcomment_comment2).text = topComment2.content

                findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).text = ""
                findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).text = ""

                findViewById<Button>(R.id.viewloc_button_topcomment_plus1).visibility = View.GONE
                findViewById<Button>(R.id.viewloc_button_topcomment_minus1).visibility = View.GONE
                findViewById<Button>(R.id.viewloc_button_topcomment_plus2).visibility = View.GONE
                findViewById<Button>(R.id.viewloc_button_topcomment_minus2).visibility = View.GONE


            }

            findViewById<Button>(R.id.viewloc_button_topcomment_plus1).setOnClickListener() {
                if (topComment1.addPostCommentRating("username", true)) {
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).text = topComment1.getCommentRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).setTextColor(getResources().getColor(R.color.design_default_color_secondary))

                } else {
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).text = topComment1.getCommentRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).setTextColor(getResources().getColor(R.color.design_default_color_primary))
                }
            }

            findViewById<Button>(R.id.viewloc_button_topcomment_minus1).setOnClickListener() {
                if (topComment1.addPostCommentRating("username", false)) {
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).text = topComment1.getCommentRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).setTextColor(getResources().getColor(R.color.design_default_color_secondary))

                } else {
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).text = topComment1.getCommentRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating1).setTextColor(getResources().getColor(R.color.design_default_color_primary))
                }
            }

            findViewById<Button>(R.id.viewloc_button_topcomment_plus2).setOnClickListener() {
                if (topComment2.addPostCommentRating("username", true)) {
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).text = topComment2.getCommentRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).setTextColor(getResources().getColor(R.color.design_default_color_secondary))

                } else {
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).text = topComment2.getCommentRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).setTextColor(getResources().getColor(R.color.design_default_color_primary))
                }
            }

            findViewById<Button>(R.id.viewloc_button_topcomment_minus2).setOnClickListener() {
                if (topComment2.addPostCommentRating("username", false)) {
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).text = topComment2.getCommentRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).setTextColor(getResources().getColor(R.color.design_default_color_secondary))

                } else {
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).text = topComment2.getCommentRating().toString()
                    findViewById<TextView>(R.id.viewloc_textview_topcomment_rating2).setTextColor(getResources().getColor(R.color.design_default_color_primary))
                }
            }

        }





    }
}