package at.fhjoanneum.photo_spots

import android.content.Context
import android.widget.Toast
import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
class UploadPostModel(val Title:String, var Photo:String, val Description:String, val Location:String, val LocationLongitude:Double, val LocationLatitude:Double, val LocationAltitude:Double) {
}
@JsonClass(generateAdapter = true)
class UploadPostModel2(val id: String, val title: String, val rating: Float, val description: String, val GpsData: GpsDataModel, val Categories: List<String>, val Image: String, val comments: MutableList<PostComment>, val postRating: MutableList<Pair<String, Boolean>>) {
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

}
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
class PostModel(val Id:Int,
                val UserId:String,
                val Title:String,
                val Photo:String,
                val Description:String,
                val Location:String,
                val LocationLongitude:Double,
                val LocationLatitude:Double,
                val LocationAltitude:Double,
                val UserName:String
                //val Categories : List<String>,
                //val CreatedDate : Date,
                //val Rating : Float

                ) {

}

