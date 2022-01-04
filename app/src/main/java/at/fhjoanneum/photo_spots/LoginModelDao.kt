package at.fhjoanneum.photo_spots

import android.content.Context
import androidx.annotation.RequiresPermission
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import at.fhjoanneum.photo_spots.LoginModel
import retrofit2.http.GET

@Dao
interface LoginModelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(logindata: LoginModel)

    @Query("SELECT access_token FROM LoginModel")
    fun getLoginToken():String?

    @Query("DELETE FROM LoginModel")
    fun deleteAll()
}




