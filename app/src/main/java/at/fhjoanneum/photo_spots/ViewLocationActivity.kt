package at.fhjoanneum.photo_spots

import android.R.attr
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import at.fhjoanneum.photo_spots.ui.dashboard.DashboardFragment
import at.fhjoanneum.photo_spots.ui.map.MapFragment
import com.bumptech.glide.Glide
import androidx.core.graphics.drawable.DrawableCompat

import android.R.attr.button

import android.graphics.drawable.Drawable
import android.content.res.ColorStateList







/*

 */

class ViewLocationActivity : AppCompatActivity() {

    lateinit var location: PostModel

    //private lateinit var locationList: List<PostModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_location)
        val bool = intent.getBooleanExtra(MapFragment.TAG_BOOL, false)

        val idFromMap = intent.getStringExtra(MapFragment.TAG_ID)?.toInt()
        val idFromDashboard = intent.getStringExtra(DashboardFragment.TAG_ID)?.toInt()

        if (idFromMap != null) {
            Toast.makeText(this, idFromMap.toString(), Toast.LENGTH_LONG).show()
            PostRepository.getphotoList(this,
                success = {
                    //location = it.filter {it.Id == idFromMap}[0]
                    if (it.find { it.Id == idFromMap } != null) {
                        location = it.find { it.Id == idFromMap}!!
                        setupPost(location)
                    }
                },
                error = {
                    Toast.makeText(this, "There was an Error!", Toast.LENGTH_LONG).show()
                    val mapIntent = Intent(this, MapFragment::class.java)
                    startActivity(mapIntent)
                }
            )
        } else if (idFromDashboard != null) {
            //Toast.makeText(this, idFromDashboard.toString(), Toast.LENGTH_LONG).show()
            PostRepository.getphotoList(this,
                success = {
                    if (it.find { it.Id == idFromDashboard } != null) {
                        location = it.find { it.Id == idFromDashboard}!!
                        setupPost(location)
                    }
                },
                error = {
                    Toast.makeText(this, "There was an Error!", Toast.LENGTH_LONG).show()
                    val dashboardIntent = Intent(this, DashboardFragment::class.java)
                    startActivity(dashboardIntent)
                }
            )
        }
    }

    fun setupPost(location: PostModel) {
        findViewById<TextView>(R.id.viewloc_textview_postuser).setText(location.UserName)
        findViewById<TextView>(R.id.viewloc_text_title).setText(location.Title)
        findViewById<TextView>(R.id.viewloc_textview_address).setText(location.getGPSData().Address)
        findViewById<RatingBar>(R.id.viewloc_ratingbar_rating).rating = location.getRating()
        findViewById<TextView>(R.id.viewloc_textview_postrating).text = location.Rating.size.toString()
        var catText = ""
        for (cat in location.Categories){
            catText = catText + "\n" + cat
        }
        findViewById<TextView>(R.id.viewloc_textview_cat1).text = catText
        val image = location.Photo
        Glide
            .with(this)
            .load(image)
            .into(findViewById(R.id.viewloc_imageview))

        //setupComments(location)
        val userHasRated = location.hasRated(this)
        if(userHasRated != null){
            if(userHasRated){
                findViewById<Button>(R.id.viewloc_button_like).backgroundTintList = getColorStateList(android.R.color.holo_purple)
                findViewById<Button>(R.id.viewloc_button_dislike).backgroundTintList = getColorStateList(android.R.color.background_dark)
            }else{
                findViewById<Button>(R.id.viewloc_button_dislike).backgroundTintList = getColorStateList(android.R.color.holo_purple)
                findViewById<Button>(R.id.viewloc_button_like).backgroundTintList = getColorStateList(android.R.color.background_dark)
            }
        }else{
            findViewById<Button>(R.id.viewloc_button_like).backgroundTintList = getColorStateList(android.R.color.background_dark)
            findViewById<Button>(R.id.viewloc_button_dislike).backgroundTintList = getColorStateList(android.R.color.background_dark)
        }

        findViewById<Button>(R.id.viewloc_button_like).setOnClickListener() {
            location.addPostRating(true,this)
            setupPost(location)
        }
        findViewById<Button>(R.id.viewloc_button_dislike).setOnClickListener() {
            location.addPostRating(false,this)
            setupPost(location)
        }

        if (location.Rating.filter { it.UserId == "username" }.isNotEmpty()) {
            findViewById<TextView>(R.id.viewloc_textview_postrating).setTextColor(getResources().getColor(R.color.design_default_color_secondary))
        }

        // share button
        findViewById<Button>(R.id.viewloc_button_share).setOnClickListener {
            val message = "Have a look at this nice PhotoSpot '" + location.Title + "' uploaded by " + location.UserName + "!\n" +
                    "Address:\n" +
                    location.getGPSData().Address +
                    "\nOpen with google maps:\n" +
                    "https://maps.google.com/maps?q=loc:" + location.getGPSData().Latitude + "," + location.getGPSData().Longitude

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }


    }

    fun setupComments(location: PostModel) {
        findViewById<Button>(R.id.viewloc_button_commentadd).setOnClickListener() {
            val commentText: String = findViewById<EditText>(R.id.viewloc_edittext_comment).text.toString()

            location.addComment(commentText)
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