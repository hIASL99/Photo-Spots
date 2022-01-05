package at.fhjoanneum.photo_spots

import com.squareup.moshi.JsonClass
import java.util.*

@JsonClass(generateAdapter = true)
class UploadPostModel(val Title:String, var Photo:String, val Description:String, val Location:String, val LocationLongitude:Double, val LocationLatitude:Double, val LocationAltitude:Double) {

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

                //val Categories : List<String>,
                //val CreatedDate : Date,
                //val Rating : Float

                ) {

}