package at.fhjoanneum.photo_spots

import com.squareup.moshi.Moshi
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*


object Api {
    //const val accessToken = ”your_access_token"
    val retrofit: Retrofit
    val retrofitService: ApiService
    init {
        val moshi = Moshi.Builder().build()
        retrofit = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://photospot.matthiaswindisch.eu")
            .build()
        retrofitService = retrofit.create(ApiService::class.java)
    }
}
interface ApiService {

    //User Account
    @FormUrlEncoded
    @POST("/token")
    fun login(@Field("grant_type") grant_type:String = "password",
              @Field("username") username:String,
              @Field("password") password:String): Call<LoginModel>;
    @POST("/API/Account/Register")
    fun register(@Body registerData :RegisterModel) :Call<Unit>

    @POST("API/Account/Logout")
    fun logout(@Header("Authorization") token: String):Call<Unit>

    @GET("api/account/userinfo")
    fun getUserInfo(@Header("Authorization") token: String):Call<Unit>

    //Photos
    @POST("/API/Photos")
    fun uploadPost(@Body uploadPostModel: UploadPostModel,@Header("Authorization") token: String):Call<Unit>

    @Multipart
    @POST("API/Picture")
    fun uploadPicture(@Part image: MultipartBody.Part, @Header("Authorization") token: String):Call<String>

    @GET("Api/photos")
    fun getPhotos(@Header("Authorization") token: String):Call<List<PostModel>>
}

