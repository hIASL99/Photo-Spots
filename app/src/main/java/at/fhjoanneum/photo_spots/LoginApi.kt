package at.fhjoanneum.photo_spots

import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


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
    @FormUrlEncoded
    @POST("/token")
    fun login(@Field("grant_type") grant_type:String = "password",
              @Field("username") username:String,
              @Field("password") password:String): Call<LoginModel>;
    @POST("/API/Account/Register")
    fun register(@Body registerData :RegisterModel) :Call<Unit>

}

