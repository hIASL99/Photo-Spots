package at.fhjoanneum.photo_spots

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import at.fhjoanneum.photo_spots.LoginModel

@Dao
interface LoginModelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(logindata: LoginModel)
}