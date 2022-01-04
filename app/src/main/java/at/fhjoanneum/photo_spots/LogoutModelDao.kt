package at.fhjoanneum.photo_spots

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query


@Dao
interface LogoutModelDao {
    //@Delete
   // fun delete (logindata :LoginModel)
   @Query("DELETE FROM LoginModel")
   fun deleteAll()


}