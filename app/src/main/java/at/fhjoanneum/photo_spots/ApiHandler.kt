package at.fhjoanneum.photo_spots

import android.content.Context
import android.util.Log
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


fun GetLoginToken(username:String,password:String,success: (loginData: LoginModel) -> Unit, error: (errorMessage: String) -> Unit) {
    Api.retrofitService.login("password",username,password) .enqueue(object:
        Callback<LoginModel> {
        override fun onFailure(call: Call<LoginModel>, t: Throwable) {
            error("The call failed")
        }

        override fun onResponse(call: Call<LoginModel>, response: Response<LoginModel>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success(responseBody)
            } else {
                error("Something went wrong")
            }
        }

    })
}

fun RegisterNewUser (registerData :RegisterModel ,success: () -> Unit, error: (errorMessage: String) -> Unit) {
    Api.retrofitService.register(registerData) .enqueue(object:
        Callback<Unit> {
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

fun UploadNewPost (uploadPostModel: UploadPostModel, context: Context ,success: () -> Unit, error: (errorMessage: String) -> Unit) {
    val token = getLoginToken(context)
    Api.retrofitService.uploadPost(uploadPostModel, token) .enqueue(object:
        Callback<Unit> {
        override fun onFailure(call: Call<Unit>, t: Throwable) {
            error("The call failed")
            Log.e("APICALL", "TEST")
        }

        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success()
            } else {
                Log.e("APICALL", response.message())
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

fun UploadPicture (picture: MultipartBody.Part, context: Context, success: (path: String) -> Unit, error: (errorMessage: String) -> Unit) {
    val token = getLoginToken(context)
    Api.retrofitService.uploadPicture(picture, token).enqueue(object:
        Callback<String> {
        override fun onFailure(call: Call<String>, t: Throwable) {
            error("The call failed")
            Log.e("APICALL", "TEST")
        }

        override fun onResponse(call: Call<String>, response: Response<String>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success(responseBody.toString())
            } else {
                Log.e("APICALL", response.message())
                error(response.code().toString())
            }
        }

    })
}

fun LogoutUser (context: Context, error: (errorMessage: String) -> Unit) {
    val token = getLoginToken(context)
    Api.retrofitService.logout(token) .enqueue(object:
        Callback<Unit> {
        override fun onFailure(call: Call<Unit>, t: Throwable) {
            error("The call failed")
        }

        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            val responseBody = response.body()
            if (!response.isSuccessful || responseBody == null) {
                error("Something went wrong")
            }
        }

    })
}
fun GetUserName (context: Context, success: (UserName:String) -> Unit, error: (errorMessage: String) -> Unit) {
    val token = getLoginToken(context)
    Api.retrofitService.getUserName(token) .enqueue(object:
        Callback<String>{
        override fun onFailure(call: Call<String>, t: Throwable) {
            error("The call failed")
        }

        override fun onResponse(call: Call<String>, response: Response<String>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success(responseBody)
            } else {
                Log.e("APICALL", response.message())
                error(response.message().toString())
            }
        }

    })
}
fun PutUserName (context: Context,UserName:ChangeUserName, success: () -> Unit, error: (errorMessage: String) -> Unit) {
    val token = getLoginToken(context)
    Api.retrofitService.putUserName(UserName, token) .enqueue(object:
        Callback<Unit>{
        override fun onFailure(call: Call<Unit>, t: Throwable) {
            error("The call failed")
        }

        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success()
            } else {
                Log.e("APICALL", response.message())
                error(response.message().toString())
            }
        }

    })
}

fun ChangePassword (changePasswordModel: ChangePasswordModel, context: Context ,success: () -> Unit, error: (errorMessage: String) -> Unit) {
    val token = getLoginToken(context)
    Api.retrofitService.changePassword(changePasswordModel, token) .enqueue(object:
        Callback<Unit> {
        override fun onFailure(call: Call<Unit>, t: Throwable) {
            error("The call failed")
            Log.e("APICALL", "TEST")
        }

        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success()
            } else {
                Log.e("APICALL", response.message())
                error(response.message().toString())
            }
        }

    })
}
fun getUserInfo (context: Context, success: () -> Unit, error: (errorMessage: String) -> Unit) {
    val token = getLoginToken(context)
    Api.retrofitService.getUserInfo(token) .enqueue(object:
        Callback<Unit> {
        override fun onFailure(call: Call<Unit>, t: Throwable) {
            error("The call failed")
        }

        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success()
            } else {
                Log.e("APICALL", response.message())
                error(response.message().toString())
            }
        }

    })
}

fun getCategories (context: Context, success: (categories: List<String>) -> Unit, error: (errorMessage: String) -> Unit) {
    val token = getLoginToken(context)
    Api.retrofitService.getCategories(token) .enqueue(object:
        Callback<List<CategoryModel>> {
        override fun onFailure(call: Call<List<CategoryModel>>, t: Throwable) {
            error("The call failed")
        }

        override fun onResponse(call: Call<List<CategoryModel>>, response: Response<List<CategoryModel>>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val returnValue = mutableListOf<String>()
                for (category in responseBody) {
                    returnValue.add(category.Title)
                }
                success(returnValue)
            } else {
                Log.e("APICALL", response.message())
                error(response.message().toString())
            }
        }

    })
}
fun postRating (context: Context,rating:UploadRatingModel, success: (ratings: List<Rating>) -> Unit, error: (errorMessage: String) -> Unit){
    val token = getLoginToken(context)
    Api.retrofitService.uploadRating(rating,token) .enqueue(object:
        Callback<List<Rating>> {
        override fun onFailure(call: Call<List<Rating>>, t: Throwable) {
            error("The call failed")
        }

        override fun onResponse(call: Call<List<Rating>>, response: Response<List<Rating>>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success(responseBody)
            } else {
                Log.e("APICALL", response.message())
                error(response.message().toString())
            }
        }

    })
}
fun deleteRating (context: Context,rating:UploadRatingModel, success: (ratings: List<Rating>) -> Unit, error: (errorMessage: String) -> Unit){
    val token = getLoginToken(context)
    Api.retrofitService.deleteRating(rating.PostId.toString(),token) .enqueue(object:
        Callback<List<Rating>> {
        override fun onFailure(call: Call<List<Rating>>, t: Throwable) {
            error("The call failed")
        }

        override fun onResponse(call: Call<List<Rating>>, response: Response<List<Rating>>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success(responseBody)
            } else {
                Log.e("APICALL", response.message())
                error(response.message().toString())
            }
        }

    })
}
fun postComment (context: Context,comment:UploadPostComment, success: (ratings: List<PostComment>) -> Unit, error: (errorMessage: String) -> Unit){
    val token = getLoginToken(context)
    Api.retrofitService.uploadComment(comment,token) .enqueue(object:
        Callback<List<PostComment>> {
        override fun onFailure(call: Call<List<PostComment>>, t: Throwable) {
            error("The call failed")
        }

        override fun onResponse(call: Call<List<PostComment>>, response: Response<List<PostComment>>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success(responseBody)
            } else {
                Log.e("APICALL", response.message())
                error(response.message().toString())
            }
        }

    })
}
