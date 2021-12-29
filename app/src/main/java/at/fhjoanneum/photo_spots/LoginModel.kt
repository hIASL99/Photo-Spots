package at.fhjoanneum.photo_spots

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.JsonClass

@Entity
@JsonClass(generateAdapter = true)
class LoginModel(val access_token:String, val token_type:String, @PrimaryKey val userName:String) {
}

class RegisterModel(val email :String, val username :String, val password :String, val confirmPassword :String) {

}