package at.fhjoanneum.photo_spots

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
class LoginModel(val access_token:String, val token_type:String, val userName:String) {
}