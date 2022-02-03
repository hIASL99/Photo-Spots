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

    @GET("api/account/username")
    fun getUserName(@Header("Authorization") token: String):Call<String>

    @PUT("api/account/username")
    fun putUserName(@Body username: ChangeUserName, @Header("Authorization") token: String):Call<Unit>

    @POST("API/Account/ChangePassword")
    fun changePassword(@Body changePasswordData: ChangePasswordModel,@Header("Authorization") token: String):Call<Unit>

    //Photos
    @POST("/API/Photos")
    fun uploadPost(@Body uploadPostModel: UploadPostModel,@Header("Authorization") token: String):Call<Unit>

    @Multipart
    @POST("API/Picture")
    fun uploadPicture(@Part image: MultipartBody.Part, @Header("Authorization") token: String):Call<String>

    @GET("Api/photos")
    fun getPhotos(@Header("Authorization") token: String):Call<List<PostModel>>

    @GET("Api/photos/myPhotos")
    fun getMyPhotos(@Header("Authorization") token: String):Call<List<PostModel>>

    @GET("Api/category")
    fun getCategories(@Header("Authorization") token: String):Call<List<CategoryModel>>

    @GET("Api/photos/{id}")
    fun getPhotoById(@Path("id") id: String,@Header("Authorization") token: String):Call<PostModel>

    @DELETE("Api/photos/{id}")
    fun deletePhotoById(@Path("id") id: String,@Header("Authorization") token: String):Call<Unit>

    @PUT("Api/photos")
    fun changePostByID(@Body postToEdit: PostModel, @Header("Authorization") token: String):Call<Unit>


    //rating
    @POST("/API/Rating")
    fun uploadRating(@Body uploadRatingModel: UploadRatingModel,@Header("Authorization") token: String):Call<List<Rating>>

    @DELETE("/API/Rating/{id}/")
    fun deleteRating(@Path("id") id: String,@Header("Authorization") token: String):Call<List<Rating>>

    //Comments
    @POST("/API/Comment")
    fun uploadComment(@Body comment: UploadPostComment,@Header("Authorization") token: String):Call<List<PostComment>>
}

