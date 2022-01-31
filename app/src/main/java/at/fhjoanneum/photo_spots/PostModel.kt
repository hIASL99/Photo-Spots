package at.fhjoanneum.photo_spots

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.squareup.moshi.JsonClass
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
                val Title:String,
                val Photo:String,
                val Description:String,
                val Location:String,
                val LocationLongitude:Double,
                val LocationLatitude:Double,
                val LocationAltitude:Double,
                val UserName:String,
                val Categories : List<String>,
                var Rating: MutableList<Rating>
                //val comments: MutableList<PostComment>

) {
    fun getGPSData():GpsDataModel{
        return GpsDataModel(LocationAltitude,
            LocationLatitude,
            LocationLongitude,
            Location)
    }
    fun getRating():Float{
        var result = 0.0f
        for (i in Rating){
            if (i.Rating){
                result += 1
            }
        }
        return (result / Rating.size) * 5
    }
    fun addPostRating(rating: Boolean, context: Context):Boolean{

        val db = LoginModelDatabase.getDatabase(context)
        val username = db.loginModelDao.getUserName().toString()

        val ratingToUpload = UploadRatingModel(rating, Id)
        var result = false

        if(Rating.filter { it.UserId == username }.isEmpty()) {
            Rating.add(Rating(username, rating))
            result = true
        } else if (Rating.filter { it.UserId == username && it.Rating != rating }.isNotEmpty()) {
            Rating.remove(Rating(username, !rating))
            Rating.add(Rating(username, rating))
            result = true
        } else if (Rating.filter {it.UserId == username && it.Rating == rating }.isNotEmpty()) {
            Rating.remove(Rating(username, rating))
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
        }else{
            deleteRating(context,ratingToUpload,
                success = {
                    // handle success
                    Rating = it.toMutableList()
                },
                error = {
                    // handle error
                    Log.e("API",it)
                }
            )
        }

        return result
    }
    fun addComment(comment:String){
        //TODO implementation

    }
    fun getTopComments():List<PostComment>{
        return listOf<PostComment>()
    }
}

/*
@JsonClass(generateAdapter = true)
class DetailPostModel(val id: String, val title: String, val rating: Float, val description: String, val gpsData: GpsDataModel,
                      val categories: List<String>, val image: String, val comments: MutableList<PostComment>,
                      val postRating: MutableList<Pair<String, Boolean>>, val username: String) {

    fun addComment(username: String, comment: String) {
        comments.add(PostComment(username, comment, mutableListOf<Pair<String, Boolean>>()))
    }

    fun addPostRating(username: String, rating: Boolean) :Boolean {
        if(postRating.filter { it.first == username }.isEmpty()) {
            postRating.add(Pair(username, rating))
            return true
        } else if (postRating.filter { it.first == username && it.second != rating }.isNotEmpty()) {
            postRating.remove(Pair(username, !rating))
            postRating.add(Pair(username, rating))
            return true
        } else if (postRating.filter {it.first == username && it.second == rating }.isNotEmpty()) {
            postRating.remove(Pair(username, rating))
            return false
        }
        return false
    }

    fun getPostRating() :Int {
        val numLike = postRating.map {it.second}.filter {it}.size
        val numDislike = postRating.map {it.second}.filter {!it}.size
        return  numLike - numDislike
    }

    fun getTopComments() :List<PostComment> {
        val emptyList = mutableListOf<PostComment>(PostComment("", "", mutableListOf()), PostComment("", "", mutableListOf()))
        if (comments.isEmpty()) {
            return emptyList
        } else if (comments.size == 1) {
            return mutableListOf(PostComment("", "", mutableListOf()), comments[0])
        }
        return comments.sortedBy {it.getCommentRating()}.takeLast(2)


    }
}*/
@JsonClass(generateAdapter = true)
class GpsDataModel (val Altitude: Double, val Latitude: Double, val Longitude: Double, val Address: String)

@JsonClass(generateAdapter = true)
class PostComment (val username: String, val content: String, val postCommentRating: MutableList<Pair<String, Boolean>>) {
    fun addPostCommentRating(otherUsername: String, rating: Boolean) :Boolean {
        if(postCommentRating.filter { it.first == otherUsername }.isEmpty()) {
            postCommentRating.add(Pair(otherUsername, rating))
            return true
        } else if (postCommentRating.filter { it.first == otherUsername && it.second != rating }.isNotEmpty()) {
            postCommentRating.remove(Pair(otherUsername, !rating))
            postCommentRating.add(Pair(otherUsername, rating))
            return true
        } else if (postCommentRating.filter {it.first == otherUsername && it.second == rating }.isNotEmpty()) {
            postCommentRating.remove(Pair(otherUsername, rating))
            return false
        }
        return false
    }

    fun getCommentRating() :Int {
        val numLike = postCommentRating.map {it.second}.filter {it}.size
        val numDislike = postCommentRating.map {it.second}.filter {!it}.size
        return  numLike - numDislike
    }


}



@JsonClass(generateAdapter = true)
class CategoryModel(val Title:String) { }

