package at.fhjoanneum.photo_spots

import android.content.Context
import com.squareup.moshi.Moshi
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*
import java.io.File


object LoginApi {
    //const val accessToken = ”your_access_token"
    val retrofit: Retrofit
    val retrofitService: LoginApiService
    init {
        val moshi = Moshi.Builder().build()
        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://photospot.matthiaswindisch.eu")
            .build()
        retrofitService = retrofit.create(LoginApiService::class.java)
    }
}
interface LoginApiService {
    /*@GET("/lessons")
    @Headers(”X-API-KEY: ${LessonApi.accessToken}")
    fun lessons(): Call<List<Lesson>>
    @POST("/lessons/{id}/rate")
    @Headers(”X-API-KEY: ${LessonApi.accessToken}")
    fun rateLesson(@Path("id") lessonId: String, @Body rating: LessonRating): Call<Unit>*/

    //User Account
    @FormUrlEncoded
    @POST("/token")
    fun login(@Field("grant_type") grant_type:String = "password",
              @Field("username") username:String,
              @Field("password") password:String): Call<LoginModel>;
    @POST("/API/Account/Register")
    fun register(@Body registerData :RegisterModel) :Call<Unit>

    //Photos
    @POST("/API/Photos")
    fun uploadPost(@Body uploadPostModel: UploadPostModel,@Header("Authorization") token: String):Call<Unit>

    @Multipart
    @POST("API/Picture")
    fun uploadPicture(@Part image: MultipartBody.Part, @Header("Authorization") token: String):Call<String>
}

