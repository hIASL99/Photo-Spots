package at.fhjoanneum.photo_spots

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.net.toUri
import at.fhjoanneum.photo_spots.ui.dashboard.DashboardFragment
import at.fhjoanneum.photo_spots.ui.map.MapFragment
import com.bumptech.glide.Glide

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
        //Toast.makeText(this, "idFromMap: ${idFromMap.toString()}, idFromDashboard: ${idFromDashboard.toString()}", Toast.LENGTH_LONG).show()





        if (idFromMap != null) {
            Toast.makeText(this, idFromMap.toString(), Toast.LENGTH_LONG).show()
            PostRepository.getphotoList(this,
                success = {
                    location = it.filter {it.Id == idFromMap}[0]
                    //Toast.makeText(this, location.Title, Toast.LENGTH_LONG).show()
                    val post = UploadPostModel2(location.Id.toString(),
                        location.Title, location.Rating,
                        location.Description,
                        GpsDataModel(location.LocationAltitude,
                            location.LocationLatitude,
                            location.LocationLongitude,
                            location.Location),
                        location.Categories,
                        location.Photo,
                        mutableListOf<PostComment>(),
                        mutableListOf<Pair<String, Boolean>>(),
                        location.UserName
                    )
                    setupPost(post)
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
                    location = it.filter {it.Id == idFromDashboard}[0]
                    //Toast.makeText(this, location.Title, Toast.LENGTH_LONG).show()
                    val post = UploadPostModel2(location.Id.toString(),
                        location.Title, location.Rating,
                        location.Description,
                        GpsDataModel(location.LocationAltitude,
                            location.LocationLatitude,
                            location.LocationLongitude,
                            location.Location),
                        location.Categories,
                        location.Photo,
                        mutableListOf<PostComment>(),
                        mutableListOf<Pair<String, Boolean>>(),
                        location.UserName
                    )
                    setupPost(post)
                },
                error = {
                    Toast.makeText(this, "There was an Error!", Toast.LENGTH_LONG).show()
                    val dashboardIntent = Intent(this, DashboardFragment::class.java)
                    startActivity(dashboardIntent)
                }
            )
        }
    }

    fun setupPost(location: UploadPostModel2) {
        findViewById<TextView>(R.id.viewloc_textview_postuser).setText(location.username)
        findViewById<TextView>(R.id.viewloc_text_title).setText(location.title)
        findViewById<TextView>(R.id.viewloc_textview_address).setText(location.gpsData.Address)
        findViewById<RatingBar>(R.id.viewloc_ratingbar_rating).rating = location.rating
        when {
            location.categories.size == 0 -> {
                findViewById<TextView>(R.id.viewloc_textview_cat1).setText("(no categories inserted)")
                findViewById<TextView>(R.id.viewloc_textview_cat2).setText("")
                findViewById<TextView>(R.id.viewloc_textview_morecat).setText("")
            }
            location.categories.size == 1 -> {
                findViewById<TextView>(R.id.viewloc_textview_cat1).setText(location.categories[0])
                findViewById<TextView>(R.id.viewloc_textview_cat2).setText("")
                findViewById<TextView>(R.id.viewloc_textview_morecat).setText("")
            }
            location.categories.size == 2 -> {
                findViewById<TextView>(R.id.viewloc_textview_cat1).setText(location.categories[0])
                findViewById<TextView>(R.id.viewloc_textview_cat2).setText(location.categories[1])
                findViewById<TextView>(R.id.viewloc_textview_morecat).setText("")
            }
            location.categories.size > 2 -> {
                findViewById<TextView>(R.id.viewloc_textview_cat1).setText(location.categories[0])
                findViewById<TextView>(R.id.viewloc_textview_cat2).setText(location.categories[1])
                findViewById<TextView>(R.id.viewloc_textview_morecat).setText("and more...")
            }

        }
        val image = location.image
        Glide
            .with(this)
            .load(image)
            .into(findViewById(R.id.viewloc_imageview))

        setupComments(location)
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

        // share button
        findViewById<Button>(R.id.viewloc_button_share).setOnClickListener {
            val message = "Have a look at this nice PhotoSpot '" + location.title + "' uploaded by " + location.username + "!\n" +
                    "Address:\n" +
                    location.gpsData.Address +
                    "\nOpen with google maps:\n" +
                    "https://maps.google.com/maps?q=loc:" + location.gpsData.Latitude + "," + location.gpsData.Longitude

            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, message)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }


    }

    fun setupComments(location: UploadPostModel2) {
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