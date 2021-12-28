package at.fhjoanneum.photo_spots

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


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
