package at.fhjoanneum.photo_spots

import android.content.Context
import android.util.Log
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File


fun GetLoginToken(username:String,password:String,success: (loginData: LoginModel) -> Unit, error: (errorMessage: String) -> Unit) {
    LoginApi.retrofitService.login("password",username,password) .enqueue(object:
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

fun RegisterNewUser (registerData :RegisterModel ,success: (registerData: RegisterModel) -> Unit, error: (errorMessage: String) -> Unit) {
    LoginApi.retrofitService.register(registerData) .enqueue(object:
        Callback<Unit> {
        override fun onFailure(call: Call<Unit>, t: Throwable) {
            error("The call failed")
        }

        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success(registerData)
            } else {
                error("Something went wrong")
            }
        }

    })
}

fun UploadNewPost (uploadPostModel: UploadPostModel, context: Context ,success: (uploadData: UploadPostModel) -> Unit, error: (errorMessage: String) -> Unit) {
    val token = getLoginToken(context)
    LoginApi.retrofitService.uploadPost(uploadPostModel, token) .enqueue(object:
        Callback<Unit> {
        override fun onFailure(call: Call<Unit>, t: Throwable) {
            error("The call failed")
            Log.e("APICALL", "TEST")
        }

        override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                success(uploadPostModel)
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
    LoginApi.retrofitService.uploadPicture(picture, token).enqueue(object:
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
                error(response.message().toString())
            }
        }

    })
}