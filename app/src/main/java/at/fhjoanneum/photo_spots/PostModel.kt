package at.fhjoanneum.photo_spots

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.squareup.moshi.JsonClass
import org.w3c.dom.Comment
import java.util.*

@JsonClass(generateAdapter = true)
class UploadPostModel(val Title:String, var Photo:String, val Description:String, val Categories: List<String>, val Location:String, val LocationLongitude:Double, val LocationLatitude:Double, val LocationAltitude:Double) {
}
@JsonClass(generateAdapter = true)
class Rating(val UserId: String, val Rating: Boolean){}
@JsonClass(generateAdapter = true)
class UploadRatingModel(val Rating: Boolean, val PostId:Int){}


@JsonClass(generateAdapter = true)
class PostModel(val Id:Int,
                val UserId:String,
                var Title:String,
                val Photo:String,
                var Description:String,
                val Location:String,
                val LocationLongitude:Double,
                val LocationLatitude:Double,
                val LocationAltitude:Double,
                val UserName:String,
                val Categories : List<String>,
                var Rating: MutableList<Rating>,
                var Comments: MutableList<PostComment>

) {
    fun getGPSData():GpsDataModel{
        return GpsDataModel(LocationAltitude,
            LocationLatitude,
            LocationLongitude,
            Location)
    }
    fun getRating():Float{


        var result = 0.0f
        if (Rating.size == 0){
            return result
        }

        for (i in Rating){
            if (i.Rating){
                result += 1
            }
        }
        if (Rating.size == 0) {
            return result
        }else{
            return (result / Rating.size) * 5
        }
    }
    fun addPostRating(rating: Boolean, context: Context){

        val db = LoginModelDatabase.getDatabase(context)
        val username = db.loginModelDao.getUserName().toString()

        val ratingToUpload = UploadRatingModel(rating, Id)
        var result = false

        if(Rating.filter { it.UserId == username }.isEmpty()) {
            Rating.add(Rating(username, rating))
            result = true
        } else if (Rating.filter { it.UserId == username && it.Rating != rating }.isNotEmpty()) {
            Rating.removeAll { e -> e.UserId == username }
            Rating.add(Rating(username, rating))
            result = true
        } else if (Rating.filter {it.UserId == username && it.Rating == rating }.isNotEmpty()) {
            Rating.removeAll { e -> e.UserId == username }
            result = false
        }

        if(result){
            postRating(context,ratingToUpload,
                success = {
                    // handle success
                    Rating = it.toMutableList()
                },
                error = {
                    // handle error
                    Log.e("API",it)
                }
            )
        }else {
            deleteRating(context, ratingToUpload,
                success = {
                    // handle success
                    Rating = it.toMutableList()
                },
                error = {
                    // handle error
                    Log.e("API", it)
                }
            )
        }
    }
    fun hasRated(context: Context):Boolean?{
        val db = LoginModelDatabase.getDatabase(context)
        val username = db.loginModelDao.getUserName().toString()

        if(Rating.filter { it.UserId == username }.isEmpty()) {
            return null
        } else if (Rating.filter { it.UserId == username }.isNotEmpty()) {
            return Rating.filter { it.UserId == username }.first().Rating
        }
        return false
    }
    fun getComments():String{
        var comments = ""
        for (comment in Comments) {
            comments = comments + comment.UserName +" "+ comment.DatePosted + ":\n" + comment.Text + "\n\n"
        }
        return comments
    }
    fun addComment(comment:String, context: Context){

        val db = LoginModelDatabase.getDatabase(context)
        val username = db.loginModelDao.getUserName().toString()
        Comments.add(PostComment(username, comment))

        postComment(context,UploadPostComment(Text = comment, PostId = Id, UserId = "Insert User Id"),
            success = {
                // handle success
                Comments = it.toMutableList()
            },
            error = {
                // handle error
                Log.e("API",it)
            }
        )

    }
}
@JsonClass(generateAdapter = true)
class GpsDataModel (val Altitude: Double, val Latitude: Double, val Longitude: Double, val Address: String)

@JsonClass(generateAdapter = true)
class PostComment (val UserName: String, val Text: String, val DatePosted:String = "Now") {
}

@JsonClass(generateAdapter = true)
class UploadPostComment (val UserId: String, val Text: String,val PostId: Int) {
}

@JsonClass(generateAdapter = true)
class CategoryModel(val Title:String) { }

