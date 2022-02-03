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
    fun getphotoList(context: Context, success: (post: List<PostModel>) -> Unit, error: (errorMessage: String) -> Unit) {
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

    fun getMyPhotoList(context: Context, success: (post: List<PostModel>) -> Unit, error: (errorMessage: String) -> Unit) {
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
    fun getPhotoByID(context: Context,id: Int,  success: (post: PostModel) -> Unit, error: (errorMessage: String) -> Unit) {
        val token = getLoginToken(context)
        Api.retrofitService.getPhotoById(id.toString(), token).enqueue(object : Callback<PostModel> {
            override fun onFailure(call: Call<PostModel>, t: Throwable) {
                error("The call failed")
            }

            override fun onResponse(call: Call<PostModel>, response: Response<PostModel>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success(responseBody)
                } else {
                    error(response.message().toString())
                }
            }

        })
    }
    fun changePost(context: Context,postToEdit: PostModel,  success: () -> Unit, error: (errorMessage: String) -> Unit) {
        val token = getLoginToken(context)
        Api.retrofitService.changePostByID(postToEdit, token).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                error("The call failed")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success()
                } else {
                    error(response.message().toString())
                }
            }

        })
    }
    fun deletePost(context: Context,id: Int,  success: () -> Unit, error: (errorMessage: String) -> Unit) {
        val token = getLoginToken(context)
        Api.retrofitService.deletePhotoById(id.toString(), token).enqueue(object : Callback<Unit> {
            override fun onFailure(call: Call<Unit>, t: Throwable) {
                error("The call failed")
            }

            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    success()
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
