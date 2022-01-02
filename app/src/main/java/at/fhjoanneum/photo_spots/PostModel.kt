package at.fhjoanneum.photo_spots

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class UploadPostModel(val Title:String, var Photo:String, val Description:String, val Location:String, val LocationLongitude:Double, val LocationLatitude:Double, val LocationAltitude:Double) {

}