package at.fhjoanneum.photo_spots

import android.content.Context
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PostRepository {
    private val posts: List<PostModel>

    init{
        posts = listOf<PostModel>()
    }
    fun getphotoList(context: Context, success: (lessonList: List<PostModel>) -> Unit, error: (errorMessage: String) -> Unit) {
        val token = getLoginToken(context)
        Api.retrofitService.getPhotos(token).enqueue(object : Callback<List<PostModel>> {
            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                error("The call failed")
            }

            override fun onResponse(call: Call<List<PostModel>>, response: Response<List<PostModel>>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(responseBody)
                } else {
                    error(response.message().toString())
                }
            }

        })
    }

    fun getMyPhotoList(context: Context, success: (lessonList: List<PostModel>) -> Unit, error: (errorMessage: String) -> Unit) {
        val token = getLoginToken(context)
        Api.retrofitService.getMyPhotos(token).enqueue(object : Callback<List<PostModel>> {
            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                error("The call failed")
            }

            override fun onResponse(call: Call<List<PostModel>>, response: Response<List<PostModel>>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(responseBody)
                } else {
                    error(response.message().toString())
                }
            }

        })
    }

    private fun getLoginToken(context: Context):String {
        val applicationContext = context.applicationContext
        val db = LoginModelDatabase.getDatabase(applicationContext)
        return "bearer " + db.loginModelDao.getLoginToken().toString()
    }
}